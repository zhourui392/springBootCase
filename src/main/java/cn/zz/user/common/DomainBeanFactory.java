package cn.zz.user.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author : zhourui
 * @version: 2019-07-24 11:27
 **/
public class DomainBeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(DomainBeanFactory.class);

    public static <T> T newInstance(Class<T> clazz){
        Field[] fields = clazz.getDeclaredFields();
        T t = null;
        try {
            t = clazz.newInstance();
        } catch (InstantiationException e) {
            logger.error("DomainBeanFactory crete instance[{}] error.",clazz,e);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            logger.error("DomainBeanFactory crete instance[{}] error.",clazz,e);
            e.printStackTrace();
        }
        if (fields == null || fields.length == 0){
            return t;
        }
        for (Field field : fields) {
            Annotation resourceAnnotation = field.getAnnotation(Resource.class);
            if (resourceAnnotation != null){
                field.setAccessible(true);
                Class filedClazz = field.getType();
                Object bean = SpringContextHolder.getBean(filedClazz);
                try {
                    field.set(t, bean);
                } catch (IllegalAccessException e) {
                    logger.error("DomainBeanFactory set filed value[{}] error.",field,e);
                    e.printStackTrace();
                }
            }
        }
        return t;
    }
}
