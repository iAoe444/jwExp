package com.iaoe.jwExp.service.impl;

import java.io.InputStream;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iaoe.jwExp.dao.ShopDao;
import com.iaoe.jwExp.dto.ShopExecution;
import com.iaoe.jwExp.entity.Shop;
import com.iaoe.jwExp.enums.ShopStateEnum;
import com.iaoe.jwExp.exceptions.ShopOperationException;
import com.iaoe.jwExp.service.ShopService;
import com.iaoe.jwExp.util.ImageUtil;
import com.iaoe.jwExp.util.PathUtil;

@Service
public class ShopServiceImpl implements ShopService {
	@Autowired
	private ShopDao shopDao;

	// 使用Transactional保证事务的原子性
	@Override
	@Transactional
	public ShopExecution addShop(Shop shop, InputStream shopImgInputStream, String fileName)
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
				if (shopImgInputStream != null) {
					try {
						// 如果参数时引用类型的话，那么传递的是指针
						addShopImg(shop, shopImgInputStream, fileName);
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
	private void addShopImg(Shop shop, InputStream shopImgInputStream, String fileName) {
		// 获取shop图片的相对值路径
		String dest = PathUtil.getShopImagePath(shop.getShopId());
		String shopImageAddr = ImageUtil.generateThumbnail(shopImgInputStream, fileName, dest);
		shop.setShopImg(shopImageAddr);
	}

	// 根据店铺id返回店铺信息
	@Override
	public Shop getByShopId(long shopId) {
		return shopDao.queryByShopId(shopId);
	}

	@Override
	public ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream, String fileName)
			throws ShopOperationException {
		if (shop == null || shop.getShopId() == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP_INFO);
		} else {
			try {
			// 1.判断是否需要处理图片
			if (shopImgInputStream != null && fileName != null && !"".equals(fileName)) {
				Shop tempShop = shopDao.queryByShopId(shop.getShopId());
				// 删除原来的图片
				if (tempShop.getShopImg() != null) {
					ImageUtil.deleteFileOrPath(tempShop.getShopImg());
				}
				// 添加图片
				addShopImg(shop, shopImgInputStream, fileName);
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
}
