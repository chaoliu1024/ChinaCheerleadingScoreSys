/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.dao;

import java.util.HashMap;
import java.util.List;

/**
 * 将web_json中的sort_flat以及决赛标志位更改，变成决赛为排序队伍
 * 
 * @author Fang Wang
 * @since ccss 1.0
 */
public class FinalOperDao {
	// 连接数据库需要的变量
	private ConnSQL connSql;

	/** 构造函数，读取数据库的配置，同时与数据库建立连接 */
	public FinalOperDao() {
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
	 * 将需要决赛的队伍中的相应标志位改变
	 * 
	 * @param data
	 * @return
	 */
	public int setFinalTeam(List<HashMap<String, Object>> data) {
		String sql = "update web_json set final_preliminary=1,sort_flag=0 where id=(select web_json_id from match_order where id=?)";
		int result = connSql.insertBatch(data, sql, new String[] { "id" });
		return result;
	}

	/***
	 * 获得预赛的该场比赛的所有项目名称
	 * 
	 * @param matchName
	 * @return
	 */
	public List<HashMap<String, Object>> getAllCategory(String matchName) {
		String sql = "select a.match_category as category,count(a.match_category) as totalCount from match_order a where a.match_name=? and a.final_preliminary=0 and a.match_category not in(select distinct match_category from web_json where match_name=? and final_preliminary=1)group by a.match_category";
		List<HashMap<String, Object>> result = connSql.selectQuery(sql,
				new Object[] { matchName, matchName });
		List<HashMap<String, Object>> subResult = null;
		for (int i = 0; i < result.size(); i++) {
			String category = result.get(i).get("category").toString();
			sql = "select count(*) as leftCount from match_order where match_name=? and final_preliminary=0 and match_category=? and unit_status=0"; // 该项目中没有比赛的队伍数量
			subResult = connSql.selectQuery(sql, new Object[] { matchName,
					category });
			if (subResult.size() != 0) {
				result.get(i).put("leftCount",
						subResult.get(0).get("leftCount"));
			}
			sql = "select count(*) as hasMatchCount from match_order where match_name=? and final_preliminary=0 and match_category=? and unit_status=1"; // 该项目中已经比赛的队伍数量
			subResult = connSql.selectQuery(sql, new Object[] { matchName,
					category });
			if (subResult.size() != 0) {
				result.get(i).put("hasMatchCount",
						subResult.get(0).get("hasMatchCount"));
			}
			sql = "select count(*) as replayCount from match_order where match_name=? and final_preliminary=0 and match_category=? and unit_status=2"; // 该项目中暂停比赛的队伍数量
			subResult = connSql.selectQuery(sql, new Object[] { matchName,
					category });
			if (subResult.size() != 0) {
				result.get(i).put("replayCount",
						subResult.get(0).get("replayCount"));
			}
			sql = "select count(*) as abstainCount from match_order where match_name=? and final_preliminary=0 and match_category=? and unit_status=3"; // 该项目中弃权比赛的队伍数量
			subResult = connSql.selectQuery(sql, new Object[] { matchName,
					category });
			if (subResult.size() != 0) {
				result.get(i).put("abstainCount",
						subResult.get(0).get("abstainCount"));
			}
		}
		return result;
	}

	/***
	 * 获取每个项目排名前count名的队伍
	 * 
	 * @param count
	 * @param matchName
	 * @param category
	 * @return
	 */
	public List<HashMap<String, Object>> getTeamByCategory(int count,
			String matchName, String category) {
		String sql = "select top(?) b.id as id from score as a,match_order as b where a.team_id=b.id and b.match_category=? and b.match_name=? and b.final_preliminary=0 "
				+ "order by a.total_score desc,dbo.getTotalScore(score1,score2,score3,score4,score5,score6,score7,score8,score9,score10,referee_sub_score,referee_add_score) desc";
		List<HashMap<String, Object>> result = connSql.selectQuery(sql,
				new Object[] { count, category, matchName });
		return result;
	}
}
