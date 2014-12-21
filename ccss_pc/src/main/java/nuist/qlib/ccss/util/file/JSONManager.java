/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.util.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import nuist.qlib.ccss.dao.JSONOperDao;

/**
 * 与JSON相关的操作，如读写JSON文件
 * 
 * @author Fang Wang
 * @since ccss 1.0
 */
public class JSONManager {
	private File file; // JSON文件
	private String matchName;
	private JSONOperDao dao;

	public JSONManager() {
		this.dao = new JSONOperDao();
	}

	public boolean isCollected() {
		return dao.isCollected();
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	/***
	 * 读JSON文件
	 * 
	 * @return
	 */
	public JSONArray readJSONFile() {
		JSONArray array = new JSONArray();
		BufferedReader reader = null;
		String result = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			String lineString = null;
			while ((lineString = reader.readLine()) != null) {
				result += lineString;
			}
			reader.close();
			if (!result.equals("")) {
				JSONObject object = JSONObject.fromObject(result);
				array = object.getJSONArray("data");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return array;
	}

	/***
	 * 将JSON数据入库
	 * 
	 * @param array
	 * @return
	 */
	public String JSONInput(JSONArray array) {
		JSONObject object = null;
		JSONArray subArray = null;
		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> one = null;
		boolean checkMark = false;

		for (int i = 0; i < array.size(); i++) {
			object = array.getJSONObject(i);
			subArray = object.getJSONArray("items");
			JSONObject subObject = null;
			for (int j = 0; j < subArray.size(); j++) {
				subObject = subArray.getJSONObject(j);
				int webId = subObject.getInt("id");
				String category = subObject.getString("subject");
				String teamName = subObject.getString("mingcheng");
				matchName = subObject.getString("sjname")
						+ subObject.getString("sqname");
				if (!checkMark) {
					String checkResult = dao.checkHasData(matchName);
					if (checkResult.equals("1")) {
						return "1:" + matchName + "赛事已经有成绩，请删除相关数据再导入数据!";
					} else if (checkResult.equals("2")) {
						return "2:" + matchName + "赛事已经有排好的队伍!";
					} else if (checkResult.equals("3")) {
						return "3:" + matchName + "赛事已经入库!";
					} else if (checkResult.equals("4")) {
						checkMark = true;
					} else {
						return "5:导入失败!"; // 发生异常错误
					}
				}
				String unit = subObject.getString("danwei");

				JSONArray members = subObject.containsKey("member") ? subObject
						.getJSONArray("member") : subObject
						.getJSONArray("members");
				String playerMember = "";
				for (int k = 0; k < members.size(); k++) {
					Object name = members.getJSONObject(k).getString("name");
					if (name != null && name.toString().trim().length() != 0) {
						playerMember += name.toString() + ",";
					}
				}
				if (!playerMember.equals("")) {
					playerMember = playerMember.substring(0,
							playerMember.lastIndexOf(","));
				}

				JSONArray coachs = subObject.getJSONArray("jlmember");
				String coachsMember = "";
				for (int k = 0; k < coachs.size(); k++) {
					Object name = coachs.getJSONObject(k).getString("name");
					if (name != null && name.toString().trim().length() != 0) {
						coachsMember += name.toString() + ",";
					}
				}
				if (!coachsMember.equals("")) {
					coachsMember = coachsMember.substring(0,
							coachsMember.lastIndexOf(","));
				}

				JSONArray tuNames = subObject.getJSONArray("hbmember");
				String tuNamesString = "";
				for (int k = 0; k < tuNames.size(); k++) {
					Object name = tuNames.getJSONObject(k).getString("name");
					if (name != null && name.toString().trim().length() != 0) {
						tuNamesString += name.toString() + ",";
					}
				}
				if (!tuNamesString.equals("")) {
					tuNamesString = tuNamesString.substring(0,
							tuNamesString.lastIndexOf(","));
				} else {
					tuNamesString = null;
				}
				one = new HashMap<String, Object>();
				one.put("webId", webId);
				one.put("category", category);
				one.put("units", unit);
				one.put("teamName", teamName);
				one.put("preliminary", 0);
				one.put("matchName", matchName);
				one.put("sortFlag", 0);
				one.put("members", playerMember);
				one.put("coachs", coachsMember);
				one.put("tuNames", tuNamesString);
				data.add(one);
			}
		}
		if (dao.insertBatchJson(data))
			return "ok:";
		else
			return "5:导入失败!"; // 发生异常错误
	}

	/***
	 * 按照赛事名称 将web_json和match_order中的数据删除
	 * 
	 * @return
	 */
	public boolean deleteOrder_Web() {
		if (dao.deleteMatchOrder(matchName) && dao.deleteWebJSON(matchName)) {
			return true;
		} else
			return false;
	}

	/***
	 * 按照赛事名称将web_json中的数据删除
	 * 
	 * @return
	 */
	public boolean deleteWebJSON() {
		if (dao.deleteWebJSON(matchName)) {
			return true;
		} else
			return false;
	}

	/***
	 * 
	 * @param data
	 * @return -1表示程序错误；0表示上传成功；2表示上传失败；3表示无返回信息
	 */
	public String sendDataToWeb(List<HashMap<String, Object>> data,
			String matchName) {
		JSONArray array = new JSONArray();
		JSONObject object = new JSONObject();
		JSONObject one = null;
		for (int i = 0; i < data.size(); i++) {
			one = new JSONObject();
			one.put("webId", data.get(i).get("webId") == null ? "" : data
					.get(i).get("webId"));
			one.put("totalScore", data.get(i).get("totalScore") == null ? ""
					: data.get(i).get("totalScore"));
			one.put("rank", data.get(i).get("rank") == null ? "" : data.get(i)
					.get("rank"));
			array.add(one);
		}
		object.put("matchName", matchName);
		object.put("data", array);
		try {
			String result = new String(object.toString().getBytes(), "utf-8");
			PropertiesManager manager = new PropertiesManager();
			String url = manager.readProperties()[3];
			String params = "code=7FA950F349A2C539CBCA82AAD65A795B&score="
					+ URLEncoder.encode(result, "utf-8");
			return uploadWeb(url, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1 + ":" + e.getMessage();
		}
	}

	/***
	 * 将成绩上传到web
	 * 
	 * @param url
	 * @param params
	 * @return -1表示程序错误；0表示上传成功；2表示上传失败；3表示无返回信息
	 */
	public String uploadWeb(String url, String params) {
		StringBuffer responseMessage = null;
		URLConnection connection = null;
		URL reqUrl = null;
		OutputStreamWriter reqOut = null;
		InputStream in = null;
		BufferedReader br = null;
		JSONObject object;

		try {
			responseMessage = new StringBuffer();
			reqUrl = new URL(url);
			connection = reqUrl.openConnection();
			connection.setDoOutput(true);
			reqOut = new OutputStreamWriter(connection.getOutputStream());
			reqOut.write(params);
			reqOut.flush();
			in = connection.getInputStream();

			br = new BufferedReader(new InputStreamReader(in, "utf-8"));
			String lineString = null;
			while ((lineString = br.readLine()) != null) {
				responseMessage.append(lineString);
			}
			br.close();
			in.close();
			reqOut.close();
			if (!responseMessage.equals("")) {
				object = JSONObject.fromObject(responseMessage.toString());
				boolean success = object.getBoolean("success");
				if (success) {
					return 0 + ":推送成功";
				} else {
					return 2 + ":推送失败";
				}
			}
			return 3 + ":网站无响应";
		} catch (Exception e) {
			e.printStackTrace();
			return -1 + ":" + e.getMessage();
		}
	}

	/***
	 * 关闭数据库连接
	 */
	public void close() {
		if (dao.isCollected()) {
			dao.close();
		}
	}
}
