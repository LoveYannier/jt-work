package com.jt.sso.mapper;

import java.util.Map;

import com.jt.common.mapper.SysMapper;
import com.jt.sso.pojo.User;

public interface UserMapper extends SysMapper<User> {
	/**
	 * 检查方法
	 * @param map 需要检查的字段
	 * @return
	 */
	public Integer check(Map<String , Object> map); 
}
