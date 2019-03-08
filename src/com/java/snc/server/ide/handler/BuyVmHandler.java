package snc.server.ide.handler;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import snc.boot.util.common.Router;
import snc.server.ide.pojo.Vm;

import snc.server.ide.service.VmService;
import snc.server.ide.service.handler.BuyVm;

import javax.annotation.PostConstruct;

@Controller
public class BuyVmHandler extends ChannelInboundHandlerAdapter {
    private static BuyVmHandler buyVmHandler;


    @Autowired
    private VmService vmService;



    @PostConstruct
    public void init(){
        buyVmHandler = this;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest fhr = (FullHttpRequest) msg;
        FullHttpResponse response = null;
        if ((fhr.uri()).equals("/snc/buy/vm")){
            System.out.println("hand---------"+buyVmHandler.vmService);

            BuyVm buyVm=new BuyVm(buyVmHandler.vmService);

            Vm vm=buyVm.getMessage(fhr);

            String gson=buyVm.buy(vm);

            Router.sendMessage("0",gson,ctx);
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
