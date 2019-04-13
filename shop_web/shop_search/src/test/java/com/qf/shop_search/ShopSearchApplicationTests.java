package com.qf.shop_search;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopSearchApplicationTests {

	@Autowired
	private SolrClient solrClient;

	/**
	 * 添加索引
	 * id相同就是修改, 变通就是增加
	 */
	@Test
	public void add() {
		//创建document对象
		SolrInputDocument solrDocument = new SolrInputDocument();
		solrDocument.addField("id", 2);
		solrDocument.addField("gname", "洗衣机");
		solrDocument.addField("gimage", "group1/M00/00/00/rBJWEFyv9ZGARmtsAACDzLkchRk159.png");
		solrDocument.addField("ginfo", "商品海尔详情");
		solrDocument.addField("gprice", "99.99");
		solrDocument.addField("gsave", "23");
		try {
			solrClient.add(solrDocument);
			solrClient.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void delete() {
		try {
//			UpdateResponse result = solrClient.deleteById("1");
//			solrClient.deleteByQuery("gname:洗衣机");//匹配的都会被删除
			List<String> list = new ArrayList<>();
			list.add("23");
			list.add("24");
			solrClient.deleteById(list);
			solrClient.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Test
	public void query() throws IOException, SolrServerException {
		SolrQuery solrQuery = new SolrQuery();
//		solrQuery.setQuery("ginfo:省电");
//		solrQuery.setQuery("gname:洗衣机 || ginfo:省电");
		String keyword = "省电洗衣机";
		solrQuery.setQuery("gname:" + keyword + "|| ginfo:" + keyword);


		//获得查询结果
		QueryResponse result = solrClient.query(solrQuery);
		SolrDocumentList documentList = result.getResults();
		for (SolrDocument document:documentList) {
			long id = Long.parseLong(document.get("id")+"");
			String gname = (String) document.get("gname");
			String ginfo = (String) document.get("ginfo");
			float gprice = (float) document.get("gprice");
			int gsave = (int) document.get("gsave");
			System.out.println(id+" "+gname+" " +ginfo+" "+gprice+" "+gsave);
		}
	}



	@Test
	public void contextLoads() {
	}

}
