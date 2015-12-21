package com.example.casting.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import cn.jpush.android.api.JPushInterface;

import com.example.casting.login.start.StartFlashActivity;
import com.example.casting.util.Session;
import com.example.casting_android.AdActivity;
import com.example.casting_android.R;

public class LoadingForm extends BaseForm{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.main, null);
		setContentView(view);
		Session.getInstance().init(this);
		// 渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
		aa.setDuration(200);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				
					startMain();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		});
	}
	
	private void startMain(){
		if(Session.getInstance().isFirstlogin()){
			startActivity(new Intent(this,StartFlashActivity.class));
		}else{
//			doActivity(R.string.loginform);
			startActivity(new Intent(this,AdActivity.class));
		}
		
		finish();
	}

	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}
	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}
}
