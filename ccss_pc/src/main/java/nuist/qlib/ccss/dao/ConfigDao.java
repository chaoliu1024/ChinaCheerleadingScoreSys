/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.dao;

import java.util.HashMap;
import java.util.List;

/**
 * 配置文件(仲裁主任、副裁判长、总裁判长、比赛地点)
 * @author Fang Wang
 * @since ccss 1.0
 */
public class ConfigDao {

	private ConnSQL connSql;

	/** 构造函数，读取数据库的配置，同时与数据库建立连接 */
	public ConfigDao() {
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

	/**
	 * 返回配置信息
	 * 
	 * @return
	 */
	public List<HashMap<String, Object>> getParams() {
		String sql = "select id,name,role,location from config order by id";
		return connSql.selectQuery(sql, new Object[0]);
	}

	/***
	 * 更新配置信息
	 * 
	 * @param data
	 * @return
	 */
	public int updateParams(List<HashMap<String, Object>> data) {
		String sql = "update config set name=?,location=? where id=?";
		return connSql.insertBatch(data, sql, new String[] { "name",
				"location", "id" });
	}

	/***
	 * 获取赛事名称
	 * 
	 * @return
	 */
	public List<HashMap<String, Object>> getMatchName() {
		String sql = "select distinct match_name as matchName from web_json";
		return connSql.selectQuery(sql, new Object[0]);
	}

	/***
	 * 更新赛事名称
	 * 
	 * @param originalName
	 * @param reName
	 * @return 0表示原赛事名称不存在，1表示更新成功
	 */
	public int updateMatchName(String originalName, String reName) {
		String sql = "select distinct match_name as matchName from web_json";
		if (connSql.selectQuery(sql, new Object[0]).size() == 0) {
			return 0;
		} else {
			sql = "update web_json set match_name='" + reName
					+ "' where match_name='" + originalName + "'";
			connSql.updateObject(sql, new Object[0]);
			sql = "update match_order set match_name='" + reName
					+ "' where match_name='" + originalName + "'";
			connSql.updateObject(sql, new Object[0]);
			return 1;
		}
	}

	/***
	 * 更改裁判组别
	 * 
	 * @param chiefCatory
	 */
	public void changeChiefCatory(String chiefCatory) {
		String sql = "update roles set part_in=0 where role_name like '打分裁判%'";
		connSql.updateObject(sql, new Object[0]);

		if (chiefCatory.equals("1")) {
			sql = "update roles set part_in=1 where role_name like '打分裁判1%'";
			connSql.updateObject(sql, new Object[0]);
		} else if (chiefCatory.equals("2")) {
			sql = "update roles set part_in=1 where role_name like '打分裁判2%'";
			connSql.updateObject(sql, new Object[0]);
			sql = "update roles set part_in=1 where role_name like '打分裁判1%'";
			connSql.updateObject(sql, new Object[0]);
		}
	}
}
