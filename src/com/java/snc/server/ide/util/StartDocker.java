package snc.server.ide.util;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import snc.boot.redis.GetJedis;
import snc.boot.util.FinalTable;
import snc.boot.util.RedisUtils;
import snc.boot.util.common.BaseString;
import snc.server.ide.pojo.Data;
import snc.server.ide.pojo.User;
import snc.server.ide.pojo.UserInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;

/**
 * Created by jingbao on 18-11-7.
 */
public class StartDocker {
    public static void main(String[] args) throws IOException, InterruptedException {

        Process process=Runtime.getRuntime().exec(new String[]
                {"/home/jac/桌面/shell/startDocker.sh",
                        "/home/jac/桌面/MAC",
                        "/data"},null,null);
        BufferedReader read=new BufferedReader(new InputStreamReader(process
                .getInputStream()));
        process.waitFor();
        String res="";
        String line="";
        while ((line=read.readLine())!=null){
            res=res+line;
        }
        UserInfo data = new UserInfo();
        data.setUser_id("qqqq");
        createDocker(data);

    }

    private static Logger log = Logger.getLogger(StartDocker.class);


    public static UserInfo createDocker(UserInfo data) throws IOException, InterruptedException {
        File file = new File("/home/jac/桌面/MAC/" + data.getUser_id());
        if (!file.exists()) {
            file.mkdir();
        }
        if (BaseString.isEmpty(data.getDockerid())) {
            Process process = Runtime.getRuntime().exec(new String[]
                    {"/home/jac/桌面/shell/startDocker.sh", "7777", "/home/jac/桌面/" + data.getUser_id(), "/home"}
                    , null, null);
            BufferedReader read = new BufferedReader(new InputStreamReader(process.getInputStream()));
            process.waitFor();
            String res = "";
            String line = "";
            while ((line = read.readLine()) != null) {
                res = res + line;
            }
            if (res == "" || res.equals("")) {
            } else {
                String[] id = res.split("[ ]");
                Jedis jedis = RedisUtils.getJedis();
                jedis.hset(FinalTable.USER_ID+data.getUser_id(), FinalTable.DOCKER_ID, id[8]);
                data.setDockerid(id[8]);
            }
        }
        return data;
    }

    public static UserInfo createDockerPort(UserInfo userInfo) throws IOException {
        ServerSocket serverSocket = new ServerSocket(0);
        String port = String.valueOf(serverSocket.getLocalPort());
        Jedis jedis = RedisUtils.getJedis();
        jedis.hset(FinalTable.USER_ID+userInfo.getIde_id(),FinalTable.DEBUG_PORT, port);
        userInfo.setPort(port);
        return userInfo;
    }
}
