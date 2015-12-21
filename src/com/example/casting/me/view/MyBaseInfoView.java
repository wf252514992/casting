package com.example.casting.me.view;

import com.example.casting.entity.MyCountBean;
import com.example.casting.entity.RegistBean;
import com.example.casting_android.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyBaseInfoView implements OnClickListener {

	ImageView img_info, img_sp, img_sj, img_sex, img_verification;
	LinearLayout fanslayout, dynamiclayout, attentionlayout;
	TextView txt_name, txt_address, txt_age, txt_education, txt_introduce,
			txt_dynamic, txt_attention, txt_fans;
	private View baseinfoview;
	Context mContext;
	OnBaseInfoClick listener;

	public MyBaseInfoView(Context ctx, OnBaseInfoClick clicklistener) {
		mContext = ctx;
		this.listener = clicklistener;
		initView(ctx);
	}

	public View getView() {
		return baseinfoview;
	}

	public void initView(Context ctx) {
		baseinfoview = LayoutInflater.from(ctx).inflate(R.layout.myview_info,
				null);
		fanslayout = (LinearLayout) baseinfoview.findViewById(R.id.fanslayout);
		dynamiclayout = (LinearLayout) baseinfoview
				.findViewById(R.id.dynamiclayout);
		attentionlayout = (LinearLayout) baseinfoview
				.findViewById(R.id.attentionlayout);
		img_info = (ImageView) baseinfoview.findViewById(R.id.img_info);
		img_sp = (ImageView) baseinfoview.findViewById(R.id.img_sp);
		img_sj = (ImageView) baseinfoview.findViewById(R.id.img_sj);
		img_sex = (ImageView) baseinfoview.findViewById(R.id.img_sex);
		img_verification = (ImageView) baseinfoview
				.findViewById(R.id.img_verification);
		txt_name = (TextView) baseinfoview.findViewById(R.id.txt_name);
		txt_address = (TextView) baseinfoview.findViewById(R.id.txt_address);
		txt_age = (TextView) baseinfoview.findViewById(R.id.txt_age);
		txt_education = (TextView) baseinfoview
				.findViewById(R.id.txt_education);
		txt_introduce = (TextView) baseinfoview
				.findViewById(R.id.txt_introduce);
		txt_dynamic = (TextView) baseinfoview.findViewById(R.id.txt_dynamic);
		txt_attention = (TextView) baseinfoview
				.findViewById(R.id.attention);
		txt_fans = (TextView) baseinfoview.findViewById(R.id.txt_fans);
		img_info.setOnClickListener(this);
		img_sp.setOnClickListener(this);
		img_sj.setOnClickListener(this);
		fanslayout.setOnClickListener(this);
		dynamiclayout.setOnClickListener(this);
		attentionlayout.setOnClickListener(this);
	}

	public void updateCount(MyCountBean bean) {
		if (bean == null || baseinfoview == null)
			return;
		txt_attention.setText(bean.getAttention());
		txt_dynamic.setText(bean.getMult());
		txt_fans.setText(bean.getFans());

	}

	public void updateView(RegistBean regist) {
		if (regist == null || baseinfoview == null) {
			return;
		}
		String sex = regist.getSex();
		if (sex.equals("男")) {
			img_sex.setImageResource(R.drawable.mine_sex_boy);
		} else {
			img_sex.setImageResource(R.drawable.mine_sex_girl);
		}
		String verification = regist.getCertification();
		if (verification.length() > 0) {
			img_verification.setVisibility(View.VISIBLE);
			img_verification.setImageResource(R.drawable.mine_special);
		} else {
			img_verification.setVisibility(View.GONE);
		}
		txt_name.setText(regist.getName());
		txt_address.setText(regist.getAddress());
		txt_age.setText(regist.getAge() + "岁");
		txt_introduce.setText("简介:" + regist.getIntroduce());
		txt_education.setText(regist.getEducation());
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.img_info) {

		} else if (R.id.fanslayout == arg0.getId()) {
			listener.onfansLayoutClick();
		} else if (R.id.attentionlayout == arg0.getId()) {
			listener.onAttentionLayoutClick();
		}
		Toast.makeText(mContext, "img_info", 2).show();

	}

	public interface OnBaseInfoClick {
		public void onfansLayoutClick();

		public void onAttentionLayoutClick();

	}
}
