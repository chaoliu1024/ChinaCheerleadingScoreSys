/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.dao;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 证书打印数据库操作
 * 
 * @author Chao Liu
 * @author Fang Wang
 * @since ccss 1.0
 */
public class PrintCertificateDao {

	private ConnSQL connSql = new ConnSQL();

	/** 判断是否连接到了数据库 */
	public boolean isCollected() {
		return connSql.isConnected();
	}

	/** 关闭数据库链接 **/
	public void close() {
		connSql.close();
	}

	/***
	 * 将需要打印证书的项目插入match_category表中
	 * 
	 * @param category
	 * @param matchName
	 * @param matchType
	 * @return
	 */
	public int insertCerti(String category, String matchName, int matchType) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String sql = "insert match_category(match_category,match_name,final_preliminary,time_tag)values(?,?,?,?)";
		return connSql.insertObject(sql, new Object[] { category, matchName,
				matchType, format.format(new Date()) });
	}
}
