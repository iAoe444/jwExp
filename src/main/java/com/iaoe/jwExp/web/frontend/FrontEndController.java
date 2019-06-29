package com.iaoe.jwExp.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/frontend")
public class FrontEndController {
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	private String index() {
		return "frontend/index";
	}
	
	/**
	 * 店铺列表功能
	 * @return
	 */
	@RequestMapping(value = "/shoplist", method = RequestMethod.GET)
	private String shopList() {
		return "frontend/shoplist";
	}
	
	/**
	 * 店铺列表功能
	 * @return
	 */
	@RequestMapping(value = "/shopdetail", method = RequestMethod.GET)
	private String shopDetail() {
		return "frontend/shopdetail";
	}
	
	/**
	 * 登录功能
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	private String login() {
		return "frontend/login";
	}
}
