package com.iaoe.jwExp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iaoe.jwExp.dao.ProductCategoryDao;
import com.iaoe.jwExp.entity.ProductCategory;
import com.iaoe.jwExp.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService{
	@Autowired
	private ProductCategoryDao productCategoryDao;

	@Override
	public List<ProductCategory> getProductCategoryList(long shopId) {
		return productCategoryDao.queryByShopId(shopId);
	}
}
