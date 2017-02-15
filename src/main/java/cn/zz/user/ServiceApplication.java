package cn.zz.user;

import cn.zz.user.common.SpringContextHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ServiceApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(ServiceApplication.class, args);
		SpringContextHolder.setApplicationContext(applicationContext);
	}
}
