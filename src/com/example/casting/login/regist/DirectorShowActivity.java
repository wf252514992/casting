package com.example.casting.login.regist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import com.example.casting.entity.RegistBean;
import com.example.casting.login.BaseForm;
import com.example.casting.login.regist.views.DirectorRegistView;
import com.example.casting.login.regist.views.UserRegistView;
import com.example.casting_android.R;

public class DirectorShowActivity extends BaseForm {

	View titleLayout;
	LinearLayout layout_content;// 内容布局
	DirectorRegistView directioview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.casting_edit_company);
		titleLayout = (View) findViewById(R.id.titlelayout);
		layout_content = (LinearLayout) findViewById(R.id.layout_content);
		initView(titleLayout);
		setTitle("我的资料");
		setLeftButtonAble(true, "返回");

		directioview = new DirectorRegistView(this, null, null,
				getOperaterType());
		layout_content.addView(directioview.getView());
		RegistBean registbean = initData(getIntent());
		updateView(registbean);

	}

	private RegistBean initData(Intent intent) {
		if (intent != null) {
			Bundle bd = intent.getExtras();
			if (bd != null
					&& bd.getSerializable(R.string.directorshowactivity + "") != null) {
				RegistBean bean = (RegistBean) bd
						.getSerializable(R.string.directorshowactivity + "");
				return bean;
			}
		}
		return null;
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

	private void updateView(RegistBean registbean) {
		directioview.updateViewVal(registbean);
	}


	public int getOperaterType() {
		// TODO Auto-generated method stub
		return UserRegistView.OperateType_Show;
	}
}
