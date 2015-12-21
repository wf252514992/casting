package com.example.casting.login.findpwd;

import org.json.JSONObject;
import com.example.casting.MainTabNew;
import com.example.casting.entity.LoginBean;
import com.example.casting.listener.ListenerManager;
import com.example.casting.login.BaseForm;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.processor.login.LoginProcess;
import com.example.casting.util.Session;
import com.example.casting_android.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FindPWDSuccessForm extends BaseForm implements OnClickListener {
	View titleLayout;
	Button btn_findpwd_submit;
	LoginProcess loginpro = new LoginProcess();
	LoginBean loginbean;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getpwd_success);
		titleLayout = (View) findViewById(R.id.titlelayout);
		btn_findpwd_submit = (Button) findViewById(R.id.btn_findpwd_submit);
		btn_findpwd_submit.setOnClickListener(this);
		initView(titleLayout);
		setLeftButtonAble(true, "返回");
		setTitle("找回密码");
		initData(getIntent());
	}

	private void initData(Intent intent){
		if( intent!=null ){
			Bundle bd = intent.getExtras();
			if(bd.getSerializable(R.string.findpwdsuccessform+"")!=null){
				loginbean  = (LoginBean) bd.getSerializable(R.string.findpwdsuccessform+"");
			}
		}
	}
	@Override
	public void onClick(View arg0) {
		HttpCall(loginpro, loginbean);
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
				Session.getInstance().setPassword(loginbean.getPass());
				Session.getInstance().setName_nick(nickname);
				Session.getInstance().setUsertype(usertype);
				Session.getInstance().Save(this);
				ListenerManager.notifyCloseActivity();
				Intent intent = new Intent(FindPWDSuccessForm.this, MainTabNew.class);
				startActivity(intent);
				finish();
			} catch (Exception e) {
				e.printStackTrace();
				showToast("登录失败");
			}
			
		} else {
			showToast(bean.getMessage());
		}
	}
}
