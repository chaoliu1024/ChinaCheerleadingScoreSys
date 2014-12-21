/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.dao;

import java.util.HashMap;
import java.util.List;

/**
 * 队伍出场顺序排序
 * 
 * @author Zhengfei Chen
 * @since ccss 1.0
 */
public class MatchOrderDao {

	private ConnSQL connSql;

	public MatchOrderDao() {
		connSql = new ConnSQL();
	}

	/**
	 * 批量插入出场顺序
	 * 
	 * @param match_OrderList
	 */
	public Boolean insertMatch_Order(
			List<HashMap<String, Object>> match_OrderList) {
		boolean mark = false;
		String sql = "insert into match_order(web_json_id,match_num,match_order,match_category,team_name,final_preliminary,match_name,unit_status,player_name)values(?,?,?,?,?,?,?,?,?)";
		if (connSql.insertBatch(match_OrderList, sql,
				new String[] { "ID", "match_num", "match_order",
						"match_category", "team_name", "final_preliminary",
						"match_name", "unit_status", "player_name" }) > 0)// 插入成功
			mark = true;
		return mark;

	}

	/**
	 * 插入一条出场顺序
	 * 
	 * @param match_Order
	 *            []
	 * @return
	 */
	public Boolean insertOneMatch_Order(Object match_Order[]) {
		connSql = new ConnSQL();// 连接数据库
		boolean mark = false;
		String sql = "insert into match_order(web_json_id,match_num,match_order,match_category,team_name,final_preliminary,match_name,unit_status,player_name)values(?,?,?,?,?,?,?,?,?)";
		if (connSql.insertObject(sql, match_Order) > 0)// 插入成功
			mark = true;
		return mark;

	}

	/**
	 * 查询某场比赛的参赛队伍
	 * 
	 * @param final_preliminary
	 * @param match_name
	 * @param matchNum
	 * @return
	 */
	public List<HashMap<String, Object>> getMatch_order(
			boolean final_preliminary, String match_name, int matchNum) {
		String sql = "select a.web_json_id as ID,a.match_category as match_category,a.team_name as team_name,a.match_name as match_name,a.final_preliminary as final_preliminary,a.player_name as player_name,b.tb_name as tb_name,b.match_units as match_units from match_order a, web_json b where a.web_json_id=b.id and a.final_preliminary=? and a.match_name=? and a.match_num=? order by a.id";// 根据预赛/决赛和赛事名称查询比赛队伍
		List<HashMap<String, Object>> Web_JsonList;
		Web_JsonList = connSql.selectQuery(sql, new Object[] {
				final_preliminary, match_name, matchNum });
		return Web_JsonList;

	}

	/**
	 * 获取本次比赛的已排场次
	 * 
	 * @param final_preliminary
	 * @param match_name
	 * @return
	 */
	public String[] getMatchNum(boolean final_preliminary, String match_name) {
		String sql = "select distinct match_num from match_order where final_preliminary=? and match_name=?";// 根据预赛/决赛和赛事名称查询比赛队伍
		List<HashMap<String, Object>> matchNumList;
		matchNumList = connSql.selectQuery(sql, new Object[] {
				final_preliminary, match_name });
		String matchNum[] = new String[matchNumList.size()];
		for (int i = 0; i < matchNum.length; i++) {
			matchNum[i] = matchNumList.get(i).get("match_num").toString();
		}
		return matchNum;
	}

	/**
	 * 删除某场比赛的参赛队伍
	 * 
	 * @param matchNum
	 * @return
	 */
	public Boolean deleteMatch_order(boolean final_preliminary,
			String match_name, int matchNum) {
		boolean mark = false;
		String sql = "delete from match_order where final_preliminary=? and match_name=? and match_num=?";// 根据预赛/决赛和赛事名称查询比赛队伍
		mark = connSql.deleteObject(sql, new Object[] { final_preliminary,
				match_name, matchNum });
		return mark;
	}
}
