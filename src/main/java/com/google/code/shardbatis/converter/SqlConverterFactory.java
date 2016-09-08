/**
 * 
 */
package com.google.code.shardbatis.converter;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import com.google.code.shardbatis.ShardException;

/**
 * 管理各种CRUD语句的Converter
 * @author sean.he
 * 
 */
public class SqlConverterFactory {
	private static final Log log = LogFactory.getLog(SqlConverterFactory.class);
	
	public static final String SELECT = "SELECT";
	public static final String INSERT = "INSERT";
	public static final String UPDATE = "UPDATE";
	public static final String DELETE = "DELETE";

	private static SqlConverterFactory factory;
	static {
		factory = new SqlConverterFactory();
	}

	public static SqlConverterFactory getInstance() {
		return factory;
	}

	private Map<String, SqlConverter> converterMap;

	private SqlConverterFactory() {
		converterMap = new HashMap<String, SqlConverter>();
		register();
	}

	private void register() {
		converterMap.put(SqlConverterFactory.SELECT, new SelectSqlConverter());
//		converterMap.put(SqlConverterFactory.INSERT, new InsertSqlConverter());
//		converterMap.put(SqlConverterFactory.UPDATE, new UpdateSqlConverter());
//		converterMap.put(SqlConverterFactory.DELETE, new DeleteSqlConverter());
	}
	
	/**
	 * 修改sql语句
	 * @param sql
	 * @param params
	 * @param mapperId
	 * @return 修改后的sql
	 * @throws ShardException 解析sql失败会抛出ShardException
	 */
	public String convert(String sql, Object params, String mapperId)
			throws ShardException {
		
		SqlConverter converter = this.converterMap.get(SqlConverterFactory.SELECT);

		return converter.convert(SqlConverterFactory.SELECT, sql, params, mapperId);
		/*
		String optionType = "";			
		sql = sql.toUpperCase().trim();
		if(sql.indexOf(SqlConverterFactory.SELECT) == 0) {
			optionType = SqlConverterFactory.SELECT;
		} else if(sql.indexOf(SqlConverterFactory.INSERT) == 0) {
			optionType = SqlConverterFactory.INSERT;
		} else if(sql.indexOf(SqlConverterFactory.UPDATE) == 0) {
			optionType = SqlConverterFactory.UPDATE;
		} else if(sql.indexOf(SqlConverterFactory.DELETE) == 0) {
			optionType = SqlConverterFactory.DELETE;
		}			

		SqlConverter converter = this.converterMap.get(optionType);

		if (converter != null) {
			return converter.convert(optionType, sql, params, mapperId);
		}
		return sql;
		*/
	}
}
