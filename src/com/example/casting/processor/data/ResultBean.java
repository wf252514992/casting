package com.example.casting.processor.data;

import java.io.Serializable;

import com.example.casting.processor.ErrorConvert;

public class ResultBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ResultBean(){
		
	}
	public ResultBean(int code,String message){
		this.code = code;
		this.message = message;
	}
	public ResultBean(int code,Object obj){
		this.code = code;
		this.message = ErrorConvert.getErrorDiscribe(code);
		this.data = obj;
	}
	public ResultBean(int code){
		this.code = code;
		this.message = ErrorConvert.getErrorDiscribe(code);
	}
	public ResultBean(int code,String message,Object obj){
		this.code = code;
		this.message = message;
		this.data = obj ;
	}
	private int code = 0;
	private String message = "";
	private Object data;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getObj() {
		return data;
	}
	public void setObj(Object obj) {
		this.data = obj;
	}

}
