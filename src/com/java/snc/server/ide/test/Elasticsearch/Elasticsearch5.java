package snc.server.ide.test.Elasticsearch;



import com.google.gson.JsonArray;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import redis.clients.jedis.Jedis;
import snc.boot.util.RedisUtils;
import snc.server.ide.dao.HoutaiCommodityDao;
import snc.server.ide.pojo.Class;

import snc.server.ide.pojo.HoutaiCommodity;
import snc.server.ide.pojo.Gift;
import snc.server.ide.service.GiftService;
import snc.server.ide.service.HoutaiCommodityService;
import snc.server.ide.service.impl.HoutaiCommodityImpl;
import snc.server.ide.tttt.pojo.User;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Controller
public class Elasticsearch5 {
    public static TransportClient client = null;
    public final static String HOST = "127.0.0.1";
    public final static int PORT = 9300;
    private static InetSocketTransportAddress node =null;
    private static Jedis jedis;

    private HoutaiCommodityService houtaiCommodityService;
    static  {
        {

            try {
                jedis = RedisUtils.getJedis();
                System.setProperty("es.set.netty.runtime.available.processors", "false");
                node = new InetSocketTransportAddress(InetAddress.getByName(HOST), PORT);
                Settings settings = Settings.builder()
                        .put("cluster.name", "elasticsearch")
                        .build();
                client = new PreBuiltTransportClient(settings);
                client.addTransportAddress(node);
                System.out.println("创建成功");
            } catch (UnknownHostException e) {
                System.out.println("创建数据库中出现错误");
                e.printStackTrace();
            }
        }
    }

    public Elasticsearch5(HoutaiCommodityService houtaiCommodityService) {
        this.houtaiCommodityService=houtaiCommodityService;
    }

    //  创建连接
    @Before
    public void getClient() {
        {

            try {
//                jedis = RedisUtils.getJedis();
                node = new InetSocketTransportAddress(InetAddress.getByName(HOST), PORT);
                Settings settings = Settings.builder()
                        .put("cluster.name", "elasticsearch")
                        .build();
                client = new PreBuiltTransportClient(settings);
                client.addTransportAddress(node);
                System.out.println("创建成功");
            } catch (UnknownHostException e) {
                System.out.println("创建数据库中出现错误");
                e.printStackTrace();
            }
        }
    }
     //创建用户
    public  void createUser(User user){
        XContentBuilder jsonBuild = null;
        try {
            jsonBuild = jsonBuilder();
            //每一个filed里面存的就是列名和列值
            jsonBuild.startObject().field("N", user.getN()).field("B", user.getB()).field("E",user.getE())
                    .field("BPH",user.getBPH()).field("FC",user.getFC()).field("ADD", user.getADD())
                    .field("CM", user.getCM()).field("GM", user.getGM()).field("BC", user.getBC())
                    .field("PWD", "asda").field("HD", "asasd").endObject();

        } catch (IOException e) {
            e.printStackTrace();
        }
            IndexResponse response = client.prepareIndex("user", "Userinfo",user.getUid()).setSource(jsonBuild).get();
        client.close();
    }

