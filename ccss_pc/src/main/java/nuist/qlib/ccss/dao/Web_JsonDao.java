/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 队伍出场顺序
 * 
 * @author Fang Wang
 * @since ccss 1.0
 */
public class Web_JsonDao {
	// 连接数据库需要的变量
	private ConnSQL connSql = new ConnSQL();

	/**
	 * 查询参赛队伍
	 * @param final_preliminary
	 * @param matchName
	 * @return
	 */
	public List<HashMap<String, Object>> getWeb_Json(Boolean final_preliminary,
			String matchName) {
		String sql = "select id as ID,match_category,match_units,team_name,match_name,final_preliminary,player_name,tb_name from web_json where final_preliminary=? and match_name=? and sort_flag='false'";// 根据预赛/决赛和赛事名称查询比赛队伍
		List<HashMap<String, Object>> Web_JsonList;
		Web_JsonList = connSql.selectQuery(sql, new Object[] {
				final_preliminary, matchName });
		return Web_JsonList;

	}

	/**
	 * 查询未排序的赛事名称或者是已排序的赛事名称
	 * 
	 * @param final_preliminary
	 * @param sort  0表示未排序的赛事名称；1表示已排序的赛事名称
	 * @return
	 */
	public List<HashMap<String, Object>> getMatch_Name(
			Boolean final_preliminary, int sort) {
		List<HashMap<String, Object>> match_nameList = new ArrayList<HashMap<String, Object>>();
		String sql = "";
		if (sort == 0) {
			sql = "select distinct match_name from web_json where final_preliminary=? and sort_flag='false'";// 根据预赛/决赛和赛事名称查询比赛队伍
			match_nameList = connSql.selectQuery(sql,
					new Object[] { final_preliminary });
		} else if (sort == 1) {
			sql = "select distinct match_name from match_order where final_preliminary=? and match_name not in(select distinct a.match_name from match_order a,score b where a.id=b.team_id and a.final_preliminary=?)";// 根据预赛/决赛和赛事名称查询比赛队伍
			match_nameList = connSql.selectQuery(sql, new Object[] {
					final_preliminary, final_preliminary });
		}
		return match_nameList;
	}

	/**
	 * 更新部分队伍是否排序
	 * 
	 * @param match_OrderList
	 * @return
	 */
	public Boolean updateSortFlag(
			List<HashMap<String, Object>> match_OrderList, boolean flag) {
		boolean mark = false;
		String sql = "update web_json set sort_flag='" + flag + "' where id=?";
		if (connSql.insertBatch(match_OrderList, sql, new String[] { "ID" }) > 0)// 插入成功
			mark = true;
		return mark;

	}

	/**
	 * 获取比赛组别
	 * @param final_preliminary
	 * @param matchName
	 * @return
	 */
	public String[] getCategory(Boolean final_preliminary, String matchName) {
		String sql = "select distinct match_category from web_json where final_preliminary=? and match_name=? and sort_flag='false'";// 根据预赛/决赛和赛事名称查询比赛队伍
		List<HashMap<String, Object>> categoryList;
		categoryList = connSql.selectQuery(sql, new Object[] {
				final_preliminary, matchName });
		String category[] = new String[categoryList.size()];
		for (int i = 0; i < category.length; i++) {
			category[i] = categoryList.get(i).get("match_category").toString();
		}
		return category;
	}

	public List<HashMap<String, Object>> getSomeWeb_Json(
			Boolean final_preliminary, String matchName, String category) {
		String sql = "select id as ID,match_units,match_category,match_units,team_name,match_name,final_preliminary,player_name from web_json where final_preliminary=? and match_name=? and match_category=? and sort_flag='false'";// 根据预赛/决赛和赛事名称查询比赛队伍
		List<HashMap<String, Object>> Web_JsonList;
		Web_JsonList = connSql.selectQuery(sql, new Object[] {
				final_preliminary, matchName, category });
		return Web_JsonList;
	}
}
