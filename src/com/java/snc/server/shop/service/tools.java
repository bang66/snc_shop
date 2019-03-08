package snc.server.shop.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import redis.clients.jedis.Jedis;
import snc.server.shop.pojo.User;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class tools {
    public static String generateUUID(){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println(uuid);
        return uuid;
    }

    public static User getUserbyTel(String phoneNumber) throws SQLException {
        Connection conn=getconn();
        String sql="select * from userInfo where (phoneNumber) = (?)";
        PreparedStatement ps=(PreparedStatement) conn.prepareStatement(sql);
        ResultSet rs=null;
        ps.setString(1,phoneNumber);
        rs = ps.executeQuery();
        User user = new User();
        while (rs.next()){
            user.setUserId(rs.getString("userId"));
            user.setPassword(rs.getString("password"));
            user.setPhoneNumber(rs.getString("phoneNumber"));
            user.setShoppingCart(rs.getString("shoppingCart"));
            user.setSharedId(rs.getString("sharedId"));
            user.setBoughtuuid(rs.getString("boughtuuid"));
            user.setMoney(rs.getString("money"));
            user.setScore(rs.getString("score"));
            user.setAmount(rs.getString("amount"));
        }
        return user;
    }

    public static int newUser2SQL(String uuid,String password,String phoneNumber) throws SQLException {
        Connection conn=getconn();
        String sql="insert into userInfo (userId,password,phoneNumber,shoppingCart,sharedId,boughtuuid,money,score,amount) values ((?),(?),(?),(?),(?),(?),(?),(?),(?))";
        PreparedStatement ps=(PreparedStatement) conn.prepareStatement(sql);
        ResultSet rs=null;
        ps.setString(1,uuid);
        ps.setString(2,password);
        ps.setString(3,phoneNumber);
        ps.setString(4,"nil");
        ps.setString(5,"nil");
        ps.setString(6,"nil");
        ps.setString(7,"0");
        ps.setString(8,"0");
        ps.setString(9,"0");
        return ps.executeUpdate();
    }

    public static void newUser2Redis(String uuid,String password,String phoneNumber){
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        HashMap hashMap = new HashMap();
        hashMap.put("P",password);
        hashMap.put("T",phoneNumber);
        hashMap.put("G","nil");
        hashMap.put("S","nil");
        hashMap.put("Y","nil");
        hashMap.put("M","0");
        hashMap.put("J","0");
        hashMap.put("F","0");
        jedis.set(uuid,JSON.toJSONString(hashMap));
        Set<String> set = (Set<String>) JSON.parseObject(jedis.get("ok_phone"),new TypeReference<Set<String>>() {});
        set.add(phoneNumber);
        jedis.set("ok_phone",JSON.toJSONString(set));
    }

    public static Connection getconn(){
        String driver = "com.mysql.jdbc.Driver";
        String url="jdbc:mysql://127.0.0.1/platform?useSSL=false";
        String username="root";
        String password="root";
        Connection conn=null;
        try {
            Class.forName(driver);
            conn=(Connection) DriverManager.getConnection(url,username,password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static boolean searchExistUser(String phoneNumber){
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        String userList = jedis.get("ok_phone");
        Set<String> set = (Set<String>) JSON.parseObject(userList,new TypeReference<Set<String>>() {
        });
        return set.contains(phoneNumber);
    }

    public static void main(String[] args) {
        tools.generateUUID();
    }
}
