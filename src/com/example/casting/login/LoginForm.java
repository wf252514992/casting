package com.example.casting.login;

import org.json.JSONObject;
import com.example.casting.MainTabNew;
import com.example.casting.db.UserManager.UserDBBusiness;
import com.example.casting.entity.LoginBean;
import com.example.casting.listener.CloseLoginFormListener;
import com.example.casting.listener.ListenerManager;
import com.example.casting.login.findpwd.FindPWDFirstForm;
import com.example.casting.login.regist.RegistForm;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.processor.login.LoginProcess;
import com.example.casting.util.Session;
import com.example.casting_android.R;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginForm extends BaseForm implements CloseLoginFormListener{
	
	EditText edt_user;
	EditText edt_pwd;
	Button btn_login;
	Button btn_findpwd;
	Button btn_zc;
	LoginProcess loginpro;
	boolean checkPhone = true;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.casting_login);
		ListenerManager.addLoginFormListener(this);
		edt_user = (EditText) findViewById(R.id.edt_user);
		edt_pwd = (EditText)findViewById( R.id.edt_pwd);
		btn_login = (Button)findViewById( R.id.btn_login);
		btn_findpwd = (Button)findViewById( R.id.btn_findpwd);
		btn_zc =(Button) findViewById( R.id.btn_zc);
		edt_pwd.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				if (edt_pwd.length() == 12) {
					showToast("密码长度不允许超过12位");
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

		edt_user.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
//				if (edt_user.length() < 11) {
//					checkPhone = false;
//				} else {
//					checkPhone = true;
//				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
		btn_login.setOnClickListener(onclicklst);
		btn_findpwd.setOnClickListener(onclicklst);
		btn_zc.setOnClickListener(onclicklst);
		
		UserDBBusiness userdb = new UserDBBusiness(this);
	};
	OnClickListener onclicklst = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (arg0.getId() == R.id.btn_zc) {
				// 用户注册---选择类型注册
				RegistForm register = new RegistForm();
				register.show(LoginForm.this);
				
//				doActivity(R.string.castingfirstform);

			} else if (arg0.getId() == R.id.btn_findpwd) {
				// 找回密码
				FindPWDFirstForm firstdialog = new FindPWDFirstForm();
				firstdialog.show(LoginForm.this);
			} else if (arg0.getId() == R.id.btn_login) {
				// 登录

//				Session.getInstance().setUser_id("1");
//				Intent intent=new Intent(LoginForm.this,MainTabNew.class);
//				startActivity(intent);
//				finish();
				if (checkNull()) {
					Logining();
				}
			}
		}
	};
	private boolean checkNull() {
		if (edt_user.length() == 0) {
			showToast("请输入手机号码");
			return false;
		}
		if (!checkPhone) {
			showToast("手机号码错误");
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
		loginpro = new LoginProcess();
		LoginBean bean = new LoginBean();
		bean.setPass(edt_pwd.getText().toString());
		bean.setPhone(edt_user.getText().toString());
		HttpCall(loginpro, bean);
	}

	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			existApp(false);
		}
		return true;
	}

	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub

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
				Intent intent = new Intent(LoginForm.this, MainTabNew.class);
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		edt_user.setText(Session.getInstance().getPhone());
		edt_pwd.setText(Session.getInstance().getPassword());
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	boolean isloginged = false;
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus&&!isloginged){
			isloginged= true;
			if(edt_user.length()>0&& edt_pwd.length()>0){
				Logining();
			};
		}
	}


	@Override
	public void closeActivity() {
		System.out.println("--finish----");
		finish();
	}


	@Override
	public int getActivityid() {
		return R.string.loginform;
	}
}
