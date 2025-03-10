/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 比赛进行时队伍、成绩的数据库操
 * 
 * @author Chao Liu
 * @since ccss 1.0
 */
public class TeamScoreDao {

	private ConnSQL connSql;
	private Logger logger;

	PreparedStatement st;
	Connection conn;

	/**
	 * 取数据库的配置，同时与数据库建立连接
	 */
	public TeamScoreDao() {
		logger = Logger.getLogger(TeamScoreDao.class);
		this.connSql = new ConnSQL();
		conn = connSql.connectDataBase();
	}

	/** 判断是否连接到了数据库 */
	public boolean isCollected() {
		return connSql.isConnected();
	}

	/** 关闭数据库链接 **/
	public void close() {
		connSql.close();
	}

	/**
	 * 获取赛事名称
	 * 
	 * @return List<HashMap<String,Object>>
	 * @throws
	 */
	public List<HashMap<String, Object>> getMatchName() {
		String sql = "select distinct match_name as matchName from match_order where unit_status=0";
		List<HashMap<String, Object>> data = connSql.selectQuery(sql,
				new Object[0]);
		return data;
	}

	/***
	 * 根据赛事名称返回该下面的赛事模式
	 * 
	 * @param matchName
	 * @return
	 */
	public List<HashMap<String, Object>> getMatchKindByMatchName(
			String matchName) {
		String sql = "select distinct a.final_preliminary as matchKind from match_order a where a.match_name=? and a.unit_status=0";
		List<HashMap<String, Object>> data = connSql.selectQuery(sql,
				new Object[] { matchName });
		return data;
	}

	/**
	 * 
	 * 获取未进行比赛的场次
	 * 
	 * @return List<Integer> 未进行比赛的场次列表
	 * @throws
	 */
	public List<HashMap<String, Object>> getMatchNum(String matchName) {
		String sql = "select Distinct(match_num) as matchNum from match_order where unit_status = 0 and match_name = ?";
		List<HashMap<String, Object>> data = connSql.selectQuery(sql,
				new Object[] { matchName });
		return data;
	}

	/**
	 * 获取初始时的比赛场次
	 * 
	 * @param matchName
	 * @return List<HashMap<String,Object>>
	 * @throws
	 */
	public List<HashMap<String, Object>> getInitMatchNum(String matchName) {
		String sql = "select min(match_num) as matchNum from match_order where unit_status = 0 and match_name = ?";
		List<HashMap<String, Object>> data = connSql.selectQuery(sql,
				new Object[] { matchName });
		return data;
	}

	/**
	 * 获取比赛类型 预赛/决赛
	 * 
	 * @param matchName
	 *            赛事名称
	 * @param matchNum
	 *            比赛场次
	 * @return List<HashMap<String,Object>>
	 * @throws
	 */
	public List<HashMap<String, Object>> getMatchType(String matchName,
			String matchNum) {
		String sql = "select Distinct(final_preliminary) as finalPreliminary from match_order where match_num = ? and match_name = ? and unit_status = 0";
		List<HashMap<String, Object>> data = connSql.selectQuery(sql,
				new Object[] { matchNum, matchName });
		return data;
	}

	/**
	 * 获取初始队伍信息
	 * 
	 * @param matchNum
	 * @param matchType
	 * @return List<HashMap<String,Object>>
	 * @throws
	 */
	public List<HashMap<String, Object>> getInitTeam(int matchNum,
			int matchType, String matchName) {
		String sql = "select id as id, match_order as matchOrder, match_num as matchNum, match_category as category, team_name as teamName,final_preliminary as final, match_name as matchName from match_order where unit_status=0 and match_num=? and final_preliminary=? and match_name=? and match_order in(select MIN(match_order) from match_order where unit_status = 0 and match_num=? and final_preliminary=? and match_name=?)";
		List<HashMap<String, Object>> data = connSql.selectQuery(sql,
				new Object[] { matchNum, matchType, matchName, matchNum,
						matchType, matchName });
		return data;
	}

	/**
	 * 更新队伍状态
	 * 
	 * @param matchOrder
	 *            出场顺序
	 * @param status
	 *            队伍状态
	 * @return
	 * @throws
	 */
	public int updateTeamStatu(int id, int status) {
		String sql = "update match_order set unit_status = ? where id = ?";
		return connSql.updateObject(sql, new Object[] { status, id });
	}

	/**
	 * 查询队伍记录
	 * 
	 * @param id
	 *            队伍id
	 * @return
	 * @throws
	 */
	public Boolean selectOneTeam(int id) {
		String sql = "select team_id from score where team_id = ?";
		if ((connSql.selectQuery(sql, new Object[] { id })).size() > 0)
			return true;
		else
			return false;
	}

