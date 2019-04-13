package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.qf.dao.GoodsMapper;
import com.qf.entity.Goods;
import com.qf.servie.IGoodsService;
import com.qf.servie.ISearchService;
import com.qf.shop_service_goods.RabbitMQConfiguration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class GoodsServiceImpl implements IGoodsService{

    @Autowired
    private GoodsMapper goodsMapper;
    @Reference
    private ISearchService searchService;
    @Autowired
    private RabbitTemplate template;


    @Override
    public List<Goods> queryAll() {
        return goodsMapper.selectList(null);
    }

    @Override
    public int insert(Goods goods) {
        //添加商品到数据库
        int result = goodsMapper.insert(goods);

        //添加商品到solr索引库
//        searchService.insertGoods(goods);
        //将添加商品的信息， 放入rabbitmq中
        template.convertAndSend(RabbitMQConfiguration.FANOUT_NAME, "",goods);

        return result;
    }

    @Override
    public int deleteById(int id) {
        return goodsMapper.deleteById(id);
    }

    @Override
    public Goods queryById(int id) {
        return goodsMapper.selectById(id);
    }

}
