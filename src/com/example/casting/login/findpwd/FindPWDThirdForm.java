package com.example.casting.login.findpwd;

import com.example.casting.entity.LoginBean;
import com.example.casting.listener.CloseAllListener;
import com.example.casting.listener.ListenerManager;
import com.example.casting.login.BaseForm;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.processor.login.FindPasswordProcess;
import com.example.casting_android.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class FindPWDThirdForm extends BaseForm implements OnClickListener ,CloseAllListener{
	View titleLayout;
	EditText edt_findpwd_newpwd;
	EditText edt_findpwd_dnewpwd;
	Button btn_findpwd_submit;
	FindPasswordProcess passwordProcess;
	private String phoneNum = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getpwd_third);
		titleLayout =(View) findViewById(R.id.titlelayout);
		edt_findpwd_newpwd =(EditText) findViewById(R.id.edt_findpwd_newpwd);
		edt_findpwd_dnewpwd =(EditText) findViewById(R.id.edt_findpwd_dnewpwd);
		btn_findpwd_submit =(Button) findViewById(R.id.btn_findpwd_submit);
		btn_findpwd_submit.setOnClickListener(this);
		initView(titleLayout);
		setTitle("找回密码");
		setLeftButtonAble(true, "返回");
		Intent intent = getIntent();
		if (intent.getStringExtra("phoneNum") != null) {
			phoneNum = intent.getStringExtra("phoneNum");
		}
		passwordProcess = new FindPasswordProcess();
		ListenerManager.addCloseListener(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ListenerManager.removeCloseListener(this);
	}
	private void findPassword() {
		LoginBean bean = new LoginBean();
		bean.setPass(edt_findpwd_dnewpwd.getText().toString());
		bean.setPhone(phoneNum);
		HttpCall(passwordProcess, bean);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (R.id.btn_findpwd_submit == arg0.getId()) {
			if (checkNull()) {
				findPassword();
			}
		}

	}

	private boolean checkNull() {
		if (edt_findpwd_newpwd.length() == 0) {
			showToast("请输入【密码】");
			return false;
		}
		if (edt_findpwd_dnewpwd.length() == 0) {
			showToast("请输入【确认密码】");
			return false;
		}
		if (!edt_findpwd_dnewpwd.getText().toString()
				.equals(edt_findpwd_newpwd.getText().toString())) {
			showToast("两次的密码输入不一致");
			return false;
		}
		return true;
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

	@Override
	public void OnReturn(String result ,IBaseProcess proid) {
		// TODO Auto-generated method stub
		super.OnReturn(result,proid);
		ResultBean bean = passwordProcess.json2Bean(result);
		if (bean.getCode() == Errors.OK) {
			LoginBean loginbean = new LoginBean();
			loginbean.setPass(edt_findpwd_dnewpwd.getText().toString());
			loginbean.setPhone(phoneNum);
			Bundle bd = new Bundle();
			bd.putSerializable(R.string.findpwdsuccessform+"",loginbean);
			doActivity(R.string.findpwdsuccessform,bd);
		} else {
			showToast(bean.getMessage());
		}
	}

	@Override
	public void closeActivity() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public int getActivityid() {
		// TODO Auto-generated method stub
		return R.string.findpwdthirdform;
	}
}
