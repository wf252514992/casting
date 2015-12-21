package com.example.casting.me3.views;

import com.example.casting.entity.RegistBean;
import com.example.casting.entity.VideoBean;
import com.example.casting.me.MeImgs;
import com.example.casting.me.MyVideoActvity;
import com.example.casting.processor.video.GetListVideoProcessor;
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

public class Me_VideosView implements OnClickListener {
	private View baseinfoview;
	TextView txt_onclick, txt_content;
	Context mContext;
	RegistBean registbean ;
	ImageView layout_backgroud ;
	public Me_VideosView(Context ctx) {
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
		baseinfoview = LayoutInflater.from(ctx).inflate(R.layout.me_videos,
				null);
		layout_backgroud = (HalfCircleImageView) baseinfoview.findViewById(R.id.layout_backgroud);
		Bitmap  mBgBitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.me_bg06b);  
		layout_backgroud.setImageDrawable(new BitmapDrawable(mBgBitmap));
		txt_content = (TextView) baseinfoview.findViewById(R.id.txt_content);
		txt_onclick = (TextView) baseinfoview.findViewById(R.id.txt_onclick);
		layout_backgroud.setOnClickListener(this);
		txt_onclick.setText("我的视频");
		txt_onclick.setBackgroundResource(R.drawable.rect_25_blue_light);
	}

	public void updateView( RegistBean bean) {
		if (  baseinfoview == null)
			return;
		registbean = bean;
	}

	@Override
	public void onClick(View arg0) {
//		Toast.makeText(mContext, "Me_BaseInfoView",0).show();
		if(registbean==null){
			return;
		}
		Intent intent = new Intent(mContext,MyVideoActvity.class);
		Bundle bd = new Bundle();
		bd.putSerializable(R.string.myvideoactvity+"", registbean);
		intent.putExtras(bd);
		mContext.startActivity(intent);
	}
}
