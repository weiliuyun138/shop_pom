package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.servie.ISearchService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Reference
    private ISearchService searchService;

    @Value("${server.ip}")
    private String serverip;
    /**
     * 根据关键字搜索
     * @param keyword
     * @return
     */
    @RequestMapping("/searchByKeyWord")
    public String searchByKeyWord(String keyword, ModelMap modelMap) {

        System.out.println("搜索工程获得的关键字: " + keyword);
        List<Goods> goods  = searchService.searchGoods(keyword);
        System.out.println("调用服务搜索到的结果："+goods);
        modelMap.put("goodlist", goods);
        modelMap.put("serverip", serverip);
        System.out.println(serverip);

        return "search_list";
    }

}
