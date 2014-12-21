/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.util;

import java.text.DecimalFormat;

/**
 * 阿拉伯数字转换为汉字
 * 
 * @since ccss 1.0
 */
public class ChnAmt {
	private String[] saChnAmount = new String[50];
	private int iArrayLen = 0;
	private int iArrayPos = 0;

	/**
	 * 构造-小写转换大写对象(不带参)
	 * 
	 * @param String
	 *            sAmount
	 */
	public ChnAmt(String sAmount) {
		int iDecFrac = 0;

		if (sAmount.indexOf(".") != -1) {
			iDecFrac = sAmount.length() - sAmount.indexOf(".");
			if (iDecFrac > 3) {
				iDecFrac = 3;
			}
			sAmount = sAmount.substring(0, sAmount.indexOf("."))
					+ sAmount.substring(sAmount.indexOf(".") + 1,
							sAmount.indexOf(".") + iDecFrac);
			for (; iDecFrac < 3; iDecFrac++) {
				sAmount += "0";
			}
		} else {
			sAmount += "00";
		}

		toChnAmt(sAmount, 2, false);
	}

	/**
	 * 小写转换大写
	 * 
	 * @param String
	 *            sAmount
	 * @param int iDec
	 * @param boolean bDot
	 */
	public void toChnAmt(String sAmount, int iDec, boolean bDot) {
		// 数字
		final String[] sUserCode = { "零", "一", "二", "三", "四", "五", "六", "七",
				"八", "九" };

		// 单位
		final String[] sChnUnit = { "", "", "", "", "十", "百", "千", "万", "十",
				"百", "千", "亿", "十", "百", "千", "万" };

		// 标志
		// '9'代表十
		// '3' - '元', '1' - '万'( 如遇前一生成字符为'亿'，则不生成'万' ), '3' - '亿'
		// '0' - 其他
		final byte[] byFlag = { 0, 0, 0, 0, 9, 0, 0, 1, 9, 0, 0, 0, 9, 0, 0, 0 };

		int iCounter, iPos;
		byte byPreFlag = -1;
		char cDigit, cPreDigit = ' ';

		iArrayLen = 0;

		switch (iDec) {
		// 补零
		case 0:
			sAmount += "0";
		case 1:
			sAmount += "0";
		case 2:
			sAmount += "0";
		case 3:
			break;
		default:
			sAmount = sAmount.substring(0, sAmount.length() - iDec + 3);
		}

		if (bDot) {
			// 转换小数点
			sAmount = sAmount.substring(0, sAmount.length() - 4)
					+ sAmount.substring(sAmount.length() - 3);
		}

		// 开始转换
		for (iCounter = sAmount.length(); iCounter > 0; iCounter--) {
			// 计算数字位置
			iPos = sAmount.length() - iCounter;
			// 获得数字字符
			cDigit = sAmount.charAt(iPos);

			// 过滤数字
			if (cDigit < '0' || cDigit > '9')
				cDigit = '0';

			if (cDigit != '0') {
				// 当前数字不为'0'
				if (cPreDigit == '0') {
					// 前一数字为'0'
					if (byPreFlag != 3 && byPreFlag != 1) {
						// '亿'，'万'，'元' 后不加 '零'
						saChnAmount[++iArrayLen] = sUserCode[0];
					}
				}

				if (cDigit == '1' && byFlag[iCounter - 1] == 9
						&& iArrayLen == 0) {
					// 数字为'1'，单位为'十'，且为第一个数字时，不报'1'，只报'十'
					saChnAmount[++iArrayLen] = sChnUnit[iCounter - 1];
				} else {
					// 生成数字
					saChnAmount[++iArrayLen] = sUserCode[Character.digit(
							cDigit, 10)];
					// 生成单位
					saChnAmount[++iArrayLen] = sChnUnit[iCounter - 1];

					// 记录当前位标志
					byPreFlag = byFlag[iCounter - 1];
				}
			} else {
				// 数字为'0'
				if (iArrayLen == 0) {
					// 过滤首位'0'
					continue;
				} else {
					if (byFlag[iCounter - 1] == 3
							|| (byFlag[iCounter - 1] == 1 && byPreFlag != 3 && !saChnAmount[iArrayLen]
									.equals(sChnUnit[11]))) {
						// 生成'亿'，'万'，'元'的单位，但如果从'千万'位到'万'位都为0，则不生成'万'
						saChnAmount[++iArrayLen] = sChnUnit[iCounter - 1];
					}
					// 记录当前位标志
					byPreFlag = byFlag[iCounter - 1];
				}
			}

			// 记录当前数字
			cPreDigit = cDigit;
		}

		if (iArrayLen == 0) {
			// 转换结束数组长度为0，生成'0元整'
			saChnAmount[++iArrayLen] = sUserCode[0];
			saChnAmount[++iArrayLen] = sChnUnit[3];
		}

	}

	/**
	 * 判断是否还有下一个转换元素
	 * 
	 * @return boolean
	 */
	public boolean next() {
		boolean bRtn = false;

		if (iArrayPos++ < iArrayLen) {
			bRtn = true;
		}
		return bRtn;
	}

	/**
	 * 获取当前转换元素
	 * 
	 * @return String
	 */
	public String getResult() {
		String sRtn = "";

		sRtn += saChnAmount[iArrayPos];

		return sRtn;
	}

	/**
	 * 转换double到String
	 * 
	 * @param double dMoney
	 * @return String
	 */
	public static String Double2String(double dMoney) {
		String sMoney = "";

		DecimalFormat df = new DecimalFormat("############");
		try {
			sMoney = df.format(dMoney);
		} catch (Exception e) {
			sMoney = "";
		}

		return sMoney;
	}
}
