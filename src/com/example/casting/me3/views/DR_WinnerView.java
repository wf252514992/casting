package com.example.casting.me3.views;

import java.util.ArrayList;
import java.util.List;

import com.example.casting.entity.RegistBean;
import com.example.casting.entity.WinnerBean;
import com.example.casting.login.regist.winner.WinnerActivity;
import com.example.casting.login.regist.winner.WinnerShowActivity;
import com.example.casting.login.regist.winner.adapter.WinnersAdapter;
import com.example.casting_android.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DR_WinnerView implements OnClickListener, OnItemClickListener {
	private View baseinfoview;
	Context mContext;
	TextView txt_add;
	ListView lstinfo;
	boolean isEdit = false;
	WinnersAdapter adapter;
	RegistBean winnerbean;
	ArrayList<WinnerBean> winners = new ArrayList<WinnerBean>();
	LinearLayout layout_more;
	
	public DR_WinnerView(Context ctx, ArrayList<WinnerBean> list, boolean edit) {
		mContext = ctx;
		winners.clear();
		isEdit = edit;
		if (list != null)
			winners.addAll(list);
		adapter = new WinnersAdapter(mContext, winners,false);
		initView(ctx);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.layout_more) {
			if(isEdit){
				Intent intent = new Intent(mContext, WinnerActivity.class);
				Bundle bd = new Bundle();
				bd.putSerializable(R.string.winneractivity + "", winnerbean);
				intent.putExtras(bd);
				mContext.startActivity(intent);
			}else{
				Intent intent = new Intent(mContext, WinnerActivity.class);
				Bundle bd = new Bundle();
				bd.putSerializable(R.string.winneractivity + "", winnerbean);
				intent.putExtras(bd);
				mContext.startActivity(intent);
			}
			
		}
		//
	}

	public View getView() {
		return baseinfoview;
	}

	public void initView(Context ctx) {
		baseinfoview = LayoutInflater.from(ctx).inflate(R.layout.dr_winner,
				null);
		layout_more = (LinearLayout) baseinfoview.findViewById(R.id.layout_more);
		txt_add = (TextView) baseinfoview.findViewById(R.id.txt_add);
		lstinfo = (ListView) baseinfoview.findViewById(R.id.lstinfo);
		lstinfo.setAdapter(adapter);
		if (isEdit) {
			txt_add.setText("  添加获奖经历");
			
		} else {
			txt_add.setText("  查看更多获奖经历");
			txt_add.setCompoundDrawables(null, null,null,null);
		}
		layout_more.setOnClickListener(this);
		lstinfo.setOnItemClickListener(this);
	}

	public void updateView(RegistBean registbean) {
		if(registbean==null)return;
		winnerbean = registbean;
		winners.clear();
		List<WinnerBean> list =registbean.getWinners();
		if (list != null)
			winners.addAll(list);
		adapter.notifyDataSetChanged(winners);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

		if (isEdit) {
			Intent intent = new Intent(mContext, WinnerActivity.class);
			Bundle bd = new Bundle();
			bd.putSerializable(R.string.winneractivity + "", winnerbean);
			intent.putExtras(bd);
			mContext.startActivity(intent);
		}else{
			Intent intent = new Intent(mContext, WinnerShowActivity.class);
			Bundle bd = new Bundle();
			WinnerBean obj = (WinnerBean)arg0.getItemAtPosition(arg2);
			bd.putSerializable(R.string.winnershowactivity + "", obj);
			intent.putExtras(bd);
			mContext.startActivity(intent);
		}
		
		
	}

}
