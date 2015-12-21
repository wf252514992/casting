package com.example.casting.publisheddynamic;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import com.example.casting.login.BaseForm;
import com.example.casting_android.R;

public class ShareRangeActivity extends BaseForm {
//	@TAInjectView(id = R.id.titlelayout)
	View titleLayout;
	private String shareRange;
	private String selectedShareRange;
    private  int selectedPosition;
    private ListView listview;
    private ArrayList<String> list;
//	private CheckBox openCheckbox,attentionalCheckbox,onlyCheckbox;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}
    private void init(){
//    	openCheckbox=(CheckBox) findViewById(R.id.openCheckbox);
//    	attentionalCheckbox=(CheckBox) findViewById(R.id.attentionalCheckbox);
//    	onlyCheckbox=(CheckBox) findViewById(R.id.onlyCheckbox);
    	Intent intent=getIntent();
    	shareRange=intent.getStringExtra("shareRange");
		setContentView(R.layout.sharerange);
		titleLayout=findViewById(R.id.titlelayout);
		initView(titleLayout);
		setLeftButtonAble(true, "返回");
		setRightButtonAble(true, "完成");
		setTitle("选择分享范围");
//		if(shareRange.equals("公开")){
//			openCheckbox.setChecked(true);
//		}else if(shareRange.equals("我关注的人")){
//			attentionalCheckbox.setChecked(true);
//		}else if(shareRange.equals("仅自己可见")){
//			onlyCheckbox.setChecked(true);
//		}
//		openCheckbox.setOnCheckedChangeListener(this);
//		attentionalCheckbox.setOnCheckedChangeListener(this);
//		onlyCheckbox.setOnCheckedChangeListener(this);
		listview=(ListView) findViewById(R.id.ShareRangeList);
		list=new ArrayList<String>();
		list.add("公开");
		list.add("我关注的人");
		list.add("仅自己可见");
		ShareRangeAdapter adapter=new ShareRangeAdapter(this, list);
		listview.setAdapter(adapter);
    }
    class ShareRangeAdapter extends BaseAdapter{
		Context context;
		ArrayList<String> arrayList;

		public ShareRangeAdapter(Context context,ArrayList<String> arrayList){
			super();
			this.context=context;
			this.arrayList=arrayList;
		
		}
		@Override
		public int getCount() {
			return arrayList.size();
		}

		@Override
		public Object getItem(int position) {
			return arrayList.get(position);
		}

		@Override
		public long getItemId(int span) {
			return span;
		}

		@Override
		public View getView( final int position, View v, ViewGroup group) {
			LayoutInflater inflater=LayoutInflater.from(context);
			v=inflater.inflate(R.layout.sharerange_item, null);
			TextView range = (TextView) v.findViewById(R.id.range);
		    CheckBox checkbox= (CheckBox) v.findViewById(R.id.checkbox);
		    range.setText(arrayList.get(position));
		    if(selectedPosition==0){
		    	 if(shareRange.equals(arrayList.get(position))){
				    	checkbox.setChecked(true);
				    }
		    }else if(position==selectedPosition){
		    	checkbox.setChecked(true);
		    }
		    if(checkbox.isChecked()){
		    	selectedShareRange=arrayList.get(position);
		    }
		    checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean flag) {
					if(flag){
						selectedPosition=position;
						ShareRangeAdapter adapter=new ShareRangeAdapter(ShareRangeActivity.this, list);
						listview.setAdapter(adapter);

					}
					
				}
			});
		   
			return v;
		}

	}
	@Override
	public void LeftButtonClick() {
		finish();
	}

	@Override
	public void RightButtonClick() {
		Intent intent = new Intent();
		intent.putExtra("shareRange", selectedShareRange);
		setResult(Activity.RESULT_OK, intent);
		finish();//结束之后会将结果传回From
	}
	
//	@Override
//	public void onCheckedChanged(CompoundButton v, boolean flag) {
//		switch (v.getId()) {
//		case R.id.openCheckbox:
//			if(flag){
//				selectedShareRange="公开";
//				attentionalCheckbox.setChecked(false);
//				onlyCheckbox.setChecked(false);
//			}
//			break;
//		case R.id.attentionalCheckbox:
//			if(flag){
//				selectedShareRange="我关注的人";
//				openCheckbox.setChecked(false);
//				onlyCheckbox.setChecked(false);
//			}
//			break;
//		case R.id.onlyCheckbox:
//			if(flag){
//				selectedShareRange="仅自己可见";
//				openCheckbox.setChecked(false);
//				attentionalCheckbox.setChecked(false);
//			}
//			break;
//		default:
//			break;
//		}
//		
//	}

}
