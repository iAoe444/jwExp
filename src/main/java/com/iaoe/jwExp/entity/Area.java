package com.iaoe.jwExp.entity;

import java.util.Date;

//区域的
public class Area {
	// 使用引用类型的原因主要是默认值是null，用基本类型的话默认值是0或者其他
	// 区域Id
	private Integer areaId;
	// 区域名称
	private String areaName;
	// 区域权重
	private Integer priority;
	// 创建时间
	private Date createTime;
	// 更新时间
	private Date lastEditTime;
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
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
