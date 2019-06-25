package com.iaoe.jwExp.util;

public class PathUtil {
	//获取当前系统分隔符
	private static String separator = System.getProperty("file.separator");
	public static String getImgBasePath() {
		String os = System.getProperty("os.name");
		String basePath = "";
		//获取当前的系统
		if(os.toLowerCase().startsWith("win")) {
			basePath = "E:/jwExp/image/";
		}else {
			basePath = "/home/iAoe/image/";
		}
		basePath = basePath.replace("/", separator);
		return basePath;
	}
	//获取项目图片的子路径
	public static String getShopImagePath(long shopId) {
		String imagePath = "upload/item/shop/" + shopId + "/";
		return imagePath.replace("/", separator);
	}
}
