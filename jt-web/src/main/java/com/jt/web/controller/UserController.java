package com.jt.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.util.CookieUtils;
import com.jt.common.vo.SysResult;
import com.jt.web.pojo.User;
import com.jt.web.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	public static final String cookieName = "JT_TICKET";
	private static final ObjectMapper MAPPER = new ObjectMapper();
	//转向注册页面
	@RequestMapping("/register")
	public String register(){
		return "register";
	}
	
	//注册  参数按js中的参数命名  路径映射难题！解决
	@RequestMapping("/doRegister")
	@ResponseBody
	public SysResult doRegister(User user) throws Exception{
		//新增
		String userJson = userService.doRegister(user);
		//SysResult不能直接转换 本身对象构造有问题
		JsonNode jsonNode = MAPPER.readTree(userJson);
		//获取对象的data属性内容
		String username = jsonNode.get("data").textValue();
		return SysResult.ok(username);
	}
	
	//转向登陆
	@RequestMapping("/login")
	public String login(){
		return "login";
	}
	
	//登陆 保存在cookie
	@RequestMapping("/doLogin")
	@ResponseBody
	public SysResult doLogin(String username , String password , HttpServletRequest request , HttpServletResponse response) throws Exception{
		String jsonData = userService.doLogin(username, password);
		//写cookie	必须在前台写入 因为其他的访问时 按照域名来获取
		
		//利用jackson 值获取SysResult.data属性
		JsonNode jsonNode = MAPPER.readTree(jsonData);
		JsonNode sysData = jsonNode.get("data");
		String ticket = sysData.textValue();//转成字符串
		
		CookieUtils.setCookie(request, response, cookieName, ticket);
		return SysResult.ok();
				
	}
	
	//退出登陆
	@RequestMapping("/logout")
	public  String doLogout(HttpServletRequest request , HttpServletResponse response){
		CookieUtils.deleteCookie(request, response, cookieName);
		return "index";
	}
}
