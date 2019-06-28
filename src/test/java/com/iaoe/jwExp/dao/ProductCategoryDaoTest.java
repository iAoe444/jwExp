package com.iaoe.jwExp.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.iaoe.jwExp.BaseTest;
import com.iaoe.jwExp.entity.ProductCategory;

public class ProductCategoryDaoTest extends BaseTest{
	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Test
	public void testQueryByShopId() {
		long shopId = 2;
		List<ProductCategory> pc = productCategoryDao.queryByShopId(shopId);
		System.err.println("店铺为的商品类别的数量"+pc.size());
	}
}
