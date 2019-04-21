package com.qf.servie;

import com.qf.entity.Orders;
import com.qf.entity.User;

import java.util.List;

public interface IOrdersService {

    String insertOrders(int aid, User user);

    List<Orders> queryOdersByUid(int uid);

    Orders queryById(int oid);

    Orders queryByOrderId(String orderid);

    int updateOrders(Orders order);


}
