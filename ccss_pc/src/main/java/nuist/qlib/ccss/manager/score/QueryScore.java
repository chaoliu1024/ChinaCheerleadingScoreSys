/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.manager.score;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nuist.qlib.ccss.dao.PublicDao;
import nuist.qlib.ccss.dao.QueryScoreDao;
import nuist.qlib.ccss.util.file.ExcelManager;
import nuist.qlib.ccss.util.file.JSONManager;
import nuist.qlib.ccss.util.file.PropertiesManager;

/**
 * 成绩查询,包括查询赛事
 * 
 * @author Fang Wang
 * @author Chao Liu
 * @since ccss 1.0
 */
public class QueryScore {
	private QueryScoreDao dao;
	private PublicDao publicDao;
	private ExcelManager excelManager;
	private JSONManager jsonManager;
	private PropertiesManager proManager;

	public QueryScore() {
		dao = new QueryScoreDao();
		publicDao = new PublicDao();
		excelManager = new ExcelManager();
		jsonManager = new JSONManager();
		proManager = new PropertiesManager();
	}

	/***
	 * 获取所有已经有成绩的赛事名称
	 * 
	 * @return
	 */
	public String[] getAllMatchNames() {
		List<HashMap<String, Object>> data = dao.getAllMatchNames();
		String[] results = new String[data.size()];
		for (int i = 0; i < data.size(); i++) {
			results[i] = data.get(i).get("matchName").toString();
		}
		return results;
	}

	/**
	 * 在首界面点击排名按钮，若此时界面中没有任何参赛队伍的id，那么根据赛事名称以及赛事类型选择id号最小的队伍
	 * 
	 * @param matchType
	 *            赛事类型
	 * @param matchName
	 *            赛事名称 * @param matchNum 场次
	 * @return -1 表示赛事该类型没有分数
	 * @return
	 */
	public int getTeamMinId(int matchType, String matchName) {
		List<HashMap<String, Object>> data = dao.getTeamMinId(matchType,
				matchName);
		if (data.size() == 0) {
			return -1;
		} else {
			return Integer.valueOf(data.get(0).get("id").toString());
		}
	}

	/**
	 * 在首界面点击排名按钮，若此时界面中没有任何参赛队伍的id，那么根据赛事名称以及赛事类型选择id号最小的队伍
	 * 
	 * @param matchType
	 *            赛事类型
	 * @param matchName
	 *            赛事名称
	 * @param matchNum
	 *            场次
	 * @param matchOrder
	 *            队伍在场次中的序号
	 * @return
	 */
	public int getTeamMinId(int matchType, String matchName, int matchNum,
			int matchOrder) {
		List<HashMap<String, Object>> data = dao.getTeamMinId(matchType,
				matchName, matchNum, matchOrder);
		if (data.size() == 0) {
			return -1;
		} else {
			return Integer.valueOf(data.get(0).get("id").toString());
		}
	}

	/** 参数为参赛者的id号,该函数根据参赛者的id号、比赛类型(如预赛或决赛)显示参赛者所属项目的排名情况 ,按照总分排名 */
	public List<HashMap<String, Object>> getRank(int id, int matchType) {
		List<HashMap<String, Object>> data = dao.getRank(id, matchType);
		int i = 1;
		HashMap<String, Object> one = null;
		String score = "";
		if (data.size() != 0) {
			score = data.get(0).get("total").toString();
			one = data.get(0);
			one.put("rank", i);
		}
		for (int j = 1; j < data.size(); j++) {
			one = data.get(j);
			if (one.get("total").toString().equals(score)) {
				one.put("rank", i);
			} else {
				i++;
				score = one.get("total").toString();
				one.put("rank", i);
			}
		}
		return data;
	}

