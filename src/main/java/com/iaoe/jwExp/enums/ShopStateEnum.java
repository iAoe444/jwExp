package com.iaoe.jwExp.enums;

//枚举类型是一个特殊的类，可以当类来使用
public enum ShopStateEnum {
	//枚举操作可能存在的状态
	CHECK(0, "审核中"), OFFLINE(-1, "非法商铺"), SUCCESS(1, "操作成功"), PASS(2, "通过认证"), INNER_ERROR(-1001, "操作失败"),
	NULL_SHOPID(-1002, "ShopId为空"), NULL_SHOP_INFO(-1003, "传入了空的信息");

	private int state;
	private String stateInfo;

	//构造函数，设为私有，不让其他第三方应用修改原有的值
	private ShopStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	//获取状态码和状态的具体信息
	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	//根据传入的state返回相应的enum值
	public static ShopStateEnum stateOf(int index) {
		for (ShopStateEnum state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}
}
