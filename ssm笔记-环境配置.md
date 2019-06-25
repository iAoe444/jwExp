# SSM学习笔记

> 书写于2019/06/18 15:13

## 1 搭建开发环境

> 这里使用`maven`+`eclipse`+`tomcat`+`jdk1.8`进行搭建
>
> 教程里的是`tomcat8`+`jkd1.8`+`maven3.3.9`进行搭建

1. 配置tomcat

	> tomcat下载地址
	
	windows->preferences->server->runtime->add一个新的tomcat就行了
	
2. 配置maven

   > maven下载地址[Maven – Download Apache Maven](http://maven.apache.org/download.cgi)

   windows ->preferences->Maven->installations->add一个新的maven就行
   ![](https://ws1.sinaimg.cn/large/006bBmqIgy1g45dtcunozj30h9058a9x.jpg)

3. 创建maven工程

   file->new->others->搜索maven->选择maven project->next->选择下图的选项

   ![](https://ws1.sinaimg.cn/large/006bBmqIgy1g45dvrhorkj30he0cj0sz.jpg)

   ![](https://ws1.sinaimg.cn/large/006bBmqIgy1g45dweuhsfj30hn0cvgln.jpg)

   点击finish就行，接下来会maven会下载相关的配置文件，如果觉得下不动，可能就要科学上网了

4. 修复错误

   ![](https://ws1.sinaimg.cn/large/006bBmqIgy1g45dzt3wqlj30rd04lt8p.jpg)

   * 修复第一个error，需要将tomcat的一个包导入

     右击项目->Properties->java build path->library->add library->server runtime->选择tomcat版本->finish

   * 修复warning

     > [Build path specifies execution environment J2SE-1.5. There are no JREs installed in the workspace th - a214704的博客 - CSDN博客](https://blog.csdn.net/a214704/article/details/83659426)
     >
> Description	Resource	Path	Location	Type
     > Build path specifies execution environment J2SE-1.5. There are no JREs installed in the workspace that are strictly compatible with this environment. 	o2o		Build path	JRE System Library Problem

~~~xml
 当前的执行环境是j2se1.5，需要换成我们的1.8版本，修改根目录porn.xml
 
 ```xml
 <build>
        <finalName>campuso2o</finalName>
 </build>
 
 <!--修改为以下的代码-->
 
 <build>
 		<finalName>campuso2o</finalName>
 		<plugins>
 			<plugin>
 				<!-- https://mvnrepository.com/artifact/org.apache.maven.plugins/maven-
                      compiler-plugin 下面三行是从这个网址上找到的并去掉plugin标签-->
 				<groupId>org.apache.maven.plugins</groupId>
 				<artifactId>maven-compiler-plugin</artifactId>
 				<version>3.8.0</version>
                 <!--此处是关联jdk哪个版本和编码类型-->
 				<configuration>
 					<source>1.8</source>
 					<target>1.8</target>
 					<encoding>UTF8</encoding>
 				</configuration>
 			</plugin>
 		</plugins>
 </build>
 ```
 
 右击项目，maven->updateproject
~~~

5. 新建java resources文件夹`src/tests/resources`

   ![](https://ws1.sinaimg.cn/large/006bBmqIgy1g47d55pc0qj308s05f748.jpg)

   * 右击项目->properties->java build path->source->选择我们新建的文件夹->输入target/classes->apply

     ![](https://ws1.sinaimg.cn/large/006bBmqIgy1g47de23a9xj30dq05umx5.jpg)

6. 补充

   javaweb主要分为静态和动态两种，动态的页面包括动态的信息，jsp之类的，静态的页面是直接写死的,这里我们的是动态的，有一个选项是dynamic web module，如果选择的版本越高，解析的速度就越快

   * 选择properities->project->facets里面选择dynamic web module，由于在里面直接改的话是不行的，所以需要修改他的一个配置文件

   * 修改项目目录/.setting/org.eclipse.wst.common.project.facet.core.xml

     ```xml
     <?xml version="1.0" encoding="UTF-8"?>
     <faceted-project>
       <fixed facet="wst.jsdt.web"/>
       <installed facet="jst.web" version="3.1"/><!--修改为这个的版本-->
       <installed facet="wst.jsdt.web" version="1.0"/>
       <installed facet="java" version="1.8"/>
     </faceted-project>
     ```

   * 然后refresh一下项目，看一下properities->project->facets里面选择dynamic web module，看看是不是最新的版本

   * 修改src/main/webapp/web.xml文件

     ```xml
     <!DOCTYPE web-app PUBLIC
      "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
      "http://java.sun.com/dtd/web-app_2_3.dtd" >
     
     <web-app>
       <display-name>Archetype Created Web Application</display-name>
     </web-app>
     
     <!--修改为-->
     
     <web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     	xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
                           http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
     	version="3.1" metadata-complete="true">
       <display-name>Archetype Created Web Application</display-name>
     </web-app>
     ```

## 2 开始学习前

### 项目目录结构

* source folder里面的src/main/java主要放置的是业务的java代码
* src/main/resources主要放置的是项目的资源文件，比如各种spring、mybasics、日志配置文件
* src/test/java主要放置的是单元测试所涉及的java代码
* src/test/resources这个没什么，只是maven项目的话加这个比较符合规范
* src/main/webapp目录下用与放html之类的

### 新建source folder和package

* 新建source folder`src/main/resoures/spring`用于放spring的配置信息和`src/main/resources/mapper`用于放dao的实现类之类的
* 新建名为web的`package`，用于放置controller层的，存放控制器
* 新建名为service的`package`，用于放置业务逻辑层的
* 新建名为service.impl的`package`，用于实习业务逻辑层的
* 新建名为dao的`package`，用于与数据打交道的
* 新建名为dto的`package`，用于扩展entity的功能
* 新建名为enums的`package`，枚举类型存放位置
* 新建名为interceptor的`package`，拦截器
* 新建名为util的`package`，通用工具类

### 引入工程所需要jar包

原来的porm.xml

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.iaoe</groupId>
  <artifactId>jwExp</artifactId>
  <packaging>war</packaging>
  <version>0.0.1-SNAPSHOT</version>
  <name>jwExp Maven Webapp</name>
  <url>http://maven.apache.org</url>
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
  <build>
    <finalName>jwExp</finalName>
	  <plugins>
		<plugin>
			<!-- https://mvnrepository.com/artifact/org.apache.maven.plugins/maven-
	                   compiler-plugin 下面三行是从这个网址上找到的并去掉plugin标签-->
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-compiler-plugin</artifactId>
			<version>3.8.0</version>
	              <!--此处是关联jdk哪个版本和编码类型-->
			<configuration>
				<source>1.8</source>
				<target>1.8</target>
				<encoding>UTF8</encoding>
			</configuration>
		</plugin>
	</plugins>
  </build>
</project>
```

修改后

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.iaoe</groupId>
	<artifactId>jwExp</artifactId>
	<packaging>war</packaging>
	<version>0.0.1-SNAPSHOT</version>
	<name>jwExp Maven Webapp</name>
	<url>http://maven.apache.org</url>
	<properties>
		<spring.version>4.3.7.RELEASE</spring.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>4.12</version>
			<scope>test</scope>
		</dependency>
		<!-- https://mvnrepository.com/artifact/ch.qos.logback/logback-classic -->
		<!--单元测试用 -->
		<dependency>
			<groupId>ch.qos.logback</groupId>
			<artifactId>logback-classic</artifactId>
			<version>1.2.3</version>
		</dependency>
		<!-- 1)Spring核心 -->
		<!-- 包含spring的核心工具类 -->
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-core</artifactId>
			<version>${spring.version}</version>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-beans</artifactId>
			<version>${spring.version}</version>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-context</artifactId>
			<version>${spring.version}</version>
		</dependency>
		<!-- 2)Spring DAO层 -->
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-jdbc</artifactId>
			<version>${spring.version}</version>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-tx</artifactId>
			<version>${spring.version}</version>
		</dependency>
		<!-- 3)Spring web -->
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-web</artifactId>
			<version>${spring.version}</version>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-webmvc</artifactId>
			<version>${spring.version}</version>
		</dependency>
		<!-- 4)Spring test -->
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-test</artifactId>
			<version>${spring.version}</version>
		</dependency>
		<!-- json解析 -->
		<dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-databind</artifactId>
			<version>2.8.7</version>
		</dependency>
		<dependency>
			<groupId>javax.servlet</groupId>
			<artifactId>javax.servlet-api</artifactId>
			<version>3.1.0</version>
		</dependency>
		<!-- Map工具类 -->
		<dependency>
			<groupId>commons-collections</groupId>
			<artifactId>commons-collections</artifactId>
			<version>3.2</version>
		</dependency>
		<!-- DAO: MyBatis -->
		<dependency>
			<groupId>org.mybatis</groupId>
			<artifactId>mybatis</artifactId>
			<version>3.4.2</version>
		</dependency>
		<dependency>
			<groupId>org.mybatis</groupId>
			<artifactId>mybatis-spring</artifactId>
			<version>1.3.1</version>
		</dependency>
		<!-- 2.数据库 -->
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<version>5.1.37</version>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>c3p0</groupId>
			<artifactId>c3p0</artifactId>
			<version>0.9.1.2</version>
		</dependency>
	</dependencies>
	<build>
		<finalName>jwExp</finalName>
		<plugins>
			<plugin>
				<!-- https://mvnrepository.com/artifact/org.apache.maven.plugins/maven- 
					compiler-plugin 下面三行是从这个网址上找到的并去掉plugin标签 -->
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>3.8.0</version>
				<!--此处是关联jdk哪个版本和编码类型 -->
				<configuration>
					<source>1.8</source>
					<target>1.8</target>
					<encoding>UTF8</encoding>
				</configuration>
			</plugin>
		</plugins>
	</build>
</project>
```

### 配置数据库连接

在`src/main/resources`里新建jdbc.properties，里面这么写

```
jdbc.driver=com.mysql.jdbc.Driver	//用的mysql的驱动
jdbc.url=jdbc:mysql://localhost:3306/jwExp?useUnicode=true&characterEncoding=utf8
jdbc.username=root
jdbc.password=123456
```

在`src/main/resources`里新建`mybatis-config.xml`里面配置

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
  PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
	<!-- 配置全局属性 -->
	<settings>
		<!-- 使用jdbc的getGeneratedKeys获取数据库自增主键值 -->
		<setting name="useGeneratedKeys" value="true" />

		<!-- 使用列别名替换列名 默认:true -->
		<setting name="useColumnLabel" value="true" />

		<!-- 开启驼峰命名转换:Table{create_time} -> Entity{createTime} -->
		<setting name="mapUnderscoreToCamelCase" value="true" />
		<!-- 打印查询语句 -->
		<setting name="logImpl" value="STDOUT_LOGGING" />
	</settings>
</configuration>
```

### 配置spring-dao

在`src/main/resources/spring`里新建`spring-dao.xml`里面配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context.xsd">
	<!-- 配置整合mybatis过程 -->
	<!-- 1.配置数据库相关参数properties的属性：${url} -->
	<context:property-placeholder location="classpath:jdbc.properties"/>
	<!-- 2.数据库连接池 -->
	<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
		<!-- 配置连接池属性 -->
		<property name="driverClass" value="${jdbc.driver}" />
		<property name="jdbcUrl" value="${jdbc.url}" />
		<property name="user" value="${jdbc.username}" />
		<property name="password" value="${jdbc.password}" />

		<!-- c3p0连接池的私有属性 -->
		<property name="maxPoolSize" value="30" />
		<property name="minPoolSize" value="10" />
		<!-- 关闭连接后不自动commit -->
		<property name="autoCommitOnClose" value="false" />
		<!-- 获取连接超时时间 -->
		<property name="checkoutTimeout" value="10000" />
		<!-- 当获取连接失败重试次数 -->
		<property name="acquireRetryAttempts" value="2" />
	</bean>

	<!-- 3.配置SqlSessionFactory对象 -->
	<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
		<!-- 注入数据库连接池 -->
		<property name="dataSource" ref="dataSource" />
		<!-- 配置MyBaties全局配置文件:mybatis-config.xml -->
		<property name="configLocation" value="classpath:mybatis-config.xml" />
		<!-- 扫描entity包 使用别名 -->
		<property name="typeAliasesPackage" value="com.iaoe.jwExp.entity" />
		<!-- 扫描sql配置文件:mapper需要的xml文件 -->
		<property name="mapperLocations" value="classpath:mapper/*.xml" />
	</bean>

	<!-- 4.配置扫描Dao接口包，动态实现Dao接口，注入到spring容器中 -->
	<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
		<!-- 注入sqlSessionFactory -->
		<property name="sqlSessionFactoryBeanName" value="sqlSessionFactory" />
		<!-- 给出需要扫描Dao接口包 -->
		<property name="basePackage" value="com.iaoe.jwExp.dao" />
	</bean>
</beans>
```

### 配置spring-service

在`src/main/resources/spring`里新建`spring-service.xml`里面配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
    xmlns:context="http://www.springframework.org/schema/context"
    xmlns:tx="http://www.springframework.org/schema/tx"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context.xsd
    http://www.springframework.org/schema/tx
    http://www.springframework.org/schema/tx/spring-tx.xsd">
    <!-- 扫描service包下所有使用注解的类型，就是service里的 -->
    <context:component-scan base-package="com.iaoe.service" />

    <!-- 配置事务管理器,保证事务的原子性 -->
    <bean id="transactionManager"
        class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <!-- 注入数据库连接池 -->
        <property name="dataSource" ref="dataSource" />
    </bean>

    <!-- 配置基于注解的声明式事务 -->
    <tx:annotation-driven transaction-manager="transactionManager" />
</beans>
```

### 配置spring-web

在`src/main/resources/spring`里新建`spring-web.xml`里面配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xmlns:mvc="http://www.springframework.org/schema/mvc"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context.xsd
    http://www.springframework.org/schema/mvc
    http://www.springframework.org/schema/mvc/spring-mvc-3.2.xsd">
	<!-- 配置SpringMVC -->
	<!-- 1.开启SpringMVC注解模式 -->
	<!-- 简化配置： (1)自动注册DefaultAnootationHandlerMapping,AnotationMethodHandlerAdapter 
		(2)提供一些列：数据绑定，数字和日期的format @NumberFormat, @DateTimeFormat, xml,json默认读写支持 -->
	<mvc:annotation-driven />

	<!-- 2.静态资源默认servlet配置 (1)加入对静态资源的处理：js,gif,png (2)允许使用"/"做整体映射 -->
	<mvc:resources mapping="/resources/**" location="/resources/" />
	<mvc:default-servlet-handler />

	<!-- 3.定义视图解析器 -->
	<bean id="viewResolver"
		class="org.springframework.web.servlet.view.InternalResourceViewResolver">
		<property name="prefix" value="/WEB-INF/html/"></property>
		<property name="suffix" value=".html"></property>
	</bean>
	
	<!-- 4.扫描web相关的bean -->
	<context:component-scan base-package="com.iaoe.web" />
</beans>
```

### 将上面的配置整合到`web.xml`里面

```xml
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
                      http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
	version="3.1" metadata-complete="true">
	<display-name>Archetype Created Web Application</display-name>
	<welcome-file-list>
		<welcome-file>index.jsp</welcome-file>
		<welcome-file>index.html</welcome-file>
	</welcome-file-list>
	<servlet>
		<servlet-name>spring-dispatcher</servlet-name>
		<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
		<init-param>
			<param-name>contextConfigLocation</param-name>
			<param-value>classpath:spring/spring-*.xml</param-value>
		</init-param>
	</servlet>
	<servlet-mapping>
		<servlet-name>spring-dispatcher</servlet-name>
		<!-- 默认匹配所有请求 -->
		<url-pattern>/</url-pattern>
	</servlet-mapping>
</web-app>

```

## 3 验证之前的配置是否成功

### 验证DAO

> 这里的话是通过单元测试的方法来测试是否连接成功，小步骤如下
>
> 1. 新建dao层的接口
> 2. 在mapper里面建立与数据库的连接
> 3. 新建test进程

1. 新建`AreaDao`接口，在`com.iaoe.jwExp.dao`包下，写入查询接口`queryArea()`方法

   ```java
   package com.iaoe.jwExp.dao;
   
   import java.util.List;
   import com.iaoe.jwExp.entity.Area;
   
   public interface AreaDao {
   	/**
   	 * 列出区域列表
   	 * @return areaList
   	 */
   	List<Area> queryArea();
   }
   ```

2. 在src/main/resources/mapper里面添加我们与数据库之间的映射`AreaDao.xml`

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <!--这里用于做规范-->
   <!DOCTYPE mapper
       PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
       "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
   
   <!--1.命名空间namespace代表Dao层所在的位置-->
   <!--2.id为查询的方法名，resultType说明返回的是Area类-->
   <!--3.中间的是查询语句-->
   <mapper namespace="com.iaoe.jwExp.dao.AreaDao">
   	<select id="qureyArea" resultType="com.iaoe.jwExp.entity.Area">
   		SELECT
   		area_id,area_name,priority,create_time,last_edit_time
   		FROM tb_area
   		ORDER BY priority DESC
   	</select>
   </mapper>
   ```

3. 在`src/test/java`里面新建包`com.iaoe.jwExp`，并新建`BaseTest.java`程序

   ```java
   package com.iaoe.jwExp;
   
   import org.junit.runner.RunWith;
   import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
   import org.springframework.test.context.ContextConfiguration;
   
   /**
    * 配置spring和junit整合，junit启动时加载springIOC容器
    * @author iAoe
    *
    */
   //使用哪个junit
   @RunWith(SpringJUnit4ClassRunner.class)
   //告诉junit spring配置文件
   @ContextConfiguration({ "classpath:spring/spring-dao.xml" })
   public class BaseTest {
   	
   }
   ```

4. 在`src/test/java`里面新建包`com.iaoe.jwExp.iaoe`，并新建`AreaDaoTest.java`程序

   ```java
   package com.iaoe.jwExp.dao;
   
   import java.util.List;
   import static org.junit.Assert.assertEquals;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.junit.Test;
   import com.iaoe.jwExp.BaseTest;
   import com.iaoe.jwExp.entity.Area;
   
   public class AreaDaoTest extends BaseTest{
   	@Autowired
   	private AreaDao areaDao;
   	
   	@Test
   	public void testQueryArea() {
   		List<Area> areaList = areaDao.queryArea();
   		assertEquals(2,areaList.size());	//断言，先在数据库建两条数据
   	}
   }
   ```

5. 验证成功画面

   ![](https://ws1.sinaimg.cn/large/006bBmqIgy1g4cbij3gt3j312l0pxdhh.jpg)

### 验证service

> 验证service的方法和验证dao层的方式类似
>
> 1. 创建service里的接口类
>
> 2. 实现service的接口类
> 3. 设置单元测试类

1. 在`com.iaoe.jwExp.service`创建`AreaService`接口类

   ```java
   package com.iaoe.jwExp.service;
   
   import java.util.List;
   
   import com.iaoe.jwExp.entity.Area;
   
   public interface AreaService {
   	List<Area> getAreaList();
   }
   ```

2. 在`com.iaoe.jwExp.service.impl`创建`AreaServiceImpl`实现类

   ```java
   package com.iaoe.jwExp.service.impl;
   
   import java.util.List;
   
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.stereotype.Service;
   
   import com.iaoe.jwExp.dao.AreaDao;
   import com.iaoe.jwExp.entity.Area;
   import com.iaoe.jwExp.service.AreaService;
   
   @Service
   public class AreaServiceImpl implements AreaService{
   	@Autowired
   	private AreaDao areaDao;
   	@Override
   	public List<Area> getAreaList() {
   		return areaDao.queryArea();
   	}
   	
   }
   ```

3. 修改`src/test/java`里的`com.iaoe.jwExp`的`BaseTest`类，将service.xml导入

   ```java
   package com.iaoe.jwExp;
   
   import org.junit.runner.RunWith;
   import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
   import org.springframework.test.context.ContextConfiguration;
   
   /**
    * 配置spring和junit整合，junit启动时加载springIOC容器
    * @author iAoe
    *
    */
   @RunWith(SpringJUnit4ClassRunner.class)
   //告诉junit spring配置文件
   @ContextConfiguration({ "classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml" })	//这里加入了srvice.xml
   public class BaseTest {
   	
   }
   ```

4. 在`src/test/java`里的`com.iaoe.jwExp.service`里创建`AreaServiceTest`类

   ```java
   package com.iaoe.jwExp.service;
   
   import static org.junit.Assert.assertEquals;
   
   import java.util.List;
   
   import org.junit.Test;
   import org.springframework.beans.factory.annotation.Autowired;
   
   import com.iaoe.jwExp.BaseTest;
   import com.iaoe.jwExp.entity.Area;
   
   public class AreaServiceTest extends BaseTest{
   	@Autowired
   	private AreaService areaService;
   	@Test
   	public void testAreaList() {
   		List<Area> areaList = areaService.getAreaList();
   		assertEquals("西方",areaList.get(0).getAreaName());//用于测试第一个数据库的结果是不是西方
   	}
   }
   ```

### 验证WEB

1. 在`src/main/java`里 `com.iaoe.jwExp.web.superadmin`里新建`AreaController`

   ```java
   package com.iaoe.jwExp.web.superadmin;
   
   import java.util.ArrayList;
   import java.util.HashMap;
   import java.util.List;
   import java.util.Map;
   
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.stereotype.Controller;
   import org.springframework.web.bind.annotation.RequestMapping;
   import org.springframework.web.bind.annotation.RequestMethod;
   import org.springframework.web.bind.annotation.ResponseBody;
   
   import com.iaoe.jwExp.entity.Area;
   import com.iaoe.jwExp.service.AreaService;
   
   @Controller
   @RequestMapping("/superadmin")
   public class AreaController {
   	@Autowired
   	private AreaService areaService;
   	// 使用get方法
   	@RequestMapping(value = "/listarea", method = RequestMethod.GET)
   	// 直接将对象转为json对象
   	@ResponseBody
   	private Map<String, Object> listArea() {
   		Map<String, Object> modelMap = new HashMap<String, Object>();
   		List<Area> list = new ArrayList<Area>();
   		try {
   			list = areaService.getAreaList();
   			modelMap.put("rows", list);
   			modelMap.put("total", list.size());
   		} catch (Exception e) {
   			e.printStackTrace();
   			modelMap.put("success", false);
   			// 返回错误信息
   			modelMap.put("errMsg", e.toString());
   		}
   		return modelMap;
   	}
   }
   ```

##  4 logback

> logback主件的作用主要作用两个，一个是错误追踪，一个是显示程序状态

### logback介绍

**logback的主要模块**

1. logback-access

   > 第三方软件可以通过这个模块来获取logback日志

2. logback-classic

   > 可以方便切换其他日志系统

3. logback-core

   > 为前面两个提供基础支持

**logback主要标签**

1. logger

   > 存放日志对象，可以定义日志类型和级别

2. appender

   > 指定日志输出的目的地/媒介，如控制台，文件

3. layout

   > 格式化日志信息

### logback配置

在`src/main/resources`里面建立一个`logback.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- scan配置文件发生改变，会自动修改 scanPeriod用于设置扫描配置的时间，这样改了配置信息就会自动生效，不用重启服务器 debug用于查看logback本身的运行状态 -->
<configuration scan="true" scanPeriod="60 seconds"
	debug="false">
	<!-- 定制参数变量 -->
	<!-- TRACE<DEBUG<INFO<WARN<ERROR -->
	<!-- 设置logger.trace("msg"),还可以设置logger.debug... -->
	<!-- 这里代表debug与后面的信息都能得到， -->
	<property name="log.level" value="debug" />
	<property name="log.maxHistory" value="30" />
	<property name="log.filePath"
		value="${catalina.base}/logs/webapps" />
	<!-- 输出时间-线程-哪个级别的日志-哪个package和类的-信息-换行 -->
	<property name="log.pattern"
		value="%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} -
				%msg%n" />
	<!-- 在控制台输出信息 -->
	<appender name="consoleAppender"
		class="ch.qos.logback.core.ConsoleAppender">
		<encoder>
			<pattern>${log.pattern}</pattern>
		</encoder>
	</appender>
	<!-- DEBUG输出的文件路径，按天按月滚动生成 -->
	<appender name="debugAppender"
		class="ch.qos.logback.core.rolling.RollingFileAppender">
		<file>${log.filePath}/debug.log</file>
		<rollingPolicy
			class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
			<!-- 文件名称 -->
			<fileNamePattern>${log.filePath}/debug/debug.%d{yyyy-MM-dd}.log.gz
			</fileNamePattern>
			<!-- 文件最大保存历史数量30天 -->
			<maxHistory>${log.maxHistory}</maxHistory>
		</rollingPolicy>
		<encoder>
			<pattern>${log.pattern}</pattern>
		</encoder>
		<filter class="ch.qos.logback.classic.filter.LevelFilter">
			<level>DEBUG</level>
			<onMatch>ACCEPT</onMatch>
			<onMismatch>DENY</onMismatch>
		</filter>
	</appender>
	<!-- info输出的文件路径，按天按月滚动生成 -->
	<appender name="infoAppender"
		class="ch.qos.logback.core.rolling.RollingFileAppender">
		<file>${log.filePath}/info.log</file>
		<rollingPolicy
			class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
			<!-- 文件名称 -->
			<fileNamePattern>${log.filePath}/info/info.%d{yyyy-MM-dd}.log.gz
			</fileNamePattern>
			<!-- 文件最大保存历史数量30天 -->
			<maxHistory>${log.maxHistory}</maxHistory>
		</rollingPolicy>
		<encoder>
			<pattern>${log.pattern}</pattern>
		</encoder>
		<filter class="ch.qos.logback.classic.filter.LevelFilter">
			<level>INFO</level>
			<onMatch>ACCEPT</onMatch>
			<onMismatch>DENY</onMismatch>
		</filter>
	</appender>
	<!-- error输出的文件路径，按天按月滚动生成 -->
	<appender name="errorAppender"
		class="ch.qos.logback.core.rolling.RollingFileAppender">
		<file>${log.filePath}/error.log</file>
		<rollingPolicy
			class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
			<!-- 文件名称 -->
			<fileNamePattern>${log.filePath}/error/error.%d{yyyy-MM-dd}.log.gz
			</fileNamePattern>
			<!-- 文件最大保存历史数量30天 -->
			<maxHistory>${log.maxHistory}</maxHistory>
		</rollingPolicy>
		<encoder>
			<pattern>${log.pattern}</pattern>
		</encoder>
		<filter class="ch.qos.logback.classic.filter.LevelFilter">
			<level>ERROR</level>
			<onMatch>ACCEPT</onMatch>
			<onMismatch>DENY</onMismatch>
		</filter>
	</appender>
	<!-- 监听com.iaoe.jwExp下的日志信息，addititivity为true会继承root的信息 -->
	<logger name="com.iaoe.jwExp" level="${log.level}"
		additivity="true">
		<appender-ref ref="debugAppender" />
		<appender-ref ref="infoAppender" />
		<appender-ref ref="errorAppender" />
	</logger>
	<root level="info">
		<appender-ref ref="consoleAppender" />
	</root>
</configuration>
```

### 验证logback

修改我们之前的`AreaController`文件

```java
package com.iaoe.jwExp.web.superadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iaoe.jwExp.entity.Area;
import com.iaoe.jwExp.service.AreaService;

@Controller
@RequestMapping("/superadmin")
public class AreaController {
	Logger logger = LoggerFactory.getLogger(AreaController.class);
	@Autowired
	private AreaService areaService;
	// 使用get方法
	@RequestMapping(value = "/listarea", method = RequestMethod.GET)
	// 直接将对象转为json对象
	@ResponseBody
	private Map<String, Object> listArea() {
		logger.info("===start===");
		long startTime = System.currentTimeMillis();
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<Area> list = new ArrayList<Area>();
		try {
			list = areaService.getAreaList();
			modelMap.put("rows", list);
			modelMap.put("total", list.size());
		} catch (Exception e) {
			e.printStackTrace();
			modelMap.put("success", false);
			// 返回错误信息
			modelMap.put("errMsg", e.toString());
			logger.error("test error!");
		}
		long endTime = System.currentTimeMillis();
		logger.debug("costTime:[{}ms]",endTime-startTime);
		logger.info("===end===");
		return modelMap;
	}
}
```

控制台输出的信息如下

![](https://ws1.sinaimg.cn/large/006bBmqIgy1g4d60vbp1gj30ob07zt9d.jpg)

日志文件输出如下

![](https://ws1.sinaimg.cn/large/006bBmqIgy1g4d61rtwkdj30iv04iaa4.jpg)

## web.xml

### 1. 修改默认启动页面

```xml
<web-app>
  <display-name>Archetype Created Web Application</display-name>
  <welcome-file-list>
  	<welcome-file>index.jsp</welcome-file>
  	<welcome-file>index.html</welcome-file>
  </welcome-file-list>
</web-app>
```

## 小问题

### Eclipse文字大小不一

> Window –> Preferences –> General –> Appearance –> Colors and Fonts，在“Colors and Fonts”中选择“Basic”–>”Text Font”，然后点“Edit”，把右下角脚本改为中欧字符即可。

## 小知识

### datetime和timestamp的区别

datetime支持从0000年到9999年，而timestamp支持从1970年开始，而timestamp是全球统一时间，如果你的系统是跨区域的话，使用timestamp就比较好

### mysql引擎engine之间的区别

`MYISAM`表检索，读表的性能高，但当更新某个键值的时候，就会锁掉这个表，如果有其他线程修改这个表的某行数据，那么是需要等待的，如果需要达99%的稳定性，方便扩展性和高可用性的话，`读多写少`，就选择MYISAM

`InnoDB`行检索，更新某个表的某行时，给某行加锁，可以更新其他行，`读少写多`，选择InnoDB

### unique key的用处

使用unique key类似于建立一个索引，查询速率会变快，但是不要建立太多的索引，添加一个唯一索引的方法

```sql
alter table tb_wechat_auth add unique index(open_id)
```

