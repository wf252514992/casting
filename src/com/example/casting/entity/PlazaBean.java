package com.example.casting.entity;

import com.example.casting_android.R;
import com.example.casting_android.bean.ImageBean;


public class PlazaBean extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String searchtype = "";
	private String minage = "";
	private String maxage = "";
	private String sex = "";
	private String attent_id = "";
	private String followers_id = "";
	private String followers_name = "";
	private String by_follower_id = "";
	private String by_followers_name = "";
	private String focus_time = "";
	private String nickname = "";
	private String head_portrait = "";
	private String followers_count = "";
	private String state = "";
	private String type = "";
	private ImageBean imgbean ;
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	private String label= "";
	public String getSearchtype() {
		return searchtype;
	}
	public void setSearchtype(String searchtype) {
		this.searchtype = searchtype;
	}
	public String getMinage() {
		return minage;
	}
	public void setMinage(String minage) {
		this.minage = minage;
	}
	public String getMaxage() {
		return maxage;
	}
	public void setMaxage(String maxage) {
		this.maxage = maxage;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getAttent_id() {
		return attent_id;
	}
	public void setAttent_id(String attent_id) {
		this.attent_id = attent_id;
	}
	public String getFollowers_id() {
		return followers_id;
	}
	public void setFollowers_id(String followers_id) {
		this.followers_id = followers_id;
	}
	public String getFollowers_name() {
		return followers_name;
	}
	public void setFollowers_name(String followers_name) {
		this.followers_name = followers_name;
	}
	public String getBy_follower_id() {
		return by_follower_id;
	}
	public void setBy_follower_id(String by_follower_id) {
		this.by_follower_id = by_follower_id;
	}
	public String getBy_followers_name() {
		return by_followers_name;
	}
	public void setBy_followers_name(String by_followers_name) {
		this.by_followers_name = by_followers_name;
	}
	public String getFocus_time() {
		return focus_time;
	}
	public void setFocus_time(String focus_time) {
		this.focus_time = focus_time;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getHead_portrait() {
		return head_portrait;
	}
	public void setHead_portrait(String head_portrait) {
		this.head_portrait = head_portrait;
	}
	public String getFollowers_count() {
		return followers_count;
	}
	public void setFollowers_count(String followers_count) {
		this.followers_count = followers_count;
	}
	public int getState() {
		try{
			return Integer.parseInt(state);
		}catch(Exception e){
		}
		return 0;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ImageBean getImgbean() {
		if(imgbean == null){
			imgbean = new ImageBean(ImageBean.type_res,R.drawable.me_edit_txsmall);
		}
		return imgbean;
	}
	public void setImgbean(ImageBean imgbean) {
		this.imgbean = imgbean;
	}

}
