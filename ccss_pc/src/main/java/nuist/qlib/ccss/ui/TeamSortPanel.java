/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nuist.qlib.ccss.dao.MatchOrderDao;
import nuist.qlib.ccss.dao.Web_JsonDao;
import nuist.qlib.ccss.manager.team.TableModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * 出场顺序排序界面设计
 * 
 * @author Zhengfei Chen
 * @since ccss 1.0
 */
public class TeamSortPanel extends Composite {
	private static TeamSortPanel panel;
	private Table totalTeams;
	private Table sortTeams;
	private Label sortTeams_title;
	private Combo is_Final;
	private Combo match_Name;
	private Combo categorySelect;
	private Combo orderSelect;
	private Combo sort_combo;
	private Boolean isFinal;
	private String matchName;
	private List<HashMap<String, Object>> totalTeamsList;
	private int matchNum = 1;
	private boolean isSaved = true;
	private boolean totalTeamsIsSelected = false;
	private boolean sortTeamsIsSelected = false;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public TeamSortPanel(final Shell shell, final Composite parent,
			final int style) {
		super(parent, style);
		this.setBounds(0, 0, 970, 663);
		setLayout(null);

		totalTeams = new Table(this, SWT.BORDER | SWT.CHECK
				| SWT.FULL_SELECTION | SWT.MULTI);
		totalTeams.setBounds(10, 53, 410, 496);
		totalTeams.setHeaderVisible(true);
		totalTeams.setLinesVisible(true);

		totalTeams.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (totalTeams.getSelection().length != 0) {
					TableItem item = totalTeams.getSelection()[0];
					if (item == null) // 判断该表格中是否有该行
						return;
					if (item.getChecked())
						item.setChecked(false);
					else
						item.setChecked(true);
					totalTeams.deselectAll();
				}
			}

		});
		TableColumn id = new TableColumn(totalTeams, SWT.LEFT); // 列名(队伍id)
		TableColumn match_category = new TableColumn(totalTeams, SWT.LEFT);// 列名(比赛项目)
		TableColumn team_name = new TableColumn(totalTeams, SWT.LEFT); // 列名(队伍名称)
		TableColumn web_id = new TableColumn(totalTeams, SWT.LEFT); // 列名(网站队伍id)
		TableColumn match_name = new TableColumn(totalTeams, SWT.LEFT);// 列名(赛事名称)
		TableColumn final_preliminary = new TableColumn(totalTeams, SWT.LEFT);// 列名(决赛/预赛)
		TableColumn player_name = new TableColumn(totalTeams, SWT.LEFT);// 列名(参赛运动员)
		TableColumn tibu_name = new TableColumn(totalTeams, SWT.LEFT);// 列名(替补运动员)
		TableColumn unit_name = new TableColumn(totalTeams, SWT.LEFT);// 列名(备用单位)
		id.setText("序号");
		team_name.setText("队伍名称");
		match_category.setText("比赛项目");
		web_id.setText("网站队伍ID");
		match_name.setText("赛事名称");
		final_preliminary.setText("决赛/预赛");
		player_name.setText("参赛者");
		tibu_name.setText("替补人员");
		unit_name.setText("备用单位");
		id.setWidth(55);
		team_name.setWidth(400);
		match_category.setWidth(200);
		web_id.setWidth(0);
		match_name.setWidth(0);
		final_preliminary.setWidth(0);
		player_name.setWidth(0);
		tibu_name.setWidth(0);
		unit_name.setWidth(0);

		sortTeams = new Table(this, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION
				| SWT.MULTI);
		sortTeams.setBounds(444, 53, 510, 496);
		sortTeams.setHeaderVisible(true);
		sortTeams.setLinesVisible(true);
		sortTeams.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (sortTeams.getSelection().length != 0) {
					TableItem item = sortTeams.getSelection()[0];
					if (item == null) // 判断该表格中是否有该行
						return;
					if (item.getChecked())
						item.setChecked(false);
					else
						item.setChecked(true);
					sortTeams.deselectAll();
				}
			}

		});
		TableColumn id2 = new TableColumn(sortTeams, SWT.LEFT); // 列名(队伍名称)
		TableColumn match_category2 = new TableColumn(sortTeams, SWT.LEFT);// 列名(比赛项目)
		TableColumn team_name2 = new TableColumn(sortTeams, SWT.LEFT); // 列名(队伍名称)
		TableColumn web_id2 = new TableColumn(sortTeams, SWT.LEFT); // 列名(网站队伍id)
		TableColumn match_name2 = new TableColumn(sortTeams, SWT.LEFT);// 列名(赛事名称)
		TableColumn final_preliminary2 = new TableColumn(sortTeams, SWT.LEFT);// 列名(决赛/预赛)
		TableColumn player_name2 = new TableColumn(sortTeams, SWT.LEFT);// 列名(参赛运动员)
		TableColumn tibu_name2 = new TableColumn(sortTeams, SWT.LEFT);// 列名(替补运动员)
		TableColumn unit_name2 = new TableColumn(sortTeams, SWT.LEFT);// 列名(备用单位)
		id2.setText("序号");
		team_name2.setText("队伍名称");
		match_category2.setText("比赛项目");
		web_id2.setText("网站队伍ID");
		match_name2.setText("赛事名称");
		final_preliminary2.setText("决赛/预赛");
		player_name2.setText("参赛者");
		tibu_name2.setText("替补人员");
		unit_name2.setText("备用单位");
		id2.setWidth(55);
		team_name2.setWidth(400);
		match_category2.setWidth(200);
		web_id2.setWidth(0);
		match_name2.setWidth(0);
		final_preliminary2.setWidth(0);
		player_name2.setWidth(0);
		tibu_name2.setWidth(0);
		unit_name2.setWidth(0);

		Label totalTeams_title = new Label(this, SWT.NONE);
		totalTeams_title.setForeground(SWTResourceManager
				.getColor(SWT.COLOR_BLUE));
		totalTeams_title.setFont(SWTResourceManager.getFont("微软雅黑", 12,
				SWT.NORMAL));
		totalTeams_title.setBounds(141, 26, 112, 21);
		totalTeams_title.setText("未排序队伍列表");

		sortTeams_title = new Label(this, SWT.NONE);
		sortTeams_title.setText("第 " + matchNum + " 场出场顺序");
		sortTeams_title.setForeground(SWTResourceManager
				.getColor(SWT.COLOR_BLUE));
		sortTeams_title.setFont(SWTResourceManager.getFont("微软雅黑", 12,
				SWT.NORMAL));
		sortTeams_title.setBounds(636, 26, 120, 21);

		Button selectTotalTeamsAll = new Button(this, SWT.NONE);
		selectTotalTeamsAll.setBounds(10, 26, 53, 27);
		selectTotalTeamsAll.setText("全选");
		selectTotalTeamsAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableModel tableModel = new TableModel();
				if (!totalTeamsIsSelected) {
					tableModel.selectAllRow(totalTeams);
					totalTeamsIsSelected = true;
				} else {
					tableModel.clearAllRow(totalTeams);
					totalTeamsIsSelected = false;
				}

			}
		});

		Button importTeams = new Button(this, SWT.NONE);
		importTeams.setBounds(250, 565, 80, 27);
		importTeams.setText("导入参赛队伍");
		importTeams.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (isSaved) {// 判断排序表是否保存。如保存了则可继续导入新的未排序表
					matchNum = 1;
					Web_JsonDao web_JsonDao = new Web_JsonDao();
					MatchOrderDao match_OrderDao = new MatchOrderDao();
					List<HashMap<String, Object>> sortTeamsListTemp = new ArrayList<HashMap<String, Object>>();
					TableModel tableModel = new TableModel();
					categorySelect.removeAll(); // 清空
					orderSelect.removeAll(); // 清空
					/* 导入未排序表 */
					totalTeamsList = web_JsonDao
							.getWeb_Json(isFinal, matchName);// 获取参赛队伍
					totalTeams.removeAll();// 清空表格
					tableModel.addRow(totalTeams, totalTeamsList);// 将查询记录添加到表格中
					/* 导入该场次第一场场次表 */
					sortTeamsListTemp = match_OrderDao.getMatch_order(isFinal,
							matchName, matchNum);
					sortTeams.removeAll();// 清空表格
					sortTeams_title.setText("第 " + matchNum + " 场出场顺序");// 更新排序表标题
					tableModel.addRow(sortTeams, sortTeamsListTemp);// 将查询记录添加到表格中
				} else {
					MessageBox mb = new MessageBox(shell, SWT.NONE);
					mb.setText("提示");
					mb.setMessage("本场比赛名单未保存，请先保存该名单！");
					mb.open();
				}
			}
		});

		Button nextSesson = new Button(this, SWT.NONE);
		nextSesson.setText("安排下一场次");
		nextSesson.setBounds(869, 610, 80, 27);
		nextSesson.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox mb = new MessageBox(shell, SWT.NONE);
				mb.setText("提示");
				// 判断当前表中是否有队伍以及下一场次是否已排序，若没有则不能进入下一场排序
				TableModel tableModel = new TableModel();
				MatchOrderDao match_OrderDao = new MatchOrderDao();
				List<HashMap<String, Object>> sortTeamsList1 = new ArrayList<HashMap<String, Object>>();
				List<HashMap<String, Object>> sortTeamsList2 = new ArrayList<HashMap<String, Object>>();
				sortTeamsList1 = tableModel.getAllRow(sortTeams);// 获取当前表格中已排序表
				if (sortTeamsList1.size() == 0) {
					mb.setMessage("未对本场比赛进行排序，不能进入下一场！");
					mb.open();
				} else {
					if (isSaved) {// 判断是否保存
						matchNum++;
						sortTeams_title.setText("第 " + matchNum + " 场出场顺序");// 更新排序表标题
						sortTeams.removeAll();// 清空排序表
						sortTeamsList2 = match_OrderDao.getMatch_order(isFinal,
								matchName, matchNum);// 获取本场次已排序的队伍
						if (sortTeamsList2.size() != 0) {
							tableModel.addRow(sortTeams, sortTeamsList2);// 将查询结果显示在表格中
							isSaved = true;// 置为保存
						} else {
							isSaved = false;// 置为未保存
						}
					} else {
						mb.setMessage("本场比赛名单未保存，请先保存该名单！");
						mb.open();
					}
				}
			}
		});

		Button saveList = new Button(this, SWT.NONE);
		saveList.setText("保存出场名单");
		saveList.setBounds(762, 565, 80, 27);
		saveList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox mb = new MessageBox(shell, SWT.NONE);
				mb.setText("保存提示");
				TableModel tableModel = new TableModel();
				List<HashMap<String, Object>> sortTeamsList = new ArrayList<HashMap<String, Object>>();
				List<HashMap<String, Object>> totalTeamsListTemp = new ArrayList<HashMap<String, Object>>();
				sortTeamsList = tableModel.getAllRow(sortTeams);// 获取已排序表
				if (sortTeamsList.size() == 0 && isSaved) {
					mb.setMessage("未对本场比赛进行排序！");
					mb.open();
				} else if (!isSaved) {// 未保存
					for (int i = 0; i < sortTeamsList.size(); i++) {// 给每条记录增加两个字段
						sortTeamsList.get(i).put("match_num", matchNum);
						sortTeamsList.get(i).put("unit_status", false);
					}
					MatchOrderDao match_OrderDao = new MatchOrderDao();
					Web_JsonDao web_JsonDao = new Web_JsonDao();
					totalTeamsListTemp = tableModel.getAllRow(totalTeams);// 获取未排序表中现有的队伍
					if (match_OrderDao.deleteMatch_order(isFinal, matchName,
							matchNum)
							&& match_OrderDao.insertMatch_Order(sortTeamsList)
							&& web_JsonDao.updateSortFlag(totalTeamsList, true)
							&& web_JsonDao.updateSortFlag(totalTeamsListTemp,
									false)) {// 先删除本场次的历史记录，再插入新的排序
						mb.setMessage("该名单保存成功！");
						mb.open();
						isSaved = true;// 置为保存
					} else {
						mb.setMessage("该名单保存失败！");
						mb.open();
					}
				} else {// 已保存
					mb.setMessage("该名单已保存");
					mb.open();
				}
			}
		});

		Button addTeams = new Button(this, SWT.NONE);
		addTeams.setBounds(340, 565, 80, 27);
		addTeams.setText(">>");
		addTeams.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableModel tableModel = new TableModel();
				List<HashMap<String, Object>> totalTeamsListTemp = new ArrayList<HashMap<String, Object>>();
				totalTeamsListTemp = tableModel.getAllRow(totalTeams);// 获取未排序表中现有的队伍
				if (totalTeamsListTemp.size() == 0) {
					return;
				}
				List<HashMap<String, Object>> sortTeamsListTemp = new ArrayList<HashMap<String, Object>>();
				sortTeamsListTemp = tableModel.getAllRow(sortTeams);// 获取排序表中现有的队伍
				sortTeamsListTemp.addAll(tableModel.getRow(totalTeams));// 获取排序表中现有的队伍并将正要加入的队伍连接到后面，方便总体排序
				sortTeams.removeAll();// 清空排序表
				tableModel.addRow(sortTeams, sortTeamsListTemp);// 将全部排好的队伍重新加入排序表中
				tableModel.removeRow(totalTeams);// 清除未排序表中选中的队伍
				totalTeamsListTemp.clear();// 请客list
				totalTeamsListTemp = tableModel.getAllRow(totalTeams);// 获取未排序表中现有的队伍
				totalTeams.removeAll();// 清空未排序表
				tableModel.addRow(totalTeams, totalTeamsListTemp);// 将全部未排序的队伍重新加入未排序表中
				isSaved = false;// 置为未保存
			}
		});

		Button removeTeams = new Button(this, SWT.NONE);
		removeTeams.setBounds(444, 565, 80, 27);
		removeTeams.setText("<<");
		removeTeams.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableModel tableModel = new TableModel();
				List<HashMap<String, Object>> sortTeamsListTemp = new ArrayList<HashMap<String, Object>>();
				sortTeamsListTemp = tableModel.getAllRow(sortTeams);// 获取排序表中现有的队伍
				if (sortTeamsListTemp.size() == 0) {// 没有排序
					return;
				}
				List<HashMap<String, Object>> totalTeamsListTemp = new ArrayList<HashMap<String, Object>>();
				totalTeamsListTemp = tableModel.getAllRow(totalTeams);// 获取排序表中现有的队伍
				totalTeamsListTemp.addAll(tableModel.getRow(sortTeams));// 获取排序表中现有的队伍并将正要加入的队伍连接到后面，方便总体排序
				totalTeams.removeAll();// 清空未排序表
				tableModel.addRow(totalTeams, totalTeamsListTemp);
				tableModel.removeRow(sortTeams);// 清除排序表中选中的队伍
				sortTeamsListTemp.clear();// 清空list
				sortTeamsListTemp = tableModel.getAllRow(sortTeams);// 获取排序表中现有的队伍
				sortTeams.removeAll();// 清空排序表
				tableModel.addRow(sortTeams, sortTeamsListTemp);// 将全部排序的队伍重新加入排序表中
				isSaved = false;// 置为未保存
			}
		});

		Button backSession = new Button(this, SWT.NONE);
		backSession.setBounds(869, 565, 80, 27);
		backSession.setText("返回上一场");
		backSession.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox mb = new MessageBox(shell, SWT.NONE);
				mb.setText("提示");
				// 判断当前表中是否有队伍，若没有则表示未进行排序，不能进入下一场排序
				TableModel tableModel = new TableModel();
				List<HashMap<String, Object>> sortTeamsList = new ArrayList<HashMap<String, Object>>();
				sortTeamsList = tableModel.getAllRow(sortTeams);// 获取已排序表
				if (sortTeamsList.size() != 0 && !isSaved) {// 判断是否保存
					mb.setMessage("本场比赛名单未保存，请先保存该名单！");
					mb.open();
				} else {
					matchNum--;
					if (matchNum == 0) {// 已到第一场
						mb.setMessage("已到第一场！");
						mb.open();
					} else {
						sortTeams_title.setText("第 " + matchNum + " 场出场顺序");// 更新排序表标题
						sortTeams.removeAll();// 清空排序表
						MatchOrderDao match_OrderDao = new MatchOrderDao();
						sortTeamsList = match_OrderDao.getMatch_order(isFinal,
								matchName, matchNum);// 获取该场次已排序的队伍
						tableModel.addRow(sortTeams, sortTeamsList);// 将查询结果显示在表格中
						isSaved = true;// 置为保存
					}
				}
			}
		});

		is_Final = new Combo(this, SWT.READ_ONLY);
		is_Final.setBounds(153, 567, 88, 25);
		is_Final.add("预赛");
		is_Final.add("决赛");
		is_Final.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Web_JsonDao web_JsonDao = new Web_JsonDao();
				List<HashMap<String, Object>> match_nameList = new ArrayList<HashMap<String, Object>>();
				match_Name.removeAll();
				if (sort_combo.getText().trim().length() == 0) {
					sort_combo.setFocus();
					MessageBox box = new MessageBox(shell, SWT.None);
					box.setMessage("请选择已排序或者未排序");
					box.setText("提示");
					box.open();
				} else {
					int sort = 0;
					if (sort_combo.getText().equals("已排序")) {
						sort = 1;
					}
					if (is_Final.getText().equals("预赛")) {
						isFinal = false;
						match_nameList = web_JsonDao.getMatch_Name(isFinal,
								sort);
					} else {
						isFinal = true;
						match_nameList = web_JsonDao.getMatch_Name(isFinal,
								sort);
					}
				}
				for (int i = 0; i < match_nameList.size(); i++) {
					match_Name.add(match_nameList.get(i).get("match_name")
							.toString());
				}
			}
		});

		match_Name = new Combo(this, SWT.READ_ONLY);
		match_Name.setBounds(153, 610, 88, 25);
		match_Name.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				matchName = match_Name.getText();
			}
		});

		Label label_category = new Label(this, SWT.NONE);
		label_category.setBounds(86, 570, 61, 17);
		label_category.setText("赛事模式：");

		Label label_name = new Label(this, SWT.NONE);
		label_name.setBounds(86, 610, 61, 17);
		label_name.setText("赛事名称：");

		Button up = new Button(this, SWT.NONE);
		up.setText("上调");
		up.setBounds(548, 565, 53, 27);
		up.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableModel tableModel = new TableModel();
				List<HashMap<String, Object>> sortTeamsList = new ArrayList<HashMap<String, Object>>();
				List<HashMap<String, Object>> sortTeamsListTemp = new ArrayList<HashMap<String, Object>>();
				sortTeamsList = tableModel.getAllRow(sortTeams);// 获取排序表中所有的队伍
				sortTeamsListTemp = tableModel.getRow(sortTeams);// 获取排序表中选中的队伍
				int length = sortTeamsListTemp.size();
				int index[] = new int[length];
				for (int i = 0; i < sortTeamsListTemp.size(); i++) {
					HashMap<String, Object> curOne = new HashMap<String, Object>();
					HashMap<String, Object> preOne = new HashMap<String, Object>();
					curOne = sortTeamsListTemp.get(i);
					int curOrder = Integer.valueOf(curOne.get("match_order")
							.toString());// 取出选中队伍的出场序号
					if (curOrder == 1) {// 若当前序号为1，则不上调
						index[i] = curOrder - 1;
					} else {
						index[i] = curOrder - 2;// 上调后的list内部编号
						preOne = sortTeamsList.get(curOrder - 2);// 前一个队伍
						curOne.put("match_order", curOrder - 1);// 当前队伍序号上调一个
						preOne.put("match_order", curOrder);// 前一个队伍序号增加一个
						sortTeamsList.remove(curOrder - 2);// 删除前一支队伍
						sortTeamsList.add(curOrder - 1, preOne);// 将前一个删除的队伍重新加入list中
					}
				}
				sortTeams.removeAll();// 清空排序表
				tableModel.addRow(sortTeams, sortTeamsList);// 将全部排好的队伍重新加入排序表中
				tableModel.selectRow(sortTeams, index);
				isSaved = false;// 置为未保存
			}
		});

		Button down = new Button(this, SWT.NONE);
		down.setBounds(610, 565, 53, 27);
		down.setText("下调");
		down.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableModel tableModel = new TableModel();
				List<HashMap<String, Object>> sortTeamsList = new ArrayList<HashMap<String, Object>>();
				List<HashMap<String, Object>> sortTeamsListTemp = new ArrayList<HashMap<String, Object>>();
				sortTeamsList = tableModel.getAllRow(sortTeams);// 获取排序表中所以的队伍
				sortTeamsListTemp = tableModel.getRow(sortTeams);// 获取排序表中选中的队伍
				int length = sortTeamsListTemp.size();
				int totalLength = sortTeamsList.size();
				int index[] = new int[length];
				for (int i = sortTeamsListTemp.size() - 1; i >= 0; i--) {
					HashMap<String, Object> curOne = new HashMap<String, Object>();
					HashMap<String, Object> latOne = new HashMap<String, Object>();
					curOne = sortTeamsListTemp.get(i);// 当前选中队伍
					int curOrder = Integer.valueOf(curOne.get("match_order")
							.toString());// 取出选中队伍的出场序号
					if (curOrder == totalLength) {// 若当前序号为最后一个，则不下调
						index[i] = totalLength - 1;
					} else {
						index[i] = curOrder;
						latOne = sortTeamsList.get(curOrder);// 后一个队伍
						curOne.put("match_order", curOrder + 1);// 当前队伍序号下调一个
						latOne.put("match_order", curOrder);// 后一个队伍序号上调一个
						sortTeamsList.remove(curOrder - 1);// 删除当前队伍
						sortTeamsList.add(curOrder, curOne);// 将当前队伍上调一个位置
					}
				}
				sortTeams.removeAll();// 清空排序表
				tableModel.addRow(sortTeams, sortTeamsList);// 将全部排好的队伍重新加入排序表中
				tableModel.selectRow(sortTeams, index);
				isSaved = false;// 置为未保存

			}
		});

		Label orderLabel = new Label(this, SWT.NONE);
		orderLabel.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
		orderLabel.setBounds(835, 30, 36, 17);
		orderLabel.setText("场次：");

		Button export = new Button(this, SWT.NONE);
		export.setBounds(762, 610, 80, 27);
		export.setText("导出到Excel");
		export.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox mb = new MessageBox(shell, SWT.NONE);
				mb.setText("保存提示");
				TableModel tableModel = new TableModel();
				List<HashMap<String, Object>> sortTeamsList = new ArrayList<HashMap<String, Object>>();
				sortTeamsList = tableModel.getAllRow(sortTeams);// 获取已排序表
				if (sortTeamsList.size() == 0) {
					mb.setMessage("未对本场比赛进行排序！");
					mb.open();
					return;
				} else if (!isSaved) {
					mb.setMessage("本场比赛名单未保存，请先保存该名单！");
					mb.open();
					return;
				}
				FileDialog dialog = new FileDialog(shell, SWT.SAVE);// 保存对话框
				dialog.setFilterExtensions(new String[] { "*.xls", "*.xlsx" });
				dialog.setFilterNames(new String[] { "Excel文件(*.xls)",
						"Excel文件(*.xlsx)" });
				String fileName = dialog.open(); // 获取选择的保存路径
				if (fileName == null || fileName.equals("")) {
					return;
				}
				String title = sortTeams_title.getText();// 获取表格标题
				tableModel.tableToExcel(title, sortTeams, fileName);
			}
		});
		orderSelect = new Combo(this, SWT.READ_ONLY);
		orderSelect.setBounds(876, 27, 78, 21);
		orderSelect.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (isFinal != null && matchName != null) {
					MatchOrderDao match_OrderDao = new MatchOrderDao();
					String matchNum[] = match_OrderDao.getMatchNum(isFinal,
							matchName);
					orderSelect.removeAll();
					orderSelect.setItems(matchNum);
				} else {
					MessageBox mb = new MessageBox(shell, SWT.None);
					mb.setMessage("请先选择赛事模式和赛事名称");
					return;
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
			}
		});
		orderSelect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox mb = new MessageBox(shell, SWT.NONE);
				mb.setText("提示");
				// 判断当前表中是否有队伍，若没有则表示未进行排序，不能进入下一场排序
				TableModel tableModel = new TableModel();
				List<HashMap<String, Object>> sortTeamsList = new ArrayList<HashMap<String, Object>>();
				sortTeamsList = tableModel.getAllRow(sortTeams);// 获取已排序表
				if (sortTeamsList.size() != 0 && !isSaved) {// 判断是否保存
					mb.setMessage("本场比赛名单未保存，请先保存该名单！");
					mb.open();
				} else {
					matchNum = Integer.parseInt(orderSelect.getItem(orderSelect
							.getSelectionIndex()));// 选择的出场顺序
					sortTeams_title.setText("第 " + matchNum + " 场出场顺序");// 更新排序表标题
					sortTeams.removeAll();// 清空排序表
					MatchOrderDao match_OrderDao = new MatchOrderDao();
					sortTeamsList = match_OrderDao.getMatch_order(isFinal,
							matchName, matchNum);// 获取该场次已排序的队伍
					tableModel.addRow(sortTeams, sortTeamsList);// 将查询结果显示在表格中
					isSaved = true;// 置为保存
				}
			}
		});

		Label categoryLabel = new Label(this, SWT.NONE);
		categoryLabel.setFont(SWTResourceManager
				.getFont("微软雅黑", 10, SWT.NORMAL));
		categoryLabel.setBounds(290, 30, 36, 17);
		categoryLabel.setText("组别：");

		categorySelect = new Combo(this, SWT.READ_ONLY);
		categorySelect.setBounds(332, 27, 88, 25);

		sort_combo = new Combo(this, SWT.READ_ONLY);
		sort_combo.setBounds(10, 567, 70, 25);
		sort_combo.add("未排序");
		sort_combo.add("已排序");
		sort_combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				is_Final.removeAll();
				is_Final.add("预赛");
				is_Final.add("决赛");
				isFinal = null;
				matchName = "";
			}
		});

		Button selectSortTeamsAll = new Button(this, SWT.NONE);
		selectSortTeamsAll.setBounds(444, 26, 53, 27);
		selectSortTeamsAll.setText("全选");
		selectSortTeamsAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableModel tableModel = new TableModel();
				if (!sortTeamsIsSelected) {
					tableModel.selectAllRow(sortTeams);
					sortTeamsIsSelected = true;
				} else {
					tableModel.clearAllRow(sortTeams);
					sortTeamsIsSelected = false;
				}

			}
		});

		categorySelect.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (isFinal != null && matchName != null) {
					Web_JsonDao web_JsonDao = new Web_JsonDao();
					String category[] = web_JsonDao.getCategory(isFinal,
							matchName);
					categorySelect.removeAll();
					categorySelect.setItems(category);
				} else {
					MessageBox mb = new MessageBox(shell, SWT.None);
					mb.setMessage("请先选择赛事模式和赛事名称");
					return;
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
			}
		});
		categorySelect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (isSaved) {// 判断排序表是否保存。如保存了则可进行选择，否则或出现两个表的部分重复
					Web_JsonDao web_JsonDao = new Web_JsonDao();
					TableModel tableModel = new TableModel();
					/* 导入查询的未排序表 */
					String category = categorySelect.getItem(categorySelect
							.getSelectionIndex());
					totalTeamsList = web_JsonDao.getSomeWeb_Json(isFinal,
							matchName, category);
					totalTeams.removeAll();// 清空表格
					tableModel.addRow(totalTeams, totalTeamsList);// 将查询记录添加到表格中
				} else {
					MessageBox mb = new MessageBox(shell, SWT.NONE);
					mb.setText("提示");
					mb.setMessage("本场比赛名单未保存，请先保存该名单！");
					mb.open();
				}

			}
		});
	}

	@Override
	protected void checkSubclass() {
	}

	/**
	 * 实例化对象的唯一接口
	 * 
	 * @param @param shell
	 * @param @param parent
	 * @param @param style
	 * @return TeamSortPanel
	 * @throws
	 */
	public static TeamSortPanel getInstance(Shell shell, Composite parent,
			int style) {
		if (panel == null) {
			panel = new TeamSortPanel(shell, parent, style);
		}
		return panel;
	}
}
