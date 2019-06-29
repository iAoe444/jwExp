package com.iaoe.jwExp.dao;

import com.iaoe.jwExp.entity.Product;

public interface ProductDao {
	/**
	 * 插入商品
	 * @param product
	 * @return
	 */
	int insertProduct(Product product);	

	/**
	 * 通过ProductId查询唯一的商品信息
	 * @param productId
	 * @return
	 */
	Product queryProductById(long productId);
	
	/**
	 * 更新商品信息
	 * @param product
	 * @return
	 */
	int updateProduct(Product product);
}
