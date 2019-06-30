package com.iaoe.jwExp.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.iaoe.jwExp.BaseTest;
import com.iaoe.jwExp.entity.PersonInfo;

public class PersonInfoDaoTest extends BaseTest{
	@Autowired
	private PersonInfoDao personInfoDao;
	
	@Test
	public void testQueryPersonInfo() {
		PersonInfo personInfo = new PersonInfo();
		personInfo.setUserName("test");
		personInfo.setPassword("test123456");
		List<PersonInfo> personInfoList = new ArrayList<PersonInfo>();
		personInfoList = personInfoDao.queryPersonInfo(personInfo);
		assertEquals(1, personInfoList.size());
	}
}
