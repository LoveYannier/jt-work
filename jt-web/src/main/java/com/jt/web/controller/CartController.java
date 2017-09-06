package com.jt.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.web.pojo.Cart;
import com.jt.web.service.CartService;

@Controller
@RequestMapping("/cart")
public class CartController {
	@Autowired
	private CartService cartService;

	//访问我的购物车 
	@RequestMapping("/show")
	public String show(Model model) throws Exception{
		Long userId = UserThreadLocal.getUserId(); //从本地线程中获取
		/*if(null == userId){	//没有登录，转向登录页面
			return "redirect:/user/login.html";
		}*/
		List<Cart> cartList = cartService.getCartList(userId);
		model.addAttribute("cartList", cartList);
		return "cart";
	}
	
	//在商品详情页面将商品加入购物车
	@RequestMapping("/add/{itemId}")
	public String add(@PathVariable Long itemId ,Integer num) throws Exception{
		Long userId = UserThreadLocal.getUserId(); //从本地线程中获取
		/*if(null == userId){	//没有登录，转向登录页面
			return "redirect:/user/login.html";
		}*/
		cartService.add(userId, itemId, num);
		return "redirect:/cart/show.html";
	}
	
	//修改商品数量 service/cart/update/num/741524/2
	@RequestMapping("/update/num/{itemId}/{num}")
	public String updateCart(@PathVariable Long itemId,@PathVariable Integer num) throws Exception{
		Long userId = UserThreadLocal.getUserId();
		/*if(userId==null){
			return "redirect:/user/login.html";
		}*/
		cartService.updateCart(userId,itemId,num);
		return "redirect:/cart/show.html";
	}
	
	//删除购物车中商品
	@RequestMapping("/delete/{itemId}")
	public String deleteCart(@PathVariable Long itemId) throws Exception{
		
		Long userId = UserThreadLocal.getUserId();
		/*if(userId==null){
			return "redirect:/user/login.html";
		}*/
		cartService.deleteCart(userId,itemId);
		return "redirect:/cart/show.html";
	}
}
