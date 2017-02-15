package cn.zz.user.common.util.lang;

/**
 * 带两个参数的通用回调接口
 * @author zhourui(zhourui0125@gmail.com)
 * @param <T>
 */
public interface Callback2<T0, T1> {

    void invoke(T0 arg0, T1 arg1);

}