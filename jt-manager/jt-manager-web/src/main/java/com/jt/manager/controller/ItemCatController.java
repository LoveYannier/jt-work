package com.jt.manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.manager.pojo.ItemCat;
import com.jt.manager.service.ItemCatServcie;

@Controller
@RequestMapping("/item/cat")
public class ItemCatController {

	@Autowired
	private ItemCatServcie itemCatServcie;
	@RequestMapping("/itemCatAll")
	@ResponseBody  //添加此注解之后 java对象在底层被springMVC嗲用jackson提供工具类方法转换为json串
	public List<ItemCat> queryAll() {
		List<ItemCat> itemCatList = itemCatServcie.queryAll();
		return itemCatList;
	}
	
	//用户通过ajax 传递参数id 返回值json /item/cat/list
	@RequestMapping("list")
	@ResponseBody
	public List<ItemCat> queryListById(@RequestParam(defaultValue="0") String id){
		 
		return itemCatServcie.queryById(id);
	}
	
}
