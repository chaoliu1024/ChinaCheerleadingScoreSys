/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.util.score;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 计算成绩
 * 
 * @author Chao Liu
 * @since ccss 1.0
 */
public class CalcScore {
	private float chief_referee_sub = 0.0f;// 裁判长减分

	/**
	 * 计算总得分，去除最高分最低分减去裁判长的减分
	 * 
	 * @return totalScore:总得分数
	 */
	public String calcTotalScore(String referee1_score, String referee2_score,
			String referee3_score, String referee4_score,
			String referee5_score, String referee6_score,
			String referee7_score, String referee8_score,
			String referee9_score, String referee10_score,
			String chief_referee_sub_score) {
		float totalScore;
		List<Float> scores = new ArrayList<Float>();
		if (referee1_score != null && !referee1_score.equals("")) {
			scores.add(Float.parseFloat(referee1_score));
		}
		if (referee2_score != null && !referee2_score.equals("")) {
			scores.add(Float.parseFloat(referee2_score));
		}
		if (referee3_score != null && !referee3_score.equals("")) {
			scores.add(Float.parseFloat(referee3_score));
		}
		if (referee4_score != null && !referee4_score.equals("")) {
			scores.add(Float.parseFloat(referee4_score));
		}
		if (referee5_score != null && !referee5_score.equals("")) {
			scores.add(Float.parseFloat(referee5_score));
		}
		if (referee6_score != null && !referee6_score.equals("")) {
			scores.add(Float.parseFloat(referee6_score));
		}
		if (referee7_score != null && !referee7_score.equals("")) {
			scores.add(Float.parseFloat(referee7_score));
		}
		if (referee8_score != null && !referee8_score.equals("")) {
			scores.add(Float.parseFloat(referee8_score));
		}
		if (referee9_score != null && !referee9_score.equals("")) {
			scores.add(Float.parseFloat(referee9_score));
		}
		if (referee10_score != null && !referee10_score.equals("")) {
			scores.add(Float.parseFloat(referee10_score));
		}
		if (chief_referee_sub_score != null
				&& !chief_referee_sub_score.equals("")) {
			chief_referee_sub = Float.parseFloat(chief_referee_sub_score);
		}
		totalScore = getTotalScore(scores) - chief_referee_sub;
		String strScore = new DecimalFormat("#0.00").format(totalScore); // 保留两位小数
		return strScore;
	}

	/**
	 * 获取分数的误差
	 * 
	 * @return
	 */
	public List<Float> getDeviations(String referee1_score,
			String referee2_score, String referee3_score,
			String referee4_score, String referee5_score,
			String referee6_score, String referee7_score,
			String referee8_score, String referee9_score, String referee10_score) {
		List<Float> results = new ArrayList<Float>();
		List<Float> scores = new ArrayList<Float>();
		float avri; // 平均值
		if (referee1_score != null && !referee1_score.equals("")) {
			scores.add(Float.parseFloat(referee1_score));
		}
		if (referee2_score != null && !referee2_score.equals("")) {
			scores.add(Float.parseFloat(referee2_score));
		}
		if (referee3_score != null && !referee3_score.equals("")) {
			scores.add(Float.parseFloat(referee3_score));
		}
		if (referee4_score != null && !referee4_score.equals("")) {
			scores.add(Float.parseFloat(referee4_score));
		}
		if (referee5_score != null && !referee5_score.equals("")) {
			scores.add(Float.parseFloat(referee5_score));
		}
		if (referee6_score != null && !referee6_score.equals("")) {
			scores.add(Float.parseFloat(referee6_score));
		}
		if (referee7_score != null && !referee7_score.equals("")) {
			scores.add(Float.parseFloat(referee7_score));
		}
		if (referee8_score != null && !referee8_score.equals("")) {
			scores.add(Float.parseFloat(referee8_score));
		}
		if (referee9_score != null && !referee9_score.equals("")) {
			scores.add(Float.parseFloat(referee9_score));
		}
		if (referee10_score != null && !referee10_score.equals("")) {
			scores.add(Float.parseFloat(referee10_score));
		}
		avri = getTotal(scores) / scores.size();
		for (int i = 0; i < scores.size(); i++) {
			results.add(Math.abs(scores.get(i) / avri - 1));
		}
		return results;
	}

	/***
	 * 去除最高分与最低分并算剩下分的和
	 * 
	 * @return
	 */
	public float getTotalScore(List<Float> scores) {
		float result = 0;
		int min = 0;
		int max = 0;
		for (int i = 1; i < scores.size(); i++) {
			if (scores.get(min) > scores.get(i)) {
				min = i;
			}
		}
		scores.remove(min);
		for (int i = 1; i < scores.size(); i++) {
			if (scores.get(max) < scores.get(i)) {
				max = i;
			}
		}
		scores.remove(max);

		for (int i = 0; i < scores.size(); i++) {
			result += scores.get(i);
		}
		return result;
	}

	/***
	 * 算出所有分数的总分
	 * 
	 * @param scores
	 * @return
	 */
	public float getTotal(List<Float> scores) {
		float result = 0;
		for (int i = 0; i < scores.size(); i++) {
			result += scores.get(i);
		}
		return result;
	}
}
