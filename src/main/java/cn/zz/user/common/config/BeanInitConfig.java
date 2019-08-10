package cn.zz.user.common.config;

import cn.zz.user.common.cache.TokenCache;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * 自定义Bean
 * @author : zhourui
 * @version: 2019-08-10 11:32
 **/
@Configuration
public class BeanInitConfig {

    @Bean
    public TokenCache tokenCache(){
        return new TokenCache();
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return (ConfigurableEmbeddedServletContainer container) -> {
                container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/static/error/404.html"));
                container.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED, "/static/error/401.html"));
            };
    }
}