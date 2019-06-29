package com.iaoe.jwExp.web.shopadmin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iaoe.jwExp.dto.ProductCategoryExecution;
import com.iaoe.jwExp.dto.Result;
import com.iaoe.jwExp.entity.ProductCategory;
import com.iaoe.jwExp.entity.Shop;
import com.iaoe.jwExp.enums.ProductCategoryStateEnum;
import com.iaoe.jwExp.exceptions.ProductCategoryOperationException;
import com.iaoe.jwExp.service.ProductCategoryService;

@Controller
@RequestMapping("/shopadmin")
public class ProductCategoryManagementController {
	@Autowired
	private ProductCategoryService productCategoryService;

	@RequestMapping(value = "/getproductcategorylist", method = RequestMethod.GET)
	@ResponseBody
	// Result为dto层的内容，用于保存状态
	private Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request) {
		// 获取session里面的currentShop
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		List<ProductCategory> list = null;
		// 如果存在currentShop且其保存的shopId大于0
		if (currentShop != null && currentShop.getShopId() > 0) {
			// 查询商店下的商品分类
			list = productCategoryService.getProductCategoryList(currentShop.getShopId());
			// 返回成功的构造器
			return new Result<List<ProductCategory>>(true, list);
		} else {
			// ps为ProductCategoryStateEnum的错误枚举
			ProductCategoryStateEnum ps = ProductCategoryStateEnum.INNER_ERROR;
			// 返回错误的构造器
			return new Result<List<ProductCategory>>(false, ps.getState(), ps.getStateInfo());
		}
	}

	@RequestMapping(value = "/addproductcategorys", method = RequestMethod.POST)
	@ResponseBody
	// @RequestBody可以直接从前端获取productCategoryList
	private Map<String, Object> addProductCategorys(@RequestBody List<ProductCategory> productCategoryList,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 通过session的方法可以尽量不依赖于前台数据
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		for (ProductCategory pc : productCategoryList) {
			pc.setShopId(currentShop.getShopId());
			pc.setCreateTime(new Date());
		}
		if (productCategoryList != null && productCategoryList.size() > 0) {
			try {
				// 批量添加商品类别
				ProductCategoryExecution pe = productCategoryService.batchAddProductCategory(productCategoryList);
				// 如果成功
				if (pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			} catch (ProductCategoryOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请至少输入一个商品类别");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/removeproductcategory", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> removeProductCategory(Long productCategoryId,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//空值和合法值判断
		if (productCategoryId != null && productCategoryId > 0) {
			try {
				//从session中获取currentShop
				Shop currentShop = (Shop) request.getSession().getAttribute(
						"currentShop");
				//删除该分类
				ProductCategoryExecution pe = productCategoryService
						.deleteProductCategory(productCategoryId,
								currentShop.getShopId());
				//如果通过验证，则返回成功
				if (pe.getState() == ProductCategoryStateEnum.SUCCESS
						.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请至少选择一个商品类别");
		}
		return modelMap;
	}
}
