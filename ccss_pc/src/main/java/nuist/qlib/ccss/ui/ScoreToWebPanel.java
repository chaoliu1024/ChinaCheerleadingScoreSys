/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nuist.qlib.ccss.manager.score.QueryScore;

import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * @author Chao Liu
 * @since ccss 1.0
 */
public class ScoreToWebPanel extends Composite {
	public static ScoreToWebPanel panel;
	private Text matchName_text;
	private Text matchCategory_text;
	private Table table;
	private Combo matchKind_combo;
	private Combo catory_combo;

	protected int matchType;
	protected String matchName;
	protected String category;
	protected String noWebCategory;
	protected int matchKind = -1;
	private List<HashMap<String, Object>> data;

	private QueryScore lalaScore;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public ScoreToWebPanel(final Shell shell, final Composite parent,
			final int style) {
		super(parent, style);

		lalaScore = new QueryScore();
		data = new ArrayList<HashMap<String, Object>>();

		Label matchName_lable = new Label(this, SWT.NONE);
		matchName_lable.setBounds(10, 10, 61, 17);
		matchName_lable.setText("赛事名称：");

		matchName_text = new Text(this, SWT.BORDER);
		matchName_text.setBounds(77, 10, 555, 23);

		Label matchKind_label = new Label(this, SWT.NONE);
		matchKind_label.setBounds(665, 10, 61, 17);
		matchKind_label.setText("赛事模式：");

		matchKind_combo = new Combo(this, SWT.NONE);
		matchKind_combo.setBounds(732, 10, 88, 25);

		Label matchCategory_label = new Label(this, SWT.NONE);
		matchCategory_label.setBounds(10, 54, 61, 17);
		matchCategory_label.setText("项目名称：");

		matchCategory_text = new Text(this, SWT.BORDER);
		matchCategory_text.setBounds(77, 54, 464, 23);

		final Composite composite = new Composite(this, SWT.NONE);
		composite.setBounds(22, 156, 929, 404);

		table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(0, 0, 929, 455);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		Button send_btn = new Button(this, SWT.NONE);
		send_btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox box = new MessageBox(shell, SWT.OK | SWT.CANCEL);
				box.setText("提示");
				box.setMessage("将推送成绩,确定推送??");
				if (box.open() == SWT.OK) {
					sendDataToWeb(shell);
				}
			}
		});
		send_btn.setBounds(851, 573, 80, 27);
		send_btn.setText("推送网站");

		Button button = new Button(this, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox box = new MessageBox(shell);
				box.setText("提示");
				if (matchName == null || matchName.trim().length() == 0) {
					matchName_text.setFocus();
					box.setMessage("赛事名称不能为空!!");
					box.open();
					return;
				}
				if (matchCategory_text.getText() != null
						&& matchCategory_text.getText().trim().length() != 0) {
					category = matchCategory_text.getText();
				} else {
					box.setMessage("项目名称不能为空");
					box.open();
					return;
				}
				if (matchType == -1) {
					matchKind_combo.setFocus();
					box.setMessage("赛事模式不能为空!!");
					box.open();
					return;
				}
				if (lalaScore.isCollected()) {
					data = lalaScore.getRank(matchName, category, matchType);
					table.dispose();
					if (category.indexOf("技巧") != -1) {
						matchKind = 1;
					} else {
						matchKind = 0;
					}
					table = createTable(composite, data, matchKind);
				}
			}
		});
		button.setBounds(742, 52, 80, 27);
		button.setText("查询");

		Label label = new Label(this, SWT.NONE);
		label.setBounds(10, 93, 139, 17);
		label.setText("未从网站下载的项目名称：");

		catory_combo = new Combo(this, SWT.NONE);
		catory_combo.setBounds(155, 90, 386, 25);

		Button search_btn2 = new Button(this, SWT.NONE);
		search_btn2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox box = new MessageBox(shell);
				box.setText("提示");
				if (matchName == null || matchName.trim().length() == 0) {
					matchName_text.setFocus();
					box.setMessage("赛事名称不能为空!!");
					box.open();
					return;
				}
				if (noWebCategory == null || noWebCategory.trim().length() == 0) {
					matchCategory_text.setFocus();
					box.setMessage("请选择未下载项目名称!!");
					box.open();
					return;
				}
				if (matchType == -1) {
					matchKind_combo.setFocus();
					box.setMessage("赛事模式不能为空!!");
					box.open();
					return;
				}
				if (lalaScore.isCollected()) {
					data = lalaScore.getNoWebData(matchName, noWebCategory,
							matchType);
					table.dispose();
					if (noWebCategory.indexOf("技巧") != -1) {
						matchKind = 1;
					} else {
						matchKind = 0;
					}
					table = createTable(composite, data, matchKind);
				}
			}
		});
		search_btn2.setBounds(740, 85, 80, 27);
		search_btn2.setText("查询");
		init(shell);
	}

	/***
	 * 初始化界面中的数据，如赛事名称等
	 */
	public void init(final Shell shell) {
		// 初始化赛事名称控件数据
		new AutoCompleteField(matchName_text, new TextContentAdapter(),
				lalaScore.getAllMatchNames());
		matchName_text.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {
				matchKind_combo.setText("");
				matchKind = -1;
				matchCategory_text.setText("");
				category = "";
			}

			@Override
			public void focusLost(FocusEvent arg0) {
			}
		});

		// 初始化赛事模式combo上的事件
		matchKind_combo.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {
				matchKind_combo.removeAll();
				MessageBox box = new MessageBox(shell);
				box.setText("提示");
				if (matchName_text.getText().trim().length() == 0) {
					matchName_text.setFocus();
					box.setMessage("赛事名称不能为空!!");
					box.open();
				} else {
					matchName = matchName_text.getText().trim();
					if (!lalaScore.isCollected()) {
						matchKind_combo.clearSelection();
						box.setText("警告");
						box.setMessage("数据库连接失败!!");
						box.open();
					} else {
						List<Integer> matchKinds = lalaScore
								.getMatchKindByMatchName(matchName_text
										.getText());
						for (int i = 0; i < matchKinds.size(); i++) {
							if (matchKinds.get(i) == 0) {
								matchKind_combo.add("预赛");
								matchKind_combo.setData(String.valueOf(i), 0);
							} else if (matchKinds.get(i) == 1) {
								matchKind_combo.add("决赛");
								matchKind_combo.setData(String.valueOf(i), 1);
							}
						}
						matchCategory_text.setText("");
						category = "";
					}
				}
			}

			@Override
			public void focusLost(FocusEvent arg0) {
			}
		});
		matchKind_combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (matchKind_combo.getSelectionIndex() != -1) {
					matchKind = Integer.valueOf(matchKind_combo
							.getData(
									String.valueOf(matchKind_combo
											.getSelectionIndex())).toString());
				}
			}
		});

		// 项目类别事件
		matchCategory_text.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {

				MessageBox box = new MessageBox(shell);
				box.setText("提示");
				if (matchName_text.getText().trim().length() == 0) {
					matchName_text.setFocus();
					box.setMessage("赛事名称不能为空!!");
					box.open();
					return;
				}
				if (matchKind == -1) {
					matchName_text.setFocus();
					// matchCategory_text.clearSelection();
					box.setMessage("必须选择赛事模式!!");
					box.open();
					return;
				}
				String[] categories = lalaScore.getCategories(matchType,
						matchName);
				if (categories.length != 0
						&& matchCategory_text.getText().trim().length() == 0) {
					matchCategory_text.setText(categories[0]);
				}
				new AutoCompleteField(matchCategory_text,
						new TextContentAdapter(), categories);
			}

			@Override
			public void focusLost(FocusEvent arg0) {
			}
		});

		catory_combo.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {
				MessageBox box = new MessageBox(shell);
				box.setText("提示");
				catory_combo.removeAll();
				if (matchName_text.getText() == null
						|| matchName_text.getText().equals("")) {
					matchName_text.setFocus();
					box.setMessage("赛事名称不可为空");
					box.open();
					return;
				}
				if (matchType == -1) {
					matchKind_combo.forceFocus();
					box.setMessage("请选择赛事模式");
					box.open();
					return;
				}
				if (lalaScore.isCollected()) {
					catory_combo.setItems(lalaScore.getCatoryNoWebId(matchName,
							matchType));
				} else {
					box.setText("警告");
					box.setMessage("数据库连接失败");
					box.open();
				}
			}

			@Override
			public void focusLost(FocusEvent arg0) {
			}

		});
		catory_combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = catory_combo.getSelectionIndex();
				if (index != -1) {
					noWebCategory = catory_combo.getItem(index);
				}
			}
		});
	}

	/**
	 * 将成绩推送到网站
	 * 
	 * @param shell
	 */
	public void sendDataToWeb(Shell shell) {
		MessageBox box = new MessageBox(shell);
		box.setText("提示");
		if (matchType == -1) {
			matchKind_combo.setFocus();
			box.setMessage("赛事模式不能为空!!");
			box.open();
			return;
		}
		if (matchName_text.getText().trim().length() != 0) {
			matchName = matchName_text.getText().trim();
			if (lalaScore.isCollected()) {
				MessageBox checkBox = new MessageBox(shell, SWT.OK | SWT.CANCEL);
				checkBox.setText("提示");
				int checkResult = lalaScore.checkSendDataToWeb(matchName,
						matchType);
				switch (checkResult) {
				case 4:
					box.setMessage("该赛事不可推动，不是来自网站");
					box.open();
					break;
				case 1:
					box.setMessage("赛事正在进行中,不可推动数据");
					box.open();
					break;
				case 2:
					checkBox.setMessage("当前有队伍不是从网站下载的，确定推送?");
					if (checkBox.open() == SWT.OK) {
						String[] resultStr = lalaScore.sendDataToManager(
								matchName, matchType).split(":");
						switch (Integer.valueOf(resultStr[0])) {
						case 0:
							box.setMessage("推送成功!!");
							box.open();
							break;
						case -1:
							box.setMessage(resultStr[1]);
							box.open();
							break;
						case 1:
							box.setMessage("临时目录不存在，请先配置!!");
							box.open();
							break;
						case 2:
							box.setMessage("推送失败!!");
							box.open();
							break;
						case 3:
							box.setMessage(resultStr[1]);
							box.open();
							break;
						}
					}
					break;
				case 3:
					String[] resultStr = lalaScore.sendDataToManager(matchName,
							0).split(":");
					switch (Integer.valueOf(resultStr[0])) {
					case 0:
						box.setMessage("推送成功!!");
						box.open();
						break;
					case -1:
						box.setMessage(resultStr[1]);
						box.open();
						break;
					case 1:
						box.setMessage("临时目录不存在，请先配置!!");
						box.open();
						break;
					case 2:
						box.setMessage("推送失败!!");
						box.open();
						break;
					case 3:
						box.setMessage(resultStr[1]);
						box.open();
						break;
					}
				}
			} else {
				box.setText("警告");
				box.setMessage("数据库连接失败!!");
				box.open();
			}
		} else {
			box.setMessage("赛事名称不能为空!!");
			box.open();
		}
	}

	/** 创建table(舞蹈table或者是技巧table) **/
	public Table createTable(Composite rank_composite,
			List<HashMap<String, Object>> data, int matchKind) {
		Table table = new Table(rank_composite, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.H_SCROLL | SWT.V_SCROLL);
		table.setBounds(0, 0, 929, 455);
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
			referee10_score.setWidth(50);
		}
		TableColumn chief_referee_sub_score = new TableColumn(table, SWT.CENTER);
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
		total.setText("总分");
		rank.setText("排名");
		team_name.setWidth(200);
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
		total.setWidth(65);
		rank.setWidth(73);

		TableItem item;
		Font font = new Font(Display.getDefault(), "宋体", 10, SWT.COLOR_BLUE);
		Color color = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);// 红色
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
						String.valueOf(new DecimalFormat("#.00").format(data
								.get(i).get("total"))),
						String.valueOf(data.get(i).get("rank")) });
				item.setFont(12, font);
				item.setBackground(12, color);
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
						String.valueOf(new DecimalFormat("#.00").format(data
								.get(i).get("total"))),
						String.valueOf(data.get(i).get("rank")) });
				item.setFont(11, font);
				item.setBackground(11, color);
			}
		return table;
	}

	public static ScoreToWebPanel getInstance(Shell shell, Composite parent,
			int style) {
		if (panel == null) {
			panel = new ScoreToWebPanel(shell, parent, style);
		}
		return panel;
	}
}
