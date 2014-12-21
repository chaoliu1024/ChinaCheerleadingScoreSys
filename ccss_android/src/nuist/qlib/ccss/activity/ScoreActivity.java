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

import nuist.qlib.ccss.dao.IPDao;
import nuist.qlib.ccss.dao.LoginDao;
import nuist.qlib.ccss.net.SendMessage;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 打分界面
 * 
 * @author Zhengfei Chen
 * @author Chao Liu
 * @since ccss 1.0
 */
public class ScoreActivity extends Activity {

	private static final String TAG = "ScoreActivity";
	private File path;
	private MyReceiver receiver = null;
	public static IntentFilter filter;
	private String scoreValue;
	private TextView units_content;
	private TextView category_content;
	private TextView role_content;
	private TextView state_content;
	private EditText score;
	private Button send;
	private IPDao ipDao;
	private HandlerThread threadt;
	private ProgressDialog loginOutdialog;
	private boolean login = true;
	private String role;
	private String roleName;
	private String error;
	private int i = -1; // 线程次数
	private List<Handler> handlers = new ArrayList<Handler>();
	private MyHandler handler;
	private Runnable mBackgroundRunnable;
	private Handler sendScoreHandler;
	// 非负浮点数 ,且范围在0~100
	private String eL = "^\\d{1,2}(\\.\\d{1})?$";
	// 三位整数
	private String eL2 = "^\\d{3}?$";
	private Pattern pattern = Pattern.compile(eL);
	private Pattern pattern2 = Pattern.compile(eL2);
	private Matcher matcher;
	private Matcher matcher2;
	private String groupNum = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 无标题模式
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_score);
		Log.i(TAG, "启动");
		units_content = (TextView) this.findViewById(R.id.units_content);
		category_content = (TextView) this.findViewById(R.id.category_content);
		// 身份信息
		role_content = (TextView) this.findViewById(R.id.role_content);
		// 当前状态信息
		state_content = (TextView) this.findViewById(R.id.state_content);
		score = (EditText) this.findViewById(R.id.editText);
		send = (Button) this.findViewById(R.id.button);
		path = this.getFilesDir();
		role = LoginActivity.role;
		roleName = LoginActivity.roleName;
		// 设置身份
		role_content.setText(roleName);
		// 初始化状态
		state_content.setText("未打分");

		// 接收服务发过来的参赛队伍和比赛项目信息
		if (receiver == null) {
			receiver = new MyReceiver();
			filter = new IntentFilter();
			filter.addAction("android.intent.action.MY_RECEIVER");
			// 动态注册广播信息
			this.registerReceiver(receiver, filter);
		}

		send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				scoreValue = score.getText().toString();
				matcher = pattern.matcher(scoreValue);
				matcher2 = pattern2.matcher(scoreValue);
				if (!(matcher.matches() || (matcher2.matches() && Integer
						.valueOf(scoreValue) == 100))) {// 判断打分是够为0~100
					Toast.makeText(ScoreActivity.this,
							"请确保打分范围为0~100,且只包含一位小数！", Toast.LENGTH_SHORT)
							.show(); // 提示打分超出范围
					return;
				} else {
					sendScoreHandler = new sendScoreHandler();
					new Thread() {
						public void run() {
							try {
								ipDao = new IPDao(path);
								List<String> list = new ArrayList<String>();
								// 接收者名称(裁判长和记录员)
								String receiver[] = { "Editor", "chiefJudge01",
										"chiefJudge02", "chiefJudge03" };
								list = ipDao.getIP(receiver); // 获取IP
								List<FutureTask<Integer>> tasks = new ArrayList<FutureTask<Integer>>();
								int sum = 1;

								// 遍历获取的IP，并发送
								if (list.size() == 0) {// 未获得ip
									sum = 0;
								} else {
									for (int i = 0; i < list.size(); i++) {
										FutureTask<Integer> task = new FutureTask<Integer>(
												new SendMessage(list.get(i),
														scoreValue));
										tasks.add(task);
										new Thread(task).start();
									}

									for (FutureTask<Integer> task : tasks)
										// 获取线程返回值
										sum *= task.get();
								}

								// 发送消息
								Message message = new Message();
								// 发送消息与处理函数里一致
								message.what = sum;
								// 内部类调用外部类的变量
								sendScoreHandler.sendMessage(message);
							} catch (Exception e) {
								Log.e(TAG, e.getMessage());
							}
						}
					}.start();
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
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

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			String item = bundle.getString("item");
			if (item.equalsIgnoreCase("infor2")) {
				// 接收队伍信息
				units_content.setText(bundle.getString("team_name"));
				category_content.setText(bundle.getString("category_name"));
				// 裁判组数
				groupNum = bundle.getString("groupNum");
				// 本组是否能打分
				boolean isEnable = true;
				if (groupNum.equals("1")) {// 若只有一组裁判
					if (bundle.getString("category_name").indexOf("技巧") == -1
							&& LoginActivity.role.substring(
									LoginActivity.role.length() - 2).equals(
									"10")) {// 若为10号裁判，需根据比赛类型授予裁判打分的权力
						score.setText("");
						send.setEnabled(false);
						score.setEnabled(false);
						state_content.setText("不能打分");
					} else {
						score.setText("");
						send.setEnabled(true);
						score.setEnabled(true);
						state_content.setText("未打分");
					}
				} else {// 两组裁判
					if (role.contains("judge1")
							&& (Integer
									.parseInt(bundle.getString("matchOrder")) % 2) == 0) {// 若为第一组裁判且为偶数次队伍
						isEnable = false;
					} else if (role.contains("judge2")
							&& (Integer
									.parseInt(bundle.getString("matchOrder")) % 2) == 1) {// 若为第二组裁判且为奇数次队伍
						isEnable = false;
					} else {
						isEnable = true;
					}
					if (!isEnable
							|| (bundle.getString("category_name").indexOf("技巧") == -1 && LoginActivity.role
									.substring(LoginActivity.role.length() - 2)
									.equals("10"))) {// 若为10号裁判，需根据比赛类型授予裁判打分的权力
						score.setText("");
						send.setEnabled(false);
						score.setEnabled(false);
						state_content.setText("不能打分");
					} else {
						score.setText("");
						send.setEnabled(true);
						score.setEnabled(true);
						state_content.setText("未打分");
					}
				}
			} else if (item.equalsIgnoreCase("Command")) {
				if (bundle.getString("CommandContent").equals("up")) {
					state_content.setText("需调分");
					ImageView img = new ImageView(ScoreActivity.this);
					img.setImageResource(R.drawable.up);
					new AlertDialog.Builder(ScoreActivity.this).setTitle("提示")
							.setView(img).setPositiveButton("确定", null).show();
				} else if (bundle.getString("CommandContent").equals("down")) {
					state_content.setText("需调分");
					ImageView img = new ImageView(ScoreActivity.this);
					img.setImageResource(R.drawable.down);
					new AlertDialog.Builder(ScoreActivity.this).setTitle("提示")
							.setView(img).setPositiveButton("确定", null).show();
				} else {
					state_content.setText("已打分");
					ImageView img = new ImageView(ScoreActivity.this);
					img.setImageResource(R.drawable.ok);
					new AlertDialog.Builder(ScoreActivity.this).setTitle("提示")
							.setView(img).setPositiveButton("确定", null).show();
					return;
				}
				send.setEnabled(true);
				score.setEnabled(true);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(ScoreActivity.this)
					.setTitle("提示")
					.setMessage("确定退出打分界面?")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									loginOutdialog = getProgressDialog(
											"请稍候...", "正在退出登陆中....");
									loginOutdialog.show();
									i++;
									threadt = new HandlerThread("loginout" + i);
									threadt.start();
									mBackgroundRunnable = new Runnable() {
										@Override
										public void run() {
											int index = i;
											LoginDao dao = new LoginDao(
													ScoreActivity.this
															.getFilesDir());
											if (dao.checkConfig()) {
												HashMap<String, Object> result = dao
														.loginOut(role);
												if (result.get("message")
														.equals("success")) {
													if (result.get("result")
															.equals("true")) {
														handlers.get(index).sendEmptyMessage(1);
													} else {
														handlers.get(index).sendEmptyMessage(2);
													}
												} else {
													error = result.get("message").toString();
													handlers.get(index).sendEmptyMessage(3);
												}
											} else {
												handlers.get(index).sendEmptyMessage(0);
											}
										}
									};
									handler = new MyHandler(threadt.getLooper());
									handler.post(mBackgroundRunnable);
									handlers.add(handler);
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).show();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	/**
	 * 打分之后更新UI
	 */
	@SuppressLint("HandlerLeak") 
	private class sendScoreHandler extends Handler {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				send.setEnabled(true);
				score.setEnabled(true);
				state_content.setText("未打分");
				new AlertDialog.Builder(ScoreActivity.this).setTitle("提示")
						.setMessage("未获得接收地址，请休息片刻！")
						.setPositiveButton("确定", null).show(); // 提示发送失败
				break;
			case 1:
				send.setEnabled(false);
				score.setEnabled(false);
				state_content.setText("已打分");
				Toast.makeText(ScoreActivity.this, "打分成功！", Toast.LENGTH_SHORT)
						.show(); // 提示发送成功
				break;
			case -1:
				// 清空配置文件
				ipDao.clearIP();
				send.setEnabled(true);
				score.setEnabled(true);
				state_content.setText("未打分");
				new AlertDialog.Builder(ScoreActivity.this).setTitle("提示")
						.setMessage("发送失败，请重发！").setPositiveButton("确定", null)
						.show(); // 提示发送失败
				break;
			}
			super.handleMessage(msg);
		}
	};

	private class MyHandler extends Handler {
		private boolean isStop = false;

		public void setStop(boolean isStop) {
			this.isStop = isStop;
		}

		public MyHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			if (isStop)
				return;
			loginOutdialog.dismiss();
			switch (msg.what) {
			case 0: // 数据库配置错误
				Toast.makeText(ScoreActivity.this, "网络不畅通，请检查！",
						Toast.LENGTH_SHORT).show();
				login = true;
				break;
			case 1: // 成功退出
				login = false;
				break;
			case 2: // 服务器端退出异常
				Toast.makeText(ScoreActivity.this, "退出登陆不成功！",
						Toast.LENGTH_SHORT).show();

				login = true;
				break;
			case 3: // 连接服务器端异常
				login = true;
				break;
			}

			if (login) {
				String message = "";
				if (error != null && error.contains("refused")) {
					message = "网络不畅通，请稍候尝试！";
				} else {
					message = "服务器发生异常，无法退出，";
				}
				new AlertDialog.Builder(ScoreActivity.this)
						.setTitle("提示信息")
						.setMessage(message + "仍然要退出程序吗？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if (LoginActivity.BroadcastIPIntent != null) {
											stopService(LoginActivity.BroadcastIPIntent); // 停止组播IP的服务
										}
										if (receiver != null) {
											unregisterReceiver(receiver);
											receiver = null;
										}
										finish();
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								}).show();
			} else {
				if (LoginActivity.BroadcastIPIntent != null) {
					stopService(LoginActivity.BroadcastIPIntent); // 停止组播IP的服务
				}
				if (receiver != null) {
					unregisterReceiver(receiver);
					receiver = null;
				}
				finish();
			}
		}
	}

	public ProgressDialog getProgressDialog(String title, String msg) {
		ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(title);
		progressDialog.setIndeterminate(true);
		progressDialog.setMessage(msg);
		progressDialog.setCancelable(true);
		progressDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				handler.setStop(true);
			}

		});
		return progressDialog;
	}
}
