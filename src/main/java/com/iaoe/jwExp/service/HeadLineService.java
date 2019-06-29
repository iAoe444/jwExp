package com.iaoe.jwExp.service;

import java.io.IOException;
import java.util.List;

import com.iaoe.jwExp.entity.HeadLine;

public interface HeadLineService {
	
	/**
	 * 根据传入条件来返回指定的头条列表
	 * @param headLineCondition
	 * @return
	 * @throws IOException
	 */
	List<HeadLine> getHeadLineList(HeadLine headLineCondition)
			throws IOException;
}
