package com.iaoe.jwExp.dto;

import java.util.List;

import com.iaoe.jwExp.entity.Shop;
import com.iaoe.jwExp.enums.ShopStateEnum;

public class ShopExecution {
	//结果标识
	private int state;
	//结果信息
	private String stateInfo;
	//店铺数量
	private int count;
	//操作的shop（增删改店铺的时候用到）
	private Shop shop;
	//shop列表（查询店铺列表的时候用到）
	private List<Shop> shopList;
	
	public ShopExecution() {
		
	}
	
	//店铺操作失败的时候构造器
	public ShopExecution(ShopStateEnum stateEnum) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}
	//增删改店铺操作成功的构造器
	public ShopExecution(ShopStateEnum stateEnum,Shop shop) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.shop = shop;
	}
	//查询店铺列别时成功的构造器
	public ShopExecution(ShopStateEnum stateEnum,List<Shop> shopList) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.shopList = shopList;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public List<Shop> getShopList() {
		return shopList;
	}

	public void setShopList(List<Shop> shopList) {
		this.shopList = shopList;
	}
}
