package com.micarol.stock.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.micarol.stock.WebConstants;
import com.micarol.stock.pojo.StockAlarmSetting;
import com.micarol.stock.pojo.User;
import com.micarol.stock.service.StockService;
import com.micarol.stock.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private StockService stockService;
	
	@RequestMapping(value = "/my/code/add", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> addStockCode(@RequestParam String codes, 
    		HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		//TODO
		return map;
	}
	
	//添加监控设置
	@RequestMapping(value = "/my/alarm/add", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> addAlarmSetting(@RequestParam String code, @RequestParam String keyword, 
    		@RequestParam int notice, HttpServletRequest request) throws Exception {
		User user = (User)request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> map = new HashMap<>();
		StockAlarmSetting setting = new StockAlarmSetting();
		setting.setCode(code);
		setting.setKeyword(keyword);
		setting.setUserid(user.getId());
		setting.setCreateTime(new Date());
		setting = stockService.addStockAlarmSetting(setting);
		if(null != setting) {
			map.put("setting", setting);
		} else {
			map.put("error", "save error");
		}
		return map;
	}
	
	//TODO: 修改通知
	
	//删除监控设置
	@RequestMapping(value = "/my/alarm/del", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> delAlarmSetting(@RequestParam Integer id,  
    		HttpServletRequest request) throws Exception {
		User user = (User)request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> map = new HashMap<>();
		if(stockService.delStockAlarmSetting(user.getId(), id) <= 0) {
			map.put("error", "del error");
		}
		return map;
	}
}
