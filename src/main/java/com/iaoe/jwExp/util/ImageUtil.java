package com.iaoe.jwExp.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import com.iaoe.jwExp.dto.ImageHolder;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {
	// 这里我在src/main/resources里面放了一个watermark.png,用于src/main/resources的路径在实际的硬盘中不同
	// 所以这里这个方法用于获取classpath的实际路径:/D:/java_WorkSpace/jwExp/target/classes/
	private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final Random r = new Random();
	/**
	 * 处理图片
	 * @param thumbnail	这个是spring里的文件格式
	 * @param targetAddr 这个是存储的图片路径
	 * @return
	 */
	public static String generateThumbnail(ImageHolder thumbnail,String targetAddr) {
		//取一个时间+随机的文件名
		String realFileName = getRandomFileName();
		//获取文件扩展名
		String extension = getFileExtension(thumbnail.getImageName());
		//创建目标路径
		makeDirPath(targetAddr);
		//组合相对路径名
		String relativeAddr = targetAddr + realFileName + extension;
		//和basePath相互结合成为绝对路径,就是文件输出的路径
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		//操作图片
		try {
			Thumbnails.of(thumbnail.getImage()).size(200, 200)
			.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "watermark.png")), 0.25f)
			.outputQuality(0.8f).toFile(dest);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return relativeAddr;
	}
	
	/**
	 * 生成较大的图片
	 * @param thumbnail	这个是spring里的文件格式
	 * @param targetAddr 这个是存储的图片路径
	 * @return
	 */
	public static String generateNormalImg(ImageHolder thumbnail,String targetAddr) {
		//取一个时间+随机的文件名
		String realFileName = getRandomFileName();
		//获取文件扩展名
		String extension = getFileExtension(thumbnail.getImageName());
		//创建目标路径
		makeDirPath(targetAddr);
		//组合相对路径名
		String relativeAddr = targetAddr + realFileName + extension;
		//和basePath相互结合成为绝对路径,就是文件输出的路径
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		//操作图片
		try {
			Thumbnails.of(thumbnail.getImage()).size(337, 640)
			.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "watermark.png")), 0.25f)
			.outputQuality(0.9f).toFile(dest);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return relativeAddr;
	}

	/**
	 * 创建目标路径所涉及到的目录
	 * @param targetAddr
	 */
	private static void makeDirPath(String targetAddr) {
		String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
		File dirPath = new File(realFileParentPath);
		if(!dirPath.exists()) {
			dirPath.mkdirs();
		}
	}

	/**
	 * 获取输入文件流的扩展名
	 * @param thumbnail
	 * @return
	 */
	private static String getFileExtension(String fileName) {
		//获取最后一个点之后的东西，也就是扩展名
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/**
	 * 生成随机文件名，当前年月日小时和分钟秒钟，加五位随机数
	 */
	private static String getRandomFileName() {
		// 获取随机的五位数
		int rannum = r.nextInt(89999)+10000;
		String nowTimeStr = sDateFormat.format(new Date());
		return nowTimeStr + rannum;
	}
	
	/**
	 *   判断storePath是文件的路径还是目录的路径，
	 *   如果storePath是文件路径则删除该文件，
	 *   如果storePath是目录路径则删除该目录下的所有文件
	 * @param storePath
	 */
	public static void deleteFileOrPath(String storePath) {
		File fileOrPath = new File(PathUtil.getImgBasePath()+storePath);
		if(fileOrPath.exists()) {
			if(fileOrPath.isDirectory()) {
				File files[] = fileOrPath.listFiles();
				for(int i=0;i<files.length;i++) {
					files[i].delete();
				}
			}
			fileOrPath.delete();
		}
	}

}
