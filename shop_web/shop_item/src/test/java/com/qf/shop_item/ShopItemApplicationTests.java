package com.qf.shop_item;

import com.qf.entity.Goods;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopItemApplicationTests {

	@Autowired
	private Configuration configuration;

	@Test
	public void contextLoads() throws IOException, TemplateException {
		//准备一个静态页面输出的位置
		String path = "C:\\Users\\wly138\\Desktop\\hello.html";
		Writer writer = new FileWriter(path);

		//通过配置对象读取模板
		Template template = configuration.getTemplate("hello.ftl");

		//准备数据部分
		HashMap<String, Object> map = new HashMap<>();
		map.put("key", "freeMarker");
		Goods goods = new Goods();
		goods.setGname("张三");
		goods.setGinfo("内容");
		List<Goods> goodsList = new ArrayList<>();
		goodsList.add(goods);
//		map.put("key",goods):
		map.put("age", 16);
		map.put("age1", 25);
		map.put("goodlist", goodsList);
		map.put("now", new Date());


		//将魔板和数据进行静态合并
		template.process(map,writer);

		//关闭流
		writer.close();
	}

}
