package com.example.casting_android.beanconvert;
import java.io.Serializable;
/**
 * 
 * @author daiyu
 * bean对象例子，凡是需要显示对象属性对应字典中中文的，有下述几点要求。
 * 1、bean对象必须实现Serializable接口
 * 2、标准属性转换使用的字典表名
 */
public class BeanExample implements Serializable {

	private static final long serialVersionUID = 1L;
	private String sex;

	@JwtDic(dic = "sex")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

}