	/** 根据站点、比赛项目、比赛类型获取该项目的比赛排名成绩 */
	public List<HashMap<String, Object>> getRank(String matchName,
			String category, int matchType) {
		List<HashMap<String, Object>> data = publicDao.getRank(matchName,
				category, matchType);
		int i = 1;
		HashMap<String, Object> one = null;
		String score = "";
		if (data.size() != 0) {
			score = data.get(0).get("total").toString();
			one = data.get(0);
			one.put("rank", i);
		}
		for (int j = 1; j < data.size(); j++) {
			one = data.get(j);
			if (one.get("total").toString().equals(score)) {
				one.put("rank", i);
			} else {
				i++;
				score = one.get("total").toString();
				one.put("rank", i);
			}
		}
		return data;
	}

	/***
	 * 获取没有web_id的队伍
	 * 
	 * @param matchName
	 * @param category
	 * @param matchType
	 * @return
	 */
	public List<HashMap<String, Object>> getNoWebData(String matchName,
			String category, int matchType) {
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> data = dao.getRankForNoWeb(matchName,
				category, matchType);
		int i = 1;
		HashMap<String, Object> one = null;
		String score = "";
		if (data.size() != 0) {
			score = data.get(0).get("total").toString();
			one = data.get(0);
			one.put("rank", i);
		}
		for (int j = 1; j < data.size(); j++) {
			one = data.get(j);
			if (one.get("total").toString().equals(score)) {
				one.put("rank", i);
			} else {
				i++;
				score = one.get("total").toString();
				one.put("rank", i);
			}
		}
		for (HashMap<String, Object> temp : data) {
			if (temp.get("webId") == null
					|| temp.get("webId").toString().equals("null")
					|| temp.get("webId").toString().equals("")) {
				result.add(temp);
			}
		}
		return result;
	}

	/**
	 * 得到生成PDF证书的相关信息
	 * 
	 * @param matchName
	 * @param category
	 * @param matchType
	 * @return
	 * @throws
	 */
	public List<HashMap<String, Object>> getCertificateInfor(String matchName,
			String category, int matchType) {
		List<HashMap<String, Object>> data = publicDao.getCertificateInfor(
				matchName, category, matchType);
		int i = 1;
		HashMap<String, Object> one = null;
		String score = "";
		if (data.size() != 0) {
			score = data.get(0).get("total").toString();
			one = data.get(0);
			one.put("rank", i);
		}
		for (int j = 1; j < data.size(); j++) {
			one = data.get(j);
			if (one.get("total").toString().equals(score)) {
				one.put("rank", i);
			} else {
				i++;
				score = one.get("total").toString();
				one.put("rank", i);
			}
		}
		return data;
	}

	/**
	 * 根据id号返回它所属的项目
	 * 
	 * @param id
	 * @return
	 */
	public String getCategory(int id) {
		return dao.getCategory(id);
	}

	/**
	 * 获得比赛的项目类型
	 * 
	 * @param matchType
	 *            预赛或决赛
	 */
	public String[] getCategories(int matchType, String matchName) {
		List<HashMap<String, Object>> data = dao.getCategories(matchType,
				matchName);
		String[] result = new String[data.size()];
		for (int i = 0; i < data.size(); i++) {
			result[i] = data.get(i).get("category").toString();
		}
		return result;
	}

	/***
	 * 导出项目成绩
	 * 
	 * @param data
	 * @param fileName
	 * @param matchName
	 * @param category
	 * @param matchType
	 * @param matchKind
	 * @return
	 */
	public boolean exportScoreExcel(String fileName, String matchName,
			String category, int matchType, int matchKind) {
		List<HashMap<String, Object>> data = dao.getRank(matchName, category,
				matchType);
		Object players = null;
		String player = "";
		int i = 1;
		HashMap<String, Object> one = null;
		String score = "";
		if (data.size() != 0) {
			score = data.get(0).get("total").toString();
			one = data.get(0);
			one.put("rank", i);
		}
		for (int j = 1; j < data.size(); j++) {
			one = data.get(j);
			if (one.get("total").toString().equals(score)) {
				one.put("rank", i);
			} else {
				i++;
				score = one.get("total").toString();
				one.put("rank", i);
			}
		}
		for (HashMap<String, Object> temp : data) {
			players = temp.get("playerNames");
			if (players != null) {
				if (players.toString().contains(",")) {
					player = "("
							+ players.toString().substring(0,
									players.toString().indexOf(",")) + "等人)";
				} else {
					player = "(" + players.toString() + ")";
				}
			}
			temp.put("playerNames", player);
		}
		return excelManager.ExportForeExcel(data, fileName, matchName,
				category, matchType, matchKind);
	}

