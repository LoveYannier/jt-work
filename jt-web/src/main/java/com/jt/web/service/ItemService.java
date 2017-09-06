package com.jt.web.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.common.spring.exetend.PropertyConfig;
import com.jt.web.pojo.Item;
import com.jt.web.pojo.ItemDesc;

@Service
public class ItemService {
	//转向httpClient
	@Autowired
	private HttpClientService httpClientService;

	@PropertyConfig
	private String MANAGE_URL;
	
	//日志
	private static final Logger LOG = Logger.getLogger(ItemService.class);
	//转换jackson格式的支持对象
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	
	public Item getItemById(Long id){
		//请求的uri
		String uri = MANAGE_URL+"/web/query/item/"+id;
		try {
			String jsonItem = httpClientService.doGet(uri,"utf-8");
			//将json串转换成java对象
			Item item = MAPPER.readValue(jsonItem, Item.class);
			return item;
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		return null;
	}


	public ItemDesc getItemDescById(Long id) {
		//请求uri
		String uri = MANAGE_URL+"/web/query/itemdesc/"+id;
		//转向httpClientService
		try {
			String jsonItemDesc = httpClientService.doGet(uri, "utf-8");
			//转换
			ItemDesc itemDesc = MAPPER.readValue(jsonItemDesc, ItemDesc.class);
			return itemDesc;
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		return null;
	}

}
