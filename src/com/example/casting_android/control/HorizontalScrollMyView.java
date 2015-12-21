package com.example.casting_android.control;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * 自定义可左右滑动的View , Author: Gordon Date: 2014/12/8 17:21 Todo:
 */
public class HorizontalScrollMyView extends HorizontalScrollView {
	
	private String getTagName = "";
	/**
	 * 根部局
	 */
	LinearLayout linear_Root;

	/**
	 * 内部点击监听器，用于派发 OnItemClick
	 */
	private OnClickListener onClickListener;
	Context context;

	private OnItemMenuClickListener onItemMenuClickListener;

	public HorizontalScrollMyView(Context context) {
		super(context);
		initView(context);
	}

	public HorizontalScrollMyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public HorizontalScrollMyView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private void initView(Context context) {
		// 添加根Layout
		linear_Root = new LinearLayout(context);
		linear_Root.setOrientation(LinearLayout.HORIZONTAL);
		linear_Root.setBackgroundColor(Color.BLACK);
		addView(linear_Root, ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		onClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onItemMenuClickListener != null) {
					onItemMenuClickListener.onItemMenuClick(
							HorizontalScrollMyView.this, v,
							linear_Root.indexOfChild(v));
				}
			}
		};
	}

	public void setOnItemMenuClickListener(
			OnItemMenuClickListener onItemMenuClickListener) {
		this.onItemMenuClickListener = onItemMenuClickListener;
	}

	@Override
	public void addView(View child) {
		linear_Root.addView(child);
		// 把数据放到 tag 中
		child.setOnClickListener(onClickListener);
	}

	@Override
	public void addView(View child, android.view.ViewGroup.LayoutParams params) {
		linear_Root.addView(child, params);
		// 把数据放到 tag 中
		child.setOnClickListener(onClickListener);
	}

	public void addViews(List<View> views) {
		if (views != null && views.size() > 0) {
			for (int i = 0; i < views.size(); i++) {
				addView(views.get(i));
			}
		}
	}

	@Override
	public void removeView(View view) {
		// TODO Auto-generated method stub
		linear_Root.removeView(view);
	}
	@Override
	public void removeViewAt(int index) {
		linear_Root.removeViewAt(index);
	}
	@Override
	public void removeAllViews() {
		// TODO Auto-generated method stub
		linear_Root.removeAllViews();
	}

	public int geCount() {
		return linear_Root.getChildCount();
	}

	public interface OnItemMenuClickListener {
		public void onItemMenuClick(HorizontalScrollMyView scrollMenuView,
				View view, int position);
	}
	
}
