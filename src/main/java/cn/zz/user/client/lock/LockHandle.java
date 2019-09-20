package cn.zz.user.client.lock;

import java.io.Closeable;

/**
 * @author : zhourui
 * @version: 2019-09-20 11:46
 **/
public interface LockHandle extends Closeable {
    /**
     * Lock key.
     */
    String getKey();

    /**
     * Lock token.
     */
    String getToken();

    /**
     * Release the lock.
     */
    void unlock();

    /**
     * The IOExceptional-off close method.
     */
    @Override
    default void close() {
        unlock();
    }
}
