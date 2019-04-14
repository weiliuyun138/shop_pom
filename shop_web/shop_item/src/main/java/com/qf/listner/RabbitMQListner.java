package com.qf.listner;

import com.qf.entity.Goods;
import com.qf.servie.IGoodsService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.FileWriter;
import java.util.HashMap;

@Component
public class RabbitMQListner {

    @Autowired
    private Configuration configuration;


    @RabbitListener(queues = "goods_queue2")
    public void rabbitmqMsg(Goods goods) {
//        System.out.println("详情,接受到消息:" +goods);

        //获得request对象
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
//        HttpServletRequest request = servletRequestAttributes.getRequest();

        //通过商品id获得商品详情信息
//        Goods goods = goodsService.queryById(id);
        String gimage = goods.getGimage();
//        System.out.println(gimage);
        String[] images = gimage.split("\\|");

        /*通过模板生成html页面*/
        try {
            //获得商品详情魔板对象
            Template template = configuration.getTemplate("goodsitem.ftl");

            //准备商品数据
            HashMap<String, Object> map = new HashMap<>();
            map.put("goods", goods);
//            map.put("context", request.getContextPath());
            map.put("images", images);

            //生成静态页面
            //获得classpath路径
            String path = this.getClass().getResource("/static/page/").getPath() + goods.getId() + ".html";
            template.process(map,new FileWriter(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
