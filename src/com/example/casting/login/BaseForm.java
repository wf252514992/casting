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
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.gui.CommonDialog;
import com.example.casting.processor.BaseProcessor;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.util.Session;
import com.example.casting.util.view.AlertView;
import com.example.casting_android.R;
import com.ta.TAApplication;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.RequestParams;
import com.ta.util.netstate.TANetWorkUtil.netType;

public abstract class BaseForm extends CastingBaseForm {

	/**
	 * 手机密度
	 */
	public static float density;
	/**
	 * 屏幕宽高
	 */
	public static int screen_width;
	public static int screen_height;
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

	public TAApplication getTAApplication() {
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
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		density = metric.density;
		screen_width = (int) (metric.widthPixels);
		screen_height = (int) (metric.heightPixels);
		asyncHttpClient = new AsyncHttpClient();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		returnflag = true;
	}

	@Override
	public void onNetConnect(netType type) {
		// TODO Auto-generated method stub
		super.onNetConnect(type);
		Toast.makeText(this, "网络连接开启", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onNetDisConnect() {
		// TODO Auto-generated method stub
		super.onNetDisConnect();
		Toast.makeText(this, "网络连接关闭", Toast.LENGTH_LONG).show();
	}

	Dialog dialog;

	/**
	 * 等待框show
	 */
	public void showWaitDialog() {
		showWaitDialog(true);
	}
	/**
	 * 等待框show
	 * @param cancelable
	 */
	public void showWaitDialog(boolean cancelable) {
		if (dialog == null) {
			dialog = CommonDialog.ProgressDialog(this);
			dialog.setCancelable(cancelable);
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
	 * 
	 * @param val
	 */
	public void showToast(String val) {
		if(val!=null && val.length()>0){
			Toast.makeText(this, val, Toast.LENGTH_SHORT).show();
		}
	}

	public static final int GETRESULT = 10110;
	private final Handler subhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GETRESULT: {
				Bundle bd = (Bundle) msg.obj;
				String result = bd.getString("value");
				BaseProcessor processor = (BaseProcessor) bd
						.getSerializable("processor");
				if (returnflag)
					OnReturn(result, processor);
			}
				break;
			}
		}
	};

	class MyResponseHandler extends AsyncHttpResponseHandler {
		BaseProcessor processor;

		public MyResponseHandler(BaseProcessor processor) {
			this.processor = processor;
		}

		public void onSuccess(String content) {
			super.onSuccess(content);
			// 得到返回数据
			Bundle bd = new Bundle();
			bd.putString("value", content);
			bd.putSerializable("processor", processor);
			Message msg = new Message();
			msg.what = GETRESULT;
			msg.obj = bd;
			subhandler.sendMessage(msg);
		};

		public void onFailure(Throwable error) {
			JSONObject json = new JSONObject();
			try {
				json.put("return", Errors.ERROR_LOCAL_REQUESTERROR + "");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Bundle bd = new Bundle();
			bd.putString("value", json.toString());
			bd.putSerializable("processor", processor);
			Message msg = new Message();
			msg.what = GETRESULT;
			msg.obj = bd;
			subhandler.sendMessage(msg);
		};

		public void onFinish() {
			dismissDialog();

		};
	}

	/**
	 * 
	 * @param showdialog
	 *            是否显示等待框
	 * @param processor
	 *            接口处理器
	 * @param obj
	 *            接口数据
	 */
	public void HttpCall(boolean showdialog, BaseProcessor processor, Object obj) {
		Map<String, String> map = processor.Bean2Map(obj);
		if (map == null) {
//			showToast("参数错误，请联系管理员");
			showToast("失败");
			return;
		}
		if (showdialog) {
			showWaitDialog();
		}
		RequestParams params = new RequestParams(map);
		asyncHttpClient.get(processor.getUrl(), params, new MyResponseHandler(
				processor));
	}

	/**
	 * 
	 * @param showdialog
	 *            是否显示等待框
	 * @param processor
	 *            接口处理器
	 * @param obj
	 *            接口数据
	 */
	public void HttpPost(boolean showdialog, BaseProcessor processor, Object obj) {
		Map<String, String> map = processor.Bean2Map(obj);
		if (map == null) {
			showToast("失败");
			return;
		}
		if (showdialog) {
			showWaitDialog();
		}
		RequestParams params = new RequestParams(map);
		asyncHttpClient.post(processor.getUrl(), params, new MyResponseHandler(
				processor));
	}

	/**
	 * 网络访问
	 * 
	 * @param processor
	 * @param obj
	 */
	public void HttpCall(BaseProcessor processor, Object obj) {
		HttpCall(true, processor, obj);
	}

	/**
	 * 网络访问
	 * 
	 * @param processor
	 * @param obj
	 */
	public void HttpPost(BaseProcessor processor, Object obj) {
		HttpPost(true, processor, obj);
	}

	private boolean returnflag = false;

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		returnflag = false;
	}

	/**
	 * json网络访问
	 * 
	 * @param processor
	 * @param obj
	 */
	public void HttpCall(BaseProcessor processor, Object obj, String jsonName) {
		HttpCall(true, processor,obj,jsonName);
	}
	
	/**
	 * json网络访问
	 * 
	 * @param processor
	 * @param obj
	 */
	public void HttpCall(boolean showdialog,BaseProcessor processor, Object obj, String jsonName) {

		try {
			JSONObject json = processor.Bean2Json(obj);
			if (json == null) {
				showToast("参数错误，请联系管理员");
				return;
			}
			Map<String, String> parammap = new HashMap<String, String>();
			parammap.put(jsonName, json.toString());
			RequestParams params = new RequestParams(parammap);
			if(showdialog)
				showWaitDialog();
			asyncHttpClient.post(processor.getUrl(), params,
					new MyResponseHandler(processor));
		} catch (JSONException e) {
			e.printStackTrace();
			showToast("参数错误，请联系管理员");
		}

	}

	public int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	/**
	 * 网络 返回结果
	 * 
	 * @param content
	 */
	public void OnReturn(String content, IBaseProcess processor) {
	};

	Dialog Alertdialog;

	/**
	 * 提示框
	 */
	public void showAlertDialog(String value) {
		AlertView alertview = new AlertView(this, dialogclick);
		alertview.setContent(value);
		Alertdialog = new Dialog(this, R.style.dialog);
		Alertdialog.setContentView(alertview.getView());
		Alertdialog.show();

	}
	/**
	 * 提示框
	 */
	public void showAlertDialog(String value,boolean cancelable) {
		AlertView alertview = new AlertView(this, dialogclick);
		alertview.setContent(value);
		Alertdialog = new Dialog(this, R.style.dialog);
		Alertdialog.setContentView(alertview.getView());
		Alertdialog.setCancelable(cancelable);
		Alertdialog.show();

	}
	
	/**
	 * 提示框
	 */
	public void showAlertDialog(String value,String left,boolean cancelable) {
		AlertView alertview = new AlertView(this, dialogclick);
		alertview.setContent(value,left);
		Alertdialog = new Dialog(this, R.style.dialog);
		Alertdialog.setContentView(alertview.getView());
		Alertdialog.setCancelable(cancelable);
		Alertdialog.show();

	}
	/**
	 * 提示框
	 */
	public void showAlertDialog(String value,String left,String right,boolean cancelable) {
		AlertView alertview = new AlertView(this, dialogclick);
		alertview.setContent(value,left,right);
		Alertdialog = new Dialog(this, R.style.dialog);
		Alertdialog.setContentView(alertview.getView());
		Alertdialog.setCancelable(cancelable);
		Alertdialog.show();

	}
	/**
	 * 提示框
	 */
	public void showAlertDialog(String value,String left,String right) {
		AlertView alertview = new AlertView(this, dialogclick);
		alertview.setContent(value,left,right);
		Alertdialog = new Dialog(this, R.style.dialog);
		Alertdialog.setContentView(alertview.getView());
		Alertdialog.show();

	}
	public void leftDialogOnclick() {

	}

	public void rightDialogOnclick() {

	}

	/**
	 * 提示框事件
	 */
	AlertView.ViewClick dialogclick = new AlertView.ViewClick() {

		@Override
		public void rightClick(Object obj) {
			// TODO Auto-generated method stub
			Alertdialog.dismiss();
			rightDialogOnclick();
		}

		@Override
		public void leftClick(Object obj) {
			// TODO Auto-generated method stub
			Alertdialog.dismiss();
			leftDialogOnclick();
		}
	};
}