package com.micarol.stock.pojo;

import java.io.Serializable;
import java.util.Date;

import com.micarol.stock.util.JsonUtil;

public class StockPubNotice implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String uniKey;
	private String code;
	private String title;
	private String link;
	private String date;
	private Date postTime;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUniKey() {
		return uniKey;
	}
	public void setUniKey(String uniKey) {
		this.uniKey = uniKey;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Date getPostTime() {
		return postTime;
	}
	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}
	
	@Override
	public String toString(){
		return JsonUtil.obj2JsonStr(this);
	}
	
	
	public static void main(String[] args) {
		String str = "aaa#notify#bbb";
		String[] arr = str.split("#notify#");
		System.out.println("len:"+arr.length);
	}
}
