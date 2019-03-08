package snc.server.ide.handler;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import snc.boot.util.FinalTable;
import snc.boot.util.common.Router;
import snc.server.ide.service.UserInfoService;
import snc.server.ide.service.handler.IDELoginHandler;
import snc.server.shop.service.handler.ShopLoginHandler;

import javax.annotation.PostConstruct;

/**
 * Created by jac on 18-11-16.
 */
@Controller
public class LoginHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = Logger.getLogger(LoginHandler.class);
    @Autowired
    private UserInfoService userInfoService;

    private static LoginHandler loginHandler;

    @PostConstruct
    public void init(){
        loginHandler = this;
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest fhr = (FullHttpRequest) msg;
        FullHttpResponse freq = null;
        if (fhr.uri().equals("/snc/ide/login")) {
            logger.info("/snc/ide/login sucess");
            JSONObject o = Router.getMessage(fhr);
            String type = o.getString(FinalTable.LOGIN_TYPE);
            switch (type) {
                case "ide":
                    IDELoginHandler Ilogin = new IDELoginHandler(ctx, loginHandler.userInfoService);
                    Ilogin.login(o);
                    break;
                case "shop":
                    ShopLoginHandler Slogin = new ShopLoginHandler();
                    break;
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
