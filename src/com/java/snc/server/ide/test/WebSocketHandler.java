package snc.server.ide.test;

import snc.boot.boot.Boot;
import snc.boot.util.common.BaseString;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.*;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * Created by jac on 18-11-12.
 */
public class WebSocketHandler extends SimpleChannelInboundHandler<Object> {
    private static Logger logger = Logger.getLogger(WebSocketHandler.class);
    private WebSocketServerHandshaker handshaker;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("客户端链接");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            handlerHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private void handlerHttpRequest (ChannelHandlerContext ctx, FullHttpRequest req ) throws Exception {
        if (!"websocket".equals(req.headers().get("Upgrade"))) {
            ctx.fireChannelRead(req);
        }
        if (!BaseString.isEmpty(Boot.getWebsocketUrl())) {
            WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(Boot.getWebsocketUrl(),null, false);
            handshaker = wsFactory.newHandshaker(req);
            if (handshaker == null) {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                handshaker.handshake(ctx.channel(),req);
            }
        } else {
            logger.error("you went to use websocket, but websocket url is null, please write websocket in properties!");
        }
    }

    private void handleWebSocketFrame (ChannelHandlerContext ctx, WebSocketFrame frame) {
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        if (!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass().getName()));
        }
        String request = ((TextWebSocketFrame) frame).text();
        ctx.channel().write(new TextWebSocketFrame(request+" ,netty websocket snc.server")+new Date().toString());
    }
}
