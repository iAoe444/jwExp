# ssm笔记-项目实战

> 书写于2019/06/25  9:59

## 1 店铺注册功能的实现

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

> 从dao层到service，dto，enums层  ，**代码部分有误**

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
   package com.iaoe.jwExp.service;
   
   ```

import static org.junit.Assert.assertEquals;

   import java.io.File;
   import java.util.Date;

   import org.junit.Test;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.web.multipart.commons.CommonsMultipartFile;

   import com.iaoe.jwExp.BaseTest;
   import com.iaoe.jwExp.dto.ShopExecution;
   import com.iaoe.jwExp.entity.Area;
   import com.iaoe.jwExp.entity.PersonInfo;
   import com.iaoe.jwExp.entity.Shop;
   import com.iaoe.jwExp.entity.ShopCategory;
   import com.iaoe.jwExp.enums.ShopStateEnum;

   public class ShopServiceTest extends BaseTest{
   	@Autowired
   	private ShopService shopService;
   	
   	@Test
   	public void testAddShop(){
   		Shop shop = new Shop();
   		PersonInfo owner = new PersonInfo();
   		Area area = new Area();
   		ShopCategory sc = new ShopCategory();
   		owner.setUserId(1L);
   		area.setAreaId(2);
   		sc.setShopCategoryId(1L);
   		shop.setOwner(owner);
   		shop.setArea(area);
   		shop.setShopCategory(sc);
   		shop.setShopName("service测试");
   		shop.setShopDesc("mytest1");
   		shop.setShopAddr("testaddr1");
   		shop.setPhone("12345678901");
   		shop.setPriority(1);
   		shop.setCreateTime(new Date());
   		shop.setLastEditTime(new Date());
   		//为了规范点改成这个
   		shop.setEnableStatus(ShopStateEnum.CHECK.getState());
   		shop.setAdvice("审核中");
   		File shopImg = new File("C:\\Users\\iAoe\\Desktop\\1.jpg");
   		ShopExecution se = shopService.addShop(shop, shopImg);
   		assertEquals(ShopStateEnum.CHECK.getState(), se.getState());
   	}
   }
   ```
   

### SUI-mobile 前端页面的展示

