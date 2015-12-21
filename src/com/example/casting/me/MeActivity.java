package com.example.casting.me;

import java.util.ArrayList;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import com.example.casting.login.BaseForm;
import com.example.casting.me.adapter.AboutMeSetAdapter;
import com.example.casting_android.R;
import com.example.casting_android.control.HorizontalScrollMyView;
import com.example.casting_android.control.HorizontalScrollMyView.OnItemMenuClickListener;

public class MeActivity extends BaseForm implements OnItemMenuClickListener,OnItemClickListener {
	View titleLayout;
	HorizontalScrollMyView scrview_content;
	ArrayList<String> contentllist = new ArrayList<String>();
	ListView lstview_setitem;
	AboutMeSetAdapter adapter ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me);
		titleLayout = (View)findViewById(R.id.titlelayout);
		lstview_setitem = (ListView)findViewById(R.id.lstview_setitem);
		scrview_content = (HorizontalScrollMyView)findViewById(R.id.scrview_content);
		initView(titleLayout);
		setLeftButtonAble(true, "返回");
		setTitle("我");
		initContentString();
		scrview_content.setOnItemMenuClickListener(this);
		adapter = new AboutMeSetAdapter(this);
		lstview_setitem.setAdapter(adapter);
		lstview_setitem.setOnItemClickListener(this);
	}
	
	private void initContentString() {
		contentllist.clear();
		contentllist.add("动态");
		contentllist.add("关注");
		contentllist.add("粉丝");
		int width = (int) (screen_width/3.5) ;
		if(contentllist.size()<4){
			width = (int) (screen_width/contentllist.size()) ;
		}
		LayoutParams params = new LayoutParams(width,screen_height/14,1 );
		params.setMargins(1, 0, 0, 0);
		for (int i = 0; i < contentllist.size(); i++) {
			TextView txt = (TextView) getLayoutInflater().inflate(
					R.layout.labelitem, null);
			txt.setText(contentllist.get(i));
			scrview_content.addView(txt,params);
		}
	}
	@Override
	public void onItemMenuClick(HorizontalScrollMyView scrollMenuView,
			View view, int position) {
		// TODO Auto-generated method stub
		if(position == 0 ){
			
		}else if(position == 1){
			doActivity(R.string.attentionlistactivity);
		}else if(position == 2){
			doActivity(R.string.beattentionedlistactivity);
		}
		
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if(arg2 == 4){
			//系统设置
			doActivity(R.string.xtszactivity);
		}else if(arg2 == 0){
			//我的资料
			doActivity(R.string.mydataactivity);
		}
	}

}
