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
}
