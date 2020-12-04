package com.micarol.stock.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.micarol.stock.constants.Constants;
import com.micarol.stock.pojo.CacheConstants;
import com.micarol.stock.service.cache.LocalCache;
import com.micarol.stock.service.rabbitmq.RabbitMQService;
import com.micarol.stock.util.JsonUtil;
import com.micarol.stock.util.Loggers;
import com.micarol.stock.util.SendMail;
import com.micarol.stock.util.spider.WeiboSpider;

@Service
public class WeiboMonitorService {

	@Autowired
	private RabbitMQService queueService;
	@Autowired
	private Queue emailQueue;
	
	private static List<String> keywords;
	private static List<String> listIds;
	private String token = "2.0035_eWDnYTUdCfe51e29f84dIN2NE";
	private String myMail = "49134598@qq.com";
	
	public static LoadingCache<String, String> cache = CacheBuilder.newBuilder().maximumSize(100000l).expireAfterWrite(30, TimeUnit.DAYS)
			.build(new CacheLoader<String, String>(){
				@Override
				public String load(String key) throws Exception {
					return null;
				}
				
			});
	
	static {
		keywords = new ArrayList<>();
		keywords.add("我开通了#微博问答#");
		listIds = new ArrayList<>();
		listIds.add("4082480439013991");
	}
	
	public void weiboQuestion() {
		int page = 1;
		int count = 50;
		for (String listId : listIds) {
			page = 1;
			boolean flag = true;
			String str = "";
			while(flag && page<10){
				try {
					str = WeiboSpider.timeline(token, listId, page++, count);
					if(StringUtils.isBlank(str)) {
						flag = false;
					} else {
						JsonNode json = JsonUtil.readTree(str);
						JsonNode arr = json.get("statuses");
						if(arr.size() > 0) {
							for (JsonNode status : arr) {
								for (String keyword : keywords) {
									if(status.get("text").asText().contains(keyword)) {
										String screenName = status.get("user").get("screen_name").asText();
										String o = cache.get(CacheConstants.CACHE_SCREEN_NAME+screenName);
										if(null != o) {
											String body = "@"+screenName+" match keyword "+keyword;
											queueService.putMessage(emailQueue, SendMail.mailJsonStr("micarol", myMail, "["+keyword+"]提醒", body));
											cache.put(CacheConstants.CACHE_SCREEN_NAME+screenName, "1");
										}
									}
								}
							}
						} else {
							flag = false;
						}
					}
				} catch (Exception e) {
					Loggers.ERROR_LOG.error(e.getMessage(), e);
				}
			}
		}
	}
}