	/**
	 * 更新比赛成绩 <b>10</b>个误差分数
	 * 
	 * @param matchName
	 *            赛事名称
	 * @param match_num
	 *            比赛场次
	 * @param matchOrder
	 *            出场顺序
	 */
	public int updateTeamScore(int id, String score1, String score2,
			String score3, String score4, String score5, String score6,
			String score7, String score8, String score9, String score10,
			String sub_score, String add_score, String total_score,
			float score_error1, float score_error2, float score_error3,
			float score_error4, float score_error5, float score_error6,
			float score_error7, float score_error8, float score_error9,
			float score_error10) {
		String sql = "update score set score1 = ?, score2 = ?, score3 = ?, score4 = ?, score5 = ?, score6 = ?, score7 = ?, score8 = ?, score9 = ?, score10 = ?, referee_sub_score = ?, referee_add_score = ?, total_score = ?, score_error1 = ?, score_error2 = ?, score_error3 = ?, score_error4 = ?, score_error5 = ?, score_error6 = ?, score_error7 = ?, score_error8 = ?, score_error9 = ?, score_error10 = ? where team_id = ?";

		return connSql.updateObject(sql, new Object[] { score1, score2, score3,
				score4, score5, score6, score7, score8, score9,
				score10, sub_score, add_score, total_score, score_error1,
				score_error2, score_error3, score_error4, score_error5,
				score_error6, score_error7, score_error8, score_error9,
				score_error10, id });
	}

