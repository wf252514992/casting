package com.example.casting.me3.views;

import java.util.ArrayList;
import java.util.List;

import com.example.casting.entity.MyCountBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.entity.WinnerBean;
import com.example.casting.entity.WorkBean;
import com.example.casting.login.regist.winner.WinnerActivity;
import com.example.casting.login.regist.works.WorksActivity;
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

public class Me_WinnersView implements OnClickListener {
	private View baseinfoview;
	TextView txt_onclick, txt_content;
	Context mContext;
	RegistBean winnerbean;
	ImageView layout_backgroud ;
	public ImageView getBackgroundView() {
		return layout_backgroud;
	}
	boolean isEdit = false;
	ArrayList<WinnerBean> winnerlist = new ArrayList<WinnerBean>();
	public Me_WinnersView(Context ctx,boolean edit) {
		mContext = ctx;
		this.isEdit = edit;
		initView(ctx);
	}

	public View getView() {
		return baseinfoview;
	}

	public void initView(Context ctx) {
		baseinfoview = LayoutInflater.from(ctx).inflate(R.layout.me_baseinfo,
				null);
		layout_backgroud = (HalfCircleImageView) baseinfoview.findViewById(R.id.layout_backgroud);
		Bitmap  mBgBitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.me_bg05b);  
		layout_backgroud.setImageDrawable(new BitmapDrawable(mBgBitmap));
		txt_content = (TextView) baseinfoview.findViewById(R.id.txt_content);
		txt_onclick = (TextView) baseinfoview.findViewById(R.id.txt_onclick);
		layout_backgroud.setOnClickListener(this);
		txt_onclick.setText("获奖经历");
		txt_onclick.setBackgroundResource(R.drawable.rect_25_purple);
	}

	public void updateView(RegistBean rgbean ) {
		if(rgbean==null)return;
		winnerbean = rgbean;
		List<WinnerBean> winners = winnerbean.getWinners();
		winnerlist.clear();
		winnerlist.addAll(winners);
		if (  baseinfoview == null)
			return;
		String text = "";
		for(int i = 0 ; i<winnerlist.size() ;i++){
			if(i==3){
				text +="...";
				break;
			}
			WinnerBean bean = winnerlist.get(i);
			if(bean!=null){
				text += bean.getWinners_name()+"  《"+ bean.getWinners_works_name()+"》";
				if(i<winners.size()-1){
					text += "\n";
				}
			}
		}
		txt_content.setText(text);

	}

	@Override
	public void onClick(View arg0) {
		if(isEdit){
			Bundle bd = new Bundle();
			bd.putSerializable(R.string.winneractivity+"",winnerbean);
			Intent intent = new Intent(mContext,WinnerActivity.class);
			intent.putExtras(bd);
			mContext.startActivity(intent);
		}else{
			Bundle bd = new Bundle();
			bd.putSerializable(R.string.winneractivity+"",winnerbean);
			Intent intent = new Intent(mContext,WinnerActivity.class);
			intent.putExtras(bd);
			mContext.startActivity(intent);
		}
		
	}
}
