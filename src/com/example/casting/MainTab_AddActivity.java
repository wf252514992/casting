package com.example.casting;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.example.casting.listener.ListenerManager;
import com.example.casting.login.BaseForm;
import com.example.casting_android.R;

public class MainTab_AddActivity extends BaseForm{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_addform);
//		animation();
	}
	
	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub
		
	}
	private void animation(){
    	RelativeLayout menu = (RelativeLayout) findViewById(R.id.menu);
		// 这里是 TranslateAnimation 动画
    	Animation showAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		// 这里是 ScaleAnimation 动画
		// showAction = new ScaleAnimation(
		// 1.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
		// Animation.RELATIVE_TO_SELF, 0.0f);
		showAction.setDuration(500);

		// 这里是 TranslateAnimation 动画
		Animation hideAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
		hideAction.setDuration(500);
    }
}
