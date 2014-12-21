/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.net;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;

import nuist.qlib.ccss.util.file.AddressManager;

/**
 * 管理客户端信息发送
 * 
 * @author Zhengfei Chen
 * @since ccss 1.0
 */
public class MainClientOutputThread {

	private AddressManager addressManager;

	/**
	 * 初始化ip地址接口
	 */
	public MainClientOutputThread() {
		this.addressManager = new AddressManager();
	}

	/**
	 * 发送队伍信息
	 * 
	 * @param teamReceiver
	 * @param team
	 * @param category
	 * @param matchName
	 * @return
	 */
	public int sendTeam(String teamReceiver[], String team, String category,
			String matchName, int matchOrder, String group_num) {
		try {
			List<String> list = new ArrayList<String>();
			list = addressManager.getIP(teamReceiver); // 获取IP
			List<FutureTask<Integer>> tasks = new ArrayList<FutureTask<Integer>>();
			int sum = 1;

			// 遍历获取的IP，并发送
			if (list.size() == 0) {// 未收到ip
				sum = 0;
			} else {
				for (int i = 0; i < list.size(); i++) {
					FutureTask<Integer> task = new FutureTask<Integer>(
							new ClientOutputThread(list.get(i), team, category,
									matchName, matchOrder, group_num));
					tasks.add(task);
					new Thread(task).start();
				}

				for (FutureTask<Integer> task : tasks)
					// 获取线程返回值
					sum *= task.get();
			}
			return sum;
		} catch (Exception e1) {
			return -1;// 发送失败
		}
	}

	/**
	 * 发送成绩
	 * 
	 * @param scoreReceiver
	 * @param judge01
	 * @param judge02
	 * @param judge03
	 * @param judge04
	 * @param judge05
	 * @param judge06
	 * @param judge07
	 * @param judge08
	 * @param judge09
	 * @param judge10
	 * @param total
	 * @return
	 */
	public int sendScore(String scoreReceiver[], String judge01,
			String judge02, String judge03, String judge04, String judge05,
			String judge06, String judge07, String judge08, String judge09,
			String judge10, String total) {
		try {
			List<String> list = new ArrayList<String>();
			list = addressManager.getIP(scoreReceiver); // 获取IP
			List<FutureTask<Integer>> tasks = new ArrayList<FutureTask<Integer>>();
			int sum = 1;

			// 遍历获取的IP，并发送
			if (list.size() == 0) {// 未收到ip
				sum = 0;
			} else {
				for (int i = 0; i < list.size(); i++) {
					FutureTask<Integer> task = new FutureTask<Integer>(
							new ClientOutputThread(list.get(i), judge01,
									judge02, judge03, judge04, judge05,
									judge06, judge07, judge08, judge09,
									judge10, total));
					tasks.add(task);
					new Thread(task).start();
				}

				for (FutureTask<Integer> task : tasks)
					// 获取线程返回值
					sum *= task.get();
			}
			return sum;
		} catch (Exception e1) {
			return -1;// 发送失败
		}
	}

	/**
	 * @Title: sendCommand
	 * @Description: TODO(给打分裁判发送指令)
	 * @param commmandReceiver
	 * @param command
	 * @return
	 */
	public int sendCommand(String commmandReceiver[], String command) {
		try {
			List<String> list = new ArrayList<String>();
			list = addressManager.getIP(commmandReceiver); // 获取IP
			List<FutureTask<Integer>> tasks = new ArrayList<FutureTask<Integer>>();
			int sum = 1;

			// 遍历获取的IP，并发送
			if (list.size() == 0) {// 未收到ip
				sum = 0;
			} else {
				for (int i = 0; i < list.size(); i++) {
					FutureTask<Integer> task = new FutureTask<Integer>(
							new ClientOutputThread(list.get(i), command));
					tasks.add(task);
					new Thread(task).start();
				}

				for (FutureTask<Integer> task : tasks)
					// 获取线程返回值
					sum *= task.get();
			}
			return sum;
		} catch (Exception e1) {
			return -1;// 发送失败
		}
	}
}
