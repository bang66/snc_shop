package snc.boot.redis;

import redis.clients.jedis.Jedis;
import snc.boot.boot.ServiceManager;

public class GetJedis {
    public static Jedis getJedis(){
        RedisConfig config = (RedisConfig) ServiceManager.getService("redisConfig");
        Jedis jedis = config.getJedisPool().getResource();
        return jedis;
    }
}
