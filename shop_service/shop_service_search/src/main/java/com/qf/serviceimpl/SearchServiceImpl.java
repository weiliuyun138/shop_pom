package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.entity.Goods;
import com.qf.servie.ISearchService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements ISearchService {

    @Autowired
    private SolrClient solrClient;

    @Override
    public List<Goods> searchGoods(String keyword) {
        System.out.println("搜索服务获得关键字: "+keyword);
        SolrQuery query = new SolrQuery();
        if (keyword == null) {
            //搜索全部
            query.setQuery("*,*");
        } else {
            //搜索具体的关键字
            query.setQuery("gname:" + keyword + " || ginfo:" + keyword);
        }
        //开启高亮
        query.setHighlight(true);
        query.setHighlightSimplePre("<font color='red'>" );
        query.setHighlightSimplePost("</font>");
        //添加需要高亮的字段
        query.addHighlightField("gname");

        List<Goods> list = new ArrayList<>();
        try {
            QueryResponse result = solrClient.query(query);

            //获得高亮结果集
            //Map<id,Map<feild,List<String>>>
            //id:有高亮的商品id , field: 商品有高亮的字段
            Map<String, Map<String, List<String>>> highlighting = result.getHighlighting();
            SolrDocumentList results = result.getResults();

            //将搜索结果集  --> list<Goods>
            for (SolrDocument document : results) {

                Goods goods = new Goods();
                goods.setId(Integer.parseInt(document.get("id").toString()));
                goods.setGname(document.get("gname").toString());
                goods.setGprice(BigDecimal.valueOf(Double.parseDouble(document.get("gprice").toString())));
                goods.setGimage(document.get("gimage").toString());
                goods.setGsave(Integer.parseInt(document.get("gsave").toString()));

                //判断当前商品是否有高亮

                //有高亮的内容
                Map<String, List<String>> stringListMap = highlighting.get(goods.getId() + "");
                //获得其中的高亮内容
                if (stringListMap.get("gname") != null) {
                    String gname = stringListMap.get("gname").get(0);
                    //将高亮的内容替换到对象中
                    goods.setGname(gname);
                }
                list.add(goods);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int insertGoods(Goods goods) {
        //创建document对象
        SolrInputDocument solrDocument = new SolrInputDocument();
        solrDocument.addField("id", goods.getId());
        solrDocument.addField("gname", goods.getGname());
        solrDocument.addField("gimage", goods.getGimage());
        solrDocument.addField("ginfo", goods.getGinfo());
        solrDocument.addField("gprice", goods.getGprice().toString());
        solrDocument.addField("gsave", goods.getGsave());
        try {
            solrClient.add(solrDocument);
            solrClient.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }


}
