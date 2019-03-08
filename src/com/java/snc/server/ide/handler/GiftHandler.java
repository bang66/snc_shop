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
import snc.server.ide.pojo.Gift;
import snc.server.ide.service.GiftService;
import snc.server.ide.service.handler.BuyGift;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Controller
public class GiftHandler extends ChannelInboundHandlerAdapter {
    Logger logger = Logger.getLogger(ClassHandler.class);
    public String json;
    private static GiftHandler giftHandler;
    @Autowired
    private GiftService giftService;

    @PostConstruct
    public void init() {
        giftHandler = this;
    }


    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest fhr = (FullHttpRequest) msg;
        FullHttpResponse response = null;
        String uri = fhr.uri();

        if (fhr.uri().equals("/snc/buy/gift")) {
            logger.info("/snc/buy/gift is success");
            JSONObject jsonObject = Router.getMessage(fhr);
            String pid = jsonObject.getString(FinalTable.UUID);
            String gid = jsonObject.getString(FinalTable.GIFT_ID);
            Gift gift = new Gift();
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dd = sdf.format(date);// new Date()为获取当前系统时间
            String newDate = sdf.format(new Date());
            String result = "";
            BuyGift buyGift = new BuyGift(giftHandler.giftService);
            Random random = new Random();
            for (int i = 0; i < 3; i++) {
                result += random.nextInt(10);
            }
            String orderId = newDate + result;   //获取订单号
            System.out.println(gift);
            if (buyGift.buyGift(pid, gid)) {
                json = "{\"res\":\"true\",\"data\":\"" + dd + "\",\"orderid\":\"" + orderId + "\"}";
                System.out.println(json);
            } else {
                json = "{\"res\":\"false\",\"data\":\"" + dd + "\",\"orderid\":\"" + orderId + "\"}";

            }
            Router.sendMessage("0", json, ctx);

        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
