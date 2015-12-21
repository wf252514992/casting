package com.example.casting.login.regist;

import java.util.List;

import com.example.casting_android.R;
import com.wangjie.wheelview.OnWheelViewListener;
import com.wangjie.wheelview.WheelView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class MyWheelView implements OnWheelViewListener,OnClickListener {
	View wheelview;
	Context mCtx;
	WheelView wheelview_province, wheelview_city, wheelview_county;
	int screenWidth = 0 ;
	Button btn_cancel , btn_ok;
	IMyWheelViewCallBackListener callback;
	public static final int type_date = 1;
	public static final int type_address = 0;
	private String val1 ="";
	private String val2 = "";
	private String val3 = "";
	private int resid_call = 0;
	private int datatype = 0;
	public MyWheelView(Context ctx,int type ,int screenWidth,int resid,IMyWheelViewCallBackListener clicklistener) {
		this.mCtx = ctx;
		this.screenWidth=screenWidth;
		this.callback = clicklistener;
		this.resid_call = resid;
		datatype = type;
	}

	public View getWheelView() {
		if (wheelview == null) {
			wheelview = LayoutInflater.from(mCtx).inflate(R.layout.mywheelview,
					null);
			wheelview_province = (WheelView) wheelview
					.findViewById(R.id.wheelview_province);
			wheelview_city = (WheelView) wheelview
					.findViewById(R.id.wheelview_city);
			wheelview_county = (WheelView) wheelview
					.findViewById(R.id.wheelview_county);
		//  均分成三份 排一排
	        final int wheelWidth = (int) (screenWidth / 3.0f);
	        //  由于父级是Linear 所以对应的LayoutParam
	        wheelview_province.setLayoutParams(new LinearLayout.LayoutParams(wheelWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
	        wheelview_city.setLayoutParams(new LinearLayout.LayoutParams(wheelWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
	        wheelview_county.setLayoutParams(new LinearLayout.LayoutParams(wheelWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
			wheelview_province.setOnWheelViewListener(this);
			wheelview_province.setOffset(1);
			wheelview_province.setSeletion(0);
			wheelview_city.setOnWheelViewListener(this);
			wheelview_city.setOffset(1);
			wheelview_city.setSeletion(0);
			wheelview_county.setOnWheelViewListener(this);
			wheelview_county.setOffset(1);
			wheelview_county.setSeletion(0);
			btn_cancel = (Button)wheelview
					.findViewById(R.id.btn_cancel);
			btn_ok = (Button)wheelview
					.findViewById(R.id.btn_ok);
			btn_cancel.setOnClickListener(this);
			btn_ok.setOnClickListener(this);
		}
		return wheelview;
	}
	
	public void setWheelViewAdapter(int position,List<String> list){
		if(list!=null){
			if(position == 0){
				wheelview_province.setItems(list);
				wheelview_province.setSeletion(0);
				val1 = wheelview_province.getSeletedItem();
			}else if(position == 1){
				wheelview_city.setItems(list);
				wheelview_city.setSeletion(0);
				val2 = wheelview_city.getSeletedItem();
			}else if(position == 2){
				wheelview_county.setItems(list);
				wheelview_county.setSeletion(0);
				val3 = wheelview_county.getSeletedItem();
			}
		}
	}
	public String getSelectedItem(int position){
		if(position == 0){
			return wheelview_province.getSeletedItem();
		}else if(position == 1){
			return wheelview_city.getSeletedItem();
		}else if(position == 2){
			return wheelview_county.getSeletedItem();
		}else return "";
	}
	public int getSelectedIndex(int position){
		if(position == 0){
			return wheelview_province.getSeletedIndex();
		}else if(position == 1){
			return wheelview_city.getSeletedIndex();
		}else if(position == 2){
			return wheelview_county.getSeletedIndex();
		}else return -1;
	}

	@Override
	public void onSelected(int wheelviewId, int selectedIndex, String item) {
		// TODO Auto-generated method stub
		int position = 0;
		if ( wheelviewId == R.id.wheelview_province) {
			val1 = item;
			position = 0;
			
		} else if ( wheelviewId == R.id.wheelview_city) {
			val2 = item;
			position = 1;
		}else if ( wheelviewId == R.id.wheelview_county) {
			val3 = item;
			position = 2;
		}
		callback.ItemNotify(position);
	}

	@Override
	public void onClick(View arg0) {
		if(arg0.getId() == R.id.btn_ok){
			callback.btn_OkClickListener(getValue(),resid_call);
		}else if(arg0.getId() == R.id.btn_cancel){
			callback.btn_CancelClickListener();
		}
	}
	
	private String getValue(){
		if(datatype == type_date ){
			return val1+"-"+val2+"-"+val3;
		}else{
			return val1+val2+val3;
		}
	}

}
