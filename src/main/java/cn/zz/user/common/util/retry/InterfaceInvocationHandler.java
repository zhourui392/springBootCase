package cn.zz.user.common.util.retry;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public abstract class InterfaceInvocationHandler implements InvocationHandler, MethodInterceptor {
    protected Class<?> __clazz;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return intercept(proxy, method, args, null);
    }
    
    /**
     * 整合jdk代理与cglib代理
     * 其中cglib代理目标为代理类时,只兼容Vine Bean
     */
    @Override
    public abstract Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy)
            throws Throwable;

    @SuppressWarnings("unchecked")
    public <T> T register(Class<T> interfacc) {
        this.__clazz = interfacc;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Class<?>[] interfaces = new Class<?>[] { interfacc };
        return (T) Proxy.newProxyInstance(loader, interfaces, this);
    }

    public <T> T register(T object) {
        return register(object, object.getClass());
    }
    
    @SuppressWarnings("unchecked")
    public <T> T register(T object, Class<?> clazz) {
        this.__clazz = clazz;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(__clazz);
        enhancer.setCallback(this);
        return (T) enhancer.create();
    }

    /**
     * 获取真实类型
     * 
     * @param proxy 代理对象
     * @return 真实类型
     */
    public static Class<?> getTarget(Object proxy) {

        if (!isProxy(proxy)) {
            return proxy.getClass();
        }
        try {
            if (AopUtils.isJdkDynamicProxy(proxy)) {
                return getJdkDynamicProxyTargetObject(proxy);
            } else {
                return getCglibProxyTargetObject(proxy);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    private static Class<?> getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);

        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("config");
        advised.setAccessible(true);

        Class<?> target = advised.get(dynamicAdvisedInterceptor).getClass();

        return target;
    }

    private static Class<?> getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);

        Field advised = aopProxy.getClass().getDeclaredField("config");
        advised.setAccessible(true);

        Object target = ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();

        return target.getClass();
    }

    public static boolean isProxy(Object object) {
        return Proxy.isProxyClass(object.getClass()) || isCglibProxyClass(object.getClass());
    }

    public static boolean isCglibProxyClass(Class<?> clazz) {
        return (clazz != null && isCglibProxyClassName(clazz.getName()));
    }

    public static boolean isCglibProxyClassName(String className) {
        return (className != null && className.contains("$$"));
    }
}
