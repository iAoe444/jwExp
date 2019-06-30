package com.iaoe.jwExp.web.personadmin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iaoe.jwExp.dto.PersonInfoExecution;
import com.iaoe.jwExp.entity.PersonInfo;
import com.iaoe.jwExp.enums.PersonInfoStateEnum;
import com.iaoe.jwExp.service.PersonInfoService;
import com.iaoe.jwExp.util.CodeUtil;
import com.iaoe.jwExp.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/person")
public class PersonController {
	@Autowired
	private PersonInfoService personInfoService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> logIn(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码错误");
			return modelMap;
		}
		try {
			PersonInfo personInfo = new PersonInfo();
			// 尝试获取用户名和密码
			String userName = HttpServletRequestUtil.getString(request,"userName");
			String password = HttpServletRequestUtil.getString(request,"password");
			personInfo.setUserName(userName);
			personInfo.setPassword(password);
			// 尝试登录功能
			PersonInfoExecution pe = personInfoService.queryPersonInfo(personInfo);
			if(pe.getState()==PersonInfoStateEnum.SUCCESS.getState()) {
				// 成功后设置session
				modelMap.put("success", true);
				request.getSession().setAttribute("user", personInfo);
				return modelMap;
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", pe.getStateInfo());
				return modelMap;
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
	}
	
	@RequestMapping(value = "/iflogin", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> ifLogin(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		if(user ==null) {
			modelMap.put("login", false);
			return modelMap;
		}else {
			modelMap.put("userName", user.getName());
			modelMap.put("login", true);
			return modelMap;
		}
	}
}
