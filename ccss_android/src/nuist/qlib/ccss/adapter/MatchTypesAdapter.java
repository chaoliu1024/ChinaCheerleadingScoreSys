/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.adapter;

import java.util.HashMap;
import java.util.List;

import nuist.qlib.ccss.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 裁判长界面
 * 
 * @author Fang Wang
 * @since ccss 1.0
 */
public class MatchTypesAdapter extends BaseAdapter {

	private Context context;
	private List<HashMap<String, Object>> list;

	public MatchTypesAdapter(Context context, List<HashMap<String, Object>> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.match_type_item, parent, false);
		}
		((TextView) convertView.findViewById(R.id.matchType)).setText(list
				.get(position).get("name").toString());
		return convertView;
	}
}
