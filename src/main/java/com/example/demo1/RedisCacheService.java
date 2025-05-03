package com.example.demo1;

import redis.clients.jedis.Jedis;

public class RedisCacheService {

    private final Jedis jedis;

    public RedisCacheService() {
        this.jedis = new Jedis("localhost", 6379);
    }

    public void cacheData(String key, String value, int ttlSeconds) {
        jedis.setex(key, ttlSeconds, value);
    }

    public String getCachedData(String key) {
        return jedis.get(key);
    }

    public boolean isCached(String key) {
        return jedis.exists(key);
    }
}