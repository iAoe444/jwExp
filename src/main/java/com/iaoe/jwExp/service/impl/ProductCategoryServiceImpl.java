package com.iaoe.jwExp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iaoe.jwExp.dao.ProductCategoryDao;
import com.iaoe.jwExp.dto.ProductCategoryExecution;
import com.iaoe.jwExp.entity.ProductCategory;
import com.iaoe.jwExp.enums.ProductCategoryStateEnum;
import com.iaoe.jwExp.exceptions.ProductCategoryOperationException;
import com.iaoe.jwExp.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService{
	@Autowired
	private ProductCategoryDao productCategoryDao;

	@Override
	public List<ProductCategory> getProductCategoryList(long shopId) {
		return productCategoryDao.queryByShopId(shopId);
	}

	@Override
	@Transactional
	public ProductCategoryExecution batchAddProductCategory(
			List<ProductCategory> productCategoryList) throws ProductCategoryOperationException {
		//空值判断
		if (productCategoryList != null && productCategoryList.size() > 0) {
			try {
				int effectedNum = productCategoryDao
						.batchInsertProductCategory(productCategoryList);
				if (effectedNum <= 0) {
					throw new RuntimeException("店铺类别创建失败");
				} else {
					return new ProductCategoryExecution(
							ProductCategoryStateEnum.SUCCESS);
				}
			} catch (Exception e) {
				throw new ProductCategoryOperationException("batchAddProductCategory error: "
						+ e.getMessage());
			}
		} else {
			return new ProductCategoryExecution(
					ProductCategoryStateEnum.EMPTY_LIST);
		}
	}

	@Override
	@Transactional
	public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException {
		// TODO 将此商品类别下的商品的类别Id置为空
		try {
			int effectedNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
			if(effectedNum <=0 ) {
				throw new ProductCategoryOperationException("商品类别删除失败");
			}else {
				return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
			}
		}catch(Exception e) {
			throw new ProductCategoryOperationException("deleteProutCategory error:"+e.getMessage());
		}
	}
	
	
}
