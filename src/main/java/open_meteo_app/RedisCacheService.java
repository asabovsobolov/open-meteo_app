package open_meteo_app;

import redis.clients.jedis.Jedis;

public class RedisCacheService {

    private static Jedis jedis;

    static {
        jedis = new Jedis("localhost", 6379);
        // Optional: test connection
        // System.out.println("Redis connected: " + jedis.ping());
        if(!isConnectionAlive())
            jedis = null;
    }

    public static boolean isConnectionAlive() {
        try {
            return "PONG".equals(jedis.ping());
        } catch (Exception e) {
            // You can log this exception if needed
            return false;
        }
    }

    public static void cacheData(String key, String value, int ttlSeconds) {
        if(jedis != null)
            jedis.setex(key, ttlSeconds, value);
    }

    public static String getCachedData(String key) {
        if(jedis != null)
            return jedis.get(key);
        return null;
    }

    public static boolean isCached(String key) {
        if(jedis != null)
            return jedis.exists(key);
        return false;
    }
}
