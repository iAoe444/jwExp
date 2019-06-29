package com.iaoe.jwExp.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iaoe.jwExp.dao.HeadLineDao;
import com.iaoe.jwExp.entity.HeadLine;
import com.iaoe.jwExp.service.HeadLineService;

@Service
public class HeadLineServiceImpl implements HeadLineService{

	@Autowired
	private HeadLineDao headLineDao; 
	
	@Override
	public List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException {
		return headLineDao.queryHeadLine(headLineCondition);
	}
	
}
