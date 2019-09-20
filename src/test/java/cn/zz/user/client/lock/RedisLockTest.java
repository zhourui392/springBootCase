package cn.zz.user.client.lock;

import java.time.Clock;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import cn.zz.user.BaseSpringBootTest;
import cn.zz.user.common.util.lang.Stopwatch;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class RedisLockTest extends BaseSpringBootTest {

    @Resource
    RedisLock redisLock;

    @Test
    public void lock() throws LockFailedException {
        LockHandle lockHandle = redisLock.lock("test");
        Assert.assertNotNull(lockHandle);
    }

    @Test
    public void lockTest() throws LockFailedException, InterruptedException {
        LockHandle lockHandle = redisLock.lock("test");
        new Thread(() -> {
            LockHandle lockHandle2 = null;
            try {
                System.out.println("lock2 start.");
                Stopwatch stopwatch = new Stopwatch();
                stopwatch.start();
                lockHandle2 = redisLock.lock("test");
                stopwatch.stop();
                System.out.println("lock2 get locked.time is "+ stopwatch.getDuration());
            } catch (LockFailedException e) {
                e.printStackTrace();
            }finally {
                if (lockHandle2 != null){
                    lockHandle2.unlock();
                }
            }
        }).start();
        Assert.assertNotNull(lockHandle);
        try {
            TimeUnit.SECONDS.sleep(6);
        }finally {
            lockHandle.unlock();
        }

    }

    @Test
    public void unlock() throws LockFailedException {
        LockHandle lockHandle = redisLock.lock("test");

        try {

        }finally {
            lockHandle.unlock();
        }
    }
}