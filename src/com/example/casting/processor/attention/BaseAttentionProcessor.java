package com.example.casting.processor.attention;


import com.example.casting.entity.AttentionBean;
import com.example.casting.processor.BaseProcessor;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.CastMap;
import com.example.casting_android.bean.ImageBean;

public abstract class BaseAttentionProcessor extends BaseProcessor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getService() {
		// TODO Auto-generated method stub
		return ProcessorID.service_attetion;
	}
	
	/**
	 * 用户类型
	 */
	protected String type = "type";
	
	protected String attention ="attention";
	/**
	 * 简介
	 */
	protected String introduction = "introduction"; 
	/**
	 * 关注状态
	 * 
	 */
	protected String attention_me = "attention_me";
	/**
	 * 关注id
	 */
	protected String attent_id = "attent_id";
	/**
	 * 关注者id
	 */
	protected String followers_id = "followers_id";
	/**
	 * 关注者名称
	 */
	protected String followers_name = "followers_name";
	/**
	 * 被关注者id
	 */
	protected String by_follower_id = "by_follower_id";
	/**
	 * 被关注者名称
	 */
	protected String by_followers_name = "by_followers_name";
	/**
	 * 关注时间
	 */
	protected String focus_time = "focus_time";
	/**
	 * 昵称
	 */
	protected String nickname = "nickname";
	/**
	 * 头像
	 */
	protected String head_portrait = "head_portrait";
	
	protected AttentionBean map2bean( CastMap map) {
		if( map!=null ){
			AttentionBean bean = new AttentionBean();
			bean.setAttent_id(map.get(attent_id));
			bean.setBy_follower_id( map.get(by_follower_id));
			bean.setBy_followers_name(map.get(by_followers_name));
			bean.setFocus_time(map.get(focus_time));
			bean.setFollowers_id(map.get(followers_id));
			bean.setFollowers_name(map.get(followers_name));
			bean.setNickname(map.get(nickname));
			bean.setAttention(map.get(attention));
			bean.setAttention_me(map.get(attention_me));
			bean.setType(map.get(type));
			bean.setIntroduce(map.get(introduction));
			String url = map.get(head_portrait);
			if(url!=null && url.length()>0){
				ImageBean img = new ImageBean(ImageBean.type_url,url);
				bean.setImgbean(img);
				bean.setHead_portrait(url);
			}
			return bean;
		}
		return null;
		
	}
}
