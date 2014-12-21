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

import org.apache.log4j.Logger;

/**
 * 建立主客户端的输出线程类， 向服务器端传送信息
 * 
 * @author Zhengfei Chen
 * @since ccss 1.0
 */
public class ClientOutputThread implements Callable<Integer> {
	private Socket socket;
	private String ip;
	private String s;
	private Logger logger;

	/* 给裁判长发送所有打分信息 */
	public ClientOutputThread(String ip, String judge01, String judge02,
			String judge03, String judge04, String judge05, String judge06,
			String judge07, String judge08, String judge09, String judge10,
			String total) { // 发送打分信息

		logger = Logger.getLogger(ClientOutputThread.class.getName());

		this.ip = ip;
		this.s = "all" + "/" + judge01 + "/" + judge02 + "/" + judge03 + "/"
				+ judge04 + "/" + judge05 + "/" + judge06 + "/" + judge07 + "/"
				+ judge08 + "/" + judge09 + "/" + judge10 + "/" + total;
	}

	/* 给裁判长和裁判发送队伍信息 */
	public ClientOutputThread(String ip, String matchUnit,
			String matchCategory, int matchOrder) {
		this.ip = ip;
		this.s = "infor1" + "/" + matchUnit + "/" + matchCategory + "/"
				+ matchOrder + "/";
		logger = Logger.getLogger(ClientOutputThread.class.getName());
	}

	/* 给裁判长和裁判发送比赛信息 */
	public ClientOutputThread(String ip, String matchUnit,
			String matchCategory, String matchName, int matchOrder,
			String group_num) {
		logger = Logger.getLogger(ClientOutputThread.class.getName());
		this.ip = ip;
		this.s = "infor2" + "/" + matchUnit + "/" + matchCategory + "/"
				+ matchName + "/" + matchOrder + "/" + group_num + "/";
	}

	public ClientOutputThread(String ip, String command) { // 发送调分指令

		this.ip = ip;
		this.s = "Command" + "/" + command;
	}

	public ClientOutputThread(String ip) { // 发送队伍信息
		logger = Logger.getLogger(ClientOutputThread.class.getName());
		this.ip = ip;
		this.s = "infor3" + "/";
	}

	public Integer call() {

		try {
			socket = new Socket(ip, 6666);
			OutputStream os = socket.getOutputStream();
			os.write(s.getBytes("utf-8"));
			os.flush();
			os.close();
			socket.close();
		} catch (UnknownHostException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return -1;
		} catch (ConnectException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return -1;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return -1;
		}
		return 1;
	}
}