    //创建礼物
    public  void createGift(Gift gift){
//        Gift gift = new Gift();
//        gift.setG_id("as");
//        gift.setG_stock("ASd");
//        gift.setG_col("asd");
//        gift.setG_stock("as");
//        gift.setG_detaile("AS");
//        gift.setG_image("/home");
        XContentBuilder jsonBuild = null;
        try {
            jsonBuild = jsonBuilder();
            //每一个filed里面存的就是列名和列值
            jsonBuild.startObject().field("g_col", gift.getG_col()).field("mod",gift.getMod())
                    .field("g_price",gift.getG_price()).field("g_size",gift.getG_size()).field("g_stock", gift.getG_stock())
                    .field("g_detaile", gift.getG_detaile()).field("g_sold", gift.getG_sold()).field("g_image", gift.getG_sold()).endObject();

        } catch (IOException e) {
            e.printStackTrace();
        }
        IndexResponse response = client.prepareIndex("gift", "gift",gift.getG_id()).setSource(jsonBuild).get();
        client.close();
    }
    @Test
    public   void createUser1(){

        XContentBuilder jsonBuild = null;
        try {
            jsonBuild = jsonBuilder();
            //每一个filed里面存的就是列名和列值
            jsonBuild.startObject().field("N", "asd").field("B", "asd").field("E","asda")
                    .field("BPH","asd").field("FC","asd").field("ADD", "asda")
                    .field("CM", "Asdasd").field("GM", "asdas").field("BC", "asdas")
                    .field("PWD", "asda").field("HD", "asasd").endObject();


        } catch (IOException e) {
            e.printStackTrace();
        }

            IndexResponse response = client.prepareIndex("user", "Userinfo","xx").setSource(jsonBuild).get();

        client.close();
    }
    @Test
    public void createType(){     //给商品类型建表
        XContentBuilder jsonBuild = null;
        try {
            jsonBuild = jsonBuilder();
            //每一个filed里面存的就是列名和列值
            jsonBuild.startObject().field("c_brand","金士顿").endObject();

        } catch (IOException e) {
            e.printStackTrace();
        }
        IndexResponse response = client.prepareIndex("commodity_type", "u盘").setSource(jsonBuild).get();
        client.close();
    }
    @Test
    //创建商品
    public  void createCommodity(){
        HoutaiCommodity commodity = new HoutaiCommodity();
        commodity.setC_name("爱国者（aigo）32GB Type-C USB3.0 手机U盘 U350 黑色 双接口手机电脑用");
        commodity.setC_price(718);
        commodity.setMarketprice(80);
        commodity.setC_brand("闪迪");
        commodity.setC_stock(10);
        commodity.setC_col("黑色");
        commodity.setC_details("双接口手机电脑用！");
        commodity.setC_image("/home/mmz");
        commodity.setC_mod("asd");
        commodity.setC_sid("29");
        commodity.setC_size("64G");
        commodity.setC_type(1);
        commodity.setC_spuid("22");
        commodity.setC_sold(510);

        HoutaiCommodity commodity2 = new HoutaiCommodity();
        commodity2.setC_name("爱国者（aigo）64GB Type-C USB3.0 手机U盘 U350 银色 双接口手机电脑用");
        commodity2.setC_price(80);
        commodity2.setMarketprice(80);
        commodity2.setC_brand("闪迪");
        commodity2.setC_stock(10);
        commodity2.setC_col("银色");
        commodity2.setC_details("双接口手机电脑用！");
        commodity2.setC_image("/home/mmz");
        commodity2.setC_mod("asd");
        commodity2.setC_size("64G");
        commodity2.setC_type(1);
        commodity2.setC_sold(510);
        commodity2.setC_spuid("22");//判断是否是同一个商品
        commodity2.setC_sid("30");



        HoutaiCommodity commodity3 = new HoutaiCommodity();
        commodity3.setC_name("爱国者（aigo）32GB Type-C USB3.0 手机U盘 U350 白色 双接口手机电脑用");
        commodity3.setC_price(718);
        commodity3.setMarketprice(80);
        commodity3.setC_brand("闪迪");
        commodity3.setC_stock(10);
        commodity3.setC_col("白色");
        commodity3.setC_details("双接口手机电脑用！");
        commodity3.setC_image("/home/mmz");
        commodity3.setC_mod("asd");
        commodity3.setC_sid("31");
        commodity3.setC_size("64G");
        commodity3.setC_type(1);
        commodity3.setC_spuid("22");
        commodity3.setC_sold(510);

        List<HoutaiCommodity> list=new ArrayList();
        list.add(commodity);
        list.add(commodity2);
        list.add(commodity3);


        XContentBuilder jsonBuild = null;

//        HoutaiCommodityImpl houtaiCommodityDao=new HoutaiCommodityImpl();   //调用数据库
//        jsonArray.add(commodity2);
        for (int i=0;i<list.size();i++){    //遍历 从而将同一商品id存入redis的list中
            HoutaiCommodity commodity1=(HoutaiCommodity)list.get(i);
            houtaiCommodityService.insertCommodity(commodity1);

            try {
                jsonBuild = jsonBuilder();
                //每一个filed里面存的就是列名和列值
                jsonBuild.startObject().field("c_name",commodity.getC_name()).field("c_spuid",commodity.getC_spuid()).field("c_price", commodity.getC_price()).field("marketprice",commodity.getMarketprice() ).field("c_size",commodity.getC_size())
                        .field("c_brand",commodity.getC_brand()).field("c_stock",commodity.getC_stock())
                        .field("c_col", commodity.getC_col()).field("c_mod", commodity.getC_mod()).field("c_image", commodity.getC_image())
                        .field("c_sold", commodity.getC_sold()).field("c_details", commodity.getC_details()).field("c_type", commodity.getC_type()).endObject();

            } catch (IOException e) {
                e.printStackTrace();
            }
            IndexResponse response = client.prepareIndex("commodity", "commodity",commodity.getC_sid()).setSource(jsonBuild).get();


            for (HoutaiCommodity h:list) {
                System.out.println(commodity1.getC_sid()+"  zzzzzzz");
                System.out.println(h.getC_sid());
                jedis.lpush(commodity1.getC_sid(),h.getC_sid());

            }

        }

        client.close();
    }
    //创建课程
    public  void createCart(Class course){
        XContentBuilder jsonBuild = null;
        try {
            jsonBuild = jsonBuilder();
            //每一个filed里面存的就是列名和列值
            jsonBuild.startObject().field("cls_price", course.getCls_pt()).field("cls_json",course.getCls_json())
                    .endObject();

        } catch (IOException e) {
            e.printStackTrace();
        }
        IndexResponse response = client.prepareIndex("class", "class",course.getCls_id()).setSource(jsonBuild).get();
        client.close();
    }

