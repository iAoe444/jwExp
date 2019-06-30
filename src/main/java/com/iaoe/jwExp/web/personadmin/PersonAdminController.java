package com.iaoe.jwExp.web.personadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/personadmin")
public class PersonAdminController {
	/**
	 * 登录功能
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	private String login() {
		return "frontend/login";
	}
}
