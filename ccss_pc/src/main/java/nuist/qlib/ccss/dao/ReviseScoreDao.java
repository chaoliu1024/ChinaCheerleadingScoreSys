/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.dao;

import java.util.HashMap;
import java.util.List;

/**
 * 修订成绩
 * 
 * @author Chao Liu
 * @since ccss 1.0
 */
public class ReviseScoreDao {

	private ConnSQL connSql = new ConnSQL();

	/** 判断是否连接到了数据库 */
	public boolean isCollected() {
		return connSql.isConnected();
	}

	/** 关闭数据库链接 **/
	public void close() {
		connSql.close();
	}

	/** 将技巧排名更正的数据库写进数据库 */
	public Boolean updateReviseSkillAll(List<HashMap<String, Object>> data) {
		String sql = "update score set score1=?,score_error1=?,score2=?,score_error2=?,score3=?,score_error3=?,score4=?,score_error4=?,score5=?,score_error5=?,score6=?,score_error6=?,score7=?,score_error7=?,score8=?,score_error8=?,score9=?,score_error9=?,score10=?,score_error10=?,referee_sub_score=?,referee_add_score=?,total_score=? where id=?";
		if (connSql.insertBatch(data, sql, new String[] { "score1",
				"score_error1", "score2", "score_error2", "score3",
				"score_error3", "score4", "score_error4", "score5",
				"score_error5", "score6", "score_error6", "score7",
				"score_error7", "score8", "score_error8", "score9",
				"score_error9", "score10", "score_error10", "sub_score",
				"add_score", "total", "scoreId" }) > 0) {
			return true;
		} else
			return false;

	}

	/** 将排名更正的数据库写进数据库 */
	public Boolean updateReviseDanceAll(List<HashMap<String, Object>> data) {
		String sql = "update score set score1=?,score_error1=?,score2=?,score_error2=?,score3=?,score_error3=?,score4=?,score_error4=?,score5=?,score_error5=?,score6=?,score_error6=?,score7=?,score_error7=?,score8=?,score_error8=?,score9=?,score_error9=?,referee_sub_score=?,referee_add_score=?,total_score=? where id=?";
		if (connSql.insertBatch(data, sql, new String[] { "score1",
				"score_error1", "score2", "score_error2", "score3",
				"score_error3", "score4", "score_error4", "score5",
				"score_error5", "score6", "score_error6", "score7",
				"score_error7", "score8", "score_error8", "score9",
				"score_error9", "sub_score", "add_score", "total", "scoreId" }) > 0) {
			return true;
		} else
			return false;
	}
}
