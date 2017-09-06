package com.jt.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.web.pojo.Cart;
import com.jt.web.pojo.Item;

@Service
public class CartService {
	@Autowired
	private HttpClientService httpClientService;
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	//获取购物车中的商品信息
	public List<Cart> getCartList(Long userId) throws Exception{
		String url = "http://cart.jt.com/cart/query/"+userId;
		String jsonData = httpClientService.doGet(url, "utf-8");
		//获取data值
		JsonNode jsonNode = MAPPER.readTree(jsonData);
		JsonNode dataNode = jsonNode.get("data");
		//将jackson 转换List<Pojo>方式!!!!
		List<Cart> cartList = MAPPER.readValue(dataNode.traverse(), 
				MAPPER.getTypeFactory().constructParametricType(List.class, Cart.class));
		return cartList;
	}
	
	//加入购物车
	public void add(Long userId, Long itemId , Integer num) throws Exception{
		//查询3个冗余的字段 转到后台的webItemControler中获取已有商品的信息
		String url = "http://manage.jt.com/web/query/item/"+itemId;
		String jsonData = httpClientService.doGet(url,"utf-8");
		//将得到的包含商品信息的json串转换成对象
		Item item = MAPPER.readValue(jsonData, Item.class);
		
		//保存页面
		String url2="http://cart.jt.com/cart/save";
		Map<String,String> params = new HashMap<String,String>();
		params.put("userId", userId+"");
		params.put("itemId", itemId+"");
		params.put("itemTitle", item.getTitle());
		try {
			params.put("itemImage", item.getImage().split(",")[0]);
		} catch (Exception e) {
			params.put("itemImage", null);
		}
		params.put("itemPrice", item.getPrice()+"");
		params.put("num", num+"");
		httpClientService.doPost(url2, params, "utf-8");
	}

	//更新购物车中商品的数量	http://cart.jt.com/cart/update/num/{userId}/{itemId}/{num}
	public void updateCart(Long userId,Long itemId, Integer num) throws Exception {
		
		String url = "http://cart.jt.com/cart/update/num/"+userId+"/"+itemId+"/"+num;
		httpClientService.doGet(url,"utf-8");
		
	}

	//删除购物车中的某商品
	public void deleteCart(Long userId, Long itemId) throws Exception {

		String url = "http://manage.jt.com/cart/delete/"+userId+"/"+itemId;
		httpClientService.doGet(url, "utf-8");
				
	}
	
}
