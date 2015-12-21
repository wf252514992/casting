package com.example.casting.util.view;

import java.io.File;

import com.example.casting.util.ConstantData;
import com.example.casting_android.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AlertView {

	public interface ViewClick{
		public void leftClick(Object obj);
		public void rightClick(Object obj);
	}
	
	View dialogView ;
	TextView txt_show;
	Button btnleft ;
	Button btnright;
	ViewClick clicklistener ;
	Context context;
	public AlertView(Context ctx,ViewClick clicklistener){
		this.clicklistener = clicklistener;
		initView(ctx);
		this.context = ctx;
	}
	
	private void initView(Context ctx ){
		dialogView = LayoutInflater.from(ctx).inflate(R.layout.alertview, null);
		txt_show = (TextView) dialogView.findViewById(R.id.txt_show); 
		btnleft = (Button) dialogView.findViewById(R.id.btn_commit); 
		btnright = (Button) dialogView.findViewById(R.id.btn_cancel); 
		btnleft.setOnClickListener(click);
		btnright.setOnClickListener(click);
	}
	public View getView(){
		return dialogView;
	}
	
	public void setContent(String value){
		txt_show.setText(value);
	}
	public void setContent(String value,String left,String right){
		txt_show.setText(value);
		btnleft.setText(left);
		btnright.setText(right);
	}
	public void setContent(String value,String right){
		txt_show.setText(value);
		btnright.setText(right);
		btnleft.setVisibility(View.GONE);
	}
	OnClickListener click =new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			if(arg0.getId() == R.id.btn_commit){
				clicklistener.rightClick( null );
				File file = new File(ConstantData.getCastingDir());
				if(file.exists())
				{
					Util.delete(file, context);
				}
			}
			else if(arg0.getId() == R.id.btn_cancel){
				clicklistener.leftClick( null );
			}
		}
	};
}
