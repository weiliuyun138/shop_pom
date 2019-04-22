package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.AddressMapper;
import com.qf.dao.OrderDetailsMapper;
import com.qf.dao.OrderMapper;
import com.qf.entity.*;
import com.qf.service.ICartService;
import com.qf.service.IOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements IOrdersService {

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private ICartService cartService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailsMapper orderDetailsMapper;

    @Override
    @Transactional
    public String insertOrders(int aid, User user) {
        //根据收货地址id查询收货地址信息
        Address address = addressMapper.selectById(aid);
        //根据用户id查询当前用户的所有购物车数据
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid", user.getId());
        List<Shopcart> shopcarts = cartService.queryCartsByUid(null, user);
        //计算总价
        BigDecimal priceall = BigDecimal.valueOf(0.00);
        for (Shopcart shopcart : shopcarts) {
            priceall = priceall.add(shopcart.getAllprice());
        }
        //通过购物车信息封装订单和订单详情对象
        Orders orders = new Orders(
                0,
                UUID.randomUUID().toString(),
                address.getPerson(),
                address.getAddress(),
                address.getPhone(),
                address.getCode(),
                priceall,
                new Date(),
                0,
                user.getId(),
                null
        );

        //订单存入数据库
        orderMapper.insert(orders);

        //订单详情
        for (Shopcart shopcart : shopcarts) {
            OrderDetails orderDetails = new OrderDetails(
                    0,
                    shopcart.getGid(),
                    shopcart.getGoods().getGimage(),
                    shopcart.getGoods().getGname(),
                    shopcart.getGoods().getGprice(),
                    shopcart.getGnumber(),
                    orders.getId()
            );
            //添加订单详情到数据库
            orderDetailsMapper.insert(orderDetails);
        }

        //TODO 清空购物车信息

        //清空购物车信息
        cartService.deleteCartsByUid(user.getId());

        return orders.getOrderid();
    }


    @Override
    public List<Orders> queryOdersByUid(int uid) {
        return orderMapper.queryOrdersByUid(uid);
    }

    @Override
    public Orders queryById(int oid) {
        return orderMapper.selectById(oid);
    }

    @Override
    public Orders queryByOrderId(String orderid) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("orderid", orderid);
        return orderMapper.selectOne(wrapper);
    }

    @Override
    public int updateOrders(Orders order) {
        return orderMapper.updateById(order);
    }

}
