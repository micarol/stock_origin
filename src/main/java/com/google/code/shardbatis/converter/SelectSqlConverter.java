/**
 * 
 */
package com.google.code.shardbatis.converter;

import java.util.Iterator;

/**
 * @author sean.he
 * 
 */
public class SelectSqlConverter extends AbstractSqlConverter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.code.shardbatis.converter.AbstractSqlConverter#doConvert(net
	 * .sf.jsqlparser.statement.Statement, java.lang.Object, java.lang.String)
	 */
	@Override
	protected String doConvert(String optionType, String sql, final Object params, final String mapperId) {
		if (!(optionType.equals(SqlConverterFactory.SELECT))) {
			throw new IllegalArgumentException(
					"The argument statement must is instance of Select.");
		}
		return convertTableName2(sql, params, mapperId);
	}

}
