package com.example.casting.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.casting.db.UserManager.UserDBBusiness;
import com.example.casting.entity.RegistBean;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Session {

	private final String sharename = "castinginfo";
	private String user_id = "156";
	private String name_nick = "汪汪";
	private String phone = "";
	private String usertype = "";
	private String password = "";
	private String headportait = "";
	boolean firstlogin = true;
	private ArrayList<String> labels = new ArrayList<String>();
	private ArrayList<HashMap<String, String>> agelist = new ArrayList<HashMap<String,String>>();
	private ArrayList<String> sexs = new ArrayList<String>();
	private static Session session;
	public static  Session getInstance(){
		if(session==null){
			session = new Session();
		}
		return session;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public ArrayList<String> getLabels() {
		return labels;
	}
	public void setLabels(ArrayList<String> labels) {
		this.labels = labels;
	}
	public ArrayList<HashMap<String, String>> getAgelist() {
		return agelist;
	}
	public void setAgelist(ArrayList<HashMap<String, String>> agelist) {
		this.agelist = agelist;
	}
	public ArrayList<String> getSexs() {
		return sexs;
	}
	public void setSexs(ArrayList<String> sexs) {
		this.sexs = sexs;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName_nick() {
		return name_nick;
	}
	public void setName_nick(String name_nick) {
		this.name_nick = name_nick;
	}
	
	
	public String getHeadportait() {
		return headportait;
	}
	public void setHeadportait(String headportait) {
		this.headportait = headportait;
	}
	public void Save(Context ctx){
		SharedPreferences share = ctx.getSharedPreferences(sharename, Activity.MODE_PRIVATE);
		share.edit().putString("user_id", getUser_id())
		.putBoolean("firstlogin", false)
		.putString("phone",getPhone())
		.putString("pwd",getPassword()).commit();

		RegistBean registbean = new RegistBean();
		registbean.setId(getUser_id());
		registbean.setPhone(getPhone());
		registbean.setNickname(getName_nick());
		registbean.setPassword(getPassword());
		registbean.setType(getUsertype());
		registbean.setHead_portrait(getHeadportait());
		UserDBBusiness userDB = new UserDBBusiness(ctx);
		userDB.InserNewUser(registbean);
		
	}
	
	public void init(Context ctx){
		SharedPreferences share = ctx.getSharedPreferences(sharename, Activity.MODE_PRIVATE);
		setUser_id(share.getString("user_id",""));
		setPhone(share.getString("phone",""));
		setPassword(share.getString("pwd",""));
		setFirstlogin(share.getBoolean("firstlogin", true));
	}
	public boolean isFirstlogin() {
		return firstlogin;
	}
	public void setFirstlogin(boolean firstlogin) {
		this.firstlogin = firstlogin;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}