> 这里使用的是sui-moblile，官网地址为[SUI Mobile](http://m.sui.taobao.org/)，感觉有点老了，没办法，先跟着走一遍先，这个框架是由淘宝推出的，主要好处是响应式布局以及防ios的界面，和jquery类似，导入一些css和script就行

​```html
<link rel="stylesheet" href="//g.alicdn.com/msui/sm/0.6.2/css/sm.min.css">
<link rel="stylesheet" href="//g.alicdn.com/msui/sm/0.6.2/css/sm-extend.min.css">

<script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
<script type='text/javascript' src='//g.alicdn.com/msui/sm/0.6.2/js/sm.min.js' charset='utf-8'></script>
<script type='text/javascript' src='//g.alicdn.com/msui/sm/0.6.2/js/sm-extend.min.js' charset='utf-8'></script>
   ```

这里为了快速开发，直接在原有的案例基础上修改[SUI Mobile Demo](http://m.sui.taobao.org/demos/)，大致生成的页面如图，具体源码太大就不贴了

![](https://ws1.sinaimg.cn/large/006bBmqIgy1g4ee9rc0amj30ck0nj74m.jpg)

### (路由) Controller实现页面的跳转

> 在上面我们已经实现了web页面，假定为`shopoperation.html`，如果放在`/jwExp/src/main/webapp`这个目录下的话，那么跳转的路径是`http://localhost:8080/jwExp/shopoperation.html`，我们知道，如果单纯的将html暴露在路径上是不好管理的，我们需要将其统一管理起来，那么就可以通过controller来实现，这就是controller的第一个功能：路由

1. 将`shopoperation.html`迁移到`/jwExp/src/main/webapp/WEB-INF/html/shop/shopoperation.html`下面，我们知道，如果将文件放在WEB-INF目录下的话，是没办法直接通过路径访问的，像`http://localhost:8080/jwExp/WEB-INF/html/shop/shopoperation.html`是没办法到达这个页面的

2. 在`com.iaoe.jwExp.shopAdmin`书写我们的controller层，`ShopAdminController`

   ```java
   package com.iaoe.jwExp.web.shopadmin;
   
   import org.springframework.stereotype.Controller;
   import org.springframework.web.bind.annotation.RequestMapping;
   import org.springframework.web.bind.annotation.RequestMethod;
   
   //这个控制层用于控制前端的地址，相当于路由
   @Controller
   @RequestMapping(value = "/shopadmin",method=RequestMethod.GET)
   public class ShopAdminController {
   	@RequestMapping(value="/shopoperation")
   	public String shopOperation() {
   		return "shop/shopoperation";
   	}
   }
   ```

   通过`requestMapping`我们可以得知，这个路由的路径是`http://localhost:8080/shopadmin/shopoperation`，那么为什么可以到达这个我们设置的html页面呢？之前我们配置spring-web.xml的时候里面有这么一段

   ```xml
   	<!-- 3.定义视图解析器 -->
   	<bean id="viewResolver"
   		class="org.springframework.web.servlet.view.InternalResourceViewResolver">
   		<property name="prefix" value="/WEB-INF/html/"></property>
   		<property name="suffix" value=".html"></property>
   	</bean>
   ```

   也就是说return回来的是`shop/shopoperation`这个路径，但是已经给它加了前后缀`/WEB-INF/html/`和`.html`了，所以能够顺利访问到这个页面

   ![](https://ws1.sinaimg.cn/large/006bBmqIly1g4eeydcqwrj30u10cnab8.jpg)

### （前后端对接）Controller完成店铺注册功能

1. controller实现注册接口，这里需要解析前端发来的信息和图片

   ```java
    package com.iaoe.jwExp.web.shopadmin;
   
   import java.util.HashMap;
   import java.util.Map;
   
   import javax.servlet.http.HttpServletRequest;
   
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.stereotype.Controller;
   import org.springframework.web.bind.annotation.RequestMapping;
   import org.springframework.web.bind.annotation.RequestMethod;
   import org.springframework.web.bind.annotation.ResponseBody;
   import org.springframework.web.multipart.MultipartHttpServletRequest;
   import org.springframework.web.multipart.commons.CommonsMultipartFile;
   import org.springframework.web.multipart.commons.CommonsMultipartResolver;
   
   import com.fasterxml.jackson.databind.ObjectMapper;
   import com.iaoe.jwExp.dto.ShopExecution;
   import com.iaoe.jwExp.entity.PersonInfo;
   import com.iaoe.jwExp.entity.Shop;
   import com.iaoe.jwExp.enums.ShopStateEnum;
   import com.iaoe.jwExp.service.ShopService;
   import com.iaoe.jwExp.util.HttpServletRequestUtil;
   
   @Controller
   @RequestMapping("/shopadmin")
   public class ShopManagementController {
   	@Autowired
   	private ShopService shopService;
   	
   	/**
   	 * 注册店铺功能
   	 * @param request
   	 * @return
   	 */
   	@RequestMapping(value = "/registershop", method = RequestMethod.POST)
   	@ResponseBody
   	private Map<String, Object> registerShop(HttpServletRequest request) {
   		Map<String, Object> modelMap = new HashMap<String, Object>();
   		// 1.接受前端发来的请求并转换相应的参数，包括前端发来的店铺信息和图片信息
   		// 这里先获取请求里面有个叫shopStr的参数
   		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
   		// 新建一个用于解析json的对象，这个在之前maven的导包里面有一个json解析包
   		ObjectMapper mapper = new ObjectMapper();
   		Shop shop = null;
   		try {
   			// 将json解析到shop这个类里面
   			shop = mapper.readValue(shopStr, Shop.class);
   		} catch (Exception e) {
   			// 如果出现意外，那么返回的错误信息
   			modelMap.put("success", false);
   			modelMap.put("errMsg", e.getMessage());
   			return modelMap;
   		}
   		// 这里用于获取请求里面的图片
   		CommonsMultipartFile shopImg = null;
   		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
   				request.getSession().getServletContext());
   		//判断是否存在这个文件流
   		if(commonsMultipartResolver.isMultipart(request)) {
   			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
   			//获取参数为shopImg的图片
   			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
   		}else {
   			modelMap.put("success", false);
   			modelMap.put("errMsg", "上传图片不能为空");
   			return modelMap;
   		}
   		//2.注册店铺功能
   		if(shopImg!=null && shop!=null) {
   			PersonInfo owner = new PersonInfo();
   			owner.setUserId(1L);
   			shop.setOwner(owner);
   			ShopExecution se = shopService.addShop(shop, shopImg);
   			if(se.getState()==ShopStateEnum.CHECK.getState()) {
   				modelMap.put("success", true);
   			}else {
   				//如果添加失败，返回之前枚举类型的失败原因
   				modelMap.put("success", false);
   				modelMap.put("errMsg", se.getStateInfo());
   			}
   			return modelMap;
   		}else {
   			modelMap.put("success", false);
   			modelMap.put("errMsg", "店铺信息不能为空");
   		}
   		return modelMap;
   	}
   }
   ```

2. js实现提交表单和获取数据，主要实现的功能是在进入这个页面的时候将区域信息和店铺分类信息更新在页面，这里我们放在`src/main/webapp/resources/js/shop`里，命名为shopoperation.js

```javascript
/**
 * 这个是shopoperation获取后台信息以及提交页面信息的操作
 */
$(function(){
	var initUrl = '/jwExp/o2o/getshopinitinfo';
	var registerShopUrl = '/jwExp/shopadmin/registershop';
	alert(initUrl);
	getShopInitInfo();
	//获取区域信息和分类信息
	function getShopInitInfo(){
		//建立连接，获取返回的信息
		$.getJSON(initUrl,function(data){
			if(data.success){
				var tempHtml = '';
				var tempAreaHtml = '';
				//遍历分类信息
				data.shopCategoryList.map(function(item, index) {
					tempHtml += '<option data-id="' + item.shopCategoryId
							+ '">' + item.shopCategoryName + '</option>';
				});
				//遍历区域信息
				data.areaList.map(function(item, index) {
					tempAreaHtml += '<option data-id="' + item.areaId + '">'
							+ item.areaName + '</option>';
				});
				//将信息放到相应的区域中
				$('#shop-category').html(tempHtml);
				$('#area').html(tempAreaHtml);
			}
		});
	}
	//点击提交后提交内容信息
	$('#submit').click(function(){
		var shop = {};

		//获取表单中的信息
		shop.shopName = $('#shop-name').val();
		shop.shopAddr = $('#shop-addr').val();
		shop.phone = $('#shop-phone').val();
		shop.shopDesc = $('#shop-desc').val();

		//shopCategory通过这种方式来获取子选项
		shop.shopCategory = {
			shopCategoryId : $('#shop-category').find('option').not(function() {
				return !this.selected;
			}).data('id')
		};
		shop.area = {
			areaId : $('#area').find('option').not(function() {
				return !this.selected;
			}).data('id')
		};

		//获取图片流
		var shopImg = $("#shop-img")[0].files[0];
		var formData = new FormData();
		formData.append('shopImg', shopImg);
		//将shop通过json的方式传递
		formData.append('shopStr', JSON.stringify(shop));
//		var verifyCodeActual = $('#j_captcha').val();
//		if (!verifyCodeActual) {
//			$.toast('请输入验证码！');
//			return;
//		}
//		formData.append("verifyCodeActual", verifyCodeActual);
		$.ajax({
			url : registerShopUrl,
			type : 'POST',
			// contentType: "application/x-www-form-urlencoded; charset=utf-8",
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if (data.success) {
					$.toast('提交成功！');
//					if (isEdit){
//						$('#captcha_img').click();
//					} else{
//						window.location.href="/shop/shoplist";
//					}
				} else {
					$.toast('提交失败！' + data.errMsg);
//					$('#captcha_img').click();
				}
			}
		});
	});
})
```

注意，在html里引入的时候的时候，位置为`../resources/js/shop/shopoperation.js`

```java
package com.iaoe.jwExp.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.iaoe.jwExp.BaseTest;
import com.iaoe.jwExp.dao.ShopCategoryDao;
import com.iaoe.jwExp.entity.ShopCategory;

public class ShopCategoryDaoTest extends BaseTest{
	@Autowired
	private ShopCategoryDao shopCategoryDao;
	
	@Test
	public void testQueryShopCategory(){
		List<ShopCategory> shopCategoryList = shopCategoryDao.queryShopCategory(new ShopCategory());
		assertEquals(1, shopCategoryList.size());
	}
	
}

```

### （其他功能）实现验证码功能

> 这里的验证码使用的是[Maven Repository: com.github.penggle » kaptcha](https://mvnrepository.com/artifact/com.github.penggle/kaptcha)这个验证码工具

```xml
<!-- https://mvnrepository.com/artifact/com.github.penggle/kaptcha -->
<dependency>
    <groupId>com.github.penggle</groupId>
    <artifactId>kaptcha</artifactId>
    <version>2.3.2</version>
</dependency>
```

1. 需要在web.xml里面添加如下代码

   ```xml
   	<servlet>
   		<!-- 生成图片的Servlet -->
   		<servlet-name>Kaptcha</servlet-name>
   		<servlet-class>com.google.code.kaptcha.servlet.KaptchaServlet</servlet-class>
   
   		<!-- 是否有边框 -->
   		<init-param>
   			<param-name>kaptcha.border</param-name>
   			<param-value>no</param-value>
   		</init-param>
   		<!-- 字体颜色 -->
   		<init-param>
   			<param-name>kaptcha.textproducer.font.color</param-name>
   			<param-value>red</param-value>
   		</init-param>
   		<!-- 图片宽度 -->
   		<init-param>
   			<param-name>kaptcha.image.width</param-name>
   			<param-value>135</param-value>
   		</init-param>
   		<!-- 使用哪些字符生成验证码 -->
   		<init-param>
   			<param-name>kaptcha.textproducer.char.string</param-name>
   			<param-value>ACDEFHKPRSTWX345679</param-value>
   		</init-param>
   		<!-- 图片高度 -->
   		<init-param>
   			<param-name>kaptcha.image.height</param-name>
   			<param-value>50</param-value>
   		</init-param>
   		<!-- 字体大小 -->
   		<init-param>
   			<param-name>kaptcha.textproducer.font.size</param-name>
   			<param-value>43</param-value>
   		</init-param>
   		<!-- 干扰线的颜色 -->
   		<init-param>
   			<param-name>kaptcha.noise.color</param-name>
   			<param-value>black</param-value>
   		</init-param>
   		<!-- 字符个数 -->
   		<init-param>
   			<param-name>kaptcha.textproducer.char.length</param-name>
   			<param-value>4</param-value>
   		</init-param>
   		<!-- 使用哪些字体 -->
   		<init-param>
   			<param-name>kaptcha.textproducer.font.names</param-name>
   			<param-value>Arial</param-value>
   		</init-param>
   	</servlet>
   	<!-- 映射的url -->
   	<servlet-mapping>
   		<servlet-name>Kaptcha</servlet-name>
   		<url-pattern>/Kaptcha</url-pattern>
   	</servlet-mapping>
   ```

2. 接着需要在原来的html里面加入验证码层

   ```html
   <li>
       <div class="item-content">
           <div class="item-media">
               <i class="icon icon-form-email"></i>
           </div>
           <div class="item-inner">
               <label for="j_captcha" class="item-title label">验证码</label> <input
                                                                                  id="j_captcha" name="j_captcha" type="text"
                                                                                  class="form-control in" placeholder="验证码" />
               <div class="item-input">
                   <img id="captcha_img" alt="点击更换" title="点击更换"
                        onclick="changeVerifyCode(this)" src="../Kaptcha" />
               </div>
           </div>
       </div>
   </li>
   ```

   这里还有一个changeVerifyCode的方法，方法如下

   ```javascr
   function changeVerifyCode(img) {
   	//生成随机数
   	img.src = "../Kaptcha?" + Math.floor(Math.random() * 100);
   }
   ```

3. 修改之前的shopoperation.js代码

   ```javascript
   //点击提交后提交内容信息
   	$('#submit').click(function(){
   		var shop = {};
   
   		//获取表单中的信息
   		shop.shopName = $('#shop-name').val();
   		shop.shopAddr = $('#shop-addr').val();
   		shop.phone = $('#shop-phone').val();
   		shop.shopDesc = $('#shop-desc').val();
   
   		//shopCategory通过这种方式来获取子选项
   		shop.shopCategory = {
   			shopCategoryId : $('#shop-category').find('option').not(function() {
   				return !this.selected;
   			}).data('id')
   		};
   		shop.area = {
   			areaId : $('#area').find('option').not(function() {
   				return !this.selected;
   			}).data('id')
   		};
   
   		//获取图片流
   		var shopImg = $("#shop-img")[0].files[0];
   		var formData = new FormData();
   		formData.append('shopImg', shopImg);
   		//将shop通过json的方式传递
   		formData.append('shopStr', JSON.stringify(shop));
           //+++++++++++++++++++++++++++++++++++++++++++++++++++
   		//获取验证码内容
   		var verifyCodeActual = $('#j_captcha').val();
   		//如果为空
   		if (!verifyCodeActual) {
   			$.toast('请输入验证码！');
   			return;
   		}
   		formData.append("verifyCodeActual", verifyCodeActual);
           //+++++++++++++++++++++++++++++++++++++++++++++++++++
   		$.ajax({
   			url : registerShopUrl,
   			type : 'POST',
   			// contentType: "application/x-www-form-urlencoded; charset=utf-8",
   			data : formData,
   			contentType : false,
   			processData : false,
   			cache : false,
   			success : function(data) {
   				if (data.success) {
   					$.toast('提交成功！');
   				} else {
   					$.toast('提交失败！' + data.errMsg);
                        //+++++++++++++++++++++++++++++++++++++++++++++++++++
   					$('#captcha_img').click();
                        //+++++++++++++++++++++++++++++++++++++++++++++++++++
   				}
   			}
   		});
   	});
   ```

4. 创建一个util类CodeUtil便于在controller层验证验证码

   ```java
   package com.iaoe.jwExp.util;
   
   import javax.servlet.http.HttpServletRequest;
   
   /**
    * 用于对比验证码的正确性
    * @author iAoe
    *
    */
   public class CodeUtil {
   	public static boolean checkVerifyCode(HttpServletRequest request) {
   		//获取真实的验证码
   		String verifyCodeExpected = (String) request.getSession().getAttribute(
   				com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
   		//获取实际填写的验证码
   		String verifyCodeActual = HttpServletRequestUtil.getString(request, "verifyCodeActual");
   		if(verifyCodeActual==null||!verifyCodeActual.equals(verifyCodeExpected)) {
   			return false;
   		}
   		return true;
   	}
   }
   ```

5. 在原有的Controller层的`ShopManagementController`里面加入验证码的验证

   ```java
   /**
   	 * 注册店铺功能
   	 * @param request
   	 * @return
   	 */
   	@RequestMapping(value = "/registershop", method = RequestMethod.POST)
   	@ResponseBody
   	private Map<String, Object> registerShop(HttpServletRequest request) {
   		Map<String, Object> modelMap = new HashMap<String, Object>();
   		if(!CodeUtil.checkVerifyCode(request)) {
   			modelMap.put("success", false);
   			modelMap.put("errMsg", "验证码错误");
   			return modelMap;
   		}
       //...........后面的省略了
   ```

6. 在这里由于上传文件的功能还未添加，需要导入一些包

   `porm.xml`

   ```xml
   <!-- 文件上传的jar包 -->
   <!-- https://mvnrepository.com/artifact/commons-fileupload/commons-fileupload -->
   <dependency>
       <groupId>commons-fileupload</groupId>
       <artifactId>commons-fileupload</artifactId>
       <version>1.3.2</version>
   </dependency>
   ```

   `spring-web.xml`

   ```xml
   <!-- 文件上传解析器 -->
   <bean id="multipartResolver"
         class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
       <property name="defaultEncoding" value="utf-8"></property>
       <property name="maxUploadSize" value="20971520"></property><!-- 最大上传文件大小单位是字节这里是20M -->
       <property name="maxInMemorySize" value="20971520"></property>
   </bean>
   ```

## 2. 店铺信息编辑的实现

> 有了之前的经验，现在大致了解了ssm前后端的开发，接下来功能的实现上应该会比之前的路轻松的多

### （查询、表联接）Dao层实现店铺信息编辑

> 由于之前已经做了更新的操作，现在加上查询的操作，为什么呢？因为我们在编辑的时候，需要获取店铺的信息，再进行相应的修改

1. dao层`shopDao`添加如下接口

   ```java
   	/**
   	 * 通过shopId查询店铺
   	 * @param shopId
   	 * @return
   	 */
   	Shop queryByShopId(long shopId);
   ```

2. mapper`shopDao.xml`添加查询接口

>  这里需要注意的是，需要进行联接表的操作，因为里面的分类和地域为id，没有名字，这里需要先设置一个符合返回类型，用resutlMap来实现，接着才是对接口的实现

```xml
   <!-- 查询返回的符合数据类型 -->
   	<resultMap id="shopMap" type="com.iaoe.jwExp.entity.Shop">
   		<!-- 主键 -->
   		<!-- Column为数据库里的属性名 -->
   		<!-- property为entity里的属性名 -->
   		<id column="shop_id" property="shopId" />
   		<result column="shop_name" property="shopName" />
   		<result column="shop_desc" property="shopDesc" />
   		<result column="shop_addr" property="shopAddr" />
   		<result column="phone" property="phone" />
   		<result column="shop_img" property="shopImg" />
   		<result column="priority" property="priority" />
   		<result column="create_time" property="createTime" />
   		<result column="last_edit_time" property="lastEditTime" />
   		<result column="enable_status" property="enableStatus" />
   		<result column="advice" property="advice" />
   		<!-- 复合类型 -->
   		<association property="area" column="area_id"
   			javaType="com.iaoe.jwExp.entity.Area">
   			<id column="area_id" property="areaId" />
   			<result column="area_name" property="areaName" />
   		</association>
   		<association property="shopCategory"
   			column="shop_category_id"
   			javaType="com.iaoe.jwExp.entity.ShopCategory">
   			<id column="shop_category_id" property="shopCategoryId" />
   			<result column="shop_category_name"
   				property="shopCategoryName" />
   		</association>
   		<association property="owner" column="user_id"
   			javaType="com.iaoe.jwExp.entity.PersonInfo">
   			<id column="user_id" property="userId" />
   			<result column="name" property="name" />
   		</association>
   	</resultMap>
   	<!-- 查询方法实现 -->
   	<select id="queryByShopId" resultMap="shopMap"
   		parameterType="Long">
   		SELECT
   		s.shop_id,
   		s.shop_name,
   		s.shop_desc,
   		s.shop_addr,
   		s.phone,
   		s.shop_img,
   		s.priority,
   		s.create_time,
   		s.last_edit_time,
   		s.enable_status,
   		s.advice,
   		a.area_id,
   		a.area_name,
   		sc.shop_category_id,
   		sc.shop_category_name
   		FROM
   		tb_shop s,
   		tb_area a,
   		tb_shop_category sc
   		WHERE
   		s.area_id=a.area_id
   		AND
   		s.shop_category_id=sc.shop_category_id
   		AND
   		s.shop_id=#{shopId}
   	</select>
```

3. junit测试`shopDaoTest`

   ```java
   	@Test
   	public void testQueryByShopId(){
   		long shopId = 2;
   		Shop shop = shopDao.queryByShopId(shopId);
   		int areaId = shop.getArea().getAreaId();
   		System.out.println("areaId:"+shop.getArea().getAreaId());
   		System.out.println("areaName:"+shop.getArea().getAreaName());
   		assertEquals(2, areaId);
   	}   
   ```

![](https://ws1.sinaimg.cn/large/006bBmqIgy1g4fsvup0inj31580q6acq.jpg)

   

   

