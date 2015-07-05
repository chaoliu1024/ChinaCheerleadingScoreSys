/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.dao;

import java.util.HashMap;
import java.util.List;

/**
 * @author Chao Liu
 * @since ccss 1.0
 */
public class PublicDao {
	// 连接数据库需要的变量
	private ConnSQL connSql;

	/** 构造函数，读取数据库的配置，同时与数据库建立连接 */
	public PublicDao() {
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

	/** 根据站点、比赛项目、比赛类型获取该项目的比赛排名成绩 */
	public List<HashMap<String, Object>> getRank(String matchName,
			String category, int matchType) {
		String sql = "select b.id as id,b.team_name as teamName,a.score1 as score1,a.score2 as score2,a.score3 as score3,a.score4 as score4,a.score5 as score5,a.score6 as score6,a.score7 as score7,a.score8 as score8,a.score9 as score9,a.score10 as score10,a.referee_sub_score as sub_score, a.referee_add_score as add_score, a.total_score as total,a.id as scoreId,b.match_category as category from score as a,match_order as b where a.team_id=b.id and b.match_category=? and b.match_name=? and b.final_preliminary=? "
				+ "order by a.total_score desc,dbo.getTotalScore(score1,score2,score3,score4,score5,score6,score7,score8,score9,score10,referee_sub_score,referee_add_score) desc";
		List<HashMap<String, Object>> data = connSql.selectQuery(sql,
				new Object[] { category, matchName, matchType });
		return data;
	}

	/**
	 * 
	 * 得到证书PDF的相关信息
	 * 
	 * @param matchName
	 *            赛事名称
	 * @param category
	 *            参赛项目
	 * @param matchType
	 *            预赛/决赛
	 * @return
	 * @throws
	 */
	public List<HashMap<String, Object>> getCertificateInfor(String matchName,
			String category, int matchType) {
		String sql = "select a.total_score as total,c.match_units as units,c.tb_name as tbNames,b.match_num as matchNum, b.match_order as matchOrder, b.match_name as matchName, b.team_name as teamName, b.match_category as category, b.player_name as playerName,c.coach_name as coachName from score as a,match_order as b,web_json as c where b.web_json_id=c.id and a.team_id=b.id and b.match_category=? and b.match_name=? and b.final_preliminary=? "
				+ "order by a.total_score desc,dbo.getTotalScore(score1,score2,score3,score4,score5,score6,score7,score8,score9,score10,referee_sub_score,referee_add_score) desc";
		List<HashMap<String, Object>> data = connSql.selectQuery(sql,
				new Object[] { category, matchName, matchType });
		return data;
	}
}
