package snc.server.ide.test;

import org.apache.log4j.Logger;
import org.junit.Test;

import org.springframework.stereotype.Controller;
import redis.clients.jedis.Jedis;
import snc.boot.redis.GetJedis;
import snc.server.ide.pojo.User;
import snc.server.ide.service.ClassService;

@Controller
public class Course {

    private ClassService classService;


    private Jedis jedis = GetJedis.getJedis();

    public Course(ClassService classService) {
        this.classService = classService;
    }

    Logger logger =Logger.getLogger(Course.class);


    public boolean buyCourse(String pid,String cid){
        String num="";
        String price;
        //首先查询该账户id的余额
        if (jedis.hexists(pid,"CM"))
        {
            System.out.println(jedis.hmget(pid, "CM"));
             num = jedis.hget(pid, "CM");
        }else {
            num = classService.getCM(pid);
           System.out.println(num+"---------num");
            jedis.hset(pid,"CM",num);
        }
        if (jedis.hexists(cid,"courseprice")){
             price = String.valueOf(jedis.hget(cid,"courseprice"));
        }else {
             price = classService.getClspt(cid);
             jedis.hset(cid,"courseprice",price);
        }
        int coursenum = Integer.parseInt(num);
        logger.info("当前所有的课时数-------"+coursenum);
        //该课程所需的学时数

        int courseprice = Integer.parseInt(price);
        logger.info("商品所需的课时数-------"+courseprice);
        if (coursenum>=courseprice){
            /*对redis进行更改*/
            coursenum=coursenum-courseprice;
            num = String.valueOf(coursenum);
            jedis.hset(pid,"CM",num);
            logger.info("当前所有的课时数-------"+jedis.hget(pid,"CM"));
            //然后在该账户的已购课程中加入该课程的id
            jedis.hset(pid,"yigou",jedis.hget(cid,"courseid"));
            System.out.println("已购课程： "+jedis.hget(pid,"yigou"));

            //对数据库进行更改
//            updatecourse(pid,num,cid);
            User user = new User();
            user.setUuid(pid);
            user.setCls_pt(num);
            user.setYG(cid);
            classService.Updatemoney(user);
            classService.Updateclass(user);
            return true;
        }else {
            System.out.println("余额不足");
            return false;
        }

    }
    @Test
    public void test(){
        if (buyCourse("user","course")){
            System.out.println("支付成功");
        }else {
            System.out.println("支付失败");
        }
    }

    public static void main(String[] args) {
//        Course c = new Course();
//        c.buyCourse("user","course");
//        Date date = new Date();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//        System.out.println(df.format(date));// new Date()为获取当前系统时间
    }


}
