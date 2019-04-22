package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.qf.entity.Orders;
import com.qf.service.IOrdersService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.alipay.api.AlipayConstants.SIGN_TYPE_RSA2;

@Controller
@RequestMapping("/pay")
public class PayController {

    @Reference
    private IOrdersService ordersService;

    /**
     * 调用阿里支付
     * @param orderid
     */
    @RequestMapping("/alipay")
    @ResponseBody
    public void pay(String orderid , HttpServletResponse response) throws IOException {
        //根据订单号查询订单
        Orders orders = ordersService.queryByOrderId(orderid);

        //调用支付宝进行支付
        //获得初始化的AlipayClient
        //获得初始化的AlipayClient - 后续的支付请求、退款请求.... 都是基于这个客户端对象的，所以这个对象应该在项目中唯一保存
        AlipayClient alipayClient = new DefaultAlipayClient(
                "https://openapi.alipaydev.com/gateway.do", //支付宝网关,固定https://openapi.alipay.com/gateway.do
                "2016093000631764",//APP_ID
                "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCDjn49a99GkIv4W4znvWfENP/NXfkVkcTzDQAq2NcLDK9AjMKC4BcEC1bukIdjonxbQ1Db6tDym3+zx14nKme1dlETbUDz6rA1ezYaxNrJdCEqFYoPiUkaI6OFinM+jb8juk8zmR1Az7tXLxzIzEXJhEOU/bauUJ1+Fareft8jULdiuoekwtPj7Rf1lRPG325kDTTyNPySL4TptxUltwBfWlEzS/Lfw8Wn0CfDdwkdy3oDtRRWM5klxKIcvDWHBeepHTTbYY1aeubqulhF2bFs6j/3uATf9uUJT2gklzjEeaponLAkjP3VKYOhFh6ub6Jt7ClxNTofVnlw1tx3BZKHAgMBAAECggEARjnCWbdDsL9oeGMmjG4m/i3sfwwy8rilkbjW5ghqgpN62dJJ4uMMZjRHa4HrQ9xw1abRexOWBmvLd4RtVirkwZEvJlej3JDjDyl+cDu1LuS272ggErsOMs3jdF27tyCjb8b8kGajnv1E2/1iOC4S8seP6/Cjm9mcuLmIRM0lAAxFsccZlxxE0QP3lnllvhnbdSxowWo3CLME7iRNNQT738nZ0JRmol64sm8uoNzdsj4ZVJfud6LOh57mqKYNZ7fLOR2ElsPr2iXu3MKP5gKhOem/pWKyx+QjOtqQ81bD0fJgtMLf00pgd6RH5x1K2Pzjvn6WRNEPIU3RKD3IlQ1FQQKBgQDke0H3aQRILu1E9kjAiuOPqZQQZVb6ueow88FBaktR3Ykh/5qK/g3+Yo/p1dw/W8WS6kIzkjMeBwtIE1L1/u6L0E6HgbaFHW8ET2vlCFm664W29WWLirTrMv6jA9SNsi40UpeqCpgQWQTLack1b2Px1Ih5CqqAV7vy+UPLMTzUywKBgQCTZsM8tVmIn809hitk6tlRxMcEfFY/SqUeVCTPn+PrHuY/V2qEsV/gUU+wYCTWpLmJ/9to724VTKOWR18igvXLEXg6lBaciXzrqSuVioV4quBlih7H70mBP/+LtuuYN05PdImG9rLspo0LBtlWNCixCvEl3hZvLl2Q5s9q0dR9tQKBgQCOF01mBQxhq7VpPtTt0TJGJMrtC2j1Umufd+gGu4kN530iBKjiqa6gNkGTxalMSdFsVX1IPzLFCNJuR34/eAR4NNqxkOymijCBeLb7356WDWTB7IjzKBbtcnBDht1IJbhwV7D0UdRRzlqZ+jbkoVqBiZA53nU8jdNX/sa3de6D0wKBgQCEOWmnurI/OcJdj06772PU0uUIZBTmH6qw4yBGU5KbppM6fMsjjw2HZslKWWSSWx/I6AhFQIqKdr5EH2/6wGmGqPwl6BPEhhzWNWHQensyfG5hB56HEWLpm6Q8C3GZkPshkVORCxoD6X+aRaGOj6l073DU3D1ZrMjoI/QqSWIJBQKBgQCZD532IBDbLhNxsd/gwi6BRD6vdm8PFSr/yc1B8groUBHuCpyMIrk+H1UhJo5R26s1mGMr8AqZqvJusLZgsAim4mtWcilJdkAyXhKbdq6OeySSCeaUuQp3U5AYi/HrsOTNKD4x+NdCJ97MELQPN9Xk51mZzhlrPnj6zMHZbGIzfA==",//APP_PRIVATE_KEY 开发者私钥
                "json",//FORMAT
                "utf-8",//CHARSET
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApkDoesbH3ZUp55Zqz9boIn8sfXikbWdPEzVLPIICLpcOp5C8tiKSn6ZZcmtgHRLHSBrCYaj83vj9WL2Y5KtC9mfqwIhgVynwsz7jU/4w0GMObt9M0ZWvFqQ+cb27koRcZemEbPZ/4RBfmNzfJCXTmMxpOlH3yqNXXwCYge5aYo8BREbLWiwPU67EasV1yqinjeEImEn7DU0Y7eq/aWZWnqFqZ/S2cIYafg5IRx6c7bpcBpRY0/cfpknR1kmnkcmlA3CphLTkN3682j5SDlh4JcDwUn/Qzw59DkrnTJbM4nSt1C54X/WsEf3qOqUctN73fcnfYgLox5hZy7zdOWmgDQIDAQAB",//ALIPAY_PUBLIC_KEY 支付宝公钥
                SIGN_TYPE_RSA2); //加密类型
        //创建一个生产交易页面的请求对象
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        //设置交易完成的同步请求地址
        alipayRequest.setReturnUrl("http://www.baidu.com");
        //设置交易完成的异步请求地址
        alipayRequest.setNotifyUrl("http://3s.dkys.org:12793/pay/paynotify");//在公共参数中设置回跳和通知地址
        //通过交易请求对象,设置业务主体
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\""+orders.getOrderid()+"\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":"+orders.getAllprice().doubleValue()+"," +
                "    \"subject\":\""+orders.getOrderid()+"\"," + //标题
                "    \"body\":\""+orders.getOrderid()+"\"," +               //内容
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"2088511833207846\"" +
                "    }"+
                "  }");//填充业务参数

        //铜鼓欧sdk的api调用, 发送业务主体信息给支付宝服务器,并且返回一个支付页面
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=" + "utf-8");
        response.getWriter().write(form);//直接将完整的表单html输出到页面
        response.getWriter().flush();
        response.getWriter().close();
    }


    /**
     * 支付宝异步通知接收的方法
     */
    @RequestMapping("/paynotify")
    @ResponseBody
    public void payNotify(String out_trade_no,String trade_status) {
        System.out.println("订单号:"+out_trade_no+" 状态:"+trade_status);
        Orders orders = ordersService.queryByOrderId(out_trade_no);
        if ("TRADE_SUCCESS".equals(trade_status)) {
            orders.setStatus(1);
            ordersService.updateOrders(orders);
        }
    }


}
