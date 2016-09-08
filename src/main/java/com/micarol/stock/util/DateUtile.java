package com.micarol.stock.util;

public class DateUtile {

	/**
	 * eg: 2016-08-01 转换成20160801返回
	 * @param date
	 * @return
	 */
	public static int dateStrToInt(String date) {
		return Integer.parseInt(date.replace("-", ""));
	}
}
