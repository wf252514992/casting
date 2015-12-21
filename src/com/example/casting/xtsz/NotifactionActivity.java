package com.example.casting.xtsz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.casting.entity.SettingBean;
import com.example.casting.login.BaseForm;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.processor.setting.SettingSetProcessor;
import com.example.casting.processor.setting.SettingGetProcessor;
import com.example.casting_android.R;

public class NotifactionActivity extends BaseForm implements OnClickListener {

	View titleLayout;
	LinearLayout layout_tx_comment;
	LinearLayout layout_tx_privateletter;
	LinearLayout layout_comment;
	LinearLayout layout_priletter;
	LinearLayout layout_notattention;
	TextView txt_qx_comment,txt_qx_priletter,
		txt_tx_comment,txt_tx_priletter,txt_tx_beattention;
	SettingSetProcessor  addpro = new SettingSetProcessor();
	SettingGetProcessor getpro = new SettingGetProcessor();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xtsz_notificationlist);
		titleLayout = (View) findViewById(R.id.titlelayout);
		layout_tx_comment = (LinearLayout) findViewById(R.id.layout_tx_comment);
		layout_tx_privateletter = (LinearLayout) findViewById(R.id.layout_tx_privateletter);
		layout_comment = (LinearLayout) findViewById(R.id.layout_comment);
		layout_priletter = (LinearLayout) findViewById(R.id.layout_priletter);
		layout_notattention = (LinearLayout) findViewById(R.id.layout_notattention);
		txt_qx_comment = (TextView) findViewById(R.id.txt_qx_comment);
		txt_qx_priletter = (TextView) findViewById(R.id.txt_qx_priletter);
		txt_tx_comment = (TextView) findViewById(R.id.txt_tx_comment);
		txt_tx_priletter = (TextView) findViewById(R.id.txt_tx_priletter);
		txt_tx_beattention = (TextView) findViewById(R.id.txt_tx_beattention);
		
		initView(titleLayout);
		setLeftButtonAble(true, "返回");
		setTitle("提醒和通知");
		layout_tx_comment.setOnClickListener(this);
		layout_comment.setOnClickListener(this);
		layout_tx_privateletter.setOnClickListener(this);
		layout_priletter.setOnClickListener(this);
		layout_notattention.setOnClickListener(this);

		HttpCall(getpro, null);
	}

	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub
		HttpCall(addpro, setBean,"str");
	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub

	}
	@Override
	public void OnReturn(String content, IBaseProcess processor) {
		// TODO Auto-generated method stub
		super.OnReturn(content, processor);
		ResultBean result = processor.json2Bean(content);
		if( processor.getMethod().equals(getpro.getMethod())){
			if(result.getCode() == Errors.OK){
				Object obj = result.getObj();
				if(obj!=null && obj instanceof SettingBean ){
					setBean = (SettingBean)obj;
					upadteUI(setBean);
				}
			}
			
		}else if( processor.getMethod().equals(addpro.getMethod())){
			finish();
		}
		
	}

	private static final int SETNOTIFICATION = 1001;
	private static final int UPDATEUI = 10002;
	private SettingBean setBean = new SettingBean();
	@Override
	public void onClick(View arg0) {
		Bundle bd = new Bundle();
		if (arg0.getId() == R.id.layout_comment) {
			bd.putString("TAG", Set_AboutmeActivity.Tag_qx_comment);
			
		} else if (arg0.getId() == R.id.layout_priletter) {
			bd.putString("TAG", Set_AboutmeActivity.Tag_qx_priletter);

		} else if (arg0.getId() == R.id.layout_tx_privateletter) {
			bd.putString("TAG", Set_AboutmeActivity.Tag_tx_priletter);

		} else if (arg0.getId() == R.id.layout_notattention) {
			bd.putString("TAG", Set_AboutmeActivity.Tag_tx_beattention);

		} else if (arg0.getId() == R.id.layout_tx_comment) {

			bd.putString("TAG", Set_AboutmeActivity.Tag_tx_comment);
		}
		bd.putSerializable(SettingBean.class.getName(), setBean);
		doActivity(R.string.set_aboutmeactivity, bd,SETNOTIFICATION);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == SETNOTIFICATION){
			if(resultCode== Activity.RESULT_OK)
			{
				try{
					SettingBean bean = (SettingBean) data.getSerializableExtra(SettingBean.class.getName());
					if(setBean!=null){
						setBean = bean;
					}
					Message msg = new Message();
					msg.what = UPDATEUI;
					msg.obj = bean;
					handler.sendMessage(msg);
					
				}catch(Exception e){
					
				}
			}
		}
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			if( msg.what == UPDATEUI ){
				upadteUI(setBean);
			}
			
		};
	};
	private void upadteUI(SettingBean setbean){
		if(setbean!=null){
			txt_qx_comment.setText(dm2mc(setbean.getComment()));
			txt_qx_priletter.setText(dm2mc(setbean.getLetter()));
			txt_tx_comment.setText(dm2mc(setbean.getRemind_comment()));
			txt_tx_priletter.setText(dm2mc(setbean.getRemind_letter()));
			txt_tx_beattention.setText(dm2mc(setbean.getRemind_attention()));
		}
	}
	
	private String dm2mc(int dm){
		if(dm==(SettingBean.val1)){
			return "所有人";
		}else if(dm==(SettingBean.val2)){
			return "我关注的人";
		}else if(dm==(SettingBean.val3)){
			return "不提醒";
		}else{
			return "";
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			LeftButtonClick();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
