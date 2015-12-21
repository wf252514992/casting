package com.example.casting.mailbox;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dbhelper.DBHelper;
import com.example.casting.MainTabNew;
import com.example.casting.SignupedActivity;
import com.example.casting.entity.MessageBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.listener.JpushMessageRefreshListener;
import com.example.casting.listener.ListenerManager;
import com.example.casting.login.BaseForm;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.Users.UserGetProcessor;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.util.Server_path;
import com.example.casting.util.Session;
import com.example.casting_android.R;
import com.ta.common.TAStringUtils;
import com.ta.util.download.DownloadManager;

/**
 * 信箱主界面
 * @author chenjiaping
 *
 */
public class MailboxActivity extends BaseForm implements JpushMessageRefreshListener{
	private ArrayList<HashMap<String,Object>> list;
	private MailboxAdapter adapter;
	private DBHelper db;
	private ListView listview;
	private UserGetProcessor getPro;
	private String from_id,to_id;
//	private static MailboxActivity sInstance;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db=DBHelper.getInstance(MailboxActivity.this);
		setContentView(R.layout.mailbox);
		
		
		init();
		ListenerManager.addJpushMessageListener(this);
	}
	private void getdbmessage(){
		
		        ArrayList<MessageBean> msgs=db.getMailboxMessage();
		        if(msgs.size()>0){
		        	for (int i = 0; i < msgs.size(); i++) {
			        	MessageBean msg=msgs.get(i);
			        	HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("nickname",msg.getNickname());
						map.put("acceptNickname", msg.getAcceptNickname());
						map.put("from_id",msg.getFrom_id());
						map.put("to_id",msg.getTo_id());
						map.put("message",msg.getContent());
						String send_time=msg.getSend_time();
						map.put("time",send_time.substring(send_time.lastIndexOf(" ") + 1));
						map.put("pic", msg.getPicPath());
						map.put("number", "0"); //TODO 私信number处理
						map.put("isSiXin", "true");
						list.add(map);
					}
			        adapter.notifyDataSetChanged();
		        }
	}
	private void getUserMessage(String from_id,String to_id){
		getPro = new UserGetProcessor();
		RegistBean bean = new RegistBean();
		String id=Session.getInstance().getUser_id();
		if(from_id.equals(id)){
			id=to_id;
		}else if(to_id.equals(id)){
			id=from_id;
		}
		bean.setId(id);
			
		this.from_id=from_id;
		this.to_id=to_id;
		HttpCall(getPro, bean);
	}
