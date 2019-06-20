package com.iaoe.jwExp.entity;

import java.util.Date;

public class WeChatAuth {
	private Long weChatAuthId;
	private String openId;
	private Date createTime;
	public Long getWeChatAuthId() {
		return weChatAuthId;
	}
	public void setWeChatAuthId(Long weChatAuthId) {
		this.weChatAuthId = weChatAuthId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public PersonInfo getPersonInfo() {
		return personInfo;
	}
	public void setPersonInfo(PersonInfo personInfo) {
		this.personInfo = personInfo;
	}
	//微信账号和peronInfo相互关联
	private PersonInfo personInfo;
} 	