package com.example.casting.login.regist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.casting.entity.RegistBean;
import com.example.casting.login.regist.views.UserRegistView;
import com.example.casting_android.R;

/**
 * 用户信息显示
 * @author Administrator
 *
 */
public class UserInfoShowActivity extends BaseUserInfoActivity {

//	UserGetProcessor usergetPro = new UserGetProcessor();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("个人资料");
		setLeftButtonAble(true, "返回");
		initData(getIntent());
	}
	
	private void initData(Intent intent){
		if(intent!=null){
			Bundle bd = intent.getExtras();
			if(bd!=null&&bd.getSerializable(R.string.userinfoshowactivity+"")!=null){
				RegistBean bean = (RegistBean) bd
						.getSerializable(R.string.userinfoshowactivity + "");
				updateView(bean);
//				HttpCall(usergetPro, bean);
				return ;
			}
		}
		finish();
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
	public RegistBean getLocalBean() {
		// TODO Auto-generated method stub
		return saveViewValue();
	}

	@Override
	protected void submitClick() {
	}

	@Override
	public void changeText(String val, int resid) {

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

	@Override
	public View getParentView() {
		// TODO Auto-generated method stub
		return getWheelViewParent();
	}

	@Override
	public void OnPicReturn(String filepath) {
	}

	@Override
	public int getOperaterType() {
		// TODO Auto-generated method stub
		return UserRegistView.OperateType_Show;
	}

	@Override
	public void uploadError() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uploadSuccess(String url) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uploadFiald(String msg) {
		// TODO Auto-generated method stub
		
	}

}
