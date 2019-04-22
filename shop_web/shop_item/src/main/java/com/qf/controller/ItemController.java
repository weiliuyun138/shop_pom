package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.FileWriter;
import java.util.HashMap;


@Controller
@RequestMapping("/item")
public class ItemController {

    @Reference
    private IGoodsService goodsService;

    @Autowired
    private Configuration configuration;

    /**
     * 生成静态页面
     */
    @RequestMapping("/createHtml")
    public String createHtml(int id, HttpServletRequest request) {
        //通过商品id获得商品详情信息
        Goods goods = goodsService.queryById(id);
        String gimage = goods.getGimage();
//        System.out.println(gimage);
        String[] images = gimage.split("\\|");
        //通过模板生成html页面
        try {
            //获得商品详情魔板对象
            Template template = configuration.getTemplate("goodsitem.ftl");

            //准备商品数据
            HashMap<String, Object> map = new HashMap<>();
            map.put("goods", goods);
            map.put("context", request.getContextPath());
            map.put("images", images);

            //生成静态页面
            //获得classpath路径
            String path = this.getClass().getResource("/static/page/").getPath() + goods.getId() + ".html";
            template.process(map,new FileWriter(path));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
