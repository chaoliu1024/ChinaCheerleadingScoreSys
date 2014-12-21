/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.manager.score;

import java.util.HashMap;
import java.util.List;

import nuist.qlib.ccss.dao.PublicDao;
import nuist.qlib.ccss.dao.ReviseScoreDao;

/**
 * 修订成绩
 * 
 * @author Fang Wang
 * @since ccss 1.0
 */
public class ReviseScore {
	private ReviseScoreDao dao;
	private PublicDao publicDao;

	public ReviseScore() {
		dao = new ReviseScoreDao();
		publicDao = new PublicDao();
	}

	/**
	 * 更新技巧数据
	 * 
	 * @param data
	 * @return
	 */
	public boolean updateSkillAll(List<HashMap<String, Object>> data) {
		return dao.updateReviseSkillAll(data);
	}

	/***
	 * 更新舞蹈数据
	 * 
	 * @param data
	 * @return
	 */
	public boolean updateDanceAll(List<HashMap<String, Object>> data) {
		return dao.updateReviseDanceAll(data);
	}

	/***
	 * 返回更新后的数据
	 * 
	 * @param matchName
	 * @param category
	 * @param matchType
	 * @return
	 */
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
		for (int j = 1; i < data.size(); j++) {
			one = data.get(j);
			if (one.get("total").toString().equals(score)) {
				one.put("rank", i);
				i++;
			} else {
				i++;
				score = one.get("total").toString();
				one.put("rank", i);
			}
		}
		return data;
	}

	/***
	 * 判断是否成功的连接了数据库
	 * 
	 * @return
	 */
	public boolean isCollected() {
		if (dao.isCollected() && publicDao.isCollected())
			return true;
		else
			return false;
	}

	public void close() {
		dao.close();
		publicDao.close();
	}
}
