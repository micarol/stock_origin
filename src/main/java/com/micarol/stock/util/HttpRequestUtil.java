package com.micarol.stock.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

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
	
	public static String get1(String uri) throws Exception{

		URL url = new URL(uri);
	    HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("183.30.204.154", 9999)));
	    String ua = UserAgent.getAgents();
	    urlConnection.setRequestProperty("User-Agent", ua);
	    urlConnection.setRequestMethod("GET");
	    urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
	    urlConnection.setRequestProperty("Content-Language", "en-US");
	    urlConnection.setUseCaches(false);
	    urlConnection.setDoInput(true);
	    urlConnection.setDoOutput(true);
//	    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
//	    out.println("");
//	    out.flush();
	    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"utf-8")); // 获取输入流
	    String line = null;
	    StringBuilder sb = new StringBuilder();
	    while ((line = br.readLine()) != null) {
	        sb.append(line + "\n");
	    }

	    return sb.toString();
	}

	public static void main(String[] args) throws Exception {
//		String uri1 = "https://ad.uniscrm.cn";
//		String str1 = HttpRequestUtil.get1(uri1);
//		System.out.println(str1);
		//140.143.238.110
//		String uri = "http://127.0.0.1:6000/custom/fb?token=测试";
//		String str = HttpRequestUtil.get1(uri);
//		System.out.println(str);
		
		String s = "http://127.0.0.1:6000/custom/fb?token=10ffea123cbc49c395d3abc62b388542&p=74472864070b716907c17aedcc53d7d2&t=1&t=12342342";
		String url = URLEncoder.encode(s, "UTF-8" );
		System.out.println(url);
		
	}
}
