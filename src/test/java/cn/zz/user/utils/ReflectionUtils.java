package cn.zz.user.utils;

import java.lang.reflect.Field;

/**
 * @author : zhourui
 * @version: 2019-09-06 14:48
 **/
public class ReflectionUtils {

    /**
     * use case:
     * ReflectionUtils.setField(auditLogic, "auditItemDso", auditItemDso);
     * @param targetObject
     * @param filed
     * @param value
     */
    public static void setField(Object targetObject, String filed, Object value){
        if (value != null){
            Field field = org.springframework.util.ReflectionUtils.findField(targetObject.getClass(), filed, value.getClass());
            org.springframework.util.ReflectionUtils.setField(field, targetObject, value);
        }
    }
}
