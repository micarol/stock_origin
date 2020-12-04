package com.micarol.stock.service.wb;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Locale;

import org.codehaus.jackson.JsonNode;

import com.micarol.stock.util.JsonUtil;
import com.micarol.stock.util.Loggers;
import com.micarol.stock.util.OkHttpUtil;
import com.micarol.stock.util.StringUtil;

import micarol.util.wb.FileUtil;

public class StatusesService {

	public static void main(String[] args) throws Exception {
		commensDataFetch("2.00PsrgFCnYTUdCb2ea6cb9f2p8q6zC", "4344408659039726");
	}
	
	/**
	 * 获取指定微博的评论列表，并将评论用户部分字段追加到指定的TSV 文件
	 * @param token
	 * @param mid
	 * @throws Exception
	 * @author micarol
	 * @date:Feb 28, 2019 6:14:10 PM
	 */
	public static void commensDataFetch(String token, String mid) throws Exception {
		int page = 1;
		int count = 50;
		while (true) {
			String data = commentsShowByMid(token, mid, page++, count, null);
			JsonNode result = JsonUtil.readTree(data);
			if(null == result) {
				break;
			}
			dataHandle(data);
			if(result.get("next_cursor").asLong() == 0) {
				break;
			}
		}
		
	}
	
	public static String commentsShowByMid(String token, String mid, int page, int count, Long sinceId) {
		String url = "https://api.weibo.com/2/comments/show.json?access_token="+token+"&id="+mid+"&page="+page+"&count="+count;
		if(null != sinceId) {
			url+="&since_id="+sinceId;
		}
		String str = OkHttpUtil.get(url);
		
		Loggers.RUNNING_LOG.info("url:{}, rs:{}", url, str);
		return str;
	}
	
	public static void dataHandle(String data) throws Exception {
		StringBuilder sbuf = new StringBuilder();
		JsonNode result = JsonUtil.readTree(data);
		JsonNode list = result.get("comments");
		Iterator<JsonNode> it = list.iterator();
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM ddHH:mm:ss Z yyyy",Locale.US);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		while(it.hasNext()) {
			JsonNode o = it.next();
			if(null != o.get("user")) {
				JsonNode u = o.get("user");
				sbuf.append(u.get("id").asLong()).append("\t");
				sbuf.append(StringUtil.getMD5(u.get("id").asText())).append("\t");
				sbuf.append(u.get("screen_name").asText()).append("\t");
				sbuf.append(u.get("province").asLong()).append("\t");
				sbuf.append(u.get("city").asLong()).append("\t");
				sbuf.append(u.get("location").asText()).append("\t");
				sbuf.append(u.get("description").asText()).append("\t");
				sbuf.append(u.get("gender").asText()).append("\t");
				sbuf.append(u.get("followers_count").asLong()).append("\t");
				sbuf.append(u.get("friends_count").asLong()).append("\t");
				sbuf.append(u.get("statuses_count").asLong()).append("\t");
				sbuf.append(u.get("favourites_count").asLong()).append("\t");
				sbuf.append(sdf1.format(sdf.parse(u.get("created_at").asText()))).append("\t");
				sbuf.append(u.get("verified").asBoolean()).append("\t");
				sbuf.append(u.get("verified_type").asText()).append("\t");
				sbuf.append(u.get("urank").asLong()).append("\t");
				sbuf.append(u.get("user_ability").asLong()).append("\t");
				sbuf.append("\n");
			}
			
		}
		FileUtil.writeFile("/Users/micarol/Documents/tmp/juzi_data/u_data", sbuf, true);
	}
}
