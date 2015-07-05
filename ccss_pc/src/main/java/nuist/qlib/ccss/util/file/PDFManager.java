/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.util.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nuist.qlib.ccss.dao.ConfigDao;
import nuist.qlib.ccss.util.ChnAmt;
import nuist.qlib.ccss.util.DateTimeUtil;
import nuist.qlib.ccss.util.StringUtil;

import org.apache.log4j.Logger;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * PDF相关操作
 * 
 * @author Chao Liu
 * @author Fang Wang
 * @since ccss 1.0
 */
public class PDFManager {
	private String[] playName_Array = null;
	private String path = "D:/证书打印/";
	private String pdfName = null;
	private Logger logger;
	private String match_num = null;
	private String match_order = null;
	private String strRank = "";
	private ChnAmt chnAMT = null; // 阿拉伯数字转汉字
	private int year;
	private int month;

	public PDFManager() {
		logger = Logger.getLogger(PDFManager.class.getName());
	}

	public boolean createPDF(int matchNum, int matchOrder, String matchName,
			String teamName, String playName, String category, int rank) {

		if (playName != null) {
			playName_Array = playName.split(",");
		}

		if (teamName != null) {
			if (matchNum < 10)
				match_num = "0" + matchNum;
			if (matchOrder > 9 && matchOrder < 100)
				match_order = "0" + matchOrder;
			if (matchOrder < 10)
				match_order = "00" + matchOrder;
			pdfName = match_num + "-" + match_order + "-" + teamName + "-"
					+ playName_Array.length;
		}

		// 阿拉伯数字转汉字
		chnAMT = new ChnAmt(String.valueOf(rank));
		while (chnAMT.next()) {
			strRank += chnAMT.getResult();
		}

		year = DateTimeUtil.getSystemCurrentYear();
		month = DateTimeUtil.getSystemCurrentMonth();

		ConfigDao dao = new ConfigDao();
		String location = "";
		List<HashMap<String, Object>> data = dao.getParams();
		if (data.size() != 0) {
			location = data.get(0).get("location").toString();
		}
		String relativelyPath = System.getProperty("user.dir");

		try {

			// 创建一个pdf读入流
			PdfReader reader = new PdfReader(
					PDFManager.class.getResourceAsStream("/templet.pdf"));
			// PdfReader reader = new PdfReader(relativelyPath +
			// "\\templet.pdf");

			createDir(path);

			// 根据一个pdfreader创建一个pdfStamper.用来生成新的pdf.
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
					path + pdfName + ".pdf"));

