package com.jt.cart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.cart.pojo.Cart;
import com.jt.cart.service.CartService;
import com.jt.common.vo.SysResult;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	//购物车查看 http://cart.jt.com/user/query/{userId}
	@RequestMapping("/query/{userId}")
	@ResponseBody
	public SysResult queryListByUserId(@PathVariable Long userId){
		List<Cart> cartList = cartService.queryListByUserId(userId);
		return SysResult.ok(cartList);
	}
	
	//新增商品到购物车 http://cart.jt.com/user/save
	@RequestMapping("/save")
	@ResponseBody
	public SysResult saveCart(Cart cart){
		Integer status = cartService.saveCart(cart);
		//先判断传回来的值
		if(200==status){
			return SysResult.ok();
		}else{
			return SysResult.build(status, "此商品已经存在，数据已更新");
		}
	}

	//已在购物车商品数量的修改 http://cart.jt.com/cart/update/num/{userId}/{itemId}/{num}
	@RequestMapping("/update/num/{userId}/{itemId}/{num}")
	@ResponseBody
	public SysResult updateCart(@PathVariable Long userId ,@PathVariable Long itemId ,@PathVariable Integer num){
		Cart _cart = new Cart();
		_cart.setNum(num);
		_cart.setUserId(userId);
		_cart.setItemId(itemId);
		cartService.updateCart(_cart);
		return SysResult.ok();
	}
	
	//删除购物车中的商品 http://manage.jt.com/cart/delete/{userId}/{itemId}
	@RequestMapping("/delete/{userId}/{itemId}")
	@ResponseBody
	public SysResult deleteCart(@PathVariable Long userId,@PathVariable Long itemId){
		Cart _cart = new Cart();
		_cart.setUserId(userId);
		_cart.setItemId(itemId);
		cartService.deleteCart(_cart);
		return SysResult.ok();
		
	}
}
