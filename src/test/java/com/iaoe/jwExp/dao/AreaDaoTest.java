package com.iaoe.jwExp.dao;

import java.util.List;
import static org.junit.Assert.assertEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;
import com.iaoe.jwExp.BaseTest;
import com.iaoe.jwExp.entity.Area;

public class AreaDaoTest extends BaseTest{
	@Autowired
	private AreaDao areaDao;
	
	@Test
	public void testQueryArea() {
		List<Area> areaList = areaDao.queryArea();
		assertEquals(2,areaList.size());
	}
}