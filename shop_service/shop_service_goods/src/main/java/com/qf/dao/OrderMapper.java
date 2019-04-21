package com.qf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qf.entity.Orders;

import java.util.List;

public interface OrderMapper extends BaseMapper<Orders> {

    List<Orders> queryOrdersByUid(int uid);

}
