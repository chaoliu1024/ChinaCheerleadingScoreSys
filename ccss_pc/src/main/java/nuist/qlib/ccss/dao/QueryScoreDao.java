/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.dao;

import java.util.HashMap;
import java.util.List;

/**
 * 成绩查询Dao，包括查询已有成绩的赛事名称
 * 
 * @author Chao Liu
 * @since ccss 1.0
 */
public class QueryScoreDao {

	private ConnSQL connSql;

	/** 构造函数，读取数据库的配置，同时与数据库建立连接 */
	public QueryScoreDao() {
		connSql = new ConnSQL();
	}

	/** 判断是否连接到了数据库 */
	public boolean isCollected() {
		return connSql.isConnected();
	}

	/** 关闭数据库链接 **/
	public void close() {
		connSql.close();
	}

	/***
	 * 获取所有已有成绩的赛事名称
	 * 
	 * @return
	 */
	public List<HashMap<String, Object>> getAllMatchNames() {
		String sql = "select distinct a.match_name as matchName from match_order a,score as b where a.id=b.team_id";
		List<HashMap<String, Object>> data = connSql.selectQuery(sql,
				new Object[0]);
		return data;
	}

	/**
	 * 在首界面点击排名按钮，若此时界面中没有任何参赛队伍的id，那么根据赛事名称以及赛事类型选择id号最小的队伍
	 * 
	 * @param matchType
	 *            赛事类型
	 * @param matchName
	 *            赛事名称
	 * @return
	 */
	public List<HashMap<String, Object>> getTeamMinId(int matchType,
			String matchName) {
		String sql = "select min(a.team_id) as id from score as a,match_order as b where a.team_id=b.id and b.final_preliminary=? and b.match_name=?";
		List<HashMap<String, Object>> data = connSql.selectQuery(sql,
				new Object[] { matchType, matchName });
		return data;
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
	public List<HashMap<String, Object>> getTeamMinId(int matchType,
			String matchName, int matchNum, int matchOrder) {
		String sql = "select b.id as id from match_order as b where b.final_preliminary=? and b.match_name=? and b.match_num=? and b.match_order=?";
		List<HashMap<String, Object>> data = connSql.selectQuery(sql,
				new Object[] { matchType, matchName, matchNum, matchOrder });
		return data;
	}

	/** 参数为参赛者的id号,该函数根据参赛者的id号、比赛类型(如预赛或决赛)显示参赛者所属项目的排名情况 ,按照总分排名 */
	public List<HashMap<String, Object>> getRank(int id, int matchType) {
		String sql = null;
		sql = "select b.id as id,b.team_name as teamName,a.score1 as score1,a.score2 as score2,a.score3 as score3,a.score4 as score4,a.score5 as score5,a.score6 as score6,a.score7 as score7,a.score8 as score8,a.score9 as score9,a.score10 as score10,a.referee_sub_score as sub_score,a.referee_add_score as add_score,a.total_score as total,a.id as scoreId,b.match_category as category from score as a,match_order as b where a.team_id=b.id and b.match_category=(select match_category from match_order where id=?) and b.match_name=(select match_name from match_order where id=?) and b.final_preliminary=? "
				+ "order by a.total_score desc,dbo.getTotalScore(score1,score2,score3,score4,score5,score6,score7,score8,score9,score10,referee_sub_score,referee_add_score) desc";
		List<HashMap<String, Object>> data = connSql.selectQuery(sql,
				new Object[] { id, id, matchType });
		return data;
	}

	/** 根据站点、比赛项目、比赛类型获取该项目的比赛排名成绩(用于打印使用) */
	public List<HashMap<String, Object>> getRank(String matchName,
			String category, int matchType) {
		String sql = "select b.player_name as playerNames,b.id as id,b.team_name as teamName,a.score1 as score1,a.score2 as score2,a.score3 as score3,a.score4 as score4,a.score5 as score5,a.score6 as score6,a.score7 as score7,a.score8 as score8,a.score9 as score9,a.score10 as score10,a.referee_sub_score as sub_score,a.referee_add_score as add_score,a.total_score as total,a.id as scoreId,b.match_category as category from score as a,match_order as b where a.team_id=b.id and b.match_category=? and b.match_name=? and b.final_preliminary=? "
				+ "order by a.total_score desc,dbo.getTotalScore(score1,score2,score3,score4,score5,score6,score7,score8,score9,score10,referee_sub_score,referee_add_score) desc";
		List<HashMap<String, Object>> data = connSql.selectQuery(sql,
				new Object[] { category, matchName, matchType });
		return data;
	}

	/**
	 * 根据id号返回它所属的项目
	 * 
	 * @param id
	 * @return
	 */
	public String getCategory(int id) {
		String sql = "select match_category as category from match_order where id=?";
		List<HashMap<String, Object>> data = connSql.selectQuery(sql,
				new Object[] { id });
		return data.get(0).get("category").toString();
	}

	/**
	 * 获得比赛的项目类型
	 * 
	 * @param matchType
	 *            预赛或决赛
	 */
	public List<HashMap<String, Object>> getCategories(int matchType,
			String matchName) {
		String sql = "select distinct a.match_category as category from match_order a,score b where a.id=b.team_id and a.final_preliminary=? and a.match_name=?";
		List<HashMap<String, Object>> data = connSql.selectQuery(sql,
				new Object[] { matchType, matchName });
		return data;
	}

	/***
	 * 根据已有成绩赛事名称返回该下面的赛事模式
	 * 
	 * @param matchName
	 * @return
	 */
	public List<HashMap<String, Object>> getMatchKindByMatchName(
			String matchName) {
		String sql = "select distinct a.final_preliminary as matchKind from match_order a,score b where a.id=b.team_id and a.match_name=?";
		List<HashMap<String, Object>> data = connSql.selectQuery(sql,
				new Object[] { matchName });
		return data;
	}

	/***
	 * 将成绩推送到网站
	 * 
	 * @param matchName
	 * @return
	 */
	public List<HashMap<String, Object>> sendDataToWeb(String matchName,
			int preliminary) {
		String sql = "select c.total_score as total,a.web_id as webId,c.total_score as totalScore,b.match_category as category from web_json a,match_order b,score c where a.web_id is not null and a.id=b.web_json_id and b.id=c.team_id and b.match_name=? and b.final_preliminary=? order by b.match_category,c.total_score desc,dbo.getTotalScore(score1,score2,score3,score4,score5,score6,score7,score8,score9,score10,referee_sub_score,referee_add_score) desc";
		List<HashMap<String, Object>> data = connSql.selectQuery(sql,
				new Object[] { matchName, preliminary });
		return data;
	}

	/***
	 * 检测推送到网站中的数据是否存在不是从网站下载来的队伍
	 * 
	 * @param matchName
	 * @param preliminary
	 * @return 1表示有队伍没有比完；2表示有的队伍没有web_id;3表示可以推送网站;4表示该赛事不是来自网站赛事
	 */
	public int checkSendDataToWeb(String matchName, int preliminary) {
		String checkWebIdSql = "select id from web_json where web_id is not null and match_name=?";
		String checkSql = "select id from match_order where unit_status=0 and match_name=? and final_preliminary=?"; // 查看是否有没有比赛完的队伍
		String sql = "select b.id from web_json a,match_order b,score c where a.web_id is null and a.id=b.web_json_id and b.id=c.team_id and b.match_name=? and b.final_preliminary=?";
		List<HashMap<String, Object>> data = connSql.selectQuery(checkWebIdSql,
				new Object[] { matchName });
		if (data.size() == 0) {
			return 4;
		} else {
			data = connSql.selectQuery(checkSql, new Object[] { matchName,
					preliminary });
			if (data.size() != 0) {
				return 1;
			} else {
				data = connSql.selectQuery(sql, new Object[] { matchName,
						preliminary });
				if (data.size() != 0) {
					return 2;
				} else
					return 3;
			}
		}
	}

	/***
	 * 根据赛事名称获取该赛事中的裁判的误差值
	 * 
	 * @param matchName
	 * @return
	 */
	public List<HashMap<String, Object>> getDeviations(String matchName,
			int matchType) {
		String sql = "select avg(b.score_error1) as error1, avg(b.score_error2) as error2,avg(b.score_error3) as error3,avg(b.score_error4) as error4,avg(b.score_error5) as error5,avg(b.score_error6) as error6,avg(b.score_error7) as error7,avg(b.score_error8) as error8,avg(b.score_error9) as error9,avg(b.score_error10) as error10 from match_order a,score b where a.id=b.team_id and a.match_name=? and a.final_preliminary=?";
		List<HashMap<String, Object>> data = connSql.selectQuery(sql,
				new Object[] { matchName, matchType });
		return data;
	}

	/***
	 * 返回又不是从网站下载下来的项目名称
	 * 
	 * @param matchName
	 * @param matchType
	 * @return
	 */
	public List<HashMap<String, Object>> getCatoryNoWebId(String matchName,
			int matchType) {
		String sql = "select distinct a.match_category as category from web_json a,match_order b,score c where a.web_id is null and a.id=b.web_json_id and b.id=c.team_id and b.match_name=? and b.final_preliminary=?";
		List<HashMap<String, Object>> data = connSql.selectQuery(sql,
				new Object[] { matchName, matchType });
		return data;
	}

	/** 根据站点、比赛项目、比赛类型获取该项目的比赛排名成绩 */
	public List<HashMap<String, Object>> getRankForNoWeb(String matchName,
			String category, int matchType) {
		String sql = "select c.web_id as webId,b.id as id,b.team_name as teamName,a.score1 as score1,a.score2 as score2,a.score3 as score3,a.score4 as score4,a.score5 as score5,a.score6 as score6,a.score7 as score7,a.score8 as score8,a.score9 as score9,a.score10 as score10,a.referee_sub_score as sub_score,a.total_score as total,a.id as scoreId,b.match_category as category from score as a,match_order as b,web_json c where c.id=b.web_json_id and a.team_id=b.id and b.match_category=? and b.match_name=? and b.final_preliminary=? "
				+ "order by a.total_score desc,dbo.getTotalScore(score1,score2,score3,score4,score5,score6,score7,score8,score9,score10,referee_sub_score,referee_add_score) desc";
		List<HashMap<String, Object>> data = connSql.selectQuery(sql,
				new Object[] { category, matchName, matchType });
		return data;
	}
}
