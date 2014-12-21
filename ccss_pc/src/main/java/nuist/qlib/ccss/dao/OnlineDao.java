/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.dao;

/**
 * 在角色上线和下线的时候设置角色的状态位
 * 
 * @author ZhengFei Chen
 * @since ccss 1.0
 */
public class OnlineDao {
	// 连接数据库需要的变量
	private ConnSQL connSql;

	public OnlineDao() {
		connSql = new ConnSQL();
	}

	/** 关闭数据库链接 **/
	public void close() {
		connSql.close();
	}

	public boolean updateLoginStatus(String roleName, boolean status) {
		String sql = "update roles set login_flag = ? where role_name = ?";
		int result = connSql.updateObject(sql,
				new Object[] { status, roleName });
		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}
}
