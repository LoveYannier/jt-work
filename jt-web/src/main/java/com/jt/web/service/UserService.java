package com.jt.web.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.common.service.HttpClientService;
import com.jt.web.pojo.User;

@Service
public class UserService {
	
	@Autowired
	private HttpClientService httpClientService;

	//注册
	public String doRegister(User user) throws Exception {
		String url ="http://sso.jt.com/user/register";
		//doPost httpClient参数以map形式提交
		Map<String , String> param = new HashMap<String, String>();
		param.put("username", user.getUsername());
		param.put("password", user.getPassword());
		param.put("phone", user.getPhone());
		param.put("email",user.getPhone());//email上有唯一索引 暂时不用
		
		String username = httpClientService.doPost(url, param, "utf-8");
		return username;
	}
	
	//登陆
	public String doLogin(String u ,String p) throws Exception {
		String url ="http://sso.jt.com/user/login";
		//doPost httpClient参数以map形式提交
		Map<String , String> param = new HashMap<String, String>();
		param.put("u", u);
		param.put("p", p);
		String ticket = httpClientService.doPost(url, param, "utf-8");
		return ticket;
	}
		
}
