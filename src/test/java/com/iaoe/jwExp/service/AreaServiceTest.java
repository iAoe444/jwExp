package com.iaoe.jwExp.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.iaoe.jwExp.BaseTest;
import com.iaoe.jwExp.entity.Area;

public class AreaServiceTest extends BaseTest{
	@Autowired
	private AreaService areaService;
	@Test
	public void testAreaList() {
		List<Area> areaList = areaService.getAreaList();
		assertEquals("西方",areaList.get(0).getAreaName());
	}
}
