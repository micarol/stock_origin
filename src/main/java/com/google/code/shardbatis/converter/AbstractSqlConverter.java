/**
 * 
 */
package com.google.code.shardbatis.converter;

import java.util.List;

import com.google.code.shardbatis.builder.ShardConfigHolder;
import com.google.code.shardbatis.strategy.ShardStrategy;

/**
 * @author sean.he
 * 
 */
public abstract class AbstractSqlConverter implements SqlConverter {

	public String convert(String optionType, String sql, Object params, String mapperId) {
		return doConvert(optionType, sql, params, mapperId);
	}
	
	/**
	 * 从ShardConfigFactory中查找ShardStrategy并对表名进行修改<br>
	 * 如果没有相应的ShardStrategy则对表名不做修改
	 * 
	 * @param tableName
	 * @param params
	 * @param mapperId
	 * @return
	 */
	protected String convertTableName(String tableName, Object params, String mapperId) {
		ShardConfigHolder configFactory = ShardConfigHolder.getInstance();
		ShardStrategy strategy = configFactory.getStrategy(tableName);
		if (strategy == null) {
			return tableName;
		}
		return strategy.getTargetTableName(tableName, params, mapperId);
	}
	
	protected String convertTableName2(String sql, Object params, String mapperId) {
		ShardConfigHolder configFactory = ShardConfigHolder.getInstance();
		List<String> tableNames = configFactory.getStrategyTable();
		String tableName;
		for(int i=0; i<tableNames.size(); i++) {
			tableName = tableNames.get(i);
			if(sql.toLowerCase().indexOf(" " + tableName + " ") != -1) {
				ShardStrategy strategy = configFactory.getStrategy(tableName);
				if (strategy == null) {
					continue;
				}
				String targetName = strategy.getTargetTableName(tableName, params, mapperId);
				sql = sql.toLowerCase().replaceAll(" " + tableName + " ", " " + targetName + " ");
			}
		}
		return sql;
	}
	
	/**
	 * 修改statement代表的sql语句
	 * @param sql
	 * @param params
	 * @param mapperId
	 * @return
	 * 2013-02-18 Statement change String
	 */	
	protected abstract String doConvert(String optionType, String sql, Object params, String mapperId);
}
