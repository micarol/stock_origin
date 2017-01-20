package com.micarol.stock.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.micarol.stock.service.StockService;
import com.micarol.stock.util.Loggers;

@Component
public class StockPubNoticeTask {

	@Autowired
	private StockService stockService;
	
	/**
	 * 公告爬虫
	 */
	public void pubNoticeSpider() {
		long s = System.currentTimeMillis();
		Loggers.RUNNING_LOG.info("pubNoticeSpider start");
		stockService.pubNoticeSpider();
		Loggers.RUNNING_LOG.info("pubNoticeSpider over, using {}ms", System.currentTimeMillis()-s);
	}
	
	/**
	 * 监控处理
	 */
	public void alarmHandler() {
		long s = System.currentTimeMillis();
		Loggers.RUNNING_LOG.info("alarmHandler start");
		stockService.alarmHander();
		Loggers.RUNNING_LOG.info("alarmHandler over, using {}ms", System.currentTimeMillis()-s);
	}
	
	public void testQueue() {
		stockService.testQueue();
	}
	
}
