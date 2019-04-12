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
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
        List<Goods> list = new ArrayList<>();
        try {
            QueryResponse result = solrClient.query(query);
            SolrDocumentList results = result.getResults();
            //将搜索结果集  --> list<Goods>
            for (SolrDocument document : results) {
                Goods goods = new Goods();
                goods.setId(Integer.parseInt(document.get("id").toString()));
                goods.setGname(document.get("gname").toString());
                goods.setGprice(BigDecimal.valueOf(Double.parseDouble(document.get("gprice").toString())));
                goods.setGimage(document.get("gimage").toString());
                goods.setGsave(Integer.parseInt(document.get("gsave").toString()));
                list.add(goods);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int insertGoods(Goods goods) {
        return 0;
    }


}
