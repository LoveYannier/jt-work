package com.jt.manager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.manager.mapper.ItemCatMapper;
import com.jt.manager.pojo.ItemCat;
@Service
public class ItemCatServcie extends BaseService<ItemCat>{

	@Autowired
	private ItemCatMapper itemCatMapper;
	
	public List<ItemCat> queryById(String id){
		return itemCatMapper.queryById(id);
	}
}
