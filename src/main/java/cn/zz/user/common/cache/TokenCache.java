package cn.zz.user.common.cache;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * @author : zhourui
 * @version: 2019-08-09 16:56
 **/
public class TokenCache {
    private static final int EXPIRED_MINUTES = 300;
    private static final int MAX_USERS = 100000;

    /**
     * token缓存
     */
    private Cache<String, UserToken> cache;

    public TokenCache(){
        cache = CacheBuilder.newBuilder()
            .expireAfterWrite(EXPIRED_MINUTES, TimeUnit.MINUTES)
            .maximumSize(MAX_USERS)
            .build();
    }

    public UserToken getCache(String key){
        return cache.getIfPresent(key);
    }

    public void setCache(String key, UserToken obj){
        cache.put(key, obj);
    }

    public void removeCache(String key){
        cache.invalidate(key);
    }

    public void removeAll(){
        cache.invalidateAll();
    }

    public long getSize(){
        return cache.size();
    }
}
