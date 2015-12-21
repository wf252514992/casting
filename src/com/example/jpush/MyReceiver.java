package com.example.jpush;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.dbhelper.DBHelper;
import com.example.casting.MainTabNew;
import com.example.casting.entity.MessageBean;
import com.example.casting.listener.ListenerManager;
import com.example.casting.mailbox.MailboxActivity;
import com.example.casting.util.Session;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	public static String from_id;

	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
                        
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        	processCustomMessage(context, bundle);
        
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
        	

            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            //TODO  处理json格式，返回格式为// 
//          {"from_id"："123","to_id":"124","type":"类型","content":"呵呵","send_time":"2015-01-29 09:10:33"}
        	//就私信显示content的吧,其它就显示有新评论或赞,有人赞了你,有人评论了你  TODO 通知栏怎么显示

            //打开自定义的Activity
            
            
			String type = null;
			JSONObject jsonResult = null; 
			DBHelper db=DBHelper.getInstance(context);
			  try {
				jsonResult = new JSONObject(bundle.getString(JPushInterface.EXTRA_ALERT));
				 type = jsonResult.getString("type");
				 //私信1
	            if(type!=null&&type.equals("1")){
//	            	id=Session.getInstance().getUser_id();
	            	from_id=jsonResult.getString("from_id");
	            	String to_id=jsonResult.getString("to_id");
//	            	if(!from_id.equals(id)&&to_id.equals(id)){
//	            	 i = new Intent(context, MailboxActivity.class);
//	            	}
	            	
	            	MessageBean msg=new MessageBean();
					msg.setFrom_id(jsonResult.getString("from_id"));
					msg.setTo_id(jsonResult.getString("to_id"));
					msg.setContent(jsonResult.getString("content"));
					msg.setSend_time(jsonResult.getString("send_time"));
					msg.setMyMessage("1");
					msg.setNickname("");
					
					msg.setAcceptNickname(Session.getInstance().getName_nick());
	        		msg.setPicPath("");//TODO
	                boolean flag=db.insert(msg);
	                if(flag){
	                	 ListenerManager.notifyJpushMessageActivity();
	                	 NotificationManager notiManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE); 
	                	 notiManager.cancel(notifactionId);
	                }
	            	
	            }
				
			  } catch (JSONException e) {

				 MessageBean msg=new MessageBean();
				 msg.setId("1");
				 msg.setContent(bundle.getString(JPushInterface.EXTRA_ALERT));
				 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
				 // new Date()为获取当前系统时间
				 msg.setSend_time(df.format(new Date()));
				 boolean flag=db.insertSystemMessage(msg);
				 if(flag){
					 ListenerManager.notifySystemMessageActivity();
					 NotificationManager notiManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE); 
               	 notiManager.cancel(notifactionId);
				 }
				 
			 
				e.printStackTrace();
			}
			
            
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            //TODO  处理json格式，返回格式为// 
//          {"from_id"："123","to_id":"124","type":"类型","content":"呵呵","send_time":"2015-01-29 09:10:33"}
        	//就私信显示content的吧,其它就显示有新评论或赞,有人赞了你,有人评论了你  TODO 通知栏怎么显示

            //打开自定义的Activity
            
            
			String type = null;
			JSONObject jsonResult; 
			try {
				jsonResult = new JSONObject(bundle.getString(JPushInterface.EXTRA_ALERT));
				type = jsonResult.getString("type");
			
			 Intent i = null; 
			 String id=jsonResult.getString("id");
			 DBHelper db=DBHelper.getInstance(context);
			 if(id!=null&&id.equals("1")){
				 MessageBean msg=new MessageBean();
				 msg.setId(id);
				 msg.setContent(jsonResult.getString("content"));
				 msg.setSend_time(jsonResult.getString("send_time"));
				 boolean flag=db.insertSystemMessage(msg);
				 ListenerManager.notifyJpushMessageActivity();
			 }
			 //私信1
            if(type!=null&&type.equals("1")){
            	id=Session.getInstance().getUser_id();
            	from_id=jsonResult.getString("from_id");
            	String to_id=jsonResult.getString("to_id");
            	if(!from_id.equals(id)&&to_id.equals(id)){
            	 i = new Intent(context, MailboxActivity.class);
            	}
            	
            	MessageBean msg=new MessageBean();
				msg.setFrom_id(jsonResult.getString("from_id"));
				msg.setTo_id(jsonResult.getString("to_id"));
				msg.setContent(jsonResult.getString("content"));
				msg.setSend_time(jsonResult.getString("send_time"));
				msg.setMyMessage("1");
				msg.setNickname(jsonResult.getString("nickname"));
				msg.setAcceptNickname(jsonResult.getString("acceptNickname"));
        		msg.setPicPath("");//TODO
                boolean flag=db.insert(msg);
                if(flag){
                	 ListenerManager.notifyJpushMessageActivity();
                }
            	
            }else{
            	 i = new Intent(context, MainTabNew.class);
            }
           if(i!=null){
        	   i.putExtras(bundle);
           	//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
           	context.startActivity(i); 
           }
        	
			} catch (JSONException e) {
				e.printStackTrace();
			}
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        	
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        	Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
        	Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} 
			else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	
	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		if (MainActivity.isForeground) {
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
			if (!ExampleUtil.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (null != extraJson && extraJson.length() > 0) {
						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			context.sendBroadcast(msgIntent);
		}
	}
}
