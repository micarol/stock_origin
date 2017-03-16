package com.micarol.stock.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micarol.stock.service.WeiboMonitorService;
import com.micarol.stock.util.Loggers;

@Component
public class WeiboMonitorTask {

	@Autowired
	private WeiboMonitorService weiboMonitorService;
	
	public void weiboQuestion() {
		Loggers.RUNNING_LOG.info("weibo question task start");
		weiboMonitorService.weiboQuestion();
		Loggers.RUNNING_LOG.info("weibo question task end");
	}
}
