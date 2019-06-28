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
}
