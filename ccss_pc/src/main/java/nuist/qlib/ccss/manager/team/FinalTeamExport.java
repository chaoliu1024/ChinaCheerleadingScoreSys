/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.manager.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nuist.qlib.ccss.dao.FinalOperDao;

/**
 * 决赛数据导出
 * 
 * @author Fang Wang
 * @since ccss 1.0
 */
public class FinalTeamExport {
	private FinalOperDao dao;

	public FinalTeamExport() {
		dao = new FinalOperDao();
	}

	/***
	 * 将需要决赛的队伍中的相应标志位改变
	 * 
	 * @param data
	 * @return
	 */
	public int setFinalTeam(List<HashMap<String, Object>> data) {
		return dao.setFinalTeam(data);
	}

	/***
	 * 获得预赛的该场比赛的所有项目名称
	 * 
	 * @param matchName
	 * @return 返回的List中键值对为category,count(该项目中总共有多少个队伍)
	 */
	public List<HashMap<String, Object>> getAllCategory(String matchName) {
		List<HashMap<String, Object>> data = dao.getAllCategory(matchName);
		int count;
		for (HashMap<String, Object> one : data) {
			count = one.get("hasMatchCount") == null ? 0 : Integer.valueOf(one
					.get("hasMatchCount").toString());
			if (count > 8) {
				one.put("exportCount", 8);
			} else {
				one.put("exportCount", count);
			}
		}
		return data;
	}

	/***
	 * 一键式导出决赛队伍
	 * 
	 * @param matchName
	 *            赛事名称
	 * @param category
	 *            其中包括键值category,count(该项目中要导出前多少个队伍)
	 * @return 返回的List中键值对为match_order表中的id
	 */
	public int setFinalTeamAll(List<HashMap<String, Object>> categorys,
			String matchName) {
		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> one = null;
		List<HashMap<String, Object>> data_temp;
		for (HashMap<String, Object> temp : categorys) {
			data_temp = dao.getTeamByCategory(
					Integer.valueOf(temp.get("selectCount").toString()),
					matchName, temp.get("category").toString());
			for (int i = 0; i < data_temp.size(); i++) {
				one = new HashMap<String, Object>();
				one.put("id", data_temp.get(i).get("id"));
				data.add(one);
			}
		}
		if (data.size() != 0) {
			return dao.setFinalTeam(data);
		} else
			return -1;
	}

	/***
	 * 判断是否成功的连接了数据库
	 * 
	 * @return
	 */
	public boolean isCollected() {
		return dao.isCollected();
	}

	public void close() {
		dao.close();
	}
}
