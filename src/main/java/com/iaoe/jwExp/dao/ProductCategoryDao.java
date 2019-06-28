package com.iaoe.jwExp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

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
	 * @param ProductCategory productCategory
	 * @return effectedNum
	 */
	int batchInsertProductCategory(List<ProductCategory> productCategoryList);

	/**
	 * 删除指定的商品类别，传入两个是为了更加安全的删除
	 * 
	 * @param productCategoryId
	 * @param shopId
	 * @return effectedNum
	 */
	int deleteProductCategory(@Param("productCategoryId") long productCategoryId, @Param("shopId") long shopId);
}
