package cn.zz.user.client.retry;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Maps;
import org.springframework.cglib.proxy.MethodProxy;

/**
 * @author : zhourui
 * @version: 2019-09-26 11:26
 **/
public class Retry extends InterfaceInvocationHandler{
    private static final RetryConfig _DEFAULT_CONFIG = RetryConfig.of(3, 3000);

    private List<Class<? extends Throwable>> _eList;
    private ResultHandler _handler;
    private static Map<String, RetryConfig> _RETRY_CONFIGS = Maps.newHashMap();
    private RetryConfig _localRetryConfig;
    private FailAction _failAction;
    private Object client;

    public static Retry get(Object service) {
        return new Retry(service);
    }

    private Retry(Object service) {
        _handler = o -> false;
        client = service;
        initStaticConfig();
    }

    private void initStaticConfig() {
        //init _RETRY_CONFIGS
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        RetryConfig config = getConfig(method);
        Object res = null;
        int failTime = 0;
        while (true) {
            Throwable e = null;
            try {
                res = method.invoke(client, args);
            } catch (Throwable ex) {
                e = ex.getCause();
            }
            if (pass(res, e)) {
                if (e != null){
                    throw e;
                }
                break;
            } else {
                failTime++;
                if (config.retryTime >= failTime) {
                    TimeUnit.MILLISECONDS.sleep(config.retryBlankingTime);
                } else {
                    e = e != null ? e
                        : new RetryException("重试后结果不符合预期[接口：" + __clazz.getName() + "#方法：" + method.getName()
                            + "#参数：" + Arrays.toString(args) + "#结果：" + res + "]");
                    if (_failAction != null) {
                        _failAction.action(e);
                    }
                    throw e;
                }
            }
        }
        return res;
    }

    private RetryConfig getConfig(Method method) {
        String configName = __clazz.getSimpleName() + "." + method.getName();
        RetryConfig retryConfig = _RETRY_CONFIGS.get(configName);
        return retryConfig != null ? retryConfig : _localRetryConfig == null ? _DEFAULT_CONFIG : _localRetryConfig;
    }

    public Retry config(RetryConfig retryConfig) {
        this._localRetryConfig = retryConfig;
        return this;
    }

    public Retry config(int retryTime, int retryBlankingTime) {
        config(RetryConfig.of(retryTime, retryBlankingTime));
        return this;
    }

    private boolean pass(Object res, Throwable e) {
        return !(_handler.handler(res) || checkErr(e));
    }

    private boolean checkErr(Throwable e) {
        if (e == null){
            return false;
        }
        return _eList.stream().anyMatch(exception -> exception.isInstance(e));
    }

    public Retry setFailAction(FailAction failAction) {
        this._failAction = failAction;
        return this;
    }

    public Retry addResultHandler(ResultHandler handler) {
        this._handler = handler;
        return this;
    }

    public Retry catchAll(List<Class<? extends Throwable>> es) {
        if (_eList == null) {
            _eList = new ArrayList<>();
        }
        for (int i = 0; i < es.size(); i++) {
            _eList.add(es.get(i));
        }
        return this;
    }

    public Retry catchAll(Class<? extends Throwable> e) {
        return catchAll(Arrays.asList(e));
    }

    @FunctionalInterface
    public interface ResultHandler {
        /**
         * 结果处理
         * @param result
         * @return
         */
        boolean handler(Object result);
    }

    @FunctionalInterface
    public interface FailAction {
        /**
         * 失败后处理
         * @param e
         */
        void action(Throwable e);
    }

    static class RetryConfig {
        int retryTime;
        int retryBlankingTime;

        static RetryConfig of(int retryTime, int retryBlankingTime) {
            RetryConfig retryConfig = new RetryConfig();
            retryConfig.retryTime = retryTime;
            retryConfig.retryBlankingTime = retryBlankingTime;
            return retryConfig;
        }
    }

    class RetryException extends Exception {
        public RetryException(String string) {
            super(string);
        }
        private static final long serialVersionUID = 1L;
    }
}
