/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nuist.qlib.ccss.activity.R;
import nuist.qlib.ccss.ui.HScrollView;
import nuist.qlib.ccss.ui.HScrollView.OnScrollChangedListener;
import nuist.qlib.ccss.util.ScoreViewHolder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 成绩容器
 * 
 * @author Fang Wang
 * @since ccss 1.0
 */
public class ScoreQueryAdapter extends BaseAdapter {

	public List<ScoreViewHolder> mHolderList = new ArrayList<ScoreViewHolder>();
	private List<HashMap<String, Object>> data;
	int id_row_layout;
	LayoutInflater mInflater;
	int matchKind;
	private Context context;
	private RelativeLayout mHead;

	public ScoreQueryAdapter(Context context, int id_row_layout,
			List<HashMap<String, Object>> data, int matchKind,
			RelativeLayout mHead) {
		super();
		this.id_row_layout = id_row_layout;
		mInflater = LayoutInflater.from(context);
		this.data = data;
		this.matchKind = matchKind;
		this.context = context;
		this.mHead = mHead;
	}

	public List<HashMap<String, Object>> getData() {
		return data;
	}

	public void setData(List<HashMap<String, Object>> data) {
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parentView) {
		ScoreViewHolder holder = null;

		HashMap<String, Object> one = data.get(position);
		if (convertView == null) {
			synchronized (context) {
				convertView = mInflater.inflate(id_row_layout, null);
				holder = new ScoreViewHolder();

				HScrollView scrollView1 = (HScrollView) convertView
						.findViewById(R.id.horizontalScrollView1);

				holder.scrollView = scrollView1;
				holder.txt1 = (TextView) convertView
						.findViewById(R.id.roleTextView);
				holder.txt11 = (TextView) convertView
						.findViewById(R.id.textView11);
				holder.score1 = (TextView) convertView
						.findViewById(R.id.score1);
				holder.score2 = (TextView) convertView
						.findViewById(R.id.score2);
				holder.score3 = (TextView) convertView
						.findViewById(R.id.score3);
				holder.score4 = (TextView) convertView
						.findViewById(R.id.score4);
				holder.score5 = (TextView) convertView
						.findViewById(R.id.score5);
				holder.score6 = (TextView) convertView
						.findViewById(R.id.score6);
				holder.score7 = (TextView) convertView
						.findViewById(R.id.score7);
				holder.score8 = (TextView) convertView
						.findViewById(R.id.score8);
				holder.score9 = (TextView) convertView
						.findViewById(R.id.score9);
				if (matchKind == 1) {
					holder.score10 = (TextView) convertView
							.findViewById(R.id.score10);
				}
				holder.sub_score = (TextView) convertView
						.findViewById(R.id.sub_score);
				holder.total = (TextView) convertView.findViewById(R.id.total);

				HScrollView headSrcrollView = (HScrollView) mHead
						.findViewById(R.id.horizontalScrollView1);
				headSrcrollView
						.AddOnScrollChangedListener(new OnScrollChangedListenerImp(
								scrollView1));

				convertView.setTag(holder);
				mHolderList.add(holder);
			}
		} else {
			holder = (ScoreViewHolder) convertView.getTag();
		}
		holder.txt1.setText(one.get("bank").toString());
		holder.txt11.setText(one.get("unit").toString());
		holder.score1.setText(one.get("score1").toString());
		holder.score2.setText(one.get("score2").toString());
		holder.score3.setText(one.get("score3").toString());
		holder.score4.setText(one.get("score4").toString());
		holder.score5.setText(one.get("score5").toString());
		holder.score6.setText(one.get("score6").toString());
		holder.score7.setText(one.get("score7").toString());
		holder.score8.setText(one.get("score8").toString());
		holder.score9.setText(one.get("score9").toString());
		if (matchKind == 1) {
			holder.score10.setText(one.get("score10").toString());
		}
		holder.sub_score.setText(one.get("sub_score").toString());
		holder.total.setText(one.get("total").toString());

		return convertView;
	}

	class OnScrollChangedListenerImp implements OnScrollChangedListener {
		HScrollView mScrollViewArg;

		public OnScrollChangedListenerImp(HScrollView scrollViewar) {
			mScrollViewArg = scrollViewar;
		}

		@Override
		public void onScrollChanged(int l, int t, int oldl, int oldt) {
			mScrollViewArg.smoothScrollTo(l, t);
		}
	};
}
