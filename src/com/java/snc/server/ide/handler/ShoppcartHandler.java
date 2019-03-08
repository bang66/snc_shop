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
import snc.server.ide.pojo.CartCommodity;
import snc.server.ide.service.HoutaiCommodityService;
import snc.server.ide.service.handler.Add;
import snc.server.ide.test.Elasticsearch.Elasticsearch5;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
@Controller
public class ShoppcartHandler extends ChannelInboundHandlerAdapter {
    Logger logger = Logger.getLogger(ShoppcartHandler.class);

    private static ShoppcartHandler shoppcartHandler;
    @Autowired
    private HoutaiCommodityService houtaiCommodityService;
    @PostConstruct
    public void init(){
        shoppcartHandler = this;
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest fhr = (FullHttpRequest) msg;
        FullHttpResponse freq = null;
        String uri = fhr.uri();
        switch (uri) {
            //购物车
            case "/snc/shop/commodity/addcart":
                logger.info("/snc/shop/commodity/addcart is success");
                JSONObject o = Router.getMessage(fhr);
                CartCommodity cartCommodity = new CartCommodity();
                cartCommodity.setC_sid(o.getString(FinalTable.COMMOFITY_SID));
                cartCommodity.setNum(Integer.parseInt(o.getString(FinalTable.COMMODITY_NUM)));

                Add add = new Add();
                add.add(o.getString(FinalTable.USER_ID),cartCommodity);
                Date dNow = new Date();
                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String json = null;
                json = "{\"res\":\"true\",\"date\":" + ft.format(dNow) + "}";
                Router.sendMessage("0", json, ctx);
                break;
            case "/snc/shop/cart/check":
                logger.info("/snc/shop/cart/check is success");

                JSONObject o1 = Router.getMessage(fhr);
                Add add1 = new Add();
                String check = add1.dianjigouwuche(o1.getString(FinalTable.USER_ID));  //只需要pid就能查
                Date dNow1 = new Date();
                SimpleDateFormat ft1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String json1 = null;
                json1 = "{\"res\":\"true\",\"list\":" + check + ",\"date\":" + ft1.format(dNow1) + "}";
                Router.sendMessage("0", json1, ctx);
                break;
            case "/snc/shop/cart/del":
                logger.info("/snc/shop/cart/del is success");

                JSONObject o2 = Router.getMessage(fhr);
//                CartCommodity commodity2 = new CartCommodity();
//                commodity2.setPid(o2.getString(FinalTable.USER_ID));
//                commodity2.setPrice(Integer.parseInt(o2.getString(FinalTable.COMMODITY_PRICE)));
//                commodity2.setMod(o2.getString(FinalTable.COMMODITY_MOD));
//                commodity2.setCol(o2.getString(FinalTable.COMMODITY_COL));
//                commodity2.setNum(Integer.parseInt(o2.getString(FinalTable.COMMODITY_NUM)));
//                commodity2.setSid(o2.getString(FinalTable.COMMOFITY_SID));
//                commodity2.setSize(o2.getString(FinalTable.COMMODITY_SIZE));
                Add add2 = new Add();
                add2.del(o2.getString(FinalTable.USER_ID),o2.getString(FinalTable.COMMOFITY_SID));

                Date dNow2 = new Date();
                SimpleDateFormat ft2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String json2 = null;

                json = "{\"res\":\"true\",\"date\":" + ft2.format(dNow2) + "}";
                Router.sendMessage("0", json, ctx);
                break;
            case "/snc/shop/cart/dell":
                Elasticsearch5 e=new Elasticsearch5(shoppcartHandler.houtaiCommodityService);
                e.createCommodity();
                System.out.println("===================");
                break;
            default:
                ctx.fireChannelRead(msg);
                break;
        }
    }
}
