package com.iaoe.jwExp.dao;

import java.util.List;

import com.iaoe.jwExp.entity.ProductImg;

public interface ProductImgDao {
	
	/**
	 * 批量添加商品详情图片
	 * @param productImgList
	 * @return
	 */
	int batchInsertProductImg(List<ProductImg> productImgList);
	
	/**
	 * 删除指定商品下的所有详情图
	 * @param productId
	 * @return
	 */
	int deleteProductImgByProductId(long productId);
	
	/**
	 * 通过productId来查询所有的商品详情图
	 * @param productId
	 * @return
	 */
	List<ProductImg> queryProductImgList(long productId);
}
