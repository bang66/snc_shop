package snc.server.shop.handler;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder;

import org.apache.log4j.Logger;
import snc.server.shop.service.LRService;

import java.text.SimpleDateFormat;
import java.util.Date;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


public class ShopHandler extends ChannelInboundHandlerAdapter {
    Logger logger = Logger.getLogger(ShopHandler.class);
    public  String json="xxx";
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest fhr = (FullHttpRequest) msg;
        String uri = fhr.uri();
        FullHttpResponse response = null;
        ByteBuf buf = fhr.content();
        String message = buf.toString(io.netty.util.CharsetUtil.UTF_8);
        buf.release();
        HttpPostRequestDecoder decoder = null;
        //{"pn":"15522210826"}
        if(uri.equals("/snc/start/register/cpt")) {
            System.out.println("调用发送验证码接口");
            JSONObject obj = JSON.parseObject(message);
            json = LRService.generateIdentityCode(obj.getString("pn"));
        }
        //{"pn":"18920862117","cpt":"8888","pwd":"admin"}
        if(uri.equals("/snc/start/register")) {
            JSONObject obj = JSON.parseObject(message);
            json = LRService.register(obj.getString("pn"),obj.getString("cpt"),obj.getString("pwd"));
        }
        //{"pid":"qwer"}
        if(uri.equals("/snc/start/pidlogin")) {
            JSONObject obj = JSON.parseObject(message);
            json = LRService.pidlogin(obj.getString("pid"));
        }
        //{"pn":"18920862117","pwd":"admin"}
        if(uri.equals("/snc/start/pwdlogin")) {
            JSONObject obj = JSON.parseObject(message);
            json = LRService.pwdlogin(obj.getString("pn"),obj.getString("pwd"));
        }

            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(json.getBytes("UTF-8")));
            response.headers().set("Access-Control-Allow_Origin","*");
            response.headers().set(CONTENT_TYPE, "text/json");
//            response.headers().set(ACCESS_CONTROL_ALLOW_ORIGIN,"*");
            response.headers().setInt(CONTENT_LENGTH,
                    response.content().readableBytes());
            ctx.writeAndFlush(response);
    }
}
