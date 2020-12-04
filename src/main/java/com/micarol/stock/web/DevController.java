package com.micarol.stock.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.micarol.stock.pojo.CacheConstants;
import com.micarol.stock.service.cache.LocalCache;
import com.micarol.stock.util.Loggers;

@Controller
public class DevController {

	@RequestMapping(value="/dev/set/token")
	public @ResponseBody String index(@RequestParam String uid, @RequestParam String token) {
		LocalCache.putValue(CacheConstants.UID_TOKEN+uid, token);
		Loggers.RUNNING_LOG.info("set token.uid:[], token:{}", uid, token);
		return "ok";
	}
	
	@RequestMapping(value="/dev/get/cache")
	public @ResponseBody String index(@RequestParam String key) {
		return String.valueOf(LocalCache.getValue(key));
	}
}
