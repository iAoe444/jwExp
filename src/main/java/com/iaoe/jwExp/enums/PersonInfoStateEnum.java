package com.iaoe.jwExp.enums;

public enum PersonInfoStateEnum {
	ERROR(-1, "登录失败"), SUCCESS(0, "登录成功"),  INNER_ERROR(
			-1001, "操作失败"),EMPTY(-1002, "请输入密码和账号");

	private int state;

	private String stateInfo;

	private PersonInfoStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public static PersonInfoStateEnum stateOf(int index) {
		for (PersonInfoStateEnum state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}
}
