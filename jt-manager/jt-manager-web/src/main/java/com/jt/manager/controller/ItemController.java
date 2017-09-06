package com.jt.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.EasyUIResult;
import com.jt.common.vo.SysResult;
import com.jt.manager.pojo.Item;
import com.jt.manager.pojo.ItemDesc;
import com.jt.manager.service.ItemService;
/**
 * 异常的捕获应该写在controller层，不能写在service层，在service层不生效
 * @author bigjsd
 *
 */
@Controller
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private ItemService itemServcie;
	
	//商品列表页面  /item/query 传入EasyUIResult中组件需要的数据：设定分页参数 page当前页 rows每页记录的条数
	@RequestMapping("/query")
	@ResponseBody //将EasyUIResult转成json串
	public EasyUIResult queryItemList(Integer page, Integer rows){
		//怎样将数据封装到EasyUIResult中
		return itemServcie.queryItemList(page, rows);
	}
	
	//商品新增
	@RequestMapping("save")
	@ResponseBody
	public SysResult saveItem(Item item , String desc){
		 try {
			itemServcie.saveItem(item,desc);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, e.getMessage());					
		}
	}
	//商品新增
	@RequestMapping("update")
	@ResponseBody
	public SysResult updateItem(Item item,String desc){
		try {
			itemServcie.updateItem(item,desc);
			return SysResult.ok();
		} catch (Exception e) {
			return SysResult.build(201, e.getMessage());
		}
	}
	
	//商品删除 /item/delete
	@RequestMapping("delete")
	@ResponseBody
	public SysResult deleteItem(String[] ids){
		try {
			itemServcie.deleteItem(ids);
			return SysResult.ok();
		} catch (Exception e) {
			return SysResult.build(201, e.getMessage());
		}
	}
	
	//商品描述的查询
	@RequestMapping("query/item/desc/{id}")
	@ResponseBody
	public SysResult queryItemDesc(@PathVariable Long id){
		ItemDesc itemDeac = itemServcie.queryDescById(id);
		return SysResult.ok(itemDeac);
	}
}
