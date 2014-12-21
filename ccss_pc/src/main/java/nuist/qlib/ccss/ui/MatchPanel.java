/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.ui;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import nuist.qlib.ccss.manager.score.QueryScore;
import nuist.qlib.ccss.manager.team.MatchTeamScore;
import nuist.qlib.ccss.net.MainClientOutputThread;
import nuist.qlib.ccss.util.file.AddressManager;
import nuist.qlib.ccss.util.score.CalcScore;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * 打分界面
 * 
 * @author Chao Liu
 * @author Fang Wang
 * @since ccss 1.0
 */
public class MatchPanel extends Composite {
	private static MatchPanel panel;

	private Composite title_composite;
	private Label match_model_label;
	private Combo match_model_combo;

	private String group_num;

	private Label match_num_label;
	private Combo match_num_combo;
	private Label title;
	private Label label_category;

	private Group score_group;
	private static Text score1;
	private Label score_label2;
	private static Text score2;
	private Label score_label3;
	private static Text score3;
	private Label score_label4;
	private static Text score4;
	private Label score_label5;
	private static Text score5;
	private Label score_label6;
	private static Text score6;
	private Label score_label7;
	private static Text score7;
	private Label score_label8;
	private static Text score8;
	private Label score_label9;
	private static Text score9;
	private Label score_label10;
	private static Text score10;
	// 裁判长减分
	private Label deduction_label;
	private static Text deduction_score;
	// 总得分
	private Label total_label;
	private Label total_score;

	private String judgeScore1;
	private String judgeScore2;
	private String judgeScore3;
	private String judgeScore4;
	private String judgeScore5;
	private String judgeScore6;
	private String judgeScore7;
	private String judgeScore8;
	private String judgeScore9;
	private String judgeScore10;
	private String totalScore;

	private Group replay_group;
	private Combo replay_match_model_combo;
	private Label team_label;
	private Combo team_combo;
	private Label category_label;
	private Combo category_combo;
	private Button replay_btn;

	private Composite button_composite;
	private Button calc_btn;
	private Button delay_btn;
	private Button next_btn;
	private Button rank_btn;
	private Button abstain_btn;
	private Button resend_btn;
	private Button selectBtn;

	// 参赛单位
	protected static String team;
	// 比赛项目
	protected static String category;
	// 区分预赛和决赛, 预赛为0, 决赛为1
	protected static int matchType = -1;
	// 补赛赛事模式, 预赛为0, 决赛为1
	protected static int replay_matchType = -1;

	// 赛事名称
	protected static String matchName;
	// 补赛赛事名称
	protected static String replayMatchName;

	// 场次
	protected static int match_num;
	// 队伍的出场顺序
	protected static int matchOrder = -1;
	// 队伍id
	protected static int id = -1;

	// 当程序打开的时候就应该从数据库中将id号取出
	// 延迟参加比赛队伍id
	protected static int re_id = 0;
	// 延迟参加比赛队伍出场顺序
	protected static int re_matchOrder = 0;
	// 是否延迟比赛标记
	protected static boolean rematch_flag = false;

	private Shell ref_edit_shell;

	// 判断是否已经打过分了
	private boolean hasScore = false;
	// 判断是否进行了二次选择，即选择了暂停比赛进行比赛，但是又没有进行比赛
	private boolean reSelect = false;
	// 判断是否是暂停之后又继续开始比赛暂停比赛
	private boolean rePause = false;
	// 判断是否是弃权之后又继续开始比赛暂停比赛
	private boolean reGiveUp = false;
	// 判断是否是点击赛事模式之后进行开始暂停比赛
	private boolean combo = false;
	// 用来表示是否已经提示过，防止弹出多个对话框
	private boolean hasMention = false;

