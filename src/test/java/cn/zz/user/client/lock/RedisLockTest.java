package cn.zz.user.client.lock;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import cn.zz.user.BaseSpringBootTest;
import cn.zz.user.common.util.lang.Stopwatch;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RedisLockTest extends BaseSpringBootTest {

    @Resource
    RedisLock redisLock;

    @Test
    public void lockAndUnLock() throws LockFailedException {
        LockHandle lockHandle = redisLock.lock("test");
        try {

        }finally {
            lockHandle.unlock();
        }
        Assert.assertNotNull(lockHandle);
    }


    @Test
    public void lockTest() throws LockFailedException, InterruptedException {
        new Thread(() -> {
            LockHandle lockHandle2 = null;
            try {
                System.out.println("lock2 start.");
                lockHandle2 = redisLock.lock("test");
                System.out.println("lock2 get locked.");
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (LockFailedException e) {
                e.printStackTrace();
            }finally {
                if (lockHandle2 != null){
                    lockHandle2.unlock();
                }
            }
        }).start();
        TimeUnit.MILLISECONDS.sleep(100);
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        LockHandle lockHandle = redisLock.lock("test");
        Assert.assertNotNull(lockHandle);
        stopwatch.stop();
        System.out.println("lock get locked. time is "+stopwatch);
        Assert.assertTrue(stopwatch.getDuration() > (3000 - 100));
    }

    @Test
    public void lockTest_EXPIRE() throws InterruptedException {
        new Thread(() -> {
            LockHandle lockHandle2 = null;
            try {
                System.out.println("lock2 start.");
                lockHandle2 = redisLock.lock("test");
                System.out.println("lock2 get locked.");
                try {
                    TimeUnit.SECONDS.sleep(6);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (LockFailedException e) {
                e.printStackTrace();
            }finally {
                if (lockHandle2 != null){
                    lockHandle2.unlock();
                }
            }
        }).start();
        TimeUnit.MILLISECONDS.sleep(100);
        Exception resultException = null;
        LockHandle lockHandle = null;
        try {
            lockHandle = redisLock.lock("test");
        }catch (LockFailedException e){
            resultException = e;
        }finally {
            if (lockHandle != null){
                lockHandle.unlock();
            }
        }
        Assert.assertNotNull(resultException);
    }
}