package com.example.casting.me3.views;

import java.util.ArrayList;
import java.util.List;

import com.example.casting.entity.RegistBean;
import com.example.casting.entity.WorkBean;
import com.example.casting.login.regist.works.WorksActivity;
import com.example.casting.login.regist.works.WorksShowActivity;
import com.example.casting.login.regist.works.adapter.WorksAdapter;
import com.example.casting_android.R;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DR_WorksView implements OnClickListener, OnItemClickListener {
	private View baseinfoview;
	Context mContext;
	TextView txt_add;
	ListView lstinfo;
	LinearLayout layout_more;

	RegistBean rgstbean ;
	WorksAdapter adapter;
	boolean isEdit = false;
	ArrayList<WorkBean> works = new ArrayList<WorkBean>();

	public DR_WorksView(Context ctx, ArrayList<WorkBean> list, boolean edit) {
		mContext = ctx;
		works.clear();
		this.isEdit = edit;
		if (list != null)
			works.addAll(list);
		adapter = new WorksAdapter(mContext, works, false);
		initView(ctx);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.layout_more) {
			if (isEdit) {
				Intent intent = new Intent(mContext, WorksActivity.class);
				Bundle bd = new Bundle();
				bd.putSerializable(R.string.worksactivity + "", rgstbean);
				intent.putExtras(bd);
				mContext.startActivity(intent);
			} else {
				Intent intent = new Intent(mContext, WorksActivity.class);
				Bundle bd = new Bundle();
				bd.putSerializable(R.string.worksactivity + "", rgstbean);
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
		layout_more = (LinearLayout) baseinfoview
				.findViewById(R.id.layout_more);
		txt_add = (TextView) baseinfoview.findViewById(R.id.txt_add);
		lstinfo = (ListView) baseinfoview.findViewById(R.id.lstinfo);
		lstinfo.setAdapter(adapter);
		if (isEdit) {
			txt_add.setText("  添加个人作品");
		} else {
			txt_add.setText("  查看更多个人作品");
			txt_add.setCompoundDrawables(null, null, null, null);
		}
		layout_more.setOnClickListener(this);
		lstinfo.setOnItemClickListener(this);

	}

	public void updateView(RegistBean registbean) {
		if (registbean == null)
			return;
		rgstbean= registbean;
		works.clear();
		List<WorkBean> list = registbean.getWorks();
		if (list != null)
			works.addAll(list);
		adapter.notifyDataSetChanged(works);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (isEdit) {
			Intent intent = new Intent(mContext, WorksActivity.class);
			Bundle bd = new Bundle();
			bd.putSerializable(R.string.worksactivity + "", rgstbean);
			intent.putExtras(bd);
			mContext.startActivity(intent);
		} else {
			WorkBean obj = (WorkBean) arg0.getItemAtPosition(arg2);
			Intent intent = new Intent(mContext, WorksShowActivity.class);
			Bundle bd = new Bundle();
			bd.putSerializable(R.string.worksshowactivity + "", obj);
			intent.putExtras(bd);
			mContext.startActivity(intent);
		}

	}

}
