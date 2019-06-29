package com.iaoe.jwExp.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.iaoe.jwExp.BaseTest;
import com.iaoe.jwExp.entity.Area;
import com.iaoe.jwExp.entity.PersonInfo;
import com.iaoe.jwExp.entity.Shop;
import com.iaoe.jwExp.entity.ShopCategory;

public class ShopDaoTest extends BaseTest{
	@Autowired
	private ShopDao shopDao;
	
	@Test
	public void testQueryShopList() {
		Shop shopCondition = new Shop();
		
		ShopCategory childCategory = new ShopCategory();
		ShopCategory parentCategory = new ShopCategory();
		
		parentCategory.setShopCategoryId(1L);
		childCategory.setParent(parentCategory);
		shopCondition.setShopCategory(childCategory);
		List<Shop> shopList = shopDao.queryShopList(shopCondition, 0, 10);
		int count = shopDao.queryShopCount(shopCondition);
		System.out.println("店铺列表的大小:"+shopList.size());
		System.out.println("店铺总数:"+count);
		
//		System.out.println("ownerId=1");
//		PersonInfo owner = new PersonInfo();
//		owner.setUserId(1L);
//		shopCondition.setOwner(owner);
//		List<Shop> shopList = shopDao.queryShopList(shopCondition, 1, 10);
//		int count = shopDao.queryShopCount(shopCondition);
//		System.out.println("店铺列表的大小:"+shopList.size());
//		System.out.println("店铺总数:"+count);
//		
//		System.out.println("ownerId=1,shopCategory=2L");
//		ShopCategory sc = new ShopCategory();
//		sc.setShopCategoryId(2L);
//		shopCondition.setShopCategory(sc);
//		shopList = shopDao.queryShopList(shopCondition, 0, 10);
//		count = shopDao.queryShopCount(shopCondition);
//		System.out.println("店铺列表的大小:"+shopList.size());
//		System.out.println("店铺总数:"+count);
//		
//		System.out.println("ownerId=1,shopCategory=2L,shopName=店铺");
//		shopCondition.setShopName("店铺");
//		shopList = shopDao.queryShopList(shopCondition, 0, 10);
//		count = shopDao.queryShopCount(shopCondition);
//		System.out.println("店铺列表的大小:"+shopList.size());
//		System.out.println("店铺总数:"+count);
//		
//		System.out.println("ownerId=1,shopCategory=2L,shopName=店铺,area_Id=1");
//		Area area = new Area();
//		area.setAreaId(1);
//		shopCondition.setArea(area);;
//		shopList = shopDao.queryShopList(shopCondition, 0, 10);
//		count = shopDao.queryShopCount(shopCondition);
//		System.out.println("店铺列表的大小:"+shopList.size());
//		System.out.println("店铺总数:"+count);
//		
//		System.out.println("ownerId=1,shopCategory=2L,shopName=店铺,area_Id=1,enable_staus=1");
//		shopCondition.setEnableStatus(1);
//		shopList = shopDao.queryShopList(shopCondition, 0, 10);
//		count = shopDao.queryShopCount(shopCondition);
//		System.out.println("店铺列表的大小:"+shopList.size());
//		System.out.println("店铺总数:"+count);
	}
	
	@Test
	@Ignore
	public void testQueryByShopId(){
		long shopId = 2;
		Shop shop = shopDao.queryByShopId(shopId);
		int areaId = shop.getArea().getAreaId();
		System.out.println("areaId:"+shop.getArea().getAreaId());
		System.out.println("areaName:"+shop.getArea().getAreaName());
		assertEquals(2, areaId);
	}
	
	@Test
	@Ignore
	public void testInsertShop(){
		Shop shop = new Shop();
		PersonInfo owner = new PersonInfo();
		Area area = new Area();
		ShopCategory sc = new ShopCategory();
		owner.setUserId(1L);
		area.setAreaId(2);
		sc.setShopCategoryId(1L);
		shop.setOwner(owner);
		shop.setArea(area);
		shop.setShopCategory(sc);
		shop.setShopName("mytest1");
		shop.setShopDesc("mytest1");
		shop.setShopAddr("testaddr1");
		shop.setPhone("12345678901");
		shop.setShopImg("test1");
		shop.setPriority(1);
		shop.setCreateTime(new Date());
		shop.setLastEditTime(new Date());
		shop.setEnableStatus(1);
		shop.setAdvice("审核中");
		int effectedNum = shopDao.insertShop(shop);
		assertEquals(1, effectedNum);
	}
	
	@Test
	@Ignore
	public void testUpdateShop(){
		Shop shop = new Shop();
		shop.setShopId(2L);
		shop.setShopDesc("更新操作描述");
		shop.setShopAddr("更新操作地址");
		shop.setLastEditTime(new Date());
		int effectedNum = shopDao.updateShop(shop);
		assertEquals(1, effectedNum);
	}
}
