package com.example.casting_android.bean;

import java.io.Serializable;


public class ImageBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int type_res = 0;
	public static final int type_filepath =1;
	public static final int type_url = 2;
	
	public ImageBean(int restype ,String img){
		this.restype =restype;
		this.img_res = img;
	}
	public ImageBean(int restype ,int img){
		this.restype =restype;
		this.img_res = img+"";
	}
	private int restype = 0;
	private String img_res = "";

	public int getRestype() {
		return restype;
	}
	public String getImg_res() {
		return img_res;//"files/1/20150316152910.jpg"
	}
	
	/**
	 * 是否注册
	 */
	private boolean registtag = false;

	public boolean isRegisttag() {
		return registtag;
	}
	public void setRegisttag(boolean registtag) {
		this.registtag = registtag;
	}

}
