package snc.server.ide.handler;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import snc.boot.util.common.Router;
import snc.server.ide.service.UinfoService;
import snc.server.ide.service.handler.UinfoHandle;


import javax.annotation.PostConstruct;
import java.util.Map;

@Controller
public class UinfoHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = Logger.getLogger(UinfoHandler.class);

    private static UinfoHandler uinfoHandler;

    @Autowired
    private UinfoService uinfoService;

    @PostConstruct
    public void init(){
        uinfoHandler = this;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        FullHttpRequest fhr = (FullHttpRequest) msg;
        UinfoHandle uinfoHandle=new UinfoHandle(uinfoHandler.uinfoService);
        JSONObject jsonObject;
        String json="";
        String uid="";
        String url=fhr.uri();

        switch (url){
            //显示个人基本信息
            case "/snc/uinfo" :
                logger.info("handle---------"+fhr.uri());
                jsonObject=uinfoHandle.getMessage(fhr);
                uid= String.valueOf(jsonObject.get("uid"));
                json=uinfoHandle.disMsg(uid);
                Router.sendMessage("0",json,ctx);
                break;

                //修改个人基本信息
            case "/snc/uinfo/change" :
                logger.info("handle---------"+fhr.uri());
                Map map=uinfoHandle.JSONObjectToMap(fhr);
                json=uinfoHandle.changeMsg(map);
                Router.sendMessage("0",json,ctx);
                break;

                //删除个人基本信息
            case "/snc/uinfo/del" :
                logger.info("handle---------"+fhr.uri());
                jsonObject=uinfoHandle.getMessage(fhr);
                uid= String.valueOf(jsonObject.get("uid"));
                json=uinfoHandle.delUinfo(uid);
                Router.sendMessage("0",json,ctx);
                break;

                //查看已购云主机
            case "/snc/uinfo/vm" :
                logger.info("handle---------"+fhr.uri());
                jsonObject=uinfoHandle.getMessage(fhr);
                uid= String.valueOf(jsonObject.get("uid"));
                json=uinfoHandle.getUinfoVm(uid);
                Router.sendMessage("0",json,ctx);
                break;

            default:
                ctx.fireChannelRead(msg);
                break;
        }

    }
}
