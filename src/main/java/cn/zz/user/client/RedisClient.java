package cn.zz.user.client;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author : zhourui
 * @version: 2019-09-20 11:23
 **/
@Service
public class RedisClient {
    @Resource
    RedisTemplate redisTemplate;



}
