package com.example.casting.login.regist;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.example.casting.entity.RegistBean;
import com.example.casting.login.regist.views.UserRegistView;
import com.example.casting.util.Session;
import com.example.casting_android.R;

public abstract class BaseUserInfoActivity extends BaseCompanyRegistActivity {

	LinearLayout contentlayout;
	View titleLayout;
	Button btn_submit;
	UserRegistView userView;

	public abstract int getOperaterType();
	protected abstract void submitClick() ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xtsz_fitcastform);
		contentlayout = (LinearLayout) findViewById(R.id.layout_content);
		titleLayout = (View) findViewById(R.id.titlelayout);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		initView(titleLayout);
		btn_submit.setOnClickListener(this);
		userView = new UserRegistView(this, handler, null,getOperaterType());
		contentlayout.addView(userView.getView());
		if(getOperaterType() == UserRegistView.OperateType_Set
				|| getOperaterType() == UserRegistView.OperateType_Show){
			btn_submit.setVisibility(View.GONE);
		}
	}

	protected boolean checkNull(RegistBean registbean) {
		if (registbean == null) {
			showToast("数据不完整");
			return false;
		}
		if (userView.checkNull()) {
			return true;
		} else {
			showToast(userView.errorInfo());
			return false;
		}
	}


	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		super.onClick(arg0);
		if (arg0.getId() == R.id.btn_submit) {
			submitClick();
		}
	}

	protected RegistBean saveViewValue() {
		RegistBean userBean = userView.getValues();
		userBean.setId(Session.getInstance().getUser_id());
		return userBean;
	}

	protected void updateView(RegistBean bean) {
		if (bean != null)
			userView.updateViewVal(bean);
	}

	@Override
	public void changeText(String val, int resid) {
		// TODO Auto-generated method stub
		userView.changeSubView(resid, val);
	}

	private final int SETVALUE = 10001;

	public void Jump(int activityResId, Bundle bd) {
		if (bd != null) {
			doActivity(activityResId, bd, SETVALUE);
		} else if (bd == null) {
			doActivity(activityResId, SETVALUE);
		}
	}


	@Override
	public View getWheelViewParent() {
		// TODO Auto-generated method stub
		return contentlayout;
	}

	@Override
	public int toCompanyOrDirector() {
		// TODO Auto-generated method stub
		return 0;
	}

}
