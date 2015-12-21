package com.example.casting.login.regist.views;

import android.text.InputType;

public class TextViewClickBean {
	
	private int resid ;
	private String keyval;
	//默认是编辑文本 0,1表示编辑标签，2.地点编辑，3，表示编辑日期
	// 4 .性别选择框    5,简介
	private int clicktype;
	private String keyname ;
	private int clickto ;
	private int edittype = InputType.TYPE_CLASS_TEXT;
	
	public int getEdittype() {
		return edittype;
	}
	public void setEdittype(int edittype) {
		this.edittype = edittype;
	}
	public int getClickto() {
		return clickto;
	}
	public void setClickto(int clickto) {
		this.clickto = clickto;
	}
	public int getResid() {
		return resid;
	}
	public void setResid(int resid) {
		this.resid = resid;
	}
	public int getClicktype() {
		return clicktype;
	}
	public void setClicktype(int clicktype) {
		this.clicktype = clicktype;
	}
	
	public String getKeyval() {
		return keyval;
	}
	public void setKeyval(String keyval) {
		this.keyval = keyval;
	}
	public String getKeyname() {
		return keyname;
	}
	public void setKeyname(String keyname) {
		this.keyname = keyname;
	}

	/**
	 * 文本编辑
	 */
	public static final int clicktype_edt = 0;
	/**
	 * 标签编辑 ，跳转到界面， 简介
	 */
	public static final int clicktype_label = 1;
	/**
	 * 地点编辑
	 */
	public static final int clicktype_address =2;
	/**
	 * 日期编辑
	 */
	public static final int clicktype_date =3 ;
	/**
	 * 性别
	 */
	public static final int clicktype_sex = 4;
	/**
	 * 照片选择
	 */
	public static final int clicktype_photo = 5;
	/**
	 * 微博认证
	 */
	public static final int clicktype_weibo = 6;
	/**
	 * 更换背景
	 */
	public static final int clicktype_cgbg = 7;
}
