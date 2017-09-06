package com.jt.cart.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.cart.mapper.CartMapper;
import com.jt.cart.pojo.Cart;
@Service
public class CartService extends BaseService<Cart>{

	@Autowired
	private CartMapper cartMapper;
	
	//我的购物车
	public List<Cart> queryListByUserId(Long userId){
		return cartMapper.queryListByUserId(userId);
	}
	
	//商品页面到--购物车 商品的保存 新增 修改
	public Integer saveCart(Cart cart){
		//判断此用户的商品是否已在购物车中
		Cart param = new Cart();
		param.setUserId(cart.getUserId());
		param.setItemId(cart.getItemId());
		Integer count = cartMapper.selectCount(param);
		//开始判断
		if(0==count){//代表此用户的次商品不存在 	新增
			cart.setCreated(new Date());
			cart.setUpdated(cart.getCreated());
			
			cartMapper.insertSelective(cart);	
			return 200;//新增成功
		}else{//代表此用户的此商品已经在购物车中	修改商品数量
			//先获取旧的商品数量 加上提交的商品的数量
			Cart oldCart = super.queryByWhere(param); //从父类BaseService中
			oldCart.setUpdated(new Date());
			
			Integer oldNum = oldCart.getNum();//已有的数量
			Integer newNum = cart.getNum();//新增的数量
			oldCart.setNum(oldNum+newNum);
			
			cartMapper.updateByPrimaryKeySelective(oldCart);
			return 202;//已经存在此商品 修改商品数量
			
		}
		
	}
	
	//购物车中商品数量的修改
	public void updateCart(Cart cart){
		Map<String , Object> param = new HashMap<String , Object>();
		param.put("num", cart.getNum());
		param.put("userId", cart.getUserId());
		param.put("itemId", cart.getItemId());
		cartMapper.updateByUserIdItemId(param);
	}
	
	//购物车中删除商品
	public void deleteCart(Cart cart){
		cartMapper.delete(cart);
	}
}
