package com.example.casting.processor.plaza;


import com.example.casting.entity.PlazaBean;
import com.example.casting.processor.BaseProcessor;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.CastMap;
import com.example.casting_android.bean.ImageBean;

public abstract class BasePlazzaProcessor extends BaseProcessor {
	@Override
	public String getService() {
		// TODO Auto-generated method stub
		return ProcessorID.service_plaza;
	}

	public static final String attent_id = "attent_id";
	public static final String followers_id = "followers_id";
	public static final String followers_name = "followers_name";
	public static final String by_follower_id = "by_follower_id";
	public static final String by_followers_name = "by_followers_name";
	public static final String focus_time = "focus_time";
	public static final String user_id = "user_id";
	public static final String nickname = "nickname";
	public static final String head_portrait = "head_portrait";
	public static final String followers_count = "followers_count";
	public static final String state = "state";
	public static final String type = "type";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected PlazaBean map2Bean(CastMap map) {
		try {
			PlazaBean bean = new PlazaBean();
			bean.setAttent_id(map.get(attent_id));
			bean.setBy_follower_id(map.get(by_follower_id));
			bean.setBy_followers_name(map.get(by_followers_name));
			bean.setFocus_time(map.get(focus_time));
			bean.setFollowers_count(map.get(followers_count));
			bean.setFollowers_id(map.get(followers_id));
			bean.setFollowers_name(map.get(by_followers_name));
			bean.setHead_portrait(map.get(head_portrait));
			bean.setId(map.get(user_id));
			bean.setState(map.get(state));
			bean.setNickname(map.get(nickname));
			bean.setType(map.get(type));
			String url = map.get(head_portrait);
			if(url!=null && url.length()>0){
				bean.setImgbean(new ImageBean(ImageBean.type_url, url));
			}
			return bean;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}
