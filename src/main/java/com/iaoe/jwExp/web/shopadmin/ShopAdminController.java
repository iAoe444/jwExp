package com.iaoe.jwExp.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//这个控制层用于控制前端的地址，相当于路由
@Controller
@RequestMapping(value = "/shopadmin",method=RequestMethod.GET)
public class ShopAdminController {
	@RequestMapping(value="/shopoperation")
	public String shopOperation() {
		return "shop/shopoperation";
	}
	@RequestMapping(value="/shoplist")
	public String shopList() {
		return "shop/shoplist";
	}
	@RequestMapping(value="/shopmanagement")
	public String shopManagement() {
		return "shop/shopmanagement";
	}
	@RequestMapping(value="/productcategorymanagement")
	public String productCategoryManagement() {
		return "shop/productcategorymanagement";
	}
	@RequestMapping(value="/productmanagement")
	public String productManagement() {
		return "shop/productmanagement";
	}
	@RequestMapping(value="/productoperation")
	public String productOperation() {
		return "shop/productoperation";
	}
}