package com.example.casting.entity;

import java.io.Serializable;

public class ConditionBean  extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * ages时间段(1)labels标签(2),不传为所有
	 * 
	 * 
	 * 3=性别
	 * 
	 */
	private String type = "";
	private String plazaid = "";
	private String labels = "";
	private String minage = "";
	private String maxage = "";
	private String plaza_time = "";
	private String sex = "";
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPlazaid() {
		return plazaid;
	}
	public void setPlazaid(String plazaid) {
		this.plazaid = plazaid;
	}
	public String getLabel() {
		return labels;
	}
	public void setLabel(String labels) {
		this.labels = labels;
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
	public String getPlaza_time() {
		return plaza_time;
	}
	public void setPlaza_time(String plaza_time) {
		this.plaza_time = plaza_time;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	

}
