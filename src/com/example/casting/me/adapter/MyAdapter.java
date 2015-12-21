package com.example.casting.me.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class MyAdapter extends PagerAdapter  {


	/** 装分页显示的view的数组 */
	private ArrayList<View> pages = new ArrayList<View>();
	public MyAdapter(ArrayList<View> pages){
		this.pages = pages;
	}
	@Override
	public int getCount() {
		return pages.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(pages.get(position));

	}
	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return super.getItemPosition(object);
	}

	/**
	 * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(pages.get(position), 0);
		return pages.get(position);
	}


}
