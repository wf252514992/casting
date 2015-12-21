package com.example.casting.me;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.casting.login.BaseForm;
import com.example.casting.me.view.MyIntroduceView;
import com.example.casting_android.R;

public class MyDataActivity extends BaseForm implements OnClickListener{
	ImageView btn_left;
	ImageView img_mypic ;
	TextView txt_attention;
	TextView txt_fans;
	TextView txt_name;
	TextView txt_introduce;
	Button btn_edt;
	Button btn_zy;
	Button btn_dynamic;
	Button btn_photos;
	LinearLayout layout_content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mydata);
		initSubView();
		btn_left.setOnClickListener(this);
		MyIntroduceView view = new MyIntroduceView(this);
		layout_content.removeAllViews();
		layout_content.addView(view.getView());
		btn_edt.setOnClickListener(this);
	}
	private void initSubView(){
		btn_left=(ImageView)findViewById(R.id.btn_left);
		img_mypic=(ImageView)findViewById(R.id.img_mypic);
		txt_attention=(TextView)findViewById(R.id.txt_attention);
		txt_fans=(TextView)findViewById(R.id.txt_fans);
		txt_name=(TextView)findViewById(R.id.txt_name);
		txt_introduce=(TextView)findViewById(R.id.txt_introduce);
		btn_edt=(Button)findViewById(R.id.btn_edt);
		btn_zy=(Button)findViewById(R.id.btn_zy);
		btn_dynamic=(Button)findViewById(R.id.btn_dynamic);
		btn_photos=(Button)findViewById(R.id.btn_photos);
		layout_content =(LinearLayout)findViewById(R.id.layout_content);
	}
	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(R.id.btn_left == arg0.getId()){
			finish();
		}else if(R.id.btn_edt == arg0.getId()){
			doActivity(R.string.userinfoactivity);
		}
	}

}
