/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.net;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import nuist.qlib.ccss.activity.LoginActivity;
import android.os.Handler;
import android.util.Log;

/**
 * 广播本机的IP地址
 * 
 * @author Zhengfei Chen
 * @since ccss 1.0
 */
public class BroadcastIP implements Runnable {
	private static final String TAG = "BroadcastIPService";
	private MulticastSocket dsock;
	private Handler ulHandler;
	private static int i = 0;
	private int port;
	private String host;
	private String data;
	public static Boolean Flag = true;

	public BroadcastIP(MulticastSocket dsock, Handler ulHandler) {
		this.dsock = dsock;
		this.ulHandler = ulHandler;
		this.port = 9999; // 广播时局域网中成员接收的端口号
		this.host = "239.0.0.1"; // 局域网广播地址
	}

	public void run() {

		try {
			while (Flag) {
				String ip = getLocalIpAddress(); // 获取当前wifi的I平地址
				data = LoginActivity.role + "/" + ip;
				if (ip != null && !ip.equals("null")) {
					DatagramPacket dataPack = // 数据打包
					new DatagramPacket(data.getBytes(), data.length(),
							InetAddress.getByName(host), // 广播
							port // 目标端口
					);
					dsock.send(dataPack);
					Log.i(TAG, data);
					if (i != 0) {
						ulHandler.sendEmptyMessage(1);
					}
					i = 0;
				} else {
					Log.i(TAG, "网络未连接");
					if (i == 0) {
						ulHandler.sendEmptyMessage(0);
					}
					i++;
				}
				Thread.sleep(10 * 1000);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (Exception e) {
			throw new RuntimeException("发送失败!");
		} finally {
			dsock.close();
		}
	}

	public static int getI() {
		return i;
	}

	public static void setI(int i) {
		BroadcastIP.i = i;
	}

	public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& !inetAddress.isLinkLocalAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}
		return null;
	}
}