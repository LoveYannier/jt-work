package com.jt.search.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jt.common.service.HttpClientService;
import com.jt.common.spring.exetend.PropertyConfig;
import com.jt.common.vo.SysResult;
import com.jt.search.pojo.Item;

@Component
public class RabbitItemService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RabbitItemService.class);
	@Autowired
	private SearchService searchService;
	@Autowired
	private HttpClientService apiService;
	@PropertyConfig
	private String MANAGE_URL;
	public void saveOrUpdateItem(Long itemId){
		LOGGER.info("接收到消息 ， 内容为：{}",itemId);
		Item item = this.getItemFromApi(itemId);
		if(null!=item){
			this.searchService.update(item);
		}
	}
	
	private Item getItemFromApi(Long itemId){
		try {
			//通过httpClient去后台的查询中拿到商品数据
			String url = MANAGE_URL+"/item/query/"+itemId; 
			String jsonData = this.apiService.doGet(url);
			//将json串转换成java对象
			SysResult result = SysResult.formatToPojo(jsonData, Item.class);
			//取到SysResult中data
			return (Item) result.getData();
		} catch (Exception e) {
			LOGGER.error("更新solr数据出错！itemId = " + itemId , e.getMessage());
		}
		return null;
	}
}
