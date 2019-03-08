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
import snc.server.ide.service.ClassService;
import snc.server.ide.service.UserInfoService;
import snc.server.ide.test.Course;
import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ClassHandler extends ChannelInboundHandlerAdapter{
    Logger logger = Logger.getLogger(ClassHandler.class);
    public  String json;
    private static ClassHandler classHandler;

    @Autowired
    private ClassService classService;

    @PostConstruct
    public void init(){
        classHandler = this;
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest fhr = (FullHttpRequest) msg;
        FullHttpResponse freq = null;
        if (fhr.uri().equals("/snc/buy/class")) {
            JSONObject o = Router.getMessage(fhr);
            logger.info("jsonObject-------"+o);
            String pid = o.getString(FinalTable.AH_USER_ID);
            String cid = o.getString(FinalTable.CLASS_ID);
            Course course = new Course(classHandler.classService);
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dd = df.format(date);// new Date()为获取当前系统时间

            if (course.buyCourse(pid,cid)){

                json = "{\"res\":\"true\",\"data\":\""+dd+"\",\"onum\":\"xxxx\"}";
                Router.sendMessage("0",json,ctx);
                logger.info("sucess class pay");
            }else {

                json =  "{\"res\":\"false\",\"data\":\""+dd+"\",\"onum\":\"xxxx\"}";
                Router.sendMessage("0",json,ctx);

                logger.info("false class pay");
            }

        }else {
            ctx.fireChannelRead(msg);
        }
    }
}
