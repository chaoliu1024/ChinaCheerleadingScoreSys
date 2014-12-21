/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.util;

/**
 * 字符串操作相关工具
 * 
 * @author Chao Liu
 * @since ccss 1.0
 */
public class StringUtil {

	/**
	 * 冒泡排序法，对字符串数组进行排序，使数组中字符串长度由小到大
	 * 
	 * @param str
	 *            字符串数组
	 * @return 长度由小到大的字符串数组
	 */
	public static String[] bubbleSortArray(String[] str) {

		String temp = null;
		for (int i = str.length - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {
				if (str[j + 1].length() < str[j].length()) {
					temp = str[j];
					str[j] = str[j + 1];
					str[j + 1] = temp;
				}
			}
		}
		return str;
	}
}
