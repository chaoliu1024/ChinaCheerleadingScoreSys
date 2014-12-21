/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nuist.qlib.ccss.adapter.ScoreQueryAdapter;
import nuist.qlib.ccss.dao.ScoreDAO;
import nuist.qlib.ccss.ui.ProgressBackDialog;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 分数查询页面
 * 
 * @author Fang Wang
 * @since ccss 1.0
 */
public class ScoreOrderWeb extends Activity {
	private ListView mListView1;
	private ScoreQueryAdapter myAdapter;
	private RelativeLayout mHead;
	// 赛事名称
	private String matchName = "";
	// 比赛项目
	private String matchCategory = "";
	// 判断是决赛还是预赛
	private int matchType = 0;
	private Spinner catoryView;
	private List<HashMap<String, Object>> data;
	private ArrayAdapter<String> adapter;
	private List<String> catoryData;
	private Button query;
	private MyHandler handler;
	private Runnable mBackgroundRunnable;
	private HandlerThread thread;
	// 用在线程中用
	private int mark = 0;
	private String error;
	private ProgressDialog dialog;
	// 线程次数
	private int i = -1;
	private List<Handler> handlers = new ArrayList<Handler>();
	// 用来区分
	private int matchKind = -1;
	// 更新界面中的内容
	private Handler uiHandler;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = this.getIntent();
		matchName = intent.getStringExtra("matchName");
		matchCategory = intent.getStringExtra("matchCategory");
		matchType = intent.getIntExtra("matchType", -1);

		this.setContentView(R.layout.activity_score_order_main);
		if (matchCategory.lastIndexOf("技巧") != -1) {
			matchKind = 1;
			this.findViewById(R.id.skill_head).setVisibility(View.VISIBLE);
			this.findViewById(R.id.dance_head).setVisibility(View.GONE);
		} else {
			matchKind = 0;
			this.findViewById(R.id.skill_head).setVisibility(View.GONE);
			this.findViewById(R.id.dance_head).setVisibility(View.VISIBLE);
		}

		TextView matchNameView = (TextView) this.findViewById(R.id.matchName);
		matchNameView.setText(matchName);
		catoryView = (Spinner) this.findViewById(R.id.category);
		query = (Button) this.findViewById(R.id.query);

		if (matchKind == 0) {
			mHead = (RelativeLayout) findViewById(R.id.dance_head);
		} else if (matchKind == 1) {
			mHead = (RelativeLayout) findViewById(R.id.skill_head);
		}
		mHead.setFocusable(true);
		mHead.setClickable(true);
		mHead.setBackgroundColor(Color.parseColor("#b2d235"));
		mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

		mListView1 = (ListView) findViewById(R.id.listView1);
		mListView1.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		data = new ArrayList<HashMap<String, Object>>();
		if (matchKind == 0) {
			myAdapter = new ScoreQueryAdapter(this, R.layout.dance_item, data,
					matchKind, mHead);
		} else if (matchKind == 1)
			myAdapter = new ScoreQueryAdapter(this, R.layout.item, data,
					matchKind, mHead);
		RelativeLayout.LayoutParams pas = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		pas.addRule(RelativeLayout.BELOW, mHead.getId());
		mListView1.setLayoutParams(pas);
		mListView1.setAdapter(myAdapter);

