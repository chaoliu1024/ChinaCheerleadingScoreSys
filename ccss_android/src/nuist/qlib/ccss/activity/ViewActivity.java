/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nuist.qlib.ccss.adapter.MatchTypesAdapter;
import nuist.qlib.ccss.dao.IPDao;
import nuist.qlib.ccss.dao.ScoreDAO;
import nuist.qlib.ccss.net.SendMessage;
import nuist.qlib.ccss.thread.LoginOutThread;
import nuist.qlib.ccss.ui.ProgressBackDialog;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 裁判长界面
 * 
 * @author Zhengfei Chen
 * @author Chao Liu
 * @since ccss 1.0
 */

public class ViewActivity extends Activity {

	private static final String TAG = "ScoreActivity";
	public static MyReceiver receiver;
	public static IntentFilter filter;
	// 比赛名称
	private String match_name = "";
	// 项目名称
	private String category_name = "";
	// 赛事模式
	private int matchType = -1;
	private String error = "";
	private List<HashMap<String, Object>> categorys;
	private TextView team_name;
	private TextView categoryName;
	private TextView role_content;
	private TextView state_content;
	private TextView referee1;
	private TextView referee2;
	private TextView referee3;
	private TextView referee4;
	private TextView referee5;
	private TextView referee6;
	private TextView referee7;
	private TextView referee8;
	private TextView referee9;
	private TextView referee10;
	private TextView subJudge;
	private EditText subJudge_score;
	private TextView total_score;
	private Button sendSubJudgeScore;
	private Button rankBut;
	private Spinner spinner;
	private IPDao ipDao;
	private String sub_score;
	private File path;
	private List<HashMap<String, Object>> matchTypes;
	private List<String> matchNames;
	public static ProgressDialog dialog;
	public static HandlerThread threadt;
	private MyHandler handler;
	private Runnable mBackgroundRunnable;
	private boolean login = true;
	private String role;
	// 线程次数
	private int i = -1;
	private List<Handler> handlers = new ArrayList<Handler>();
	// 非负浮点数 ,且范围在0~100
	private String eL = "^\\d{1,2}(\\.\\d{1})?$";
	// 三位整数
	private String eL2 = "^\\d{3}?$";
	private Pattern pattern = Pattern.compile(eL);
	private Pattern pattern2 = Pattern.compile(eL2);
	private Matcher matcher;
	private Matcher matcher2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 无标题模式
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.judges_view);
		team_name = (TextView) this.findViewById(R.id.team_name); // 参赛队伍信息
		categoryName = (TextView) this.findViewById(R.id.category_name); // 参赛项目
		role_content = (TextView) this.findViewById(R.id.role_content); // 身份信息
		state_content = (TextView) this.findViewById(R.id.state_content); // 当前状态信息
		referee1 = (TextView) this.findViewById(R.id.textscore1); // 得分一
		referee2 = (TextView) this.findViewById(R.id.textscore2); // 得分二
		referee3 = (TextView) this.findViewById(R.id.textscore3); // 得分三
		referee4 = (TextView) this.findViewById(R.id.textscore4); // 得分四
		referee5 = (TextView) this.findViewById(R.id.textscore5); // 得分五
		referee6 = (TextView) this.findViewById(R.id.textscore6); // 得分六
		referee7 = (TextView) this.findViewById(R.id.textscore7); // 得分七
		referee8 = (TextView) this.findViewById(R.id.textscore8); // 得分八
		referee9 = (TextView) this.findViewById(R.id.textscore9); // 得分九
		referee10 = (TextView) this.findViewById(R.id.textscore10); // 得分十
		subJudge = (TextView) this.findViewById(R.id.subJudge); // 裁判长减分
		subJudge_score = (EditText) this.findViewById(R.id.subJudge_score); // 裁判长减分
		total_score = (TextView) this.findViewById(R.id.total_score); // 总得分
		sendSubJudgeScore = (Button) this.findViewById(R.id.sendSubJudgeScore); // 发送裁判长减分
		rankBut = (Button) this.findViewById(R.id.lala_score_order); // 排名
		spinner = (Spinner) this.findViewById(R.id.spinner); // 赛事类型
		role = LoginActivity.role;
		if (role.equals("chiefJudge01")) {
			subJudge.setVisibility(View.VISIBLE);
			subJudge_score.setVisibility(View.VISIBLE);
			sendSubJudgeScore.setVisibility(View.VISIBLE);
			subJudge_score.setText("0"); // 设置裁判长减分默认值
		}
		role_content.setText(LoginActivity.roleName); // 设置身份
		state_content.setText("未打分"); // 初始化状态
		path = this.getFilesDir();

		// 设置赛事类型
		matchTypes = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> one = new HashMap<String, Object>();
		one.put("name", "预赛");
		one.put("value", 0);
		matchTypes.add(one);
		one = new HashMap<String, Object>();
		one.put("name", "决赛");
		one.put("value", 1);
		matchTypes.add(one);
		spinner.setAdapter(new MatchTypesAdapter(ViewActivity.this, matchTypes));
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				matchType = Integer.valueOf(matchTypes.get(arg2).get("value")
						.toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		rankBut.setOnClickListener(new OnClickListener() {
			@SuppressLint("ShowToast")
			@Override
			public void onClick(View v) {
				if (matchType == -1) {
					Toast.makeText(ViewActivity.this, "未选择赛事模式，请选择赛事模式！",
							Toast.LENGTH_LONG);
					return;
				}
				if (match_name.equals("")) {
					// 根据赛事模式选择比赛，并将比赛的项目名称取出来跳转到排序页面
					getLaLaScoreMatchNames();
				} else if (!category_name.equals("")) {
					Intent intent = new Intent(ViewActivity.this,
							ScoreOrderWeb.class);
					intent.putExtra("matchName", match_name);
					intent.putExtra("matchCategory", category_name);
					intent.putExtra("matchType", matchType);
					ViewActivity.this.startActivity(intent);
				} else {
					Toast.makeText(ViewActivity.this, "程序错误,无项目！",
							Toast.LENGTH_LONG);
				}
			}
		});
		// 接收服务发过来的参赛队伍和比赛项目、得分等信息
		if (receiver == null) {
			receiver = new MyReceiver();
			filter = new IntentFilter();
			filter.addAction("android.intent.action.MY_RECEIVER");
			ViewActivity.this.registerReceiver(receiver, filter); // 动态注册广播信息
		}

		// 发送裁判长减分
		sendSubJudgeScore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				sub_score = subJudge_score.getText().toString();
				matcher = pattern.matcher(sub_score);
				matcher2 = pattern2.matcher(sub_score);
				if (!(matcher.matches() || (matcher2.matches() && Integer
						.valueOf(sub_score) == 100))) {// 判断打分是够为0~100
					Toast.makeText(ViewActivity.this, "请确保打分范围为0~100！",
							Toast.LENGTH_SHORT).show(); // 提示打分超出范围
					return;
				} else {
					ipDao = new IPDao(path);
					List<String> list = new ArrayList<String>();
					try {
						// 接收者名称(记录员)
						String receiver[] = { "Editor" };
						// 获取IP
						list = ipDao.getIP(receiver);

						if (list.size() == 0) {
							// 无ip
							sendSubJudgeScore.setEnabled(true);
							subJudge_score.setEnabled(true);
							state_content.setText("未打分");
							new AlertDialog.Builder(ViewActivity.this)
									.setTitle("提示")
									.setMessage("未获得接收地址，请稍等片刻！")
									.setPositiveButton("确定", null).show();
							return;
						} else {
							FutureTask<Integer> task = new FutureTask<Integer>(
									new SendMessage(list.get(0), sub_score));
							new Thread(task).start();
							if (task.get() == 1) {
								sendSubJudgeScore.setEnabled(false);
								subJudge_score.setEnabled(false);
								state_content.setText("已打分");
								Toast.makeText(ViewActivity.this, "打分成功！",
										Toast.LENGTH_SHORT).show();
							} else {
								// 清空配置文件
								ipDao.clearIP();
								sendSubJudgeScore.setEnabled(true);
								subJudge_score.setEnabled(true);
								state_content.setText("未打分");
								new AlertDialog.Builder(ViewActivity.this)
										.setTitle("提示").setMessage("发送失败，请重发！")
										.setPositiveButton("确定", null).show();
								return;
							}
						}
					} catch (Exception e) {
						Log.e(TAG, e.getMessage());
					}
				}
			}
		});

	}

	@Override
	protected void onDestroy() {
		if (handler != null) {
			handler.removeCallbacks(mBackgroundRunnable);
		}
		if (LoginActivity.BroadcastIPIntent != null) {
			// 停止组播IP的服务
			stopService(LoginActivity.BroadcastIPIntent);
		}
		if (receiver != null) {
			unregisterReceiver(receiver);
			receiver = null;
		}
		super.onDestroy();
	}

	/**
	 * 广播接收器
	 */
	private class MyReceiver extends BroadcastReceiver {
		private String item;

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			item = bundle.getString("item");
			if (item.contains("judge")) { // 接收得分
				switch (Integer.valueOf(bundle.getString("num"))) {
				case 1:
					referee1.setText(intent.getStringExtra("score1")); // 技巧裁判1
					break;
				case 2:
					referee2.setText(intent.getStringExtra("score2")); // 技巧裁判2
					break;
				case 3:
					referee3.setText(intent.getStringExtra("score3")); // 技巧裁判3
					break;
				case 4:
					referee4.setText(intent.getStringExtra("score4")); // 技巧裁判4
					break;
				case 5:
					referee5.setText(intent.getStringExtra("score5")); // 技巧裁判5
					break;
				case 6:
					referee6.setText(intent.getStringExtra("score6")); // 技巧裁判6
					break;
				case 7:
					referee7.setText(intent.getStringExtra("score7")); // 技巧裁判7
					break;
				case 8:
					referee8.setText(intent.getStringExtra("score8")); // 技巧裁判8
					break;
				case 9:
					referee9.setText(intent.getStringExtra("score9")); // 技巧裁判9
					break;
				case 10:
					referee10.setText(intent.getStringExtra("score10")); // 技巧裁判10
					break;
				}
			} else if (item.equalsIgnoreCase("infor1")) { // 接收编辑员发过来的消息
				cleanScore();
				subJudge_score.setText("0");
				sendSubJudgeScore.setEnabled(true);
				subJudge_score.setEnabled(true);
				state_content.setText("未打分");
				team_name.setText(intent.getStringExtra("team_name"));
				categoryName.setText(intent.getStringExtra("category_name"));
			} else if (item.equalsIgnoreCase("infor2")) { // 接收编辑员发过来的消息
				cleanScore();
				subJudge_score.setText("0");
				sendSubJudgeScore.setEnabled(true);
				subJudge_score.setEnabled(true);
				state_content.setText("未打分");
				team_name.setText(intent.getStringExtra("team_name"));
				categoryName.setText(intent.getStringExtra("category_name"));
				match_name = intent.getStringExtra("match_name");
				category_name = intent.getStringExtra("category_name");
			} else if (item.equalsIgnoreCase("all")) {
				// 接收编辑员发过来的成绩
				cleanScore();
				sendSubJudgeScore.setEnabled(false);
				subJudge_score.setEnabled(false);
				state_content.setText("已打分");
				referee1.setText(intent.getStringExtra("score1"));
				referee2.setText(intent.getStringExtra("score2"));
				referee3.setText(intent.getStringExtra("score3"));
				referee4.setText(intent.getStringExtra("score4"));
				referee5.setText(intent.getStringExtra("score5"));
				referee6.setText(intent.getStringExtra("score6"));
				referee7.setText(intent.getStringExtra("score7"));
				referee8.setText(intent.getStringExtra("score8"));
				referee9.setText(intent.getStringExtra("score9"));
				referee10.setText(intent.getStringExtra("score10"));
				total_score.setText(intent.getStringExtra("totalscore"));
			}
		}
	}

	/**
	 * 清楚界面上的分数
	 */
	public void cleanScore() {
		referee1.setText("");
		referee2.setText("");
		referee3.setText("");
		referee4.setText("");
		referee5.setText("");
		referee6.setText("");
		referee7.setText("");
		referee8.setText("");
		referee9.setText("");
		referee10.setText("");
		total_score.setText("");
	}

	/***
	 * 获取某模式下的赛事名称并且是有成绩的
	 */
	public void getLaLaScoreMatchNames() {
		i++;
		dialog = new ProgressBackDialog(this).getProgressDialog("请稍候...",
				"正在加载赛事名称中...");
		dialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				handler.setStop(true);
			}
		});
		dialog.show();

		threadt = new HandlerThread("post" + i);
		threadt.start();

		mBackgroundRunnable = new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				int index = i;
				ScoreDAO dao = new ScoreDAO(ViewActivity.this.getFilesDir());
				if (dao.checkConfig()) {
					HashMap<String, Object> result = dao
							.getLaLaScoreMatchNames(matchType);
					if (result.get("message").equals("success")) {
						matchNames = (List<String>) result.get("matchNames");
						categorys = (List<HashMap<String, Object>>) result
								.get("categorys");
						handlers.get(index).sendEmptyMessage(2);
					} else {
						error = result.get("message").toString();
						handlers.get(index).sendEmptyMessage(1);
					}
				} else
					handlers.get(index).sendEmptyMessage(0);
			}
		};

		handler = new MyHandler(threadt.getLooper());
		handler.setMark(0);
		handlers.add(handler);
		handler.post(mBackgroundRunnable);
	}

	private class MyHandler extends Handler {
		private boolean isStop = false;
		// 0表示执行获得赛事名称线程
		private int mark = 0;

		public void setStop(boolean isStop) {
			this.isStop = isStop;
		}

		public void setMark(int mark) {
			this.mark = mark;
		}

		public MyHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {

			if (isStop)
				return;
			switch (mark) {
			case 0:
				switch (msg.what) {
				case 0:
					dialog.dismiss();
					Toast.makeText(ViewActivity.this, "网络不畅通，请检查网络！",
							Toast.LENGTH_LONG);
					break;
				case 1:
					dialog.dismiss();
					Toast.makeText(ViewActivity.this, error, Toast.LENGTH_LONG)
							.show();
					break;
				case 2:
					dialog.dismiss();
					if (matchNames.size() == 0) {
						Toast.makeText(ViewActivity.this, "无赛事！",
								Toast.LENGTH_LONG).show();
						return;
					}
					final View view = LayoutInflater.from(ViewActivity.this)
							.inflate(R.layout.lalascore_matchnames_spinner,
									null);
					final Spinner spinner = (Spinner) view
							.findViewById(R.id.matchNames);
					spinner.setAdapter(new ArrayAdapter(ViewActivity.this,
							android.R.layout.simple_list_item_1, matchNames));
					new AlertDialog.Builder(ViewActivity.this)
							.setTitle("赛事名称选择")
							.setView(view)
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											String matchName = (String) spinner
													.getSelectedItem()
													.toString();
											if (matchName == null
													|| matchName.equals("")) {
												Toast.makeText(
														ViewActivity.this,
														"请选择赛事名称",
														Toast.LENGTH_SHORT)
														.show();
											} else {
												dialog.dismiss();
												String category = "";
												for (int i = 0; i < categorys
														.size(); i++) {
													String matchName_temp = categorys
															.get(i)
															.get("matchName")
															.toString();
													if (matchName
															.equals(matchName_temp)) {
														category = categorys
																.get(i)
																.get("category")
																.toString();
														break;
													}
												}
												if (category.equals("")) {
													Toast.makeText(
															ViewActivity.this,
															"出现程序错误，无项目！",
															Toast.LENGTH_LONG)
															.show();
												} else {
													Intent intent = new Intent(
															ViewActivity.this,
															ScoreOrderWeb.class);
													intent.putExtra(
															"matchName",
															matchName);
													intent.putExtra(
															"matchCategory",
															category);
													intent.putExtra(
															"matchType",
															matchType);
													ViewActivity.this
															.startActivity(intent);
												}
											}
										}
									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									}).show();

				}
				break;
			}

		}
	}

	@SuppressLint("HandlerLeak")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (handler != null) {
				handler.removeCallbacks(mBackgroundRunnable);
			}
			Handler outHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					if (LoginActivity.BroadcastIPIntent != null) {
						// 停止组播IP的服务
						stopService(LoginActivity.BroadcastIPIntent);
					}
					if (receiver != null) {
						unregisterReceiver(receiver);
						receiver = null;
					}
					finish();
				}
			};
			new LoginOutThread(this, role, login, outHandler).loginOut();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}
