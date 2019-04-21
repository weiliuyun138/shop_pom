package com.qf.servie;

import com.qf.entity.Shopcart;
import com.qf.entity.User;

import java.util.List;

public interface ICartService {

    //添加购物车
    int addCart(String cartToken, Shopcart shopcart, User user);

    //查询购物车
    List<Shopcart> queryCartsByUid(String cartToken,User user);

    //合并购物车
    int mergeCarts(String cartToken, User user);

    //清空购物车
    int deleteCartsByUid(int uid);
}
