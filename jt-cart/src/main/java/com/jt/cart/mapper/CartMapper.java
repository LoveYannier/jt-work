package com.jt.cart.mapper;

import java.util.List;
import java.util.Map;

import com.jt.cart.pojo.Cart;
import com.jt.common.mapper.SysMapper;

public interface CartMapper extends SysMapper<Cart>{
	public List<Cart> queryListByUserId(Long userId);
	public void updateByUserIdItemId(Map<String , Object> map);
}
