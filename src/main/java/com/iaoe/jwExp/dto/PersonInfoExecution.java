package com.iaoe.jwExp.dto;

import java.util.List;

import com.iaoe.jwExp.entity.PersonInfo;
import com.iaoe.jwExp.enums.PersonInfoStateEnum;

public class PersonInfoExecution {
	// 结果标识
	private int state;
	// 结果信息
	private String stateInfo;
	// 用户数量
	private int count;

	private PersonInfo personInfo;

	private List<PersonInfo> personInfoList;

	public PersonInfoExecution() {

	}

	// 操作失败的时候构造器
	public PersonInfoExecution(PersonInfoStateEnum stateEnum) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}

	// 操作成功的构造器
	public PersonInfoExecution(PersonInfoStateEnum stateEnum, PersonInfo personInfo) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.personInfo = personInfo;
	}

	// 操作成功的构造器
	public PersonInfoExecution(PersonInfoStateEnum stateEnum, List<PersonInfo> personInfoList) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.personInfoList = personInfoList;
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

	public PersonInfo getPersonInfo() {
		return personInfo;
	}

	public void setPersonInfo(PersonInfo personInfo) {
		this.personInfo = personInfo;
	}

	public List<PersonInfo> getPersonInfoList() {
		return personInfoList;
	}

	public void setPersonInfoList(List<PersonInfo> personInfoList) {
		this.personInfoList = personInfoList;
	}

}
