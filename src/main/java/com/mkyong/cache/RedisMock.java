package com.mkyong.cache;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * redis 的简单测试工具类，采用map来模拟redis.暂未加expire等功能。
 * 使用栗子：
 *
 * @author Michael.Wang
 * @Tested RedisTokenHandler redisTokenHandler;
 * @Injectable private WeimobRedisSimpleClient redisSimpleClient = new RedisMock();
 * <p/>
 * 或：
 * WeimobRedisSimpleClient weimobRedisSimpleClient = new RedisMock();
 * ......
 * execute(weimobRedisSimpleClient);
 * .......
 * assert(...);
 */
@Service("weimobSimpleRedisClient")
public class RedisMock extends ReidsClientAdapter {
    private Map map = new ConcurrentHashMap();

    @Override
    public String get(String key) {
        return (String) map.get(key);
    }

    @Override
    public String set(String key, String value) {
        map.put(key, value);
        return value;
    }

    public Boolean exists(String key) {
        return map.keySet().contains(key);
    }

    @Override
    public Long del(String key) {
        map.remove(key);
        return 1L;
    }

    @Override
    public Long expire(String key, int seconds) {
        //TODO;
        return 0L;
    }

}
