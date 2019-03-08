package snc.server.shop.service;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import redis.clients.jedis.Jedis;
import snc.server.shop.pojo.User;

import java.sql.SQLException;
import java.util.Random;

public class LRService {
    public static String register(String phoneNumber, String cpt, String password) throws SQLException {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        String identityCode = "";
        identityCode = jedis.get(phoneNumber);
        if ((identityCode == null) || !(cpt.equals(identityCode)) ) {
            return "{\"s\":\"1\"}";
        }
        //{"s":"1"}
        else if(tools.searchExistUser(phoneNumber)){
            return "{\"s\":\"2\"}";
        }
        else {
            String uuid = tools.generateUUID();
            tools.newUser2SQL(uuid,password,phoneNumber);
            tools.newUser2Redis(uuid,password,phoneNumber);
            return "{\"s\":\"0\"}";
        }
    }

    public static String generateIdentityCode(String phoneNumber) {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        String identityCode = "";
        identityCode = jedis.get(phoneNumber);

        if (identityCode != null) {
            return "{\"s\":\"e\",\"t\":" + jedis.ttl(phoneNumber) + "}";
        } else {
            String status = jedis.get(phoneNumber + "time");
            identityCode = "";
            String str = "0123456789";
            Random r = new Random();
            String arr[] = new String[4];
            if (status == null) {
                for (int i = 0; i < 4; i++) {
                    int n = r.nextInt(10);

                    arr[i] = str.substring(n, n + 1);
                    identityCode += arr[i];

                }
//                SendSmsResponse response = sendSms(identityCode);
                jedis.set(phoneNumber, identityCode, "NX", "EX", 120);
                jedis.set(phoneNumber + "time", "1", "NX", "EX", 60 * 60 * 12);
            } else if (Integer.parseInt(status) > 3) {
                return "{\"s\":\"o\",\"t\":\"9999\"}";
            } else {
                for (int i = 0; i < 4; i++) {
                    int n = r.nextInt(10);

                    arr[i] = str.substring(n, n + 1);
                    identityCode += arr[i];

                }
//                SendSmsResponse response = sendSms(identityCode);
                jedis.set(phoneNumber, identityCode, "NX", "EX", 120);
                System.out.println(String.valueOf(Integer.parseInt(status) + 1));
                jedis.set(phoneNumber + "time", String.valueOf(Integer.parseInt(status) + 1), "XX", "EX", 60 * 60 * 12);
                // NX是不存在时才set， XX是存在时才set， EX是秒，PX是毫秒
            }
            return "{\"s\":\"ok\",\"t\":\"120\"}";
        }
    }

    public static String pidlogin(String pid){
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        if(jedis.exists(pid)){
            return "{\"r\":\"0\"}";
        }
        else {
            return "{\"r\":\"1\"}";
        }
    }

    public static String pwdlogin(String phoneNumber, String password) throws SQLException {
        User user = tools.getUserbyTel(phoneNumber);
        if(phoneNumber.equals(user.getPhoneNumber()) && password.equals(user.getPassword())){
            return "{\"r\":\"0\",\"d\":\""+user.getUserId()+"\"}";
        }
        else{
            return "{\"r\":\"1\"}";
        }
    }

    public static void main(String[] args) throws SQLException {
        System.out.println(register("18920862117","3129","root"));
//        generateIdentityCode("18920862117");
    }
}
