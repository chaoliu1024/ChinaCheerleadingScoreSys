/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.manager.team;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * TODO
 * 
 * @author Fang Wang
 * @since ccss 1.0
 */
public class TableModel {
	/**
	 * 获取选中行
	 * 
	 * @param table
	 * 
	 * @return
	 */
	public List<HashMap<String, Object>> getRow(Table table) {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		TableItem item[] = table.getItems();// 获取所有行
		if (item == null) // 判断是否有选中行
			return null;
		else {
			for (int i = 0; i < item.length; i++) {
				if (item[i].getChecked()) {
					HashMap<String, Object> one = new HashMap<String, Object>();
					one.put("match_order", item[i].getText(0));// 序号
					one.put("team_name", item[i].getText(2));// 队伍名称
					one.put("match_category", item[i].getText(1));// 比赛项目
					one.put("ID", item[i].getText(3));
					one.put("match_name", item[i].getText(4));
					one.put("final_preliminary", item[i].getText(5));
					one.put("player_name", item[i].getText(6));
					one.put("tb_name", item[i].getText(7));
					one.put("match_units", item[i].getText(8));
					list.add(one);
				}
			}
		}
		return list;
	}

	/**
	 * 获取所有行
	 * 
	 * @param table
	 * 
	 * @return
	 */
	public List<HashMap<String, Object>> getAllRow(Table table) {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		TableItem item[] = table.getItems();// 获取所有行
		if (item == null) // 判断是否有选中行
			return null;
		else {
			for (int i = 0; i < item.length; i++) {
				HashMap<String, Object> one = new HashMap<String, Object>();
				one.put("match_order", item[i].getText(0));// 序号
				one.put("team_name", item[i].getText(2));// 队伍名称
				one.put("match_category", item[i].getText(1));// 比赛项目
				one.put("ID", item[i].getText(3));
				one.put("match_name", item[i].getText(4));
				one.put("final_preliminary", item[i].getText(5));
				one.put("player_name", item[i].getText(6));
				one.put("tb_name", item[i].getText(7));
				one.put("match_units", item[i].getText(8));
				list.add(one);
			}
		}
		return list;
	}

	/**
	 * 增加行
	 * 
	 * @param table
	 * @param list
	 * 
	 * @return
	 */
	public void addRow(Table table, List<HashMap<String, Object>> list) {
		// int count = table.getItemCount();//获取表格中原有的行数
		for (int i = 0; i < list.size(); i++) {// 将查询结果显示在表格中
			TableItem item = new TableItem(table, SWT.NONE);
			String match_order = Integer.toString(i + 1);// 给行编号
			String team_name = list.get(i).get("team_name").toString();
			String match_category = list.get(i).get("match_category")
					.toString();
			String ID = list.get(i).get("ID").toString();
			String match_name = list.get(i).get("match_name").toString();
			String final_preliminary = list.get(i).get("final_preliminary")
					.toString();
			String player_name = list.get(i).get("player_name").toString();
			String tu_name = list.get(i).get("tb_name") == null ? "" : list
					.get(i).get("tb_name").toString();
			String match_units = list.get(i).get("match_units").toString();
			item.setText(new String[] { match_order, match_category, team_name,
					ID, match_name, final_preliminary, player_name, tu_name,
					match_units });
		}
	}

	/**
	 * 删除选中的行
	 * 
	 * @param table
	 */
	public void removeRow(Table table) {
		TableItem item[] = table.getItems();// 获取所有行
		if (item != null) { // 判断是否有选中行
			for (int i = item.length - 1; i >= 0; i--) {
				if (item[i].getChecked()) {
					table.remove(i);
				}
			}
		}
	}

	/**
	 * 选中指定的行
	 * 
	 * @param table
	 * @param index
	 */
	public void selectRow(Table table, int index[]) {
		for (int i = 0; i < index.length; i++) {
			if (index[i] > -1 && index[i] < table.getItemCount()) {// 判断是够越界
				table.getItem(index[i]).setChecked(true);
				;
			}
		}
	}

	/**
	 * 选中所有行
	 * 
	 * @param table
	 */
	public void selectAllRow(Table table) {
		TableItem item[] = table.getItems();// 获取所有行
		for (int j = 0; j < item.length; j++) {
			item[j].setChecked(true);
		}
	}

