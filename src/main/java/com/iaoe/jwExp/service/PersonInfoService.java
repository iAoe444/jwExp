package com.iaoe.jwExp.service;

import com.iaoe.jwExp.dto.PersonInfoExecution;
import com.iaoe.jwExp.entity.PersonInfo;

public interface PersonInfoService {
	/**
	 * 根据用户信息查询用户
	 * @param personInfo
	 * @return
	 */
	PersonInfoExecution queryPersonInfo(PersonInfo personInfo);
}
