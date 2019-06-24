package com.iaoe.jwExp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iaoe.jwExp.dao.AreaDao;
import com.iaoe.jwExp.entity.Area;
import com.iaoe.jwExp.service.AreaService;

@Service
public class AreaServiceImpl implements AreaService{
	@Autowired
	private AreaDao areaDao;
	@Override
	public List<Area> getAreaList() {
		return areaDao.queryArea();
	}
}