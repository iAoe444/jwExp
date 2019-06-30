package com.iaoe.jwExp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iaoe.jwExp.entity.PersonInfo;

public interface PersonInfoDao {
	/**
	 * 查询方法
	 */
	List<PersonInfo> queryPersonInfo(
			@Param("personInfo") PersonInfo personInfo);
}
