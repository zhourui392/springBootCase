package cn.zz.user.common.util.retry;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.cglib.proxy.MethodProxy;


public class Retry extends InterfaceInvocationHandler {
    private static final RetryConfig _DEFAULT_CONFIG = RetryConfig.of(3,3000);

    private List<Class<? extends Throwable>> _eList;
    private ResultHandler _handler;
    private StrikeAction _strikeAction;
    private FailAction _failAction;

    private RetryConfig _localRetryConfig;

    public static Retry get() {
        return new Retry();
    }

    private Retry() {
        _handler = o -> false;
        _localRetryConfig = _DEFAULT_CONFIG;
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        RetryConfig config = _localRetryConfig;
        Object client = proxy;
        Object res = null;
        int failTime = 0;
        while (true) {
            Throwable e = null;
            try {
                res = method.invoke(client, args);
            } catch (Throwable ex) {
                e = ex.getCause();
            }
            strike(failTime);
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
                            : new RuntimeException("重试后结果不符合预期[接口：" + __clazz.getName() + "#方法：" + method.getName()
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

    private void strike(int failTime) {
        if (_strikeAction != null){
            _strikeAction.action(failTime);
        }
    }


    private Retry config(RetryConfig retryConfig) {
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
    public static interface ResultHandler {
        boolean handler(Object result);
    }

    @FunctionalInterface
    public static interface StrikeAction {
        void action(int i);
    }

    @FunctionalInterface
    public static interface FailAction {
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
}

