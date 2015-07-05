/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import nuist.qlib.ccss.dao.ConnSQL;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * Excel成绩导出
 * 
 * @author Fang Wang
 * @since ccss 1.0
 */
public class ExcelManager {

	Connection conn;
	PreparedStatement st;
	boolean mark; // 用来表明是否正确取到配置信息

	/** 构造函数，读取数据库的配置，同时与数据库建立连接 */
	public ExcelManager() {
		ConnSQL connSql = new ConnSQL();
		conn = connSql.connectDataBase();
		mark = connSql.isConnected();
	}

	/** 判断是否连接到了数据库 */
	public boolean isCollected() {
		return mark;
	}

	public void close() {
		try {
			if (!conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/** 将成绩导出 ,参数分别为数据，文件名，比赛事件，比赛项目 */
	public boolean ExportForeExcel(List<HashMap<String, Object>> data,
			String fileName, String matchName, String category, int matchType,
			int matchKind) {
		String matchTypeString = "";
		if (matchType == 0) {
			matchTypeString = "预赛";
		} else if (matchType == 1) {
			matchTypeString = "决赛";
		}
		// 创建一个HSSFWorkbook
		HSSFWorkbook wb = new HSSFWorkbook();
		// 由HSSFWorkbook创建一个HSSFSheet
		HSSFSheet sheet = wb.createSheet("sheet1");
		sheet.setDefaultColumnWidth((short) 4);
		sheet.setColumnWidth((short) 18, (short) 1500);
		sheet.setColumnWidth((short) 20, (short) 1500);
		sheet.setMargin(HSSFSheet.TopMargin, (short) 1);
		sheet.setMargin(HSSFSheet.BottomMargin, (short) 0.8);
		HSSFPrintSetup printSetup = sheet.getPrintSetup();
		printSetup.setPaperSize((HSSFPrintSetup.A4_PAPERSIZE));
		printSetup.setLandscape(true);
		// 由HSSFSheet创建HSSFRow
		// 创建显示赛事的标题
		HSSFRow row0 = sheet.createRow((short) 0);
		row0.setHeight((short) 400);
		HSSFCell cell0 = row0.createCell((short) 0);
		cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell0.setCellValue(matchName);
		HSSFCellStyle matchNamestyle = wb.createCellStyle();
		matchNamestyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 设置字体样式
		HSSFFont matchNameFont = wb.createFont();
		matchNameFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		matchNameFont.setFontHeight((short) (15 * 20));
		matchNamestyle.setFont(matchNameFont);
		cell0.setCellStyle(matchNamestyle);
		for (int i = 1; i < 21; i++) {
			cell0 = row0.createCell((short) i);
			cell0.setCellStyle(matchNamestyle);
		}
		String[] params = getParams();
		// 将赛事标题所在行的第一列到第21列合并
		sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) 21));
		// 创建子标题(占第二行和第三行，15和16列)
		HSSFRow subRow = sheet.createRow(2);
		HSSFCellStyle subStyle = wb.createCellStyle();
		subStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont subFont = wb.createFont();
		subFont.setFontHeight((short) (12 * 15));
		subStyle.setFont(subFont);
		HSSFCellStyle subValueStyle = wb.createCellStyle();
		subValueStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		HSSFFont subValueFont = wb.createFont();
		subValueFont.setFontHeight((short) (12 * 15));
		subValueStyle.setFont(subValueFont);
		HSSFCell subCell = subRow.createCell((short) 1);
		subCell.setCellStyle(subStyle);
		subCell.setEncoding(HSSFCell.ENCODING_UTF_16);
		subCell.setCellValue("比赛地点:");
		subCell = subRow.createCell((short) 2);
		sheet.addMergedRegion(new Region(2, (short) 1, 2, (short) 2));
		subCell = subRow.createCell((short) 3);
		subCell.setCellStyle(subValueStyle);
		subCell.setCellValue(params[3]);
		subCell = subRow.createCell((short) 4);
		sheet.addMergedRegion(new Region(2, (short) 3, 2, (short) 4));
		subCell = subRow.createCell((short) 7);
		subCell.setCellStyle(subStyle);
		subCell.setEncoding(HSSFCell.ENCODING_UTF_16);
		subCell.setCellValue("比赛时间:");
		subCell = subRow.createCell((short) 8);
		sheet.addMergedRegion(new Region(2, (short) 7, 2, (short) 8));
		subCell = subRow.createCell((short) 9);
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		String time = format.format(new Date(System.currentTimeMillis()));
		subCell.setCellStyle(subStyle);
		subCell.setCellValue(time);
		subCell = subRow.createCell((short) 10);
		subCell = subRow.createCell((short) 11);
		sheet.addMergedRegion(new Region(2, (short) 9, 2, (short) 11));

		// 第3行的bottom上添加线条
		HSSFRow lineRow = sheet.createRow(3);
		HSSFCellStyle lineStyle = wb.createCellStyle();
		lineStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		HSSFCell lineCell;
		for (int i = 0; i < 22; i++) {
			lineCell = lineRow.createCell((short) i);
			lineCell.setCellStyle(lineStyle);
		}

		// 第4行设置参赛项目名称
		HSSFRow categoryRow = sheet.createRow(4);
		categoryRow.setHeight((short) 400);
		HSSFCellStyle categoryStyle = wb.createCellStyle();
		categoryStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont categoryFont = wb.createFont();
		categoryFont.setFontHeight((short) (14 * 15));
		categoryFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		categoryStyle.setFont(categoryFont);
		HSSFCell categoryCell = categoryRow.createCell((short) 5);
		categoryCell.setCellStyle(categoryStyle);
		categoryCell.setEncoding(HSSFCell.ENCODING_UTF_16);
		categoryCell.setCellValue(category + " " + matchTypeString);
		for (int i = 10; i < 14; i++) {
			categoryCell = categoryRow.createCell((short) i);
			categoryCell.setCellStyle(categoryStyle);
		}
		sheet.addMergedRegion(new Region(4, (short) 5, 4, (short) 14));

		// 设置人员签名占第5行
		HSSFRow sigRow = sheet.createRow(5);
		sigRow.setHeight((short) 300);
		HSSFCell sigCell = sigRow.createCell((short) 5);
		sigCell.setEncoding(HSSFCell.ENCODING_UTF_16);
		sigCell.setCellValue("仲裁主任:" + params[0]);
		sigCell = sigRow.createCell((short) 6);
		sigCell = sigRow.createCell((short) 7);
		sheet.addMergedRegion(new Region(5, (short) 5, 5, (short) 7));

		sigCell = sigRow.createCell((short) 9);
		sigCell.setEncoding(HSSFCell.ENCODING_UTF_16);
		sigCell.setCellValue("总裁判长:" + params[2]);
		sigCell = sigRow.createCell((short) 10);
		sigCell = sigRow.createCell((short) 11);
		sheet.addMergedRegion(new Region(5, (short) 9, 5, (short) 11));
		sigCell = sigRow.createCell((short) 13);
		sigCell.setEncoding(HSSFCell.ENCODING_UTF_16);
		sigCell.setCellValue("副裁判长:" + params[1]);
		sigCell = sigRow.createCell((short) 14);
		sigCell = sigRow.createCell((short) 15);
		sheet.addMergedRegion(new Region(5, (short) 13, 5, (short) 15));

		// 说明标题占第6行
		HSSFRow desRow = sheet.createRow(6);
		desRow.setHeight((short) 400);
		HSSFCellStyle desStyle = wb.createCellStyle();
		HSSFCell desCell;
		desStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		desStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		desStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		for (int i = 0; i < 22; i++) {
			desCell = desRow.createCell((short) i);
			desCell.setCellStyle(desStyle);
		}
		desCell = desRow.getCell((short) 1);
		desCell.setEncoding(HSSFCell.ENCODING_UTF_16);
		desCell.setCellValue("裁判分数");
		desCell = desRow.getCell((short) 3);
		desCell.setEncoding(HSSFCell.ENCODING_UTF_16);
		if (matchKind == 0) {
			desCell.setCellValue("J1 J2 J3 J4 J5 J6 J7 J8 J9");
		} else if (matchKind == 1) {
			desCell.setCellValue("J1 J2 J3 J4 J5 J6 J7 J8 J9 J10");
		}
		sheet.addMergedRegion(new Region(6, (short) 1, 6, (short) 2));
		sheet.addMergedRegion(new Region(6, (short) 3, 6, (short) 8));

		// 设置第7行
		HSSFRow NineRow = sheet.createRow(7);
		NineRow.setHeight((short) 50);

		// 总表头行，占第8行
		HSSFRow totalRow = sheet.createRow(8);
		totalRow.setHeight((short) 400);
		HSSFCellStyle totalStyle = wb.createCellStyle();
		HSSFCell totalCell;
		HSSFFont totalFont = wb.createFont();
		totalFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		totalStyle.setFont(totalFont);
		totalStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		totalStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		for (int i = 0; i < 22; i++) {
			totalCell = totalRow.createCell((short) i);
			totalCell.setCellStyle(totalStyle);
		}
		totalCell = totalRow.getCell((short) 0);
		totalCell.setEncoding(HSSFCell.ENCODING_UTF_16);
		totalCell.setCellValue("名次");
		totalCell = totalRow.getCell((short) 1);
		totalCell.setEncoding(HSSFCell.ENCODING_UTF_16);
		totalCell.setCellValue("姓名/单位");
		sheet.addMergedRegion(new Region(8, (short) 1, 8, (short) 2));

		// 创建excel的表头占第9行和第10行
		HSSFCellStyle style = wb.createCellStyle();
		totalFont = wb.createFont();
		totalFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(totalFont);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setWrapText(true);
		HSSFRow row = sheet.createRow((short) 9);
		row.setHeight((short) 350);
		HSSFCell cell = row.createCell((short) 17);
		cell.setCellStyle(style);
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("最后得分");
		style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		row = sheet.createRow((short) 10);
		cell = row.createCell((short) 1);
		cell.setCellStyle(style);
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("J1");
		cell = row.createCell((short) 2);
		cell.setCellStyle(style);
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("J2");
		cell = row.createCell((short) 3);
		cell.setCellStyle(style);
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("J3");
		cell = row.createCell((short) 4);
		cell.setCellStyle(style);
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("J4");
		cell = row.createCell((short) 5);
		cell.setCellStyle(style);
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("J5");
		cell = row.createCell((short) 7);
		cell.setCellStyle(style);
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("J6");
		cell = row.createCell((short) 8);
		cell.setCellStyle(style);
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("J7");
		cell = row.createCell((short) 9);
		cell.setCellStyle(style);
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("J8");
		cell = row.createCell((short) 10);
		cell.setCellStyle(style);
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("J9");
		if (matchKind == 1) {
			cell = row.createCell((short) 11);
			cell.setCellStyle(style);
			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue("J10");
		}

		cell = row.createCell((short) 13);
		cell.setCellStyle(style);
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("裁判长减分");

		cell = row.createCell((short) 15);
		cell.setCellStyle(style);
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("裁判长加分");

		cell = row.createCell((short) 17);
		cell.setCellStyle(style);
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("最后得分");
		sheet.addMergedRegion(new Region(9, (short) 17, 10, (short) 17));

		// 添加数据时的样式
		HSSFCellStyle datastyle = wb.createCellStyle();
		datastyle.setBorderBottom((short) 1);

		// /下面的是根据list 进行遍历循环
		int i = 11;
		int k = 1;
		int count = data.size();
		int left; // 总共的行数
		int pageSize; // 总共的页数
		int currentPage = 1; // 当前的页数
		if (count <= 8) {
			left = 39;
			pageSize = 1;
		} else {
			pageSize = (count - 8) % 12 == 0 ? (count - 8) / 12
					: (count - 8) / 12 + 1; // 页数
			left = 39 + pageSize * 41;
			pageSize = pageSize + 1;
		}
		// 脚页面的文字
		HSSFCellStyle footerStyle = wb.createCellStyle();
		footerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		for (; i < left; i++) {

			HashMap<String, Object> o = (HashMap<String, Object>) data
					.get(k - 1);

			row = sheet.getRow(i);
			if (row == null)
				row = sheet.createRow((short) i);
			for (int j = 0; j < 22; j++) {
				cell = row.getCell((short) j);
				if (cell == null)
					cell = row.createCell((short) j);
			}
			/* 创建参赛排名的单元格 */
			cell = row.getCell((short) 0);
			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if (o.get("rank") == null) {
				cell.setCellValue("");
			} else {
				cell.setCellValue(String.valueOf(o.get("rank")));
			}
			/* 创建参赛单位的单元格 */
			cell = row.getCell((short) 1);
			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if (o.get("teamName") == null) {
				cell.setCellValue("");
			} else {
				cell.setCellValue(String.valueOf(o.get("teamName"))
						+ String.valueOf(o.get("playerNames")));
			}
			sheet.addMergedRegion(new Region(i, (short) 1, i, (short) 21));
			i++;
			row = sheet.createRow((short) i);
			for (int j = 0; j < 22; j++) {
				cell = row.getCell((short) j);
				if (cell == null)
					cell = row.createCell((short) j);
				cell.setCellStyle(datastyle);
			}
			// 设置分数的单元格
			cell = row.getCell((short) 1);
			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if (o.get("score1") == null) {
				cell.setCellValue(0);
			} else {
				cell.setCellValue(String.valueOf(o.get("score1")));
			}
			cell = row.getCell((short) 2);
			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if (o.get("score2") == null) {
				cell.setCellValue(0);
			} else {
				cell.setCellValue(String.valueOf(o.get("score2")));
			}
			cell = row.getCell((short) 3);
			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if (o.get("score3") == null) {
				cell.setCellValue(0);
			} else {
				cell.setCellValue(String.valueOf(o.get("score3")));
			}
			cell = row.getCell((short) 4);
			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if (o.get("score4") == null) {
				cell.setCellValue(0);
			} else {
				cell.setCellValue(String.valueOf(o.get("score4")));
			}
			cell = row.getCell((short) 5);
			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if (o.get("score5") == null) {
				cell.setCellValue(0);
			} else {
				cell.setCellValue(String.valueOf(o.get("score5")));
			}
			cell = row.getCell((short) 7);
			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if (o.get("score6") == null) {
				cell.setCellValue(0);
			} else {
				cell.setCellValue(String.valueOf(o.get("score6")));
			}
			cell = row.getCell((short) 8);
			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if (o.get("score7") == null) {
				cell.setCellValue(0);
			} else {
				cell.setCellValue(String.valueOf(o.get("score7")));
			}
			cell = row.getCell((short) 9);
			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if (o.get("score8") == null) {
				cell.setCellValue(0);
			} else {
				cell.setCellValue(String.valueOf(o.get("score8")));
			}
			cell = row.getCell((short) 10);
			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if (o.get("score9") == null) {
				cell.setCellValue(0);
			} else {
				cell.setCellValue(String.valueOf(o.get("score9")));
			}
			if (matchKind == 1) {
				cell = row.getCell((short) 11);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				if (o.get("score10") == null) {
					cell.setCellValue(0);
				} else {
					cell.setCellValue(String.valueOf(o.get("score10")));
				}
			}

			/* 创建裁判长减分的单元格 */
			cell = row.getCell((short) 13);
			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if (o.get("sub_score") == null) {
				cell.setCellValue(0);
			} else {
				cell.setCellValue(String.valueOf(o.get("sub_score")));
			}

			/* 创建裁判长减分的单元格 */
			cell = row.getCell((short) 15);
			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if (o.get("add_score") == null) {
				cell.setCellValue(0);
			} else {
				cell.setCellValue(String.valueOf(o.get("add_score")));
			}

			/* 创建最后得分单元格 */
			cell = row.getCell((short) 17);
			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if (o.get("total") == null) {
				cell.setCellValue(0);
			} else {
				cell.setCellValue(new DecimalFormat("#.00").format(o
						.get("total")));
			}

			i++;
			if (k <= 8) {
				currentPage = 1;
			} else {
				currentPage = (k - 8) % 12 == 0 ? (k - 8) / 12 + 1
						: (k - 8) / 12 + 2;
			}
			if (k == data.size() && currentPage == 1 && data.size() <= 8) {
				i = i + (8 - data.size()) * 3;
				// 创造签名的行
				i = i + 1;
				HSSFRow rowLast = sheet.getRow(i);
				if (rowLast == null)
					rowLast = sheet.createRow((short) i);
				HSSFCell cellLast = rowLast.getCell((short) 17);
				if (cellLast == null)
					cellLast = rowLast.createCell((short) 17);
				cellLast.setEncoding(HSSFCell.ENCODING_UTF_16);
				cellLast.setCellValue("裁判长签名：");
				HSSFCellStyle style0 = wb.createCellStyle();
				HSSFFont font = wb.createFont();
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				font.setFontName("Times New Roman");
				font.setFontHeight((short) (15 * 15));
				style0.setFont(font);
				cellLast.setCellStyle(style0);
				i = i + 2;
				row = sheet.createRow((short) i);
				for (int j = 0; j < 22; j++) {
					cell = row.getCell((short) j);
					if (cell == null)
						cell = row.createCell((short) j);
				}
				cell = row.getCell((short) 0);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(category + "  " + time);
				cell.setCellStyle(footerStyle);
				sheet.addMergedRegion(new Region(i, (short) 0, i, (short) 21));
				i++;
			} else if (k == data.size() && currentPage == pageSize) {
				i = i + (8 + 12 * (currentPage - 1) - k) * 3;
				// 创造签名的行
				i = i + 2;
				HSSFRow rowLast = sheet.getRow(i);
				if (rowLast == null)
					rowLast = sheet.createRow((short) i);
				HSSFCell cellLast = rowLast.getCell((short) 17);
				if (cellLast == null)
					cellLast = rowLast.createCell((short) 17);
				cellLast.setEncoding(HSSFCell.ENCODING_UTF_16);
				cellLast.setCellValue("裁判长签名：");
				HSSFCellStyle style0 = wb.createCellStyle();
				HSSFFont font = wb.createFont();
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				font.setFontName("Times New Roman");
				font.setFontHeight((short) (15 * 15));
				style0.setFont(font);
				cellLast.setCellStyle(style0);
				i = i + 2;
				row = sheet.createRow((short) i);
				for (int j = 0; j < 22; j++) {
					cell = row.getCell((short) j);
					if (cell == null)
						cell = row.createCell((short) j);
				}
				cell = row.getCell((short) 0);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(category + "  " + time);
				cell.setCellStyle(footerStyle);
				sheet.addMergedRegion(new Region(i, (short) 0, i, (short) 21));
				i++;
			} else if ((k - 8) % 12 == 0 && currentPage < pageSize) { // 中间页
				i = i + 4;
				row = sheet.createRow((short) i);
				for (int j = 0; j < 22; j++) {
					cell = row.getCell((short) j);
					if (cell == null)
						cell = row.createCell((short) j);
				}
				cell = row.getCell((short) 0);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(category + "  " + time);
				cell.setCellStyle(footerStyle);
				sheet.addMergedRegion(new Region(i, (short) 0, i, (short) 21));
				i++;
			} // 第一页的脚底
			k++;
		}

		final File file = new File(fileName);
		try {

			OutputStream os = new FileOutputStream(file);
			try {
				if (!file.exists()) {
					file.createNewFile();
				}
				wb.write(os);
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param type
	 *            表示导入数据的类型，判断是附加数据,还是直接导入数据,0表示直接导入,1表示附加数据
	 * @return 0 表示获取数据库的配置信息不正确
	 * @return 1 表示excel文件的数据写错或者是文件名错误
	 * @return 2 表示导入数据库成功
	 * @return 3 表示导入数据库失败
	 * @return 4 表示程序出错
	 * @return 5 表示比赛已经开始，不可以重新入库
	 * @return 6 表示附加数据时，该赛事不存在，不可以附加
	 */
	public int importExcel(String path, int type) {
		if (mark == false) {
			return 0;
		} else
			try {
				POIFSFileSystem fs;
				HSSFWorkbook book;
				fs = new POIFSFileSystem(new FileInputStream(new File(path)));
				book = new HSSFWorkbook(fs);
				// 获得第一个工作表对象
				HSSFSheet sheet = book.getSheetAt(0);
				String temp = path.substring(path.lastIndexOf("\\") + 1,
						path.indexOf("."));
				// 赛事名称
				String location = temp.substring(0, temp.indexOf("_"));
				// 获得比赛类型
				String matchType = temp.substring(temp.indexOf("_") + 1);
				// 检验文件名是否正确
				if (matchType.equals("预赛")) {
					matchType = "0";
				} else if (matchType.equals("决赛")) {
					matchType = "1";
				} else
					return 1;
				// 插入数据的代码
				String sql;
				if (type == 0) {
					// 检验该文件中的比赛名称是否在数据库中已经存在，如果存在，查找团体分数表，判断是否有该赛事名称，如果有，通知用户不可更新，如果没有，删除团体信息表已有重复的数据的，重新入库
					String deleteSql = "delete from web_json where match_name=? and final_preliminary=?";
					String deleteMatchOrderSql = "delete from match_order where match_name=? and final_preliminary=?";
					String checkScore = "select b.* from match_order as a,score as b where a.id=b.team_id and a.match_name=?";
					String checkSql = "select * from match_order where match_name=? and final_preliminary=?";
					st = conn.prepareStatement(checkSql);
					st.setString(1, location);
					st.setString(2, matchType);
					ResultSet rs = st.executeQuery();
					// 已存在数据
					if (rs.next()) {
						// 判断打分表中是否已经有了这个数据，如果有的话提示用户，不能覆盖
						st = conn.prepareStatement(checkScore);
						st.setString(1, location);
						rs = st.executeQuery();
						if (rs.next()) {
							return 5; // 告知用户不能修改，已经进行了比赛
						} else {
							st = conn.prepareStatement(deleteMatchOrderSql);
							st.setString(1, location);
							st.setString(2, matchType);
							st.execute();
						}
					}
					st = conn.prepareStatement(deleteSql);
					st.setString(1, location);
					st.setString(2, matchType);
					st.execute();
				} else if (type == 1) { // 当附加数据时，先判断是否有该赛事名称，如果没有提醒用户
					String checkSql = "select * from web_json where match_name=? and final_preliminary=?";
					st = conn.prepareStatement(checkSql);
					st.setString(1, location);
					st.setString(2, matchType);
					ResultSet rs = st.executeQuery();
					if (!rs.next()) {
						return 6; // 提醒用户信息表中没有该赛事，不能够进行附加
					}
				}
				// 没有存在,直接将数据导入数据库
				sql = "insert into web_json (match_category,match_units,team_name,match_name,final_preliminary,sort_flag,player_name,coach_name,tb_name)values(?,?,?,?,?,?,?,?,?)";
				// 将数据直接插入的过程
				conn.setAutoCommit(false);
				st = conn.prepareStatement(sql);
				// 得到总行数
				int rowNum = sheet.getLastRowNum();
				HSSFRow row = sheet.getRow(0);
				// 正文内容应该从第二行开始,第一行为表头的标题
				for (int i = 1; i <= rowNum; i++) {
					row = sheet.getRow(i);
					// 参赛项目
					HSSFCell cell = row.getCell((short) 0);
					// 参赛单位
					HSSFCell cell2 = row.getCell((short) 1);
					// 队伍名称
					HSSFCell cell3 = row.getCell((short) 2);
					// 成员名单
					HSSFCell cell4 = row.getCell((short) 3);
					// 教练员名字
					HSSFCell cell5 = row.getCell((short) 4);
					// 替补人员名单
					HSSFCell cell6 = row.getCell((short) 5);
					if (cell == null || cell2 == null || cell3 == null
							|| cell4 == null) {
						return 1;
					} else {
						// 将第一列的数据放进st中
						st.setString(1, cell.getStringCellValue()); // 参赛项目
						// 将第二列的数据放进st中
						st.setString(2, cell2.getStringCellValue()); // 参赛单位
						st.setString(3, cell3.getStringCellValue()); // 队伍名称
						st.setString(4, location);
						st.setInt(5, Integer.valueOf(matchType));
						st.setInt(6, 0);
						st.setString(7, cell4.getStringCellValue()); // 姓名
						if (cell5 != null) {
							st.setString(8, cell5.getStringCellValue()); // 教练员名单
						} else {
							st.setString(8, null);
						}
						if (cell6 != null) {
							st.setString(9, cell6.getStringCellValue()); // 替补成员名单
						} else {
							st.setString(9, null);
						}

					}
					st.addBatch();
					if (i > 1000) {
						int[] result = st.executeBatch();
						for (int k : result) {
							if (k < 0) {
								return 3;
							}
						}
					}

				}
				int[] result = st.executeBatch();
				for (int k : result) {
					if (k < 0) {
						return 3;
					}
				}
				conn.commit();
				return 2;
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return 1;
			} catch (Exception e) {
				e.printStackTrace();
				return 4;
			}
	}

	/***
	 * 返回裁判等参数
	 * 
	 * @return params[0]表示仲裁主任，params[1]表示副裁判长，params[2]表示总裁判长，params[3]表示比赛地点
	 */
	public String[] getParams() {
		String[] params = new String[4];
		String sql = "select id,name,role,location from config order by id";
		try {
			st = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ResultSet res = st.executeQuery();
			int i = 0;
			while (res.next()) {
				params[i] = res.getString(2);
				i++;
			}
			res.first();
			params[3] = res.getString(4);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return params;
	}
}
