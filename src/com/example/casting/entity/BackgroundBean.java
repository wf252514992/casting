package com.example.casting.entity;

import com.example.casting.login.regist.ChangeBgActivity;
import com.example.casting.login.regist.ChangeBgActivity.BACKGROUND_TYPE;
import com.example.casting_android.R;

/**
 * 模块的背景图实体类
 * 
 * @author hunagjian
 * 
 */
public class BackgroundBean extends BaseBean {

	/**
	 * 获奖经历_背景
	 */
	public final static String winners_background = "winners_background";
	/**
	 * 工作经历_背景
	 */
	public final static String works_background = "works_background";
	/**
	 * 个人信息_背景
	 */
	public final static String user_background = "user_background";
	/**
	 * 个人展示_背景
	 */
	public final static String gallery_background = "gallery_background";
	/**
	 * 我的试镜_背景
	 */
	public final static String audition_background = "audition_background";
	/**
	 * 我的视频_背景
	 */
	public final static String video_background = "video_background";
	/**
	 * */
	public final static String custom_background = "custom_background";
	/** 从服务端返回的封面url */
	private String url_cover = "";

	public String getUrl_cover() {
		return url_cover;
	}

	public void setUrl_cover(String url_cover) {
		this.url_cover = url_cover;
	}

	public String getUrl_winners() {
		return url_winners;
	}

	public void setUrl_winners(String url_winners) {
		this.url_winners = url_winners;
	}

	public String getUrl_works() {
		return url_works;
	}

	public void setUrl_works(String url_works) {
		this.url_works = url_works;
	}

	public String getUrl_userinfo() {
		return url_userinfo;
	}

	public void setUrl_userinfo(String url_userinfo) {
		this.url_userinfo = url_userinfo;
	}

	public String getUrl_personshow() {
		return url_personshow;
	}

	public void setUrl_personshow(String url_personshow) {
		this.url_personshow = url_personshow;
	}

	public String getUrl_audition() {
		return url_audition;
	}

	public void setUrl_audition(String url_audition) {
		this.url_audition = url_audition;
	}

	public String getUrl_video() {
		return url_video;
	}

	public void setUrl_video(String url_video) {
		this.url_video = url_video;
	}

	/** 获奖经历背景url */
	private String url_winners = "";
	/** 工作经历背景url */
	private String url_works = "";
	/** 个人信息背景url */
	private String url_userinfo = "";
	/** 个人展示背景url */
	private String url_personshow = "";
	/** 试镜背景url */
	private String url_audition = "";
	/** 视频背景url */
	private String url_video = "";

	public BACKGROUND_TYPE backType;

	public BACKGROUND_TYPE getBackType() {
		return backType;
	}

	public void setBackType(BACKGROUND_TYPE backType) {
		this.backType = backType;
	}

	
	public String getImg_type() {
		if (img_type != null && img_type.length() > 0)
			return img_type;
		if (img_url != null && img_url.length() > 0)
			img_type = img_url.substring(img_url.indexOf(".") + 1);
		if (img_type.equals("")) {
			return "jpg";
		}
		return img_type;
	}

	public void setImg_type(String img_type) {
		this.img_type = img_type;
	}

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	/** 设置背景图的Url */
	private String img_url = "";
	/** 恢复默认设置时，传本地resource */
	private int img_resource;

	/**
	 * 图片数据
	 */
	private String img_data;
	
	private String img_type = "";

	public String getImg_data() {
		return img_data;
	}

	public void setImg_data(String img_data) {
		this.img_data = img_data;
	}

	public int getImg_resource() {
		return img_resource;
	}

	public void setImg_resource(int img_resource) {
		this.img_resource = img_resource;
	}

	public String getBackgroundType() {
		if (backType == BACKGROUND_TYPE.MY_SCRREN_TEST) {
			return audition_background;
		} else if (backType == BACKGROUND_TYPE.COVER) {
			return custom_background;
		} else if (backType == BACKGROUND_TYPE.MY_VIDEO) {
			return video_background;
		} else if (backType == BACKGROUND_TYPE.PERSON_INFO) {
			return user_background;
		} else if (backType == BACKGROUND_TYPE.PERSON_WORK) {
			return works_background;
		} else if (backType == BACKGROUND_TYPE.PERSONAL_SHOW) {
			return gallery_background;
		} else if (backType == BACKGROUND_TYPE.WIN_EXPERIENCE) {
			return winners_background;
		} else {
			return "";
		}
	}
}
