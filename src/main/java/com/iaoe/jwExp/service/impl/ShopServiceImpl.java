package com.iaoe.jwExp.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iaoe.jwExp.dao.ShopDao;
import com.iaoe.jwExp.dto.ImageHolder;
import com.iaoe.jwExp.dto.ShopExecution;
import com.iaoe.jwExp.entity.Shop;
import com.iaoe.jwExp.enums.ShopStateEnum;
import com.iaoe.jwExp.exceptions.ShopOperationException;
import com.iaoe.jwExp.service.ShopService;
import com.iaoe.jwExp.util.ImageUtil;
import com.iaoe.jwExp.util.PageCalculator;
import com.iaoe.jwExp.util.PathUtil;

@Service
public class ShopServiceImpl implements ShopService {
	@Autowired
	private ShopDao shopDao;

	// 使用Transactional保证事务的原子性
	@Override
	@Transactional
	public ShopExecution addShop(Shop shop, ImageHolder thumbnail)
			throws ShopOperationException {
		// 空值判断
		if (shop == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP_INFO);
		}
		try {
			// 给店铺信息赋初始值
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			int effectedNum = shopDao.insertShop(shop);
			if (effectedNum <= 0) {
				// 这里要用RuntimeException才能保证原子性
				throw new ShopOperationException("店铺创建失败");
			} else {
				if (thumbnail.getImage() != null) {
					try {
						// 如果参数时引用类型的话，那么传递的是指针
						addShopImg(shop, thumbnail);
					} catch (Exception e) {
						throw new ShopOperationException("addShopImg error:" + e.getMessage());
					}
					// 更新店铺地址
					effectedNum = shopDao.updateShop(shop);
					if (effectedNum <= 0) {
						throw new ShopOperationException("更新图片地址失败");
					}
				}
			}
		} catch (Exception e) {
			throw new ShopOperationException("addShop error:" + e.getMessage());
		}
		return new ShopExecution(ShopStateEnum.CHECK, shop);
	}

	// 添加图片
	private void addShopImg(Shop shop, ImageHolder thumbnail) {
		// 获取shop图片的相对值路径
		String dest = PathUtil.getShopImagePath(shop.getShopId());
		String shopImageAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		shop.setShopImg(shopImageAddr);
	}

	// 根据店铺id返回店铺信息
	@Override
	public Shop getByShopId(long shopId) {
		return shopDao.queryByShopId(shopId);
	}

	@Override
	public ShopExecution modifyShop(Shop shop, ImageHolder thumbnail)
			throws ShopOperationException {
		if (shop == null || shop.getShopId() == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP_INFO);
		} else {
			try {
			// 1.判断是否需要处理图片
			if (thumbnail.getImage() != null && thumbnail.getImageName() != null && !"".equals(thumbnail.getImageName())) {
				Shop tempShop = shopDao.queryByShopId(shop.getShopId());
				// 删除原来的图片
				if (tempShop.getShopImg() != null) {
					ImageUtil.deleteFileOrPath(tempShop.getShopImg());
				}
				// 添加图片
				addShopImg(shop, thumbnail);
			}
			// 2.更新店铺消息
			shop.setLastEditTime(new Date());
			int effectedNum = shopDao.updateShop(shop);
			if (effectedNum <= 0) {
				//更新失败返回错误信息
				return new ShopExecution(ShopStateEnum.INNER_ERROR);
			} else {
				//更新成功
				shop = shopDao.queryByShopId(shop.getShopId());
				return new ShopExecution(ShopStateEnum.SUCCESS, shop);
			}
			}catch (Exception e) {
				throw new ShopOperationException("modifyShp error:" + e.getMessage());
			}
		}
	}

	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
		//转换页数和页内结构大小为行数
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		//Dao层查询结构列表和总数
		List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		int count = shopDao.queryShopCount(shopCondition);
		//将总数置入和店铺列表注入到里面
		ShopExecution se = new ShopExecution();
		if(shopList!=null) {
			se.setShopList(shopList);
			se.setCount(count);
		}
		else {
			se.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return se;
	}
}
