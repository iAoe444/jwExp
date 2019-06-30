package com.iaoe.jwExp.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.iaoe.jwExp.BaseTest;
import com.iaoe.jwExp.entity.Product;
import com.iaoe.jwExp.entity.ProductCategory;
import com.iaoe.jwExp.entity.Shop;

public class ProductDaoTest extends BaseTest{
	@Autowired
	private ProductDao productDao;
	
	@Test
	@Ignore
	public void testAInsertProduct() throws Exception {
		Shop shop1 = new Shop();
		shop1.setShopId(2L);
		ProductCategory pc1 = new ProductCategory();
		pc1.setProductCategoryId(3L);
		Product product1 = new Product();
		product1.setProductName("测试1");
		product1.setProductDesc("测试Desc1");
		product1.setImgAddr("test1");
		product1.setPriority(0);
		product1.setEnableStatus(1);
		product1.setCreateTime(new Date());
		product1.setLastEditTime(new Date());
		product1.setShop(shop1);
		product1.setProductCategory(pc1);
		int effectedNum = productDao.insertProduct(product1);
		assertEquals(1, effectedNum);
	}
	
	@Test
	public void testBQueryProductByProductId() throws Exception{
		Product product = productDao.queryProductById(11L);
		System.out.println(product.getProductImgList().get(0).getImgAddr());
		System.out.println(product.getProductImgList().get(1).getImgAddr());
		assertEquals(3, product.getProductImgList().size());
	}
	
	@Test
	@Ignore
	public void testCUpdateProduct() throws Exception{
		Product product = new Product();
		Shop shop = new Shop();
		shop.setShopId(8L);
		product.setShop(shop);
		product.setProductId(1L);
		product.setProductName("我换了个名字");
		productDao.updateProduct(product);
	}
}
