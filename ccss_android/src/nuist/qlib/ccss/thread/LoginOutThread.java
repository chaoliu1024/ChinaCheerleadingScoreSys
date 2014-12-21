/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nuist.qlib.ccss.dao.LoginDao;
import nuist.qlib.ccss.ui.ProgressBackDialog;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

/**
 * 退出线程
 * 
 * @author Fang Wang
 * @since ccss 1.0
 */
public class LoginOutThread {
	private Context context;
	private ProgressDialog loginOutdialog;
	private HandlerThread threadt;
	private int i = -1;
	private Runnable mBackgroundRunnable;
	private String role;
	private Handler handler;
	private boolean login;
	private String error;
	private Handler outHandler;
	private List<Handler> handlers = new ArrayList<Handler>();

	public LoginOutThread(Context context, String role, boolean login,
			Handler outHandler) {
		this.context = context;
		this.role = role;
		this.login = login;
		this.outHandler = outHandler;
	}

	@SuppressLint("HandlerLeak")
	public void loginOut() {

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
			}
		};
		new AlertDialog.Builder(context)
				.setTitle("提示")
				.setMessage("确定退出打分界面?")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						loginOutdialog = new ProgressBackDialog(context)
								.getProgressDialog("请稍候...", "正在退出登陆中....");
						loginOutdialog.show();

						threadt = new HandlerThread("loginout" + i);
						threadt.start();
						i++;
						mBackgroundRunnable = new Runnable() {
							@Override
							public void run() {
								LoginDao dao = new LoginDao(context
										.getFilesDir());
								if (dao.checkConfig()) {
									HashMap<String, Object> result = dao
											.loginOut(role);
									if (result.get("message").equals("success")) {
										if (result.get("result").equals("true")) {
											handlers.get(i).sendEmptyMessage(1);
										} else {
											handlers.get(i).sendEmptyMessage(2);
										}
									} else {
										error = result.get("message")
												.toString();
										handlers.get(i).sendEmptyMessage(3);
									}

								} else {
									handlers.get(i).sendEmptyMessage(0);
								}
							}
						};
						handler = new MyHandler(threadt.getLooper());
						handlers.add(handler);
						handler.post(mBackgroundRunnable);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}

	private class MyHandler extends Handler {
		private boolean isStop = false;

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
				Toast.makeText(context, "网络不畅通，请检查！！", Toast.LENGTH_SHORT)
						.show();
				login = true;
				break;
			case 1: // 成功退出
				login = false;
				break;
			case 2: // 服务器端退出异常
				Toast.makeText(context, "退出登陆不成功!!", Toast.LENGTH_SHORT).show();
				login = true;
				break;
			case 3: // 连接服务器端异常
				login = true;
				break;
			}
			if (login) {
				String message = "";
				if (error.contains("refused")) {
					message = "网络异常，请稍候尝试！！或者";
				} else {
					message = "服务器发生异常，无法退出，";
				}
				new AlertDialog.Builder(context)
						.setTitle("提示信息")
						.setMessage(message + "仍然要退出程序吗？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										outHandler.sendEmptyMessage(0);
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
				outHandler.sendEmptyMessage(0);
			}
		}
	}
}
