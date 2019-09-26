package cn.zz.user.client.retry;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

/**
 * @author : zhourui
 * @version: 2019-09-26 11:37
 **/
public  abstract class InterfaceInvocationHandler implements InvocationHandler, MethodInterceptor {
    protected Class<?> __clazz;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return intercept(proxy, method, args, null);
    }

    /**
     * 整合jdk代理与cglib代理
     * 其中cglib代理目标为代理类时,只兼容Vine Bean
     * @param proxy
     * @param method
     * @param args
     * @param methodProxy
     * @return
     * @throws Throwable
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
}