	/***
	 * 将成绩生成JSON文件
	 * 
	 * @param matchName
	 * @return 1表示临时目录不存在；-1表示程序出错；0表示推送成功；2表示推送失败；3表示无返回信息
	 */
	public String sendDataToManager(String matchName, int preliminary) {
		String tempDir = proManager.readProperties()[1];
		if (tempDir.trim().length() == 0) {
			return 1 + ":临时目录不存在";
		}
		File file = new File(tempDir, matchName + ".json");
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			List<HashMap<String, Object>> data = dao.sendDataToWeb(matchName,
					preliminary);
			int i = 1;
			HashMap<String, Object> one = null;
			String score = "";
			String categry = "";
			if (data.size() != 0) {
				categry = data.get(0).get("category").toString();
				score = data.get(0).get("total").toString();
				one = data.get(0);
				one.put("rank", i);
			}
			for (int j = 1; j < data.size(); j++) {
				one = data.get(j);
				if (categry.equals(one.get("category").toString())) {
					if (one.get("total").toString().equals(score)) {
						one.put("rank", i);
					} else {
						i++;
						score = one.get("total").toString();
						one.put("rank", i);
					}
				} else {
					categry = one.get("category").toString();
					i = 1;
					score = one.get("total").toString();
					one.put("rank", i);
				}
			}

			return jsonManager.sendDataToWeb(data, matchName);
		} catch (Exception e) {
			e.printStackTrace();
			return -1 + ":" + e.getMessage();
		}

	}

	/***
	 * 校验推送到网站的信息
	 * 
	 * @param matchName
	 * @param preliminary
	 * @return 1表示有队伍没有比完；2表示有的队伍没有web_id;3表示可以推送网站
	 */
	public int checkSendDataToWeb(String matchName, int preliminary) {
		return dao.checkSendDataToWeb(matchName, preliminary);
	}

	/***
	 * 根据已有成绩赛事名称返回该下面的赛事模式
	 * 
	 * @param matchName
	 * @return
	 */
	public List<Integer> getMatchKindByMatchName(String matchName) {
		List<HashMap<String, Object>> data = dao
				.getMatchKindByMatchName(matchName);
		List<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < data.size(); i++) {
			result.add(Boolean.valueOf(data.get(i).get("matchKind").toString()) == false ? 0
					: 1);
		}
		return result;
	}

	/***
	 * 根据赛事名称获取该赛事中的裁判的误差值
	 * 
	 * @param matchName
	 * @return
	 */
	public HashMap<String, Object> getDeviations(String matchName, int matchType) {
		HashMap<String, Object> one = null;
		List<HashMap<String, Object>> data = dao.getDeviations(matchName,
				matchType);
		if (data.size() != 0) {
			one = data.get(0);
		}
		return one;
	}

	/***
	 * 返回又不是从网站下载下来的项目名称
	 * 
	 * @param matchName
	 * @param matchType
	 * @return
	 */
	public String[] getCatoryNoWebId(String matchName, int matchType) {
		List<HashMap<String, Object>> data = dao.getCatoryNoWebId(matchName,
				matchType);
		String[] results = new String[data.size()];
		for (int i = 0; i < data.size(); i++) {
			results[i] = data.get(i).get("category").toString();
		}
		return results;
	}

	/***
	 * 判断是否成功的连接了数据库
	 * 
	 * @return
	 */
	public boolean isCollected() {
		if (publicDao.isCollected() && dao.isCollected()) {
			return true;
		} else
			return false;
	}

	public void close() {
		dao.close();
		publicDao.close();
	}
}
