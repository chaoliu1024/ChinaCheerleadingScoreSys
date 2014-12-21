/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.service;

import java.io.File;
import java.io.IOException;
import java.net.MulticastSocket;

import nuist.qlib.ccss.net.ReceIP;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.IBinder;

/**
 * @author Zhengfei Chen
 * @since ccss 1.0
 */
public class ReceiveIPService extends Service {

	private MulticastSocket receSocket;
	MulticastLock multicastLock;
	private File path;

	@Override
	public void onCreate() {
		super.onCreate();
		try {
			this.receSocket = new MulticastSocket(9999);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		path = this.getFilesDir();
		multicastLock = allowMulticast();
		new Thread(new ReceIP(receSocket, path)).start();
		ReceIP.Flag = true;
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		// 关闭线程
		ReceIP.Flag = false;
		multicastLock.release();
		super.onDestroy();
	}

	// 获取wifi锁，以便可以接受组播信息
	public MulticastLock allowMulticast() {
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		MulticastLock multicastLock = wifiManager
				.createMulticastLock("multicast.test");
		multicastLock.acquire();
		return multicastLock;
	}
}
