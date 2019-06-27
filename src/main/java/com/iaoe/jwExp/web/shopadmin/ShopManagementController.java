package com.iaoe.jwExp.web.shopadmin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iaoe.jwExp.dto.ShopExecution;
import com.iaoe.jwExp.entity.PersonInfo;
import com.iaoe.jwExp.entity.Shop;
import com.iaoe.jwExp.enums.ShopStateEnum;
import com.iaoe.jwExp.service.ShopService;
import com.iaoe.jwExp.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
	@Autowired
	private ShopService shopService;
	
	/**
	 * 注册店铺功能
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/registershop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> registerShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 1.接受前端发来的请求并转换相应的参数，包括前端发来的店铺信息和图片信息
		// 这里先获取请求里面有个叫shopStr的参数
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		// 新建一个用于解析json的对象，这个在之前maven的导包里面有一个json解析包
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			// 将json解析到shop这个类里面
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			// 如果出现意外，那么返回的错误信息
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		// 这里用于获取请求里面的图片
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		//判断是否存在这个文件流
		if(commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			//获取参数为shopImg的图片
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "上传图片不能为空");
			return modelMap;
		}
		//2.注册店铺功能
		if(shopImg!=null && shop!=null) {
			PersonInfo owner = new PersonInfo();
			owner.setUserId(1L);
			shop.setOwner(owner);
			ShopExecution se = shopService.addShop(shop, shopImg);
			if(se.getState()==ShopStateEnum.CHECK.getState()) {
				modelMap.put("success", true);
			}else {
				//如果添加失败，返回之前枚举类型的失败原因
				modelMap.put("success", false);
				modelMap.put("errMsg", se.getStateInfo());
			}
			return modelMap;
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "店铺信息不能为空");
		}
		return modelMap;
	}
}
