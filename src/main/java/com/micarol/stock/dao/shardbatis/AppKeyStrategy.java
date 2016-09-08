package com.micarol.stock.dao.shardbatis;

import java.util.Map;

import com.google.code.shardbatis.strategy.ShardStrategy;

public class AppKeyStrategy implements ShardStrategy {
	
	private static final int DIVID_NUMBER = 11;
	
	/**
	 * keyword_log wk_data_statuses wk_data_users分表策略
	 * @param params.get("appKey"); 根据此字段对数据分表
	 */
	public String getTargetTableName(String baseTableName, Object params, String mapperId) {
		Map map = (Map) params;
		Object uid = map.get("appKey");
		int tId = (int) (Long.parseLong(uid.toString())%AppKeyStrategy.DIVID_NUMBER);
		return baseTableName + "_" + tId;
	}

}
