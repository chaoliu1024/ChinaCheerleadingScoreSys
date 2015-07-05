/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nuist.qlib.ccss.manager.score.ReviseScore;
import nuist.qlib.ccss.util.score.CalcScore;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 修订成绩界面
 * 
 * @author Fang Wang
 * @since ccss 1.0
 */
public class ReviseDataPanel {
	protected Shell shell;
	// 需要更正的数据
	private List<HashMap<String, Object>> data;
	// 项目名称
	private String category;
	private Tree tree;
	private TreeEditor editor;
	private Text text;

	private ReviseScore reviseScore;
	private int index;
	// 预赛还是决赛
	private int matchType;
	// 赛事名称，第一次就要获得
	private String matchName;
	private int matchKind;

	public List<HashMap<String, Object>> getData() {
		return data;
	}

	public void setData(List<HashMap<String, Object>> data) {
		this.data = data;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getMatchType() {
		return matchType;
	}

	public void setMatchType(int matchType) {
		this.matchType = matchType;
	}

	public String getMatchName() {
		return matchName;
	}

	public void setMatchName(String matchName) {
		this.matchName = matchName;
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents(display);
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents(Display display) {

		if (category.indexOf("技巧") != -1) {
			matchKind = 1;
		} else {
			matchKind = 0;
		}
		reviseScore = new ReviseScore();

		shell = new Shell(display, SWT.CLOSE | SWT.MIN);
		shell.setSize(1176, 610);
		shell.setImage(new Image(display, ReviseDataPanel.class
				.getResourceAsStream("/img/logo.png")));
		// shell.setImage(new Image(display, "img/logo.png"));
		shell.setText("数据更正");
		Rectangle displayBounds = display.getPrimaryMonitor().getBounds();
		Rectangle shellBounds = shell.getBounds();
		int x = displayBounds.x + (displayBounds.width - shellBounds.width) >> 1;
		int y = displayBounds.y + (displayBounds.height - shellBounds.height) >> 1;
		shell.setLocation(x, y);
		shell.addShellListener(new ShellAdapter() {
			public void shellClosed(ShellEvent e) {
				if (reviseScore.isCollected()) {
					reviseScore.close();
				}
				shell.dispose();
			}
		});

		tree = new Tree(shell, SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL
				| SWT.V_SCROLL);
		tree.setBounds(0, 10, 1172, 508);
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		createTreeTable(tree, data, matchKind);

		shell.setText(category + "项目排名情况");
		editor = new TreeEditor(tree);
		editor.horizontalAlignment = SWT.RIGHT;
		editor.grabHorizontal = true;
		tree.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event arg0) {
				Tree tree1 = ((Tree) arg0.widget);
				if (tree1.getSelection().length != 0) {
					final TreeItem item = tree1.getSelection()[0];
					Point point = new Point(arg0.x, arg0.y);
					if (item.getData() != null)
						return;
					for (int i = 0; i < tree.getColumnCount(); i++) {
						Rectangle rect = item.getBounds(i);
						if (rect.contains(point))
							index = i;
					}
					if (text != null) {
						text.dispose();
					}
					text = new Text(tree, 0);
					text.addListener(SWT.FocusOut, new Listener() {

						public void handleEvent(Event arg0) {
							if (arg0.type == SWT.FocusOut) {
								item.setText(index, text.getText());
							}
						}
					});
					text.setText(item.getText(index));
					editor.setEditor(text, item, index);
				}
			}
		});
		Button button = new Button(shell, SWT.NONE);
		button.setBounds(1039, 537, 80, 27);
		button.setText("确认更正");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox box = new MessageBox(shell, SWT.OK);
				box.setText("提示");
				if (reviseScore.isCollected()) {
					updateData(box, reviseScore,
							preparedHasReviseData(tree, matchKind), tree,
							matchKind);
				} else {
					box.setMessage("数据库连接出错!");
					box.open();
				}
			}
		});
	}

	/**
	 * 更新数据
	 * 
	 * @param box
	 * @param dao
	 * @param realData
	 * @param tree
	 * @param matchKind
	 * @since dss 1.0
	 */
	public void updateData(MessageBox box, ReviseScore dao,
			List<HashMap<String, Object>> realData, Tree tree, int matchKind) {
		boolean mark = false;
		if (matchKind == 0) {
			if (dao.updateDanceAll(realData)) {
				mark = true;
			} else
				mark = false;
		} else if (matchKind == 1) {
			if (dao.updateSkillAll(realData)) {
				mark = true;
			} else
				mark = false;
		}
		final Font font = new Font(Display.getDefault(), "宋体", 10,
				SWT.COLOR_BLUE);
		// 填充色
		final Color color = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
		if (mark) {
			box.setMessage("修正成功!");
			data = dao.getRank(matchName, category, matchType);
			// 填充数据
			TreeItem[] items = tree.getItems();
			tree.deselectAll();
			for (int i = 0; i < data.size(); i++) {
				items[i].setText(0, (String) data.get(i).get("teamName"));
				items[i].setText(1, data.get(i).get("score1") == null ? ""
						: String.valueOf(data.get(i).get("score1")));
				items[i].setText(2, data.get(i).get("score2") == null ? ""
						: String.valueOf(data.get(i).get("score2")));
				items[i].setText(3, data.get(i).get("score3") == null ? ""
						: String.valueOf(data.get(i).get("score3")));
				items[i].setText(4, data.get(i).get("score4") == null ? ""
						: String.valueOf(data.get(i).get("score4")));
				items[i].setText(5, data.get(i).get("score5") == null ? ""
						: String.valueOf(data.get(i).get("score5")));
				items[i].setText(6, data.get(i).get("score6") == null ? ""
						: String.valueOf(data.get(i).get("score6")));
				items[i].setText(7, data.get(i).get("score7") == null ? ""
						: String.valueOf(data.get(i).get("score7")));
				items[i].setText(8, data.get(i).get("score8") == null ? ""
						: String.valueOf(data.get(i).get("score8")));
				items[i].setText(9, data.get(i).get("score9") == null ? ""
						: String.valueOf(data.get(i).get("score9")));
				if (matchKind == 0) {
					items[i].setText(
							10,
							data.get(i).get("sub_score") == null ? "" : String
									.valueOf(data.get(i).get("sub_score")));
					items[i].setText(
							11,
							data.get(i).get("add_score") == null ? "" : String
									.valueOf(data.get(i).get("add_score")));
					items[i].setText(
							12,
							new DecimalFormat("#.00").format(data.get(i).get(
									"total")));
					items[i].setText(13,
							String.valueOf(data.get(i).get("rank")));
					items[i].setFont(12, font);
					items[i].setBackground(12, color);
				} else if (matchKind == 1) {
					items[i].setText(
							10,
							data.get(i).get("score10") == null ? "" : String
									.valueOf(data.get(i).get("score10")));
					items[i].setText(
							11,
							data.get(i).get("sub_score") == null ? "" : String
									.valueOf(data.get(i).get("sub_score")));
					items[i].setText(
							12,
							data.get(i).get("add_score") == null ? "" : String
									.valueOf(data.get(i).get("add_score")));
					items[i].setText(
							13,
							new DecimalFormat("#.00").format(data.get(i).get(
									"total")));
					items[i].setText(14,
							String.valueOf(data.get(i).get("rank")));
					items[i].setFont(13, font);
					items[i].setBackground(13, color);
				}
			}
		} else {
			box.setMessage("修正失败!");
		}
		box.open();
	}

	/**
	 * 计算成绩,并准备数据集,插入数据库中
	 * 
	 * @param tree
	 * @param matchKind
	 * @return
	 */
	public List<HashMap<String, Object>> preparedHasReviseData(Tree tree,
			int matchKind) {
		CalcScore cs = new CalcScore();
		TreeItem[] items = tree.getItems();
		List<HashMap<String, Object>> realData = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> one = null;
		DecimalFormat format = new DecimalFormat("#0.00");
		if (matchKind == 1) {
			for (int i = 0; i < items.length; i++) {
				String totalScore = cs.calcTotalScore(items[i].getText(1),
						items[i].getText(2), items[i].getText(3),
						items[i].getText(4), items[i].getText(5),
						items[i].getText(6), items[i].getText(7),
						items[i].getText(8), items[i].getText(9),
						items[i].getText(10), items[i].getText(11),
						items[i].getText(12));
				List<Float> deviations = cs.getDeviations(items[i].getText(1),
						items[i].getText(2), items[i].getText(3),
						items[i].getText(4), items[i].getText(5),
						items[i].getText(6), items[i].getText(7),
						items[i].getText(8), items[i].getText(9),
						items[i].getText(10));
				one = new HashMap<String, Object>();
				one.put("scoreId", data.get(i).get("scoreId"));
				one.put("score1", items[i].getText(1));
				one.put("score_error1", format.format(deviations.get(0)));
				one.put("score2", items[i].getText(2));
				one.put("score_error2", format.format(deviations.get(1)));
				one.put("score3", items[i].getText(3));
				one.put("score_error3", format.format(deviations.get(2)));
				one.put("score4", items[i].getText(4));
				one.put("score_error4", format.format(deviations.get(3)));
				one.put("score5", items[i].getText(5));
				one.put("score_error5", format.format(deviations.get(4)));
				one.put("score6", items[i].getText(6));
				one.put("score_error6", format.format(deviations.get(5)));
				one.put("score7", items[i].getText(7));
				one.put("score_error7", format.format(deviations.get(6)));
				one.put("score8", items[i].getText(8));
				one.put("score_error8", format.format(deviations.get(7)));
				one.put("score9", items[i].getText(9));
				one.put("score_error9", format.format(deviations.get(8)));
				one.put("score10", items[i].getText(10));
				one.put("score_error10", format.format(deviations.get(9)));
				one.put("sub_score",
						items[i].getText(11).trim().length() == 0 ? null
								: items[i].getText(11));
				one.put("add_score",
						items[i].getText(12).trim().length() == 0 ? null
								: items[i].getText(11));
				one.put("total", totalScore);
				realData.add(one);
			}
		} else
			for (int i = 0; i < items.length; i++) {
				String totalScore = cs.calcTotalScore(items[i].getText(1),
						items[i].getText(2), items[i].getText(3),
						items[i].getText(4), items[i].getText(5),
						items[i].getText(6), items[i].getText(7),
						items[i].getText(8), items[i].getText(9), null,
						items[i].getText(10), items[i].getText(11));
				List<Float> deviations = cs.getDeviations(items[i].getText(1),
						items[i].getText(2), items[i].getText(3),
						items[i].getText(4), items[i].getText(5),
						items[i].getText(6), items[i].getText(7),
						items[i].getText(8), items[i].getText(9), null);
				one = new HashMap<String, Object>();
				one.put("scoreId", data.get(i).get("scoreId"));
				one.put("score1", items[i].getText(1));
				one.put("score_error1", format.format(deviations.get(0)));
				one.put("score2", items[i].getText(2));
				one.put("score_error2", format.format(deviations.get(1)));
				one.put("score3", items[i].getText(3));
				one.put("score_error3", format.format(deviations.get(2)));
				one.put("score4", items[i].getText(4));
				one.put("score_error4", format.format(deviations.get(3)));
				one.put("score5", items[i].getText(5));
				one.put("score_error5", format.format(deviations.get(4)));
				one.put("score6", items[i].getText(6));
				one.put("score_error6", format.format(deviations.get(5)));
				one.put("score7", items[i].getText(7));
				one.put("score_error7", format.format(deviations.get(6)));
				one.put("score8", items[i].getText(8));
				one.put("score_error8", format.format(deviations.get(7)));
				one.put("score9", items[i].getText(9));
				one.put("score_error9", format.format(deviations.get(8)));
				one.put("sub_score",
						items[i].getText(10).trim().length() == 0 ? null
								: items[i].getText(10));
				one.put("add_score",
						items[i].getText(11).trim().length() == 0 ? null
								: items[i].getText(11));
				one.put("total", totalScore);
				realData.add(one);
			}
		return realData;
	}

	/**
	 * 创建treeTable
	 * 
	 * @param tree
	 * @param data
	 * @param matchKind
	 */
	public void createTreeTable(Tree tree, List<HashMap<String, Object>> data,
			int matchKind) {
		// 创建表头
		TreeColumn match_units = new TreeColumn(tree, SWT.CENTER);
		TreeColumn referee1_score = new TreeColumn(tree, SWT.CENTER);
		TreeColumn referee2_score = new TreeColumn(tree, SWT.CENTER);
		TreeColumn referee3_score = new TreeColumn(tree, SWT.CENTER);
		TreeColumn referee4_score = new TreeColumn(tree, SWT.CENTER);
		TreeColumn referee5_score = new TreeColumn(tree, SWT.CENTER);
		TreeColumn referee6_score = new TreeColumn(tree, SWT.CENTER);
		TreeColumn referee7_score = new TreeColumn(tree, SWT.CENTER);
		TreeColumn referee8_score = new TreeColumn(tree, SWT.CENTER);
		TreeColumn referee9_score = new TreeColumn(tree, SWT.CENTER);
		if (matchKind == 1) {
			TreeColumn referee10_score = new TreeColumn(tree, SWT.CENTER);
			referee10_score.setText("裁判10");
			referee10_score.setWidth(50);
		}
		TreeColumn chief_referee_sub_score = new TreeColumn(tree, SWT.CENTER);
		TreeColumn chief_referee_add_score = new TreeColumn(tree, SWT.CENTER);
		TreeColumn total = new TreeColumn(tree, SWT.CENTER);
		TreeColumn rank = new TreeColumn(tree, SWT.CENTER);
		match_units.setText("参赛单位");
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
		rank.setText("当前排名");
		match_units.setWidth(200);
		referee1_score.setWidth(48);
		referee2_score.setWidth(46);
		referee3_score.setWidth(46);
		referee4_score.setWidth(44);
		referee5_score.setWidth(66);
		referee6_score.setWidth(48);
		referee7_score.setWidth(50);
		referee8_score.setWidth(49);
		referee9_score.setWidth(50);
		chief_referee_sub_score.setWidth(75);
		chief_referee_add_score.setWidth(75);
		total.setWidth(64);
		rank.setWidth(67);

		// 填充数据
		final Font font = new Font(Display.getDefault(), "宋体", 10,
				SWT.COLOR_BLUE);
		// 填充颜色
		final Color color = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
		TreeItem item;
		if (matchKind == 1) {
			for (int i = 0; i < data.size(); i++) {
				item = new TreeItem(tree, SWT.NONE);
				item.setText(0, (String) data.get(i).get("teamName"));
				item.setText(
						1,
						data.get(i).get("score1") == null ? "" : String
								.valueOf(data.get(i).get("score1")));
				item.setText(
						2,
						data.get(i).get("score2") == null ? "" : String
								.valueOf(data.get(i).get("score2")));
				item.setText(
						3,
						data.get(i).get("score3") == null ? "" : String
								.valueOf(data.get(i).get("score3")));
				item.setText(
						4,
						data.get(i).get("score4") == null ? "" : String
								.valueOf(data.get(i).get("score4")));
				item.setText(
						5,
						data.get(i).get("score5") == null ? "" : String
								.valueOf(data.get(i).get("score5")));
				item.setText(
						6,
						data.get(i).get("score6") == null ? "" : String
								.valueOf(data.get(i).get("score6")));
				item.setText(
						7,
						data.get(i).get("score7") == null ? "" : String
								.valueOf(data.get(i).get("score7")));
				item.setText(
						8,
						data.get(i).get("score8") == null ? "" : String
								.valueOf(data.get(i).get("score8")));
				item.setText(
						9,
						data.get(i).get("score9") == null ? "" : String
								.valueOf(data.get(i).get("score9")));
				item.setText(10, data.get(i).get("score10") == null ? ""
						: String.valueOf(data.get(i).get("score10")));
				item.setText(11, data.get(i).get("sub_score") == null ? ""
						: String.valueOf(data.get(i).get("sub_score")));
				item.setText(12, data.get(i).get("add_score") == null ? ""
						: String.valueOf(data.get(i).get("add_score")));
				item.setText(
						13,
						new DecimalFormat("#.00").format(data.get(i).get(
								"total")));
				item.setText(14, String.valueOf(data.get(i).get("rank")));
				item.setFont(13, font);
				item.setBackground(13, color);
			}
		} else {
			for (int i = 0; i < data.size(); i++) {
				item = new TreeItem(tree, SWT.NONE);
				item.setText(0, (String) data.get(i).get("teamName"));
				item.setText(
						1,
						data.get(i).get("score1") == null ? "" : String
								.valueOf(data.get(i).get("score1")));
				item.setText(
						2,
						data.get(i).get("score2") == null ? "" : String
								.valueOf(data.get(i).get("score2")));
				item.setText(
						3,
						data.get(i).get("score3") == null ? "" : String
								.valueOf(data.get(i).get("score3")));
				item.setText(
						4,
						data.get(i).get("score4") == null ? "" : String
								.valueOf(data.get(i).get("score4")));
				item.setText(
						5,
						data.get(i).get("score5") == null ? "" : String
								.valueOf(data.get(i).get("score5")));
				item.setText(
						6,
						data.get(i).get("score6") == null ? "" : String
								.valueOf(data.get(i).get("score6")));
				item.setText(
						7,
						data.get(i).get("score7") == null ? "" : String
								.valueOf(data.get(i).get("score7")));
				item.setText(
						8,
						data.get(i).get("score8") == null ? "" : String
								.valueOf(data.get(i).get("score8")));
				item.setText(
						9,
						data.get(i).get("score9") == null ? "" : String
								.valueOf(data.get(i).get("score9")));
				item.setText(10, data.get(i).get("sub_score") == null ? ""
						: String.valueOf(data.get(i).get("sub_score")));
				item.setText(11, data.get(i).get("add_score") == null ? ""
						: String.valueOf(data.get(i).get("add_score")));
				item.setText(
						12,
						new DecimalFormat("#.00").format(data.get(i).get(
								"total")));
				item.setText(13, String.valueOf(data.get(i).get("rank")));
				item.setFont(12, font);
				item.setBackground(12, color);
			}
		}
	}
}
