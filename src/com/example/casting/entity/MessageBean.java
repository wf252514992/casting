package com.example.casting.entity;

import java.io.Serializable;

public class MessageBean implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	 {"from_id"："123","to_id":"124","type":"类型","content":"呵呵","send_time":"2015-01-29 09:10:33"}
	private int messageId;
	private String from_id;
	private String to_id;
//	private String type;
	private String content;
    private String send_time;
    private String isMyMessage;//0表示我发送的消息
    private String picPath;
    private String nickname;
    private String acceptNickname;
    private String id;// 系统广播推送的id
	public int getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	public String getFrom_id() {
		return from_id;
	}
	public void setFrom_id(String from_id) {
		this.from_id = from_id;
	}
	public String getTo_id() {
		return to_id;
	}
	public void setTo_id(String to_id) {
		this.to_id = to_id;
	}
//	public String getType() {
//		return type;
//	}
//	public void setType(String type) {
//		this.type = type;
//	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSend_time() {
		return send_time;
	}
	public void setSend_time(String send_time) {
		this.send_time = send_time;
	}
	public String isMyMessage() {
		return isMyMessage;
	}
	public void setMyMessage(String isMyMessage) {
		this.isMyMessage = isMyMessage;
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public String getIsMyMessage() {
		return isMyMessage;
	}
	public void setIsMyMessage(String isMyMessage) {
		this.isMyMessage = isMyMessage;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public String getAcceptNickname() {
		return acceptNickname;
	}
	public void setAcceptNickname(String acceptNickname) {
		this.acceptNickname = acceptNickname;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
    
}
