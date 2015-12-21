package com.example.casting.me.adapter;

import java.util.LinkedHashMap;
import java.util.Map;

import com.example.casting_android.R;
import com.example.casting_android.bean.UserInfoBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyIntroduceAdapter extends BaseAdapter {
	Object[] keys = null;
	Map<String, String> vals = new LinkedHashMap<String, String>();
	Context mContext;

	public MyIntroduceAdapter(Context context, UserInfoBean bean) {
		super();
		mContext = context;
		vals = bean.toHashMap();
		keys = vals.keySet().toArray();

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return keys.length;
	}

	@Override
	public Object getItem(int position) {
		return keys[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder = null;
		if (arg1 == null) {
			holder = new ViewHolder();
			arg1 = LayoutInflater.from(mContext).inflate(R.layout.mydataitem,
					null);
			holder.txt_key = (TextView)arg1.findViewById(R.id.txt_key);
			holder.txt_val = (TextView)arg1.findViewById(R.id.txt_val);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}
		String key = keys[arg0].toString();
		String value = vals.get(key);
		holder.txt_key.setText(key );
		holder.txt_val.setText(  value);
		return arg1;
	}

	final class ViewHolder {
		public TextView txt_key;
		public TextView txt_val;
	}

}
