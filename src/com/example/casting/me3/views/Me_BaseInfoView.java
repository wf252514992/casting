package com.example.casting.me3.views;

import com.example.casting.entity.RegistBean;
import com.example.casting.login.regist.CompanyRegistActivity;
import com.example.casting.login.regist.UserInfoShowActivity;
import com.example.casting.util.ConstantData;
import com.example.casting.util.Session;
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
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Me_BaseInfoView implements OnClickListener {
	private View baseinfoview;
	TextView txt_onclick, txt_content;
	Context mContext;
	ImageView layout_backgroud;
	RegistBean registbean;

	public Me_BaseInfoView(Context ctx ) {
		mContext = ctx;
		initView(ctx );
	}

	public View getView() {
		return baseinfoview;
	}
	public ImageView getBackgroundView() {
		return layout_backgroud;
	}
	public void initView(Context ctx ) {
		baseinfoview = LayoutInflater.from(ctx).inflate(R.layout.me_baseinfo,
				null);
		layout_backgroud = (HalfCircleImageView) baseinfoview
				.findViewById(R.id.layout_backgroud);
		Bitmap  mBgBitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.me_bg03b);  
		layout_backgroud.setImageDrawable(new BitmapDrawable(mBgBitmap));
		txt_content = (TextView) baseinfoview.findViewById(R.id.txt_content);
		txt_onclick = (TextView) baseinfoview.findViewById(R.id.txt_onclick);
		layout_backgroud.setOnClickListener(this);
		txt_onclick.setText("个人资料");
		txt_onclick.setBackgroundResource(R.drawable.rect_25_green);
	}

	public void updateView(RegistBean bean) {
		if (baseinfoview == null)
			return;
		registbean = bean;
		String value = "";
		if (bean != null) {
			value = bean.getHometown() + "  " + bean.getAge() + "岁   "
					+ bean.getSex() + "\n" + bean.getEducation() + "\n"
					+ bean.getIntroduce();
		}
		txt_content.setText(value);

	}

	@Override
	public void onClick(View arg0) {
		if (registbean == null)
			return;
		if (registbean.getType().equals(ConstantData.Login_Type_Nomal)) {
			Intent intent = new Intent(mContext, UserInfoShowActivity.class);
			Bundle bd = new Bundle();
			bd.putSerializable(R.string.userinfoshowactivity + "", registbean);
			intent.putExtras(bd);
			mContext.startActivity(intent);
		} else if (registbean.getType()
				.equals(ConstantData.Login_Type_Director)) {
			mContext.startActivity(new Intent(mContext,
					CompanyRegistActivity.class));
		}
	}
}
