package com.example.casting_android.control;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.casting_android.R;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class JTableView extends LinearLayout {

	private int mode = 1;
	public final static String TITLE = "title";
	public final static int MODE_SCREENING = 1;
	public final static int MODE_DETAILS = 2;

	private final static int TEXTSIZE = 18;

	private List<String> conditions;
	private String defaultTitle = "记录";

	private LinearLayout.LayoutParams params, txtParams1, txtParams2;
	private Context context;
	// JTableView适配器
	private int index = -1;
	private OnJTableItemClickListener listener;

	public void setMode(int mode) {
		this.mode = mode;
	}

	public void setTitle(String title) {
		this.defaultTitle = title;
	}

	public JTableView(Context context) {
		super(context);
		this.context = context;
		initView();

	}

	public JTableView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		this.context = context;
		initView();
	}

	public void setConditions(List<String> conditions) {
		this.conditions = conditions;
	}

	private void initView() {

		setBackgroundColor(Color.WHITE);
		setOrientation(LinearLayout.VERTICAL);


		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		txtParams1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		txtParams1.weight = 7;
		txtParams2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		txtParams2.weight = 3;
	}

	public void bindData(Map<String, String> data) {
		bindData(data, null);
	}

	public void bindData(Map<String, String> data, List<String> conditions) {
		this.conditions = conditions;
		fillView(data);
	}

	/**
	 * 使用异步的方式加载数据
	 * 
	 * @param datas
	 *            数据源,如果每天数据都需要插入标题,就需要在每个HashMap中添加一个键为JTableView常量TITLE的数据
	 */
	public void bindData(List<Map<String, String>> datas) {
		bindData(datas, null);
	}

	/**
	 * 给与固定的字段显示
	 * 
	 * @param datas
	 *            数据源,如果每天数据都需要插入标题,就需要在每个HashMap中添加一个键为JTableView常量TITLE的数据
	 * @param conditions
	 *            需要显示的字段
	 */
	public void bindData(List<Map<String, String>> datas,
			List<String> conditions) {
		this.conditions = conditions;
		new LoadTask(datas).execute();
	}

	public void fillView(Map<String, String> data) {

		if (data.containsKey(TITLE)) {
			addCusstomText(data.get(TITLE));
			data.remove(TITLE);
		}
		Iterator<String> iterator = data.keySet().iterator();
		while (iterator.hasNext()) {

			String name = iterator.next();

			if (conditions != null) {
				if (!conditions.contains(name)) {
					continue;
				}
			}

			String value = data.get(name);
			if (mode == MODE_SCREENING) {
				if (value != null && value.length() > 0) {
					addView(name, value);
				}
			} else {
				addView(name, value);
			}
		}
		invalidate();
	}

	/**
	 * 可以用于添加头部
	 * 
	 * @param view
	 */
	public void addCusstomView(View view) {
		LinearLayout itemLayout = new LinearLayout(context);
		itemLayout.setOrientation(LinearLayout.VERTICAL);
		itemLayout.setBackgroundColor(Color.WHITE);
		itemLayout.setGravity(Gravity.CENTER);
		itemLayout.addView(view, params);
		this.addView(itemLayout, 0, params);
	}

	/**
	 * 可用户中间添加分割标题
	 * 
	 * @param txt
	 */
	public void addCusstomText(String txt, int color) {
		LinearLayout itemLayout = new LinearLayout(context);
		itemLayout.setOrientation(LinearLayout.VERTICAL);
		itemLayout.setBackgroundColor(Color.WHITE);
		itemLayout.setGravity(Gravity.CENTER);
		TextView txt_Name = new TextView(context);
		txt_Name.setTextSize(TEXTSIZE + 2);
		txt_Name.setTextColor(color);
		txt_Name.setText(txt);
		txt_Name.setBackgroundResource(R.drawable.tableview_bolder_bg);
		txt_Name.setPadding(8, 6, 6, 6);
		txt_Name.setGravity(Gravity.CENTER_VERTICAL);

		itemLayout.addView(txt_Name);
		this.addView(itemLayout);
	}

	public void addCusstomText(String text) {
		// 默认标题为蓝色
		this.addCusstomText(text, Color.BLUE);
	}

	public void setOnJTableItemClickListener(OnJTableItemClickListener listener) {
		this.listener = listener;
	}

	public void addView(String name, String value) {
		final LinearLayout itemLayout = new LinearLayout(context);
		itemLayout.setOrientation(LinearLayout.HORIZONTAL);
		itemLayout.setBackgroundColor(Color.WHITE);
		TextView txt_Name = new TextView(context);
		txt_Name.setTextSize(TEXTSIZE);
		txt_Name.setTextColor(Color.BLACK);
		txt_Name.setText(name);
		txt_Name.setBackgroundResource(R.drawable.tableview_bolder_bg);
		txt_Name.setPadding(8, 6, 6, 6);
		txt_Name.setGravity(Gravity.CENTER_VERTICAL);
		TextView txt_Value = new TextView(context);
		txt_Value.setTextSize(TEXTSIZE);
		txt_Value.setText(value);
		txt_Value.setTextColor(Color.BLACK);
		txt_Value.setGravity(Gravity.CENTER_VERTICAL);
		txt_Value.setBackgroundResource(R.drawable.tableview_bolder_bg);
		txt_Value.setPadding(8, 6, 6, 6);
		itemLayout.addView(txt_Name, txtParams1);
		itemLayout.addView(txt_Value, txtParams2);
		itemLayout.setTag(index);
		// itemLayout.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// if (listener != null) {
		// listener.onItemClick(JTableView.this,
		// (Integer) itemLayout.getTag());
		// }
		// }
		//
		// });
		itemLayout.setOnTouchListener(new OnTouchListener1(itemLayout) {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP
						&& listener != null) {
					listener.onItemClick(JTableView.this,
							(Integer) itemLayout.getTag());
					return false;
				} else {
					return true;
				}
			}
		});
		this.addView(itemLayout, params);
	}


	abstract class OnTouchListener1 implements OnTouchListener {
		public OnTouchListener1(ViewGroup container) {
		}
	}

	class LoadTask extends AsyncTask<Void, Integer, Void> {
		List<Map<String, String>> datas;

		public LoadTask(List<Map<String, String>> datas) {
			this.datas = datas;
		}

		@Override
		protected Void doInBackground(Void... params) {
			for (int i = 0; i < datas.size(); i++) {
				publishProgress(i);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);

			int index = values[0];
			JTableView.this.index = index;
			JTableView.this.addCusstomText(defaultTitle + index);
			fillView((Map<String, String>) datas.get(index));
		}

	}

}
