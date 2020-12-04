package com.micarol.stock.listener;

import java.nio.charset.StandardCharsets;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micarol.stock.constants.Constants;
import com.micarol.stock.pojo.CacheConstants;
import com.micarol.stock.pojo.StockAlarmSetting;
import com.micarol.stock.pojo.StockPubNotice;
import com.micarol.stock.service.WeiboMonitorService;
import com.micarol.stock.service.cache.LocalCache;
import com.micarol.stock.service.rabbitmq.RabbitMQService;
import com.micarol.stock.util.JsonUtil;
import com.micarol.stock.util.Loggers;
import com.micarol.stock.util.SendMail;

@Component
public class StockMailListener {

	@Autowired
	private Queue stockMailQueue;
	@Autowired
	private RabbitMQService queueService;
	
	public void mailQueueOut(byte[] date) {
		mailQueueOut(new String(date, StandardCharsets.UTF_8));
    }
	
	public static void mailQueueOut(String message) {
		Loggers.RUNNING_LOG.info("mailQueueOut: {}", message);
		try {
			String[] arr = message.split(Constants.SPLIT_MAIL);
			if (arr.length == 2) {
				StockAlarmSetting setting = JsonUtil.jsonStr2Obj(arr[0], StockAlarmSetting.class);
				StockPubNotice notice = JsonUtil.jsonStr2Obj(arr[1], StockPubNotice.class);
				String subject = "[" + setting.getCode() + "]提醒, 关键词:["+setting.getKeyword()+"]";
				String body = "标题:"+notice.getTitle()+"\n链接:"+notice.getLink()+"\n日期:"+notice.getDate().toString();
				SendMail.qqMailSend(setting.getEmail(), subject, body, null);
				WeiboMonitorService.cache.put(CacheConstants.CACHE_MAIL+notice.getUniKey(), "1");
				Thread.sleep(10000);
			} else {
				Loggers.ERROR_LOG.error("queue msg format error.msg: {}", message);
			} 
		} catch (Exception e) {
			Loggers.ERROR_LOG.error("mailQueueOut error.msg:{}", message);
			Loggers.ERROR_LOG.error(e.getMessage(), e);
		}
	}
}
