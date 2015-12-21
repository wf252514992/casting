package com.example.casting.me.view;

import com.example.casting.me.adapter.MyIntroduceAdapter;
import com.example.casting_android.bean.UserInfoBean;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;

public class MyIntroduceView {

	Context mContext;

	public MyIntroduceView(Context ctx){
		this.mContext = ctx;
	}
	MyIntroduceAdapter adapter;
	public View getView() {
		ListView lstview = new ListView(mContext);
		lstview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		adapter = new MyIntroduceAdapter(mContext, new UserInfoBean());
		lstview.setAdapter(adapter);
		return lstview;
	}

}
