package com.example.casting.login;

import com.example.casting.util.ConstantData;
import com.example.casting.util.Session;
import com.example.casting_android.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class CastingFirstForm extends BaseForm {
	LinearLayout btn_recruit;
	LinearLayout btn_berecruit;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.casting_firstform);
		btn_berecruit =(LinearLayout) findViewById(R.id.layout_beizhaomu);
		btn_recruit =(LinearLayout) findViewById(R.id.layout_zhaomu);
		btn_recruit.setOnClickListener(listener);
		btn_berecruit.setOnClickListener(listener);
	}
	
	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if( R.id.layout_beizhaomu == arg0.getId()){
				Session.getInstance().setUsertype( ConstantData.Login_Type_Nomal+"");
				doActivity(R.string.userinfoactivity,setRegist);
			}else{
				Session.getInstance().setUsertype( ConstantData.Login_Type_Director+"");
				doActivity(R.string.companyregistactivity,setRegist);
			}
//			doActivity(R.string.companyregistactivity,setRegist);
		}
	};

	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub
		
	}
	
	private static final int setRegist =10001;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(setRegist == requestCode ){
			finish();
		}
		
	}
}
