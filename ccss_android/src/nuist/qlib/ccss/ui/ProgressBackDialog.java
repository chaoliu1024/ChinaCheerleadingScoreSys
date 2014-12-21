/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.ui;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * 点击back可以取消的进度条
 * 
 * @author Fang Wang
 * @since ccss 1.0
 */
public class ProgressBackDialog {
	private Context context;

	public ProgressBackDialog(Context context) {
		this.context = context;
	}

	public ProgressDialog getProgressDialog(String title, String msg) {
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setTitle(title);
		progressDialog.setIndeterminate(true);
		progressDialog.setMessage(msg);
		progressDialog.setCancelable(true);
		return progressDialog;
	}
}
