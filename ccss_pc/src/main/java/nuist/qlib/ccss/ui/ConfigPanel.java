/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.ui;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nuist.qlib.ccss.config.Config;

import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * 配置裁判长名字等配置信息
 * 
 * @author Chao Liu
 * @since ccss 1.0
 */
public class ConfigPanel extends Composite {

	private static ConfigPanel panel;
	protected Shell shell;
	private Text intercessorText;
	private Text viceRefereeText;
	private Text refreeText;
	private Text locationText;
	private Config config;
	private List<HashMap<String, Object>> data;
	private Text download_text;
	private Text tempDir_text;
	private Text original_matchName;
	private Text reMatchName;
	public static Combo group_combo;
	private Text loadScore_text;

	public ConfigPanel(final Shell shell, final Composite parent,
			final int style) {
		super(parent, style);
		this.setBounds(0, 0, 970, 663);
		setLayout(null);
		config = new Config();
		this.shell = shell;

		Group group_2 = new Group(this, SWT.NONE);
		group_2.setText("裁判长配置");
		group_2.setBounds(10, 10, 937, 244);

		Group group = new Group(group_2, SWT.NONE);
		group.setText("裁判长");
		group.setBounds(59, 24, 861, 73);

		Label label = new Label(group, SWT.NONE);
		label.setBounds(41, 30, 55, 15);
		label.setText("仲裁主任");

		intercessorText = new Text(group, SWT.BORDER);
		intercessorText.setBounds(102, 27, 177, 21);

		Label label_1 = new Label(group, SWT.NONE);
		label_1.setBounds(308, 30, 55, 15);
		label_1.setText("副裁判长");

		viceRefereeText = new Text(group, SWT.BORDER);
		viceRefereeText.setBounds(368, 27, 208, 21);

		Label label_2 = new Label(group, SWT.NONE);
		label_2.setBounds(600, 30, 55, 15);
		label_2.setText("总裁判长");

		refreeText = new Text(group, SWT.BORDER);
		refreeText.setBounds(661, 27, 190, 21);

		Group group_1 = new Group(group_2, SWT.NONE);
		group_1.setText("比赛地点配置");
		group_1.setBounds(59, 117, 861, 73);

		Label label_3 = new Label(group_1, SWT.NONE);
		label_3.setBounds(174, 40, 55, 15);
		label_3.setText("参赛地点");

		locationText = new Text(group_1, SWT.BORDER);
		locationText.setBounds(247, 34, 73, 21);

		Button submit = new Button(group_2, SWT.NONE);
		submit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String mention = "";
				String intercessor = intercessorText.getText();
				boolean mark = false;
				if (intercessor == null || intercessor.equals("")) {
					mention += "未填写仲裁主任的名字\n";
				}
				String viceReferee = viceRefereeText.getText();
				if (viceReferee == null || viceReferee.equals("")) {
					mention += "未填写副裁判长的名字\n";
				}
				String refree = refreeText.getText();
				if (refree == null || refree.equals("")) {
					mention += "未填写总裁判长的名字\n";
				}
				String location = locationText.getText();
				if (location == null || location.equals("")) {
					mention += "未填写比赛地点的名字\n";
				}
				if (mention.equals("")) {
					mark = true;
				} else {
					MessageBox box = new MessageBox(shell, SWT.OK | SWT.CANCEL);
					box.setText("提示");
					box.setMessage("存在以下问题是否继续?\n" + mention);
					if (box.open() == SWT.OK) {
						mark = true;
					}
				}
				if (mark) {
					if (intercessor == null || intercessor.equals("")) {
						data.get(0).put("name", "");
					} else {
						data.get(0).put("name", intercessor);
					}
					if (viceReferee == null || viceReferee.equals("")) {
						data.get(1).put("name", "");
					} else {
						data.get(1).put("name", viceReferee);
					}
					if (refree == null || refree.equals("")) {
						data.get(2).put("name", "");
					} else {
						data.get(2).put("name", refree);
					}
					if (location == null || location.equals("")) {
						data.get(0).put("location", "");
						data.get(1).put("location", "");
						data.get(2).put("location", "");
					} else {
						data.get(0).put("location", location);
						data.get(1).put("location", location);
						data.get(2).put("location", location);
					}
					MessageBox box = new MessageBox(shell, SWT.OK);
					box.setText("提示");
					if (config.updateParams(data) > -1) {
						box.setMessage("修改成功!!");
					} else {
						box.setMessage("修改失败!!");
					}
					box.open();
				}
			}
		});
		submit.setBounds(818, 209, 75, 25);
		submit.setText("确定");

		Group group_3 = new Group(this, SWT.NONE);
		group_3.setText("队伍网络下载配置");
		group_3.setBounds(10, 274, 937, 141);

		Label download_lab = new Label(group_3, SWT.NONE);
		download_lab.setBounds(98, 32, 61, 17);
		download_lab.setText("下载地址：");

		String[] params = config.getProParams();
		download_text = new Text(group_3, SWT.BORDER);
		download_text.setBounds(162, 32, 670, 23);
		download_text.setText(params[0]);

		Label tempDir_lab = new Label(group_3, SWT.NONE);
		tempDir_lab.setBounds(70, 107, 84, 17);
		tempDir_lab.setText("临时存放目录：");

		tempDir_text = new Text(group_3, SWT.BORDER);
		tempDir_text.setBounds(163, 104, 313, 23);
		tempDir_text.setText(params[1]);

		Button confirm_btn = new Button(group_3, SWT.NONE);
		confirm_btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox box = new MessageBox(shell);
				box.setText("提示");
				if (download_text.getText().trim().length() == 0) {
					box.setMessage("下载地址不能为空!");
				} else if (tempDir_text.getText().trim().length() == 0) {
					box.setMessage("临时存放目录不能为空!");
				} else if (loadScore_text.getText().trim().length() == 0) {
					box.setMessage("上传成绩地址不能为空!");
				} else {
					config.updateProParams(download_text.getText().trim(),
							tempDir_text.getText().trim(), loadScore_text
									.getText().trim());
					box.setMessage("更新成功!");
				}
				box.open();
			}
		});
		confirm_btn.setBounds(825, 102, 80, 27);
		confirm_btn.setText("确定");

		Label label_7 = new Label(group_3, SWT.NONE);
		label_7.setBounds(70, 63, 89, 17);
		label_7.setText("上传成绩地址：");

		loadScore_text = new Text(group_3, SWT.BORDER);
		loadScore_text.setBounds(162, 63, 670, 23);
		loadScore_text.setText(params[3]);

		Group group_4 = new Group(this, SWT.NONE);
		group_4.setText("赛事名称修改");
		group_4.setBounds(10, 440, 937, 84);

		Label label_4 = new Label(group_4, SWT.NONE);
		label_4.setBounds(74, 40, 61, 17);
		label_4.setText("赛事名称:");

		original_matchName = new Text(group_4, SWT.BORDER);
		original_matchName.setBounds(141, 40, 266, 23);

		Label label_5 = new Label(group_4, SWT.NONE);
		label_5.setBounds(424, 40, 48, 17);
		label_5.setText("替换为");

		reMatchName = new Text(group_4, SWT.BORDER);
		reMatchName.setBounds(476, 40, 306, 23);

		Button matchName_btn = new Button(group_4, SWT.NONE);
		matchName_btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String originalName = original_matchName.getText();
				String reName = reMatchName.getText();
				MessageBox box = new MessageBox(shell);
				box.setText("提示");

				if (originalName == null || originalName.equals("")) {
					box.setMessage("要替换的赛事名称不可为空");
					box.open();
					return;
				} else if (reName == null || reName.equals("")) {
					box.setMessage("新赛事名称不可为空");
					box.open();
					return;
				} else if (reName.contains("暨")) {
					String[] temps = reName.split("暨");
					if (calChar(temps[0].trim()) > 17) {
						box.setMessage("主标题汉字不可以超过17个");
						box.open();
						return;
					} else if (calChar(temps[1].trim()) > 17) {
						box.setMessage("副标题汉字不可以超过17个");
						box.open();
						return;
					}
				} else if (calChar(reName.trim()) > 17) {
					box.setMessage("主标题汉字不可以超过17个");
					box.open();
					return;
				}
				if (config.isCollected()) {
					System.out.println(reName);
					int result = config.updateMatchName(originalName, reName);
					if (result == 0) {
						box.setMessage("不存在要替换的赛事名称");
						box.open();
					} else {
						box.setMessage("更新成功");
						box.open();
					}
				} else {
					box.setText("警告");
					box.setMessage("数据库连接失败");
					box.open();
				}
			}
		});
		matchName_btn.setBounds(828, 35, 80, 27);
		matchName_btn.setText("确定");

		Button matchName_reflash = new Button(group_4, SWT.NONE);
		matchName_reflash.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (config.isCollected()) {
					String[] matchNames = config.getMatchNames();
					new AutoCompleteField(original_matchName,
							new TextContentAdapter(), matchNames);
					if (matchNames.length != 0) {
						original_matchName.setText(matchNames[0]);
					}
					reMatchName.setText("");
				} else {
					MessageBox box = new MessageBox(shell, SWT.OK);
					box.setText("提示");
					box.setMessage("数据库连接失败!!");
					box.open();
				}
			}
		});
		matchName_reflash.setBounds(21, 35, 44, 27);
		matchName_reflash.setText("刷新");

		Label label_8 = new Label(group_4, SWT.NONE);
		label_8.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		label_8.setBounds(104, 10, 467, 17);
		label_8.setText("提示:主标题与副标题均为17个汉字，且用\"暨\"隔开");

		Group group_5 = new Group(this, SWT.NONE);
		group_5.setText("裁判组别配置");
		group_5.setBounds(10, 552, 937, 71);

		Label label_6 = new Label(group_5, SWT.NONE);
		label_6.setBounds(57, 32, 54, 17);
		label_6.setText("组别个数");

		group_combo = new Combo(group_5, SWT.NONE);
		group_combo.setItems(new String[] { "1", "2" });
		group_combo.setBounds(139, 29, 88, 25);
		group_combo.setText(params[2]);

		Button chiefCatoryBtn = new Button(group_5, SWT.NONE);
		chiefCatoryBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox box = new MessageBox(shell);
				box.setText("提示");
				if (group_combo.getText().trim().length() == 0) {
					box.setMessage("裁判组别不能为空!!");
				} else {
					config.updateChiefCatory(group_combo.getText());
					config.changeChiefCatory(group_combo.getText());
					box.setMessage("更新成功!!");
				}
				box.open();
			}
		});
		chiefCatoryBtn.setText("确定");
		chiefCatoryBtn.setBounds(836, 27, 80, 27);

		// 初始化数据
		if (config.isCollected()) {
			data = config.getParams();
			if (data.get(0).get("name") != null) {
				intercessorText.setText(data.get(0).get("name").toString());
			}
			if (data.get(1).get("name") != null) {
				viceRefereeText.setText(data.get(1).get("name").toString());
			}
			if (data.get(2).get("name") != null) {
				refreeText.setText(data.get(2).get("name").toString());
			}
			if (data.get(2).get("location") != null) {
				locationText.setText(data.get(2).get("location").toString());
			}
			String[] matchNames = config.getMatchNames();
			new AutoCompleteField(original_matchName, new TextContentAdapter(),
					matchNames);
			if (matchNames.length != 0) {
				original_matchName.setText(matchNames[0]);
			}
		} else {
			MessageBox box = new MessageBox(this.shell, SWT.OK);
			box.setText("提示");
			box.setMessage("数据库连接失败!!");
			box.open();
		}
	}

	public static ConfigPanel getInstance(Shell shell, Composite parent,
			int style) {
		if (panel == null) {
			panel = new ConfigPanel(shell, parent, style);
		}
		return panel;
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
