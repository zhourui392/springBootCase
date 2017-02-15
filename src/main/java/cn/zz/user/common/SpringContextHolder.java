package cn.zz.user.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;


/**
 * Spring 工具类 
 * 获取Spring容器中的上下文信息
 * 测试不同机器上的修改
 *
 */
public class SpringContextHolder {

	private static ApplicationContext context;


	public static ApplicationContext getApplicationContext() {
		checkApplicationContext();
		return context;
	}


	public static <T> T getBean( Class<T> clazz ) {
		checkApplicationContext();
		return context.getBean(clazz);
	}


	public static void setApplicationContext( ApplicationContext ac ) throws BeansException {
		context = ac;
	}


	private static void checkApplicationContext() {
		if ( context == null ) {
			throw new IllegalStateException("applicaitonContext未注入,请在applicationContext.xml中定义SpringContextHolder");
		}
	}

}
