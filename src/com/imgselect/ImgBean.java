package com.imgselect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.R.bool;

public class ImgBean implements Serializable
{
	/**
	 * 图片地址
	 */
   private String url="";
   public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
/**
    * 是否已选，已选会添加背景
    */
   private boolean state_Selected = false;
   
   public boolean isState_Selected() {
	return state_Selected;
}
public void setState_Selected(boolean state_Selected) {
	this.state_Selected = state_Selected;
}
public static List<ImgBean> convertToBean(List<String> str)
   {
	List<ImgBean> beans = new ArrayList<ImgBean>();
	int size = str.size();
	   for(int i=0;i<size;i++)
	   {
		   ImgBean bean = new ImgBean();
		   bean.setUrl(str.get(i));
		   bean.setState_Selected(false);
		   beans.add(bean);
	   }
	return beans;
   }

}
