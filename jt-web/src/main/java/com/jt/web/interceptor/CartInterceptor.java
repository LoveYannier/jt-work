package com.jt.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.RedisService;
import com.jt.common.util.CookieUtils;
import com.jt.web.controller.UserController;
import com.jt.web.controller.UserThreadLocal;
import com.jt.web.pojo.User;

public class CartInterceptor implements HandlerInterceptor {

	@Autowired
	private RedisService redisService;

	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Override // 在controller之前执行 拦截请求 先判断是否登录
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 从cookie中获取值
		String ticket = CookieUtils.getCookieValue(request, UserController.cookieName);
		// 是否有值
		if (StringUtils.isNotEmpty(ticket)) {
			// 有值的话 从缓存中拿到对应的json串
			String jsonData = redisService.get(ticket);
			// 判断得到的jsonData是否不为空
			if (StringUtils.isNotEmpty(jsonData)) {
				// 转换成user对象
				User curUser = MAPPER.readValue(jsonData, User.class);
				// 设置到本地线程中 任意的Controller都可以访问这个本地线程 拿到当前用户这一变量
				UserThreadLocal.set(curUser);
				
			} else {
				UserThreadLocal.set(null);
			}
		} else {
			UserThreadLocal.set(null);
		}
		
		//优化：没有登录的情况 ， 本地线程中拿不到用户信息 去登录页面
		if(null == UserThreadLocal.getUserId()){
			response.sendRedirect("http://www.jt.com/user/login.html");
			return false;
		}else{
			return true; // false不放行 true放行
		}
		
	}

	@Override //执行controller方法后执行
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override //执行controller方法后执行，转向页面前（render渲染）
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
