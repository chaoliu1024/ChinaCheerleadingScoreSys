/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.config;

import java.util.HashMap;
import java.util.List;

import nuist.qlib.ccss.dao.ConfigDao;
import nuist.qlib.ccss.util.file.PropertiesManager;

/**
 * 配置文件(仲裁主任、副裁判长、总裁判长、比赛地点)
 * 
 * @author Fang Wang
 * @since ccss 1.0
 */
public class Config {
	private ConfigDao dao;
	private PropertiesManager manager;

	public Config() {
		dao = new ConfigDao();
		manager = new PropertiesManager();
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

	/**
	 * 返回裁判长的配置信息
	 * 
	 * @return
	 */
	public List<HashMap<String, Object>> getParams() {
		return dao.getParams();
	}

	/***
	 * 更新裁判长的配置信息
	 * 
	 * @param data
	 * @return
	 */
	public int updateParams(List<HashMap<String, Object>> data) {
		return dao.updateParams(data);
	}

	/***
	 * 更改组别
	 * 
	 * @param chiefCatory
	 */
	public void changeChiefCatory(String chiefCatory) {
		dao.changeChiefCatory(chiefCatory);
	}

	/***
	 * 返回参数
	 * 
	 * @return 第一个字符串为下载路径，第二个字符串为临时目录路径
	 */
	public String[] getProParams() {
		return manager.readProperties();
	}

	/***
	 * 更新下载路径和临时目录路径
	 * 
	 * @param dowmloadUrl
	 * @param tempDir
	 */
	public void updateProParams(String downloadUrl, String tempDir,
			String uploadScoreUrl) {
		manager.updateProperties(downloadUrl, tempDir, uploadScoreUrl);
	}

	/***
	 * 更新裁判组别
	 * 
	 * @param chiefCatory
	 */
	public void updateChiefCatory(String chiefCatory) {
		manager.updateProperties(chiefCatory);
	}

	/***
	 * 获得所有的赛事名称
	 * 
	 * @return
	 */
	public String[] getMatchNames() {
		List<HashMap<String, Object>> data = dao.getMatchName();
		String[] matchNames = new String[data.size()];
		for (int i = 0; i < data.size(); i++) {
			matchNames[i] = data.get(i).get("matchName").toString();
		}
		return matchNames;
	}

	/***
	 * 更新赛事名称
	 * 
	 * @param originalName
	 * @param reName
	 * @return 0表示原赛事名称不存在，1表示更新成功
	 */
	public int updateMatchName(String originalName, String reName) {
		return dao.updateMatchName(originalName, reName);
	}
}
