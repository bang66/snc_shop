package snc.boot.inithandler;

import snc.boot.boot.Boot;
import snc.boot.util.common.Router;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Created by jac on 18-11-3.
 */
public class IDEInitChannel extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel sc) throws Exception {
        sc.pipeline().addLast(new HttpRequestDecoder());
        sc.pipeline().addLast(new HttpResponseEncoder());
        sc.pipeline().addLast(new HttpObjectAggregator(65535));
        sc.pipeline().addLast(new ChunkedWriteHandler());
        Router.init(Boot.getIdePackageName(),sc);
    }
}
