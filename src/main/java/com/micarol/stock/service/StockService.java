package com.micarol.stock.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.micarol.stock.dao.mapper.StockMapper;
import com.micarol.stock.pojo.StockAlarmSetting;
import com.micarol.stock.pojo.StockPubNotice;
import com.micarol.stock.util.DateUtile;
import com.micarol.stock.util.Loggers;
import com.micarol.stock.util.spider.EastMoneySpider;

@Service
public class StockService {

	@Autowired
	private StockMapper stockMapper;
	
	private String NOTICE_URL = "http://data.eastmoney.com/notice/{STOCK_CODE}.html";
	
	public void pubNoticeSpider() {
		List<Map<String, Object>> codeMap = stockMapper.allCodesForPubNotice();
		EastMoneySpider spider = new EastMoneySpider();
		List<StockPubNotice> tempList = new ArrayList<>();
		int rs = 0;
		for (Map<String, Object> map : codeMap) {
			Loggers.RUNNING_LOG.info("code:{}, date:{}", map.get("code"), map.get("date"));
			String url = NOTICE_URL.replace("{STOCK_CODE}", String.valueOf(map.get("code")));
			List<StockPubNotice> list = spider.getPubNoticeList(url, String.valueOf(map.get("code")), DateUtile.dateStrToInt(String.valueOf(map.get("date"))));
			if(null != list) {
				tempList.addAll(list);
			}
			if(tempList.size() >= 1000) {
				rs += stockMapper.insertUpdatePubNotice(tempList);
				tempList.clear();
			}
		}
		if(tempList.size() > 0) {
			rs += stockMapper.insertUpdatePubNotice(tempList);
		}
		Loggers.RUNNING_LOG.info("pubNoticeSpider effect rows:{}", rs);
	}
	
	public StockAlarmSetting addStockAlarmSetting(StockAlarmSetting setting) {
		if(stockMapper.addStockAlarmSetting(setting) > 0) {
			return setting;
		}
		return null;
	}
	
	@Transactional
	public int backupStockAlarmSetting(int id) {
		if(stockMapper.backupStockAlarmSetting(id) > 0) {
			return this.backupStockAlarmSetting(id);
		}
		return 0;
	}
	
	public int delStockAlarmSetting(int userid, int id) {
		return stockMapper.delStockAlarmSetting(userid, id);
	}
}
