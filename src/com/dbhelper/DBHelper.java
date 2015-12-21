package com.dbhelper;

import java.util.ArrayList;
import com.example.casting.entity.MessageBean;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private final static String NAME = "casting";
	private final static int VERSION = 5;
	private SQLiteDatabase db;
	private static DBHelper instance = null;

	public static DBHelper getInstance(Context context) {
		if (instance == null) {
			instance = new DBHelper(context);
		}
		return instance;
	}

	public DBHelper(Context context) {
		super(context, NAME, null, VERSION);
	}

	private void openDatabase() {
		if (db == null) {
			db = this.getWritableDatabase();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

//		 {"from_id"："123","to_id":"124","type":"类型","content":"呵呵","send_time":"2015-01-29 09:10:33"}
		String sql = "create table Message( "
				+ "messageId Integer primary key autoincrement,"
				+"nickname varchar(60)," 
				+"acceptNickname varchar(60)," 
				+ "from_id varchar(60) not null,"
				+ "to_id varchar(60) not null," +
//				"type varchar(60)," +
				"content varchar(60) ," +
				"send_time varchar(60)," +
				"isMyMessage varchar(60)," +
				"isRead varchar(60)," +
				"picPath varchar(60))";
		db.execSQL(sql);
		String sql2 = "create table MailboxMessage( "
				+ "messageId Integer primary key autoincrement,"
				+"nickname varchar(60)," 
				+"acceptNickname varchar(60)," 
				+ "from_id varchar(60) not null,"
				+ "to_id varchar(60) not null," +
//				"type varchar(60)," +
				"content varchar(60) ," +
				"send_time varchar(60)," +
				"isMyMessage varchar(60)," +
				"picPath varchar(60))";
		db.execSQL(sql2);
		String sql3 = "create table SystemMessage( "
				+ "messageId Integer primary key autoincrement,"+
				"id varchar(60)," +
				"content varchar(60) ," +
				"send_time varchar(60)" +
				")";
		db.execSQL(sql3);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {

		String sql = "drop table if exists Message";

		db.execSQL(sql);
		String sql2 = "drop table if exists MailboxMessage";

		db.execSQL(sql2);
		String sql3 = "drop table if exists SystemMessage";

		db.execSQL(sql3);
		onCreate(db);
 
	}
	public boolean insert(MessageBean msg){
		openDatabase();
		ContentValues values = new ContentValues();
		values.put("from_id", msg.getFrom_id());
		values.put("to_id", msg.getTo_id());
//		values.put("type", msg.getType());
		values.put("content", msg.getContent());
		values.put("send_time", msg.getSend_time());
		values.put("isMyMessage", msg.isMyMessage());
		values.put("picPath", msg.getPicPath());
		values.put("nickname", msg.getNickname());
		values.put("acceptNickname",msg.getAcceptNickname());
		values.put("isRead","0");
		
		long id = db.insert("Message", null, values);
		if (id > 0) {
			boolean flag=HasUserMessage(msg.getFrom_id(), msg.getTo_id());
			boolean insertFlag;
			if(flag){//有消息則更新到最新消息
				insertFlag=updateMailboxMessage(msg);
			}else{//沒有消息則插入消息
				insertFlag=insertMailboxMessage(msg);
			}
			
			if(insertFlag){
				return true;
			}else{
			    return false;
			}
		} else {
			return false;
		}
	}
	/**
	 * 更新非本人的头像，如果我主动发私信，则把对方的图片路径传过来；如果别人给我发私信，则调用获取个人资料接口得到其头像路径
	 * @param from_id
	 * @param picPath
	 * @return
	 */
	public boolean update(String from_id,String to_id,String picPath,String nickName){
		openDatabase();
		ContentValues values = new ContentValues();
		values.put("picPath", picPath);
		if(!nickName.equals("")){
			values.put("nickname",nickName);
		}
		int i=db.update("MailboxMessage", values, "(from_id = ? and to_id = ?) or (to_id = ? and from_id = ?)",
				new String[] { from_id ,to_id,from_id ,to_id});
		int j=db.update("Message", values, "(from_id = ? and to_id = ?) or (to_id = ? and from_id = ?)",
				new String[] { from_id ,to_id,from_id ,to_id});
	    if(i>0&&j>0){
	    	return true;
	    }else{
	    	return false;
	    }
	}
	
//	public String getPic(String from_id,String to_id){
//		openDatabase();
//		Cursor c=db.query("Message", null, "from_id =  ? or to_id = ?", new String[]{from_id,to_id},null,null,null);
//		String pic="";
//		if(c.moveToFirst()){
//	    	pic=c.getString(c.getColumnIndex("picPath"));
//	    }
//	    return pic;
//	}
	
	
	public boolean insertMailboxMessage(MessageBean msg){
//		int i=db.delete("MailboxMessage", "(from_id = ? and to_id = ?) or (to_id = ? and from_id = ?)",
//				new String[] { msg.getFrom_id() ,msg.getTo_id(),msg.getFrom_id() ,msg.getTo_id()});
//		if(i>0){
			ContentValues values = new ContentValues();
			values.put("from_id", msg.getFrom_id());
			values.put("to_id", msg.getTo_id());
//			values.put("type", msg.getType());
			values.put("content", msg.getContent());
			values.put("send_time", msg.getSend_time());
			values.put("isMyMessage", msg.isMyMessage());
			values.put("picPath", msg.getPicPath());
			values.put("nickname", msg.getNickname());
			values.put("acceptNickname",msg.getAcceptNickname());
			long id = db.insert("MailboxMessage", null, values);
			if (id > 0) {
				return true;
			} else {
				return false;
			}
//		}
//		return false;
	}
	
	public boolean updateMailboxMessage(MessageBean msg){
		openDatabase();
		
		ContentValues values = new ContentValues();
		values.put("from_id", msg.getFrom_id());
		values.put("to_id", msg.getTo_id());
//		values.put("type", msg.getType());
		values.put("content", msg.getContent());
		values.put("send_time", msg.getSend_time());
		values.put("isMyMessage", msg.isMyMessage());
		values.put("picPath", msg.getPicPath());
		values.put("nickname", msg.getNickname());
		values.put("acceptNickname",msg.getAcceptNickname());
		int i=db.update("MailboxMessage", values, "(from_id = ? and to_id = ?) or (to_id = ? and from_id = ?)",
				new String[] { msg.getFrom_id() ,msg.getTo_id(),msg.getFrom_id() ,msg.getTo_id()});
	    if(i>0){
	    	return true;
	    }else{
	    	return false;
	    }
	}
	
			public ArrayList<MessageBean> getMailboxMessage(){
				openDatabase();
				ArrayList<MessageBean> list = new ArrayList<MessageBean>();
				Cursor c = db.query("MailboxMessage", null, null,
						null, null, null, null, null);
				if (c != null) {
					if (c.moveToFirst()) {
						while (!c.isAfterLast()) {
							try {
//								int cols = c.getColumnCount();
								MessageBean msg = new MessageBean();
//								for (int i = 0; i < cols; i++) {
									String from_id = c.getString(c.getColumnIndex("from_id"));
									String to_id = c.getString(c.getColumnIndex("to_id"));
				                 	String content = c.getString(c.getColumnIndex("content"));
					                String send_time = c.getString(c.getColumnIndex("send_time"));
					                String isMyMessage=c.getString(c.getColumnIndex("isMyMessage"));
					                String picPath=c.getString(c.getColumnIndex("picPath"));
				                 	String nickName=c.getString(c.getColumnIndex("nickname"));
				                 	String acceptNickname=c.getString(c.getColumnIndex("acceptNickname"));
				                 	msg.setFrom_id(from_id);
				                 	msg.setTo_id(to_id);
				                 	msg.setContent(content);
			                		msg.setSend_time(send_time);
			                		msg.setMyMessage(isMyMessage);
				                	msg.setPicPath(picPath);
				                	msg.setNickname(nickName);
				                	msg.setAcceptNickname(acceptNickname);
//								}
								list.add(msg);
								c.moveToNext();
							} catch (Exception ex) {
								break;
							}
						}
					}
					if (c != null && !c.isClosed())
						c.close();
				}
				return list;
			}
			public boolean HasUserMessage(String from_id,String to_id){
				openDatabase();
				Cursor c = db.query("MailboxMessage", null, "(from_id = ? and to_id = ?) or (to_id = ? and from_id = ?)",
						new String[] { from_id ,to_id,from_id ,to_id}, null, null, null, null);
				if (c == null) {
					return false;
				}
				if(c.moveToFirst()){
					return true;
					}else {
						return false;
					}
			}
	
	public ArrayList<MessageBean> getMessage(String from_id,String to_id){
		openDatabase();
		ArrayList<MessageBean> list = new ArrayList<MessageBean>();
		Cursor cursor = db.query("Message", null, "(from_id = ? and to_id = ?) or (to_id = ? and from_id = ?)",
				new String[] { from_id ,to_id,from_id ,to_id}, null, null, null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				while (!cursor.isAfterLast()) {
					try {
//						int cols = cursor.getColumnCount();
						MessageBean msg = new MessageBean();
//						for (int i = 0; i < cols; i++) {
							from_id = cursor.getString(cursor.getColumnIndex("from_id"));
							to_id = cursor.getString(cursor.getColumnIndex("to_id"));
							String content = cursor.getString(cursor.getColumnIndex("content"));
							String send_time = cursor.getString(cursor.getColumnIndex("send_time"));
							String isMyMessage=cursor.getString(cursor.getColumnIndex("isMyMessage"));
							String picPath=cursor.getString(cursor.getColumnIndex("picPath"));
							String nickName=cursor.getString(cursor.getColumnIndex("nickname"));
							String acceptNickname=cursor.getString(cursor.getColumnIndex("acceptNickname"));
							msg.setFrom_id(from_id);
		                 	msg.setTo_id(to_id);
							msg.setContent(content);
							msg.setSend_time(send_time);
							msg.setMyMessage(isMyMessage);
							msg.setPicPath(picPath);
							msg.setNickname(nickName);
						    msg.setAcceptNickname(acceptNickname);
//						}
						list.add(msg);
						cursor.moveToNext();
					} catch (Exception ex) {
						break;
					}
				}
				setReaded(from_id, to_id);
			}
			if (cursor != null && !cursor.isClosed())
				cursor.close();
		}
		return list;
	}
	public boolean setReaded(String from_id,String to_id){
		openDatabase();
		ContentValues values = new ContentValues();
		values.put("isRead", "1");
		int i=db.update("Message", values, "(from_id = ? and to_id = ?) or (to_id = ? and from_id = ?)",
				new String[] { from_id ,to_id,from_id ,to_id});
	    if(i>0){
	    	return true;
	    }else{
	    	return false;
	    }
	}
	public int getUnRead(String from_id,String to_id){
		Cursor cursor = db.query("Message", null, "(from_id = ? and to_id = ? and isRead = ?) or (to_id = ? and from_id = ? and isRead = ?)",
				new String[] { from_id ,to_id,"0",from_id ,to_id,"0"}, null, null, null, null);
	    int count=0;
		if (cursor != null) {
			count=cursor.getCount();
		}
		return count;
	}

	public boolean insertSystemMessage(MessageBean msg){
		    openDatabase();
			ContentValues values = new ContentValues();
			values.put("content", msg.getContent());
			values.put("send_time", msg.getSend_time());
			values.put("id", msg.getId());
			long id = db.insert("SystemMessage", null, values);
			if (id > 0) {
				return true;
			} else {
				return false;
			}
	}
	public ArrayList<MessageBean> getSystemMessage(String id){
		openDatabase();
		ArrayList<MessageBean> list = new ArrayList<MessageBean>();
		Cursor c = db.query("SystemMessage", null, "id = ?",
				new String[] {id}, null, null, null, null);
//		String sql="select * from Message where from_id = "+from_id+"and and to_id = "+to_id;
		if (c == null) {
			return null;
		}
		if (c != null) {
			if (c.moveToFirst()) {
				while (!c.isAfterLast()) {

					MessageBean msg = new MessageBean();
					String content = c.getString(c.getColumnIndex("content"));
					String send_time = c.getString(c
							.getColumnIndex("send_time"));
					msg.setContent(content);
					msg.setSend_time(send_time);
					list.add(msg);
					c.moveToNext();
				}

			}
		}
//		while (c.moveToNext()) {}
		c.close();
		return list;
	}
	
}
