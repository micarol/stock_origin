package com.micarol.stock.pojo;

import java.io.Serializable;
import java.util.Date;

import com.micarol.stock.util.JsonUtil;

public class StockAlarmSetting implements Serializable {

	private static final long serialVersionUID = 3785337596079327472L;
	
	private int id;
	private int userid;
	private String code;
	private String keyword;
	private String email;
	private int notice;
	private Date createTime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getNotice() {
		return notice;
	}
	public void setNotice(int notice) {
		this.notice = notice;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Override
	public String toString(){
		return JsonUtil.obj2JsonStr(this);
	}
	
}
