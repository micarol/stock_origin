package com.micarol.stock.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class HttpRequestUtil {

	public static String get(String uri) throws Exception{

	    URL url = new URL(uri);
	    URLConnection urlConnection = url.openConnection();                                                    // 打开连接
	    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"utf-8")); // 获取输入流
	    String line = null;
	    StringBuilder sb = new StringBuilder();
	    while ((line = br.readLine()) != null) {
	        sb.append(line + "\n");
	    }

	    return sb.toString();
	}

	public static void main(String[] args) throws Exception {
		String uri = "http://open.weibo.com/wiki/2/friendships/groups/timeline";
		HttpRequestUtil.get(uri);
	}
}
