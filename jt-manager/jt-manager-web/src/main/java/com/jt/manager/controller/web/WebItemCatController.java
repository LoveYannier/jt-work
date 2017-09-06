package com.jt.manager.controller.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.ItemCatData;
import com.jt.common.vo.ItemCatResult;
import com.jt.manager.pojo.ItemCat;
import com.jt.manager.service.ItemCatServcie;

//共前台系统调用，获取商品分类的数据
@Controller
@RequestMapping("/web")
public class WebItemCatController {
	
	@Autowired
	private ItemCatServcie itemCatServcie;

	@RequestMapping("/itemcat/all")
	@ResponseBody 
	public ItemCatResult getItemCatAll(){
		//拼接三级菜单数据
		ItemCatResult result = new ItemCatResult();
		//拿到所有值
		List<ItemCat> itemCatList = itemCatServcie.queryAll();
		//所有的节点的子节点形成一个map结构 key时父分类 value是这个分类下的所有子节点
		Map<Long,List<ItemCat>> map = new HashMap<Long,List<ItemCat>>();
		for (ItemCat itemCat : itemCatList) {
			//判断当前map中有没有商品分类的父类id值
			if(!map.containsKey(itemCat.getParentId())){
				//如果当前map中没有此商品的父id，则创建一个空的数组
				map.put(itemCat.getParentId(), new ArrayList<ItemCat>());
			}
			//从map中获取某商品的父分类id值 ，给此父分类添加元素。（给当前商品的分类添加子元素）
			map.get(itemCat.getParentId()).add(itemCat);
		}
		
		List<ItemCat> cats = map.get(0L);	//规定，一级分类的父id=0
		List<ItemCatData> itemCatDataList = new ArrayList<ItemCatData>();
		for(ItemCat itemCat1 : cats){
			//拼接第一级分类
			ItemCatData d1 = new ItemCatData();
			d1.setUrl("/prducts/"+itemCat1.getId()+".html");
			d1.setName("<a href=\"/prducts/"+itemCat1.getId()+".html\">"+itemCat1.getName()+"</a>");
			
			//拼接二级分类内容
			List<ItemCatData> itemCatDataList2 = new ArrayList<ItemCatData>();	//保存第二分层分类
			for(ItemCat itemCat2 : map.get(itemCat1.getId())){
				ItemCatData d2 = new ItemCatData();
				d2.setUrl("/prducts/"+itemCat2.getId()+".html");
				d2.setName(itemCat2.getName());
				
				//拼接三级分类内容
				List<String> itemCatDataList3 = new ArrayList<String>();	//保存第三层分类
				for(ItemCat itemCat3 : map.get(itemCat2.getId())){
					//ItemCatData d3 = new ItemCatData();
					//d3.setName("/prducts/"+itemCat3.getId()+".html|"+itemCat3.getName());
					itemCatDataList3.add("/prducts/"+itemCat3.getId()+".html|"+itemCat3.getName());
				}
				d2.setItems(itemCatDataList3);
				itemCatDataList2.add(d2);
			}
			d1.setItems(itemCatDataList2);
			
			itemCatDataList.add(d1);	//增加一个一级分类到list中
			
			//一级分类菜单页面只要14个
			if(itemCatDataList.size()<14){
				result.setItemCats(itemCatDataList);
			}else{
				return result;
			}
		}
		return result;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
	}
	
}
