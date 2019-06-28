package com.iaoe.jwExp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iaoe.jwExp.entity.Shop;

public interface ShopDao {
	/**
	 * 分页查询数据，可输入的条件有：店铺名（模糊），店铺状态，店铺类别，区域Id，owner
	 * @param shopCondition
	 * @param rowIndex	从第几行开始取
	 * @param pageSize	返回的条数
	 * @return
	 */
	List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition,
			@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);
	/**
	 * 通过条件获取店铺的总数
	 * @param shopCondition
	 * @return
	 */
	int queryShopCount(@Param("shopCondition") Shop shopCondition);
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
	/**
	 * 通过shopId查询店铺
	 * @param shopId
	 * @return
	 */
	Shop queryByShopId(long shopId);
}
