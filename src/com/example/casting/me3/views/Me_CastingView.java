package com.example.casting.me3.views;

import java.util.ArrayList;
import java.util.List;

import com.example.casting.entity.MyCountBean;
import com.example.casting.entity.RecruitBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.login.regist.works.WorksActivity;
import com.example.casting.mailbox.EntrantsActivity;
import com.example.casting.util.view.HalfCircleImageView;
import com.example.casting_android.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Me_CastingView implements OnClickListener {
	private View baseinfoview;
	TextView txt_onclick, txt_content;
	Context mContext;
	ImageView layout_backgroud;
	RegistBean rgstbean;
	List<RecruitBean> myCastings = new ArrayList<RecruitBean>();

	public Me_CastingView(Context ctx) {
		mContext = ctx;
		initView(ctx);
	}

	public View getView() {
		return baseinfoview;
	}

	public ImageView getBackgroundView() {
		return layout_backgroud;
	}

	public void initView(Context ctx) {
		baseinfoview = LayoutInflater.from(ctx).inflate(R.layout.me_baseinfo,
				null);
		layout_backgroud = (HalfCircleImageView) baseinfoview
				.findViewById(R.id.layout_backgroud);
		Bitmap mBgBitmap = BitmapFactory.decodeResource(ctx.getResources(),
				R.drawable.me_bg02b);
		layout_backgroud.setImageDrawable(new BitmapDrawable(mBgBitmap));
		txt_content = (TextView) baseinfoview.findViewById(R.id.txt_content);
		txt_onclick = (TextView) baseinfoview.findViewById(R.id.txt_onclick);
		layout_backgroud.setOnClickListener(this);
		txt_onclick.setText("我的试镜");
		txt_onclick.setBackgroundResource(R.drawable.rect_25_orange);
	}

	public void updateView(RegistBean bean) {
		if (baseinfoview == null)
			return;
		rgstbean = bean;
		List<RecruitBean> list = bean.getRecrutbeans();
		myCastings.clear();
		myCastings.addAll(list);
		String value = "";
		for (int i = 0; i < myCastings.size(); i++) {
			RecruitBean rt = myCastings.get(i);
			if (i == 3) {
				value += "...";
				break;
			}
			value += rt.getTitle();
		}
		txt_content.setText(value);

	}

	@Override
	public void onClick(View arg0) {// 报名者
		Bundle bd = new Bundle();
		bd.putSerializable(EntrantsActivity.class.getName(), rgstbean);
		Intent intent = new Intent(mContext, EntrantsActivity.class);
		intent.putExtra("title", "我的试镜");
		intent.putExtras(bd);
		mContext.startActivity(intent);
	}
}
