package com.example.demo1;

import redis.clients.jedis.Jedis;

public class RedisCacheService {

    private static final Jedis jedis;

    static {
        jedis = new Jedis("localhost", 6379);
        // Optional: test connection
        // System.out.println("Redis connected: " + jedis.ping());
    }

    public static void cacheData(String key, String value, int ttlSeconds) {
        jedis.setex(key, ttlSeconds, value);
    }

    public static String getCachedData(String key) {
        return jedis.get(key);
    }

    public static boolean isCached(String key) {
        return jedis.exists(key);
    }
}
