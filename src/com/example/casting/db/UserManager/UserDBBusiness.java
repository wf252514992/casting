package com.example.casting.db.UserManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;

import com.example.casting.db.DBBusiness;
import com.example.casting.entity.RegistBean;

public class UserDBBusiness extends DBBusiness{

	
	public static final String TableName = "USERINFO";
	public UserDBBusiness(Context context) {
		super(context);
	}

	
	/**
	 * 新增用户
	 * @param registbean
	 * @param pwd
	 */
	public void InserNewUser(RegistBean registbean){
		int num = getUserNum(registbean);
		if(num>1){
			//如果存在用户，则更新用户信息
			updateUser(registbean);
			return;
		}
		//用户id不存在 则新增用户
		ContentValues values = new ContentValues();
		values.put(Column_USERNAME, registbean.getName());
		values.put(Column_NICKNAME, registbean.getNickname());
		values.put(Column_USERTYPE, registbean.getType());
		values.put(Column_USERID,registbean.getId());
		values.put(Column_HEADPORTRAIT, registbean.getHead_portrait());
		values.put(Column_PASSWORD, registbean.getPassword());
		values.put(Column_PHONENUM, registbean.getPhone());
		values.put(Column_LOGINTAG, "0");
		executeInsert(TableName, null, values);
	}
	/**
	 * @param registbean
	 */
	public void updateUser(RegistBean registbean){
		String whereClause = Column_USERID+" =? ";
		String[] whereArgs = {registbean.getId()};
		ContentValues values = new ContentValues();
		values.put(Column_USERNAME, registbean.getName());
		values.put(Column_NICKNAME, registbean.getNickname());
		values.put(Column_USERTYPE, registbean.getType());
		values.put(Column_USERID,registbean.getId());
		values.put(Column_HEADPORTRAIT, registbean.getHead_portrait());
		values.put(Column_PASSWORD, registbean.getPassword());
		values.put(Column_PHONENUM, registbean.getPhone());
		values.put(Column_LOGINTAG, "0");
		executeUpdate(TableName, values, whereClause, whereArgs);
	}

	/**
	 * 获取用户数量
	 * @param registbean
	 * @return
	 */
	public int  getUserNum(RegistBean registbean ){
		String sql = "select * from "+TableName+" where "+ Column_USERID+"="+registbean.getId();
//		String[] columns = {" count(*) "};
//		String selection = Column_USERID+"=？ ";
//		String[] selectionArgs = {registbean.getId()};
//		ArrayList<HashMap<String, String>> list = getArrayList(TableName, columns, selection, selectionArgs, null, null, null);
		ArrayList<HashMap<String, String>> list = getArrayListBySQl(sql);
		return list.size();
	}
	
	/**
	 * 搜索所有在本机上登录过的用户信息
	 * @return
	 */
	public List<RegistBean> getAllUser( ){
		String sql ="select * from "+TableName ;
		ArrayList<HashMap<String, String>> list = getArrayListBySQl(sql);
		List<RegistBean> mylist = new ArrayList<RegistBean>();
		for( HashMap<String, String> map : list){
			mylist.add(map2Bean(map));
		}
		return mylist;
	}
	public void deleteUserById(RegistBean bean)
	{
		if(bean != null && !bean.getId().equals(""))
		{
			String sql = "delete from "+TableName +" where "+Column_USERID+"="+bean.getId();
			executeDelete(sql);
		}
		
	}
	
	private RegistBean map2Bean(HashMap<String, String> map){
		if(map==null)return null;
		RegistBean bean = new RegistBean();
		bean.setName(map.get(Column_USERNAME));
		bean.setNickname(map.get(Column_NICKNAME));
		bean.setId(map.get(Column_USERID));
		bean.setType(map.get(Column_USERTYPE));
		bean.setPassword(map.get(Column_PASSWORD));
		bean.setHead_portrait(map.get(Column_HEADPORTRAIT));
		bean.setPhone(map.get(Column_PHONENUM));
		return bean;
	}
	private static final String Column_USERNAME = "USERNAME";
	private static final String Column_NICKNAME = "NICKNAME";
	private static final String Column_USERTYPE = "USERTYPE";
	private static final String Column_USERID = "USERID";
	private static final String Column_HEADPORTRAIT = "HEADPORTRAIT";
	private static final String Column_PASSWORD = "PASSWORD";
	private static final String Column_PHONENUM = "PHONENUM";
	private static final String Column_LOGINTAG = "LOGINTAG";
}
