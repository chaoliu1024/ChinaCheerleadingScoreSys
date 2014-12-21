/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.net;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import org.apache.log4j.Logger;

/**
 * 接收IP的线程
 * 
 * @author Zhengfei Chen
 * @since ccss 1.0
 */
public class ReceIP implements Runnable {
	private MulticastSocket dsock; // 广播套接字
	private String host;
	private Logger logger;
	private String path;
	private static long[] timer;

	public ReceIP(MulticastSocket dsock) {
		logger = Logger.getLogger(ReceIP.class.getName());
		this.dsock = dsock;
		this.host = "239.0.0.1";
		String relativelyPath = System.getProperty("user.dir");// 获取工程目录
		this.path = relativelyPath + "\\Address.txt";
		timer = new long[23];
		for (int i = 0; i < timer.length; i++) {
			timer[i] = 0;
		}
	}

	public void run() {
		try {
			InetAddress ip = InetAddress.getByName(this.host);
			dsock.joinGroup(ip); // 加入到广播组
			while (true) {
				byte[] data = new byte[256];

				DatagramPacket packet = new DatagramPacket(data, data.length);
				dsock.receive(packet);
				String message = new String(packet.getData(), 0,
						packet.getLength());
				// 根据角色更新该角色记录的时间
				String role = message.split("/")[0];
				if (role != null && !role.equals("")) {
					if (role.startsWith("judge1")) {// 接受到的为裁判组一的裁判
						int roleIndex = Integer.valueOf((String) message
								.split("/")[0].subSequence(7, 9));// 裁判序号
						timer[roleIndex - 1] = System.currentTimeMillis();
					} else if (role.startsWith("judge2")) {// 接受到的为裁判组二的裁判
						int roleIndex = Integer.valueOf((String) message
								.split("/")[0].subSequence(7, 9));// 裁判序号
						timer[9 + roleIndex] = System.currentTimeMillis();
					} else if (role.startsWith("chief")) {// 接受到的为裁判长
						int roleIndex = Integer.valueOf((String) message
								.split("/")[0].subSequence(10, 12));// 裁判序号
						timer[19 + roleIndex] = System.currentTimeMillis();
					}

				}
				// 将接受的消息存入Address.txt文件
				try {
					FileWriter fw = new FileWriter(path, true);
					PrintWriter pw = new PrintWriter(fw);
					FileReader fr = new FileReader(path);
					BufferedReader br = new BufferedReader(fr);

					while (true) {
						String messageTemp = br.readLine();
						if (messageTemp == null) {
							pw.println(message);
							break;
						} else if (messageTemp.equalsIgnoreCase(message))
							break;
						else {
							if (messageTemp.split("/")[0]
									.equalsIgnoreCase(message.split("/")[0])
									|| messageTemp.split("/")[1]
											.equalsIgnoreCase(message
													.split("/")[1])) { // 若身份一样ip不一样或ip一样身份不一样则更新
								fw.close();
								pw.close();
								fw = new FileWriter(path);
								fw.write("");
								pw = new PrintWriter(fw);
								break;
								// pw.println(message); // 写入新的IP
							} else
								continue;
						}

					}
					pw.close();
					fw.close();
					br.close();
					fr.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("接受失败!");
		} finally {
			dsock.close();
		}
	}

	public static long[] getTimer() {
		return timer;
	}

	public static void setTimer(long[] timer) {
		ReceIP.timer = timer;
	}
}
