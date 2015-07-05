/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.util.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * properties文件的处理
 * 
 * @author Fang Wang
 * @since ccss 1.0
 */
public class PropertiesManager {
	private String fileName; // 要操作的文件
	private Properties pro;

	private Logger logger = LoggerFactory.getLogger(PropertiesManager.class);

	public PropertiesManager() {
		// this.fileName = "/downLoadConfig.properties";
		this.fileName = "downLoadConfig.properties";
		pro = new Properties();
	}

	/***
	 * 读取文件
	 * 
	 * @return 返回的数组中，第一个字符串为下载文件地址，第二个字符串为临时文档目录
	 */
	public String[] readProperties() {
		String[] result = new String[4];
		try {
			// InputStream in =
			// PropertiesManager.class.getResourceAsStream(fileName);
			FileInputStream in = new FileInputStream(fileName);
			pro.load(in);
			result[0] = pro.getProperty("downloadUrl", "");
			result[1] = pro.getProperty("tempDir", "");
			result[2] = pro.getProperty("chiefCategory", "");
			result[3] = pro.getProperty("uploadScoreUrl", "");
			in.close();
		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return result;
	}

	/***
	 * 更新文件
	 * 
	 * @param downloadUrl
	 *            下载队伍序列网络地址
	 * @param tempSir
	 *            放置临时文件的目录
	 * @param uploadScoreUrl
	 *            上传成绩地址
	 */
	public void updateProperties(String downloadUrl, String tempSir,
			String uploadScoreUrl) {
		try {
			FileInputStream in = new FileInputStream(fileName);
			pro.load(in);
			in.close();
			if (downloadUrl.trim().length() != 0) {
				pro.setProperty("downloadUrl", downloadUrl);
			}
			if (tempSir.trim().length() != 0) {
				pro.setProperty("tempDir", tempSir);
			}
			if (uploadScoreUrl.trim().length() != 0) {
				pro.setProperty("uploadScoreUrl", uploadScoreUrl);
			}
			FileOutputStream out = new FileOutputStream(fileName);
			pro.store(out, "update");
			out.close();
		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
	}

	/***
	 * 更新文件
	 * 
	 * @param chiefCategory
	 *            设置裁判组别
	 */
	public void updateProperties(String chiefCategory) {
		try {
			FileInputStream in = new FileInputStream(fileName);
			pro.load(in);
			in.close();
			if (chiefCategory.trim().length() != 0) {
				pro.setProperty("chiefCategory", chiefCategory);
			}
			FileOutputStream out = new FileOutputStream(fileName);
			pro.store(out, "update");
			out.close();
		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
	}
}
