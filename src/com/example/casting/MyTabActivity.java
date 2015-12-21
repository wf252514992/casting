package com.example.casting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.casting.listener.ListenerManager;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public abstract class MyTabActivity extends TabActivity {
	private static String TAG_NAME = MyTabActivity.class.getSimpleName();

	private TabHost tabHost;

	private int tabLayout;
	private int selectDrawable;
	private Drawable selectBackground;

	private int textColor = Color.parseColor("#000000");
	private int selectTextColor = Color.parseColor("#FF0000");

	private Map<String, TabView> tabViewMap = new HashMap<String, TabView>();
	private String tabViewTagPrev = null;

	public abstract List<MyTab> getMyTabList();

	public MyTabActivity(int tabLayout, int selectDrawable) {
		this.tabLayout = tabLayout;
		this.selectDrawable = selectDrawable;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(tabLayout);
		tabHost = getTabHost(); // Get TabHost after setContentView()
	}

	public void initTabHost() {
		selectBackground = this.getResources().getDrawable(selectDrawable);

		int index = 0;
		// Create TabSpec for each MyTab. The first tab is the default
		String defaultTag = null;
		TabView defaultTabView = null;
		List<MyTab> myTabList = getMyTabList();
		for (MyTab myTab : myTabList) {
			index++;
			String tag = Integer.toString(index);
			TabView view = new TabView(this, myTab.icon, myTab.selectedicon,
					myTab.text, myTab.type);
			TabSpec tabSpec = tabHost.newTabSpec(tag).setIndicator(view)
					.setContent(new Intent(this, myTab.activity)) // setIndicator(view)
			;
			tabViewMap.put(tag, view);
			tabHost.addTab(tabSpec);
			if (defaultTag == null) {
				defaultTag = tag;
				defaultTabView = view;
			}
		}
		// Listener on tab change
		tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				if(tabId.equals("1")){
					//第一个是 首页动态内容，
					ListenerManager.notifyFreshDynimacform();
				}
				Log.d(TAG_NAME, "change tab: id=" + tabId + ", prevId="
						+ tabViewTagPrev);
				if (tabViewTagPrev != null) {
					// Reset prev tab
					TabView tvPrev = tabViewMap.get(tabViewTagPrev);
					if (tvPrev != null && tvPrev.textView != null) {
						tvPrev.setBackgroundDrawable(null);
						tvPrev.textView.setTextColor(textColor);
						tvPrev.setSelectedImg(false);
					}
				}
				// Set current selected tab
				TabView tv = tabViewMap.get(tabId);
				if (tv != null && tv.textView != null) {
					tv.setBackgroundDrawable(selectBackground);
					tv.textView.setTextColor(selectTextColor);
					tv.setSelectedImg(true);
				}
				//
				tabViewTagPrev = tabId;
			}
		});
		// Set default tab
		if (defaultTag != null) {
			defaultTabView.setBackgroundDrawable(selectBackground);
			if (defaultTabView.textView != null) {
				defaultTabView.textView.setTextColor(selectTextColor);
			}
			defaultTabView.setSelectedImg(true);
			tabViewTagPrev = defaultTag;
		}
	}

	/**
	 * Layout for each TabSpec
	 */
	private class TabView extends LinearLayout {
		private ImageView imageView;
		private TextView textView;
		private int icon = 0;
		private int selectedicon = 0;

		public void setSelectedImg(boolean choiceflag) {
			if (choiceflag) {
				imageView.setImageResource(selectedicon);
			} else {
				imageView.setImageResource(icon);
			}

		}

		public TabView(Context c, int icon, int selectedicon, String text,
				int type) {
			super(c);
			setOrientation(VERTICAL);
			setGravity(Gravity.CENTER);

			imageView = new ImageView(c);
			imageView.setImageDrawable(this.getResources().getDrawable(icon));
			imageView.setBackgroundColor(Color.TRANSPARENT);
			if (type == 1) {
				imageView.setOnClickListener(onclicklistener);
			}
			addView(imageView);

			if (type == 0) {
				textView = new TextView(c);
				textView.setText(text);
				textView.setTextColor(textColor);
				// textView.setGravity(Gravity.CENTER);
				// addView(textView);
			}
			this.selectedicon = selectedicon;
			this.icon = icon;
		}
	}

	/**
	 * Options for each TabSpec. Icon + Text + Activity
	 */
	public class MyTab {
		private int icon;
		private int selectedicon;
		private String text;
		private Class<? extends Activity> activity;
		private int type = 1;

		public MyTab(int type, int icon, int selectedicon, String text,
				Class<? extends Activity> activity) {
			this.icon = icon;
			this.text = text;
			this.activity = activity;
			this.type = type;
			this.selectedicon = selectedicon;
		}
	}

	public void changeView() {
		tabHost.setCurrentTab(4);

	}

	OnClickListener onclicklistener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			startAddActivity();
			System.out.println("MyTabActivity.enclosing_method()");
		}
	};

	public abstract void startAddActivity();
}
