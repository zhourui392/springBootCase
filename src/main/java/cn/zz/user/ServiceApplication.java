package cn.zz.user;

import cn.zz.user.common.SpringContextHolder;
import cn.zz.user.common.cache.TokenCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author zhourui
 */
@SpringBootApplication
public class ServiceApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(ServiceApplication.class, args);
		SpringContextHolder.setApplicationContext(applicationContext);
	}
}
