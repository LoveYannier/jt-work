package com.jt.manager.controller.web;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.RedisService;
import com.jt.manager.pojo.Item;
import com.jt.manager.pojo.ItemDesc;
import com.jt.manager.service.ItemService;

@Controller
public class WebItemControler {
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private RedisService redisService;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	//前台系统中 按照商品的id来查询某个商品
	@RequestMapping("/web/query/item/{id}")
	@ResponseBody //htppClient中大多需要的是json字符串
	public Item getItemById(@PathVariable Long id) throws JsonParseException, JsonMappingException, IOException{
		/*
		 * 增加redis的缓存
		 */
		
		//redis缓存的读
		String key ="ITEM_"+id; //唯一的
		String jsonData = redisService.get(key);	//并不是一定能够读取到 第一次缓存中是 没有值的
		//判断值是否存在
		if(StringUtils.isNotEmpty(jsonData)){
			//有数据 则直接返回 对象
			Item item = MAPPER.readValue(jsonData, Item.class);
			return item;
		}
		
		//如果缓存没有数据 则继续执行业务 进到数据库中查询数据
		Item item = itemService.queryById(id);
		
		//redis缓存 写 设置一个缓存信息保留时间
		redisService.set(key, MAPPER.writeValueAsString(item),60*60*24*10);//需要json串
		System.out.println(redisService.get(key));
		return item;
	}
	
	@RequestMapping("/web/query/itemdesc/{id}")
	@ResponseBody
	public ItemDesc getItemDescById(@PathVariable Long id){
		return itemService.queryDescById(id);
	}
}
