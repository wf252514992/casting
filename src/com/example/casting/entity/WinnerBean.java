package com.example.casting.entity;


import com.example.casting_android.R;
import com.example.casting_android.bean.ImageBean;

public class WinnerBean extends BaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 经历id
	 */
	private String winners_id = "";
	/**
	 * 获奖年份
	 */
	private String winners_time = "";
	/**
	 * 获得奖项
	 */
	private String winners_name = "";
	/**
	 * 获奖作品
	 */
	private String winners_works_name = "";
	/**
	 * 提交时间
	 */
	private String submit_time = "";
	/**
	 * 获奖图片
	 */
	private String winners_images="";
	
	private ImageBean winnerspic ;
	
	private String bz = "";
	public String getWinners_id() {
		return winners_id;
	}
	public void setWinners_id(String winners_id) {
		this.winners_id = winners_id;
	}
	public String getWinners_time() {
		return winners_time;
	}
	public void setWinners_time(String winners_time) {
		this.winners_time = winners_time;
	}
	public String getWinners_name() {
		return winners_name;
	}
	public void setWinners_name(String winners_name) {
		this.winners_name = winners_name;
	}
	public String getWinners_works_name() {
		return winners_works_name;
	}
	public void setWinners_works_name(String winners_works_name) {
		this.winners_works_name = winners_works_name;
	}
	public String getSubmit_time() {
		return submit_time;
	}
	public void setSubmit_time(String submit_time) {
		this.submit_time = submit_time;
	}
	public ImageBean getWinnerspic() {
		if(winnerspic == null){
			winnerspic = new ImageBean(ImageBean.type_res, R.drawable.ic_launcher);
		}
		return winnerspic;
	}
	public void setWinnerspic(ImageBean winnerspic) {
		this.winnerspic = winnerspic;
	}
	public String getWinners_images() {
		return winners_images;
	}
	public void setWinners_images(String winners_images) {
		this.winners_images = winners_images;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	
	
	
}
