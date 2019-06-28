package com.iaoe.jwExp.dao;

import com.iaoe.jwExp.entity.Product;

public interface ProductDao {
	/**
	 * 插入商品
	 * @param product
	 * @return
	 */
	int insertProduct(Product product);	
}
