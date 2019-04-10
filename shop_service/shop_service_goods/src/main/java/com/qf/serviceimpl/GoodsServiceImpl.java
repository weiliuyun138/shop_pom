package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.dao.GoodsMapper;
import com.qf.entity.Goods;
import com.qf.servie.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class GoodsServiceImpl implements IGoodsService{

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public List<Goods> queryAll() {
        return goodsMapper.selectList(null);
    }

    @Override
    public int insert(Goods goods) {
        return goodsMapper.insert(goods);
    }

    @Override
    public int deleteById(int id) {
        return goodsMapper.deleteById(id);
    }

    @Override
    public Goods getGoodsById(int id) {
        return goodsMapper.selectById(id);
    }
}
