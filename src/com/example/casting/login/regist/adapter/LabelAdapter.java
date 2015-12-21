package com.example.casting.login.regist.adapter;

import java.util.ArrayList;
import java.util.List;
import com.example.casting.entity.ConditionBean;
import com.example.casting_android.R;
import com.ta.common.Arrays;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public  class LabelAdapter extends BaseAdapter {
	ArrayList<ConditionBean> conditions = new ArrayList<ConditionBean>();

	private LayoutInflater mInflater;
	Context mContext;
	public LabelAdapter(Context ctx,ArrayList<ConditionBean> list){
		mContext = ctx;
		conditions.clear();
		if(list!=null){
			conditions.addAll(list);
		}
		this.mInflater = LayoutInflater.from(ctx);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return conditions.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return conditions.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public void notifyDataSetChanged(ArrayList<ConditionBean> list) {
		// TODO Auto-generated method stub
		conditions.clear();
		if(list!=null)
			conditions.addAll(list);
		super.notifyDataSetChanged();
	}
	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.labelitem, null);
			holder.txt_label = (TextView)convertView.findViewById(R.id.txt_label);
			holder.ckb_label = (CheckBox)convertView.findViewById(R.id.ckb_label);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		ConditionBean bean = conditions.get(arg0);
		holder.txt_label.setText(bean.getLabel());
		String vlaue = ","+getSelectedValue()+",";
		boolean checked = false;
		if( vlaue.indexOf(","+bean.getLabel()+",")>-1){
			checked = true;
		}
		holder.ckb_label.setChecked(checked);
		holder.ckb_label.setOnCheckedChangeListener(new MyCheckedChangeListener(bean));
		convertView.setOnClickListener(new MyClickListener(holder.ckb_label));
		return convertView;
	}
	private static class ViewHolder {
		public TextView txt_label ;
		public CheckBox ckb_label;
	}
	
	protected class MyClickListener implements OnClickListener{
		CheckBox cbbox ;
		MyClickListener(CheckBox view){
			this.cbbox =view;
		}
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			boolean checked = cbbox.isChecked();
			cbbox.setChecked(!checked);
		}
		
	}
	protected class MyCheckedChangeListener  implements OnCheckedChangeListener{
		ConditionBean bean ;
		MyCheckedChangeListener(ConditionBean cbean){
			this.bean =cbean;
		}
		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			if( arg1 ){
				selectedItems.add(bean.getLabel());
			}else{
				selectedItems.remove(bean.getLabel());
			}
		}
	}
	private ArrayList<String> selectedItems = new ArrayList<String>();
	
	public String getSelectedValue(){
		String value = "";
		for(String selected : selectedItems){
			value+=selected+",";
		}
		return value;
	}
	
	private void initSelectedItems(List<String> list){
		this.selectedItems.clear();
		if(list!=null)
			selectedItems.addAll(list);
			
	}
	
	public void initSelectedItems(String labelstr){
		if( labelstr== null)
			labelstr = "";
		String[] labels = labelstr.split(",");
		List<String> list = Arrays.asList(labels);
		initSelectedItems(list);
	}
}
