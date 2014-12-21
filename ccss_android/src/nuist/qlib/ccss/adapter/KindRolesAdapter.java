/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.adapter;

import java.util.HashMap;
import java.util.List;

import nuist.qlib.ccss.activity.R;
import nuist.qlib.ccss.util.ViewHolder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * 角色容器
 * 
 * @author Fang Wang
 * @since ccss 1.0
 */
public class KindRolesAdapter extends BaseAdapter {

	private Context context;
	private List<HashMap<String, Object>> list;

	public KindRolesAdapter(Context context, List<HashMap<String, Object>> list) {
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
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.roles_dialog_item, parent, false);
			holder.txt = (TextView) convertView.findViewById(R.id.spinner_txt);
			holder.radio = (RadioButton) convertView
					.findViewById(R.id.spinner_radio);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txt.setText(list.get(position).get("name").toString());
		return convertView;
	}
}
