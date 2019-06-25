# ssm笔记-项目实战

> 书写于2019/06/25  9:59

## 1 店铺功能的实现

```
这里功能的实现的步骤主要分下面的几步：

> 连接数据库

> Mybatis数据库表映射关系的配置

> dao->service->controller层代码的编写，junit的使用

> Session，图片处理工具Thumbnailator的使用

> suimobile前端设计与开发
```

### （增）DAO层插入店铺功能的实现

1. 实现shopDao层，在`src/main/java`的`com.iaoe.jwExp.dao`里新建`ShopDao`接口

   ```java
   package com.iaoe.jwExp.dao;
   
   import com.iaoe.jwExp.entity.Shop;
   
   public interface ShopDao {
   	/**
   	 * 新增店铺
   	 * @param shop
   	 * @return insert影响的行数，如果是1的就代表返回成功
   	 */
   	int insertShop(Shop shop);
   }
   ```

2. 实现dao层的自动映射，通过mybatis进行配置，在`src/main/resources/mapper`里面新建`AreaDao.xml`

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <!-- 按照mybatis-3的规范书写 -->
   <!DOCTYPE mapper
       PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
       "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
   <!-- 命名空间为com.iaoe.jwExp.dao.ShopDao即实现这个类 -->
   <!-- 属性id为实现的方法 -->
   <!-- 属性KeyColumn为主键 -->
   <!-- 属性useGeneratedKeys为true代表返回主键值 -->
   <!-- 属性KeyProperty为主键对应的类属性 -->
   <mapper namespace="com.iaoe.jwExp.dao.ShopDao">
   	<insert id="insertShop" useGeneratedKeys="true"
   		keyProperty="shopId" keyColumn="shop_id">
   		<!-- 具体的sql -->
   		<!-- #{为输入的信息} -->
   		INSERT INTO
   		tb_shop(owner_id,area_id,shop_category_id,
   		shop_name,shop_desc,shop_addr,phone,shop_img,
   		priority,create_time,last_edit_time,
   		enable_status,advice)
   		VALUES
   		(#{owner.userId},#{area.areaId},#{shopCategory.shopCategoryId},
   		#{shopName},#{shopDesc},#{shopAddr},#{phone},#{shopImg},
   		#{priority},#{createTime},#{lastEditTime},
   		#{enableStatus},#{advice})
   	</insert>
   </mapper>
   ```

3. `junit`单元测试，在`src/mian/test`里的`com.iaoe.jwExp.dao`包中的`ShopDaoTest.java`

   ```java
   package com.iaoe.jwExp.dao;
   
   import static org.junit.Assert.assertEquals;
   
   import java.util.Date;
   
   import org.junit.Test;
   import org.springframework.beans.factory.annotation.Autowired;
   
   import com.iaoe.jwExp.BaseTest;
   import com.iaoe.jwExp.entity.Area;
   import com.iaoe.jwExp.entity.PersonInfo;
   import com.iaoe.jwExp.entity.Shop;
   import com.iaoe.jwExp.entity.ShopCategory;
   
   public class ShopDaoTest extends BaseTest{
   	@Autowired
   	private ShopDao shopDao;
   	@Test
   	public void testAInsertShop(){
           //插入时的准备
   		Shop shop = new Shop();
   		PersonInfo owner = new PersonInfo();
   		Area area = new Area();
   		ShopCategory sc = new ShopCategory();
   		owner.setUserId(1L);	//Long类型数字
   		area.setAreaId(2);
   		sc.setShopCategoryId(1L);
   		shop.setOwner(owner);
   		shop.setArea(area);
   		shop.setShopCategory(sc);
   		shop.setShopName("mytest1");
   		shop.setShopDesc("mytest1");
   		shop.setShopAddr("testaddr1");
   		shop.setPhone("12345678901");
   		shop.setShopImg("test1");
   		shop.setPriority(1);
   		shop.setCreateTime(new Date());
   		shop.setLastEditTime(new Date());
   		shop.setEnableStatus(1);
   		shop.setAdvice("审核中");
   		int effectedNum = shopDao.insertShop(shop);
   		assertEquals(1, effectedNum);	//断言
   	}
   }
   ```

### （改）DAO层更新店铺功能的实现

1. 在之前的dao层的ShopDao文件中添加下面的内容

   ```java
   	/**
   	 * 更新店铺信息
   	 * @param shop
   	 * @return
   	 */
   	int updateShop(Shop shop);
   ```

2. 在之前的mapper文件中添加以下内容

   ```xml
   <!-- id为方法名，priameterType为传入的参数 -->
   <!-- set中的if指的是如果传入的参数里的值为null则不更新这个数据 -->
   <update id="updateShop"
           parameterType="com.iaoe.jwExp.entity.Shop">
       update tb_shop
       <set>
           <if test="shopName != null">shop_name=#{shopName},</if>
           <if test="shopDesc != null">shop_desc=#{shopDesc},</if>
           <if test="shopAddr != null">shop_addr=#{shopAddr},</if>
           <if test="phone != null">phone=#{phone},</if>
           <if test="shopImg != null">shop_img=#{shopImg},</if>
           <if test="priority != null">priority=#{priority},</if>
           <if test="lastEditTime != null">last_edit_time=#{lastEditTime},</if>
           <if test="enableStatus != null">enable_status=#{enableStatus},</if>
           <if test="advice != null">advice=#{advice},</if>
           <if test="area != null">area_id=#{area.areaId},</if>
           <!-- 注意这里前面有逗号，下面这一句没有，代表结尾 -->
           <if test="shopCategory != null">shop_category_id=#{shopCategory.shopCategoryId}</if>
       </set>
       where shop_id=#{shopId}
   </update>
   ```

3. junit测试，在之前的文件新增,之前test的代码加入`@Ignore`忽视掉

   ```java
   @Test
   public void testUpdateShop(){
       Shop shop = new Shop();
       shop.setShopId(2L);
       shop.setShopDesc("更新操作描述");
       shop.setShopAddr("更新操作地址");
       shop.setLastEditTime(new Date());
       int effectedNum = shopDao.updateShop(shop);
       assertEquals(1, effectedNum);
   }
   ```

### （工具类）Thumbnailator—图片的处理和封装Util

> 这里主要展示的是关于图片的操作，可以缩放，加水印，压缩图片等，主要是用于该程序中有关图片的操作

1. `porm.xml`导入这个包`Thumbnailator`

   ```xml
   <!-- https://mvnrepository.com/artifact/net.coobird/thumbnailator -->
   <dependency>
       <groupId>net.coobird</groupId>
       <artifactId>thumbnailator</artifactId>
       <version>0.4.8</version>
   </dependency>
   ```

2. 简易学习

   在`src\main\java`里的`com.iaoe.jwExp.util`创建这个图片处理类`ImageUtil`

   > [Github使用教程](https://github.com/coobird/thumbnailator)

   ```JAVA
   package com.iaoe.jwExp.util;
   
   import java.io.File;
   import java.io.IOException;
   
   import javax.imageio.ImageIO;
   
   import net.coobird.thumbnailator.Thumbnails;
   import net.coobird.thumbnailator.geometry.Positions;
   
   public class ImageUtil {
   	public static void main(String[] args) throws IOException {
   		// 这里我在src/main/resources里面放了一个watermark.png,用于src/main/resources的路径在实际的硬盘中不同
   		// 所以这里这个方法用于获取classpath的实际路径:/D:/java_WorkSpace/jwExp/target/classes/
   		String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
   		//watermark的三个参数为水印的位置，水印的位置以及水印的透明度
   		//outputQuality为输出的质量
   		Thumbnails.of(new File("test.jpg")).size(200, 200)
   				.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "watermark.png")), 0.25f)
   				.outputQuality(0.8f).toFile(new File("‪test1.jpg"));
   	}
   }
   ```

3. 实际使用

   由于需要知道图片的输入路径，这里就新建一个util类PathUtil用于处理地址

   ```java
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
   		String imagePath = "upload/item/shop" + shopId + "/";
   		return imagePath.replace("/", separator);
   	}
   }
   ```

4. 修改ImageUtil

   ```java
   package com.iaoe.jwExp.util;
   
   import java.io.File;
   import java.io.IOException;
   import java.text.SimpleDateFormat;
   import java.util.Date;
   import java.util.Random;
   
   import javax.imageio.ImageIO;
   
   import org.springframework.web.multipart.commons.CommonsMultipartFile;
   
   import net.coobird.thumbnailator.Thumbnails;
   import net.coobird.thumbnailator.geometry.Positions;
   
   public class ImageUtil {
   	// 这里我在src/main/resources里面放了一个watermark.png,用于src/main/resources的路径在实际的硬盘中不同
   	// 所以这里这个方法用于获取classpath的实际路径:/D:/java_WorkSpace/jwExp/target/classes/
   	private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
   	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
   	private static final Random r = new Random();
   	/**
   	 * 存储图片
   	 * @param thumbnail	这个是spring里的文件格式
   	 * @param targetAddr 这个是存储的图片路径
   	 * @return
   	 */
   	public static String generateThumbnail(CommonsMultipartFile thumbnail,String targetAddr) {
   		//取一个时间+随机的文件名
   		String realFileName = getRandomFileName();
   		//获取文件扩展名
   		String extension = getFileExtension(thumbnail);
   		//创建目标路径
   		makeDirPath(targetAddr);
   		//组合相对路径名
   		String relativeAddr = targetAddr + realFileName + extension;
   		//和basePath相互结合成为绝对路径,就是文件输出的路径
   		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
   		//操作图片
   		try {
   			Thumbnails.of(thumbnail.getInputStream()).size(200, 200)
   			.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "watermark.png")), 0.25f)
   			.outputQuality(0.8f).toFile(dest);
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
   	private static String getFileExtension(CommonsMultipartFile cFile) {
   		String originalFileName = cFile.getOriginalFilename();
   		//获取最后一个点之后的东西，也就是扩展名
   		return originalFileName.substring(originalFileName.lastIndexOf("."));
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
   
   }
   ```

### （思维的转变）service层实现注册店铺功能

> 从dao层到service，dto，enums层  

​	这里由于之前学的框架没有中间这个service层，之所以出现这个层，主要也是为了高内聚低耦合，dao层负责最简单的某个表增删查改的方法的实现，面向数据库的，而service层的主要作用是面向应用的，例如我注册一个账户，往往不只是在数据库的用户表里添加一个用户记录那么简单，还有像保存用户照片，监测用户是否存在等操作，那么这里就交给service层来操作了


​	那么值得一提的是在监测操作是否成功等获取操作状态的时候，又在dao层中隔离了出一个dto层，而enums层用于枚举一些可能发生的状态

   1. 在`src\main\java`里的`com.iaoe.jwExp.enums`里创建一个`ShopStateEnum`的枚举类

      ```java
      package com.iaoe.jwExp.enums;
      
      //枚举类型是一个特殊的类，可以当类来使用
      public enum ShopStateEnum {
      	//枚举操作可能存在的状态
      	CHECK(0, "审核中"), OFFLINE(-1, "非法商铺"), SUCCESS(1, "操作成功"), PASS(2, "通过认证"), INNER_ERROR(-1001, "操作失败"),
      	NULL_SHOPID(-1002, "ShopId为空"), NULL_SHOP_INFO(-1003, "传入了空的信息");
      
      	private int state;
      	private String stateInfo;
      
      	//构造函数，设为私有，不让其他第三方应用修改原有的值
      	private ShopStateEnum(int state, String stateInfo) {
      		this.state = state;
      		this.stateInfo = stateInfo;
      	}
      
      	//获取状态码和状态的具体信息
      	public int getState() {
      		return state;
      	}
      
      	public String getStateInfo() {
      		return stateInfo;
      	}
      
      	//根据传入的state返回相应的enum值
      	public static ShopStateEnum stateOf(int index) {
      		for (ShopStateEnum state : values()) {
      			if (state.getState() == index) {
      				return state;
      			}
      		}
      		return null;
      	}
      }
      ```

   2. 在`src\main\java`里的`com.iaoe.jwExp.dto`包里面创建一个`ShopExecution`类

      ```java
      package com.iaoe.jwExp.dto;
      
      import java.util.List;
      
      import com.iaoe.jwExp.entity.Shop;
      import com.iaoe.jwExp.enums.ShopStateEnum;
      
      public class ShopExecution {
      	//结果标识
      	private int state;
      	//结果信息
      	private String stateInfo;
      	//店铺数量
      	private int count;
      	//操作的shop（增删改店铺的时候用到）
      	private Shop shop;
      	//shop列表（查询店铺列表的时候用到）
      	private List<Shop> shopList;
      	
      	public ShopExecution() {
      		
      	}
      	
      	//店铺操作失败的时候构造器
      	public ShopExecution(ShopStateEnum stateEnum) {
      		this.state = stateEnum.getState();
      		this.stateInfo = stateEnum.getStateInfo();
      	}
      	//增删改店铺操作成功的构造器
      	public ShopExecution(ShopStateEnum stateEnum,Shop shop) {
      		this.state = stateEnum.getState();
      		this.stateInfo = stateEnum.getStateInfo();
      		this.shop = shop;
      	}
      	//查询店铺列别时成功的构造器
      	public ShopExecution(ShopStateEnum stateEnum,List<Shop> shopList) {
      		this.state = stateEnum.getState();
      		this.stateInfo = stateEnum.getStateInfo();
      		this.shopList = shopList;
      	}
      
      	public int getState() {
      		return state;
      	}
      
      	public void setState(int state) {
      		this.state = state;
      	}
      
      	public String getStateInfo() {
      		return stateInfo;
      	}
      
      	public void setStateInfo(String stateInfo) {
      		this.stateInfo = stateInfo;
      	}
      
      	public int getCount() {
      		return count;
      	}
      
      	public void setCount(int count) {
      		this.count = count;
      	}
      
      	public Shop getShop() {
      		return shop;
      	}
      
      	public void setShop(Shop shop) {
      		this.shop = shop;
      	}
      
      	public List<Shop> getShopList() {
      		return shopList;
      	}
      
      	public void setShopList(List<Shop> shopList) {
      		this.shopList = shopList;
      	}
      }
      ```

   3. 在`com.iaoe.jwExp.service`里创建`ShopService`接口

      ```java
      package com.iaoe.jwExp.service;
      
      import java.io.File;
      
      import com.iaoe.jwExp.dto.ShopExecution;
      import com.iaoe.jwExp.entity.Shop;
      
      public interface ShopService {
          //注意这里返回的是操作的状态，也就是我们的dto层里面的类
      	ShopExecution addShop(Shop shop,File shopImg);
      }
      ```

   4. 在`com.iaoe.jwExp.service.impl`里实现该接口，创建`ShopServiceImpl`

      ```java
      package com.iaoe.jwExp.service.impl;
      
      import java.util.Date;
      
      import org.springframework.beans.factory.annotation.Autowired;
      import org.springframework.transaction.annotation.Transactional;
      import org.springframework.web.multipart.commons.CommonsMultipartFile;
      
      import com.iaoe.jwExp.dao.ShopDao;
      import com.iaoe.jwExp.dto.ShopExecution;
      import com.iaoe.jwExp.entity.Shop;
      import com.iaoe.jwExp.enums.ShopStateEnum;
      import com.iaoe.jwExp.exceptions.ShopOperationException;
      import com.iaoe.jwExp.service.ShopService;
      import com.iaoe.jwExp.util.ImageUtil;
      import com.iaoe.jwExp.util.PathUtil;
      
      @Service
      public class ShopServiceImpl implements ShopService{
      	@Autowired
      	private ShopDao shopDao;
      	
      	//使用Transactional保证事务的原子性
      	@Override
      	@Transactional
      	public ShopExecution addShop(Shop shop, CommonsMultipartFile shopImg) {
      		//空值判断
      		if(shop==null) {
      			return new ShopExecution(ShopStateEnum.NULL_SHOP_INFO);
      		}
      		try {
      			//给店铺信息赋初始值
      			shop.setEnableStatus(0);
      			shop.setCreateTime(new Date());
      			shop.setLastEditTime(new Date());
      			int effectedNum = shopDao.insertShop(shop);
      			if(effectedNum<=0) {
      				//这里要用RuntimeException才能保证原子性
      				throw new ShopOperationException("店铺创建失败");
      			} else {
      				if(shopImg!=null) {
      					try {
      						//如果参数时引用类型的话，那么传递的是指针
      						addShopImg(shop,shopImg);
      					}catch(Exception e) {
      						throw new ShopOperationException("addShopImg error:"+e.getMessage());
      					}
      					//更新店铺地址
      					effectedNum = shopDao.updateShop(shop);
      					if(effectedNum<=0) {
      						throw new ShopOperationException("更新图片地址失败");
      					}
      				}
      			}
      		} catch(Exception e) {
      			throw new ShopOperationException("addShop error:"+e.getMessage());
      		}
      		return new ShopExecution(ShopStateEnum.CHECK,shop);
      	}
      	//添加图片
      	private void addShopImg(Shop shop,CommonsMultipartFile shopImg) {
      		//获取shop图片的相对值路径
      		String dest = PathUtil.getShopImagePath(shop.getShopId());
      		String shopImageAddr = ImageUtil.generateThumbnail(shopImg, dest);
      		shop.setShopImg(shopImageAddr);
      	}
      }
      ```

5. 这里面的RuntimeException被我换成了ShopOperationException，这样写比较规范，你也可以换回去，如果要继续使用的话需要在`src\main\java`里面的`com.iaoe.jwExp.exceptions`里面新建`ShopOperationException`类

   ```java
   package com.iaoe.jwExp.exceptions;
   
   public class ShopOperationException extends RuntimeException{
   
   	private static final long serialVersionUID = 1L;
   	
   	public ShopOperationException(String msg) {
   		super(msg);
   	}
   }
   ```

6. 使用单元测试，在`src/test/java`里的`com.iaoe.jwExp.service`里面创建一个ShopServiceTest类

   ```java
   
   ```

   