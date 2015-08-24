/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import nuist.qlib.ccss.db.SpringDBAction;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 控制逻辑
 * 
 * @author Fang Wang
 * @since ccss 1.0
 */
public class FaceController {
	@Resource
	private SpringDBAction springDBAction;
	
	private Logger logger = Logger.getLogger(FaceController.class.getName());

	@RequestMapping(value = { "/hello" }, method = { RequestMethod.POST })
	public String check(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("test");
		return null;
	}

	/***
	 * 获得所有的角色
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = { "/allRoles" }, method = { RequestMethod.POST })
	@ResponseBody
	public String allRoles(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String roleDes = request.getParameter("roleDes");
		String sql = "select role_name as name,role_value as value from roles where role_name like '%"
				+ roleDes + "%' and part_in=1";
		List<HashMap<String, Object>> data = springDBAction.query(sql,
				new Object[0]);
		try {
			response.getWriter().write(JSONArray.fromObject(data).toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * 身份校验
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = { "/roleCheck" }, method = { RequestMethod.POST })
	@ResponseBody
	public String roleCheck(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String role = request.getParameter("role");
		String sql = "select login_flag as state from roles where role_value=?";
		List<HashMap<String, Object>> data = springDBAction.query(sql,
				new Object[] { role });
		String result = "";
		if (data.get(0).get("state").toString().equals("true")) {
			result = "false"; // 该身份已经有人登陆过
		} else {
			if (this.springDBAction.update(
					"update roles set login_flag=? where role_value=?",
					new Object[] { 1, role }) > 0) {
				result = "true"; // 成功登陆
			} else
				result = "fail"; // 登陆失败
		}
		try {
			logger.debug("登陆结果：" + result);
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * 退出登陆
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = { "/loginOut" }, method = { RequestMethod.POST })
	@ResponseBody
	public String loginOut(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String result;
		String role = request.getParameter("role");
		if (this.springDBAction.update(
				"update roles set login_flag=? where role_value=?",
				new Object[] { 0, role }) > 0) {
			result = "true"; // 成功退出
		} else
			result = "fail"; // 退出失败
		try {
			logger.debug("退出登陆：" + result + ":" + role);
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * 获取外国比赛成绩的排名
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getForeScoreRankData", method = RequestMethod.POST)
	@ResponseBody
	public String getForeScoreRankData(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String matchName = request.getParameter("matchName");
		int matchType = Integer.valueOf(request.getParameter("matchType"));
		String matchCategory = request.getParameter("matchCategory");
		String sql = "select b.id as id,b.team_name as unit,a.score1 as score1,a.score2 as score2,a.score3 as score3,a.score4 as score4,a.score5 as score5,a.score6 as score6,a.score7 as score7,a.score8 as score8,a.score9 as score9,a.score10 as score10,a.referee_sub_score as sub_score,a.total_score as total,a.id as scoreId,b.match_category as category from score as a,match_order as b where a.team_id=b.id and b.match_category=? and b.match_name=? and b.final_preliminary=? "
				+ "order by a.total_score desc,dbo.getTotalScore(score1,score2,score3,score4,score5,score6,score7,score8,score9,score10,referee_sub_score) desc";
		List<HashMap<String, Object>> data = springDBAction.query(sql,
				new Object[] { matchCategory, matchName, matchType });
		int i = 1;
		HashMap<String, Object> one = null;
		String score = "";
		if (data.size() != 0) {
			score = data.get(0).get("total").toString();
			one = data.get(0);
			one.put("rank", i);
		}
		for (int j = 1; j < data.size(); j++) {
			one = data.get(j);
			if (one.get("total").toString().equals(score)) {
				one.put("rank", i);
				i++;
			} else {
				i++;
				score = one.get("total").toString();
				one.put("rank", i);
			}
		}
		try {
			response.getWriter().write(JSONArray.fromObject(data).toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * 按照外国比赛名称以及赛事模式取出其中所有的项目
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = { "/getForeCatoryData" }, method = { RequestMethod.POST })
	@ResponseBody
	public String getForeCatoryData(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String matchName = request.getParameter("matchName");
		int matchType = Integer.valueOf(request.getParameter("matchType"));
		List<HashMap<String, Object>> data = springDBAction
				.query("select distinct a.match_category as category from match_order a,score b where a.id=b.team_id and a.match_name=? and a.final_preliminary=?",
						new Object[] { matchName, matchType });
		try {
			response.getWriter().write(JSONArray.fromObject(data).toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * 获得该赛事模式下所有已经比过的赛事名称
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = { "/getForeLaLaScoreMatchNames" }, method = { RequestMethod.POST })
	@ResponseBody
	public String getForeLaLaScoreMatchNames(HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject one = null;
		JSONArray array = new JSONArray();
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		int matchType = Integer.valueOf(request.getParameter("matchType"));
		String sql = "select distinct b.match_name as name from score a,match_order b where a.team_id=b.id and b.final_preliminary=?";

		List<HashMap<String, Object>> data = springDBAction.query(sql,
				new Object[] { matchType });
		List<HashMap<String, Object>> catorys = null;
		JSONObject oneMap = null;
		sql = "select distinct b.match_category as category from score a,match_order b where a.team_id=b.id and b.final_preliminary=? and b.match_name=?";
		if (data.size() != 0) {
			for (int i = 0; i < data.size(); i++) {
				catorys = this.springDBAction.query(sql, new Object[] {
						matchType, data.get(i).get("name") });
				oneMap = new JSONObject();
				oneMap.put("matchName", data.get(i).get("name"));
				oneMap.put("category", catorys.get(0).get("category"));
				array.add(oneMap);
			}
		}
		one = new JSONObject();
		one.put("matchNames", JSONArray.fromObject(data));
		one.put("categorys", array);
		try {
			System.out.println(one.toString());
			response.getWriter().write(one.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
