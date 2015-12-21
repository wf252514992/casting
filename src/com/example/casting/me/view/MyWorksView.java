package com.example.casting.me.view;

import java.util.ArrayList;
import java.util.List;

import com.example.casting.entity.WorkBean;
import com.example.casting_android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyWorksView {

	private View workView;
	private ListView lst_myworks;
	@SuppressWarnings("rawtypes")
	ArrayAdapter adapter;
	List<String> mydata = new ArrayList<String>();
	public MyWorksView(Context ctx) {
		initView(ctx);
	}

	public View getView() {
		return workView;
	}

	private void initView(Context ctx) {
//		mydata.add("2014年古装大戏《武媚娘传奇》；饰演武则天，导演高翔俊，合作演员: 张丰毅，李延治");
//		mydata.add("2014年古装大戏《武媚娘传奇》；饰演武则天，导演高翔俊，合作演员: 张丰毅，李延治");
//		mydata.add("2014年古装大戏《武媚娘传奇》；饰演武则天，导演高翔俊，合作演员: 张丰毅，李延治");
//		mydata.add("2014年古装大戏《武媚娘传奇》；饰演武则天，导演高翔俊，合作演员: 张丰毅，李延治");
//		mydata.add("2014年古装大戏《武媚娘传奇》；饰演武则天，导演高翔俊，合作演员: 张丰毅，李延治");
		workView = LayoutInflater.from(ctx).inflate(R.layout.myview_work, null);
		lst_myworks = (ListView)workView.findViewById(R.id.lst_myworks);
		adapter = new ArrayAdapter<String>(ctx, R.layout.myview_workitem, mydata);
		lst_myworks.setAdapter(adapter);
	}
	
	public void updateView(List<WorkBean> works ){
		mydata.clear();
		for(WorkBean work : works){
			StringBuffer sb = new StringBuffer();
			sb.append(work.getRelease_time()+"年");
			sb.append("《"+work.getWorks_name()+"》");
			sb.append("饰演"+work.getRole()+";");
			sb.append("导演"+work.getDirector()+";");
			sb.append("合作演员"+work.getCooperation_actor()+";");
			mydata.add(sb.toString());
		}
		adapter.notifyDataSetChanged();
	}
	
}
