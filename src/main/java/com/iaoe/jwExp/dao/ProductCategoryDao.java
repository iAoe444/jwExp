package com.iaoe.jwExp.dao;

import java.util.List;

import com.iaoe.jwExp.entity.ProductCategory;

public interface ProductCategoryDao {
	/**
	 * 通过shopId 查询店铺的商品类别
	 * 
	 * @param long shopId
	 * @return List<ProductCategory>
	 */
	List<ProductCategory> queryByShopId(long shopId);
	
	/**
	 * 批量添加新增商品类别
	 * 
	 * @param ProductCategory
	 *            productCategory
	 * @return effectedNum
	 */
	int batchInsertProductCategory(List<ProductCategory> productCategoryList);
}
