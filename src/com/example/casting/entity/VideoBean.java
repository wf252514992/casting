package com.example.casting.entity;

public class VideoBean extends BaseBean{

	/**
	 * 昵称
	 */
	private String nickName = "";
	/**
	 * 视频下载地址*/
	private String url = "";

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
}
