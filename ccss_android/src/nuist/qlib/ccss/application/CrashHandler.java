/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.application;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

/**
 * 自定义未处理异常的异常处理
 * 
 * @author Zhengfei Chen
 * @since ccss 1.0
 */
public class CrashHandler implements UncaughtExceptionHandler {

	public static final String TAG = "CrashHandler";

	// 系统默认的UncaughtException处理类
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	// CrashHandler实例
	private static CrashHandler INSTANCE;
	// 程序的Context对象
	private Context mContext;

	/**
	 * 获取CrashHandler实例 ,单例模式
	 * 
	 * @return
	 */
	public static CrashHandler getInstance(Context context) {
		if (INSTANCE == null) {
			INSTANCE = new CrashHandler(context);
			return INSTANCE;
		} else {
			return INSTANCE;
		}
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public CrashHandler(Context context) {
		mContext = context;
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			// 退出程序
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成
	 * 
	 * @param ex
	 * @return
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG)
						.show();
				Looper.loop();
			}
		}.start();
		return true;
	}
}
