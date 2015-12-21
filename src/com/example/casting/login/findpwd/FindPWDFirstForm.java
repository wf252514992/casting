package com.example.casting.login.findpwd;

import java.util.HashMap;

import com.example.casting.listener.CloseAllListener;
import com.example.casting.listener.ListenerManager;
import com.example.casting.login.RquestMessageBaseDialog;
import com.example.casting_android.R;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class FindPWDFirstForm extends RquestMessageBaseDialog implements CloseAllListener{

	Button btn_getchecknum;
	EditText edt_inputphonenum;
	Context context ;
	public void show(Context context) {
		super.show(context);
		this.context = context;
	}
	@Override
	public void onCreate( ) {
		super.onCreate( );
		activity.setContentView(R.layout.getpwd_first);
		btn_getchecknum = (Button) activity
				.findViewById(R.id.btn_getchecknum);
		edt_inputphonenum= (EditText) activity
				.findViewById(R.id.edt_inputphonenum);
		btn_getchecknum.setOnClickListener(onClickListener);
		initView(null);
		setTitle("找回密码");
		setLeftButtonAble(true, "返回");
		ListenerManager.addCloseListener(this);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		ListenerManager.removeCloseListener(this);
	}
	
	OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			if (!isLoadingCountryRules()) {
				getSupportedCountries();
			} else {
				String phone = edt_inputphonenum.getText().toString().trim().replaceAll("\\s*", "");
				String code = getCountryCode(); 
				checkPhoneNum(phone, code);
			}
		}
	};

	@Override
	public void getCheckNumSuccess(String verifyCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterVerificationCodeRequested() {
		FindPWDSecondForm secondedialog = new FindPWDSecondForm(edt_inputphonenum.getText().toString());
		secondedialog.show(activity);
	}
	
	@Override
	public void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!isLoadingCountryRules())getSupportedCountries();
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
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
		return R.string.findpwdfirstform;
	}
	
}
