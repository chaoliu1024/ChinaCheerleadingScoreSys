/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.dao;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Fang Wang
 * @since ccss 1.0
 */
public class JSONOperDao {

	// 连接数据库需要的变量
	private ConnSQL connSql;
	private static Logger logger = LoggerFactory.getLogger(JSONOperDao.class);

	/** 构造函数，读取数据库的配置，同时与数据库建立连接 */
	public JSONOperDao() {
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
	 * 判断是否在未排序列表中已有了该赛事数据
	 * 
	 * @param matchName
	 *            赛事名称
	 * @return
	 */
	public String checkHasData(String matchName) {
		String checkWebJSOnSql = "select * from web_json where match_name=?";
		String checkMatchOrder = "select * from match_order where match_name=?";
		String checkScore = "select b.* from match_order as a,score as b where a.id=b.team_id and a.match_name=?";
		boolean webJSOnSqlMark = false;
		boolean matchOrderMark = false;
		boolean scoreMark = false;
		List<HashMap<String, Object>> data;
		try {
			data = connSql.selectQuery(checkWebJSOnSql,
					new Object[] { matchName });
			if (data.size() != 0) { // web_json中有了该赛事的数据
				webJSOnSqlMark = true;
				data = connSql.selectQuery(checkMatchOrder,
						new Object[] { matchName });
				if (data.size() != 0) {
					matchOrderMark = true;
					data = connSql.selectQuery(checkScore,
							new Object[] { matchName });
					if (data.size() != 0) {
						scoreMark = true;
					}
				}
			}
			if (scoreMark) {
				return "1"; // 分数表中已经有了数据不能够重新导入JSON数据
			} else if (matchOrderMark) { // match_order表中有该赛事数据，但是分数表中没有
				return "2";
			} else if (webJSOnSqlMark) { // web_json表中有该赛事数据，但是match_order表中没有
				return "3";
			} else
				return "4"; // 可以直接导入数据
		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
			return e.toString();
		}
	}

	/***
	 * 根据赛事名称删除web_json表示中的记录
	 * 
	 * @param matchName
	 * @return
	 */
	public boolean deleteWebJSON(String matchName) {
		String sql = "delete from web_json where match_name=?";
		return connSql.deleteObject(sql, new Object[] { matchName });
	}

	/***
	 * 删除match_order中的数据
	 * 
	 * @param matchName
	 * @return
	 */
	public boolean deleteMatchOrder(String matchName) {
		String sql = "delete from match_order where match_name=?";
		return connSql.deleteObject(sql, new Object[] { matchName });
	}

	/***
	 * 批量将JSON数据导入到web_JOSN表中
	 * 
	 * @param data
	 * @return
	 */
	public boolean insertBatchJson(List<HashMap<String, Object>> data) {
		boolean mark = false;
		String sql = "insert into web_json(web_id,match_category,match_units,team_name,match_name,final_preliminary,sort_flag,player_name,coach_name,tb_name)values(?,?,?,?,?,?,?,?,?,?)";
		if (connSql.insertBatch(data, sql, new String[] { "webId", "category",
				"units", "teamName", "matchName", "preliminary", "sortFlag",
				"members", "coachs", "tuNames" }) > 0)
			mark = true;
		return mark;
	}
}
