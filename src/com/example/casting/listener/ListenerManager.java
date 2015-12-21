package com.example.casting.listener;

import java.util.ArrayList;

public class ListenerManager {

	private static ArrayList<CloseAllListener> aliveActivity = new ArrayList<CloseAllListener>();
	private static ArrayList<TabHostChangeListener> tabList = new ArrayList<TabHostChangeListener>();
	private static ArrayList<CloseLoginFormListener> loginAtivity = new ArrayList<CloseLoginFormListener>();
	private static ArrayList<JpushMessageRefreshListener> jpushMessageList = new ArrayList<JpushMessageRefreshListener>();
	private static ArrayList<TabHostRefreshListener> freshListener = new ArrayList<TabHostRefreshListener>();
	private static ArrayList<SystemMessageRefreshListener> systemMessageList = new ArrayList<SystemMessageRefreshListener>();
	public static void addLoginFormListener(CloseLoginFormListener loginForm)
	{
		loginAtivity.add(loginForm);
	}
	public static void notifyCloseLoginActivity(){
		while(loginAtivity!=null && !loginAtivity.isEmpty()){
			CloseLoginFormListener a  = loginAtivity.remove(0);
			a.closeActivity();
		}
		
	}
	public static void addJpushMessageListener(JpushMessageRefreshListener listener){
		jpushMessageList.add(listener);
	}
	public static void addSystemMessageListener(SystemMessageRefreshListener listener){
		systemMessageList.add(listener);
	}
	public static void addCloseListener(CloseAllListener activity){
		for( CloseAllListener a : aliveActivity){
			if( a.getActivityid()== activity.getActivityid()){
				aliveActivity.remove(a);
			}
		}
		aliveActivity.add(activity);
	}
	
	public static void addTabHostListener(TabHostChangeListener activity){
		if(tabList.size()==0){
			tabList.add(activity);
		}
	}
	
	public static void notifyTabHostChange(){
		for(TabHostChangeListener listener :tabList){
			//主要用于  自己查看 自己，切换 
			listener.changeView(4);
		}
	}
	public static void notifyCloseActivity(){
		tabList.clear();
		while(aliveActivity!=null && !aliveActivity.isEmpty()){
			CloseAllListener a  = aliveActivity.remove(0);
			a.closeActivity();
		}
		
	}
	
	/**
	 * 监听tabhost的切换
	 */
	public static void notifyFreshDynimacform(){
		for( TabHostRefreshListener a : freshListener){
			a.freshDynamic();
		}
	}
	/**
	 * 添加 首页动态刷新监听  管理
	 * @param listener
	 */
	public static void addFreshDynimacListener(TabHostRefreshListener listener){
		for( TabHostRefreshListener a : freshListener){
			if( a.getListenerId()== listener.getListenerId()){
				freshListener.remove(listener);
			}
		}
		freshListener.add(listener);
	}
	/**
	 * 移除 首页动态监听管理
	 * @param listener
	 */
	public static void removeFreshDynimacListener(TabHostRefreshListener listener){
		freshListener.remove(listener);
	}
	
	public static void notifyJpushMessageActivity(){
		for (int i = 0; i < jpushMessageList.size(); i++) {
			jpushMessageList.get(i).refresh();
		}
	}
	public static void notifySystemMessageActivity(){
		for (int i = 0; i < systemMessageList.size(); i++) {
			systemMessageList.get(i).refresh();
		}
	}
	public static void removeCloseListener(CloseAllListener activity){
		aliveActivity.remove(activity);
	}
	public static void removeJpushMessageListener(JpushMessageRefreshListener activity){
		jpushMessageList.remove(activity);
	}
	public static void removeSystemMessageListener(SystemMessageRefreshListener activity){
		systemMessageList.remove(activity);
	}
}
