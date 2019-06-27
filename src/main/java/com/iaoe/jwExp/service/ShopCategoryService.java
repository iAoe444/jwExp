package com.iaoe.jwExp.service;

import java.util.List;

import com.iaoe.jwExp.entity.ShopCategory;

public interface ShopCategoryService {
	List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
}
