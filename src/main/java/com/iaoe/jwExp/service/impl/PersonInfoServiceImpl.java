package com.iaoe.jwExp.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iaoe.jwExp.dao.PersonInfoDao;
import com.iaoe.jwExp.dto.PersonInfoExecution;
import com.iaoe.jwExp.entity.PersonInfo;
import com.iaoe.jwExp.enums.PersonInfoStateEnum;
import com.iaoe.jwExp.service.PersonInfoService;

@Service
public class PersonInfoServiceImpl implements PersonInfoService{
	@Autowired
	private PersonInfoDao personInfoDao;
	
	@Override
	public PersonInfoExecution queryPersonInfo(PersonInfo personInfo) {
		List<PersonInfo> personInfoList = new ArrayList<PersonInfo>();
		
		//实现登录功能
		if(personInfo.getUserName()!=null && personInfo.getPassword()!=null) {
			personInfoList = personInfoDao.queryPersonInfo(personInfo);
			//登录成功就返回登录成功
			if (personInfoList.size()==1) {
				return new PersonInfoExecution(PersonInfoStateEnum.SUCCESS,personInfoList.get(0));
			}else {
				return new PersonInfoExecution(PersonInfoStateEnum.ERROR);
			}
		}
		else {
			return new PersonInfoExecution(PersonInfoStateEnum.EMPTY);
		}
	}
	
}
