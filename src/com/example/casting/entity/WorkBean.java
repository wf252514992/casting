package com.example.casting.entity;


import com.example.casting_android.R;
import com.example.casting_android.bean.ImageBean;

public class WorkBean extends BaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 作品id
	 */
	private String works_id = "";
	/**
	 * 剧名
	 */
	private String works_name = "";
	/**
	 * 上映时间
	 */
	private String release_time = "";
	/**
	 * 扮演角色
	 */
	private String role = "";
	/**
	 * 导演
	 */
	private String director = "";
	/**
	 * 合作演员
	 */
	private String cooperation_actor = "";
	/**
	 * 提交时间
	 */
	private String submit_time = "";
	/**
	 * 作品照片
	 */
	private ImageBean workpic = new ImageBean(ImageBean.type_res,R.drawable.ic_launcher);

	private String works_images = "";
	/**
	 * 备注
	 */
	private String bz = "";
	public String getWorks_id() {
		return works_id;
	}
	public void setWorks_id(String works_id) {
		this.works_id = works_id;
	}
	public String getWorks_name() {
		return works_name;
	}
	public void setWorks_name(String works_name) {
		this.works_name = works_name;
	}
	public String getRelease_time() {
		return release_time;
	}
	public void setRelease_time(String release_time) {
		this.release_time = release_time;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getCooperation_actor() {
		return cooperation_actor;
	}
	public void setCooperation_actor(String cooperation_actor) {
		this.cooperation_actor = cooperation_actor;
	}
	public String getSubmit_time() {
		return submit_time;
	}
	public void setSubmit_time(String submit_time) {
		this.submit_time = submit_time;
	}
	public ImageBean getWorkpic() {
		return workpic;
	}
	public void setWorkpic(ImageBean workpic) {
		this.workpic = workpic;
	}
	public String getWorks_images() {
		return works_images;
	}
	public void setWorks_images(String works_images) {
		this.works_images = works_images;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	
	
}
