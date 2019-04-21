package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.CartMapper;
import com.qf.dao.GoodsMapper;
import com.qf.entity.Goods;
import com.qf.entity.Shopcart;
import com.qf.entity.User;
import com.qf.servie.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public int addCart(String cartToken, Shopcart shopcart, User user) {

        //处理购商品的小计
        Goods goods = goodsMapper.selectById(shopcart.getGid());
        BigDecimal bigDecimal = BigDecimal.valueOf(shopcart.getGnumber());
        //计算当前商品的小计
        shopcart.setAllprice(goods.getGprice().multiply(bigDecimal));
        if (user != null) {
            //已经登录
            shopcart.setUid(user.getId());
            cartMapper.insert(shopcart);
        } else {
            //未登录 -- redis
            redisTemplate.opsForList().leftPush(cartToken, shopcart);
        }

        return 1;
    }

    @Override
    public List<Shopcart> queryCartsByUid(String cartToken, User user) {
        List<Shopcart> shopcarts = null;
        if (user != null) {
            //去数据库查询购物车信息
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("uid", user.getId());
            shopcarts = cartMapper.selectList(queryWrapper);
        } else if (cartToken != null) {
            //去redis查询购物车信息
            //查询链表的长度
            Long size = redisTemplate.opsForList().size(cartToken);
            //获取所有链表的数据
            shopcarts = redisTemplate.opsForList().range(cartToken, 0, size);
        }
        for (int i = 0; i < shopcarts.size(); i++) {
            //获取商品详情信息
            Goods goods = goodsMapper.selectById(shopcarts.get(i).getGid());
            shopcarts.get(i).setGoods(goods);
        }
        return shopcarts;
    }

    @Override
    public int mergeCarts(String cartToken, User user) {
        if (cartToken != null && cartToken != "") {
            //获得临时购物车的信息
            Long size = redisTemplate.opsForList().size(cartToken);
            List<Shopcart> list = redisTemplate.opsForList().range(cartToken, 0, size);
            if (list==null)
                return 1;
            for (Shopcart shopcart : list) {
                shopcart.setUid(user.getId());
                cartMapper.insert(shopcart);
            }
            //清空临时购物车
            Boolean result = redisTemplate.delete(cartToken);
        }
        return 1;
    }

    @Override
    public int deleteCartsByUid(int uid) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("uid", uid);
        return cartMapper.delete(wrapper);
    }
}
