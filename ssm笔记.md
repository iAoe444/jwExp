# SSM学习笔记

> 书写于2019/06/18 15:13

## 搭建开发环境

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

## 开始学习

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

原来

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



### web.xml

#### 1. 修改默认启动页面

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

