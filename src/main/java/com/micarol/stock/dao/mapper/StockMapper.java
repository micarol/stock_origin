package com.micarol.stock.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.micarol.stock.pojo.StockAlarmRec;
import com.micarol.stock.pojo.StockAlarmSetting;
import com.micarol.stock.pojo.StockPubNotice;

public interface StockMapper {

	public List<Map<String, Object>> allCodesForPubNotice();
	
	public int insertUpdatePubNotice(List<StockPubNotice> list);
	
	public int addStockAlarmSetting(@Param("setting") StockAlarmSetting setting);
	
	public int backupStockAlarmSetting(@Param("id") int id);
	
	public int delStockAlarmSetting(@Param("userid") int userid, @Param("id") int id);
	
	public List<StockAlarmSetting> listSettings();
	
	public int insertAlarmRec(List<StockAlarmRec> list);
}
