package com.example.casting.login;

import org.json.JSONObject;

import com.example.casting.MainTabNew;
import com.example.casting.entity.LoginBean;
import com.example.casting.listener.CloseAllListener;
import com.example.casting.listener.ListenerManager;
import com.example.casting.login.findpwd.FindPWDFirstForm;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.processor.login.LoginProcess;
import com.example.casting.util.Session;
import com.example.casting_android.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class LoginAddIDForm extends BaseForm implements OnEditorActionListener ,OnClickListener,CloseAllListener{
	LoginProcess loginpro = new LoginProcess();
	View titlelayout ;
	EditText edt_pwd,edt_phone;
	TextView txt_forgetpwd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.casting_login_addid);
		titlelayout = findViewById(R.id.titlelayout);
		initView(titlelayout);
		setLeftButtonAble(true, "返回");
		setTitle("添加账号");
		edt_pwd = (EditText)findViewById(R.id.edt_pwd);
		edt_phone = (EditText)findViewById(R.id.edt_phone);
		txt_forgetpwd = (TextView)findViewById(R.id.txt_forgetpwd);
		edt_pwd.setOnEditorActionListener(this);
		edt_phone.setOnEditorActionListener(this);
		txt_forgetpwd.setOnClickListener(this);
		ListenerManager.addCloseListener(this);
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
	public boolean onEditorAction(TextView v, int arg1, KeyEvent arg2) {
		// TODO Auto-generated method stub
		if(arg1 == EditorInfo.IME_ACTION_DONE){
			InputMethodManager imm = (InputMethodManager) v.getContext()
					.getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			if (checkNull()) {
				Logining();
			}
		}
		
		return false;
	}
	private boolean checkNull() {
		if (edt_phone.length() == 0) {
			showToast("请输入手机号码");
			return false;
		}
		if (edt_pwd.length() == 0) {
			showToast("请输入密码");
			return false;
		}
		if (edt_pwd.length() < 6) {
			showToast("请输入至少6位的密码");
			return false;
		}
		return true;
	}
	private void Logining() {
		
		LoginBean bean = new LoginBean();
		bean.setPass(edt_pwd.getText().toString());
		bean.setPhone(edt_phone.getText().toString());
		HttpCall(loginpro, bean);
	}
	public void OnReturn(String result, IBaseProcess proid) {
		super.OnReturn(result, proid);
		ResultBean bean = loginpro.json2Bean(result);
		if (bean.getCode() == Errors.OK) {
			try {
				
				JSONObject json = (JSONObject) bean.getObj();
				String id = json.getString("id");
				String phone = json.getString("phone");
				String nickname = json.getString("nickname");
				String usertype = json.getString("type");
				String headportait = json.getString("head_portrait");
				Session.getInstance().setUser_id(id);
				Session.getInstance().setPhone(phone);
				Session.getInstance().setHeadportait(headportait);
				Session.getInstance().setPassword(edt_pwd.getText().toString());
				Session.getInstance().setName_nick(nickname);
				Session.getInstance().setUsertype(usertype);
				Session.getInstance().Save(this);
				ListenerManager.notifyCloseActivity();
				Intent intent = new Intent(LoginAddIDForm.this, MainTabNew.class);
				startActivity(intent);
				
			} catch (Exception e) {
				e.printStackTrace();
				showToast("登录失败");
			}
			
		} else {
			showToast(bean.getMessage());
		}
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		FindPWDFirstForm firstdialog = new FindPWDFirstForm();
		firstdialog.show(LoginAddIDForm.this);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ListenerManager.removeCloseListener(this);
	}
	@Override
	public void closeActivity() {
		// TODO Auto-generated method stub
		finish();
	}
	@Override
	public int getActivityid() {
		// TODO Auto-generated method stub
		return R.string.loginaddidform;
	}
}
