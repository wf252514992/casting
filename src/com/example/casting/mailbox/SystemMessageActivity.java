package com.example.casting.mailbox;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dbhelper.DBHelper;
import com.example.casting.entity.MessageBean;
import com.example.casting.listener.JpushMessageRefreshListener;
import com.example.casting.listener.ListenerManager;
import com.example.casting.listener.SystemMessageRefreshListener;
import com.example.casting.login.BaseForm;
import com.example.casting_android.R;

public class SystemMessageActivity extends BaseForm implements SystemMessageRefreshListener{

	private DBHelper db;
	private PrivateletterAdapter adapter;
	private ListView systemmessagelist;
	private ArrayList<MessageBean> msgs;
	View titleLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.systemmessage); 
		titleLayout=findViewById(R.id.titlelayout);
		initView(titleLayout);
		setLeftButtonAble(true, "返回");
		setTitle("系统广播");
		ListenerManager.addSystemMessageListener(this);
		db=DBHelper.getInstance(this);
		msgs=db.getSystemMessage("1");
		//TODO 暂定1
		adapter=new PrivateletterAdapter(this,msgs);
		systemmessagelist=(ListView) findViewById(R.id.systemmessagelist);
		systemmessagelist.setAdapter(adapter);
	}
	
	class PrivateletterAdapter extends BaseAdapter{
		Context context;
		ArrayList<MessageBean> arrayList;

		public PrivateletterAdapter(Context context,ArrayList<MessageBean> arrayList){
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
		public View getView(int position, View v, ViewGroup group) {
			LayoutInflater inflater=LayoutInflater.from(context);
			v=inflater.inflate(R.layout.privateletter_item, null);
			
			ImageView othersidepic=(ImageView) v.findViewById(R.id.othersidepic);
			TextView othersidetime=(TextView) v.findViewById(R.id.othersidetime);
			TextView othermessagedetail = (TextView) v.findViewById(R.id.othermessagedetail);
//			ImageView mypic=(ImageView) v.findViewById(R.id.mypic);
//			TextView mytime=(TextView) v.findViewById(R.id.mytime);
//			TextView mymessagedetail = (TextView) v.findViewById(R.id.mymessagedetail);
			
			
			
			String time=arrayList.get(position).getSend_time();
			String contentStr=arrayList.get(position).getContent();
				if(position>0){
					if(!time.equals(arrayList.get(position-1).getSend_time())){
						othersidetime.setVisibility(View.VISIBLE);
						othersidetime.setText(time);
					}
				}else{
					othersidetime.setVisibility(View.VISIBLE);
					othersidetime.setText(time);
				}
				
				othermessagedetail.setVisibility(View.VISIBLE);
				othersidepic.setBackgroundResource(R.drawable.applogo);
				othersidepic.setVisibility(View.VISIBLE);
				
				othermessagedetail.setText(contentStr);
			
			
			return v;
		}

	}

	@Override
	public void LeftButtonClick() {
		finish();
	}

	@Override
	public void RightButtonClick() {
		
	}

	@Override
	public void refresh() {
		msgs=db.getSystemMessage("1");//TODO 暂定1
		adapter=new PrivateletterAdapter(this,msgs);
		systemmessagelist.setAdapter(adapter);
		
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ListenerManager.removeSystemMessageListener(this);
	}
}
