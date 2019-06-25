package com.iaoe.jwExp.service.impl;

import java.io.File;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.iaoe.jwExp.dao.ShopDao;
import com.iaoe.jwExp.dto.ShopExecution;
import com.iaoe.jwExp.entity.Shop;
import com.iaoe.jwExp.enums.ShopStateEnum;
import com.iaoe.jwExp.exceptions.ShopOperationException;
import com.iaoe.jwExp.service.ShopService;
import com.iaoe.jwExp.util.ImageUtil;
import com.iaoe.jwExp.util.PathUtil;

@Service
public class ShopServiceImpl implements ShopService{
	@Autowired
	private ShopDao shopDao;
	
	//使用Transactional保证事务的原子性
	@Override
	@Transactional
	public ShopExecution addShop(Shop shop, File shopImg) {
		//空值判断
		if(shop==null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP_INFO);
		}
		try {
			//给店铺信息赋初始值
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			int effectedNum = shopDao.insertShop(shop);
			if(effectedNum<=0) {
				//这里要用RuntimeException才能保证原子性
				throw new ShopOperationException("店铺创建失败");
			} else {
				if(shopImg!=null) {
					try {
						//如果参数时引用类型的话，那么传递的是指针
						addShopImg(shop,shopImg);
					}catch(Exception e) {
						throw new ShopOperationException("addShopImg error:"+e.getMessage());
					}
					//更新店铺地址
					effectedNum = shopDao.updateShop(shop);
					if(effectedNum<=0) {
						throw new ShopOperationException("更新图片地址失败");
					}
				}
			}
		} catch(Exception e) {
			throw new ShopOperationException("addShop error:"+e.getMessage());
		}
		return new ShopExecution(ShopStateEnum.CHECK,shop);
	}
	//添加图片
	private void addShopImg(Shop shop,File shopImg) {
		//获取shop图片的相对值路径
		String dest = PathUtil.getShopImagePath(shop.getShopId());
		String shopImageAddr = ImageUtil.generateThumbnail(shopImg, dest);
		shop.setShopImg(shopImageAddr);
	}
}
