package com.iaoe.jwExp.service;

import java.util.List;

import com.iaoe.jwExp.entity.ProductCategory;

public interface ProductCategoryService {
	/**
	 * 通过shopId查询店铺下的所有商品类别信息
	 * @param shopId
	 * @return
	 */
	List<ProductCategory> getProductCategoryList(long shopId);
}
