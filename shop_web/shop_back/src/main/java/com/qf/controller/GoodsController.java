package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 管理商品的Controller
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Reference
    private IGoodsService goodsService;

    @Value("${server1.ip}")
    private String serverIp;

    /**
     * 商品列表
     * @return
     */
    @RequestMapping("/list")
    public String goodsList(ModelMap map) {
        List<Goods> goods = goodsService.queryAll();
//        for (Goods g : goods) {
//            System.out.println(g);
//        }
        map.put("goodslist", goods);
        map.put("serverip", serverIp);

        return "goodslist";
    }

    /**
     * 添加商品
     *
     * 商品描述的处理 -- 周末作业
     *      通常商品描述是一个富文本信息,页面上需要使用一些富文本的编辑器,这些编辑器都有现成的插件.
     *      数据可以中只需要保存这些富文本的html值,显示的时候,直接在页面上输出这些html标签,浏览器就会自动解析这些富文本信息.
     *
     *      - 商品描述
     *      - 发送邮件
     * 商品图片的上传
     *
     *         -上传到哪里?
     *          单体架构直接上传到本地磁盘空间(d:/xxx)
     *          分布式集群项目,通常会上传到一个共享的分布式文件系统
     *
     * @param goods
     * @return
     */
    @RequestMapping("/insert")
    public String insert(Goods goods) {
        System.out.println("添加的商品信息: "+goods);
        int insert = goodsService.insert(goods);
        System.out.println(insert);
        return "redirect:/goods/list";
    }


    @RequestMapping("/delete")
    public String delete(Integer id) {
        goodsService.deleteById(id);
        return "redirect:/goods/list";
    }


    @RequestMapping("/toUpdate")
    public String toUpdate(Integer id) {
        Goods goods = goodsService.queryById(id);
        System.out.println(goods);

        return null;
    }

}
