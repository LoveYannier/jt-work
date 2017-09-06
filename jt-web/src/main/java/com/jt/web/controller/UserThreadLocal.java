package com.jt.web.controller;

import com.jt.web.pojo.User;

//共享变量
public class UserThreadLocal {                                                                              
	private static final ThreadLocal<User> USER = new ThreadLocal<User>();

	public static ThreadLocal<User> get() {
		return USER;
	}
             
	public static void set(User user) {
		USER.set(user);
	}
	//获取用户id
	public static Long getUserId(){
		//用户可能为null
		try {
			return USER.get().getId();
		} catch (Exception e) {
			return null;
		}
	}
}
