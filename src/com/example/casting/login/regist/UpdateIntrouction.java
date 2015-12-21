package com.example.casting.login.regist;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import com.example.casting.login.BaseForm;
import com.example.casting_android.R;

public class UpdateIntrouction extends BaseForm {
	View titleLayout;
	EditText edt_introduction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.casting_update_introduction);
		initSubView();
	}

	protected void initSubView() {
		// TODO Auto-generated method stub
		titleLayout =(View)findViewById(R.id.titlelayout);
		edt_introduction = (EditText)findViewById(R.id.edt_introduction);
		initView(titleLayout);
		setTitle("简介");
		setLeftButtonAble(true, "返回");
		setRightButtonAble(true, "确定");
		
		initData(getIntent().getExtras());

	}

	@Override
	public void LeftButtonClick() {
		setBack(getText());
	}

	@Override
	public void RightButtonClick() {
		setBack(getText());
	}

	private void initData(Bundle bd) {
		if (bd != null) {
			String value  =  bd.getString(R.string.updateintrouction+"");
			setText(value);
		}
	}

	public void setText(String introduction) {
		edt_introduction.setText(introduction);
	}

	public String getText() {
		return edt_introduction.getText().toString();
	}

	void setBack(String introduce) {
		Intent intent = new Intent();
		Bundle bd = new Bundle();
		bd.putSerializable(R.string.updateintrouction+"",introduce);
		intent.putExtras(bd);
		setResult(R.string.updateintrouction,intent);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setBack(getText());
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}
