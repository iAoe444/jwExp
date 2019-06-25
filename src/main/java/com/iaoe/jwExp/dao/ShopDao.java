package com.iaoe.jwExp.dao;

import com.iaoe.jwExp.entity.Shop;

public interface ShopDao {
	/**
	 * 新增店铺
	 * @param shop
	 * @return insert影响的行数，如果是1的就代表返回成功
	 */
	int insertShop(Shop shop);
	/**
	 * 更新店铺信息
	 * @param shop
	 * @return
	 */
	int updateShop(Shop shop);
}
