package com.example.casting.me;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.casting.entity.AttentionBean;
import com.example.casting.login.BaseForm;
import com.example.casting.me.adapter.AttentionAdapter;
import com.example.casting_android.R;
import com.example.casting_android.bean.ImageBean;
import com.example.casting_android.bean.UserInfoBean;

public class JoinedListActivity extends BaseForm  {
	View titleLayout;
	ListView lstview_attention;
	AttentionAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attentionlist);
		titleLayout = (View)findViewById(R.id.titlelayout);
		lstview_attention = (ListView)findViewById(R.id.lstview_attention);
		initView(titleLayout);
		setLeftButtonAble(true, "返回");
		setTitle("已报名");
		initFans();
	}
	private void initFans(){
		ArrayList<AttentionBean> users = new ArrayList<AttentionBean>();
		AttentionBean bean = new AttentionBean();
		bean.setNickname("范冰冰");
//		bean.setJJ("互粉无法绯闻绯闻绯闻绯闻服务费为服务费");
		ImageBean img = new ImageBean(ImageBean.type_res,R.drawable.ic_launcher+"");
		bean.setImgbean(img);
		users.add(bean);
		users.add(bean);
		users.add(bean);
		users.add(bean);
		AttentionAdapter adapter = new AttentionAdapter(this,users){

			@Override
			public void addAttention(AttentionBean bean) {
				// TODO Auto-generated method stub
				
			}
			
		};
		lstview_attention.setAdapter(adapter);
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
