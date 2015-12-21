package com.example.casting.login;

import java.io.InputStream;

import org.apache.http.util.EncodingUtils;

import com.example.casting.login.regist.works.adapter.WorksAdapter;
import com.example.casting_android.R;
import com.ui.baoyz.swipemenulistview.SwipeMenuListView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class RemindMeActivity extends BaseForm{

	View titleLayout;
	TextView txt_remind;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remind);
		txt_remind = (TextView)findViewById(R.id.txt_remind);
		
		titleLayout = (View) findViewById(R.id.titlelayout); 
		initView(titleLayout);
		setTitle("用户协议");
		setLeftButtonAble(true, "返回");
		String fileName = "userinfo.txt"; //文件名字 
		String res=""; 
		try{ 

		   //得到资源中的asset数据流
		   InputStream in = getResources().getAssets().open(fileName); 
		   int length = in.available();         
		   byte [] buffer = new byte[length];        

		   in.read(buffer);            
		   in.close();
		   res = EncodingUtils.getString(buffer, "UTF-8");     

		  }catch(Exception e){ 

		      e.printStackTrace();         

		   } 
		txt_remind.setText(res);
	}
	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub
		
	}

}
