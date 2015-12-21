package com.example.casting.xtsz;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.example.casting.login.BaseForm;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.processor.feedback.FeedBackProcessor;
import com.example.casting_android.R;

public class FeedBackActivity extends BaseForm{
	View titleLayout;
	EditText edt_ideal;
	FeedBackProcessor feedbackPro = new FeedBackProcessor();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xtsz_feedback);
		titleLayout =(View) findViewById(R.id.titlelayout);
		edt_ideal =(EditText) findViewById(R.id.edt_ideal);
		edt_ideal.requestFocus();
		initView(titleLayout);
		setLeftButtonAble(true, "返回");
		setRightButtonAble(true, "发送");
		setTitle("意见反馈");
		
	}
	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub
		if(edt_ideal.getText().toString().trim().length()>0)
		{
			HttpCall(feedbackPro, edt_ideal.getText().toString(),"str");
		}else{
			showToast("请输入反馈的内容!");
		}
	}

	@Override
	public void OnReturn(String content, IBaseProcess processor) {
		// TODO Auto-generated method stub
		super.OnReturn(content, processor);
		ResultBean result = processor.json2Bean(content);
		if( result.getCode() == Errors.OK){
			showToast("反馈成功，谢谢您的反馈!");
			finish();
		}else{
			showToast("反馈失败!");
		}
	}
}
