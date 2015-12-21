package com.example.casting.me3.views;


import com.example.casting.MainTab;
import com.example.casting.entity.RegistBean;
import com.example.casting.login.regist.CompanyShowActivity;
import com.example.casting.login.regist.DirectorShowActivity;
import com.example.casting.me.MyVideoActvity;
import com.example.casting.util.ConstantData;
import com.example.casting_android.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class DR_ContactView implements OnClickListener {
	private View baseinfoview;
	Context mContext;
	TextView txt_info, txt_video, txt_casting ;
	RegistBean registbean;
	public DR_ContactView(Context ctx ,RegistBean bean) {
		mContext = ctx;
		this.registbean = bean;
		initView(ctx);
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(registbean == null)return;
		if(arg0.getId() == R.id.txt_info){
			if(registbean.getType().equals(ConstantData.Login_Type_Director)){
				Intent intent = new Intent(mContext,DirectorShowActivity.class);
				Bundle bd = new Bundle();
				bd.putSerializable(R.string.directorshowactivity+"", registbean);
				intent.putExtras(bd);
				mContext.startActivity(intent);
			}else if(registbean.getType().equals(ConstantData.Login_Type_Company)){
				Intent intent = new Intent(mContext,CompanyShowActivity.class);
				Bundle bd = new Bundle();
				bd.putSerializable(R.string.companyshowactivity+"", registbean);
				intent.putExtras(bd);
				mContext.startActivity(intent);
			}
			
		}else if(arg0.getId() == R.id.txt_casting){
			Bundle bd = new Bundle();
			bd.putSerializable(MainTab.class.getName(), registbean);
			bd.putString("title", "报名者");
			Intent intent=new Intent(mContext,MainTab.class);
			intent.putExtra("action", "zhaomu");
			intent.putExtras(bd);
			mContext.startActivity(intent);
			
//			Intent intent=new Intent(mContext,EntrantsActivity.class);
//			mContext.startActivity(intent);
		}else if(arg0.getId() == R.id.txt_video){
//			mContext.startActivity(new Intent(mContext, director.class));
			Intent intent = new Intent(mContext,MyVideoActvity.class);
			Bundle bd = new Bundle();
			bd.putSerializable(R.string.myvideoactvity+"", registbean);
			intent.putExtras(bd);
			mContext.startActivity(intent);
		}
//		
	}

	public View getView() {
		return baseinfoview;
	}

	public void updateView(RegistBean bean){
		this.registbean = bean;
	}
	public void initView(Context ctx) {
		baseinfoview = LayoutInflater.from(ctx).inflate(
				R.layout.dr_contact, null);
		txt_info = (TextView) baseinfoview.findViewById(R.id.txt_info);
		txt_video = (TextView) baseinfoview.findViewById(R.id.txt_video);
		txt_casting = (TextView) baseinfoview.findViewById(R.id.txt_casting);
		txt_info.setOnClickListener(this);
		txt_video.setOnClickListener(this);
		txt_casting.setOnClickListener(this);
	}


}
