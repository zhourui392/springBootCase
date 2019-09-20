package cn.zz.user.client.lock;

import java.util.UUID;

import lombok.Getter;

/**
 * @author : zhourui
 * @version: 2019-09-20 12:18
 **/
@Getter
public class LockHandleImpl implements LockHandle {
    private Lock lock;
    private String key;
    private String token;

    public LockHandleImpl(Lock lock, String key) {
        this.lock = lock;
        this.key = key;
        this.token = UUID.randomUUID().toString();
    }

    @Override
    public void unlock() {
        lock.unlock(this);
    }
}
