package snc.boot.redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by jac on 18-5-27.
 */
public class RedisConfig {
    private JedisPool jedisPool;
    private int maxTotal;
    private int maxIdle;
    private int maxWait;
    private String ip;
    private int port;

    public RedisConfig() {
    }

    public RedisConfig(int maxTotal, int maxIdle, int maxWait, String ip, int port) {
        this.maxTotal = maxTotal;
        this.maxIdle = maxIdle;
        this.maxWait = maxWait;
        this.ip = ip;
        this.port = port;
    }

    private void init(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(maxIdle);
        config.setMaxTotal(maxTotal);
        config.setMaxWaitMillis(maxWait);
        jedisPool = new JedisPool(config,ip,port);
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    @Override
    public String toString() {
        return "RedisConfig{" +
                "jedisPool=" + jedisPool +
                ", maxTotal=" + maxTotal +
                ", maxIdle=" + maxIdle +
                ", maxWait=" + maxWait +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                '}';
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

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }
}
