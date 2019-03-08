package snc.server.ide.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import snc.boot.util.es.ElasticsearchService;
import snc.server.ide.pojo.A_User;
import snc.server.ide.service.UinfoService;
import snc.server.ide.tttt.pojo.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class E {
    public A_User esIsEmpty(String id){
        ElasticsearchService esService=new ElasticsearchService("elasticsearch","127.0.0.1",9300);
        Client client=esService.getClient();
        IdsQueryBuilder queryBuilder = QueryBuilders.idsQuery();
        queryBuilder.addIds(id);
        //index = IUser   type = IUserinfo
//        SearchResponse response = client.prepareSearch("IUser").setTypes("IUserinfo").setQuery(queryBuilder).get();
        SearchResponse response = client.prepareSearch("user").setTypes("userinfo").setQuery(queryBuilder).get();
        //查询所获取的数据都在hits里面，是以数据的形式，每一个hits[？]里面都有很多其他数据
        SearchHit[] hits = response.getHits().getHits();
        String json="";
        for (SearchHit hit : hits) {
            json = hit.getSourceAsString();
        }
//        System.out.println(json);

        A_User a_user = JSON.parseObject(json,A_User.class);
//        System.out.println(a_user.toString());
        return a_user;
    }




    public static void main(String[] args) {

    }
}
