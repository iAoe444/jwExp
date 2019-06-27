package com.iaoe.jwExp.service;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.iaoe.jwExp.dto.ShopExecution;
import com.iaoe.jwExp.entity.Shop;

public interface ShopService {
	ShopExecution addShop(Shop shop, CommonsMultipartFile shopImg);
}
