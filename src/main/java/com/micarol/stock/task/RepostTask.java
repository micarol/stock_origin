package com.micarol.stock.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.micarol.stock.pojo.CacheConstants;
import com.micarol.stock.service.cache.LocalCache;
import com.micarol.stock.service.wb.WeiboService;
import com.micarol.stock.util.JsonUtil;
import com.micarol.stock.util.Loggers;

@Component
public class RepostTask {

	private static SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
	
	private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	

    @Scheduled(cron = "0 0/1 * * * *")
	public void repostYCY() {
		String today = sdf1.format(new Date());
		long uid = 2582600707l;
		String token = null!=LocalCache.getValue(CacheConstants.UID_TOKEN+uid)?String.valueOf(LocalCache.getValue(CacheConstants.UID_TOKEN+uid)):null;
		String listId = "3667285636325046";
		long sinceId = null!=LocalCache.getValue(CacheConstants.SINCE_ID+uid)?((Long)LocalCache.getValue(CacheConstants.SINCE_ID+uid)):0l;
		int page = 1;
		if(StringUtils.isNotBlank(token)) {
			String str = WeiboService.getGroupTimeline(token, listId, page, 10, sinceId);
			JsonNode statuses = JsonUtil.readTree(str);
			for (JsonNode status : statuses) {
				if(status.get("id").asLong() > sinceId) {
					sinceId = status.get("id").asLong();
				}
				if(status.has("retweeted_status")) {
					if(status.get("retweeted_status").get("id").asLong() == 4283770074873824l) {
						try {
							Date createAt = sdf.parse(status.get("retweeted_status").get("created_at").asText());
							if(sdf1.format(createAt).equals(today)) {
								String s = WeiboService.repostWeibo(token, status.get("retweeted_status").get("mid").asText(), "repost weibo", 0);
								Loggers.RUNNING_LOG.info("repost rs:{}", s);
							}
						} catch (Exception e) {
							Loggers.ERROR_LOG.error("error msg: {}", status);
							Loggers.ERROR_LOG.error(e.getMessage(), e);
						}
					}
				}
			}
		}
		LocalCache.putValue(CacheConstants.SINCE_ID+uid, sinceId);
		Loggers.RUNNING_LOG.info("repostYCY over");
	}
	
}
