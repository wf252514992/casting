package com.example.casting.processor.company;

import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.Users.BaseUserProcess;

public abstract class BaseCompanyProcessor extends BaseUserProcess{

	/**
	 * 
	 */
	private long serialVersionUID = 1L;

	@Override
	public String getService() {
		// TODO Auto-generated method stub
		return ProcessorID.service_company;
	}
	
	public String company_profile = "company_profile";
	public String company_address = "company_address";
	public String company_phone = "company_phone";
	public String company_email = "company_email";
	public String company_certification = "company_certification";
	public String author_id = "author_id";
	public String submit_time = "submit_time";
	public String company_icon = "company_icon";
	/**
	 * 公司状态
	 */
	public String company_status = "company_status";
	/**
	 * 公司id
	 */
	public String company_id = "company_id";
	/**
	 * 名称
	 */
	public String company_name = "company_name";
	
	public String company_legal_representative = "company_legal_representative";
	
}
