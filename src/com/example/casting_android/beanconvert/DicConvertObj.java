package com.example.casting_android.beanconvert;

/**
 * 字典转换中使用到的中间量
 * 
 * @author daiyu
 * 
 */
public class DicConvertObj {
	// 需要修改的bean中设置值的方法
	private String setMethodName;
	// 字典名称
	private String dicName;
	// 字段值
	private String value;

	public String getSetMethodName() {
		return setMethodName;
	}

	public void setSetMethodName(String setMethodName) {
		this.setMethodName = setMethodName;
	}

	public String getDicName() {
		return dicName;
	}

	public void setDicName(String dicName) {
		this.dicName = dicName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
