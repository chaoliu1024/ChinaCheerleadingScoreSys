/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nuist.qlib.ccss.manager.score.PrintCertificate;
import nuist.qlib.ccss.manager.score.QueryScore;
import nuist.qlib.ccss.util.file.PDFManager;

import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * @author Fang Wang
 * @since ccss 1.0
 */
public class RankPanel {
	protected Shell rank_shell;
	private Table table;
	private Text search;
	// 导出成绩按钮
	private Button export_result_but;
	// 查询成绩按钮
	private Button search_but;
	// 修订成绩按钮
	private Button revise_data_but;
	// 一键式
	private Button export_all_but;
	// 打印证书
	private Button print_but;
	private Composite rank_composite;
	// 预赛决赛
	protected int matchType;
	protected int id;
	// 赛事名称
	protected String matchName;
	// 比赛项目
	protected String category;
	protected int matchKind;
	private List<HashMap<String, Object>> data;

	private QueryScore score;
	private PrintCertificate print;

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents(display);
		rank_shell.open();
		rank_shell.layout();
		while (!rank_shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public static void main(String[] args) {
		try {
			RankPanel window = new RankPanel();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents(Display display) {
		score = new QueryScore();
		print = new PrintCertificate();
		data = new ArrayList<HashMap<String, Object>>();

		rank_shell = new Shell(display, SWT.CLOSE | SWT.MIN);
		rank_shell.setSize(1176, 653);
		rank_shell.setImage(new Image(display, RankPanel.class
				.getResourceAsStream("/img/logo.png")));
		// rank_shell.setImage(new Image(display, "img/logo.png"));
		rank_shell.addShellListener(new ShellAdapter() {
			public void shellClosed(ShellEvent e) {
				if (score.isCollected()) {
					score.close();
				}
				if (print.isCollected()) {
					print.close();
				}
				rank_shell.dispose();
			}
		});
		rank_shell.setText("成绩排名");

		Rectangle displayBounds = display.getPrimaryMonitor().getBounds();
		Rectangle shellBounds = rank_shell.getBounds();
		int x = displayBounds.x + (displayBounds.width - shellBounds.width) >> 1;
		int y = displayBounds.y + (displayBounds.height - shellBounds.height) >> 1;
		rank_shell.setLocation(x, y);

		rank_composite = new Composite(rank_shell, SWT.NONE);
		rank_composite.setBounds(0, 0, 1218, 699);

		export_result_but = new Button(rank_composite, SWT.NONE);
		export_result_but.setBounds(572, 590, 80, 27);
		export_result_but.setText("导出成绩单");

		search = new Text(rank_composite, SWT.BORDER);
		search.setBounds(10, 10, 204, 23);

		search_but = new Button(rank_composite, SWT.NONE);
		search_but.setBounds(234, 8, 80, 27);
		search_but.setText("查询");

		revise_data_but = new Button(rank_composite, SWT.NONE);
		revise_data_but.setBounds(453, 590, 80, 27);
		revise_data_but.setText("订正数据");

		// 决赛数据一键式导出
		export_all_but = new Button(rank_composite, SWT.NONE);
		export_all_but.setBounds(857, 590, 145, 27);
		export_all_but.setText("决赛数据一键式导出");

		print_but = new Button(rank_composite, SWT.NONE);
		print_but.setBounds(1009, 9, 75, 25);
		print_but.setText("打印证书");

		Button deviations_btn = new Button(rank_composite, SWT.NONE);
		deviations_btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ViewDeviations window = new ViewDeviations();
				ViewDeviations.matchName = matchName;
				ViewDeviations.matchType = matchType;
				window.open();
			}
		});
		deviations_btn.setBounds(893, 10, 80, 27);
		deviations_btn.setText("查看误差");

		// 初始化导出数据
		if (!score.isCollected()) {
			MessageBox box = new MessageBox(rank_shell, SWT.OK);
			box.setMessage("提示");
			box.setMessage("连接数据库出错");
			export_result_but.setEnabled(false);
			box.open();
			createTable(rank_composite, data, 0);
		} else {
			data = score.getRank(id, matchType);
			// 设置项目类别
			category = score.getCategory(id);
			if (category.indexOf("技巧") != -1) {
				matchKind = 1;
			} else {
				matchKind = 0;
			}
			table = createTable(rank_composite, data, matchKind);
			rank_shell.setText(matchName + "项目排名情况");
			search.setText(category);
			new AutoCompleteField(search, new TextContentAdapter(),
					score.getCategories(matchType, matchName));
		}
		addEvent();
	}

