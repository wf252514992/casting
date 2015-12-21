package com.example.casting.mailbox;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.dbhelper.DBHelper;
import com.example.casting.entity.MessageBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.listener.JpushMessageRefreshListener;
import com.example.casting.listener.ListenerManager;
import com.example.casting.listener.SystemMessageRefreshListener;
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
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.RequestParams;

/**
 * 私信聊天界面
 * @author chenjiaping
 *
 */
public class PrivateletterActivity extends BaseForm  implements OnClickListener, JpushMessageRefreshListener,SystemMessageRefreshListener{
//	@TAInjectView(id = R.id.titlelayout)
	View titleLayout;
	private AsyncHttpClient asyncHttpClient;
	private EditText message;
	private ListView listview;
	private DBHelper db;
	private ArrayList<MessageBean> list;
	private PrivateletterAdapter adapter;
	private String   sendDate;
	private  HashMap<String, Object> bean;  
//	private String from_id,to_id;
	private UserGetProcessor getPro;
	private String head_portrait;
	private String otherhead_portrait;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.privateletter); 
		titleLayout=findViewById(R.id.titlelayout);
		initView(titleLayout);
		setLeftButtonAble(true, "返回");
		String myNickName=Session.getInstance().getName_nick();
		
		Intent intent=getIntent();
		bean=(HashMap<String, Object>) intent.getSerializableExtra("bean");
		if(bean!=null){
			getUserMessage(myNickName);
//			from_id=bean.get("from_id").toString();
//			to_id=bean.get("to_id").toString();
			String nickName=bean.get("nickname").toString();
			String acceptNickname=bean.get("acceptNickname").toString();
			String nickN=Session.getInstance().getName_nick();
			if(nickName.equals(nickN)){
				setTitle(acceptNickname);
			}else if(acceptNickname.equals(nickN)){
				setTitle(nickName);
			}
			init();
		} 
		
		ListenerManager.addJpushMessageListener(this);
		ListenerManager.addSystemMessageListener(this);
		asyncHttpClient=new AsyncHttpClient();
	}

	private void init(){ 
		message=(EditText) findViewById(R.id.message);
		Button btn_send=(Button) findViewById(R.id.btn_send);
		btn_send.setOnClickListener(this);
		listview=(ListView) findViewById(R.id.privateletterlist);
		db=DBHelper.getInstance(PrivateletterActivity.this);
		
	    list=db.getMessage(bean.get("from_id").toString(), bean.get("to_id").toString());
		//TODO
		adapter=new PrivateletterAdapter(PrivateletterActivity.this, list);
		listview.setAdapter(adapter);	
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
			ImageView mypic=(ImageView) v.findViewById(R.id.mypic);
			TextView mytime=(TextView) v.findViewById(R.id.mytime);
			TextView mymessagedetail = (TextView) v.findViewById(R.id.mymessagedetail);
			
			
			
			String time=arrayList.get(position).getSend_time();
			if((arrayList.get(position).isMyMessage()!=null&&arrayList.get(position).isMyMessage().equals("0"))){
				if(head_portrait!=null){
					mypic.setImageBitmap(BitmapFactory.decodeFile(head_portrait));
				}
				if(position>0){
					if(!time.equals(arrayList.get(position-1).getSend_time())){
						mytime.setVisibility(View.VISIBLE);
						mytime.setText(time);
					}
				}else{
					mytime.setVisibility(View.VISIBLE);
					mytime.setText(time);
				}
				
				mymessagedetail.setVisibility(View.VISIBLE);
				mypic.setVisibility(View.VISIBLE);
				
				mymessagedetail.setText(arrayList.get(position).getContent());
			}else{
				othersidepic.setImageBitmap(BitmapFactory.decodeFile(arrayList.get(position).getPicPath()));
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
				othersidepic.setVisibility(View.VISIBLE);
				
				othermessagedetail.setText(arrayList.get(position).getContent());
			
			}
			
			return v;
		}

	}
	@Override
	public void LeftButtonClick() {
//		Intent intent = new Intent(this, MainTab.class);
////		intent.putExtra("action", "mail");
//		startActivity(intent);
		finish();
	}

	@Override
	public void RightButtonClick() {

	}

	private void getUserMessage(String nickName){
		getPro = new UserGetProcessor();
		RegistBean bean = new RegistBean();
		bean.setNickname(nickName);
			
		HttpCall(getPro, bean);
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
					head_portrait=regist.getHead_portrait();
					head_portrait = DownloadManager.FILE_ROOT
							+ TAStringUtils
									.getFileNameFromUrl(Server_path.serverfile_path
											+ head_portrait);
					File file=new File(head_portrait);
					if(file.exists()){
						adapter.notifyDataSetChanged();
					
					}
					
				}
			}
		}	
	}
	private void send(final MessageBean msg)
    {
        JSONObject params = new JSONObject();
        RequestParams param = new RequestParams();
        try
        {
            params.put("from_id", msg.getFrom_id());
            params.put("to_id", msg.getTo_id());
            params.put("content", msg.getContent());
            params.put("type", "1");
            params.put("send_time", msg.getSend_time());
            params.put("nickname", Session.getInstance().getName_nick());
            params.put("acceptNickname", bean.get("nickname").toString());
            params.put("pic", msg.getPicPath());
            msg.setNickname(Session.getInstance().getName_nick());
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        param.put("str", params.toString());
        param.put("response", "application/json");
        asyncHttpClient.post(Server_path.sendLetter, param, new AsyncHttpResponseHandler() {

            public void onSuccess(String content)
            {
                super.onSuccess(content);
            
        		msg.setMyMessage("0");
        	    otherhead_portrait=bean.get("pic").toString();
        		otherhead_portrait = DownloadManager.FILE_ROOT
						+ TAStringUtils
								.getFileNameFromUrl(Server_path.serverfile_path
										+ otherhead_portrait);
        		msg.setPicPath(otherhead_portrait);
        		msg.setAcceptNickname(bean.get("acceptNickname").toString());
                boolean flag=db.insert(msg);
                list.add(msg);
                adapter.notifyDataSetChanged();
            }

        });
    }
	/**
	 * 如果现在时间比之前时间大于2分钟返回1，否则返回-1
	 * @param dateStr 现在时间   格式：yy-MM-dd hh:mm
	 * @param sendDateStr 之前时间  格式：yy-MM-dd hh:mm
	 * @return
	 */
     private  int compare_date(String dateStr, String sendDateStr) {
        DateFormat df = new SimpleDateFormat("yy-MM-dd hh:mm");
        try {
            Date date = df.parse(dateStr);
            Date sendDate = df.parse(sendDateStr);
            if (date.getTime()-2 > sendDate.getTime()) {
                return 1;
            } else if (date.getTime()-2 < sendDate.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_send){
			String messageStr=message.getText().toString();
			if(messageStr.equals("")){
				return ;
			}
			MessageBean msg=new MessageBean();
			String from_id = Session.getInstance().getUser_id();
			msg.setFrom_id(from_id);
			String dbfrom_id=bean.get("from_id").toString();
			String dbto_id=bean.get("to_id").toString();
			String to_id = null;
			if(dbfrom_id.equals(from_id)){
				to_id=dbto_id;
			}
			if(dbto_id.equals(from_id)){
				to_id=dbfrom_id;
			}
			if(to_id!=null){
				msg.setTo_id(to_id);//TODO
			}
			
			msg.setContent(messageStr);
		    //2分钟之内显示同一个时间
    		SimpleDateFormat   sDateFormat   =   new   SimpleDateFormat("yy-MM-dd   hh:mm");     
    		String   date   =   sDateFormat.format(new   java.util.Date());  
    		if(sendDate!=null){
    			//比较sendDate和date，小于2分钟则使用sendDate，大于或等于2分钟则使用date
    			int i=compare_date(date, sendDate);
    			if(i==1){//大于2分钟
    				sendDate=date;
    			}else if(i==-1){
    				
    			}
    		}else{
    			sendDate=date;
    		}
    		otherhead_portrait=bean.get("pic").toString();
//    		otherhead_portrait = DownloadManager.FILE_ROOT
//					+ TAStringUtils
//							.getFileNameFromUrl(Server_path.serverfile_path
//									+ otherhead_portrait);
    		msg.setPicPath(otherhead_portrait);
    		msg.setSend_time(sendDate);
			send(msg);
			message.setText("");
		}
		
	}

	@Override
	public void refresh() {
		list=db.getMessage(bean.get("from_id").toString(), bean.get("to_id").toString());
		//TODO
		adapter=new PrivateletterAdapter(PrivateletterActivity.this, list);
		listview.setAdapter(adapter);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ListenerManager.removeJpushMessageListener(this);
		ListenerManager.removeSystemMessageListener(this);
	}

}
