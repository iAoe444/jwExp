package com.iaoe.jwExp.web.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iaoe.jwExp.dto.ProductExecution;
import com.iaoe.jwExp.entity.Product;
import com.iaoe.jwExp.entity.ProductCategory;
import com.iaoe.jwExp.entity.Shop;
import com.iaoe.jwExp.service.ProductCategoryService;
import com.iaoe.jwExp.service.ProductService;
import com.iaoe.jwExp.service.ShopService;
import com.iaoe.jwExp.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/frontend")
public class ShopDetailController {
	@Autowired
	private ShopService shopService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductCategoryService productCategoryService;

	/**
	 * 获取店铺信息和商品列表的信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listshopdetailpageinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listShopDetailPageInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取shopId
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		Shop shop = null;
		List<ProductCategory> productCategoryList = null;
		// 空值判断
		if (shopId != -1) {
			// 获取店铺的信息
			shop = shopService.getByShopId(shopId);
			// 获取区域信息
			productCategoryList = productCategoryService.getProductCategoryList(shopId);
			modelMap.put("shop", shop);
			modelMap.put("productCategoryList", productCategoryList);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		return modelMap;
	}

	/**
	 * 依据信息来筛选商品
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listproductsbyshop", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listProductsByShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取页码
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		// 获取一页需要显示的条数
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// 获取店铺Id
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		// 空值判断
		if ((pageIndex > -1) && (pageSize > -1) && (shopId > -1)) {
			// 尝试获取店铺Id
			long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
			// 尝试获取产品名
			String productName = HttpServletRequestUtil.getString(request, "productName");
			// 组合所有的信息
			Product productCondition = compactProductCondition4Search(shopId, productCategoryId, productName);
			// 获取商品信息
			ProductExecution pe = productService.getProductList(productCondition, pageIndex, pageSize);
			modelMap.put("productList", pe.getProductList());
			modelMap.put("count", pe.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}

	private Product compactProductCondition4Search(long shopId, long productCategoryId, String productName) {
		Product productCondition = new Product();
		Shop shop = new Shop();
		shop.setShopId(shopId);
		productCondition.setShop(shop);
		if (productCategoryId != -1L) {
			// 查询某个商品类别下面的商品列表
			ProductCategory productCategory = new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			productCondition.setProductCategory(productCategory);
		}
		if (productName != null) {
			// 查询有商品名的商品
			productCondition.setProductName(productName);
		}
		productCondition.setEnableStatus(1);
		return productCondition;
	}
}
