package com.iaoe.jwExp.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.iaoe.jwExp.dto.ImageHolder;
import com.iaoe.jwExp.dto.ShopExecution;
import com.iaoe.jwExp.entity.Area;
import com.iaoe.jwExp.entity.PersonInfo;
import com.iaoe.jwExp.entity.Shop;
import com.iaoe.jwExp.entity.ShopCategory;
import com.iaoe.jwExp.enums.ShopStateEnum;
import com.iaoe.jwExp.exceptions.ShopOperationException;
import com.iaoe.jwExp.service.AreaService;
import com.iaoe.jwExp.service.ShopCategoryService;
import com.iaoe.jwExp.service.ShopService;
import com.iaoe.jwExp.util.CodeUtil;
import com.iaoe.jwExp.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private AreaService areaService;
	
	/**
	 * 进入店铺管理页面后调用的函数
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getshopmanagementinfo",method=RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> getShopManagementInfo(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		//如果shopId不合法
		if(shopId<=0) {
			Object currentShopObj = request.getSession().getAttribute("currentShop");
			if(currentShopObj==null) {
				//如果当前的没有session表示当前的shopId，那么就重定向
				modelMap.put("redirect",true);
				modelMap.put("url", "/jwExp/shopadmin/shoplist");
			}else {
				Shop currentShop = (Shop) currentShopObj;
				modelMap.put("redirect",false);
				modelMap.put("shopId",currentShop.getShopId());
			}
		}else {
			//新建一个currentShop写入我们的shopId
			Shop currentShop = new Shop();
			currentShop.setShopId(shopId);
			request.getSession().setAttribute("currentShop", currentShop);
			modelMap.put("redirect", false);
		}
		return modelMap;
	}
	/**
	 * 根据session里面的ownerid获取店铺信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getshoplist",method=RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> getShopList(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//由于没有实现登录功能，这里暂时使用假的用户信息
		PersonInfo user = new PersonInfo();
		user.setUserId(1L);
		user.setName("test");
		request.getSession().setAttribute("user", user);
		user = (PersonInfo) request.getSession().getAttribute("user");
		try {
			Shop shopCondition = new Shop();
			shopCondition.setOwner(user);
			ShopExecution se = shopService.getShopList(shopCondition, 0, 100);
			modelMap.put("success",true);
			modelMap.put("user",user);
			modelMap.put("shopList",se.getShopList());
		}catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	
	/**
	 * 根据shopId返回区域列表等信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getshopbyid",method=RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> getShopById(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId > -1) {
			try {
				Shop shop = shopService.getByShopId(shopId);
				List<Area> areaList = areaService.getAreaList();
				modelMap.put("shop", shop);
				modelMap.put("areaList", areaList);
				modelMap.put("success", true);
			}catch(Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		return modelMap;
	}
	
	/**
	 * 获取区域和分类列表
	 * @return
	 */
	@RequestMapping(value="/getshopinitinfo",method=RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> getShopInitInfo(){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		List<Area> areaList = new ArrayList<Area>();
		try {
			//获取区域和分类列表
			shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
			areaList = areaService.getAreaList();
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	
	/**
	 * 注册店铺功能
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/registershop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> registerShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码错误");
			return modelMap;
		}
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
			PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
			System.out.println("------------------"+ owner.getUserId());
			shop.setOwner(owner);
			ShopExecution se;
			try {
				ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
				se = shopService.addShop(shop, imageHolder);
				if(se.getState()==ShopStateEnum.CHECK.getState()) {
					modelMap.put("success", true);
				}else {
					//如果添加失败，返回之前枚举类型的失败原因
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (ShopOperationException | IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				e.printStackTrace();
			}
			return modelMap;
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "店铺信息不能为空");
		}
		return modelMap;
	}
	
	/**
	 * 编辑店铺功能
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码错误");
			return modelMap;
		}
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
		}
		//2.编辑店铺功能
		if(shop!=null && shop.getShopId()!=null) {
			ShopExecution se;
			try {
				if (shopImg == null) {
					se = shopService.modifyShop(shop, null);
				} else {
					ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
					se = shopService.modifyShop(shop, imageHolder);
				}
				if(se.getState()==ShopStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					//如果添加失败，返回之前枚举类型的失败原因
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (ShopOperationException | IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				e.printStackTrace();
			}
			return modelMap;
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺id");
		}
		return modelMap;
	}
}
