package com.example.casting.util.view;

import java.util.Random;

import com.example.casting_android.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;


public class LabelView extends LinearLayout implements OnCheckedChangeListener {

	private boolean selected = false;
	private Context mContext;
	TextView txt_label;
	CheckBox ckb_select;
	View checkBoxLayout;
	OnClickListener clicklistener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			setSelected(!isSelected());
		}
	};

	public void setText(String text) {
		if(text!=null && text.length()>0){
			txt_label.setText(text);
			setBackground();
		}else{
			txt_label.setText("");
			checkBoxLayout.setBackgroundResource(R.color.smssdk_transparent);
		}
		
	}

	public String getText(){
		return	txt_label.getText().toString();
	}
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		ckb_select.setChecked(selected);
	}

	private void initView() {
		random = new Random();
		txt_label = (TextView) findViewById(R.id.txt_label);
		ckb_select = (CheckBox) findViewById(R.id.ckb_select);
		checkBoxLayout = findViewById(R.id.checkBoxLayout);
		setSelected(selected);
		if(this.isFocusable()){
			this.setOnClickListener(clicklistener);
			ckb_select.setOnCheckedChangeListener(this);
		}else{
			ckb_select.setEnabled(false);
			ckb_select.setFocusable(false);
		}
		
		
	}

	public LabelView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		LayoutInflater.from(context).inflate(R.layout.labellayout, this,true);
		mContext = context;
		initView();
		// TODO Auto-generated constructor stub
	}

	public LabelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.labellayout, this,true);
		mContext = context;
		initView();
	}

	public LabelView(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.labellayout, this,true);
		mContext = context;
		initView();
		// TODO Auto-generated constructor stub
	}

	Random random;
	private void setBackground( ) {
		// TODO Auto-generated method stub
		int resid =0;
		if(random!=null){
			resid = random.nextInt(BgColor.length-1);
		}
		checkBoxLayout.setBackgroundResource(BgColor[resid]);
	}
	static int[] BgColor = { R.drawable.bg_8_blue,
			R.drawable.bg_8_darkred,
			R.drawable.bg_8_orange,
			R.drawable.bg_8_red,
			R.drawable.bg_8_blue,
			R.drawable.bg_8_yellow,
			R.drawable.bg_8_purple };
	
	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub
		selected = arg1;
	}

}
