package com.iaoe.jwExp.service;

import java.io.InputStream;

import com.iaoe.jwExp.dto.ShopExecution;
import com.iaoe.jwExp.entity.Shop;
import com.iaoe.jwExp.exceptions.ShopOperationException;

public interface ShopService {
	/**
	 * 通过店铺ID返回店铺信息
	 * @param shopId
	 * @return
	 */
	Shop getByShopId(long shopId);
	
	/**
	 * 更新店铺信息，包括对图片的处理
	 * @param shop
	 * @param shopImgInputStream
	 * @param fileName
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream,String fileName) throws ShopOperationException;
	
	/**
	 * 添加店铺操作，包括对图片的处理
	 * @param shop
	 * @param shopImgInputStream
	 * @param fileName
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution addShop(Shop shop, InputStream shopImgInputStream,String fileName) throws ShopOperationException;
}
