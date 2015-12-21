package com.example.casting.entity;


public class SearchBean extends BaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String searchstr  = "";
	/**
	 * type - 搜全部0，搜公司1，搜人2
	 */
	private String type = "";
	

	public String getSearchstr() {
		return searchstr;
	}

	public void setSearchstr(String searchstr) {
		this.searchstr = searchstr;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	
}
