package com.iaoe.jwExp.entity;

import java.util.Date;

public class PersonInfo {
	private Long userId;
	private String name;
	private String profileImg;
	private String gender;
	//用户状态	是否有资格登录商城做操作 0.可以登录 	1.无法登录
	private Integer enableStaus;
	//用户身份	1.顾客	2.	店家		3.超级管理员
	private Integer userType;
	private String userName;
	private String password;
	private Date createTime;
	private Date lastEditTime;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProfileImg() {
		return profileImg;
	}
	public void setProfileImg(String profileImg) {
		this.profileImg = profileImg;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Integer getEnableStaus() {
		return enableStaus;
	}
	public void setEnableStaus(Integer enableStaus) {
		this.enableStaus = enableStaus;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastEditTime() {
		return lastEditTime;
	}
	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}
}
