package com.micarol.stock.util.spider;

import java.io.IOException;
import java.net.URLConnection;
import java.util.List;

import org.apache.http.HttpRequest;
import org.codehaus.jackson.JsonNode;

import com.micarol.stock.util.HttpRequestUtil;
import com.micarol.stock.util.JsonUtil;

public class WeiboSpider {

	//https://api.weibo.com/2/friendships/groups.json 获取分组id
	//http://open.weibo.com/wiki/2/friendships/groups/timeline 获取分组微博
	public static String getGroupId(String token) throws Exception {
		String groupsUri = "https://api.weibo.com/2/friendships/groups.json";
		String str = HttpRequestUtil.get(groupsUri+"?access_token="+token);
		
		return str;
	}
	
	public static String timeline(String token, String listId, int page, int count) throws Exception {
		String groupTimelineUri = "https://api.weibo.com/2/friendships/groups/timeline.json";
		String str = HttpRequestUtil.get(groupTimelineUri+"?access_token="+token+"&list_id="+listId+"&page="+page+"&count="+count);
		
		return str;
	}
	
	public static void main(String[] args) throws Exception {
		String str = WeiboSpider.timeline(WeiboSpider.getGroupId("2.0035_eWDnYTUdCfe51e29f84dIN2NE"), "4082480439013991", 1, 50);
		JsonNode json = JsonUtil.readTree(str);
		JsonNode arr = json.get("statuses");
		for (JsonNode status : arr) {
			System.out.println(status.get("text"));
		}
	}
}
