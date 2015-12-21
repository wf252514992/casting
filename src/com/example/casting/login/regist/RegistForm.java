package com.example.casting.login.regist;

import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.casting.entity.LoginBean;
import com.example.casting.login.CastingFirstForm;
import com.example.casting.login.RemindMeActivity;
import com.example.casting.login.RquestMessageBaseDialog;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.processor.login.RegistProcess;
import com.example.casting.util.Session;
import com.example.casting_android.R;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

public class RegistForm extends RquestMessageBaseDialog implements
		OnClickListener, OnCheckedChangeListener {

	EditText edt_regist_phone;
	EditText edt_regist_checknum;
	EditText edt_regist_pwd;
	TextView txt_remind;
	Button btn_regist_getchecknum;
	Button btn_regist_next;
	boolean isRegistCheckNum = false;
	boolean pageShowing = false;
	CheckBox ckb_regist_casting;

	public void show(Context context) {
		super.show(context);
		pageShowing = true;
	}

	LoginBean registBean;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		activity.setContentView(R.layout.casting_regist);
		ckb_regist_casting = (CheckBox) activity
				.findViewById(R.id.ckb_regist_casting);
		btn_regist_getchecknum = (Button) activity
				.findViewById(R.id.btn_regist_getchecknum);
		btn_regist_next = (Button) activity.findViewById(R.id.btn_regist_next);
		edt_regist_pwd = (EditText) activity.findViewById(R.id.edt_regist_pwd);
		edt_regist_phone = (EditText) activity
				.findViewById(R.id.edt_regist_phone);
		txt_remind = (TextView) activity.findViewById(R.id.txt_remind);
		edt_regist_checknum = (EditText) activity
				.findViewById(R.id.edt_regist_checknum);
		btn_regist_getchecknum.setOnClickListener(this);
		btn_regist_next.setOnClickListener(this);
		txt_remind.setOnClickListener(this);
		ckb_regist_casting.setOnCheckedChangeListener(this);
		initView(null);
		setTitle("注册");
		setLeftButtonAble(true, "返回");
		registBean = new LoginBean();
	}

	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == R.id.txt_remind) {
			startActivity(new Intent(activity, RemindMeActivity.class));
		} else if (arg0.getId() == R.id.btn_regist_getchecknum) {
			// 请求发送短信验证码
			if (!isLoadingCountryRules()) {
				getSupportedCountries();
			} else {
				String phone = edt_regist_phone.getText().toString().trim()
						.replaceAll("\\s*", "");
				String code = getCountryCode();
				checkPhoneNum(phone, code);
			}
		} else if (arg0.getId() == R.id.btn_regist_next) {
			if(!checkNull()){
				return ;
			}
			if (isRegistCheckNum) {
				Regist();
			} else {

				// 下一步...先提交验证码 再进入信息录入界面
				String phone = edt_regist_phone.getText().toString().trim()
						.replaceAll("\\s*", "");
				String verificationCode = edt_regist_checknum.getText()
						.toString().trim();
				if (!TextUtils.isEmpty(verificationCode)) {
					submitVerificationCode(phone, verificationCode);
				}
			}
		}
	}

	RegistProcess registPro = new RegistProcess();

	private void Regist() {
		LoginBean bean = new LoginBean();
		bean.setPass(edt_regist_pwd.getText().toString());
		bean.setPhone(edt_regist_phone.getText().toString());
		HttpCall(registPro, bean);
	}

	@Override
	public void getCheckNumSuccess(String verifyCode) {
		//获取到验证消息后自动填充
//		if (edt_regist_checknum != null && pageShowing) {
//			edt_regist_checknum.setText(verifyCode);
//		}

	}

	@Override
	public void afterVerificationCodeRequested() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!isLoadingCountryRules() && pageShowing)
			getSupportedCountries();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public boolean onFinish() {
		// TODO Auto-generated method stub
		pageShowing = false;
		return super.onFinish();
	}

	@Override
	public void whenTimeoutofMessage() {
		// TODO Auto-generated method stub
//		showToast("连接超时，请重新尝试");
	}

	@Override
	public void releaseShowingMessage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void submitCheckNumSuccess(HashMap<String, Object> res) {
		// TODO Auto-generated method stub
		// 短信验证成功后，注册
		isRegistCheckNum = true;
		Regist();

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
	public void OnReturn(String content, IBaseProcess processor) {
		// TODO Auto-generated method stub
		super.OnReturn(content, processor);
		if (processor.getMethod() == registPro.getMethod()) {
			ResultBean bean = registPro.json2Bean(content);
			if (bean.getCode() == Errors.OK) {
				String phone = edt_regist_phone.getText().toString().trim()
						.replaceAll("\\s*", "");
				JSONObject json;
				try {
					json = new JSONObject(bean.getObj().toString());
					String userid = json.getString("id");
					Session.getInstance().setPhone(phone);
					Session.getInstance().setUser_id(userid);

					startActivity(new Intent(activity, CastingFirstForm.class));
					finish();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				showToast(bean.getMessage());
			}
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub
		btn_regist_next.setEnabled(arg1);
		if (!arg1) {
			btn_regist_next.setBackgroundResource(R.drawable.bg_8_grey);
		} else {
			btn_regist_next.setBackgroundResource(R.drawable.bg_8_red);
		}
	}
	
	private boolean checkNull(){
		if(edt_regist_phone.length()==0){
			showToast("请输入手机号");
			return false;
		}

		if(edt_regist_checknum.length()==0){
			showToast("请输入验证码");
			return false;
		}
		if(edt_regist_pwd.length() == 0){
			showToast("请输入密码");
			return false;
		}
		if(edt_regist_pwd.length() < 6){
			showToast("请输入至少6位的密码");
			return false;
		}
		return true;
	}
}
