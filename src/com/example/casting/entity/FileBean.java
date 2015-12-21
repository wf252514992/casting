package com.example.casting.entity;


public class FileBean extends BaseBean {

	public static final int DT_FILEPATH = 0 ;
	public static final int DT_BYTE=1;
	public static final int DT_STR = 2;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 数据类型  0== 文件
	 * 1=byte[]
	 * 2 = String
	 */
	private int datatype = 0;
	/**
	 * 结尾 标志
	 */
	private String endtag = "";
	
	/**
	 * 文件数据
	 */
	private String fieldata = "";
	
	public String getEndtag() {
		return endtag;
	}
	public void setEndtag(String endtag) {
		this.endtag = endtag;
	}
	public int getDatatype() {
		return datatype;
	}
	public void setDatatype(int datatype) {
		this.datatype = datatype;
	}
	public String getFieldata() {
		return fieldata;
	}
	public void setFieldata(String fieldata) {
		this.fieldata = fieldata;
	}
	
	
}
