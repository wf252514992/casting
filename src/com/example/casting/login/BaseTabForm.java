package com.example.casting.login;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.gui.CommonDialog;
import com.example.casting.MyTabActivity;
import com.example.casting.processor.BaseProcessor;
import com.example.casting.processor.IBaseProcess;
import com.example.casting_android.R;
import com.ta.TAApplication;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.RequestParams;

public abstract class BaseTabForm extends MyTabActivity {

	public BaseTabForm(int tabLayout, int selectDrawable) {
		super(tabLayout, selectDrawable);
	}

	/**
	 * 手机密度
	 */
	public float density;
	/**
	 * 屏幕宽高
	 */
	public int screen_width;
	public int screen_height;
	private AsyncHttpClient asyncHttpClient;

	public abstract void LeftButtonClick();

	public abstract void RightButtonClick();

	public void initView(View root) {
		left_layout = (LinearLayout) root.findViewById(R.id.left_layout);
		right_layout = (LinearLayout) root.findViewById(R.id.right_layout);
		titletext = (TextView) root.findViewById(R.id.titletext);
		right_txt = (TextView) root.findViewById(R.id.txt_right);
	}

	LinearLayout right_layout;
	TextView right_txt;
	LinearLayout left_layout;
	TextView titletext;
	
	public TAApplication getTAApplication()
	{
		return (TAApplication) getApplication();
	}
	/**
	 * 是否显示标题栏左边的按钮
	 * 
	 * @param visiable
	 * @param text
	 *            按钮上的文本
	 */
	public void setLeftButtonAble(boolean visiable, String text) {
		if (visiable) {
			// btn_left.setText(text);
			left_layout.setOnClickListener(onclick);
			left_layout.setVisibility(View.VISIBLE);

		} else {
			left_layout.setVisibility(View.GONE);
		}
	}

	OnClickListener onclick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (arg0.getId() == R.id.left_layout) {
				LeftButtonClick();
			} else if (arg0.getId() == R.id.right_layout) {
				RightButtonClick();
			}
		}
	};

	/**
	 * 是否显示标题栏右边的按钮
	 * 
	 * @param visiable
	 * @param text
	 *            按钮上的文本
	 */
	public void setRightButtonAble(boolean visiable, String text) {
		if (visiable) {
			right_layout.setOnClickListener(onclick);
			right_layout.setVisibility(View.VISIBLE);
			right_txt.setText(text);
		} else {
			right_layout.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置标题
	 * 
	 * @param text
	 */
	public void setTitle(String text) {
		titletext.setText(text);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		density = metric.density;
		screen_width = (int) (metric.widthPixels);
		screen_height = (int) (metric.heightPixels);
		asyncHttpClient = new AsyncHttpClient();
	}

	Dialog dialog;

	/**
	 * 等待框show
	 */
	public void showWaitDialog() {
		if (dialog == null) {
			dialog = CommonDialog.ProgressDialog(this);

			// dialog = new ProgressDialog(this);
			// dialog.setCancelable(false);
		}
		if (!dialog.isShowing())
			dialog.show();
	}

	/**
	 * 等待框 dismiss
	 */
	public void dismissDialog() {
		if (dialog != null && dialog.isShowing())
			dialog.dismiss();
	}

	/**
	 * 显示消息
	 * @param val
	 */
	public void showToast(String val) {
		Toast.makeText(this, val, Toast.LENGTH_LONG).show();
	}
	public static final int  GETRESULT = 10110;
	private final Handler subhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GETRESULT: {
				Bundle bd = (Bundle) msg.obj;
				String result = bd.getString("value");
				BaseProcessor processor = (BaseProcessor)bd.getSerializable("processor");
				OnReturn(result, processor);
			}break;
			}
		}
	};
	
	class MyResponseHandler extends AsyncHttpResponseHandler{
		BaseProcessor processor ;
		public MyResponseHandler(BaseProcessor processor){
			this.processor = processor;
		}
		public void onSuccess(String content) {
			super.onSuccess(content);
			//得到返回数据
			Bundle bd =new Bundle();
			bd.putString("value", content);
			bd.putSerializable("processor", processor);
			Message msg = new Message() ;
			msg.what = GETRESULT;
			msg.obj = bd;
			subhandler.sendMessage(msg);
		};

		public void onFailure(Throwable error) {
			showToast(error.getMessage());
		};

		public void onFinish() {
			dismissDialog();

		};
	}
	/**
	 * 
	 * @param showdialog 是否显示等待框
	 * @param processor 接口处理器
	 * @param obj 接口数据
	 */
	public void HttpCall(boolean showdialog ,BaseProcessor processor, Object obj) {
		if(showdialog){
			showWaitDialog();
		}
		Map<String, String> map = processor.Bean2Map(obj);
		if (map == null) {
			showToast("参数错误，请联系管理员");
			return;
		}
		RequestParams params = new RequestParams(map);
		asyncHttpClient.get(processor.getUrl(), params, new MyResponseHandler(processor));
	}
	/**
	 * 网络访问
	 * 
	 * @param processor
	 * @param obj
	 */
	public void HttpCall(BaseProcessor processor, Object obj) {
		HttpCall( true,processor,obj);
	}
	
	/**
	 * json网络访问
	 * 
	 * @param processor
	 * @param obj
	 */
	public void HttpCall(BaseProcessor processor, Object obj,String jsonName) {
		showWaitDialog();
		
		try {
			JSONObject json = processor.Bean2Json(obj);
			if (json == null) {
				showToast("参数错误，请联系管理员");
				return;
			}
			Map<String,String> parammap = new HashMap<String, String>();
			parammap.put(jsonName, json.toString());
			RequestParams params = new RequestParams(parammap);
			asyncHttpClient.post(processor.getUrl(), params, new MyResponseHandler(processor));
		} catch (JSONException e) {
			e.printStackTrace();
			showToast("参数错误，请联系管理员");
		}
		
	}

	/**
	 * 网络 返回结果
	 * 
	 * @param content
	 */
	public void OnReturn(String content , IBaseProcess processor){};

}
