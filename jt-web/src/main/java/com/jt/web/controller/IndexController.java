package com.jt.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	//转向首页 views/index.jsp
	/*
	 * 请求 在浏览器端	 http://www.jt.com/index.html
	 * 经过nginx转发	http://localhost:8082/index.html
	 * --》springmvc容器规范	  /index.html 
	 * 映射 （*.html）后	 /index
	 */
	@RequestMapping("/index") //全局唯一 扫描包之后 通过反射拿到底下的类和方法  
	public String index(){
		return "index";
	}
}
