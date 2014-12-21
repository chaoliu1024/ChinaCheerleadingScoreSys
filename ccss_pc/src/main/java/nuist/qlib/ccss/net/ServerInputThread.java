/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import nuist.qlib.ccss.ui.MatchPanel;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;

/**
 * 建立主服务器的输入线程类，接收客户端的信息
 * 
 * @author Zhengfei Chen
 * @since ccss 1.0
 */
public class ServerInputThread extends Thread {
	// tcp/ip协议
	private Socket socket;
	private Logger logger;

	public ServerInputThread(Socket socket) {
		logger = Logger.getLogger(ServerInputThread.class.getName());
		this.socket = socket;
	}

	@Override
	public void run() {

		try {
			// 建立输入流
			InputStream is = socket.getInputStream();
			byte[] by = new byte[1024];
			// 将输入流里的字节读到字节数组里，并返回读的字节数
			int length = is.read(by);
			// 将字节数组里的length个字节转换为字符串
			if (length == -1) {
				return;
			}
			String str = new String(by, 0, length, "utf-8");
			System.out.println("str:" + str);
			// 打印出字符串
			final String message[] = str.split("/");
			// 接收得分
			if (message[0].contains("judge")) {
				switch (Integer.valueOf(message[1])) {
				case 1:
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							MatchPanel.getScore1().setText(message[2]);
						}
					});
					break;
				case 2:
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							MatchPanel.getScore2().setText(message[2]);
						}
					});
					break;
				case 3:
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							MatchPanel.getScore3().setText(message[2]);
						}
					});
					break;
				case 4:
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							MatchPanel.getScore4().setText(message[2]);
						}
					});
					break;
				case 5:
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							MatchPanel.getScore5().setText(message[2]);
						}
					});
					break;
				case 6:
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							MatchPanel.getScore6().setText(message[2]);
						}
					});
					break;
				case 7:
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							MatchPanel.getScore7().setText(message[2]);
						}
					});
					break;
				case 8:
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							MatchPanel.getScore8().setText(message[2]);
						}
					});
					break;
				case 9:
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							MatchPanel.getScore9().setText(message[2]);
						}
					});
					break;
				case 10:
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							MatchPanel.getScore10().setText(message[2]);
						}
					});
					break;
				}
			} else if (message[0].contains("chief")) { // 接收裁判长打分
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						MatchPanel.getDeduction_score().setText(message[2]);
					}
				});
			}

			is.close();
			socket.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
}