	// 记录员发送信息接口
	private MainClientOutputThread mainClientOutputThread = new MainClientOutputThread();
	// 地址管理
	private AddressManager adressManager = new AddressManager();
	private String teamReceiver[] = { "chiefJudge01", "chiefJudge02",
			"chiefJudge03", "judge1-01", "judge1-02", "judge1-03", "judge1-04",
			"judge1-05", "judge1-06", "judge1-07", "judge1-08", "judge1-09",
			"judge1-10", "judge2-01", "judge2-02", "judge2-03", "judge2-04",
			"judge2-05", "judge2-06", "judge2-07", "judge2-08", "judge2-09",
			"judge2-10" };
	// 参赛队伍接收者名称(裁判长和裁判)
	private String scoreReceiver[] = { "chiefJudge01", "chiefJudge02",
			"chiefJudge03" };
	// 分数接收者名称(裁判长)
	private Label replay_match_model_Label;
	private Label group;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public MatchPanel(final Shell shell, final Composite parent, final int style) {
		super(parent, style);
		this.setBounds(0, 0, 970, 663);
		ref_edit_shell = shell;

		title_composite = new Composite(this, SWT.NONE);
		title_composite.setBounds(0, 10, 965, 151);

		selectBtn = new Button(title_composite, SWT.NONE);
		selectBtn.setBounds(17, 20, 44, 27);
		selectBtn.setText("赛事");

		title = new Label(title_composite, SWT.CENTER);
		title.setFont(SWTResourceManager.getFont("微软雅黑", 20, SWT.NORMAL));
		title.setBackground(SWTResourceManager.getColor(153, 204, 0));
		title.setBounds(0, 10, 965, 38);
		title.setText("参赛队伍");

		label_category = new Label(title_composite, SWT.CENTER);
		label_category.setFont(SWTResourceManager.getFont("微软雅黑", 20,
				SWT.NORMAL));
		label_category.setBackground(SWTResourceManager.getColor(153, 204, 0));
		label_category.setBounds(0, 48, 965, 38);

		match_model_label = new Label(title_composite, SWT.NONE);
		match_model_label.setFont(SWTResourceManager.getFont("微软雅黑", 14,
				SWT.NORMAL));
		match_model_label.setBounds(214, 112, 100, 29);
		match_model_label.setText("赛事模式：");

		match_model_combo = new Combo(title_composite, SWT.NONE);
		match_model_combo.setFont(SWTResourceManager.getFont("微软雅黑", 12,
				SWT.NORMAL));
		match_model_combo.setBounds(320, 112, 88, 25);

		match_num_label = new Label(title_composite, SWT.NONE);
		match_num_label.setText("场次：");
		match_num_label.setFont(SWTResourceManager.getFont("微软雅黑", 14,
				SWT.NORMAL));
		match_num_label.setBounds(24, 112, 57, 29);

		match_num_combo = new Combo(title_composite, SWT.NONE);
		match_num_combo.setFont(SWTResourceManager.getFont("微软雅黑", 12,
				SWT.NORMAL));
		match_num_combo.setBounds(87, 112, 64, 25);

		group = new Label(title_composite, SWT.NONE);
		group.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		group.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.BOLD));
		group.setBounds(785, 115, 57, 26);
		group.setText("A组");

		score_group = new Group(this, SWT.NONE);
		score_group.setBounds(10, 180, 945, 256);

		Label score_label1 = new Label(score_group, SWT.NONE);
		score_label1
				.setFont(SWTResourceManager.getFont("微软雅黑", 13, SWT.NORMAL));
		score_label1.setBounds(21, 27, 68, 28);
		score_label1.setText("得分一：");

		score1 = new Text(score_group, SWT.BORDER);
		score1.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		score1.setBounds(95, 27, 73, 28);

		score_label2 = new Label(score_group, SWT.NONE);
		score_label2.setText("得分二：");
		score_label2
				.setFont(SWTResourceManager.getFont("微软雅黑", 13, SWT.NORMAL));
		score_label2.setBounds(204, 27, 68, 28);

		score2 = new Text(score_group, SWT.BORDER);
		score2.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		score2.setBounds(278, 27, 73, 28);

		score_label3 = new Label(score_group, SWT.NONE);
		score_label3.setText("得分三：");
		score_label3
				.setFont(SWTResourceManager.getFont("微软雅黑", 13, SWT.NORMAL));
		score_label3.setBounds(387, 27, 68, 28);

		score3 = new Text(score_group, SWT.BORDER);
		score3.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		score3.setBounds(461, 27, 73, 28);

		score_label4 = new Label(score_group, SWT.NONE);
		score_label4.setText("得分四：");
		score_label4
				.setFont(SWTResourceManager.getFont("微软雅黑", 13, SWT.NORMAL));
		score_label4.setBounds(570, 27, 68, 28);

		score4 = new Text(score_group, SWT.BORDER);
		score4.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		score4.setBounds(644, 27, 73, 28);

		score_label5 = new Label(score_group, SWT.NONE);
		score_label5.setText("得分五：");
		score_label5
				.setFont(SWTResourceManager.getFont("微软雅黑", 13, SWT.NORMAL));
		score_label5.setBounds(753, 27, 68, 28);

		score5 = new Text(score_group, SWT.BORDER);
		score5.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		score5.setBounds(827, 27, 73, 28);

		score_label6 = new Label(score_group, SWT.NONE);
		score_label6.setText("得分六：");
		score_label6
				.setFont(SWTResourceManager.getFont("微软雅黑", 13, SWT.NORMAL));
		score_label6.setBounds(21, 110, 68, 28);

		score6 = new Text(score_group, SWT.BORDER);
		score6.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		score6.setBounds(95, 110, 73, 28);

		score_label7 = new Label(score_group, SWT.NONE);
		score_label7.setText("得分七：");
		score_label7
				.setFont(SWTResourceManager.getFont("微软雅黑", 13, SWT.NORMAL));
		score_label7.setBounds(204, 110, 68, 28);

		score7 = new Text(score_group, SWT.BORDER);
		score7.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		score7.setBounds(278, 110, 73, 28);

		score_label8 = new Label(score_group, SWT.NONE);
		score_label8.setText("得分八：");
		score_label8
				.setFont(SWTResourceManager.getFont("微软雅黑", 13, SWT.NORMAL));
		score_label8.setBounds(387, 110, 68, 28);

		score8 = new Text(score_group, SWT.BORDER);
		score8.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		score8.setBounds(461, 110, 73, 28);

		score_label9 = new Label(score_group, SWT.NONE);
		score_label9.setText("得分九：");
		score_label9
				.setFont(SWTResourceManager.getFont("微软雅黑", 13, SWT.NORMAL));
		score_label9.setBounds(570, 110, 68, 28);

		score9 = new Text(score_group, SWT.BORDER);
		score9.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		score9.setBounds(644, 110, 73, 28);

		score_label10 = new Label(score_group, SWT.NONE);
		score_label10.setText("得分十：");
		score_label10.setFont(SWTResourceManager
				.getFont("微软雅黑", 13, SWT.NORMAL));
		score_label10.setBounds(753, 110, 68, 28);

		score10 = new Text(score_group, SWT.BORDER);
		score10.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		score10.setBounds(827, 110, 73, 28);

		deduction_label = new Label(score_group, SWT.NONE);
		deduction_label.setText("裁判长减分：");
		deduction_label.setFont(SWTResourceManager.getFont("微软雅黑", 16,
				SWT.NORMAL));
		deduction_label.setBounds(107, 181, 130, 38);

		deduction_score = new Text(score_group, SWT.BORDER);
		deduction_score.setFont(SWTResourceManager.getFont("微软雅黑", 14,
				SWT.NORMAL));
		deduction_score.setBounds(243, 186, 73, 28);

		total_label = new Label(score_group, SWT.HORIZONTAL);
		total_label.setForeground(SWTResourceManager.getColor(255, 0, 0));
		total_label.setText("最后得分：");
		total_label.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.BOLD));
		total_label.setAlignment(SWT.CENTER);
		total_label.setBounds(424, 181, 110, 38);

		total_score = new Label(score_group, SWT.HORIZONTAL);
		total_score.setForeground(SWTResourceManager.getColor(255, 0, 0));
		total_score.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.BOLD));
		total_score.setAlignment(SWT.CENTER);
		total_score.setBounds(524, 182, 92, 28);

		replay_group = new Group(this, SWT.NONE);
		replay_group
				.setFont(SWTResourceManager.getFont("微软雅黑", 15, SWT.NORMAL));
		replay_group.setText("补赛");
		replay_group.setBounds(10, 464, 609, 158);

		replay_match_model_Label = new Label(replay_group, SWT.NONE);
		replay_match_model_Label.setFont(SWTResourceManager.getFont("微软雅黑", 12,
				SWT.NORMAL));
		replay_match_model_Label.setBounds(16, 35, 103, 27);
		replay_match_model_Label.setText("补赛赛事模式");

		replay_match_model_combo = new Combo(replay_group, SWT.NONE);
		replay_match_model_combo.setFont(SWTResourceManager.getFont("微软雅黑", 12,
				SWT.NORMAL));
		replay_match_model_combo.setBounds(125, 32, 88, 29);
		replay_match_model_combo.add("预赛");
		replay_match_model_combo.setData("0", 0);
		replay_match_model_combo.add("决赛");
		replay_match_model_combo.setData("1", 1);

		team_label = new Label(replay_group, SWT.NONE);
		team_label.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		team_label.setBounds(16, 87, 73, 28);
		team_label.setText("参赛队伍");

		team_combo = new Combo(replay_group, SWT.READ_ONLY);
		team_combo.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.NORMAL));
		team_combo.setBounds(91, 85, 190, 31);

		category_label = new Label(replay_group, SWT.NONE);
		category_label.setFont(SWTResourceManager.getFont("微软雅黑", 12,
				SWT.NORMAL));
		category_label.setBounds(327, 87, 76, 28);
		category_label.setText("比赛项目");

		category_combo = new Combo(replay_group, SWT.READ_ONLY);
		category_combo.setFont(SWTResourceManager.getFont("微软雅黑", 11,
				SWT.NORMAL));
		category_combo.setBounds(409, 85, 190, 31);

		replay_btn = new Button(replay_group, SWT.NONE);
		replay_btn.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.NORMAL));
		replay_btn.setBounds(506, 121, 93, 27);
		replay_btn.setText("开始比赛");

		button_composite = new Composite(this, SWT.NONE);
		button_composite.setBounds(639, 452, 317, 185);

		calc_btn = new Button(button_composite, SWT.NONE);
		calc_btn.setEnabled(false);
		calc_btn.setFont(SWTResourceManager.getFont("微软雅黑", 15, SWT.NORMAL));
		calc_btn.setBounds(10, 18, 134, 37);
		calc_btn.setText("计算总得分");

		next_btn = new Button(button_composite, SWT.NONE);
		next_btn.setEnabled(false);
		next_btn.setText("下一只队伍");
		next_btn.setFont(SWTResourceManager.getFont("微软雅黑", 15, SWT.NORMAL));
		next_btn.setBounds(10, 73, 134, 37);

		rank_btn = new Button(button_composite, SWT.NONE);
		rank_btn.setText("排名");
		rank_btn.setFont(SWTResourceManager.getFont("微软雅黑", 15, SWT.NORMAL));
		rank_btn.setBounds(10, 128, 134, 37);

		delay_btn = new Button(button_composite, SWT.NONE);
		delay_btn.setEnabled(true);
		delay_btn.setText("暂停比赛");
		delay_btn.setFont(SWTResourceManager.getFont("微软雅黑", 15, SWT.NORMAL));
		delay_btn.setBounds(173, 18, 134, 37);

		abstain_btn = new Button(button_composite, SWT.NONE);
		abstain_btn.setEnabled(false);
		abstain_btn.setText("弃权比赛");
		abstain_btn.setFont(SWTResourceManager.getFont("微软雅黑", 15, SWT.NORMAL));
		abstain_btn.setBounds(173, 73, 134, 37);

		resend_btn = new Button(button_composite, SWT.NONE);
		resend_btn.setText("重发");
		resend_btn.setFont(SWTResourceManager.getFont("微软雅黑", 15, SWT.NORMAL));
		resend_btn.setBounds(173, 128, 134, 37);

		matchNumComboEvent();
		setMatchModelValue();
		matchModelComboEvent();
		setMatchNameDialog();

		calcButton();
		rankButton();
		nextTeamButton();
		delayButton();
		abstainButton();

		teamComboEvent();
		categoryComboEvent();
		replayButton();
		resendButton();

		replayComboEvent();
	}

	/**
	 * @Description: 设置选择赛事
	 */
	public void setMatchNameDialog() {
		selectBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MatchTeamScore query = new MatchTeamScore();
				String[] matchNames = query.getMatchNames();
				if (matchNames.length == 0) {
					MessageBox box = new MessageBox(ref_edit_shell, SWT.OK);
					box.setText("提示");
					box.setMessage("无比赛进行！");
					box.open();
				} else {
					MatchDialog dialog = new MatchDialog(ref_edit_shell);
					if (matchName != null && matchName.trim().length() != 0) {
						dialog.setMatchName(matchName);
					}
					dialog.setMatchNames(matchNames);
					dialog.open();
					matchName = dialog.getMatchName();

					if (matchName != null && !matchName.equals("")) {
						replayMatchName = matchName;
						match_num = query.getInitMatchNum(matchName); // 场次
						match_num_combo.setText(String.valueOf(match_num));
						match_model_combo.removeAll();
						title.setText("参赛队伍");
						label_category.setText("");
					}
				}
			}
		});
	}

	/**
	 * 设置赛事模式
	 */
	public void setMatchModelValue() {
		match_model_combo.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				match_model_combo.removeAll();
				MatchTeamScore query = new MatchTeamScore();
				List<String> matchType = query.getMatchType(matchName,
						match_num_combo.getText());

				// 获得未进行比赛的赛事模式
				for (int i = 0; i < matchType.size(); i++) {
					match_model_combo.add(matchType.get(i));
					if (matchType.get(i) == "预赛") {
						match_model_combo.setData("0", 0);
					}
					if (matchType.get(i) == "决赛") {
						match_model_combo.setData("1", 1);
					}
				}
				query.close();
			}

			@Override
			public void focusLost(FocusEvent e) {

			}
		});
	}

	/**
	 * 设置未进行的比赛场次
	 */
	public void matchNumComboEvent() {
		match_num_combo.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				match_num_combo.removeAll();
				MatchTeamScore query = new MatchTeamScore();
				if ((matchName == null || matchName.equals(""))
						&& hasMention == false) {
					selectBtn.setFocus();
					selectBtn.forceFocus();
					match_num_combo.clearSelection();
					hasMention = true;

					MessageBox box = new MessageBox(ref_edit_shell, SWT.OK);
					box.setText("提示");
					box.setMessage("请选择要进行的比赛！");
					box.open();
				} else {
					List<String> matchNum = query.getMatchNum(matchName); // 获得未进行比赛的场次
					for (int i = 0; i < matchNum.size(); i++) {
						match_num_combo.add(matchNum.get(i));
					}
					match_model_combo.removeAll();
				}
				query.close();
			}

			@Override
			public void focusLost(FocusEvent e) {
				hasMention = false;
				match_num_combo.clearSelection();
			}
		});
	}

	/**
	 * 点击赛事模式，首个队伍处理
	 */
	public void matchModelComboEvent() {

		match_model_combo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				MatchTeamScore query = new MatchTeamScore();
				// 清除预赛、决赛信息
				if (matchOrder > 0) {
					matchOrder = -1;
				}

				match_num = Integer.parseInt(match_num_combo.getText()); // 重新设置比赛场次
				cleanScore();
				hasScore = false;
				reSelect = false;
				rePause = false;
				reGiveUp = false;
				combo = true;
				calc_btn.setEnabled(true);
				next_btn.setEnabled(false);

				String str_matchType = match_model_combo.getText();
				if (str_matchType.equals("预赛"))
					matchType = 0;
				if (str_matchType.equals("决赛"))
					matchType = 1;

				if (query.isCollected()) {
					List<HashMap<String, Object>> data = query.getInitTeam(
							match_num, matchType, matchName);
					if (data.size() != 0) {
						category = data.get(0).get("category").toString(); // 比赛项目
						if (category.indexOf("技巧") != -1) {
							score10.setEnabled(true);
						} else {
							score10.setEnabled(false);
						}
						team = data.get(0).get("teamName").toString(); // 参赛单位
						matchName = data.get(0).get("matchName").toString(); // 赛事名称
						matchOrder = Integer.valueOf(data.get(0)
								.get("matchOrder").toString()); // 出场顺序
						id = Integer.valueOf(data.get(0).get("id").toString()); // 队伍id
						title.setText(team);
						label_category.setText(category);
						group_num = MainUI.configPanel.group_combo.getText();// 裁判组数

						/********************************** 发送队伍信息 ***********************************/
						if (group_num.equals("1")) {// 只有一组裁判
							group.setText("A组");
						} else {// 两组裁判
							if (matchOrder % 2 == 0) {// 若为偶数次队伍
								group.setText("B组");
							} else if (matchOrder % 2 == 1) {// 若为奇数次队伍
								group.setText("A组");
							}
						}
						new Thread() {
							public void run() {
								int sum = mainClientOutputThread.sendTeam(
										teamReceiver, team, category,
										matchName, matchOrder, group_num);
								if (sum == -1) {// 发送失败
									adressManager.clearIP();
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													MessageBox box = new MessageBox(
															ref_edit_shell,
															SWT.OK);
													box.setText("提示");
													box.setMessage("发送失败，请重发！");
													int val = box.open();
													if (val == SWT.OK)
														return;
												}
											});
								} else if (sum == 0) {
									// 无ip
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													MessageBox box = new MessageBox(
															ref_edit_shell,
															SWT.OK);
													box.setText("提示");
													box.setMessage("未获得接收地址，请稍等片刻！");
													int val = box.open();
													if (val == SWT.OK)
														return;
												}
											});
								}
								Display.getDefault().syncExec(new Runnable() {
									public void run() {
										abstain_btn.setEnabled(false);
									}
								});
							}
						}.start();
					} else {
						// 清楚界面分数
						cleanScore();
						title.setText("参赛队伍");
						label_category.setText("");
						MessageBox box = new MessageBox(ref_edit_shell, SWT.OK);
						box.setText("提示");
						box.setMessage("无参赛队伍信息！！");
						box.open();
					}
				} else {
					MessageBox box = new MessageBox(ref_edit_shell, SWT.OK);
					box.setText("提示");
					box.setMessage("数据库连接失败！！");
					box.open();
				}
				query.close();

				score1.setFocus();
			}
		});
	}

	/**
	 * 计算总成绩
	 */
	public void calcButton() {

		calc_btn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (regVerify()) { // 成绩验证合法
					MatchTeamScore query = new MatchTeamScore();
					CalcScore cs = new CalcScore();

					String sub_score = "0";
					if (deduction_score.getText() != null
							&& !deduction_score.getText().equals("")) {
						sub_score = deduction_score.getText();
					}
					/* 计算总得分 */
					total_score.setText(cs.calcTotalScore(score1.getText(),
							score2.getText(), score3.getText(),
							score4.getText(), score5.getText(),
							score6.getText(), score7.getText(),
							score8.getText(), score9.getText(),
							score10.getText(), sub_score));
					/* 计算总误差 */
					List<Float> deviation = cs.getDeviations(score1.getText(),
							score2.getText(), score3.getText(),
							score4.getText(), score5.getText(),
							score6.getText(), score7.getText(),
							score8.getText(), score9.getText(),
							score10.getText());
					MessageBox box = new MessageBox(ref_edit_shell, SWT.OK);
					box.setText("提示");

					if (query.isCollected()) { // 数据库连接正常

						if (Float.valueOf(total_score.getText()) > 0) { // 总分大于0
							if (rematch_flag == false) { // 正常比赛
								if (match_model_combo.getText() != null
										&& !match_model_combo.getText().equals(
												"") && matchOrder > 0) {
									if (deviation.size() == 9) {
										if (query.insertScore(id,
												score1.getText(),
												score2.getText(),
												score3.getText(),
												score4.getText(),
												score5.getText(),
												score6.getText(),
												score7.getText(),
												score8.getText(),
												score9.getText(),
												score10.getText(), sub_score,

												total_score.getText(),
												deviation.get(0),
												deviation.get(1),
												deviation.get(2),
												deviation.get(3),
												deviation.get(4),
												deviation.get(5),
												deviation.get(6),
												deviation.get(7),
												deviation.get(8)) == 1) {
										} else {
											box.setMessage("录入比赛成绩失败");
											box.open();
										}
									}
									if (deviation.size() == 10) {
										if (query.insertScore(id,
												score1.getText(),
												score2.getText(),
												score3.getText(),
												score4.getText(),
												score5.getText(),
												score6.getText(),
												score7.getText(),
												score8.getText(),
												score9.getText(),
												score10.getText(), sub_score,

												total_score.getText(),
												deviation.get(0),
												deviation.get(1),
												deviation.get(2),
												deviation.get(3),
												deviation.get(4),
												deviation.get(5),
												deviation.get(6),
												deviation.get(7),
												deviation.get(8),
												deviation.get(9)) == 1) {
										} else {
											box.setMessage("录入比赛成绩失败");
											box.open();
										}
									}
									if (query.updateTeamStatu(id, 1) == -1) {
										box.setMessage("更改队伍状态失败！");
										box.open();
									}
								} else if (match_model_combo.getText() != null
										&& !match_model_combo.getText().equals(
												"") && matchOrder == -1) {
									box.setMessage("无参赛队伍信息！！");
									box.open();
								} else {
									box.setMessage("请选择赛事模式！！");
									box.open();
								}
								next_btn.setEnabled(true);
								delay_btn.setEnabled(false);
								abstain_btn.setEnabled(false);
								hasScore = true;
								reSelect = false;
								rePause = false;
								reGiveUp = false;
								combo = false;
							} else { // 延迟比赛
								if (query.updateTeamStatu(re_id, 1) == -1) {
									box.setMessage("更改队伍状态失败！");
									box.open();
								}
								if (deviation.size() == 9) {
									if (query.insertScore(re_id,
											score1.getText(), score2.getText(),
											score3.getText(), score4.getText(),
											score5.getText(), score6.getText(),
											score7.getText(), score8.getText(),
											score9.getText(),
											score10.getText(), sub_score,
											total_score.getText(),
											deviation.get(0), deviation.get(1),
											deviation.get(2), deviation.get(3),
											deviation.get(4), deviation.get(5),
											deviation.get(6), deviation.get(7),
											deviation.get(8)) == 1) {
									} else {
										box.setMessage("录入比赛成绩失败");
										box.open();
									}
								}
								if (deviation.size() == 10) {
									if (query.insertScore(re_id,
											score1.getText(), score2.getText(),
											score3.getText(), score4.getText(),
											score5.getText(), score6.getText(),
											score7.getText(), score8.getText(),
											score9.getText(),
											score10.getText(), sub_score,
											total_score.getText(),
											deviation.get(0), deviation.get(1),
											deviation.get(2), deviation.get(3),
											deviation.get(4), deviation.get(5),
											deviation.get(6), deviation.get(7),
											deviation.get(8), deviation.get(9)) == 1) {
									} else {
										box.setMessage("录入比赛成绩失败");
										box.open();
									}
								}
								next_btn.setEnabled(true);
								abstain_btn.setEnabled(false);
								hasScore = true;
								reSelect = false;
								rePause = false;
								reGiveUp = false;
								combo = false;
							}
							/********************************** 发送比赛成绩至裁判长 ***********************************/
							new Thread() {
								public void run() {
									judgeScore1 = null;
									judgeScore2 = null;
									judgeScore3 = null;
									judgeScore4 = null;
									judgeScore5 = null;
									judgeScore6 = null;
									judgeScore7 = null;
									judgeScore8 = null;
									judgeScore9 = null;
									judgeScore10 = null;
									totalScore = null;
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judgeScore1 = score1
															.getText();
													judgeScore2 = score2
															.getText();
													judgeScore3 = score3
															.getText();
													judgeScore4 = score4
															.getText();
													judgeScore5 = score5
															.getText();
													judgeScore6 = score6
															.getText();
													judgeScore7 = score7
															.getText();
													judgeScore8 = score8
															.getText();
													judgeScore9 = score9
															.getText();
													judgeScore10 = score10
															.getText();
													totalScore = total_score
															.getText();
												}
											});
									int sum = mainClientOutputThread.sendScore(
											scoreReceiver, judgeScore1,
											judgeScore2, judgeScore3,
											judgeScore4, judgeScore5,
											judgeScore6, judgeScore7,
											judgeScore8, judgeScore9,
											judgeScore10, totalScore);
									if (sum == -1) {
										// 发送失败
										adressManager.clearIP();
										Display.getDefault().syncExec(
												new Runnable() {
													public void run() {
														MessageBox box1 = new MessageBox(
																ref_edit_shell,
																SWT.OK);
														box1.setText("提示");
														box1.setMessage("发送失败，请重发！！");
														int val = box1.open();
														if (val == SWT.OK)
															return;
													}
												});
									}
								}
							}.start();
						} else if (Float.valueOf(total_score.getText()) < 0) {
							box.setMessage("该队伍总成绩小于零分，有误！");
							box.open();
						} else {
							// 总得分等于0
							if (rematch_flag) {
								abstain_btn.setEnabled(true);
								next_btn.setEnabled(true);
							} else {
								abstain_btn.setEnabled(true);
								next_btn.setEnabled(false);
							}
							new Thread() {
								public void run() {
									judgeScore1 = null;
									judgeScore2 = null;
									judgeScore3 = null;
									judgeScore4 = null;
									judgeScore5 = null;
									judgeScore6 = null;
									judgeScore7 = null;
									judgeScore8 = null;
									judgeScore9 = null;
									judgeScore10 = null;
									totalScore = null;
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judgeScore1 = score1
															.getText();
													judgeScore2 = score2
															.getText();
													judgeScore3 = score3
															.getText();
													judgeScore4 = score4
															.getText();
													judgeScore5 = score5
															.getText();
													judgeScore6 = score6
															.getText();
													judgeScore7 = score7
															.getText();
													judgeScore8 = score8
															.getText();
													judgeScore9 = score9
															.getText();
													judgeScore10 = score10
															.getText();
													totalScore = total_score
															.getText();
												}
											});
									int sum = mainClientOutputThread.sendScore(
											scoreReceiver, judgeScore1,
											judgeScore2, judgeScore3,
											judgeScore4, judgeScore5,
											judgeScore6, judgeScore7,
											judgeScore8, judgeScore9,
											judgeScore10, totalScore);
									if (sum == -1) {
										// 发送失败
										adressManager.clearIP();
										Display.getDefault().syncExec(
												new Runnable() {
													public void run() {
														MessageBox box1 = new MessageBox(
																ref_edit_shell,
																SWT.OK);
														box1.setText("提示");
														box1.setMessage("发送失败，请重发！！");
														int val = box1.open();
														if (val == SWT.OK)
															return;
													}
												});
									}
								}
							}.start();
						}
					} else {
						box.setMessage("数据库连接失败！！");
						box.open();
					}
				}
			}
		});
	}

	/**
	 * 下一只参赛队伍
	 * 
	 * @throws
	 */
	public void nextTeamButton() {
		next_btn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				cleanScore();
				rematch_flag = false;
				calc_btn.setEnabled(true);
				hasScore = false;
				reSelect = false;
				rePause = false;
				reGiveUp = false;
				combo = false;
				if (match_model_combo.getText() == null
						|| match_model_combo.getText().equals("")) {
					MessageBox box = new MessageBox(ref_edit_shell, SWT.OK);
					box.setText("提示");
					box.setMessage("请选择赛事模式！！");
					box.open();
				} else {
					matchOrder++;
					re_id = 0;
					MatchTeamScore query = new MatchTeamScore();
					if (query.isCollected()) {
						List<HashMap<String, Object>> data = query.getNextTeam(
								matchType, matchOrder, match_num, matchName);
						if (data.size() != 0) {
							id = Integer.valueOf(data.get(0).get("id")
									.toString());
							matchOrder = Integer.parseInt(data.get(0)
									.get("matchOrder").toString());
							// 比赛项目
							category = data.get(0).get("category").toString();
							// 参赛单位
							team = data.get(0).get("teamName").toString();
							// 赛事名称
							matchName = data.get(0).get("matchName").toString();
							title.setText(team);
							label_category.setText(category);
							if (category.indexOf("技巧") != -1) {
								score10.setEnabled(true);
							} else {
								score10.setEnabled(false);
							}
							// 裁判组数
							group_num = MainUI.configPanel.group_combo
									.getText();
							/*********************** 发送队伍信息给裁判长和打分裁判 ******************************/
							if (group_num.equals("1")) {
								// 只有一组裁判
								group.setText("A组");
							} else {// 两组裁判
								if (matchOrder % 2 == 0) {// 若为偶数次队伍
									group.setText("B组");
								} else if (matchOrder % 2 == 1) {// 若为奇数次队伍
									group.setText("A组");
								}
							}
							new Thread() {
								public void run() {
									int sum = mainClientOutputThread.sendTeam(
											teamReceiver, team, category,
											matchName, matchOrder, group_num);
									if (sum == -1) {// 发送失败
										adressManager.clearIP();
										Display.getDefault().syncExec(
												new Runnable() {
													public void run() {
														MessageBox box = new MessageBox(
																ref_edit_shell,
																SWT.OK);
														box.setText("提示");
														box.setMessage("发送失败，请重发！！");
														int val = box.open();
														if (val == SWT.OK)
															return;
													}
												});
									} else if (sum == 0) {// 无ip
										Display.getDefault().syncExec(
												new Runnable() {
													public void run() {
														MessageBox box = new MessageBox(
																ref_edit_shell,
																SWT.OK);
														box.setText("提示");
														box.setMessage("未获得接收地址，请稍等片刻！");
														int val = box.open();
														if (val == SWT.OK)
															return;
													}
												});
									}
								}
							}.start();
						} else {
							// 最后一只队伍,id减1
							matchOrder--;
							String strMatch = match_model_combo.getText();
							MessageBox box = new MessageBox(ref_edit_shell,
									SWT.OK);
							box.setText("提示");
							box.setMessage("第" + match_num + "场" + strMatch
									+ "已结束");
							box.open();
						}
					} else {
						MessageBox box = new MessageBox(ref_edit_shell, SWT.OK);
						box.setText("提示");
						box.setMessage("数据库连接失败！！");
						box.open();
					}
					query.close();
				}
				next_btn.setEnabled(false);
				abstain_btn.setEnabled(false);
				delay_btn.setEnabled(true);
			}
		});
	}

	/**
	 * 队伍暂停比赛
	 */
	public void delayButton() {
		delay_btn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				MessageBox messagebox = new MessageBox(ref_edit_shell,
						SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				messagebox.setText("提示");
				messagebox.setMessage("确定暂停该队伍比赛?");
				int message = messagebox.open();
				if (message == SWT.YES) {
					e.doit = true;

					cleanScore(); // 清空列表内容
					setScoreZore(); // 设置界面分数为零分
					calc_btn.setEnabled(false);
					hasScore = false;
					reSelect = false;
					rePause = true;
					reGiveUp = false;
					combo = false;

					MessageBox box = new MessageBox(ref_edit_shell, SWT.OK);
					box.setText("提示");
					if (match_model_combo.getText() == null
							|| match_model_combo.getText().equals("")) {
						box.setMessage("请选择赛事模式！");
						box.open();
					} else {
						MatchTeamScore query = new MatchTeamScore();
						if (query.isCollected()) {
							if (re_id != 0) {
								if (query.updateTeamStatu(re_id, 2) == -1) {
									box.setMessage("更改队伍状态失败！");
									box.open();
								} else {
									title.setText("该参赛队伍比赛暂停......");
									label_category.setText("");
									team_combo.setEnabled(true);
									category_combo.setEnabled(true);
								}
							} else if (query.updateTeamStatu(id, 2) == -1) {
								box.setMessage("更改队伍状态失败！");
								box.open();
							} else {
								title.setText("该参赛队伍比赛暂停......");
								label_category.setText("");
								team_combo.setEnabled(true);
								category_combo.setEnabled(true);
							}
							re_id = 0;
						} else {
							box.setMessage("数据库连接失败！！");
							box.open();
						}
						query.close();
					}
					next_btn.setEnabled(true);
					delay_btn.setEnabled(false);
					abstain_btn.setEnabled(false);

				} else {
					e.doit = false;
				}
			}
		});
	}

	/**
	 * 弃权比赛
	 */
	public void abstainButton() {

		abstain_btn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				cleanScore();
				calc_btn.setEnabled(false);
				hasScore = false;
				reSelect = false;
				rePause = false;
				reGiveUp = true;
				combo = false;
				MessageBox box = new MessageBox(ref_edit_shell, SWT.OK);
				box.setText("提示");
				if (match_model_combo.getText() == null
						|| match_model_combo.getText().equals("")) {
					box.setMessage("请选择赛事模式！！");
					box.open();
				} else {
					MatchTeamScore query = new MatchTeamScore();
					if (query.isCollected()) {
						if (re_id != 0) {
							if (query.updateTeamStatu(re_id, 3) == 0) {
								box.setMessage("更改队伍状态失败！");
								box.open();
							} else {
								title.setText("该参赛队伍弃权比赛......");
								label_category.setText("");
							}
							re_id = 0;
						} else if (query.updateTeamStatu(id, 3) == 0) {
							box.setMessage("更改队伍状态失败！");
							box.open();
						} else {
							title.setText("该参赛队伍弃权比赛......");
							label_category.setText("");
						}
					} else {
						box.setMessage("数据库连接失败！！");
						box.open();
					}
					query.close();
				}
				next_btn.setEnabled(true);
				abstain_btn.setEnabled(false);
			}
		});
	}

	/**
	 * 选择延迟比赛参赛队伍发生的事件
	 */
	public void teamComboEvent() {
		team_combo.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent arg0) {
				team_combo.removeAll();

				// 得到推迟比赛队伍的信息
				MatchTeamScore query = new MatchTeamScore();
				List<String> noRaceTeam = query.getNoRaceTeam(replayMatchName,
						replay_matchType);
				for (int i = 0; i < noRaceTeam.size(); i++) {
					team_combo.add(noRaceTeam.get(i));
				}
				category_combo.removeAll();
			}

			@Override
			public void focusLost(FocusEvent arg0) {
			}
		});
	}

	/**
	 * 选择延迟比赛参赛队伍的参赛项目发生的事件
	 */
	public void categoryComboEvent() {
		category_combo.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				category_combo.removeAll();
				// 得到推迟比赛队伍的参赛项目信息
				MatchTeamScore query = new MatchTeamScore();
				List<String> noRaceCategory = query.getNoRaceCategory(
						replayMatchName, team_combo.getText(), replay_matchType);
				for (int i = 0; i < noRaceCategory.size(); i++) {
					category_combo.add(noRaceCategory.get(i));
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
			}
		});

		category_combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (category_combo.getText() != null
						&& !category_combo.getText().equalsIgnoreCase("")) {
					replay_btn.setEnabled(true);
				} else {
					replay_btn.setEnabled(false);
				}
				MatchTeamScore query = new MatchTeamScore();
				re_id = query.getNoRaceTeamID(replayMatchName,
						replay_matchType, team_combo.getText(),
						category_combo.getText());
				abstain_btn.setEnabled(false);
			}
		});
	}

	/**
	 * 暂停比赛队伍重新开始比赛
	 */
	public void replayButton() {
		replay_btn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				team = team_combo.getText();
				category = category_combo.getText();

				MessageBox box = new MessageBox(ref_edit_shell);
				box.setText("提示");
				if (team == null || team.equals("")) {
					box.setMessage("补赛中队伍未选");
					box.open();
					return;
				}
				if (category == null || category.equals("")) {
					box.setMessage("补赛中项目未选");
					box.open();
					return;
				}
				cleanScore();
				MatchTeamScore query = new MatchTeamScore();
				List<HashMap<String, Object>> data = query.getReTeam(re_id);
				// 出场顺序
				re_matchOrder = Integer.valueOf(data.get(0).get("matchOrder")
						.toString());

				if (category.indexOf("技巧") != -1) {
					score10.setEnabled(true);
				} else {
					score10.setEnabled(false);
				}
				title.setText(team);
				label_category.setText(category);

				team_combo.clearSelection();
				category_combo.clearSelection();
				team_combo.removeAll();
				category_combo.removeAll();

				next_btn.setEnabled(false);
				calc_btn.setEnabled(true);
				rematch_flag = true;

				if (hasScore == false && re_id != 0 && reSelect == false
						&& rePause == false && reGiveUp == false || combo) {
					if (matchOrder > 0) {
						matchOrder--;
					}
				}

				hasScore = false;
				reSelect = true;
				rePause = false;
				reGiveUp = false;
				combo = false;
				group_num = MainUI.configPanel.group_combo.getText();// 裁判组数

				/*********************** 发送队伍信息给裁判长和打分裁判 ******************************/
				if (group_num.equals("1")) {
					// 只有一组裁判
					group.setText("A组");
				} else {
					// 两组裁判
					if (re_matchOrder % 2 == 0) { // 若为偶数次队伍
						group.setText("B组");
					} else if (re_matchOrder % 2 == 1) { // 若为奇数次队伍
						group.setText("A组");
					}
				}
				new Thread() {
					public void run() {
						int sum = mainClientOutputThread.sendTeam(teamReceiver,
								team, category, replayMatchName, re_matchOrder,
								group_num);
						if (sum == -1) { // 发送失败
							adressManager.clearIP();
							Display.getDefault().syncExec(new Runnable() {
								public void run() {
									MessageBox box = new MessageBox(
											ref_edit_shell, SWT.OK);
									box.setText("提示");
									box.setMessage("发送失败，请重发！！");
									int val = box.open();
									if (val == SWT.OK)
										return;
								}
							});
						} else if (sum == 0) {
							// 无ip
							Display.getDefault().syncExec(new Runnable() {
								public void run() {
									MessageBox box = new MessageBox(
											ref_edit_shell, SWT.OK);
									box.setText("提示");
									box.setMessage("未获得接收地址，请稍等片刻！");
									int val = box.open();
									if (val == SWT.OK)
										return;
								}
							});
						}
					}
				}.start();
			}
		});
	}

	/**
	 * 发送失败时重新发送按钮
	 */
	public void resendButton() {
		resend_btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new Thread() {
					public void run() {
						totalScore = null;
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
								totalScore = total_score.getText();
								group_num = MainUI.configPanel.group_combo
										.getText();// 裁判组数

							}
						});
						if (totalScore == null || totalScore.equals("")) {
							int sum;
							// 出场顺序
							if (rematch_flag) {
								MatchTeamScore query = new MatchTeamScore();
								List<HashMap<String, Object>> data = query
										.getReTeam(re_id);

								re_matchOrder = Integer.valueOf(data.get(0)
										.get("matchOrder").toString());
								Display.getDefault().syncExec(new Runnable() {
									public void run() {
										if (group_num.equals("1")) {// 只有一组裁判
											group.setText("A组");
										} else {// 两组裁判
											if (re_matchOrder % 2 == 0) {// 若为偶数次队伍
												group.setText("B组");
											} else if (re_matchOrder % 2 == 1) {// 若为奇数次队伍
												group.setText("A组");
											}
										}
									}
								});
								sum = mainClientOutputThread.sendTeam(
										teamReceiver, team, category,
										replayMatchName, re_matchOrder,
										group_num);
							} else {
								Display.getDefault().syncExec(new Runnable() {
									public void run() {
										if (group_num.equals("1")) {// 只有一组裁判
											group.setText("A组");
										} else {// 两组裁判
											if (matchOrder % 2 == 0) {// 若为偶数次队伍
												group.setText("B组");
											} else if (matchOrder % 2 == 1) {// 若为奇数次队伍
												group.setText("A组");
											}
										}
									}
								});
								sum = mainClientOutputThread.sendTeam(
										teamReceiver, team, category,
										matchName, matchOrder, group_num);
							}
							if (sum == -1) {// 发送失败
								adressManager.clearIP();
								Display.getDefault().syncExec(new Runnable() {
									public void run() {
										MessageBox box = new MessageBox(
												ref_edit_shell, SWT.OK);
										box.setText("提示");
										box.setMessage("发送失败，请重发！！");
										int val = box.open();
										if (val == SWT.OK)
											return;
									}
								});
							} else if (sum == 0) {// 无ip
								Display.getDefault().syncExec(new Runnable() {
									public void run() {
										MessageBox box = new MessageBox(
												ref_edit_shell, SWT.OK);
										box.setText("提示");
										box.setMessage("未获得接收地址，请稍等片刻！");
										int val = box.open();
										if (val == SWT.OK)
											return;
									}
								});
							}
						} else {
							judgeScore1 = null;
							judgeScore2 = null;
							judgeScore3 = null;
							judgeScore4 = null;
							judgeScore5 = null;
							judgeScore6 = null;
							judgeScore7 = null;
							judgeScore8 = null;
							judgeScore9 = null;
							judgeScore10 = null;
							totalScore = null;
							Display.getDefault().syncExec(new Runnable() {
								public void run() {
									judgeScore1 = score1.getText();
									judgeScore2 = score2.getText();
									judgeScore3 = score3.getText();
									judgeScore4 = score4.getText();
									judgeScore5 = score5.getText();
									judgeScore6 = score6.getText();
									judgeScore7 = score7.getText();
									judgeScore8 = score8.getText();
									judgeScore9 = score9.getText();
									judgeScore10 = score10.getText();
									totalScore = total_score.getText();
								}
							});
							int sum = mainClientOutputThread.sendScore(
									scoreReceiver, judgeScore1, judgeScore2,
									judgeScore3, judgeScore4, judgeScore5,
									judgeScore6, judgeScore7, judgeScore8,
									judgeScore9, judgeScore10, totalScore);
							if (sum == -1) {// 发送失败
								adressManager.clearIP();
								Display.getDefault().syncExec(new Runnable() {
									public void run() {
										MessageBox box1 = new MessageBox(
												ref_edit_shell, SWT.OK);
										box1.setText("提示");
										box1.setMessage("发送失败，请重发！！");
										int val = box1.open();
										if (val == SWT.OK)
											return;
									}
								});
							} else if (sum == 0) {// 无ip
								Display.getDefault().syncExec(new Runnable() {
									public void run() {
										MessageBox box = new MessageBox(
												ref_edit_shell, SWT.OK);
										box.setText("提示");
										box.setMessage("未获得接收地址，请稍等片刻！");
										int val = box.open();
										if (val == SWT.OK)
											return;
									}
								});
							}
						}
					}
				}.start();
			}
		});
	}

	/**
	 * 重新开始比赛----赛事模式
	 */
	public void replayComboEvent() {
		replay_match_model_combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				MatchTeamScore query = new MatchTeamScore();

				MessageBox box = new MessageBox(ref_edit_shell, SWT.OK);
				box.setText("提示");

				int replay_key = replay_match_model_combo.getSelectionIndex();
				replay_matchType = Integer.valueOf(String
						.valueOf(replay_match_model_combo.getData(String
								.valueOf(replay_key))));

				if (query.isCollected()) {
					if (query.getAllReplayMatchNames().length == 0) {
						box.setMessage("无补赛信息！");
						box.open();
						return;
					}
					ReplayMatchNameDialog dialog = new ReplayMatchNameDialog(
							ref_edit_shell);
					if (replayMatchName != null && !replayMatchName.equals("")
							&& !replayMatchName.equals("noMatch")) {
						dialog.setMatchName(replayMatchName);
					}

					dialog.setMatchNames(query.getAllReplayMatchNames());
					dialog.open();
					replayMatchName = dialog.getMatchName();
					if (query.isCollected()) {
						query.close();
					}
				} else {
					box.setMessage("连接数据库失败！！");
					box.open();
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
	 * @return MatchPanel
	 * @throws
	 */
	public static MatchPanel getInstance(Shell shell, Composite parent,
			int style) {
		if (panel == null) {
			panel = new MatchPanel(shell, parent, style);
		}
		return panel;
	}

	/**
	 * 清楚界面上的分数
	 */
	public void cleanScore() {
		title.setText("");
		label_category.setText("");
		score1.setText("");
		score2.setText("");
		score3.setText("");
		score4.setText("");
		score5.setText("");
		score6.setText("");
		score7.setText("");
		score8.setText("");
		score9.setText("");
		score10.setText("");
		deduction_score.setText("");
		total_score.setText("");
	}

	/**
	 * 设置界面分数为零分
	 */
	public void setScoreZore() {
		title.setText("");
		label_category.setText("");
		score1.setText("0");
		score2.setText("0");
		score3.setText("0");
		score4.setText("0");
		score5.setText("0");
		score6.setText("0");
		score7.setText("0");
		score8.setText("0");
		score9.setText("0");
		score10.setText("0");
		deduction_score.setText("0");
		total_score.setText("0");
	}

	/**
	 * 正则表达式判断输入是否一位小数
	 */
	public boolean regVerify() {
		MessageBox box = new MessageBox(ref_edit_shell, SWT.OK);
		box.setText("提示");

		Pattern p = Pattern.compile("\\d{0,3}(\\.\\d)?");

		boolean b = true;

		if (score1.getText() != null && !score1.getText().equals("")) {
			if (!p.matcher(score1.getText()).matches()) {
				box.setMessage("'裁判1分数'输入不合法,保留一位小数");
				box.open();
				b = false;
			}
		} else {
			box.setMessage("'裁判1分数'不能为空");
			box.open();
			b = false;
		}

		if (score2.getText() != null && !score2.getText().equals("")) {
			if (!p.matcher(score2.getText()).matches()) {
				box.setMessage("'裁判2分数'输入不合法,保留一位小数");
				box.open();
				b = false;
			}
		} else {
			box.setMessage("'裁判2分数'不能为空");
			box.open();
			b = false;
		}

		if (score3.getText() != null && !score3.getText().equals("")) {
			if (!p.matcher(score3.getText()).matches()) {
				box.setMessage("'裁判3分数'输入不合法,保留一位小数");
				box.open();
				b = false;
			}
		} else {
			box.setMessage("'裁判3分数'不能为空");
			box.open();
			b = false;
		}

		if (score4.getText() != null && !score4.getText().equals("")) {
			if (!p.matcher(score4.getText()).matches()) {
				box.setMessage("'裁判4分数'输入不合法,保留一位小数");
				box.open();
				b = false;
			}
		} else {
			box.setMessage("'裁判4分数'不能为空");
			box.open();
			b = false;
		}

		if (score5.getText() != null && !score5.getText().equals("")) {
			if (!p.matcher(score5.getText()).matches()) {
				box.setMessage("'裁判5分数'输入不合法,保留一位小数");
				box.open();
				b = false;
			}
		} else {
			box.setMessage("'裁判5分数'不能为空");
			box.open();
			b = false;
		}

		if (score6.getText() != null && !score6.getText().equals("")) {
			if (!p.matcher(score6.getText()).matches()) {
				box.setMessage("'裁判6分数'输入不合法,保留一位小数");
				box.open();
				b = false;
			}
		} else {
			box.setMessage("'裁判6分数'不能为空");
			box.open();
			b = false;
		}

		if (score7.getText() != null && !score7.getText().equals("")) {
			if (!p.matcher(score7.getText()).matches()) {
				box.setMessage("'裁判7分数'输入不合法,保留一位小数");
				box.open();
				b = false;
			}
		} else {
			box.setMessage("'裁判7分数'不能为空");
			box.open();
			b = false;
		}

		if (score8.getText() != null && !score8.getText().equals("")) {
			if (!p.matcher(score8.getText()).matches()) {
				box.setMessage("'裁判8分数'输入不合法,保留一位小数");
				box.open();
				b = false;
			}
		} else {
			box.setMessage("'裁判8分数'不能为空");
			box.open();
			b = false;
		}

		if (score9.getText() != null && !score9.getText().equals("")) {
			if (!p.matcher(score9.getText()).matches()) {
				box.setMessage("'裁判9分数'输入不合法,保留一位小数");
				box.open();
				b = false;
			}
		} else {
			box.setMessage("'裁判9分数'不能为空");
			box.open();
			b = false;
		}

		if (category.indexOf("技巧") != -1) {
			if (score10.getText() != null && !score10.getText().equals("")) {
				if (!p.matcher(score10.getText()).matches()) {
					box.setMessage("'裁判10分数'输入不合法,保留一位小数");
					box.open();
					b = false;
				}
			} else {
				box.setMessage("'技巧比赛--裁判10分数'不能为空");
				box.open();
				b = false;
			}
		}

		if (deduction_score.getText() != null
				&& !deduction_score.getText().equals("")) {
			if (!p.matcher(deduction_score.getText()).matches()) {
				box.setMessage("'裁判长减分'输入不合法,保留一位小数");
				box.open();
				b = false;
			}
		}

		return b;
	}

	/**
	 * 排名按钮触发事件
	 */
	public void rankButton() {

		rank_btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				QueryScore query = new QueryScore();

				MessageBox box = new MessageBox(ref_edit_shell, SWT.OK);
				box.setText("提示");
				if (re_id != 0) {
					if (replay_matchType == -1 || replayMatchName == null
							|| replayMatchName.length() == 0) {
						box.setMessage("程序错误!!赛事名称或者赛事模式没有值!!");
						box.open();
					} else {
						RankPanel window = new RankPanel();
						window.id = re_id;
						window.matchType = replay_matchType;
						window.matchName = replayMatchName;
						window.open();
					}
				} else if (matchOrder < 1 || matchName.equals("noMatch")) { // 不考虑matchType
					if (query.isCollected()) {
						int matchType = -1;
						String matchName_temp;

						// 当matchName不为空时，跳出选择界面，让用户选择赛事模式
						if (query.getAllMatchNames().length == 0) {
							box.setMessage("无有成绩赛事！！");
							box.open();
							return;
						}
						MatchNameDialog dialog = new MatchNameDialog(
								ref_edit_shell);
						if (matchName != null && !matchName.equals("")
								&& !matchName.equals("noMatch")) {
							dialog.setMatchName(matchName);
						}
						dialog.setMatchNames(query.getAllMatchNames());
						dialog.open();
						matchName_temp = dialog.getMatchName();
						matchType = dialog.getMatchType();
						if (matchName_temp != null
								&& !matchName_temp.equals("")
								&& matchType != -1) {
							int temp = query.getTeamMinId(matchType,
									matchName_temp);
							switch (temp) {
							case -1: {
								box.setMessage("暂无成绩！！");
								box.open();
								break;
							}
							default:
								RankPanel window = new RankPanel();
								window.id = temp;
								window.matchType = matchType;
								window.matchName = matchName_temp;
								window.open();
							}
						}
						if (query.isCollected()) {
							query.close();
						}
					} else {
						box.setMessage("连接数据库失败！！");
						box.open();
					}
				} else {
					int temp;
					if (match_num == -1) {
						box.setMessage("请选择场次！！");
						box.open();
					} else if (matchName == null || matchName.equals("")) {
						box.setMessage("赛事名称不能为空，程序错误！！");
						box.open();
					} else {
						temp = query.getTeamMinId(matchType, matchName,
								match_num, matchOrder);
						RankPanel window = new RankPanel();
						window.id = temp;
						window.matchType = matchType;
						window.matchName = matchName;
						window.open();
					}
				}
			}
		});
	}

	public static Text getScore1() {
		return score1;
	}

	public static void setScore1(Text score1) {
		MatchPanel.score1 = score1;
	}

	public static Text getScore2() {
		return score2;
	}

	public static void setScore2(Text score2) {
		MatchPanel.score2 = score2;
	}

	public static Text getScore3() {
		return score3;
	}

	public static void setScore3(Text score3) {
		MatchPanel.score3 = score3;
	}

	public static Text getScore4() {
		return score4;
	}

	public static void setScore4(Text score4) {
		MatchPanel.score4 = score4;
	}

	public static Text getScore5() {
		return score5;
	}

	public static void setScore5(Text score5) {
		MatchPanel.score5 = score5;
	}

	public static Text getScore6() {
		return score6;
	}

	public static void setScore6(Text score6) {
		MatchPanel.score6 = score6;
	}

	public static Text getScore7() {
		return score7;
	}

	public static void setScore7(Text score7) {
		MatchPanel.score7 = score7;
	}

	public static Text getScore8() {
		return score8;
	}

	public static void setScore8(Text score8) {
		MatchPanel.score8 = score8;
	}

	public static Text getScore9() {
		return score9;
	}

	public static void setScore9(Text score9) {
		MatchPanel.score9 = score9;
	}

	public static Text getScore10() {
		return score10;
	}

	public static void setScore10(Text score10) {
		MatchPanel.score10 = score10;
	}

	public static Text getDeduction_score() {
		return deduction_score;
	}

	public static void setDeduction_score(Text deduction_score) {
		MatchPanel.deduction_score = deduction_score;
	}
}
