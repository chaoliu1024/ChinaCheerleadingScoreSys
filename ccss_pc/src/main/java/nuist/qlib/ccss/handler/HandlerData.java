/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import nuist.qlib.ccss.dao.HistoryDataOperDao;
import nuist.qlib.ccss.util.file.JSONManager;
import nuist.qlib.ccss.util.file.PropertiesManager;

/**
 * 下载,历史数据的处理
 * 
 * @author Fang Wang
 * @since ccss 1.0
 */
public class HandlerData {
	private String dir;
	private JSONManager manager;
	private PropertiesManager proManager;
	private HistoryDataOperDao dao;
	private File downLoadFile;
	private String urlString;

	public HandlerData() {
		manager = new JSONManager();
		proManager = new PropertiesManager();
		dao = new HistoryDataOperDao();
	}

	public boolean isCollected() {
		if (manager.isCollected() && dao.isCollected()) {
			return true;
		} else
			return false;
	}

	public void close() {
		if (manager.isCollected()) {
			manager.close();
		}
		if (dao.isCollected()) {
			dao.close();
		}
	}

	/***
	 * 在网络上下载数据
	 * 
	 * @param url
	 * @return "ok"表示成功下载
	 */
	public String downloadNet() {
		// 下载网络文件
		int byteread = 0;
		String[] temp = proManager.readProperties();
		dir = temp[1];
		urlString = temp[0];
		if (urlString.trim().length() == 0) {
			return "请配置下载路径!!";
		}
		if (dir.trim().length() == 0) {
			return "请配置临时目录路径!!";
		}
		try {
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();
			InputStream inStream = conn.getInputStream();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
			downLoadFile = new File(dir, format.format(new Date()) + ".json");
			if (!downLoadFile.exists()) {
				downLoadFile.createNewFile();
			}
			FileOutputStream fs = new FileOutputStream(downLoadFile);
			byte[] buffer = new byte[1204];
			while ((byteread = inStream.read(buffer)) != -1) {
				fs.write(buffer, 0, byteread);
			}
			manager.setFile(downLoadFile);
			fs.close();
			return "ok";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "无法连接网络!";
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	/**
	 * 读JSON文件并且入库
	 * 
	 * @return
	 */
	public String operaJSONFile() {
		JSONArray array = manager.readJSONFile();
		if (array.size() != 0) {
			String result = manager.JSONInput(array);
			return result;
		} else {
			return "无数据!";
		}
	}

	/***
	 * 按照赛事名称 将web_json和match_order中的数据删除
	 * 
	 * @return
	 */
	public boolean deleteOrder_Web() {
		return manager.deleteOrder_Web();
	}

	/***
	 * 按照赛事名称将web_json中的数据删除
	 * 
	 * @return
	 */
	public boolean deleteWebJSON() {
		return manager.deleteWebJSON();
	}

	/***
	 * 删除临时下载的文件
	 */
	public void deleteFile() {
		if (downLoadFile.exists()) {
			downLoadFile.delete();
		}
	}

	/***
	 * 返回参数
	 * 
	 * @return 第一个字符串为下载路径，第二个临时目录路径
	 */
	public String[] getParams() {
		return proManager.readProperties();
	}

	/***
	 * 返回赛事名称
	 * 
	 * @return result[0]字符串数组存放的是已经有成绩的赛事名称；result[1]字符串数组存放的是没有成绩的赛事名称
	 */
	public String[][] getMatchNames() {
		List<HashMap<String, Object>> hasScoreMatches = dao.getAllMatchName();
		List<HashMap<String, Object>> matches = dao.getMatchNameWebJson();
		String[][] results = new String[2][matches.size()];
		for (int i = 0; i < hasScoreMatches.size(); i++) {
			results[0][i] = hasScoreMatches.get(i).get("matchName").toString();
		}
		String matchName;
		int count = 0;
		for (int j = 0; j < matches.size(); j++) {
			matchName = matches.get(j).get("matchName").toString();
			int i = 0;
			for (; i < hasScoreMatches.size(); i++) {
				if (matchName.equals(hasScoreMatches.get(i).get("matchName")
						.toString())) {
					break;
				}
			}
			if (i == hasScoreMatches.size()) {
				results[1][count] = matchName;
				count++;
			}
		}
		return results;
	}

	/***
	 * 删除赛事
	 * 
	 * @param hasScoreMatches
	 *            要删除的有成绩的赛事
	 * @param hasNoScoreMatches
	 *            要删除的没有成绩的赛事
	 * @return 0表示删除成功，1表示删除有成绩的赛事失败；2表示删除没有成绩的赛事成功
	 */
	public int deleteMatches(List<String> hasScoreMatches,
			List<String> hasNoScoreMatches) {
		String hasScoreMatchesS = "";
		String hasNoScoreMatchesS = "";
		if (hasScoreMatches.size() != 0) {
			for (String temp : hasScoreMatches) {
				hasScoreMatchesS += "'" + temp + "',";
			}
			hasScoreMatchesS = hasScoreMatchesS.substring(0,
					hasScoreMatchesS.length() - 1);
		}
		if (hasNoScoreMatches.size() != 0) {
			for (String temp : hasNoScoreMatches) {
				hasNoScoreMatchesS += "'" + temp + "',";
			}
			hasNoScoreMatchesS = hasNoScoreMatchesS.substring(0,
					hasNoScoreMatchesS.length() - 1);
		}
		if (hasScoreMatchesS.trim().length() != 0) { // 删除有成绩的赛事
			if (!dao.deleteMatches(hasScoreMatchesS)) {
				return 1;
			}
		}
		if (hasNoScoreMatchesS.trim().length() != 0) { // 删除没有成绩的赛事
			if (!dao.deleteMatches(hasNoScoreMatchesS)) {
				return 2;
			}
		}
		return 0;
	}
}
