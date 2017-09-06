package com.jt.sso.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.RedisService;
import com.jt.sso.mapper.UserMapper;
import com.jt.sso.pojo.User;
@Service
public class UserService extends BaseService<User>{
	//1、
	@Autowired
	private UserMapper userMapper;
	
	//2、缓存新增
	@Autowired
	private RedisService redisService;
	
	//3、json串 与 java对象 转换
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	//4、日志
	private Logger log =Logger.getLogger(UserService.class) ;
	
	//检查方法的调用 页面传递请求参数以及类型
	public Boolean check(Integer type , String param){
		Map<String,Object> map = new HashMap<String,Object>();
		String typename= "";
		if(1==type){
			typename="username";
		}else if(2==type){
			typename="phone";
		}else if(3==type){
			typename="email";
		}
		//给mapper中的map添加参数
		map.put("typename", typename);
		map.put("val", param);
		//获取数量
		int i = userMapper.check(map);
		if(i==0){
			return false;
		}else{
			return true;
		}
	}
	
	//用户的注册
	public User register(User user){
		user.setCreated(new Date());
		user.setUpdated(user.getCreated());
		//密码加密
		user.setPassword(DigestUtils.md5Hex(user.getPassword()));
		userMapper.insertSelective(user);
		return user;
	}
	
	//用户的登陆 返回的是ticket
	public String login(String username , String password){
		//需要得到对象 按照用户名进行查询 密码进行比较
		User param = new User();
		param.setUsername(username);
		
		List<User> userList = userMapper.select(param);
		//判断是否有数据 
		if(userList != null && userList.size()>0){
			//拿到当前的user
			User curUser = userList.get(0);
			//比较密码
			String dbPassword = curUser.getPassword();//数据库中已加密的密码
			String enPassword = DigestUtils.md5Hex(password);//页面密码加密
			if(dbPassword.equals(enPassword)){
				//构造ticket!!! 唯一 
				String ticket = DigestUtils.md5Hex("JT_TICKET"+System.currentTimeMillis()+username);
				//将当前用户设置到缓存中  将user转换成json串 然后放入redis中 业务判断是否登陆 看redis中是否有这个ticket
				try {
					redisService.set(ticket, MAPPER.writeValueAsString(curUser), 60*60*24810);
					return ticket;
				} catch (JsonProcessingException e) {
					//写日志
					log.error(e.getMessage());
				}
				
			}
		}
		return null;//前台按照这个值 为null则转到登陆页面
	}

	//按照ticket在redis中查询user信息
	public String queryByTicket(String ticket) {
		try {
			return redisService.get(ticket);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
