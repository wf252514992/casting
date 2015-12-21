package com.example.casting.entity;

import com.example.casting_android.R;
import com.example.casting_android.bean.ImageBean;

public class AttentionBean extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String attent_id = "";
	private String followers_id = "";
	private String followers_name = "";
	private String by_follower_id = "";
	private String by_followers_name = "";
	private String focus_time = "";
	private String nickname = "";
	private String head_portrait = "";
	private String type = "";
	private String introduce = "";
	private String attention_me = "";
	private String attention = "";

	private ImageBean imgbean;

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

	public ImageBean getImgbean() {
		if (imgbean == null) {
			imgbean = new ImageBean(ImageBean.type_res, R.drawable.ic_launcher);
		}
		return imgbean;
	}

	public void setImgbean(ImageBean imgbean) {
		this.imgbean = imgbean;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getAttention_me() {
		return attention_me;
	}

	public void setAttention_me(String attention_me) {
		this.attention_me = attention_me;
	}

	public String getAttention() {
		return attention;
	}

	public void setAttention(String attention) {
		this.attention = attention;
	}

	public int getStatue() {
		// 我关注对方的状态 0=未关注，1=已关注
		// String attention = bean.getAttention();
		// 对方关注我的状态0=未关注，1=已关注
		// String beattention = bean.getAttention_me();

		if (attention.equals("0")) {
			// 我未关注对方
			return 0;
		} else if (attention.equals("1")) {
			if (attention_me.equals("0")) {
				// 对方为关注我
				return 1;
			} else if (attention_me.equals("1")) {
				// 相互关注
				return 2;
			}
		}
		return 0;
	}
}
