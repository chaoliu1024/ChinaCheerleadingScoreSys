/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import nuist.qlib.ccss.util.ToolUtil;
import android.util.Log;

/**
 * 接收IP的线程
 * 
 * @author Zhengfei Chen
 * @since ccss 1.0
 */
public class ReceIP implements Runnable {
	private static final String TAG = "ReceIP";
	private MulticastSocket dsock; // 广播套接字、
	private String host;
	private File path;
	public static Boolean Flag = true;

	public ReceIP(MulticastSocket dsock, File path) {
		this.dsock = dsock;
		this.host = "239.0.0.1";
		this.path = path;
	}

	public void run() {
		try {
			InetAddress ip = InetAddress.getByName(this.host);
			dsock.joinGroup(ip); // 加入到广播组
			while (Flag) {
				byte[] data = new byte[256];

				DatagramPacket packet = new DatagramPacket(data, data.length);
				dsock.receive(packet);

				String message = new String(packet.getData(), 0,
						packet.getLength());
				if (message.split("/")[0].contains("Editor")) {// 接收记录员和裁判长的IP
					ToolUtil.createDBConfig(path, message.split("/")[1]);
				}
				Log.i(TAG, message);

				try {
					File file = new File(path, "Adress.txt");
					FileWriter fw = new FileWriter(file, true);
					PrintWriter pw = new PrintWriter(fw);
					FileReader fr = new FileReader(file);
					BufferedReader br = new BufferedReader(fr);
					while (true) {
						String messageTemp = br.readLine();
						if (messageTemp == null) {
							pw.println(message);
							break;
						} else if (messageTemp.equalsIgnoreCase(message)) {
							break;
						} else {
							if (messageTemp.split("/")[0]
									.equalsIgnoreCase(message.split("/")[0])
									|| messageTemp.split("/")[1]
											.equalsIgnoreCase(message
													.split("/")[1])) { // 更新更改的IP
								fw.close();
								pw.close();
								fw = new FileWriter(file);
								fw.write("");
								pw = new PrintWriter(fw);
								break;
							} else {
								continue;
							}
						}
					}
					pw.close();
					fw.close();
					br.close();
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("接受失败!");
		} finally {
			dsock.close();
		}
	}
}