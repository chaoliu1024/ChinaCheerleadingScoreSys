/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.activity;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

/**
 * 管理所有Activity,便于结束程序
 * 
 * @author Chao Liu
 * @since ccss 1.0
 */
public class ManageApplication extends Application {

	private List<Activity> activityList = new LinkedList<Activity>();
	private static ManageApplication instance;

	private ManageApplication() {
	}

	/**
	 * 单例模式中获取唯一的Application实例
	 * 
	 * @return
	 */
	public static ManageApplication getInstance() {
		if (null == instance) {
			instance = new ManageApplication();
		}
		return instance;
	}

	/**
	 * 添加Activity到容器中
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	/**
	 * 遍历所有Activity并finish
	 */
	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}
}