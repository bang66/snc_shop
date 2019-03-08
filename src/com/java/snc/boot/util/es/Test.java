package snc.boot.util.es;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class Test {
    @org.junit.Test
    public void stest() {
        try {
            ElasticsearchService service = new ElasticsearchService("elasticsearch", "127.0.0.1", 9300);
            ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
            List<String> lists = new ArrayList<>();
            lists.add("c_brand");
            lists.add("c_type");
            constructor.mutilsearch(new ESQueryBuilders().multiMatch("三",lists));
            constructor.setSize(15);  //查询返回条数，最大 10000
            constructor.setAsc("c_price");
            List<Map<String, Object>> list = service.search("commodity", "commodity", constructor);
            System.out.println(list.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        ElasticsearchService service = new ElasticsearchService("elasticsearch", "127.0.0.1", 9300);
//        MultiMatchQueryBuilder builder = QueryBuilders.multiMatchQuery("三星", "c_type", "c_details","c_brand");
//        SearchResponse response = service.getClient().prepareSearch("commodity").setTypes("commodity")
//                .setQuery(builder)
//                .setSize(3)
//                .get();

//        MultiSearchRequest request = service.getClient().multiSearch()

//        SearchHits hits = response.getHits();
//
//        for (SearchHit hit : hits){
//            System.out.println(hit.getSourceAsString());
//
//            Map<String, Object> map = hit.getSourceAsMap();
//
//            for (String key : map.keySet()){
//                System.out.println(key +" = " +map.get(key));
//            }
//        }
    }
}
