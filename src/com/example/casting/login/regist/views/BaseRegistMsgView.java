package com.example.casting.login.regist.views;

import java.util.List;

import com.example.casting.entity.RegistBean;
import com.example.casting.entity.WinnerBean;
import com.example.casting.entity.WorkBean;

import android.view.View;

public abstract class BaseRegistMsgView {
	public static final int OperateType_New = 1;
	public static final int OperateType_Set = 2; 
	public static final int OperateType_Show = 3;
	
	public abstract void changeSubView(int resid ,String val);
	public abstract View getView();
	public abstract RegistBean getValues();
	public abstract void updateViewVal(RegistBean bean);
		
	public String getWorks(List<WorkBean> beans){
		if(beans==null)return "";
		String value = "";
		for( WorkBean bean : beans){
			value +="《"+bean.getWorks_name()+"》  ";
		}
		return value ;
	}
	public String getWinner(List<WinnerBean> beans){
		if(beans==null)return "";
		String value = "";
		for( WinnerBean bean : beans){
			value +="《"+bean.getWinners_name()+"》  ";
		}
		return value ;
	}
}
