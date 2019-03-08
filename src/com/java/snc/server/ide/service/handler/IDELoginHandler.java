package snc.server.ide.service.handler;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import snc.boot.redis.GetJedis;
import snc.boot.util.FinalTable;
import snc.boot.util.common.BaseString;
import snc.boot.util.common.Router;
import snc.server.ide.handler.LoginHandler;
import snc.server.ide.pojo.Status;
import snc.server.ide.pojo.UserInfo;
import snc.server.ide.service.UserInfoService;
import snc.server.ide.util.StartDocker;

import java.io.IOException;

public class IDELoginHandler {
    private static Logger logger = Logger.getLogger(LoginHandler.class);
    private ChannelHandlerContext ctx;
    private UserInfoService userInfoService;

    public IDELoginHandler(ChannelHandlerContext ctx, UserInfoService userInfoService) {
        this.ctx = ctx;
        this.userInfoService = userInfoService;
    }

    public void login(JSONObject o) {
        Status status = new Status();
        logger.info("ide login");
        String aid = o.getString(FinalTable.ARM_USER_ID);
        logger.info("user id : " + aid);
        String ide_key = FinalTable.Prefix_AID + aid;
        Jedis jedis = GetJedis.getJedis();
        String uuid = FinalTable.NIL;
        String dockerid = FinalTable.NIL;
        String debugPort = FinalTable.NIL;
        UserInfo userInfo = new UserInfo();
        userInfo.setIde_id(aid);
        String data = FinalTable.NIL;
        int sta = 0;

        if (jedis.exists(ide_key)) {
            uuid = jedis.get(ide_key);
            if (uuid == null) {
                uuid = userInfoService.getUUID(aid);
                if (uuid == null) {
                    status.setResult(FinalTable.ERROR);
                    status.setData(data);
                    Router.sendMessage(ctx, status);
                    return;
                }
                sta = userInfoService.getStatus(aid);
                if (sta == 0) {
                    // 首次登录,创建docker
                }
            }
        } else {
            uuid = userInfoService.getUUID(aid);
            if (uuid == null) {
                logger.error(uuid + " not exist!" );
                status.setResult(FinalTable.ERROR);
                status.setData(data);
                Router.sendMessage(ctx, status);
            }
            jedis.set(ide_key,uuid);
        }
        dockerid = jedis.hget(FinalTable.USER_ID + uuid, FinalTable.DOCKER_ID);
        debugPort = jedis.hget(FinalTable.USER_ID + uuid, FinalTable.DEBUG_PORT);
        if (dockerid == null) {
            dockerid = userInfoService.getDockerID(uuid);
            if (BaseString.isEmpty(dockerid)) {
                try {
                    userInfo = StartDocker.createDocker(userInfo);
                } catch (IOException | InterruptedException e) {
                    logger.error(uuid + " create docker error error is :" + e.getMessage());
                    status.setResult(FinalTable.ERROR);
                    status.setData(data);
                    Router.sendMessage(ctx, status);
                }
            }
            userInfo.setDockerid(dockerid);
        }
        if (debugPort == null) {
            debugPort = userInfoService.getDebugPort(uuid);
            if (debugPort == null) {
                try {
                    userInfo = StartDocker.createDockerPort(userInfo);
                } catch (IOException e) {
                    logger.error(uuid + " create debug error error is :" + e.getMessage());
                    status.setResult(FinalTable.ERROR);
                    status.setData(data);
                    Router.sendMessage(ctx, status);
                }
            }

        }
        userInfo.setUser_id(uuid);
        userInfo.setDockerid(dockerid);
        userInfo.setIde_id(uuid);
        userInfo.setPort(debugPort);
        data = JSON.toJSONString(userInfo);
        status.setData(data);
        status.setResult("0");
        Router.sendMessage(ctx,status);
    }
}
