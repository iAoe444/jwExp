package com.iaoe.jwExp.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iaoe.jwExp.dao.ProductDao;
import com.iaoe.jwExp.dao.ProductImgDao;
import com.iaoe.jwExp.dto.ImageHolder;
import com.iaoe.jwExp.dto.ProductExecution;
import com.iaoe.jwExp.entity.Product;
import com.iaoe.jwExp.entity.ProductImg;
import com.iaoe.jwExp.enums.ProductStateEnum;
import com.iaoe.jwExp.exceptions.ProductOperationException;
import com.iaoe.jwExp.service.ProductService;
import com.iaoe.jwExp.util.ImageUtil;
import com.iaoe.jwExp.util.PageCalculator;
import com.iaoe.jwExp.util.PathUtil;

@Service
public class ProductServiceImpl implements ProductService{
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;	
	
	//1.处理缩略图，获取缩略图相对路径并赋值给product
	//2.往tb_product写入商品信息，获取productId
	//3.结合productId批量处理商品详情图
	//4.将商品详情图列表批量插入tb_product_img中
	@Override
	@Transactional
	public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList)
			throws ProductOperationException {
		//空值判断
		if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
			// 给商品设置上属性 
			product.setCreateTime(new Date());
			product.setLastEditTime(new Date());
			// 默认为上架的情况
			product.setEnableStatus(1);
			// 如果缩略图不为空
			if (thumbnail != null) {
				addThumbnail(product, thumbnail);
			}
			try {
				int effectedNum = productDao.insertProduct(product);
				if (effectedNum <= 0) {
					throw new RuntimeException("创建商品失败");
				}
			} catch (Exception e) {
				throw new RuntimeException("创建商品失败:" + e.toString());
			}
			if (productImgList != null && productImgList.size() > 0) {
				addProductImgs(product, productImgList);
			}
			return new ProductExecution(ProductStateEnum.SUCCESS, product);
		} else {
			//返回空值
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}
	
	/**
	 * 添加缩略图
	 * @param product
	 * @param thumbnail
	 */
	private void addThumbnail(Product product, ImageHolder thumbnail) {
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		product.setImgAddr(thumbnailAddr);
	}
	
	/**
	 * 批量添加商品详情图
	 * @param product
	 * @param thumbnail
	 */
	private void addProductImgs(Product product,  List<ImageHolder> productImgHolderList) {
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		if (productImgHolderList != null && productImgHolderList.size() > 0) {
			//创建用于批量导入图片的productImgList
			List<ProductImg> productImgList = new ArrayList<ProductImg>();
			//遍历productImgHolderList给productImgList设值以及存在里面的图片
			for (ImageHolder productImgHodler : productImgHolderList) {
				String imgAddr = ImageUtil.generateNormalImg(productImgHodler, dest);
				ProductImg productImg = new ProductImg();
				productImg.setImgAddr(imgAddr);
				productImg.setProductId(product.getProductId());
				productImg.setCreateTime(new Date());
				productImgList.add(productImg);
			}
			try {
				int effectedNum = productImgDao.batchInsertProductImg(productImgList);
				if (effectedNum <= 0) {
					throw new ProductOperationException("创建商品详情图片失败");
				}
			} catch (Exception e) {
				throw new ProductOperationException("创建商品详情图片失败:" + e.toString());
			}
		}
	}

	@Override
	public Product getProductById(long productId) {
		return productDao.queryProductById(productId);
	}

	// 1.若缩略图参数有值，则处理缩略图，若原先存在缩略图则先删除再添加新图，之后获取缩略图相对路径并赋值给product
	// 2.若商品详情图列表参数有值，对商品详情图片列表进行同样的操作
	// 3.将tb_product_img下面的该商品原先的商品详情图记录全部清除
	// 4.更新tb_product_img和tb_product的信息
	@Override
	public ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList)
			throws ProductOperationException {
		//空值判断
		if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
			//更新时间
			product.setLastEditTime(new Date());
			//如果商品缩略图不为空
			if (thumbnail != null) {
				// 查询这个product
				Product tempProduct = productDao.queryProductById(product.getProductId());
				if (tempProduct.getImgAddr() != null) {
					//删除图片
					ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
				}
				//添加图片
				addThumbnail(product, thumbnail);
			}
			//如果有商品详情图传入
			if (productImgList != null && productImgList.size() > 0) {
				//删除图片
				deleteProductImgList(product.getProductId());
				//添加图片
				addProductImgs(product, productImgList);
			}
			try {
				int effectedNum = productDao.updateProduct(product);
				if (effectedNum <= 0) {
					throw new RuntimeException("更新商品信息失败");
				}
				return new ProductExecution(ProductStateEnum.SUCCESS, product);
			} catch (Exception e) {
				throw new RuntimeException("更新商品信息失败:" + e.toString());
			}
		} else {
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}
	
	//删除图片操作
	private void deleteProductImgList(long productId) {
		// 根据productId查询所有的图片
		List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
		//删除所有的图片
		for (ProductImg productImg : productImgList) {
			ImageUtil.deleteFileOrPath(productImg.getImgAddr());
		}
		//数据库删除所有图片
		productImgDao.deleteProductImgByProductId(productId);
	}

	@Override
	public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Product> productList = productDao.queryProductList(productCondition, rowIndex, pageSize);
		int count = productDao.queryProductCount(productCondition);
		ProductExecution pe = new ProductExecution();
		pe.setProductList(productList);
		pe.setCount(count);
		return pe;
	}

}
