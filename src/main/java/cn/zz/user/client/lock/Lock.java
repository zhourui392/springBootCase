package cn.zz.user.client.lock;

/**
 * @author : zhourui
 * @version: 2019-09-20 11:46
 **/
public interface Lock {
    /**
     * Try to get lock by key.
     * @param key lock key
     */
    LockHandle lock(String key) throws LockFailedException;

    /**
     * Try to get lock by key with expire time in milliseconds.
     * @param key lock key
     * @param expireMills expire time
     */
    LockHandle lock(String key, int expireMills) throws LockFailedException;

    /**
     * Try to get lock by key with expire time and wait time both in milliseconds.
     * @param key lock key
     * @param expireMills expire time
     * @param waitMills wait time
     * @throws LockFailedException
     */
    LockHandle lock(String key, int expireMills, int waitMills) throws LockFailedException;

    /**
     * Release lock by handle.
     * @param handle lock handle
     */
    void unlock(LockHandle handle);
}
