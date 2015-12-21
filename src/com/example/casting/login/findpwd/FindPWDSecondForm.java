package com.example.casting.login.findpwd;

import static cn.smssdk.framework.utils.R.getStringRes;

import java.util.HashMap;


import com.example.casting.listener.CloseAllListener;
import com.example.casting.listener.ListenerManager;
import com.example.casting.login.RquestMessageBaseDialog;
import com.example.casting_android.R;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FindPWDSecondForm extends RquestMessageBaseDialog implements
		OnClickListener,CloseAllListener {

	EditText edt_inputchecknum;
	Button btn_checknum_submmit;
	String phoneNum = "";
	public FindPWDSecondForm(String phone){
		phoneNum = phone ;
	}
	@Override
	public void show(Context arg0) {
		// TODO Auto-generated method stub
		super.show(arg0);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		activity.setContentView(R.layout.getpwd_second);
		btn_checknum_submmit = (Button) activity
				.findViewById(R.id.btn_checknum_submmit);
		edt_inputchecknum = (EditText) activity
				.findViewById(R.id.edt_inputchecknum);
		btn_checknum_submmit.setOnClickListener(this);
		initView(null);
		setTitle("找回密码");
		setLeftButtonAble(true, "返回");
		ListenerManager.addCloseListener(this);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ListenerManager.removeCloseListener(this);
	}
	@Override
	public void onClick(View arg0) {
		// 提交验证
		String verificationCode = edt_inputchecknum.getText().toString().trim();
		if (!TextUtils.isEmpty(verificationCode)) {
			submitVerificationCode(phoneNum,verificationCode);
		} else {
			int resId = getStringRes(activity, "smssdk_write_identify_code");
			if (resId > 0) {
				Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void getCheckNumSuccess(String verifyCode) {
		// TODO Auto-generated method stub
		edt_inputchecknum.setText(verifyCode);
	}

	@Override
	public void afterVerificationCodeRequested() {
		// TODO Auto-generated method stub
	}
	@Override
	public void whenTimeoutofMessage() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void releaseShowingMessage() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void submitCheckNumSuccess(HashMap<String, Object> res) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(activity,FindPWDThirdForm.class);
		intent.putExtra("phoneNum", phoneNum);
		startActivity(intent);
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
	public void closeActivity() {
		// TODO Auto-generated method stub
		finish();
	}
	@Override
	public int getActivityid() {
		// TODO Auto-generated method stub
		return R.string.findpwdsecondform;
	}
}
