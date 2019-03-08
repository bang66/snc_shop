package snc.server.ide.util;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedidTest {
    private Jedis jedis;

    @Before
    public void setup() {
        //连接redis服务器，192.168.0.100:6379
        jedis = new Jedis("127.0.0.1", 6379);
    }
    @Test
    public void testcourse() {
        //-----添加数据----------
        Map<String, String> map = new HashMap<String, String>();
        map.put("courseid", "1");
        map.put("sales","50");
        map.put("type","python");
        map.put("bread","三星");
        map.put("courseprice", "22");
        jedis.hmset("course", map);
        //第一个参数是存入redis中map对象的key，后面跟的是放入map中的对象的key，后面的key可以跟多个，是可变参数
        List<String> rsmap = jedis.hmget("course", "courseid", "courseprice","type","sales", "bread");
        System.out.println(rsmap);
    }

    @Test
    public void testuser() {
        //-----添加数据----------
        Map<String, String> map = new HashMap<>();
        List list = new ArrayList();
        list.add("课程1");
        list.add("课程2");
        map.put("name", "min");
        map.put("phone", "13920510359");
        map.put("passwd", "123456");
        map.put("birthday", "1997.2.5");
        map.put("telephone", "18835103332");
        map.put("address", "山西省");
        map.put("coursenum" , "1000");//学时数
        map.put("headimage", "/home/mzh/图片/1.png");
        map.put("yigou", "1");
//        map.put("yigou",list);
        jedis.hmset("user", map);
        //第一个参数是存入redis中map对象的key，后面跟的是放入map中的对象的key，后面的key可以跟多个，是可变参数
        List<String> rsmap = jedis.hmget("user", "name", "phone","passwd", "birthday","telephone", "address", "coursenum","headimage");
        System.out.println(rsmap);
    }
    }
//public class RedidTest {
//    private static String HOST = "127.0.0.1";
//    private static int PORT = 6379;
//
//    private static JedisPool jedisPool = null;
//
//    /*
//     * 初始化redis连接池
//     * */
//    private static void initPool(){
//        try {
//            JedisPoolConfig config = new JedisPoolConfig();
//
//            jedisPool = new JedisPool(config, HOST, PORT);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /*
//     * 获取jedis实例
//     * */
//    public synchronized static Jedis getJedis() {
//        try {
//            if(jedisPool == null){
//                initPool();
//            }
//            Jedis jedis = jedisPool.getResource();
//
//            return jedis;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//    public static byte[] serizlize(Object object){
//        ObjectOutputStream oos = null;
//        ByteArrayOutputStream baos = null;
//        try {
//            baos = new ByteArrayOutputStream();
//            oos = new ObjectOutputStream(baos);
//            oos.writeObject(object);
//            byte[] bytes = baos.toByteArray();
//            return bytes;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            try {
//                if(baos != null){
//                    baos.close();
//                }
//                if (oos != null) {
//                    oos.close();
//                }
//            } catch (Exception e2) {
//                e2.printStackTrace();
//            }
//        }
//        return null;
//    }
//    /*
//     * 反序列化
//     * */
//    public static Object deserialize(byte[] bytes){
//        ByteArrayInputStream bais = null;
//        ObjectInputStream ois = null;
//        try{
//            bais = new ByteArrayInputStream(bytes);
//            ois = new ObjectInputStream(bais);
//            return ois.readObject();
//        }catch(Exception e){
//            e.printStackTrace();
//        }finally {
//            try {
//
//            } catch (Exception e2) {
//                e2.printStackTrace();
//            }
//        }
//        return null;
//    }
//    public static void set(String key,String value){
//        Jedis jedis = getJedis();
//        jedis.set(key, value);
//        jedis.close();
//    }
//    public static String get(String key){
//        Jedis jedis = getJedis();
//        String value = jedis.get(key);
//        jedis.close();
//        return value;
//    }
//    public static void setObject(String key,Object object){
//        Jedis jedis = getJedis();
//        jedis.set(key.getBytes(), serizlize(object));
//        jedis.close();
//    }
//    public static Object getObject(String key){
//        Jedis jedis = getJedis();
//        byte[] bytes = jedis.get(key.getBytes());
//        jedis.close();
//        return deserialize(bytes);
//    }
//
//
//
//    @Test
//    public void testString(){
//        set("user:1", "sisu");
//        String user = get("user:1");
//        Assert.assertEquals("sisu", user);
//    }
//
//    @Test
//    public void testObject(){
//        setObject("user:2",new UserInfo(2,"lumia"));
//        UserInfo user = (UserInfo)getObject("user:2");
//        Assert.assertEquals("lumia", user.getName());
//    }
//
//
//
//    public static void setJsonString(String key,Object object){
//        Jedis jedis = getJedis();
//        jedis.set(key, JSON.toJSONString(object));
//        jedis.close();
//    }
//    public static Object getJsonObject(String key,Class clazz){
//        Jedis jedis = getJedis();
//        String value = jedis.get(key);
//        jedis.close();
//        return JSON.parseObject(value,clazz);
//    }
//
//
//    @Test
//    public void testObject2(){
//        setJsonString("user:3", new UserInfo(3,"xiaoming"));
//        UserInfo user = (UserInfo)getJsonObject("user:3",UserInfo.class);
//        Assert.assertEquals("xiaoming", user.getName());
//    }
//
//}