		dialog = new ProgressBackDialog(this).getProgressDialog("请稍候...",
				"正在加载中...");
		dialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				handler.setStop(true);
			}
		});
		dialog.show();

		catoryView.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				matchCategory = (String) ((TextView) arg1).getText();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		thread = new HandlerThread("post" + i);
		thread.start();
		i++;

		mBackgroundRunnable = new Runnable() { // 初始化数据
			@Override
			public void run() {
				int index = i;
				if (mark == 0) {
					catoryView_DB(index);
					create_scoreRank_data(index);
				} else if (mark == 1) {
					create_scoreRank_data(index);
				}
			}

		};
		handler = new MyHandler(thread.getLooper());
		handlers.add(handler);
		handler.post(mBackgroundRunnable);

		uiHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0: // 更新项目
					catoryView.setAdapter(adapter);
					int index = catoryData.indexOf(matchCategory);
					if (index != -1) {
						catoryView.setSelection(index);
					}
					break;
				case 1:
					if (matchKind == 0) {
						mHead = (RelativeLayout) findViewById(R.id.dance_head);
						findViewById(R.id.skill_head).setVisibility(View.GONE);
						mHead.setVisibility(View.VISIBLE);
					} else if (matchKind == 1) {
						mHead = (RelativeLayout) findViewById(R.id.skill_head);
						findViewById(R.id.dance_head).setVisibility(View.GONE);
						mHead.setVisibility(View.VISIBLE);
					}
					mHead.setFocusable(true);
					mHead.setClickable(true);
					mHead.setBackgroundColor(Color.parseColor("#b2d235"));
					mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
					RelativeLayout.LayoutParams pas = new RelativeLayout.LayoutParams(
							ViewGroup.LayoutParams.WRAP_CONTENT,
							ViewGroup.LayoutParams.WRAP_CONTENT);
					pas.addRule(RelativeLayout.BELOW, mHead.getId());
					mListView1.setLayoutParams(pas);
					mListView1.setAdapter(myAdapter);
					mListView1.invalidate();
					mListView1.forceLayout();
				}
			}

		};

		query.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mark = 1;
				dialog = new ProgressBackDialog(ScoreOrderWeb.this)
						.getProgressDialog("请稍候...", "正在加载中...");
				dialog.setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						handler.setStop(true);
					}
				});
				dialog.show();
				handler.removeCallbacks(mBackgroundRunnable);

				thread = new HandlerThread("post" + i);
				thread.start();
				i++;
				mBackgroundRunnable = new Runnable() {
					@Override
					public void run() {
						int index = i;
						create_scoreRank_data(index);
					}
				};
				handler = new MyHandler(thread.getLooper());
				handlers.add(handler);
				handler.post(mBackgroundRunnable);
			}

		});
	}

	/**
	 * 获取赛事项目
	 * 
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public void catoryView_DB(int index) {
		ScoreDAO dao = new ScoreDAO(ScoreOrderWeb.this.getFilesDir());
		if (dao.checkConfig()) {
			HashMap<String, Object> result = dao.getCatoryData(matchName,
					matchType);
			if (result.get("message").equals("success")) {
				catoryData = (List<String>) result.get("data");
				handlers.get(index).sendEmptyMessage(2);
			} else {
				error = (String) result.get("message");
				handlers.get(index).sendEmptyMessage(1);
			}
		} else {
			handlers.get(index).sendEmptyMessage(0);
		}
	}

	/**
	 * 获取排名根据赛事项目
	 */
	@SuppressWarnings("unchecked")
	public void create_scoreRank_data(int index) {
		ScoreDAO dao = new ScoreDAO(ScoreOrderWeb.this.getFilesDir());
		if (dao.checkConfig()) {
			HashMap<String, Object> result = dao.getScoreRankData(matchName,
					matchCategory, matchType);
			if (result.get("message").equals("success")) {
				data = (List<HashMap<String, Object>>) result.get("data");
				handlers.get(index).sendEmptyMessage(3);
			} else {
				error = (String) result.get("message");
				handlers.get(index).sendEmptyMessage(1);
			}
		} else {
			handlers.get(index).sendEmptyMessage(0);
		}
	}

	private class MyHandler extends Handler {
		private boolean isStop = false;

		public void setStop(boolean isStop) {
			this.isStop = isStop;
		}

		public MyHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			if (isStop)
				return;
			switch (msg.what) {
			case 0: {
				dialog.dismiss();
				Toast.makeText(ScoreOrderWeb.this, "请检查网络是否畅通！",
						Toast.LENGTH_LONG).show();
				break;
			}
			case 1: {
				dialog.dismiss();
				Toast.makeText(ScoreOrderWeb.this, error, Toast.LENGTH_LONG)
						.show();
				break;
			}
			case 2: {
				dialog.dismiss();
				adapter = new ArrayAdapter(ScoreOrderWeb.this,
						android.R.layout.simple_dropdown_item_1line, catoryData);
				uiHandler.sendEmptyMessage(0);
				break;
			}
			case 3: {
				dialog.dismiss();
				if (matchCategory.lastIndexOf("技巧") != -1) {
					matchKind = 1;
					myAdapter = new ScoreQueryAdapter(ScoreOrderWeb.this,
							R.layout.item, data, matchKind, mHead);
				} else {
					matchKind = 0;
					myAdapter = new ScoreQueryAdapter(ScoreOrderWeb.this,
							R.layout.dance_item, data, matchKind, mHead);
				}
				uiHandler.sendEmptyMessage(1);
			}
			}
		}
	}

	/**
	 * 菜单、返回键响应
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@SuppressLint("ClickableViewAccessibility") 
	class ListViewAndHeadViewTouchLinstener implements View.OnTouchListener {

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// 当在列头 和 listView控件上touch时，将这个touch的事件分发给 ScrollView
			HorizontalScrollView headSrcrollView = (HorizontalScrollView) mHead
					.findViewById(R.id.horizontalScrollView1);
			headSrcrollView.onTouchEvent(arg1);
			return false;
		}
	}
}
