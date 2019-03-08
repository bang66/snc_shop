package snc.boot.connector;

import snc.boot.inithandler.ShopInitHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import org.apache.log4j.Logger;

import java.util.concurrent.Executors;

/**
 * Created by jac on 18-11-12.
 */
public class ShopServerConnector {
    private String ip;
    private int port;
    private static Logger logger = Logger.getLogger(ShopServerConnector.class);
    public ShopServerConnector(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void run () {

        EventLoopGroup bossGroup = new EpollEventLoopGroup(0x1, Executors.newCachedThreadPool()); //mainReactor    1个线程 Server建议用newCachedThreadPool  client的线程数建议用核数×2,4都行
        EventLoopGroup workerGroup = new EpollEventLoopGroup(Runtime.getRuntime().availableProcessors() * 0x3, Executors.newCachedThreadPool());   //subReactor       线程数量等价于cpu个数+1
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(EpollServerSocketChannel.class);
            bootstrap.childHandler(new ShopInitHandler());
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            ChannelFuture future = bootstrap.bind(ip, port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("bootserver error, Execption is :\n" + e.toString());
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