	public void addEvent() {

		/* 跳转到修订成绩界面 */
		revise_data_but.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (data.size() != 0
						&& (category != null && !category.equals(""))) {
					ReviseDataPanel window = new ReviseDataPanel();
					window.setCategory(category);
					window.setData(data);
					window.setMatchName(matchName);
					window.setMatchType(matchType);
					window.open();
				} else {
					MessageBox box = new MessageBox(rank_shell, SWT.OK);
					box.setText("提示");
					box.setMessage("没有数据或者无项目名称");
					box.open();
				}
			}
		});

		/** 跳转到挑选项目人数界面 */
		export_all_but.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox box = new MessageBox(rank_shell, SWT.OK);
				box.setText("提示");
				if (score.isCollected()) {
					ExportFinalAllPanel window = new ExportFinalAllPanel(
							matchName);
					window.open();

				} else {
					box.setMessage("未与数据库连接");
					box.open();
				}
			}
		});

		/* 查询成绩事件 */
		search_but.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (search.getText() != null && !search.getText().equals("")) {
					String selectedValue = search.getText();
					category = selectedValue;
					data = score.getRank(matchName, selectedValue, matchType);
					table.dispose();
					if (category.indexOf("技巧") != -1) {
						matchKind = 1;
					} else {
						matchKind = 0;
					}
					table = createTable(rank_composite, data, matchKind);
				}
			}
		});

		/* 导出成绩事件 */
		export_result_but.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox box = new MessageBox(rank_shell, SWT.OK);
				box.setText("提示");
				if (data.size() == 0) {
					box.setMessage("暂时没有成绩数据,不能导出");
					box.open();
					return;
				}
				if (category.indexOf("技巧") != -1) {
					matchKind = 1;
				} else {
					matchKind = 0;
				}

				FileDialog dialog = new FileDialog(rank_shell, SWT.SAVE);
				dialog.setText("保存文件");// 设置对话框的标题
				dialog.setFilterExtensions(new String[] { "*.xls", "*.xlsx" });
				dialog.setFilterNames(new String[] { "Excel文件(*.xls)",
						"Excel文件(*.xlsx)" });
				dialog.setFileName(category);
				String fileName = dialog.open(); // 获得保存的文件名
				if (fileName != null && !fileName.equals("")) {
					boolean mark = score.exportScoreExcel(fileName, matchName,
							category, matchType, matchKind);
					if (mark) {
						box.setMessage("导出成绩单成功");
					} else {
						box.setMessage("程序发生错误");
					}
					box.open();
				}
			}
		});

		// 打印证书
		print_but.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				MessageBox box = new MessageBox(rank_shell, SWT.OK);
				box.setText("提示");

				boolean isExportPDF = false;
				if (matchName != null && category != null && matchType != -1) {

					int match_num;
					int match_order;
					String match_name = null;
					String unit_name = null;
					String player_name = null;
					String _category = null;
					String coach_name = null;
					String _tuNames = null;
					int _rank = 0;

					List<HashMap<String, Object>> data = score
							.getCertificateInfor(matchName, category, matchType);
					for (int i = 0; i < data.size(); i++) {
						match_num = Integer.parseInt(data.get(i)
								.get("matchNum").toString());
						match_order = Integer.parseInt(data.get(i)
								.get("matchOrder").toString());
						if (data.get(i).get("matchName") != null) {
							match_name = data.get(i).get("matchName")
									.toString();
						}
						if (data.get(i).get("units") != null) {
							unit_name = data.get(i).get("units").toString();
						}
						if (data.get(i).get("playerName") != null) {
							player_name = data.get(i).get("playerName")
									.toString();
						}
						if (data.get(i).get("category") != null) {
							_category = data.get(i).get("category").toString();
						}
						if (data.get(i).get("rank") != null) {
							_rank = Integer.parseInt(data.get(i).get("rank")
									.toString());
						}
						if (data.get(i).get("rank") != null) {
							_rank = Integer.parseInt(data.get(i).get("rank")
									.toString());
						}
						if (data.get(i).get("tbNames") != null) {
							_tuNames = data.get(i).get("tbNames").toString();
						} else {
							_tuNames = null;
						}
						PDFManager pdf = new PDFManager();
						if (_rank == 1 || _rank == 2 || _rank == 3) {
							if (data.get(i).get("coachName") != null) {
								coach_name = data.get(i).get("coachName")
										.toString();
								String[] coachNames = coach_name.split(",");
								for (int k = 0; k < coachNames.length; k++) {
									isExportPDF = pdf.createCoachPDF(
											match_name, unit_name,
											coachNames[k]);
								}
							}
						}
						if (_tuNames != null) {
							player_name += "," + _tuNames;
						}
						isExportPDF = pdf.createPDF(match_num, match_order,
								match_name, unit_name, player_name, _category,
								_rank);
					}
					if (isExportPDF) {
						box.setMessage(category
								+ "\n所有获奖证书导出成功\n证书地址：<D盘,证书打印>文件夹下");
						box.open();
					} else {
						box.setMessage("获奖证书导出失败");
						box.open();
					}
				} else {
					box.setMessage("赛事名称,参赛项目不能为空值");
					box.open();
				}
			}
		});
	}

	/**
	 * 创建table 舞蹈table或者是技巧table
	 * 
	 * @param rank_composite
	 * @param data
	 * @param matchKind
	 * @return
	 */
	public Table createTable(Composite rank_composite,
			List<HashMap<String, Object>> data, int matchKind) {
		Table table = new Table(rank_composite, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.H_SCROLL | SWT.V_SCROLL);
		table.setBounds(0, 60, 1172, 508);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		TableColumn team_name = new TableColumn(table, SWT.CENTER);
		TableColumn referee1_score = new TableColumn(table, SWT.CENTER);
		TableColumn referee2_score = new TableColumn(table, SWT.CENTER);
		TableColumn referee3_score = new TableColumn(table, SWT.CENTER);
		TableColumn referee4_score = new TableColumn(table, SWT.CENTER);
		TableColumn referee5_score = new TableColumn(table, SWT.CENTER);
		TableColumn referee6_score = new TableColumn(table, SWT.CENTER);
		TableColumn referee7_score = new TableColumn(table, SWT.CENTER);
		TableColumn referee8_score = new TableColumn(table, SWT.CENTER);
		TableColumn referee9_score = new TableColumn(table, SWT.CENTER);
		if (matchKind == 1) {
			TableColumn referee10_score = new TableColumn(table, SWT.CENTER);
			referee10_score.setText("裁判10");
			referee10_score.setWidth(60);
		}
		TableColumn chief_referee_sub_score = new TableColumn(table, SWT.CENTER);
		TableColumn chief_referee_add_score = new TableColumn(table, SWT.CENTER);
		TableColumn total = new TableColumn(table, SWT.CENTER);
		TableColumn rank = new TableColumn(table, SWT.CENTER);
		team_name.setText("参赛单位");
		referee1_score.setText("裁判1");
		referee2_score.setText("裁判2");
		referee3_score.setText("裁判3");
		referee4_score.setText("裁判4");
		referee5_score.setText("裁判5");
		referee6_score.setText("裁判6");
		referee7_score.setText("裁判7");
		referee8_score.setText("裁判8");
		referee9_score.setText("裁判9");
		chief_referee_sub_score.setText("裁判长减分");
		chief_referee_add_score.setText("裁判长加分");
		total.setText("总分");
		rank.setText("排名");
		team_name.setWidth(280);
		referee1_score.setWidth(60);
		referee2_score.setWidth(60);
		referee3_score.setWidth(60);
		referee4_score.setWidth(60);
		referee5_score.setWidth(60);
		referee6_score.setWidth(60);
		referee7_score.setWidth(60);
		referee8_score.setWidth(60);
		referee9_score.setWidth(60);
		chief_referee_sub_score.setWidth(75);
		chief_referee_add_score.setWidth(75);
		total.setWidth(65);
		rank.setWidth(73);

		TableItem item;
		Font font = new Font(Display.getDefault(), "宋体", 10, SWT.COLOR_BLUE);
		// 填充色
		Color color = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
		if (matchKind == 1) {
			for (int i = 0; i < data.size(); i++) {
				item = new TableItem(table, SWT.NONE);
				item.setText(new String[] {
						(String) data.get(i).get("teamName"),
						String.valueOf(data.get(i).get("score1") == null ? ""
								: data.get(i).get("score1")),
						String.valueOf(data.get(i).get("score2") == null ? ""
								: data.get(i).get("score2")),
						String.valueOf(data.get(i).get("score3") == null ? ""
								: data.get(i).get("score3")),
						String.valueOf(data.get(i).get("score4") == null ? ""
								: data.get(i).get("score4")),
						String.valueOf(data.get(i).get("score5") == null ? ""
								: data.get(i).get("score5")),
						String.valueOf(data.get(i).get("score6") == null ? ""
								: data.get(i).get("score6")),
						String.valueOf(data.get(i).get("score7") == null ? ""
								: data.get(i).get("score7")),
						String.valueOf(data.get(i).get("score8") == null ? ""
								: data.get(i).get("score8")),
						String.valueOf(data.get(i).get("score9") == null ? ""
								: data.get(i).get("score9")),
						String.valueOf(data.get(i).get("score10") == null ? ""
								: data.get(i).get("score10")),
						String.valueOf(data.get(i).get("sub_score") == null ? ""
								: data.get(i).get("sub_score")),
						String.valueOf(data.get(i).get("add_score") == null ? ""
								: data.get(i).get("add_score")),
						String.valueOf(new DecimalFormat("#0.00").format(data
								.get(i).get("total"))),
						String.valueOf(data.get(i).get("rank")) });
				item.setFont(13, font);
				item.setBackground(13, color);
			}
		} else
			for (int i = 0; i < data.size(); i++) {
				item = new TableItem(table, SWT.NONE);
				item.setText(new String[] {
						(String) data.get(i).get("teamName"),
						String.valueOf(data.get(i).get("score1") == null ? ""
								: data.get(i).get("score1")),
						String.valueOf(data.get(i).get("score2") == null ? ""
								: data.get(i).get("score2")),
						String.valueOf(data.get(i).get("score3") == null ? ""
								: data.get(i).get("score3")),
						String.valueOf(data.get(i).get("score4") == null ? ""
								: data.get(i).get("score4")),
						String.valueOf(data.get(i).get("score5") == null ? ""
								: data.get(i).get("score5")),
						String.valueOf(data.get(i).get("score6") == null ? ""
								: data.get(i).get("score6")),
						String.valueOf(data.get(i).get("score7") == null ? ""
								: data.get(i).get("score7")),
						String.valueOf(data.get(i).get("score8") == null ? ""
								: data.get(i).get("score8")),
						String.valueOf(data.get(i).get("score9") == null ? ""
								: data.get(i).get("score9")),
						String.valueOf(data.get(i).get("sub_score") == null ? ""
								: data.get(i).get("sub_score")),
						String.valueOf(data.get(i).get("add_score") == null ? ""
								: data.get(i).get("add_score")),
						String.valueOf(new DecimalFormat("#0.00").format(data
								.get(i).get("total"))),
						String.valueOf(data.get(i).get("rank")) });
				item.setFont(12, font);
				item.setBackground(12, color);
			}
		return table;
	}
}
