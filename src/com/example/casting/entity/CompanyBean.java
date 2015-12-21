package com.example.casting.entity;

import java.io.Serializable;

public class CompanyBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 公司图标
	 */
	private String company_icon ="";
	/**
	 * 法人代表
	 */
	private String company_legal_representative ="";
	/**
	 * 公司id
	 */
	private String company_id = "company_id";
//	/**
//	 * 是否认证
//	 */
//	private String is_certification ="";
	/**
	 * 提交人
	 */
	private String author_id ="";
	/**
	 * 提交时间
	 */
	private String submit_time ="";
//	public String getCompany_profile() {
//		return company_profile;
//	}
//	public void setCompany_profile(String company_profile) {
//		this.company_profile = company_profile;
//	}
//	public String getCompany_address() {
//		return company_address;
//	}
//	public void setCompany_address(String company_address) {
//		this.company_address = company_address;
//	}
//	public String getCompany_phone() {
//		return company_phone;
//	}
//	public void setCompany_phone(String company_phone) {
//		this.company_phone = company_phone;
//	}
//	public String getCompany_email() {
//		return company_email;
//	}
//	public void setCompany_email(String company_email) {
//		this.company_email = company_email;
//	}
//	public String getCompany_certification() {
//		return company_certification;
//	}
//	public void setCompany_certification(String company_certification) {
//		this.company_certification = company_certification;
//	}
	public String getCompany_icon() {
		return company_icon;
	}
	public void setCompany_icon(String company_icon) {
		this.company_icon = company_icon;
	}
	public String getCompany_legal_representative() {
		return company_legal_representative;
	}
	public void setCompany_legal_representative(String company_legal_representative) {
		this.company_legal_representative = company_legal_representative;
	}
//	public String getIs_certification() {
//		return is_certification;
//	}
//	public void setIs_certification(String is_certification) {
//		this.is_certification = is_certification;
//	}
	public String getAuthor_id() {
		return author_id;
	}
	public void setAuthor_id(String author_id) {
		this.author_id = author_id;
	}
	public String getSubmit_time() {
		return submit_time;
	}
	public void setSubmit_time(String submit_time) {
		this.submit_time = submit_time;
	}
	public String getCompany_id() {
		return company_id;
	}
	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}

	
	
}
