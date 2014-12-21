/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.manager.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nuist.qlib.ccss.dao.TeamScoreDao;

/**
 * 比赛进行时,所有队伍信息与成绩操作
 * 
 * @author Chao Liu
 * @since ccss 1.0
 */
public class MatchTeamScore {
	TeamScoreDao dao;

	public MatchTeamScore() {
		dao = new TeamScoreDao();
	}

	/**
	 * 得到未进行比赛的场次
	 * 
	 * @return List<Integer> 场次List
	 * @throws
	 */
	public List<String> getMatchNum(String matchName) {
		List<HashMap<String, Object>> data = dao.getMatchNum(matchName);
		List<String> results = new ArrayList<String>();
		for (int i = 0; i < data.size(); i++) {
			results.add(data.get(i).get("matchNum").toString());
		}
		return results;
	}

	/**
	 * 获得初始时的比赛场次
	 * 
	 * @param matchName
	 *            赛事名称
	 * @return String
	 * @throws
	 */
	public int getInitMatchNum(String matchName) {
		List<HashMap<String, Object>> data = dao.getInitMatchNum(matchName);
		int result = Integer.parseInt(data.get(0).get("matchNum").toString());
		return result;
	}

	/**
	 * 获取赛事模式
	 * 
	 * @param matchName
	 *            赛事名称
	 * @param matchNum
	 *            比赛场次
	 * @return List<String>
	 * @throws
	 */
	public List<String> getMatchType(String matchName, String matchNum) {
		List<HashMap<String, Object>> data = dao.getMatchType(matchName,
				matchNum);
		List<String> results = new ArrayList<String>();
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).get("finalPreliminary").toString().equals("false"))
				results.add("预赛");
			if (data.get(i).get("finalPreliminary").toString().equals("true"))
				results.add("决赛");
		}
		return results;
	}

	/**
	 * 得到初始第一个参赛队伍信息
	 * 
	 * @param matchNum
	 *            比赛场次
	 * @param matchType
	 *            预赛/决赛
	 * @return HashMap<String,Object>
	 * @throws
	 */
	public List<HashMap<String, Object>> getInitTeam(int matchNum,
			int matchType, String matchName) {
		List<HashMap<String, Object>> data = dao.getInitTeam(matchNum,
				matchType, matchName);
		return data;
	}

	/**
	 * 得到赛事名称
	 * 
	 * @return String
	 * @throws
	 */
	public String[] getMatchNames() {
		List<HashMap<String, Object>> data = dao.getMatchName();
		String[] results = new String[data.size()];
		for (int i = 0; i < data.size(); i++) {
			results[i] = data.get(i).get("matchName").toString();
		}
		return results;
	}

	/***
	 * 根据赛事名称返回该下面的赛事模式
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

	/**
	 * 更改队伍状态
	 * 
	 * @param matchOrder
	 *            出场顺序
	 * @param status
	 *            队伍状态信息:'1'已比赛, '2'延迟比赛, '3'弃权
	 * @return '1'更新成功，'-1'更新失败
	 * @throws
	 */
	public int updateTeamStatu(int id, int status) {
		return dao.updateTeamStatu(id, status);
	}

	/**
	 * 
	 * 比赛成绩录入数据库
	 * 
	 * @param matchName
	 *            赛事名称
	 * @param match_num
	 *            比赛场次
	 * @param matchOrder
	 *            出场顺序
	 * @param score1
	 *            成绩1
	 * @param score2
	 *            成绩2
	 * @param score3
	 *            成绩3
	 * @param score4
	 *            成绩4
	 * @param score5
	 *            成绩5
	 * @param score6
	 *            成绩6
	 * @param score7
	 *            成绩7
	 * @param score8
	 *            成绩8
	 * @param score9
	 *            成绩9
	 * @param score10
	 *            成绩10
	 * @param sub_score
	 *            裁判长减分
	 * @param total_score
	 *            总成绩
	 * @param score_error1
	 *            误差1
	 * @param score_error2
	 *            误差2
	 * @param score_error3
	 *            误差3
	 * @param score_error4
	 *            误差4
	 * @param score_error5
	 *            误差5
	 * @param score_error6
	 *            误差6
	 * @param score_error7
	 *            误差7
	 * @param score_error8
	 *            误差8
	 * @param score_error9
	 *            误差9
	 * @param score_error10
	 *            误差10
	 * @return
	 * @throws
	 */
	public int insertScore(int id, String score1, String score2, String score3,
			String score4, String score5, String score6, String score7,
			String score8, String score9, String score10, String sub_score,
			String total_score, float score_error1, float score_error2,
			float score_error3, float score_error4, float score_error5,
			float score_error6, float score_error7, float score_error8,
			float score_error9, float score_error10) {

		Boolean isInserted = dao.selectOneTeam(id);
		if (isInserted) {
			// 更新成绩
			return dao.updateTeamScore(id, score1, score2, score3, score4,
					score5, score6, score7, score8, score9, score10, sub_score,
					total_score, score_error1, score_error2, score_error3,
					score_error4, score_error5, score_error6, score_error7,
					score_error8, score_error9, score_error10);
		} else {
			// 插入成绩
			return dao.insertTeamScore(id, score1, score2, score3, score4,
					score5, score6, score7, score8, score9, score10, sub_score,
					total_score, score_error1, score_error2, score_error3,
					score_error4, score_error5, score_error6, score_error7,
					score_error8, score_error9, score_error10);
		}
	}

	/**
	 * 
	 * 比赛成绩录入数据库
	 * 
	 * @param matchName
	 *            赛事名称
	 * @param match_num
	 *            比赛场次
	 * @param matchOrder
	 *            出场顺序
	 * @param score1
	 *            成绩1
	 * @param score2
	 *            成绩2
	 * @param score3
	 *            成绩3
	 * @param score4
	 *            成绩4
	 * @param score5
	 *            成绩5
	 * @param score6
	 *            成绩6
	 * @param score7
	 *            成绩7
	 * @param score8
	 *            成绩8
	 * @param score9
	 *            成绩9
	 * @param score10
	 *            成绩10
	 * @param sub_score
	 *            裁判长减分
	 * @param total_score
	 *            总成绩
	 * @param score_error1
	 *            误差1
	 * @param score_error2
	 *            误差2
	 * @param score_error3
	 *            误差3
	 * @param score_error4
	 *            误差4
	 * @param score_error5
	 *            误差5
	 * @param score_error6
	 *            误差6
	 * @param score_error7
	 *            误差7
	 * @param score_error8
	 *            误差8
	 * @param score_error9
	 *            误差9
	 * @return
	 * @throws
	 */
	public int insertScore(int id, String score1, String score2, String score3,
			String score4, String score5, String score6, String score7,
			String score8, String score9, String score10, String sub_score,
			String total_score, float score_error1, float score_error2,
			float score_error3, float score_error4, float score_error5,
			float score_error6, float score_error7, float score_error8,
			float score_error9) {

		Boolean isInserted = dao.selectOneTeam(id);
		if (isInserted) {
			// 更新成绩
			return dao.updateTeamScore(id, score1, score2, score3, score4,
					score5, score6, score7, score8, score9, score10, sub_score,
					total_score, score_error1, score_error2, score_error3,
					score_error4, score_error5, score_error6, score_error7,
					score_error8, score_error9);
		} else {
			// 插入成绩
			return dao.insertTeamScore(id, score1, score2, score3, score4,
					score5, score6, score7, score8, score9, score10, sub_score,
					total_score, score_error1, score_error2, score_error3,
					score_error4, score_error5, score_error6, score_error7,
					score_error8, score_error9);
		}
	}

	/**
	 * 
	 * 得到下一只队伍的相关信息
	 * 
	 * @param matchType
	 *            赛事模式：预赛/决赛
	 * @param matchOrder
	 *            出场顺序
	 * @param match_num
	 *            组别
	 * @return
	 * @throws
	 */
	public List<HashMap<String, Object>> getNextTeam(int matchType,
			int matchOrder, int match_num, String matchName) {
		List<HashMap<String, Object>> data = dao.getNextTeam(matchType,
				matchOrder, match_num, matchName);
		return data;
	}

	/**
	 * 获取重新比赛队伍的出场顺序
	 * 
	 * @param matchType
	 * @param matchOrder
	 * @param match_num
	 * @return
	 */
	public List<HashMap<String, Object>> getReTeam(int re_id) {
		List<HashMap<String, Object>> data = dao.getReTeam(re_id);
		return data;
	}

	/**
	 * 返回暂停比赛的队伍
	 * 
	 * @param matchName
	 *            赛事名称
	 * @param matchType
	 *            赛事模式：预赛/决赛
	 * @return
	 * @throws
	 */
	public List<String> getNoRaceTeam(String matchName, int matchType) {
		List<HashMap<String, Object>> data = dao.getNoRaceTeam(matchName,
				matchType);
		List<String> teamName = new ArrayList<String>();
		for (int i = 0; i < data.size(); i++) {
			teamName.add(data.get(i).get("teamName").toString());
		}
		return teamName;
	}

	/**
	 * 返回暂停比赛队伍的项目
	 * 
	 * @param matchName
	 *            赛事名称
	 * @param match_num
	 *            比赛场次
	 * @param team
	 *            参赛队伍
	 * @param matchType
	 *            赛事模式：预赛/决赛
	 * @return
	 * @throws
	 */
	public List<String> getNoRaceCategory(String matchName, String team,
			int matchType) {
		List<HashMap<String, Object>> data = dao.getNoRaceCategory(matchName,
				team, matchType);
		List<String> category = new ArrayList<String>();
		for (int i = 0; i < data.size(); i++) {
			category.add(data.get(i).get("category").toString());
		}
		return category;
	}

	/**
	 * 得到暂停比赛的队伍ID
	 * 
	 * @param matchName
	 *            赛事名称
	 * @param match_num
	 *            比赛场次
	 * @param matchType
	 *            赛事模式：预赛/决赛
	 * @param team
	 *            参赛队伍
	 * @param category
	 *            参赛项目
	 * @return
	 * @throws
	 */
	public int getNoRaceTeamID(String matchName, int matchType, String team,
			String category) {
		List<HashMap<String, Object>> data = dao.getNoRaceTeamID(matchName,
				matchType, team, category);
		int re_id = Integer.parseInt(String.valueOf(data.get(0).get("re_id")));
		return re_id;
	}

	/**
	 * 得到所有需要进行补赛的赛事名称
	 * 
	 * @return
	 * @throws
	 */
	public String[] getAllReplayMatchNames() {
		List<HashMap<String, Object>> data = dao.getAllReplayMatchNames();
		String[] results = new String[data.size()];
		for (int i = 0; i < data.size(); i++) {
			results[i] = data.get(i).get("matchName").toString();
		}
		return results;
	}

	public boolean isCollected() {
		return dao.isCollected();
	}

	public void close() {
		dao.close();
	}
}
