package com.iaoe.jwExp.service;

import java.util.List;

import com.iaoe.jwExp.dto.ImageHolder;
import com.iaoe.jwExp.dto.ProductExecution;
import com.iaoe.jwExp.entity.Product;
import com.iaoe.jwExp.exceptions.ProductOperationException;

public interface ProductService {

	/**
	 * 添加商品和图片处理
	 * @param product
	 * @param thumbnail
	 * @param productImgList
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution addProduct(Product product, ImageHolder thumbnail,
			List<ImageHolder> productImgList) throws ProductOperationException;
	
	/**
	 * 通过id查询商品信息
	 * @param productId
	 * @return
	 */
	Product getProductById(long productId);
	
	/**
	 * 修改商品信息并对图片进行处理
	 * @param product
	 * @param thumbnail
	 * @param productImgList
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution modifyProduct(Product product,ImageHolder thumbnail,
			List<ImageHolder> productImgList) throws ProductOperationException;
	
	/**
	 * 根据信息获取商品列表
	 * @param productCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);
}
