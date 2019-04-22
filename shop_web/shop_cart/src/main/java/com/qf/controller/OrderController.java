package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.aop.IsLogin;
import com.qf.entity.Address;
import com.qf.entity.Orders;
import com.qf.entity.Shopcart;
import com.qf.entity.User;
import com.qf.service.IAddressService;
import com.qf.service.ICartService;
import com.qf.service.IOrdersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Reference
    private ICartService cartService;

    @Reference
    private IAddressService addressService;

    @Reference
    private IOrdersService ordersService;

    /**
     * 强行登陆
     * 跳转到订单的编译页面（默认下单整个购物车）
     * @param user
     * @return
     */
    @RequestMapping("/edit")
    @IsLogin(mustLogin = true)
    public String editOrder(User user, ModelMap map) {

        //当前用户的整个购物车信息
        List<Shopcart> shopcarts = cartService.queryCartsByUid(null, user);
        map.put("carts", shopcarts);

        //当前用户关联的收获地址信息
        List<Address> addresses = addressService.queryByUid(user.getId());
        map.put("addresses", addresses);

        //计算总价
        BigDecimal priceall = BigDecimal.valueOf(0.00);
        for (Shopcart shopcart : shopcarts) {
            priceall = priceall.add(shopcart.getAllprice());
        }
        map.put("priceall", priceall.doubleValue());

        return "orderedit";
    }


    /**
     * 下单
     * @param aid
     * @param user
     * @return
     */
    @RequestMapping("/insertOrders")
    @IsLogin(mustLogin = true)
    @ResponseBody
    public String insertOrders(int aid, User user) {
        String orderid = ordersService.insertOrders(aid, user);
        return orderid;
    }


    @RequestMapping("/orderlist")
    @IsLogin(mustLogin = true)
    public String orderList(User user, Model model) {
        List<Orders> orders = ordersService.queryOdersByUid(user.getId());
        System.out.println(orders);
        model.addAttribute("orders", orders);
        return "orderlist";
    }


}
