package com.example.casting.xtsz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.example.casting.entity.SettingBean;
import com.example.casting.login.BaseForm;
import com.example.casting_android.R;

public class Set_AboutmeActivity extends BaseForm implements
		OnCheckedChangeListener {

	public static final String Tag_qx_priletter = "1";
	public static final String Tag_qx_comment = "2";
	public static final String Tag_tx_priletter = "3";
	public static final String Tag_tx_comment = "4";
	public static final String Tag_tx_beattention = "5";
	View titleLayout;
	RadioGroup group;
	RadioButton txt_notifaction_all, txt_notifaction_myattention,
			txt_notifaction_noting;

	TextView txt_notificaiton;
	SettingBean setbean ;
	String tag = Tag_qx_priletter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xtsz_set_aboutme);
		titleLayout = (View) findViewById(R.id.titlelayout);
		initView(titleLayout);
		setLeftButtonAble(true, "返回");
		group = (RadioGroup) findViewById(R.id.group);
		txt_notifaction_all = (RadioButton) findViewById(R.id.txt_notifaction_all);
		txt_notifaction_myattention = (RadioButton) findViewById(R.id.txt_notifaction_myattention);
		txt_notifaction_noting = (RadioButton) findViewById(R.id.txt_notifaction_noting);
		txt_notificaiton = (TextView)findViewById(R.id.txt_notificaiton);
		Intent intent = getIntent();
		Bundle bd = intent.getExtras();
		int val = -1 ;
		if(bd.getSerializable(SettingBean.class.getName())!=null){
			setbean = (SettingBean)bd.getSerializable(SettingBean.class.getName() );
			val = setbean.getRemind_attention();
		}
		if( bd.getString("TAG")!=null){
			tag = bd.getString("TAG");
		}
		if(setbean==null)
			setbean = new SettingBean();
		String title = "";
		String notificationstr = "";
		if( tag!=null&& setbean!=null){
			if( tag.equals(Tag_qx_priletter)){
				val = setbean.getLetter();
				title = "私信权限";
				notificationstr = "允许这些人私信我";
			}else if( tag.equals(Tag_qx_comment)){
				val = setbean.getComment();
				title = "评论权限";
				notificationstr = "允许这些人评论我";
			}else if( tag.equals(Tag_tx_comment)){
				val = setbean.getRemind_comment();
				title = "评论提醒";
				notificationstr = "我将收到这些人的评论提醒";
			}else if( tag.equals(Tag_tx_priletter)){
				val = setbean.getRemind_letter();
				title = "私信提醒";
				notificationstr = "我将收到这些人的私信提醒";
			}else if( tag.equals(Tag_tx_beattention)){
				val = setbean.getRemind_attention();
				title = "被关注提醒";
				notificationstr = "我将收到这些人的关注提醒";
			}
		}
		setTitle(title);
		txt_notificaiton.setText(notificationstr);
		setSelectItem(val);
		group.setOnCheckedChangeListener(this);
		
	}
	
	
	private void setSelectItem(int val){
		if(val== SettingBean.val1){
			txt_notifaction_all.setChecked(true);
		}else if(val== SettingBean.val2){
			txt_notifaction_myattention.setChecked(true);
		}else if(val== SettingBean.val3){
			txt_notifaction_noting.setChecked(true);
		}
	}

	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub
//		if( tag.equals(Tag_qx_priletter)){
//			setbean.setLetter(val);
//		}else if( tag.equals(Tag_qx_comment)){
//			setbean.setComment(val);
//		}else if( tag.equals(Tag_tx_comment)){
//			setbean.setRemind_comment(val);
//		}else if( tag.equals(Tag_tx_priletter)){
//			setbean.setRemind_letter(val);
//		}else if( tag.equals(Tag_tx_beattention)){
//			setbean.setRemind_attention(val);
//		}
//		Intent intent = new Intent();
//		intent.putExtra(SettingBean.class.getName(), setbean);
//		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		int checkedid = arg0.getCheckedRadioButtonId();
		int val = -1;
		if (checkedid == R.id.txt_notifaction_all) {
			val = SettingBean.val1;
		}else if(checkedid == R.id.txt_notifaction_myattention){
			val = SettingBean.val2;
		}else if(checkedid == R.id.txt_notifaction_noting){
			val = SettingBean.val3;
		}
		
		if( tag.equals(Tag_qx_priletter)){
			setbean.setLetter(val);
		}else if( tag.equals(Tag_qx_comment)){
			setbean.setComment(val);
		}else if( tag.equals(Tag_tx_comment)){
			setbean.setRemind_comment(val);
		}else if( tag.equals(Tag_tx_priletter)){
			setbean.setRemind_letter(val);
		}else if( tag.equals(Tag_tx_beattention)){
			setbean.setRemind_attention(val);
		}
		Intent intent = new Intent();
		intent.putExtra(SettingBean.class.getName(), setbean);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode== KeyEvent.KEYCODE_BACK){
			LeftButtonClick();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
