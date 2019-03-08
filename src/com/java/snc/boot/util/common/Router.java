package snc.boot.util.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Set;
import io.netty.channel.socket.SocketChannel;
import snc.boot.boot.Boot;
import snc.boot.util.FinalTable;
import snc.server.ide.pojo.Status;
import snc.server.ide.test.WebSocketHandler;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by jac on 18-11-12.
 */
public class Router {
    private static final Logger log = Logger.getLogger(Router.class);
    private static final String classSuffix = "Handler";
    private static WebSocketHandler webSocketHandler;
    public static void init(String path, SocketChannel sc) {
        log.info(path);
        Set<Class<?>> classes = BaseFile.getClasses(path, classSuffix, true);
        System.out.println(classes.toString());
        for (Class<?> c : classes) {
            String className = c.getName();
            try {
                ChannelHandler handler = (ChannelHandler) c.newInstance();
                // 只取Handler前面部分
                String handlerName = className.replace(path + ".", "").replace(classSuffix, "").toLowerCase();
//                if (webSocketHandler == null) {
//                    sc.pipeline().addLast(new WebSocketHandler());
//                }
                sc.pipeline().addLast(handler);
                log.info("handler类 : " + handlerName + " 加载完成");
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("handler类 : " + className + " 加载失败", e);
            }
        }
    }

    public static JSONObject getMessage(FullHttpRequest fhr) {
        ByteBuf bbuf= fhr.content();
        Charset charset = Boot.getCharset();
        String json=bbuf.toString(charset);
        JSONObject object = JSON.parseObject(json);
        return object;
    }

    public static void sendMessage(String res, String data, ChannelHandlerContext ctx) {
        Status status = new Status();
        status.setResult(res);
        status.setData(data);
        String result ="{\"" + FinalTable.SEND_RESULT + "\":\""+res+"\",\""+FinalTable.SEND_DATA+"\":"+data+"}";
        FullHttpResponse response = null;
        log.info("result-----"+result);
        try {
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(result.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            log.error(ctx.channel().id() + " send message is error" + e);
        }
        response.headers().set("Access-Control-Allow_Origin","*");
        response.headers().set(CONTENT_TYPE, "text/html");
        response.headers().setInt(CONTENT_LENGTH,
                response.content().readableBytes());
        ctx.writeAndFlush(response);
    }

    public static void sendMessage(ChannelHandlerContext ctx, Status status) {
        String result ="{\"" + FinalTable.SEND_RESULT + "\":\""+status.getResult()+"\",\""+FinalTable.SEND_DATA+"\":"+status.getData()+"}";
        FullHttpResponse response = null;
        log.info("result-----"+result);
        try {
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(result.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            log.error(ctx.channel().id() + " send message is error" + e);
        }
        response.headers().set("Access-Control-Allow_Origin","*");
        response.headers().set(CONTENT_TYPE, "text/html");
        response.headers().setInt(CONTENT_LENGTH,
                response.content().readableBytes());
        ctx.writeAndFlush(response);
    }

}