//	public static MailboxActivity getInstance() {
//		if (null == sInstance) {
//			synchronized (MailboxActivity.class) {
//				if (sInstance == null) {
//					sInstance = new MailboxActivity();
//				}
//			}
//		}
//		return sInstance;
//	}
//	private void initJpushData(String head_portrait){
//		Intent intent = getIntent();
//		if (null != intent) {
//			Bundle bundle = getIntent().getExtras();
//			if (bundle != null) {
//				// String title =
//				// bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
//				String content = bundle.getString(JPushInterface.EXTRA_ALERT);
//				// {"from_id"："123","to_id":"124","type":"类型","content":"呵呵","send_time":"2015-01-29 09:10:33"}
//				try {
//					JSONObject jsonResult = new JSONObject(content);
//					String type = jsonResult.getString("type");
//					if (type != null && type.equals("1")) {
//						
//							HashMap<String, Object> map = new HashMap<String, Object>();
//							map.put("nickname",jsonResult.getString("nickname"));
//							map.put("from_id",jsonResult.getString("from_id"));
//							map.put("message",jsonResult.getString("content"));
//							String send_time=jsonResult.getString("send_time");
//							map.put("time",send_time.substring(send_time.lastIndexOf(" ") + 1));
//							map.put("pic", head_portrait);//TODO
//							map.put("number", "0"); //TODO 私信number处理
//							map.put("isSiXin", "true");
//							list.add(map);
//							MessageBean msg=new MessageBean();
//							msg.setFrom_id(jsonResult.getString("from_id"));
//							msg.setTo_id(jsonResult.getString("to_id"));
//							msg.setContent(jsonResult.getString("content"));
//							msg.setSend_time(jsonResult.getString("send_time"));
//							msg.setMyMessage("1");
//							msg.setNickname(jsonResult.getString("nickname"));
//			        		msg.setPicPath(head_portrait);//TODO
//			                db.insert(msg);
//			                
//							adapter.notifyDataSetChanged();
//						
//						} 
//				    
////					Button num = (Button) findViewById(R.id.num);
////					num.setBackgroundResource(R.drawable.alert_small);
//					// num.setText(number); TODO 所有的number加起来，包括私信
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			} 
//		}
//	}
	@Override
	protected void onResume() {
		super.onResume();
		init();
	}
	private void init(){
		//listview
		listview=(ListView)findViewById(R.id.mailboxlist
				);
		list=new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("content","@我的");
		map.put("pic", R.drawable.message_pic_contact);
		map.put("number", MainTabNew.atNumber);
		map.put("isSiXin", "false");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content","评论");
		map.put("pic", R.drawable.message_pic_comment);
		map.put("number", MainTabNew.reviewNumber);
		map.put("isSiXin", "false");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content","赞");
		map.put("pic", R.drawable.message_pic_unlike);
		map.put("number", MainTabNew.praiseNumber);
		map.put("isSiXin", "false");
		list.add(map);
		final String usertype=Session.getInstance().getUsertype();//用户类型:普通用户和导演(1/2)
		//招募方有报名者，被招募方没有报名者
		if(usertype!=null&&usertype.equals("1")){
			
		}else{
		map = new HashMap<String, Object>();
		map.put("content","报名者");
		map.put("pic", R.drawable.entrants);
		map.put("number", MainTabNew.recruitNumber); 
		map.put("isSiXin", "false");
		list.add(map);
		}
		map = new HashMap<String, Object>();
		map.put("content","系统广播");
		map.put("pic", R.drawable.systemicon);
		map.put("number", 0); 
		map.put("isSiXin", "false");
		list.add(map);
		adapter=new MailboxAdapter(MailboxActivity.this, list);
		listview.setAdapter(adapter);
		getdbmessage();
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				System.out.println("...list:"+list);
				
				if (position == 0) {// @我的
                   Intent intent=new Intent(MailboxActivity.this,AtListActivity.class);
                   startActivity(intent);
				}else if (position == 1) {// 评论
					 Intent intent=new Intent(MailboxActivity.this,ReviewOrPraiseListActivity.class);
	                 intent.putExtra("op", "Review"); 
					 startActivity(intent);
					

				}else if (position == 2) {// 赞
					 Intent intent=new Intent(MailboxActivity.this,ReviewOrPraiseListActivity.class);
					 intent.putExtra("op", "Praise");  
					 startActivity(intent);
					
				}else if (position == 3) {// 报名者
					if(usertype!=null&&usertype.equals("1")){
						// 系统广播
						Intent intent=new Intent(MailboxActivity.this,SystemMessageActivity.class);
//						intent.putExtra("action", "systembroadcast");
		                startActivity(intent);
					}else{
					   Intent intent=new Intent(MailboxActivity.this,SignupedActivity.class);
					   intent.putExtra("action", "all");
	                   startActivity(intent);
					}
				}else if (usertype!=null&&!usertype.equals("1")&&position == 4) {// 系统广播
					Intent intent=new Intent(MailboxActivity.this,SystemMessageActivity.class);
//					intent.putExtra("action", "systembroadcast");
	                startActivity(intent);
				}else if(list.get(position).get("from_id")!=null
						&&!list.get(position).get("from_id").toString().equals("")&&position == 4){// 私信
                    Intent intent=new Intent(MailboxActivity.this,PrivateletterActivity.class);
                    intent.putExtra("bean", list.get(position));
                    startActivity(intent);
				}else if(list.get(position).get("from_id")!=null
						&&!list.get(position).get("from_id").toString().equals("")){// 私信
                    System.out.println("..........私信");
                    Intent intent=new Intent(MailboxActivity.this,PrivateletterActivity.class);
                    intent.putExtra("bean", list.get(position));
                    startActivity(intent);
				}
			}
		});
		
	}
	class MailboxAdapter extends BaseAdapter {
		Context context;
		ArrayList<HashMap<String, Object>> arrayList;

		public MailboxAdapter(Context context,
				ArrayList<HashMap<String, Object>> arrayList) {
			super();
			this.context = context;
			this.arrayList = arrayList;

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
			LayoutInflater inflater = LayoutInflater.from(context);
			v = inflater.inflate(R.layout.mailbox_item, null);

			ImageView pic = (ImageView) v.findViewById(R.id.pic);
			// isSiXin为true时（私信）
			TextView name = (TextView) v.findViewById(R.id.name);
			TextView message = (TextView) v.findViewById(R.id.message);
			TextView time = (TextView) v.findViewById(R.id.time);
			// isSiXin为false时
			TextView content = (TextView) v.findViewById(R.id.content);
			TextView num = (TextView) v.findViewById(R.id.num);
			RelativeLayout redhot = (RelativeLayout) v
					.findViewById(R.id.redhot);
			
			if (arrayList.get(position).get("pic") != null) {
				// TODO 框架图片加载
				if (arrayList.get(position).get("isSiXin").toString()
						.equals("true")) {
					if(!arrayList.get(position).get("pic").toString().equals("")
									&&!arrayList.get(position).get("pic").toString().equals("null")){
						 pic.setImageBitmap(BitmapFactory.decodeFile(arrayList.get(position)
									.get("pic").toString()));
					}else{
							getUserMessage(arrayList.get(position).get("from_id").toString(), 
									arrayList.get(position).get("to_id").toString());
					}
				   
				}else{
					pic.setImageResource(Integer.parseInt(arrayList.get(position)
							.get("pic").toString()));
				}
			}

			if (arrayList.get(position).get("isSiXin").toString()
					.equals("true")) {
				
				name.setVisibility(View.VISIBLE);
				message.setVisibility(View.VISIBLE);
				time.setVisibility(View.VISIBLE);
				String nickName=arrayList.get(position).get("nickname").toString();
				String acceptNickname=arrayList.get(position).get("acceptNickname").toString();
				String nickN=Session.getInstance().getName_nick();
				if(!nickName.equals("")&&nickName.equals(nickN)){
					name.setText(acceptNickname);
				}else if(acceptNickname.equals(nickN)){
					name.setText(nickName);
				}
				
				message.setText(arrayList.get(position).get("message")
						.toString());
				time.setText(arrayList.get(position).get("time").toString());
				int number =db.getUnRead(arrayList.get(position).get("from_id").toString(), arrayList.get(position).get("to_id").toString());
				if (number != 0) {
					num.setVisibility(View.VISIBLE);
					redhot.setVisibility(View.VISIBLE);
					num.setBackgroundResource(R.drawable.alert_small);
					num.setText(number+"");
				}
			}
			content.setVisibility(View.VISIBLE);

			if (!arrayList.get(position).get("isSiXin").toString()
					.equals("true")) {
				content.setText(arrayList.get(position).get("content")
						.toString());
			}
			

			return v;
		}

	}
	@Override
	public void LeftButtonClick() {
		
	}
	@Override
	public void RightButtonClick() {
		
	}
	@Override
	public void OnReturn(String content, IBaseProcess processor) {
		// TODO Auto-generated method stub
		super.OnReturn(content, processor);
		ResultBean result = processor.json2Bean(content);
		if (processor.getProcessorId().equals(getPro.getProcessorId())) {
			if (result.getCode() == Errors.Regist_USER_ERROR) {
				// 用户不存在，则可以注册
				Toast.makeText(this, "用户不存在", Toast.LENGTH_SHORT).show();
			} else {

				if (result.getCode() == Errors.OK) {

					RegistBean regist = (RegistBean) result.getObj();
					String head_portrait=regist.getHead_portrait();
					String nickName=regist.getNickname();
					head_portrait = DownloadManager.FILE_ROOT
							+ TAStringUtils
									.getFileNameFromUrl(Server_path.serverfile_path
											+ head_portrait);
					boolean flag=db.update(this.from_id,this.to_id,head_portrait,nickName);
					File file=new File(head_portrait);
					if(flag&&file.exists()){
						
						init();
					}
					
				}
			}
		}	
	}
	@Override
	public void refresh() {
		//TODO
//		getUserMessage();
		init();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ListenerManager.removeJpushMessageListener(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		} else
			return super.onKeyDown(keyCode, event);
	}
}
