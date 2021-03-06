package com.iaoe.jwExp.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.iaoe.jwExp.BaseTest;
import com.iaoe.jwExp.dto.ImageHolder;
import com.iaoe.jwExp.dto.ShopExecution;
import com.iaoe.jwExp.entity.Area;
import com.iaoe.jwExp.entity.PersonInfo;
import com.iaoe.jwExp.entity.Shop;
import com.iaoe.jwExp.entity.ShopCategory;
import com.iaoe.jwExp.enums.ShopStateEnum;
import com.iaoe.jwExp.exceptions.ShopOperationException;

public class ShopServiceTest extends BaseTest{
	@Autowired
	private ShopService shopService;
	
	@Test
	public void testGetShopList() {
		Shop shopCondition = new Shop();
		System.out.println("ownerId=1");
		PersonInfo owner = new PersonInfo();
		owner.setUserId(1L);
		shopCondition.setOwner(owner);
		ShopExecution se = shopService.getShopList(shopCondition, 1, 3);
		System.out.println("满足要求的店铺总大小:"+se.getCount());
		System.out.println("页面显示的店铺列表:"+se.getShopList().size());
	}
	
	@Test
	@Ignore
	public void testModifyShop() throws ShopOperationException,FileNotFoundException{
		Shop shop = shopService.getByShopId(2L);
		shop.setShopName("修改后的店铺名称");
		File shopImg = new File("C:\\Users\\iAoe\\Desktop\\1.jpg");
		InputStream shopImgInputStream = new FileInputStream(shopImg);
		ImageHolder imageHolder = new ImageHolder("1.jpg", shopImgInputStream);
		ShopExecution se = shopService.modifyShop(shop, imageHolder);
		System.out.println("新的图片地址:" + se.getShop().getShopImg());
	}
	
	@Test
	@Ignore
	public void testAddShop() throws FileNotFoundException{
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
		shop.setShopName("service测试");
		shop.setShopDesc("mytest1");
		shop.setShopAddr("testaddr1");
		shop.setPhone("12345678901");
		shop.setPriority(1);
		shop.setCreateTime(new Date());
		shop.setLastEditTime(new Date());
		//为了规范点改成这个
		shop.setEnableStatus(ShopStateEnum.CHECK.getState());
		shop.setAdvice("审核中");
		File shopImg = new File("C:\\Users\\iAoe\\Desktop\\1.jpg");
		InputStream shopImgInputStream = new FileInputStream(shopImg);
		ImageHolder imageHolder = new ImageHolder("1.jpg", shopImgInputStream);
		ShopExecution se = shopService.addShop(shop, imageHolder);
		assertEquals(ShopStateEnum.CHECK.getState(), se.getState());
	}
}
