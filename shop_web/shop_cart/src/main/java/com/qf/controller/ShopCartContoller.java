package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.qf.aop.IsLogin;
import com.qf.entity.Shopcart;
import com.qf.entity.User;
import com.qf.service.ICartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/cart")
public class ShopCartContoller {

    @Reference
    private ICartService cartService;

    /**
     * 自定义注解 + AOP
     *
     * 添加购物车
     * @param shopcart
     * @return
     */
    @RequestMapping("/add")
    @IsLogin
    public String addCart(
            @CookieValue(name = "cart_token",required = false) String cartToken,
            Shopcart shopcart,
            User user,
            HttpServletResponse response) {
//            System.out.println("测试自定义注解是否成功");

        if (cartToken == null) {
            cartToken = UUID.randomUUID().toString();
            //回写cookie
            Cookie cookie = new Cookie("cart_token", cartToken);
            cookie.setMaxAge(60 * 60 * 24 * 365);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        cartService.addCart(cartToken, shopcart, user);
        return "addsucc";
    }

    /**
     * 获取购物车信息
     * @param cartToken
     * @param user
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    @IsLogin
    public String cartSizeList(@CookieValue(name = "cart_token", required = false) String cartToken, User user) {
        System.out.println("cart_token:" + cartToken);
        List<Shopcart> shopcarts = cartService.queryCartsByUid(cartToken, user);
//        for (Shopcart shopcart : shopcarts) {
//            System.out.println("liebiao:" + shopcart);
//        }
        return "showcarts(" + JSON.toJSONString(shopcarts) + ")";
    }

    /**
     * 跳转到购物车列表
     * @param cartToken
     * @param user
     * @param map
     * @return
     */
    @RequestMapping("/cartlist")
    @IsLogin
    public String toCartList(
            @CookieValue(name = "cart_token",required = false) String cartToken ,
            User user,
            ModelMap map) {
        List<Shopcart> shopcarts = cartService.queryCartsByUid(cartToken, user);
        map.put("carts", shopcarts);
        //计算总价
        BigDecimal priceAll = BigDecimal.valueOf(0.00);
        for (Shopcart shopcart : shopcarts) {
            priceAll = priceAll.add(shopcart.getAllprice());
        }
        map.put("priceall", priceAll);
        return "cartlist";
    }

}
