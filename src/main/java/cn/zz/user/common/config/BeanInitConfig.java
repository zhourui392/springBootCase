package cn.zz.user.common.config;

import cn.zz.user.common.cache.TokenCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

}