/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.manager.score;

import nuist.qlib.ccss.dao.PrintCertificateDao;

/**
 * 设置打印证书
 * 
 * @author Fang Wang
 * @since ccss 1.0
 */
public class PrintCertificate {

	private PrintCertificateDao dao;

	public PrintCertificate() {
		dao = new PrintCertificateDao();
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
		return dao.insertCerti(category, matchName, matchType);
	}

	/***
	 * 判断是否成功的连接了数据库
	 * 
	 * @return
	 */
	public boolean isCollected() {
		return dao.isCollected();
	}

	public void close() {
		dao.close();
	}
}
