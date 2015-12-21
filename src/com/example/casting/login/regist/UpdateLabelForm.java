package com.example.casting.login.regist;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import com.example.casting.login.BaseForm;
import com.example.casting.util.view.LabelView;
import com.example.casting_android.R;

public class UpdateLabelForm extends BaseForm {
	View titleLayout;
	LabelView labelview1;
	LabelView labelview2;
	LabelView labelview3;
	LabelView labelview4;
	LabelView labelview5;
	LabelView labelview6;
	LabelView labelview7;
	int[] BgColor = { R.drawable.bg_8_blue,
			R.drawable.bg_8_darkred,
			R.drawable.bg_8_orange,
			R.drawable.bg_8_red,
			R.drawable.bg_8_blue,
			R.drawable.bg_8_yellow,
			R.drawable.bg_8_purple };
	LabelView[] labelviews;
	String[] labels = { "起床困难户", "小可耐", "单身待解放", "学霸", "戏霸", "麦霸", "萌萌哒" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edt_label);
		initSubView();

	}

	private void initSubView() {
		titleLayout = (View) findViewById(R.id.titlelayout);
		labelview1 = (LabelView) findViewById(R.id.labelview1);
		labelview2 = (LabelView) findViewById(R.id.labelview2);
		labelview3 = (LabelView) findViewById(R.id.labelview3);
		labelview4 = (LabelView) findViewById(R.id.labelview4);
		labelview5 = (LabelView) findViewById(R.id.labelview5);
		labelview6 = (LabelView) findViewById(R.id.labelview6);
		labelview7 = (LabelView) findViewById(R.id.labelview7);
		labelviews = new LabelView[] { labelview1, labelview2, labelview3,
				labelview4, labelview5, labelview6, labelview7 };
		
		initView(titleLayout);
		setTitle("编辑标签");
		setLeftButtonAble(true, "返回");
		setRightButtonAble(true, "保存");
		initData(getIntent().getExtras());
	}

	private void getDefaultLabels(String labelstr) {
		if (labelstr == null) {
			labelstr = "";
		}
		for (int i = 0; i < labelviews.length; i++) {
//			labelviews[i].setBackgroundResource(BgColor[i]);
			labelviews[i].setText(labels[i]);
			if (labelstr.indexOf(labels[i]) > -1) {
				labelviews[i].setSelected(true);
			}
		}
	}


	private void initData(Bundle bd) {
		if (bd != null) {
			String defaultLabels =  bd.getString(R.string.updatelabelnform+"");
			getDefaultLabels(defaultLabels);
		}
	}

	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub
		setBack( );
	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub
		setBack( );
	}

	private String getText(){
		String value = "";
		for (int i = 0; i < labelviews.length; i++) {
			if (labelviews[i].isSelected()) {
				value += (labels[i]) + ",";
			}
		}
		return value;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setBack();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	private void setBack() {
		String values = getText() ;
		Intent intent = new Intent();
		Bundle bd = new Bundle();
		bd.putString(R.string.updatelabelnform+"", values);
		intent.putExtras(bd);
		setResult(R.string.updatelabelnform,intent);
		finish();
		
	}
}
