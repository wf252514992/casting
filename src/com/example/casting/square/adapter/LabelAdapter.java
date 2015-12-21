package com.example.casting.square.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.casting.entity.ConditionBean;
import com.example.casting_android.R;

public class LabelAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	ArrayList<ConditionBean> labellist = new ArrayList<ConditionBean>();
	Context mContext;
	public LabelAdapter(Context ctx, ArrayList<ConditionBean> value) {
		// TODO Auto-generated constructor stub
		mContext = ctx;
		labellist.clear();
		if(value!=null)
			labellist.addAll(value);
		this.mInflater = LayoutInflater.from(ctx);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return labellist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return labellist.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public void notifyDataSetChanged(ArrayList<ConditionBean> list) {
		labellist.clear();
		if(list!=null)
			labellist.addAll(list);
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
//		TextView txtview ;
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.simple_listitem, null);
			holder.text = (TextView) convertView.findViewById(R.id.text);
//			txtview = new TextView(mContext);
//			txtview.setTextSize(16);
//			txtview.setPadding(20, 15, 0,15);
			convertView.setTag(holder);
			
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		ConditionBean bean = labellist.get(arg0);
		holder.text.setText(bean.getLabel());
		return convertView;
	}
	
	public class ViewHolder
	{
		TextView text;
	}
}
