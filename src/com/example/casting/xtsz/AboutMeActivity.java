package com.example.casting.xtsz;

import android.os.Bundle;
import android.view.View;
import com.example.casting.login.BaseForm;
import com.example.casting_android.R;

public class AboutMeActivity extends BaseForm{
	View titleLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xtsz_aboutme);
		titleLayout =(View) findViewById(R.id.titlelayout);
		initView(titleLayout);
		setLeftButtonAble(true, "返回");
		setTitle("关于我们");
		
	}
	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub
		
	}

}
