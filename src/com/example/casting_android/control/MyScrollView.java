package com.example.casting_android.control;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class MyScrollView extends HorizontalScrollView {
	/**
	 * 根部局
	 */
	LinearLayout linear_Root;

	private int subChildCount = 0;
	private ViewGroup firstChild = null;
	private int downX = 0;
	private int currentPage = 0;
	private ArrayList<Integer> pointList = new ArrayList<Integer>();

	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MyScrollView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		// 添加根Layout
		linear_Root = new LinearLayout(context);
		linear_Root.setOrientation(LinearLayout.HORIZONTAL);
		linear_Root.setBackgroundColor(Color.TRANSPARENT);
		addView(linear_Root, ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		setHorizontalScrollBarEnabled(false);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		receiveChildInfo();
	}

	public void receiveChildInfo() {

		firstChild = (ViewGroup) getChildAt(0);
		pointList.clear();
		if (firstChild != null) {
			subChildCount = firstChild.getChildCount();
			for (int i = 0; i < subChildCount; i++) {
				if (((View) firstChild.getChildAt(i)).getWidth() > 0) {
					pointList.add(((View) firstChild.getChildAt(i)).getLeft());
				}
			}
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) ev.getX();
			break;
		case MotionEvent.ACTION_MOVE: {

		}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL: {
			if (Math.abs((ev.getX() - downX)) > getWidth() / 4) {
				if (ev.getX() - downX > 0) {
					smoothScrollToPrePage();
				} else {
					smoothScrollToNextPage();
				}
			} else {
				smoothScrollToCurrent();
			}
			return true;
		}
		}
		return super.onTouchEvent(ev);
	}

	private void smoothScrollToCurrent() {
		if(pointList.size()>0)
			smoothScrollTo(pointList.get(currentPage), 0);
	}

	private void smoothScrollToNextPage() {
		if (currentPage < subChildCount - 1) {
			currentPage++;
			if(pointList.size()>currentPage)
				smoothScrollTo(pointList.get(currentPage), 0);
		}
	}

	private void smoothScrollToPrePage() {
		if (currentPage > 0) {
			currentPage--;
			if(pointList.size()>currentPage)
			smoothScrollTo(pointList.get(currentPage), 0);
		}
	}

	/**
	 * 下一页
	 */
	public void nextPage() {
		smoothScrollToNextPage();
	}

	/**
	 * 上一页
	 */
	public void prePage() {
		smoothScrollToPrePage();
	}

	/**
	 * 跳转到指定的页面
	 * 
	 * @param page
	 * @return
	 */
	public boolean gotoPage(int page) {
		if (page > 0 && page < subChildCount - 1) {
			if(pointList.size()>page)
				smoothScrollTo(pointList.get(page), 0);
			currentPage = page;
			return true;
		}
		return false;
	}
	@Override
	public void addView(View child) {
		linear_Root.addView(child);
	}

	@Override
	public void addView(View child, android.view.ViewGroup.LayoutParams params) {
		linear_Root.addView(child, params);
	}

	public void addViews(List<View> views) {
		if (views != null && views.size() > 0) {
			for (int i = 0; i < views.size(); i++) {
				addView(views.get(i));
			}
		}
	}
}