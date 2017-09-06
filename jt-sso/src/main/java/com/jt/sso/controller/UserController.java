package com.jt.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jt.common.vo.SysResult;
import com.jt.sso.pojo.User;
import com.jt.sso.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired 
	private UserService userService;
	
	//检验 自己写mapper中的方法
	@RequestMapping("/check/{param}/{type}")
	@ResponseBody
	public SysResult check(@PathVariable String param ,@PathVariable Integer type){
		Boolean b = userService.check(type, param);
		return SysResult.ok(b);
	}
	
	//注册
	@RequestMapping("/register")
	@ResponseBody
	public SysResult register(User user){
		User _user = userService.register(user); //临时变量
		return SysResult.ok(_user.getUsername()); //传递用户名
	}
	
	//登陆
	@RequestMapping("/login")
	@ResponseBody
	public SysResult login(@RequestParam("u") String username , @RequestParam("p") String password) throws JsonProcessingException{
		String ticket = userService.login(username, password);
		return SysResult.ok(ticket);
	}
	
	//通过ticket查询用户信息
	@RequestMapping("/query/{ticket}")
	@ResponseBody
	public SysResult query(@PathVariable String ticket){
		String userJson = userService.queryByTicket(ticket);
		return SysResult.ok(userJson);
	}
}
