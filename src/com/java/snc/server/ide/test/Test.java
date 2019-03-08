package snc.server.ide.test;

import redis.clients.jedis.Jedis;

public class Test {
    public static void main(String[] args) {
        Jedis jedis = new Jedis();
        int i = 0;
        jedis.set("a", String.valueOf(i));
        int j = Integer.parseInt(jedis.get("b"));
        System.out.println(j);
    }
}
