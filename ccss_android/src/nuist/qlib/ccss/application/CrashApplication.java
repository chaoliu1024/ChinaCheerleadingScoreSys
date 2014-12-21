/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.application;

import android.app.Application;

/**
 * 继承Application,在出现未处理的异常时，调用自定义的全局异常
 * 
 * @author Zhengfei Chen
 * @since ccss 1.0
 */
public class CrashApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		CrashHandler crashHandler = CrashHandler
				.getInstance(getApplicationContext());
	}
}
