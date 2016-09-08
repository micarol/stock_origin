package com.micarol.stock.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.micarol.stock.service.UserService;
import com.micarol.stock.util.Loggers;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login(@RequestParam String name, @RequestParam String psw) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("user", userService.login(userService.userByName(name), psw));
		return map;
	}
}
