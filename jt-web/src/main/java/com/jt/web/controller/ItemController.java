package com.jt.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.web.pojo.Item;
import com.jt.web.pojo.ItemDesc;
import com.jt.web.service.ItemService;
@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;
	//请求路径
	@RequestMapping("/item/{id}")
	public String queryItemById(@PathVariable Long id , Model model) throws Exception{
		//转向httpClient
		Item item = itemService.getItemById(id);
		ItemDesc itemDesc = itemService.getItemDescById(id);
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		return "item";
	}

}
