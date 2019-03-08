package snc.boot.redis;

import redis.clients.jedis.Jedis;

/**
 * Created by jac on 18-5-27.
 */
public class RedisClient {
    private RedisConfig config;
    private String ip;
    private int port;
    private Jedis jedis;

    public RedisClient(RedisConfig config, String ip, int port) {
        this.config = config;
        this.ip = ip;
        this.port = port;
        this.jedis = config.getJedisPool().getResource();
    }

    public RedisClient() {
    }

    public RedisConfig getConfig() {
        return config;
    }

    public void setConfig(RedisConfig config) {
        this.config = config;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Jedis getJedis(){
        return jedis;
    }

    @Override
    public String toString() {
        return "RedisClient{" +
                "config=" + config +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}
