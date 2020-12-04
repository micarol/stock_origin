package com.micarol.stock.service.wb;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.JsonNode;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.micarol.stock.pojo.CacheConstants;
import com.micarol.stock.service.cache.LocalCache;
import com.micarol.stock.util.HttpRequestUtil;
import com.micarol.stock.util.JsonUtil;
import com.micarol.stock.util.Loggers;
import com.micarol.stock.util.OkHttpUtil;

/**
 * 删除微博
 * 
 * @author micarol
 * @date:2018年7月20日 下午5:39:29
 */
public class WeiboService {
	
	//http://open.weibo.com/wiki/2/statuses/destroy/biz
	public static String destory(String token, String id) throws Exception {
		String url = "https://c.api.weibo.com/2/statuses/destroy/biz.json";
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("access_token", token);
		String str = OkHttpUtil.post(url, params, false);
		System.out.println("destory:"+str);
		return str;
	}
	
	//http://open.weibo.com/wiki/2/statuses/user_timeline
	public static String userTimeline(String token, String uid, int page) throws Exception {
		String url = "https://api.weibo.com/2/statuses/user_timeline.json?access_token="+token+"&uid="+uid+"&page="+page;
		String str = OkHttpUtil.get(url);
		System.out.println("userTimeline:"+str);
		return str;
	}
	
	public static String homeTimeline(String token, String uid, int sinceId, int page) throws Exception {
		String url = "https://api.weibo.com/2/statuses/home_timeline.json?access_token="+token+"&since_id="+sinceId+"&page="+page+"&count=100";
		String str = OkHttpUtil.get(url);
		Loggers.RUNNING_LOG.info("url:{}, rs:{}", url, str);
		return str;
	}

	/**
	 * 获取当前登录用户某一好友分组的微博列表
	 * @param accessToken
	 * @param listId	需要查询的好友分组ID，建议使用返回值里的idstr，当查询的为私有分组时，则当前登录用户必须为其所有者。
	 * @param sinceId	若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为
	 * @param maxId		若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
	 * @param count		单页返回的记录条数，最大不超过200，默认为50。
	 * @param page		返回结果的页码，默认为1。
	 * @param baseApp	是否只获取当前应用的数据。0为否（所有数据），1为是（仅当前应用），默认为0。	
	 * @param feature	过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0。
	 * @see <a href="http://open.weibo.com/wiki/2/friendships/groups/timeline">friendships/groups/timeline</a>
	 */
	public static String getGroupTimeline(String token, String listId, int page, int count, long sinceId) {
		String url = "https://api.weibo.com/2/friendships/groups/timeline.json?access_token="+token+"&list_id="+listId+"&since_id="+sinceId+"&page="+page+"&count="+count;
		String str = OkHttpUtil.get(url);
		Loggers.RUNNING_LOG.info("url:{}, rs:{}", url, str);
		return str;
	}
	
	public static void delUserStatuses(String token, String uid) throws Exception {
		int page = 1;
		while (true) {
			JsonNode node = JsonUtil.readTree(userTimeline(token, uid, page++));
			if(null != node) {
				Iterator<JsonNode> it = node.get("statuses").getElements();
				if(!it.hasNext()) break;
				while (it.hasNext()) {
					JsonNode status = it.next();
//					System.out.println(status.get("mid"));
					System.out.println(destory(token, status.get("mid").asText()));;
					Loggers.RUNNING_LOG.info(status.asText());
				}
			} else {
				break;
			}
		}
		
	}
	
	/**
	 * 转发一条新微博 
	 * 
	 * @param id
	 *            要转发的微博ID
	 * @return Status
	 * @throws WeiboException
	 *             when Weibo service or network is unavailable
	 * @version weibo4j-V2 1.0.0
	 * @see <a
	 *      href="http://open.weibo.com/wiki/2/statuses/repost">statuses/repost</a>
	 */
	public static String repostWeibo(String token, String id, String status, int isComment) throws Exception {
		String url = "https://api.weibo.com/2/statuses/repost.json?token="+token+"&id="+id+"status="+status+"&is_comment"+isComment;
		String str = OkHttpUtil.get(url);
		Loggers.RUNNING_LOG.info("url:{}, rs:{}", url, str);
		return str;
	}
	
	public static void main(String[] args) throws Exception {
//		String token = "2.00PsrgFCnYTUdCb65ee6db9es8RJMB";
		String uid = "1916364215";
		String token = "2.00H71moCnYTUdCf778179a54hTDI8B";
//		delUserStatuses(token, uid);
//		homeTimeline(token, uid, 0, 1);
		getGroupTimeline(token, "3667285636325046", 1, 10, 0);
	}
	
}
