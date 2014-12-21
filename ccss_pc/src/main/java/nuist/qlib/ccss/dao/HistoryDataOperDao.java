/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.dao;

import java.util.HashMap;
import java.util.List;

/**
 * 历史数据处理Dao
 * 
 * @author Fang Wang
 * @since ccss 1.0
 */
public class HistoryDataOperDao {
	// 连接数据库需要的变量
	private ConnSQL connSql;

	/** 构造函数，读取数据库的配置，同时与数据库建立连接 */
	public HistoryDataOperDao() {
		connSql = new ConnSQL();
	}

	/** 判断是否连接到了数据库 */
	public boolean isCollected() {
		return connSql.isConnected();
	}

	public void close() {
		if (connSql.isConnected()) {
			connSql.close();
		}
	}

	/***
	 * 获取所有的已经有成绩的赛事名称
	 * 
	 * @return
	 */
	public List<HashMap<String, Object>> getAllMatchName() {
		List<HashMap<String, Object>> data = connSql
				.selectQuery(
						"select distinct a.match_name as matchName from match_order as a,score as b where a.id=b.team_id",
						new Object[0]);
		return data;
	}

	/***
	 * 获取web_json表中的所有赛事名称
	 * 
	 * @return
	 */
	public List<HashMap<String, Object>> getMatchNameWebJson() {
		List<HashMap<String, Object>> data = connSql.selectQuery(
				"select distinct match_name as matchName from web_json",
				new Object[0]);
		return data;
	}

	/***
	 * 删除有成绩的赛事
	 * 
	 * @param hasScoreMatches
	 *            格式："","",""
	 * @return
	 */
	public boolean deleteMatches(String matches) {
		if (connSql
				.deleteObject(
						"delete from score where team_id in (select id from match_order where match_name in ("
								+ matches + "))", new Object[0])) {
			if (connSql.deleteObject(
					"delete from match_order where match_name in(" + matches
							+ ")", new Object[0])) {
				if (connSql.deleteObject(
						"delete from web_json where match_name in(" + matches
								+ ")", new Object[0])) {
					return true;
				} else
					return false;
			} else
				return false;
		} else
			return false;
	}
}
