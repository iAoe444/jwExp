package com.iaoe.jwExp.service;

import java.util.List;

import com.iaoe.jwExp.dto.ProductCategoryExecution;
import com.iaoe.jwExp.entity.ProductCategory;
import com.iaoe.jwExp.exceptions.ProductCategoryOperationException;

public interface ProductCategoryService {
	/**
	 * 通过shopId查询店铺下的所有商品类别信息
	 * @param shopId
	 * @return
	 */
	List<ProductCategory> getProductCategoryList(long shopId);
	
	/**
	 * 批量添加商品分类
	 * @param productCategoryList
	 * @return
	 * @throws ProductCategoryOperationException
	 */
	ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException;
	
	/**
	 * 删除商品分类
	 * 将此类别下的商品里的类别id置为空，再删除掉该商品类别
	 * @param productCategoryId
	 * @param shopId
	 * @return
	 * @throws RuntimeException
	 */
	ProductCategoryExecution deleteProductCategory(long productCategoryId,
			long shopId) throws ProductCategoryOperationException;
}
