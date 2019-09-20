package cn.zz.user.client;

import javax.annotation.Resource;

import cn.zz.user.BaseSpringBootTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author : zhourui
 * @version: 2019-09-20 11:25
 **/
public class RedisTemplateTest extends BaseSpringBootTest {

    @Resource
    RedisTemplate redisTemplate;

    @Test
    public void testRedisConnection(){
        redisTemplate.opsForValue().set("a","b");
        String b = (String)redisTemplate.opsForValue().get("a");
        Assert.assertEquals("b", b);
    }
}
