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
@ContextConfiguration({ "classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml" })
public class BaseTest {
	
}
