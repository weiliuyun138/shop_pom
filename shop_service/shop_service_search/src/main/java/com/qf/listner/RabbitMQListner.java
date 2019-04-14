package com.qf.listner;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.servie.ISearchService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RabbitMQListner {

    @Autowired
    private SolrClient solrClient;

    @Reference
    private ISearchService searchService;

    @RabbitListener(queues = "goods_queue1")
    public void handleMsg(Goods goods) {
        //接受到MQ的消息
        System.out.println("搜索服务,接受到消息: "+goods);

        //同步索引库
        searchService.insertGoods(goods);
//        //创建document对象
//        SolrInputDocument solrDocument = new SolrInputDocument();
//        solrDocument.addField("id", goods.getId());
//        solrDocument.addField("gname", goods.getGname());
//        solrDocument.addField("gimage", goods.getGimage());
//        solrDocument.addField("ginfo", goods.getGinfo());
//        solrDocument.addField("gprice", goods.getGprice().toString());
//        solrDocument.addField("gsave", goods.getGsave());
//        try {
//            solrClient.add(solrDocument);
//            solrClient.commit();
//        } catch (SolrServerException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
