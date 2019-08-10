package cn.zz.user.common.config;

import javax.annotation.Resource;

import cn.zz.user.common.interceptor.AuthenticationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author : zhourui
 * @version: 2019-08-09 14:38
 **/
@Configuration
public class SpringWebMvcConfig extends WebMvcConfigurerAdapter {
    @Resource
    AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
            .addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration r1 = registry.addInterceptor(authenticationInterceptor);
        //添加拦截请求
        r1.addPathPatterns("/*");
        //添加不拦截的请求
        r1.excludePathPatterns("/login","error");
    }
}