			// 设置字体,使用simsun.ttc字体样式
			BaseFont bf = BaseFont.createFont(relativelyPath
					+ "\\src\\main\\resources\\simsun.ttc,1",
					BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			// BaseFont bf = BaseFont.createFont(
			// relativelyPath + "\\simsun.ttc,1", BaseFont.IDENTITY_H,
			// BaseFont.EMBEDDED);

			Font font = new Font(bf, 10, Font.BOLD);
			font.getBaseFont();

			// 页数是从1开始的
			for (int i = 1; i <= reader.getNumberOfPages(); i++) {

				// 获得pdfstamper在当前页的上层打印内容.这些内容会覆盖在原先的pdf内容之上.
				PdfContentByte over = stamper.getOverContent(i);
				// 用PdfReader获得当前页字典对象.包含了该页的一些数据.比如该页的坐标轴信息.
				PdfDictionary p = reader.getPageN(i);

				// 拿到mediaBox里面放着该页pdf的大小信息.
				PdfObject po = p.get(new PdfName("MediaBox"));

				// po是一个数组对象.里面包含了该页pdf的坐标轴范围.
				PdfArray pa = (PdfArray) po;

				// y轴的最大值.
				float y = Float.parseFloat(pa.getAsNumber(pa.size() - 1)
						.toString());

				// 开始写入文本
				over.beginText();

				// 赛事名称位置、字体和大小
				over.setFontAndSize(font.getBaseFont(), 16); // 三号字体

				float matchName_Y = y * 5 / 11 - 14; // 主标题的y轴坐标

				// 设置主标题
				float matchName_X = calMatchNameStart(matchName.split("暨")[0]);
				over.setTextMatrix(matchName_X, matchName_Y);
				over.showText(matchName.split("暨")[0]);

				String[] temp = matchName.split("暨");
				if (temp.length == 2) {
					String subMatchName = temp[1];
					// 副标题
					float sub_matchName_X = calTeamNameStart(subMatchName);
					over.setTextMatrix(sub_matchName_X, matchName_Y - 22);
					over.showText(subMatchName);
				}

				// 参赛队伍位置、字体和大小
				over.setFontAndSize(font.getBaseFont(), 14); // 四号字体

				float teamName_Y = y * 3 / 7 - 36;
				float teamName_X = calTeamNameStart(teamName);
				over.setTextMatrix(teamName_X, teamName_Y);
				over.showText(teamName);

				// 运动员姓名位置
				float playName_X = 193f;
				float playName_Y = y / 3 + 14;

				StringUtil.bubbleSortArray(playName_Array); // 排序运动员姓名

				for (int j = 0; j < playName_Array.length; j++) {

					over.setTextMatrix(playName_X, playName_Y);

					if (playName_Array[j].length() == 2) {
						String firstName = playName_Array[j].substring(0, 1);
						String lastName = playName_Array[j].substring(1, 2);
						playName_Array[j] = firstName + "  " + lastName;
					}

					over.showText(playName_Array[j]);
					playName_X = playName_X + 60;
					if ((j + 1) % 6 == 0) { // 四个人名换行
						playName_Y = playName_Y - 15;
						playName_X = 193f;
					}
				}

				// 组别、名次字体和大小
				over.setFontAndSize(font.getBaseFont(), 15); // 小三号字体

				// 组别位置
				float category_X = calCategoryStart(category);
				float category_Y = y * 1 / 4 - 15;
				over.setTextMatrix(category_X, category_Y);
				over.showText(category);

				// 名次位置
				float rank_Y = y * 1 / 4 - 40;
				String rankStirng = "第" + strRank + "名";
				float rank_X = calCategoryStart(rankStirng);
				over.setTextMatrix(rank_X, rank_Y);
				over.showText(rankStirng);

				// 时间地点位置
				float time_Y = y / 6 - 10;
				location = year + "年" + month + "月" + "    " + location;
				float time_X = calCategoryStart(location);
				over.setTextMatrix(time_X, time_Y);
				over.showText(location);

				over.endText();
			}
			stamper.close();

			return true;
		} catch (IOException | DocumentException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	/***
	 * 打印优秀教练员证书
	 * 
	 * @param matchName
	 * @param teamName
	 * @param coachName
	 * @param category
	 * @return
	 */
	public boolean createCoachPDF(String matchName, String teamName,
			String coachName) {
		if (teamName != null) {
			pdfName = teamName + "-" + coachName;
		}

		year = DateTimeUtil.getSystemCurrentYear();
		month = DateTimeUtil.getSystemCurrentMonth();

		ConfigDao dao = new ConfigDao();
		String location = "";
		List<HashMap<String, Object>> data = dao.getParams();
		if (data.size() != 0) {
			location = data.get(0).get("location").toString();
		}
		String relativelyPath = System.getProperty("user.dir");

		try {
			// 创建一个pdf读入流
			// PdfReader reader = new PdfReader(relativelyPath +
			// "\\templet.pdf");
			PdfReader reader = new PdfReader(
					PDFManager.class.getResourceAsStream("/templet.pdf"));

			createDir(path);

			// 根据一个pdfreader创建一个pdfStamper.用来生成新的pdf.
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
					path + pdfName + ".pdf"));

			// 设置字体,使用simsun.ttc字体样式
			BaseFont bf = BaseFont.createFont(relativelyPath
					+ "\\src\\main\\resources\\simsun.ttc,1",
					BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			// BaseFont bf = BaseFont.createFont(
			// relativelyPath + "\\simsun.ttc,1", BaseFont.IDENTITY_H,
			// BaseFont.EMBEDDED);

			Font font = new Font(bf, 10, Font.BOLD);
			font.getBaseFont();

			// 页数是从1开始的
			for (int i = 1; i <= reader.getNumberOfPages(); i++) {

				// 获得pdfstamper在当前页的上层打印内容.这些内容会覆盖在原先的pdf内容之上.
				PdfContentByte over = stamper.getOverContent(i);
				// 用PdfReader获得当前页字典对象.包含了该页的一些数据.比如该页的坐标轴信息.
				PdfDictionary p = reader.getPageN(i);

				// 拿到mediaBox里面放着该页pdf的大小信息.
				PdfObject po = p.get(new PdfName("MediaBox"));

				// po是一个数组对象.里面包含了该页pdf的坐标轴范围.
				PdfArray pa = (PdfArray) po;

				// y轴的最大值.
				float y = Float.parseFloat(pa.getAsNumber(pa.size() - 1)
						.toString());

				// 开始写入文本
				over.beginText();

				// 赛事名称位置、字体和大小
				over.setFontAndSize(font.getBaseFont(), 16); // 三号字体

				float matchName_Y = y * 5 / 11 - 14; // 主标题的y轴坐标

				// 设置主标题
				float matchName_X = calMatchNameStart(matchName.split("暨")[0]);
				over.setTextMatrix(matchName_X, matchName_Y);
				over.showText(matchName.split("暨")[0]);

				String[] temp = matchName.split("暨");
				if (temp.length == 2) {
					String subMatchName = temp[1];
					// 副标题
					float sub_matchName_X = calTeamNameStart(subMatchName);
					over.setTextMatrix(sub_matchName_X, matchName_Y - 22);
					over.showText(subMatchName);
				}

				// 参赛队伍位置、字体和大小
				over.setFontAndSize(font.getBaseFont(), 14); // 四号字体

				// 运动员姓名位置
				float coachName_Y = y / 3 - 14;

				if (coachName.length() == 2) {
					String firstName = coachName.substring(0, 1);
					String lastName = coachName.substring(1, 2);
					coachName = firstName + "  " + lastName;
				}
				float coachName_X = calTeamNameStart(coachName);
				over.setTextMatrix(coachName_X, coachName_Y);
				over.showText(coachName);

				// 组别、名次字体和大小
				over.setFontAndSize(font.getBaseFont(), 15); // 小三号字体

				// 名次位置
				float rank_Y = y * 1 / 4 - 40;
				String rankStirng = "优秀教练员";
				float rank_X = calCategoryStart(rankStirng);
				over.setTextMatrix(rank_X, rank_Y);
				over.showText(rankStirng);

				// 时间地点位置
				float time_Y = y / 6 - 10;
				location = year + "年" + month + "月" + "    " + location;
				float time_X = calCategoryStart(location);
				over.setTextMatrix(time_X, time_Y);
				over.showText(location);

				over.endText();
			}
			stamper.close();

			return true;
		} catch (IOException | DocumentException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 新建文件夹
	 * 
	 * @param path
	 *            文件夹路径
	 * @throws
	 */
	public void createDir(String path) {

		File dirFile = null;
		try {
			dirFile = new File(path);
			if (!(dirFile.exists()) && !(dirFile.isDirectory())) {
				boolean creadok = dirFile.mkdirs();
				if (creadok) {
					System.out.println("创建文件夹" + path + "成功");
				} else {
					System.out.println("创建文件夹" + path + "失败");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * 计算赛事名称的起始位置，可以容纳17个汉字
	 * 
	 * @param matchName
	 * @return
	 */
	public float calMatchNameStart(String matchName) {
		float charWidth = 16; // 一个汉字宽13,汉字之间的间距为3
		float start = 193; // 打印在pdf中的开始位置
		float end = 462; // 打印在pdf中的结束位置
		int count = calChar(matchName); // 汉字个数
		float finalStart = 0;
		finalStart = (end - start - (charWidth * (count - 1) + 13)) / 2 + start;
		return finalStart;
	}

	/***
	 * 计算队伍名称的起始位置，可以容纳19个汉字
	 * 
	 * @param matchName
	 * @return
	 */
	public float calTeamNameStart(String teamName) {
		float charWidth = 14; // 一个汉字宽12,汉字之间的间距为2
		float start = 193; // 打印在pdf中的开始位置
		float end = 462; // 打印在pdf中的结束位置
		int count = calChar(teamName); // 汉字个数
		float finalStart = 0;
		finalStart = (end - start - (charWidth * (count - 1) + 12)) / 2 + start;
		return finalStart;
	}

	/***
	 * 计算队伍名称的起始位置，可以容纳18个汉字
	 * 
	 * @param matchName
	 * @return
	 */
	public float calCategoryStart(String category) {
		float charWidth = 15; // 一个汉字宽12,汉字之间的间距为3
		float start = 193; // 打印在pdf中的开始位置
		float end = 462; // 打印在pdf中的结束位置
		int count = calChar(category); // 汉字个数
		float finalStart = 0;
		finalStart = (end - start - (charWidth * (count - 1) + 12)) / 2 + start;
		return finalStart;
	}

	/***
	 * 计算字符串中汉字的个数(两个非汉字算一个汉字)
	 * 
	 * @param str
	 * @return
	 */
	public int calChar(String str) {
		int count = 0;
		String regEx = "[\\u4e00-\\u9fa5]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		while (m.find()) {
			for (int i = 0; i <= m.groupCount(); i++) {
				count = count + 1;
			}
		}
		count = (str.length() - count) % 2 == 0 ? ((str.length() - count) / 2 + count)
				: ((str.length() - count) / 2 + count + 1);
		return count;
	}
}
