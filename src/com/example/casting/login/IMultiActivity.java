package com.example.casting.login;

import android.view.View;

public interface IMultiActivity {
	/**
	 * 在多页滑屏的功能界面里，这个方法可以获取当前页面
	 * @param currentView 当前view
	 */
	void currentViewOnChange(View currentView);
	/** 
	 * @return 返回一个boolean变量，当为true时，允许切换子页；当为false时，阻止切换子页。
	 * 和currentViewOnChange方法配合使用，可以作为当前页滑动是否需要信息验证等判断。
	 */
	boolean allowChangeView();
}
