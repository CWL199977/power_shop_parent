package com.powershop.service;

import com.powershop.feign.ItemServiceFeign;
import com.powershop.pojo.SearchItem;
import com.powershop.utils.JsonUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ItemServiceFeign itemServiceFeign;
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Value("${ES_INDEX_NAME}")
    private String ES_INDEX_NAME;

    @Value("${ES_TYPE_NAME}")
    private String ES_TYPE_NAME;

    @Override
    public void importAll() throws IOException {
        if(!indexIsExists()) {
            createIndex();
        }
        int page=1;
        while(true) {
            //1、查询商品信息-->List<SearchItem>
            List<SearchItem> searchItemList = itemServiceFeign.selectSearchItem(page, 1000);
            if(searchItemList==null || searchItemList.size()==0){
                break;
            }
            BulkRequest bulkRequest = new BulkRequest();
            for (SearchItem searchItem : searchItemList) {
                //2、把商品信息导入到es

                IndexRequest indexRequest = new IndexRequest(ES_INDEX_NAME,ES_TYPE_NAME);
                indexRequest.source(JsonUtils.objectToJson(searchItem), XContentType.JSON);
                bulkRequest.add(indexRequest);
            }
            restHighLevelClient.bulk(bulkRequest);
            page++;
        }
    }

    @Override
    public List<SearchItem> list(String q, Long page, Integer pageSize) {
        try {
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(ES_INDEX_NAME);
            searchRequest.types(ES_TYPE_NAME);

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            //1、按名称、卖点、描述、类别搜索商品：multi_match
            searchSourceBuilder.query(QueryBuilders.multiMatchQuery(q, new String[]{"item_title", "item_sell_point", "item_category_name", "item_desc"}));
            //2、分页：from、size
            /**
             *  page   pageSize    from
             *   1         20        0
             *   2         20        20
             *   3         20        40
             *
             *  from=(page-1)*pageSize
             */
            Long form = (page - 1) * pageSize;
            searchSourceBuilder.from(form.intValue());
            searchSourceBuilder.size(pageSize);

            //3、高亮：highlight
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("item_title");
            highlightBuilder.preTags("<font color='red'>");
            highlightBuilder.postTags("</font>");

            searchSourceBuilder.highlighter(highlightBuilder);

            searchRequest.source(searchSourceBuilder);
            SearchResponse response = restHighLevelClient.search(searchRequest);
            SearchHit[] hits = response.getHits().getHits();
            List<SearchItem> searchItemList = new ArrayList<>();
            for (SearchHit hit : hits) {
                SearchItem searchItem = JsonUtils.jsonToPojo(hit.getSourceAsString(), SearchItem.class);
                Map<String, HighlightField> map = hit.getHighlightFields();
                if(map!=null && map.size()>0){
                    searchItem.setItem_title(map.get("item_title").getFragments()[0].toString());
                }
                searchItemList.add(searchItem);
            }
            return searchItemList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void addDoc(Long itemId) {
      SearchItem searchItem= itemServiceFeign.getSearchItem(itemId);
    }

    private void createIndex() throws IOException {
        IndicesClient indicesClient = restHighLevelClient.indices();

        CreateIndexRequest createIndexRequest = new CreateIndexRequest(ES_INDEX_NAME);
        createIndexRequest.source("{\n" +
                "  \"settings\": {\n" +
                "    \"number_of_shards\": 2,\n" +
                "    \"number_of_replicas\": 1\n" +
                "  }\n" +
                "}",XContentType.JSON);
        createIndexRequest.mapping(ES_TYPE_NAME,"{\n" +
                "  \"_source\": {\n" +
                "    \"excludes\": [\n" +
                "      \"item_desc\"\n" +
                "    ]\n" +
                "  },\n" +
                "  \"properties\": {\n" +
                "    \"id\":{\n" +
                "      \"type\": \"keyword\"\n" +
                "    },\n" +
                "    \"item_title\": {\n" +
                "      \"type\": \"text\",\n" +
                "      \"analyzer\": \"ik_max_word\",\n" +
                "      \"search_analyzer\": \"ik_smart\"\n" +
                "    },\n" +
                "    \"item_sell_point\": {\n" +
                "      \"type\": \"text\",\n" +
                "      \"analyzer\": \"ik_max_word\",\n" +
                "      \"search_analyzer\": \"ik_smart\"\n" +
                "    },\n" +
                "    \"item_price\": {\n" +
                "      \"type\": \"float\"\n" +
                "    },\n" +
                "    \"item_image\": {\n" +
                "      \"type\": \"text\",\n" +
                "      \"index\": false\n" +
                "    },\n" +
                "    \"item_category_name\": {\n" +
                "      \"type\": \"text\",\n" +
                "      \"analyzer\": \"ik_max_word\",\n" +
                "      \"search_analyzer\": \"ik_smart\"\n" +
                "    },\n" +
                "    \"item_desc\": {\n" +
                "      \"type\": \"text\",\n" +
                "      \"analyzer\": \"ik_max_word\",\n" +
                "      \"search_analyzer\": \"ik_smart\"\n" +
                "    }\n" +
                "  }\n" +
                "}",XContentType.JSON);
        indicesClient.create(createIndexRequest);
    }

    public boolean indexIsExists(){
        OpenIndexRequest openIndexRequest = new OpenIndexRequest(ES_INDEX_NAME);
        try {
            restHighLevelClient.indices().open(openIndexRequest);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}