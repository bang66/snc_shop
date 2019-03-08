package snc.server.ide.service.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import io.netty.handler.codec.http.FullHttpRequest;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import redis.clients.jedis.Jedis;
import snc.boot.redis.GetJedis;
import snc.boot.util.common.Router;
import snc.boot.util.es.ElasticsearchService;
import snc.server.ide.pojo.A_User;
import snc.server.ide.pojo.Vm;
import snc.server.ide.pojo.VmPay;
import snc.server.ide.service.UinfoService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class UinfoHandle {
    private static Logger log=Logger.getLogger(UinfoHandle.class);
    private UinfoService uinfoService;
    Jedis jedis =  GetJedis.getJedis();
    public UinfoHandle(UinfoService uinfoService){
        this.uinfoService=uinfoService;
    }

    //获取时间戳
    public  String getDate(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
        return date;
    }

    //接受前端的请求，并返回JsonObject
    public JSONObject getMessage(FullHttpRequest fhr){
        JSONObject o = Router.getMessage(fhr);
        log.info("request json package----uri=/snc/uinfo----->"+o);
//        String uid = o.getString(FinalTable.UUID);
        return o;
    }


    //展示个人信息
    public String disMsg(String uid){
        A_User a_user=new A_User();
        a_user.setUid(uid);
        //从redis中读取个人信息
        if (jedis.exists(uid)){
            a_user.setName(jedis.hget(uid,"name"));
            a_user.setAddr(jedis.hget(uid,"addr"));
            a_user.setBir(jedis.hget(uid,"bir"));
            a_user.setCm(jedis.hget(uid,"cm"));
            a_user.setD_id(jedis.hget(uid,"d_id"));
            a_user.setEml(jedis.hget(uid,"eml"));
            a_user.setGm(jedis.hget(uid,"gm"));
            a_user.setHd(jedis.hget(uid,"hd"));
            a_user.setPh(jedis.hget(uid,"ph"));
            a_user.setPort(Integer.parseInt(jedis.hget(uid,"port")));
            log.info("从redis中获取到用户基本信息  uid---->"+uid);
        }
        //从es中读取个人信息
        else if (!esIsEmpty(uid)){
            a_user=getEsObj(uid);
            log.info("从es中获取到用户基本信息  uid---->"+uid);
        }
        //从mysql中读取个人信息
        else {
            a_user=uinfoService.getAllMessage(uid);
            log.info("从mysql中获取到用户基本信息  uid---->"+uid);
        }
        String json=JSON.toJSONString(a_user);
        return json;
    }

    //把json对象转为map
    public Map JSONObjectToMap(FullHttpRequest fhr){
        JSONObject obj=getMessage(fhr);
        Map<String, Object> map = JSONObject.parseObject(obj.toJSONString(), new TypeReference<Map<String, Object>>(){});
        return  map;
    }


    //修改个人信息
    public String changeMsg(Map<String,String> map){
        VmPay vmPay = new VmPay();
        String uid=map.get("uid");
        try {
            //修改redis中的uinfo信息
            jedis.hset(uid,"name",map.get("name"));
            jedis.hset(uid,"addr",map.get("addr"));
            jedis.hset(uid,"bir",map.get("bir"));
            jedis.hset(uid,"cm",map.get("cm"));
            jedis.hset(uid,"d_id",map.get("d_id"));
            jedis.hset(uid,"eml",map.get("eml"));
            jedis.hset(uid,"gm",map.get("gm"));
            jedis.hset(uid,"hd",map.get("hd"));
            jedis.hset(uid,"ph",map.get("ph"));
            jedis.hset(uid,"port",map.get("port"));
            log.info("redis中的用户基本信息已修改  uid---->"+uid);
            //修改mysql中的uinfo信息
            uinfoService.changeMsg(map);
            log.info("MySQL中的用户基本信息已修改  uid---->"+uid);


            //修改es中的uinfo信息
            map.remove("uid");
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(map));
            String  json= jsonObject.toString();
            ElasticsearchService esService=new ElasticsearchService("elasticsearch","127.0.0.1",9300);
            esService.updateData("IUser","IUserinfo",uid,json);
            log.info("es中的用户基本信息已修改  uid---->"+uid);

            vmPay.setState(true);
            String gson=JSON.toJSONString(vmPay);
            return  gson;

        } catch (Exception e){
            log.error("用户数据修改异常  uid---->"+uid+"        "+e.getMessage());
            vmPay.setState(false);
            String gson=JSON.toJSONString(vmPay);
            log.info("返回前端用户信息修改状态为失败   json包----->"+gson);
            return  gson;
        }

    }

    //删除个人信息
    public String delUinfo(String uid){
        try {
            //删除redis中的uinfo信息
            jedis.del(uid);
            log.info("redis中的用户基本信息已删除  uid---->"+uid);

            //删除mysql中的uinfo信息
            uinfoService.delUinfo(uid);
            log.info("mysql中的用户基本信息已删除  uid---->"+uid);


            //删除es中的uinfo信息
            ElasticsearchService elasticsearchService=new ElasticsearchService("elasticsearch","127.0.0.1",9300);
            elasticsearchService.deleteData("IUser","IUserinfo",uid);
            elasticsearchService.close();
            log.info("es中的用户基本信息已删除  uid---->"+uid);

            return "true";
        } catch (Exception e){
            log.error("用户数据删除异常  uid---->"+uid+"        "+e.getMessage());
            return "false";
        }
    }



    //显示用户已购云主机
    public String getUinfoVm(String uid){
        String json ="";
        if(jedis.hexists(uid,"vm")){
            json = jedis.hget(uid,"vm");
            if (json.equals("")||json.equals(null)){
                json="";
                log.info("从redis中未获取到用户购买云主机信息  uid---->"+uid+"  并设置redis的v为空值,同时返给前端空值");
                return json;
            }else {
                log.info("从redis中获取到用户购买云主机的信息  uid---->"+uid+"     信息为----->"+json+"   并设置到redis中");
                return json;
            }
        }else {
            List<Vm> vms = uinfoService.getUinfoVm(uid);
            log.info("从mysql中获取到用户购买云主机的信息  uid---->"+uid+"     信息----->"+vms.toString());
            if (vms.isEmpty()){
                json="";
                jedis.hset(uid,"vm",json);
                log.info("从mysql中未获取到用户购买云主机信息  uid---->"+uid+"  并设置redis的v为空值,同时返给前端空值");
                return json;
            }else {
                for (Vm vm : vms) {
                    vm.setUid(uid);
                }
                json = JSON.toJSONString(vms);
                jedis.hset(uid,"vm", json);
                log.info("从mysql中获取到用户购买云主机的信息  uid---->"+uid+"     信息为----->"+json+"   并设置到redis中");
                return json;
            }

        }
    }

    //通过es的index type id 判断记录是否存在
    public boolean esIsEmpty(String id){
        ElasticsearchService esService=new ElasticsearchService("elasticsearch","127.0.0.1",9300);
        Client client=esService.getClient();
        IdsQueryBuilder queryBuilder = QueryBuilders.idsQuery();
        queryBuilder.addIds(id);
        //index = IUser   type = IUserinfo
        SearchResponse response = client.prepareSearch("IUser").setTypes("IUserinfo").setQuery(queryBuilder).get();
        //查询所获取的数据都在hits里面，是以数据的形式，每一个hits[？]里面都有很多其他数据
        SearchHit[] hits = response.getHits().getHits();
        String json="";
        for (SearchHit hit : hits) {
            json = hit.getSourceAsString();
        }
        esService.close();
        return json.isEmpty();
    }



    //通过id查询该条记录信息
    public A_User getEsObj(String id){
        ElasticsearchService esService=new ElasticsearchService("elasticsearch","127.0.0.1",9300);
        Client client=esService.getClient();
        IdsQueryBuilder queryBuilder = QueryBuilders.idsQuery();
        queryBuilder.addIds(id);
        //index = IUser   type = IUserinfo
        SearchResponse response = client.prepareSearch("IUser").setTypes("IUserinfo").setQuery(queryBuilder).get();
//        SearchResponse response = client.prepareSearch("user").setTypes("userinfo").setQuery(queryBuilder).get();
        //查询所获取的数据都在hits里面，是以数据的形式，每一个hits[？]里面都有很多其他数据
        SearchHit[] hits = response.getHits().getHits();
        String json="";
        for (SearchHit hit : hits) {
            json = hit.getSourceAsString();
        }
        esService.close();
        A_User a_user = JSON.parseObject(json,A_User.class);
        return a_user;
    }
    
}
