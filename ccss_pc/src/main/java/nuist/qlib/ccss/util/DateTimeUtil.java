/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.util;

import java.util.Calendar;

/**
 * TODO
 * 
 * @author Chao Liu
 * @since ccss 1.0
 */
public class DateTimeUtil {
	private static Calendar cal = Calendar.getInstance();

	/**
	 * 得到系统当前年份
	 * 
	 * @return
	 */
	public static int getSystemCurrentYear() {
		return cal.get(Calendar.YEAR);
	}

	/**
	 * 得到系统当前月份
	 * 
	 * @return
	 */
	public static int getSystemCurrentMonth() {
		return cal.get(Calendar.MONTH) + 1;
	}
}