	/**
	 * 插入比赛成绩 <b>10</b>个误差分数
	 */
	public int insertTeamScore(int id, String _score1, String _score2,
			String _score3, String _score4, String _score5, String _score6,
			String _score7, String _score8, String _score9, String _score10,
			String _sub_score, String _add_score, String _total_score,
			float score_error1, float score_error2, float score_error3,
			float score_error4, float score_error5, float score_error6,
			float score_error7, float score_error8, float score_error9,
			float score_error10) {

		float score1 = Float.parseFloat(_score1);
		float score2 = Float.parseFloat(_score2);
		float score3 = Float.parseFloat(_score3);
		float score4 = Float.parseFloat(_score4);
		float score5 = Float.parseFloat(_score5);
		float score6 = Float.parseFloat(_score6);
		float score7 = Float.parseFloat(_score7);
		float score8 = Float.parseFloat(_score8);
		float score9 = Float.parseFloat(_score9);
		float score10 = Float.parseFloat(_score10);
		float sub_score = Float.parseFloat(_sub_score);
		float add_score = Float.parseFloat(_add_score);
		float total_score = Float.parseFloat(_total_score);

		String sql = "insert into score (team_id, score1, score2, score3, score4, score5, score6, score7, score8, score9, score10, referee_sub_score, referee_add_score, total_score, score_error1, score_error2, score_error3, score_error4, score_error5, score_error6, score_error7, score_error8, score_error9, score_error10) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		return connSql.insertObject(sql, new Object[] { id, score1, score2,
				score3, score4, score5, score6, score7, score8, score9,
				score10, sub_score, add_score, total_score, score_error1,
				score_error2, score_error3, score_error4, score_error5,
				score_error6, score_error7, score_error8, score_error9,
				score_error10 });
	}

	/**
	 * <b>9</b>个误差分数
	 * 
	 * @return
	 * @throws
	 */
	public int updateTeamScore(int id, String score1, String score2,
			String score3, String score4, String score5, String score6,
			String score7, String score8, String score9, String sub_score,
			String add_score, String total_score, float score_error1,
			float score_error2, float score_error3, float score_error4,
			float score_error5, float score_error6, float score_error7,
			float score_error8, float score_error9) {
		String sql = "update score set score1 = ?, score2 = ?, score3 = ?, score4 = ?, score5 = ?, score6 = ?, score7 = ?, score8 = ?, score9 = ?, referee_sub_score = ?, referee_add_score = ?, total_score = ?, score_error1 = ?, score_error2 = ?, score_error3 = ?, score_error4 = ?, score_error5 = ?, score_error6 = ?, score_error7 = ?, score_error8 = ?, score_error9 = ? where team_id = ?";

		return connSql.updateObject(sql, new Object[] { score1, score2, score3,
				score4, score5, score6, score7, score8, score9, sub_score,
				add_score, total_score, score_error1, score_error2,
				score_error3, score_error4, score_error5, score_error6,
				score_error7, score_error8, score_error9, id });
	}

	/**
	 * 插入比赛成绩 <b>9/b>个误差分数
	 */
	public int insertTeamScore(int id, String _score1, String _score2,
			String _score3, String _score4, String _score5, String _score6,
			String _score7, String _score8, String _score9, String _sub_score,
			String _add_score, String _total_score, float score_error1,
			float score_error2, float score_error3, float score_error4,
			float score_error5, float score_error6, float score_error7,
			float score_error8, float score_error9) {

		float score1 = Float.parseFloat(_score1);
		float score2 = Float.parseFloat(_score2);
		float score3 = Float.parseFloat(_score3);
		float score4 = Float.parseFloat(_score4);
		float score5 = Float.parseFloat(_score5);
		float score6 = Float.parseFloat(_score6);
		float score7 = Float.parseFloat(_score7);
		float score8 = Float.parseFloat(_score8);
		float score9 = Float.parseFloat(_score9);
		float sub_score = Float.parseFloat(_sub_score);
		float add_score = Float.parseFloat(_add_score);
		float total_score = Float.parseFloat(_total_score);

		String sql = "insert into score (team_id, score1, score2, score3, score4, score5, score6, score7, score8, score9, referee_sub_score, referee_add_score, total_score, score_error1, score_error2, score_error3, score_error4, score_error5, score_error6, score_error7, score_error8, score_error9) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		return connSql.insertObject(sql, new Object[] { id, score1, score2,
				score3, score4, score5, score6, score7, score8, score9,
				sub_score, add_score, total_score, score_error1, score_error2,
				score_error3, score_error4, score_error5, score_error6,
				score_error7, score_error8, score_error9 });
	}

	/**
	 * 得到延迟比赛的队伍信息
	 * 
	 * @param matchName
	 *            赛事名称
	 * @param matchType
	 *            赛事模式：预赛/决赛
	 * @return
	 * @throws
	 */
	public List<HashMap<String, Object>> getNoRaceTeam(String matchName,
			int matchType) {
		String sql = "select Distinct(team_name) as teamName from match_order where unit_status=2 and match_name = ? and final_preliminary = ?";
		List<HashMap<String, Object>> data = connSql.selectQuery(sql,
				new Object[] { matchName, matchType });
		return data;
	}

	/**
	 * 获取重新比赛队伍的出场顺序
	 * 
	 * @param re_id
	 * @return
	 */
	public List<HashMap<String, Object>> getReTeam(int re_id) {
		String sql = "SELECT match_order as matchOrder FROM match_order where id=?";
		List<HashMap<String, Object>> data = connSql.selectQuery(sql,
				new Object[] { re_id });
		return data;
	}

	/**
	 * 
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
	public List<HashMap<String, Object>> getNoRaceCategory(String matchName,
			String team, int matchType) {
		String sql = "select match_category as category from match_order where unit_status=2 and match_name = ? and team_name = ? and final_preliminary = ?";
		List<HashMap<String, Object>> data = connSql.selectQuery(sql,
				new Object[] { matchName, team, matchType });
		return data;
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
	public List<HashMap<String, Object>> getNoRaceTeamID(String matchName,
			int matchType, String team, String category) {
		String sql = "select id as re_id from match_order where unit_status=2 and match_name = ? and final_preliminary = ? and team_name = ? and match_category = ?";
		List<HashMap<String, Object>> data = connSql.selectQuery(sql,
				new Object[] { matchName, matchType, team, category });
		return data;
	}

	/**
	 * 得到所有需要进行补赛的赛事名称
	 * 
	 * @return
	 * @throws
	 */
	public List<HashMap<String, Object>> getAllReplayMatchNames() {
		String sql = "select distinct match_name as matchName from match_order where unit_status = 2";
		List<HashMap<String, Object>> data = connSql.selectQuery(sql,
				new Object[0]);
		return data;
	}

	/**
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
		String sql = "select id as id, match_order as matchOrder, match_num as matchNum, match_category as category, team_name as teamName,final_preliminary as final, match_name as matchName from match_order where unit_status=0 and match_num = ? and final_preliminary = ? and match_order = ? and match_name=?";
		List<HashMap<String, Object>> data = connSql.selectQuery(sql,
				new Object[] { match_num, matchType, matchOrder, matchName });
		return data;
	}

	/**
	 * 非统一数据库关闭函数
	 * 
	 * @throws
	 */
	public void _close() {
		try {
			if (!conn.isClosed()) {
				conn.close();
			}
			if (st != null && !st.isClosed()) {
				st.close();
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
}
