/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.net;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

import nuist.qlib.ccss.activity.LoginActivity;
import android.util.Log;

/**
 * 建立主客户端的输出线程类， 向服务器端传送信息
 * 
 * @author Zhengfei Chen
 * @since ccss 1.0
 */
public class SendMessage implements Callable<Integer> {

	// tcp/ip协议
	private static final String TAG = "SendMessage";
	private Socket socket;
	private String ip;
	private String scoreValue;

	public SendMessage(String ip, String scoreValue) {
		this.ip = ip;
		this.scoreValue = scoreValue;
	}

	@Override
	public Integer call() throws Exception {
		try {
			socket = new Socket(ip, 6666);
			OutputStream os = socket.getOutputStream();
			String s = null;

			if (scoreValue.equalsIgnoreCase(""))
				s = LoginActivity.role.substring(0,
						LoginActivity.role.length() - 2)
						+ "/"
						+ LoginActivity.role.substring(LoginActivity.role
								.length() - 2) + "/" + " ";
			else
				s = LoginActivity.role.substring(0,
						LoginActivity.role.length() - 2)
						+ "/"
						+ LoginActivity.role.substring(LoginActivity.role
								.length() - 2) + "/" + scoreValue;
			Log.i(TAG, s + ":");
			os.write(s.getBytes("utf-8"));
			os.flush();
			os.close();
			socket.close();

		} catch (UnknownHostException e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
			return -1;
		} catch (ConnectException e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
			return -1;
		}
		return 1;
	}
}
