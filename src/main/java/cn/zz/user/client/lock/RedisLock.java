package cn.zz.user.client.lock;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Resource;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * @author : zhourui
 * @version: 2019-09-20 11:49
 **/
@Component
public class RedisLock implements Lock{
    private static final int NANOS_PER_MILLI = 1_000_000;
    private static final int WAIT_INTERVAL_MIN_MILLS = 10;
    private static final int WAIT_INTERVAL_MAX_MILLS = 100;

    private static final int DEFAULT_EXPIRE_MILLS = 10_000;

    /**
     * 重复尝试5s
     */
    private static final int DEFAULT_WAIT_MILLS = 5000;

    @Resource
    private RedisTemplate redisTemplate;
    private ThreadLocalRandom random = ThreadLocalRandom.current();

    private long nano(int mills) {
        return (long)mills * NANOS_PER_MILLI;
    }

    private Jedis getClient(){
        RedisConnection conn =  RedisConnectionUtils.getConnection(redisTemplate.getConnectionFactory());
        Jedis jedis = (Jedis) conn.getNativeConnection();
        return jedis;
    }

    @Override
    public LockHandle lock(String key) throws LockFailedException {
        return lock(key, DEFAULT_EXPIRE_MILLS);
    }

    @Override
    public LockHandle lock(String key, int expireMills) throws LockFailedException {
        return lock(key, expireMills, DEFAULT_WAIT_MILLS);
    }

    @Override
    public LockHandle lock(String key, int expireMills, int waitMills) throws LockFailedException {
        Jedis jedis = getClient();
        LockHandle handle = new LockHandleImpl(this, key);

        LocalDateTime till = LocalDateTime.now().plusNanos(nano(waitMills));
        do {
            String locked = jedis.set(key, handle.getToken(), "nx", "px", expireMills);
            if (locked != null) {
                return handle;
            }

            try {
                Thread.sleep(random.nextInt(WAIT_INTERVAL_MIN_MILLS, WAIT_INTERVAL_MAX_MILLS));
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        } while (LocalDateTime.now().isBefore(till));

        throw new LockFailedException("Max waiting time exceed.");
    }

    @Override
    public void unlock(LockHandle handle) {
        Jedis jedis = getClient();
        String token = jedis.get(handle.getKey());
        if (token != null && token.equals(handle.getToken())) {
            jedis.del(handle.getKey());
        }
    }
}
