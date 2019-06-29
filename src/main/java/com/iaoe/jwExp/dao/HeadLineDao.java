package com.iaoe.jwExp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iaoe.jwExp.entity.HeadLine;

public interface HeadLineDao {
	
	/**
	 * 查询头条列表
	 * @param headLineCondition
	 * @return
	 */
	List<HeadLine> queryHeadLine(
			@Param("headLineCondition") HeadLine headLineCondition);
}
