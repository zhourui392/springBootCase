package cn.zz.user.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 服务初始化
 * @author : zhourui
 * @version: 2019-08-10 11:13
 **/
@Component
public class AppInitService implements InitializingBean, DisposableBean, ApplicationContextAware{

    private static final Logger logger = LoggerFactory.getLogger(AppInitService.class);

    @Override
    public void afterPropertiesSet() throws Exception {


        logger.debug("service init finished.");
    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.setApplicationContext(applicationContext);
    }
}