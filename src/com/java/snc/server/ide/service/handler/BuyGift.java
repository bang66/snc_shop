package snc.server.ide.service.handler;

import com.sun.javafx.iio.gif.GIFDescriptor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import redis.clients.jedis.Jedis;
import snc.boot.redis.GetJedis;
import snc.server.ide.dao.GiftDao;


import snc.server.ide.pojo.Gift;
import snc.server.ide.pojo.User;
import snc.server.ide.service.GiftService;

import java.sql.ResultSet;
import java.sql.Statement;

public class BuyGift {
    private GiftService giftService;

    public BuyGift(GiftService giftService) {
        this.giftService = giftService;
    }

    //    //连接redis
    private static Jedis jedis = GetJedis.getJedis();


    public  boolean buyGift(String pid, String g_id) {
        System.out.println(g_id);
        //首先查询该账户id的余额
        System.out.println(jedis.hmget(pid, "pt"));
        String num = jedis.hget(pid, "pt");
        int pt = Integer.parseInt(num);

        String price = String.valueOf(jedis.hget(g_id, "gprice"));
        int gprice = Integer.parseInt(price);

        //库存的数量
        String g_stock = jedis.hget(g_id, "gnum");
        int gnum = Integer.parseInt(g_stock);
        if (pt >= gprice) {
            /*对redis进行更改*/
            pt = pt - gprice;
            num = String.valueOf(pt);
            jedis.hset(pid, "pt", num);
//            userDao.updatept(pid,num);//该改数据库
            //更改库存
            int g_stock1 =gnum -=1;
//                gnum1 = String.valueOf(gnum);
            jedis.hset(g_id,"gnum",String.valueOf( g_stock1));
//            logger.info("当前所有的课时数-------" + jedis.hget(pid, "coursenum"));
            System.out.println(jedis.hget(pid, "pt"));
            //然后在该账户的已购课程中加入该课程的id
            jedis.hset(pid, "yigou", jedis.hget(g_id, "gid"));
//           System.out.println("已购礼物： " + jedis.hget(pid, "yigou"));
            //对数据库进行更改
            Gift gift = new Gift();
            gift.setG_id(g_id);
            gift.setG_stock(String.valueOf( g_stock1));
            System.out.println(gift.getG_id()+"   "+gift.getG_stock());

            giftService.UpdateStock(gift);

            User user = new User();
            user.setUuid(pid);
            user.setPt(num);
            System.out.println(user.getUuid()+"============="+user.getPt());
            giftService.Updatept(user);
            return true;
        } else {
            System.out.println("余额不足");
            return false;
        }
    }
}
