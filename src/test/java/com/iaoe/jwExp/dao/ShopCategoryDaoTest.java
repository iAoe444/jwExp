package com.iaoe.jwExp.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.iaoe.jwExp.BaseTest;
import com.iaoe.jwExp.dao.ShopCategoryDao;
import com.iaoe.jwExp.entity.ShopCategory;

public class ShopCategoryDaoTest extends BaseTest{
	@Autowired
	private ShopCategoryDao shopCategoryDao;
	
	@Test
	public void testQueryShopCategory(){
		ShopCategory testCategory = new ShopCategory();
		ShopCategory parentCategory = new ShopCategory();
		//设置父类别的编号为1
		parentCategory.setShopCategoryId(1L);
		//设置该类别的类别为上面的类别
		testCategory.setParent(parentCategory);
		//查询父类别为1的子类别
		List<ShopCategory> shopCategoryList = shopCategoryDao.queryShopCategory(testCategory);
		
		assertEquals(1, shopCategoryList.size());
	}
	
}