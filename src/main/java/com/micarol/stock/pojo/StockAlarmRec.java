package com.micarol.stock.pojo;

import java.io.Serializable;
import java.util.Date;

import com.micarol.stock.util.JsonUtil;

public class StockAlarmRec implements Serializable {

	private static final long serialVersionUID = -7181228999792078270L;
	
	private long id;
	private int alarmid;
	private String code;
	private String keyword;
	private String uniKey;
	private Date createTime;
	
	public StockAlarmRec(){}
	
	public StockAlarmRec(long id, int alarmid, String code, String keyword, String uniKey, Date createTime) {
		super();
		this.id = id;
		this.alarmid = alarmid;
		this.code = code;
		this.keyword = keyword;
		this.uniKey = uniKey;
		this.createTime = createTime;
	}	
			
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getAlarmid() {
		return alarmid;
	}
	public void setAlarmid(int alarmid) {
		this.alarmid = alarmid;
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
	public String getUniKey() {
		return uniKey;
	}
	public void setUniKey(String uniKey) {
		this.uniKey = uniKey;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Override
	public String toString() {
		return JsonUtil.obj2JsonStr(this);
	}
	
	public static void main(String[] args) {
		StockAlarmRec rec = new StockAlarmRec();
		rec.setId(1);
		rec.setCode("300369");
		rec.setKeyword("te哈哈");
		String s = rec.toString();
		System.out.println(s);
		StockAlarmRec rec1 = JsonUtil.jsonStr2Obj(s, StockAlarmRec.class);
		System.out.println(rec1.toString());
	}
}
