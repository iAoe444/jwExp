package com.iaoe.jwExp.web.shopadmin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iaoe.jwExp.dto.Result;
import com.iaoe.jwExp.entity.ProductCategory;
import com.iaoe.jwExp.entity.Shop;
import com.iaoe.jwExp.enums.ProductCategoryStateEnum;
import com.iaoe.jwExp.service.ProductCategoryService;

@Controller
@RequestMapping("/shopadmin")
public class ProductCategoryManagementController {
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@RequestMapping(value="/getproductcategorylist",method = RequestMethod.GET)
	@ResponseBody
	//Result为dto层的内容，用于保存状态
	private Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request){
		//获取session里面的currentShop
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		List<ProductCategory> list = null;
		//如果存在currentShop且其保存的shopId大于0
		if(currentShop!=null&&currentShop.getShopId()>0) {
			//查询商店下的商品分类
			list = productCategoryService.getProductCategoryList(currentShop.getShopId());
			//返回成功的构造器
			return new Result<List<ProductCategory>>(true,list);
		}else {
			//ps为ProductCategoryStateEnum的错误枚举
			ProductCategoryStateEnum ps = ProductCategoryStateEnum.INNER_ERROR;
			//返回错误的构造器
			return new Result<List<ProductCategory>>(false,ps.getState(),ps.getStateInfo());
		}
	}
}
