package com.jt.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

//统一中专到views下的jsp页面
@Controller
public class PageController {

	//利用RESTFul形式，最后一个占位符就是页面名称
	@RequestMapping("/page/{pageName}")
	public String goHome(@PathVariable String pageName){
		//逻辑名就是jsp名称
		return pageName;
	} 
}
