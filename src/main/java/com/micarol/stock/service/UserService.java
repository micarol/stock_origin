package com.micarol.stock.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micarol.stock.dao.mapper.UserMapper;
import com.micarol.stock.pojo.User;
import com.micarol.stock.util.StringUtil;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;
	
	public User userByName(String name) {
		return userMapper.userByName(name);
	}
	
	public Map<String, Object> login(User user, String psw) {
		Map<String, Object> map = new HashMap<>();
		map.put("code", "0");
		if(null != user) {
			if(this.pswGenerator(psw).equals(user.getPsw())) {
				map.put("code", "1");
				user.setPsw("");
				map.put("user", user);
			} else {
				map.put("code", "wrong password");
			}
		}
		return map;
	}
	
	private String pswGenerator(String psw) {
		return StringUtil.getMD5(psw);
	}
}
