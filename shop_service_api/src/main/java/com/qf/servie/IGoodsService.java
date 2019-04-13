package com.qf.servie;

import com.qf.entity.Goods;

import java.util.List;

public interface IGoodsService {
    List<Goods> queryAll();

    int insert(Goods goods);

    int deleteById(int id);

    Goods queryById(int id);
}
