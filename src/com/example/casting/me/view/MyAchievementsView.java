package com.example.casting.me.view;

import java.util.ArrayList;
import java.util.List;
import com.example.casting.entity.WinnerBean;
import com.example.casting_android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyAchievementsView {

	private View Achievementview;

	private ListView lst_myworks;
	@SuppressWarnings("rawtypes")
	ArrayAdapter adapter;
	List<String> mydata = new ArrayList<String>();
	public MyAchievementsView(Context ctx) {
		initView(ctx);
	}

	public View getView() {
		return Achievementview;
	}

	private void initView(Context ctx) {
//		mydata.add("2014获得百花奖最佳女主角获奖作品《武媚娘传奇》");
//		mydata.add("2014获得百花奖最佳女主角获奖作品《武媚娘传奇》");
//		mydata.add("2014获得百花奖最佳女主角获奖作品《武媚娘传奇》");
//		mydata.add("2014获得百花奖最佳女主角获奖作品《武媚娘传奇》");
//		mydata.add("2014获得百花奖最佳女主角获奖作品《武媚娘传奇》");
		Achievementview = LayoutInflater.from(ctx).inflate(R.layout.myview_achievements, null);
		lst_myworks = (ListView)Achievementview.findViewById(R.id.lst_myworks);
		adapter = new ArrayAdapter<String>(ctx, R.layout.myview_workitem, mydata);
		lst_myworks.setAdapter(adapter);
	}
	
	public void updateView(List<WinnerBean> winners ){
		mydata.clear();
		for(WinnerBean winner : winners){
			StringBuffer sb = new StringBuffer();
			sb.append(winner.getWinners_time()+"年");
			sb.append("获得"+winner.getWinners_name()+"》");
			sb.append("获奖作品《"+winner.getWinners_works_name()+"》");
			mydata.add(sb.toString());
		}
		adapter.notifyDataSetChanged();
	}
	
	
}