	/***
	 * 取消所有行的选中
	 * 
	 * @param table
	 */
	public void clearAllRow(Table table) {
		TableItem item[] = table.getItems();// 获取所有行
		for (int j = 0; j < item.length; j++) {
			item[j].setChecked(false);
		}
	}

	/**
	 * 将table中的数据到到Excel中
	 * 
	 * @param title
	 * @param table
	 * @return
	 */
	public Boolean tableToExcel(String title, Table table, String fileName) {
		// create a workbook
		HSSFWorkbook wb = new HSSFWorkbook();

		// add a worksheet
		HSSFSheet sheet = wb.createSheet("比赛出场顺序");
		HSSFPrintSetup printSetup = sheet.getPrintSetup();
		printSetup.setPaperSize((HSSFPrintSetup.A4_PAPERSIZE));
		printSetup.setLandscape(true);
		sheet.setColumnWidth((short) 0, (short) 1500);
		sheet.setColumnWidth((short) 1, (short) (1500 * 4));
		sheet.setColumnWidth((short) 2, (short) (1500 * 3));
		sheet.setColumnWidth((short) 3, (short) (1500 * 10));
		sheet.setColumnWidth((short) 4, (short) (1500 * 3));
		int rowIndex = 0;
		int cellIndex = 0;

		// 设置表格的标题
		HSSFCellStyle titleStyle = wb.createCellStyle();// 设置标题样式
		HSSFFont font = wb.createFont();
		font.setFontName("黑体");
		font.setFontHeightInPoints((short) 16);// 设置字体大小
		titleStyle.setFont(font);
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		HSSFRow tableTitle = sheet.createRow((short) rowIndex++);
		sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) 3));// 合并第一行的前四列
		HSSFCell cellTitle = tableTitle.createCell((short) 0);
		cellTitle.setCellValue(title);
		cellTitle.setCellStyle(titleStyle);

		HSSFCellStyle headerStyle = wb.createCellStyle();
		headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		headerStyle.setWrapText(true);

		// add header row
		TableColumn[] columns = table.getColumns();
		HSSFRow header = sheet.createRow((short) rowIndex++);
		for (TableColumn column : columns) {
			String columnName = column.getText();
			if (columnName.equals("序号") || columnName.equals("队伍名称")
					|| columnName.equals("参赛者") || columnName.equals("替补人员")
					|| columnName.equals("比赛项目") || columnName.equals("备用单位")) {
				HSSFCell cell = header.createCell((short) cellIndex++);
				cell.setCellValue(column.getText());
				cell.setCellStyle(headerStyle);
			}
		}

		// add data rows
		List<HashMap<String, Object>> items = getAllRow(table);
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setWrapText(true);
		for (int i = 0; i < items.size(); i++) {// 遍历行
			HashMap<String, Object> item = items.get(i);
			// create a new row
			HSSFRow row = sheet.createRow((short) rowIndex++);
			cellIndex = 0;
			// create a new cell
			String match_order = item.get("match_order").toString();
			HSSFCell cell = row.createCell((short) cellIndex++);
			// set the cell's value
			cell.setCellValue(match_order);
			cell.setCellStyle(cellStyle);
			String team_name = item.get("match_category").toString();
			HSSFCell cell2 = row.createCell((short) cellIndex++);
			// set the cell's value
			cell2.setCellValue(team_name);
			cell2.setCellStyle(cellStyle);
			String match_category = item.get("team_name").toString();
			HSSFCell cell3 = row.createCell((short) cellIndex++);
			// set the cell's value
			cell3.setCellValue(match_category);
			cell3.setCellStyle(cellStyle);
			String player_name = item.get("player_name").toString();
			HSSFCell cell4 = row.createCell((short) cellIndex++);

			cell4.setCellValue(player_name);
			cell4.setCellStyle(cellStyle);
			String tibu_name = item.get("tb_name").toString();
			HSSFCell cell5 = row.createCell((short) cellIndex++);
			// set the cell's value
			cell5.setCellValue(tibu_name);
			cell5.setCellStyle(cellStyle);
			String match_units = item.get("match_units").toString();
			HSSFCell cell6 = row.createCell((short) cellIndex++);
			// set the cell's value
			cell6.setCellValue(match_units);
			cell6.setCellStyle(cellStyle);
		}
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(fileName);
			wb.write(fileOut);
			fileOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
