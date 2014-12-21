/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.service;

import java.io.IOException;
import java.net.MulticastSocket;
import java.util.List;

import nuist.qlib.ccss.net.BroadcastIP;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.view.WindowManager;

/**
 * @author Zhengfei Chen
 * @since ccss 1.0
 */
public class BroadcastIPService extends Service {

	private MulticastSocket sendSocket;
	private Handler ulHandler;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		try {
			sendSocket = new MulticastSocket(9998);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		ulHandler = new Handler(Looper.getMainLooper()) {
			public void handleMessage(Message msg) {

				switch (msg.what) {
				case 0:
					AlertDialog.Builder dialog = new AlertDialog.Builder(
							getApplicationContext());
					dialog.setTitle("警告");
					dialog.setMessage("设备未连接网络，请检查网络后再继续使用！");
					dialog.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					AlertDialog mDialog = dialog.create();
					// 设定为系统级警告
					mDialog.getWindow().setType(
							WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
					mDialog.show();
					break;
				case 1:
					AlertDialog.Builder dialog1 = new AlertDialog.Builder(
							getApplicationContext());
					dialog1.setTitle("提示");
					dialog1.setMessage("设备已连接网络，请继续使用！");
					dialog1.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					AlertDialog mDialog1 = dialog1.create();
					// 设定为系统级警告
					mDialog1.getWindow().setType(
							WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
					mDialog1.show();
				}
			}

		};
		// 开启广播IP的线程
		new Thread(new BroadcastIP(sendSocket, ulHandler)).start();
		BroadcastIP.Flag = true;
		return START_NOT_STICKY;
	}

	public boolean isServiceRunning(Context mContext, String className) {

		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(30);

		if (!(serviceList.size() > 0)) {
			return false;
		}

		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

	@Override
	public void onDestroy() {
		// 关闭线程
		BroadcastIP.Flag = false;
		super.onDestroy();
	}
}
