package snc.server.ide.service.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import snc.boot.redis.GetJedis;
import snc.server.ide.pojo.CartCommodity;

import java.util.List;

public class Add {                //点击加入购物车时的redis流程，购物车的数据存储
    private static Logger log=Logger.getLogger(Add.class);

    private Jedis jedis = GetJedis.getJedis();

    public void add(String uid,CartCommodity commodity){

        String coStr=jedis.hget("myc_"+uid,commodity.getC_sid());
        Transaction multi=jedis.multi();                //用返回的jedis对象开启事务
        jedis.watch("myc_" +uid);// 监听key
        CartCommodity commodity1=(CartCommodity) JSONObject.toJavaObject(JSON.parseObject(coStr), CartCommodity.class);
        if(commodity1!=null){ //在购物车里，用户再一次的选择商品的数量和京东一样,如果不为null，说明说明我是在购物车里修改商品的数量
            commodity1.setNum(commodity.getNum());              //购买数量
            log.info("用户 "+uid+" 修改购物车商品  "+commodity.getC_sid()+"  数量为："+commodity.getNum());
        }else{

            commodity1=commodity;
            log.info("用户 "+uid+" 添加商品  "+commodity.getC_sid()+" 到购物车数量为："+commodity.getNum());


        }
        multi.hset("myc_"+uid,commodity.getC_sid(), JSON.toJSONString(commodity1));
        // 当取redis缓存的时候，值发生改变的，watch下面的事物事件会中断，这样的话
        List<Object> exec = multi.exec();
        // 释放监听
//        jedis.unwatch(jedis);
        // 当事务执行失败是重新执行一次代码
        if (exec == null) {
            add(uid,commodity);
        }

    }
    public String dianjigouwuche(String pid){    //当用户点击购物车时，给前端返回所有的商品

        List<String> strings=jedis.hvals("myc_"+pid);

        log.info("用户 "+pid+" 查看购物车商品  ");

        JSONArray json = (JSONArray) JSONArray.parse(String.valueOf(strings));

        return String.valueOf(strings);

    }


public  void del(String uid,String sid){
    jedis.hdel("myc_"+uid,sid);
    log.info("用户 "+uid+" 删除购物车商品  "+sid);

}
//    public boolean buyCommodity(CartCommodity commodity){          //付款
//        Transaction multi=jedis.multi();
//        int money= Integer.parseInt(jedis.hget(commodity.getPid(),"money"));
//        if(money-commodity.getPrice()>=0){
//            int money1= (int) (money-commodity.getPrice());
//            multi.hset("buy_"+commodity.getPid(),commodity.getSid(), JSON.toJSONString(commodity));
//            multi.hset(commodity.getPid(),"money", String.valueOf(money));
//            return true;
//        }else {
//            return false;
//        }
//
//    }
//    public String product(String s){
//        JSONObject jsonObj = JSON.parseObject(s);
//        ResultSet resultSet=null;
//        String pt1=jsonObj.getString("pt");
//        String limit=jsonObj.getString("limit");
//        int limit1= Integer.parseInt(limit);
//        String type1=jsonObj.getString("type");
//        String brand1=jsonObj.getString("brand");
//        String text1=jsonObj.getString("text");
//
//        return String.valueOf(resultSet);
//    }

}
