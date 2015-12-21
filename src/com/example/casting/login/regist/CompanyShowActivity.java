package com.example.casting.login.regist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import com.example.casting.entity.RegistBean;
import com.example.casting.login.BaseForm;
import com.example.casting.login.regist.views.CompanyRegistView;
import com.example.casting.login.regist.views.UserRegistView;
import com.example.casting_android.R;

/**
 * 15387538103 公司注册测试手机号码
 * 
 * @author Administrator
 * 
 */
public class CompanyShowActivity extends BaseForm {
	View titleLayout;
	LinearLayout layout_content;// 内容布局
	CompanyRegistView companyview;

	// CompanyGetProcessor companygetPro = new CompanyGetProcessor();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.casting_edit_company);
		titleLayout = (View) findViewById(R.id.titlelayout);
		layout_content = (LinearLayout) findViewById(R.id.layout_content);
		initView(titleLayout);
		setTitle("我的资料");
		setLeftButtonAble(true, "返回");

		companyview = new CompanyRegistView(this, null, null, getOperaterType());
		layout_content.addView(companyview.getView());
		RegistBean registbean = initData(getIntent());
		updateView(registbean);
		// RegistBean bean = getBean();
		// HttpCall(companygetPro, bean);
		// HttpCall(false , getworkPro, bean);
		// HttpCall(false , getwinPro, bean);

	}

	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public void RightButtonClick() {
	}

	public void updateView(RegistBean registbean) {
		companyview.updateViewVal(registbean);
	}

	public RegistBean getLocalBean() {
		// TODO Auto-generated method stub
		return companyview.getValues();
	}

	private RegistBean initData(Intent intent) {
		if (intent != null) {
			Bundle bd = intent.getExtras();
			if (bd != null
					&& bd.getSerializable(R.string.companyshowactivity + "") != null) {
				RegistBean bean = (RegistBean) bd
						.getSerializable(R.string.companyshowactivity + "");
				return bean;
			}
		}
		return null;
	}


	public int getOperaterType() {
		// TODO Auto-generated method stub
		return UserRegistView.OperateType_Show;
	}

}
