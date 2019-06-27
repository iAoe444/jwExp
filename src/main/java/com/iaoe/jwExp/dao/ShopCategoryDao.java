package com.iaoe.jwExp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iaoe.jwExp.entity.ShopCategory;

public interface ShopCategoryDao {
	List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition") ShopCategory shopCategory);
}
