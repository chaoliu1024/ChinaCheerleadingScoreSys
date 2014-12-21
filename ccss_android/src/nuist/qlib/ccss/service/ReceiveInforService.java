/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * @author Zhengfei Chen
 * @since ccss 1.0
 */
public class ReceiveInforService extends Service {

	public static String units_name;
	public static String category_name;
	private ServerSocket serverSocket;
	public static Boolean Flag = true;

	@Override
	public void onCreate() {
		super.onCreate();
		try {
			this.serverSocket = new ServerSocket(6666);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread() {
			public void run() {
				receiveInfor();
			};
		}.start();
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		Flag = false;
		super.onDestroy();
	}

	private void receiveInfor() {
		try {
			while (Flag) {
				Socket socket = serverSocket.accept(); // 监听
				// 建立输入流
				InputStream is = socket.getInputStream();
				byte[] by = new byte[1024];
				// 将输入流里的字节读到字节数组里，并返回读的字节数
				int length = is.read(by);
				// 将字节数组里的length个字节转换为字符串
				String str = new String(by, 0, length, "utf-8");
				final String message[] = str.split("/");
				Intent intent = new Intent();
				intent.setAction("android.intent.action.MY_RECEIVER");
				if (message[0].contains("judge")) { // 接收裁判打分
					intent.putExtra("item", "judge");
					intent.putExtra("num", message[1]);
					if (message[1].equalsIgnoreCase("01")) {
						intent.putExtra("score1", message[2]);
					} else if (message[1].equalsIgnoreCase("02")) {
						intent.putExtra("score2", message[2]);
					} else if (message[1].equalsIgnoreCase("03")) {
						intent.putExtra("score3", message[2]);
					} else if (message[1].equalsIgnoreCase("04")) {
						intent.putExtra("score4", message[2]);
					} else if (message[1].equalsIgnoreCase("05")) {
						intent.putExtra("score5", message[2]);
					} else if (message[1].equalsIgnoreCase("06")) {
						intent.putExtra("score6", message[2]);
					} else if (message[1].equalsIgnoreCase("07")) {
						intent.putExtra("score7", message[2]);
					} else if (message[1].equalsIgnoreCase("08")) {
						intent.putExtra("score8", message[2]);
					} else if (message[1].equalsIgnoreCase("09")) {
						intent.putExtra("score9", message[2]);
					} else if (message[1].equalsIgnoreCase("10")) {
						intent.putExtra("score10", message[2]);
					}
				} else if (message[0].equalsIgnoreCase("infor1")) { // 接收编辑员发过来的消息
					intent.putExtra("item", "infor1");
					intent.putExtra("team_name", message[1]);
					intent.putExtra("category_name", message[2]);
					intent.putExtra("matchOrder", message[3]);
				} else if (message[0].equalsIgnoreCase("infor2")) { // 接收编辑员发过来的消息
					intent.putExtra("item", "infor2");
					intent.putExtra("team_name", message[1]);
					intent.putExtra("category_name", message[2]);
					intent.putExtra("match_name", message[3]);
					intent.putExtra("matchOrder", message[4]);
					intent.putExtra("groupNum", message[5]);
				} else if (message[0].equalsIgnoreCase("infor3")) { // 比赛结束
					intent.putExtra("item", "infor3");
				} else if (message[0].equalsIgnoreCase("all")) {// 接收编辑员发过来的成绩
					intent.putExtra("item", "all");
					intent.putExtra("score1", message[1]);
					intent.putExtra("score2", message[2]);
					intent.putExtra("score3", message[3]);
					intent.putExtra("score4", message[4]);
					intent.putExtra("score5", message[5]);
					intent.putExtra("score6", message[6]);
					intent.putExtra("score7", message[7]);
					intent.putExtra("score8", message[8]);
					intent.putExtra("score9", message[9]);
					intent.putExtra("score10", message[10]);
					intent.putExtra("totalscore", message[11]);
				} else if (message[0].startsWith("Command")) { // 接收指令
					intent.putExtra("item", "Command");
					intent.putExtra("CommandContent", message[1]);
				}
				sendBroadcast(intent);
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
