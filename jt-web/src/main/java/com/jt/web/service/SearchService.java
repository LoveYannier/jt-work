package com.jt.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.web.pojo.Item;

@Service
public class SearchService {
	
	@Autowired
	private HttpClientService httpClientService; 
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	//调用search系统业务接口，进行solr数据全文检索
	public List<Item> getItemListBySearch(String keyWords,Integer page, Integer rows) throws Exception{
		//将将索引所需属性 放入map中
		String url ="http://search.jt.com/search";
		Map<String , String> map = new HashMap<String , String >();
		map.put("keyWords", keyWords);
		map.put("page", ""+page);
		map.put("rows", rows+"");
		//访问search系统
		String jsonData = httpClientService.doGet(url, map, "utf-8");
		if(jsonData != null){
			
			JsonNode jsonNode = MAPPER.readTree(jsonData);
			JsonNode itemData = jsonNode.get("data");
			
			//使用ObjectMapper转换
			return MAPPER.readValue(itemData.traverse(), MAPPER.getTypeFactory().constructCollectionType(List.class, Item.class));
		}
		
		return null;
	}

}