    //分页,排序
//    @Test
    public List CommodityFirst(String page)
    {
        String index="commodity";
        String type="commodity";
        String pt="20";
        SearchResponse searchResponse = client.prepareSearch(index)
                .setTypes(type)
                .setQuery(QueryBuilders.matchAllQuery())
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .setFrom((Integer.parseInt(page)-1)*10).setSize(10)//分页,设置分页的大小和展示第几页
//                .addSort("c_price", SortOrder.DESC)//排序
                .get();
        SearchHits hits = searchResponse.getHits();
        long total = hits.getTotalHits();
        System.out.println(total);
        SearchHit[] searchHits = hits.hits();
        List list=new ArrayList();

        for(SearchHit s : searchHits)
        {
            String json = s.getSourceAsString();
            list.add(json);
            System.out.println(s.getSourceAsString());

        }
        return list;
    }


    @Test
    public void testShop()
    {
        String index="commodity";
        String type="commodity";
        String pt="20";
        SearchResponse searchResponse = client.prepareSearch(index)
                .setTypes(type)
                .setQuery(QueryBuilders.matchAllQuery())
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .setFrom(1).setSize(1)//分页
                .addSort("c_price", SortOrder.DESC)//排序
                .get();

        SearchHits hits = searchResponse.getHits();
        long total = hits.getTotalHits();
        System.out.println(total);
        SearchHit[] searchHits = hits.hits();
        for(SearchHit s : searchHits)
        {
            System.out.println(s.getSourceAsString());
            String []logindex=s.getSourceAsString().split(",");

        }
    }
    //商品价格降序查询
    @Test
    public void testjShop()
    {
        String index="commodity";
        String type="commodity";

        SearchResponse searchResponse = client.prepareSearch(index)
                .setTypes(type)
//                .setQuery(QueryBuilders.matchAllQuery()) //查询所有
                .setSearchType(SearchType.QUERY_THEN_FETCH)
//                .setFrom(1).setG_size(1)//分页
                .addSort("c_price", SortOrder.DESC)//排序
                .get();

        SearchHits hits = searchResponse.getHits();
        long total = hits.getTotalHits();
        System.out.println("查询总数据： "+total);
        SearchHit[] searchHits = hits.hits();
        for(SearchHit s : searchHits)
        {
            System.out.println(s.getSourceAsString());
        }
    }


    @Test
    public void searchMutil()throws Exception {
        SearchRequestBuilder srb = client.prepareSearch("commodity").setTypes("commodity");
        QueryBuilder queryBuilder = QueryBuilders.matchPhraseQuery("c_type", "电脑");
        QueryBuilder queryBuilder2 = QueryBuilders.matchPhraseQuery("c_brand", "三星");
        SearchResponse sr = srb.setQuery(QueryBuilders.boolQuery()
                .must(queryBuilder)
                .must(queryBuilder2))
                .execute()
                .actionGet();
        SearchHits hits = sr.getHits();
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
    }

    //multiMatch查询指定多个字段
    @Test
    public void test11() throws Exception {

        MultiMatchQueryBuilder builder = QueryBuilders.multiMatchQuery("Kingston 64", "c_name","c_type", "c_details");
        SearchResponse response = client.prepareSearch("commodity").setTypes("commodity")
                .setQuery(builder)
                .setSize(3)
                .get();

        SearchHits hits = response.getHits();

        for (SearchHit hit : hits){
            System.out.println("==========================================="+hit.getSourceAsString());

            Map<String, Object> map = hit.getSourceAsMap();

            for (String key : map.keySet()){
                System.out.println(key +" = " +map.get(key));
            }
        }
    }

    //删除数据
    @Test
    public void testDelete() {
        DeleteResponse response = client.prepareDelete("commodity", "commodity","4")
                .get();
        jedis.del("4");
        String index = response.getIndex();
        String type = response.getType();
        String id = response.getId();
        long version = response.getVersion();
        System.out.println(index + " : " + type + ": " + id + ": " + version);
    }
    @Test
    public  void testupsert() throws IOException, ExecutionException, InterruptedException {
        IndexRequest indexRequest = new IndexRequest("commodity", "commodity", "5")
                .source(jsonBuilder()
                        .startObject()
                        .field("c_sold", 34)
                        .endObject());
        UpdateRequest updateRequest = new UpdateRequest("commodity", "commodity", "5")
                .doc(jsonBuilder()
                        .startObject()
                        .field("c_sold", 100)
                        .endObject())
                .upsert(indexRequest); //如果不存在此文档 ，就增加 `indexRequest`
        client.update(updateRequest).get();
    }



    public static void main(String[] args) {

//        createUser();
//        Elasticsearch5 elasticsearch5 = new Elasticsearch5();

//        elasticsearch5.createCart(c);
    }

}
