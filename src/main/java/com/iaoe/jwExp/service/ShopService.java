package com.iaoe.jwExp.service;

import java.io.File;

import com.iaoe.jwExp.dto.ShopExecution;
import com.iaoe.jwExp.entity.Shop;

public interface ShopService {
	ShopExecution addShop(Shop shop, File shopImg);
}
