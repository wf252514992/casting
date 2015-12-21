package com.example.casting;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.casting_android.R;

public class companydetailitem1 extends Fragment {


	private static companydetailitem1 sInstance;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.companydetail_item1_list, container, false);
	}
	private void init(){
			//listview
			ListView listview=(ListView) getView().findViewById(R.id.companydetaillist);
			ArrayList<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("title","昵称");
			map.put("content","副影视公司");
			list.add(map);
			map = new HashMap<String, Object>();
			map.put("title","所在地");
			map.put("content","湖南长沙");
			list.add(map);
			map = new HashMap<String, Object>();
			map.put("title","简介");
			map.put("content","哈哈");
			list.add(map);
			map = new HashMap<String, Object>();
			map.put("title","公司电话");
			map.put("content","400-106-2365");
			list.add(map);
			MessageAdapter adapter=new MessageAdapter(getActivity(), list);
			listview.setAdapter(adapter);
			
	}
	public static companydetailitem1 getInstance() {
		if (null == sInstance) {
			synchronized (companydetailitem1.class) {
				if (sInstance == null) {
					sInstance = new companydetailitem1();
				}
			}
		}
		return sInstance;
	}
	class MessageAdapter extends BaseAdapter{
		Context context;
		ArrayList<HashMap<String,Object>> arrayList;

		public MessageAdapter(Context context,ArrayList<HashMap<String,Object>> arrayList){
			super();
			this.context=context;
			this.arrayList=arrayList;
		
		}
		@Override
		public int getCount() {
			return arrayList.size();
		}

		@Override
		public Object getItem(int position) {
			return arrayList.get(position);
		}

		@Override
		public long getItemId(int span) {
			return span;
		}

		@Override
		public View getView(int position, View v, ViewGroup group) {
			LayoutInflater inflater=LayoutInflater.from(context);
			v=inflater.inflate(R.layout.companydetail_item1_list_item, null);
			
			TextView title=(TextView) v.findViewById(R.id.title);
			TextView content=(TextView) v.findViewById(R.id.content);
			
			title.setText(arrayList.get(position).get("title").toString());
			content.setText(arrayList.get(position).get("content").toString());
		
			return v;
		}

	}
}
