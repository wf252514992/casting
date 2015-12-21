package com.example.casting.login;

import static cn.smssdk.framework.utils.R.getStringRes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.casting.processor.BaseProcessor;
import com.example.casting.processor.ErrorConvert;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.data.ResultBean;
import com.example.casting_android.R;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.RequestParams;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.analysis.MobclickAgent;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.framework.FakeActivity;
import cn.smssdk.gui.CommonDialog;
import cn.smssdk.gui.SMSReceiver;

public abstract class RquestMessageBaseDialog extends FakeActivity {

	Dialog pd;
	private AsyncHttpClient asyncHttpClient;

	// 获取所在国家编号
	public String getCountryCode() {
		String[] country = getCurrentCountry();
		if (country != null) {
			return country[1];
		}
		return DEFAULT_COUNTRY_ID;
	}

	// 默认使用中国区号
	private static final String DEFAULT_COUNTRY_ID = "42";

	/**
	 * 成功获取验证码
	 * 
	 * @param verifyCode
	 */
	public abstract void getCheckNumSuccess(String verifyCode);

	/**
	 * 成功获取验证码后的操作
	 */
	public abstract void afterVerificationCodeRequested();

	// 国家号码规则
	private HashMap<String, String> countryRules;

	/**
	 * 初始化 国家列表
	 * 
	 * @param countries
	 */
	public void onCountryListGot(ArrayList<HashMap<String, Object>> countries) {
		// 解析国家列表
		for (HashMap<String, Object> country : countries) {
			String code = (String) country.get("zone");
			String rule = (String) country.get("rule");
			if (TextUtils.isEmpty(code) || TextUtils.isEmpty(rule)) {
				continue;
			}

			if (countryRules == null) {
				countryRules = new HashMap<String, String>();
			}
			countryRules.put(code, rule);
		}
	}

	public boolean isLoadingCountryRules() {
		if (countryRules == null) {
			return false;
		}
		return true;

	}

	/**
	 * 获取支持的国家
	 */
	public void getSupportedCountries() {
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
		pd = CommonDialog.ProgressDialog(activity);
		if (pd != null) {
			pd.show();
		}

		SMSSDK.getSupportedCountries();
	}

	public void showDialog() {
		if (pd != null) {
			pd.show();
		}
	}

	public void dismissDialog() {
		if (pd != null) {
			pd.dismiss();
		}
	}

	/**
	 * 检查电话号码 发送验证码请求
	 * 
	 * @param phone
	 *            电话号码
	 * @param code
	 *            国家代码
	 */
	public void checkPhoneNum(String phone, String code) {
		if (code.startsWith("+")) {
			code = code.substring(1);
		}

		if (TextUtils.isEmpty(phone)) {
			int resId = getStringRes(activity, "smssdk_write_mobile_phone");
			if (resId > 0) {
				Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
			}
			return;
		}

		String rule = countryRules.get(code);
		Pattern p = Pattern.compile(rule);
		Matcher m = p.matcher(phone);
		int resId = 0;
		if (!m.matches()) {
			resId = getStringRes(activity, "smssdk_write_right_mobile_phone");
			if (resId > 0) {
				Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
			}
			return;
		}
		// 弹出对话框，发送验证码
		// showDialog(phone, code);
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
		pd = CommonDialog.ProgressDialog(activity);
		if (pd != null) {
			pd.show();
		}
		SMSSDK.getVerificationCode(code, phone.trim());
	}

	private BroadcastReceiver smsReceiver = new SMSReceiver(
			new SMSSDK.VerifyCodeReadListener() {
				@Override
				public void onReadVerifyCode(final String verifyCode) {
					runOnUIThread(new Runnable() {
						@Override
						public void run() {
							getCheckNumSuccess(verifyCode);
						}
					});
				}
			});

	public void submitVerificationCode(String phone, String verificationcode) {
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
		pd = CommonDialog.ProgressDialog(activity);
		if (pd != null) {
			pd.show();
		}
		SMSSDK.submitVerificationCode(getCountryCode(), phone, verificationcode);
	}

	public void show(Context arg0) {
		// TODO Auto-generated method stub
		super.show(arg0, null);
	}

	LinearLayout right_layout;
	TextView right_txt;
	LinearLayout left_layout;
	View titleLayout;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		if (smsReceiver != null) {
			activity.registerReceiver(smsReceiver, new IntentFilter(
					"android.provider.Telephony.SMS_RECEIVED"));
		}
		asyncHttpClient = new AsyncHttpClient();
		SMSSDK.registerEventHandler(handler);
		countDown();
	}

	public void initView(ViewGroup root) {
		titleLayout = activity.findViewById(R.id.titlelayout);
		left_layout = (LinearLayout) titleLayout.findViewById(R.id.left_layout);
		right_layout = (LinearLayout) titleLayout
				.findViewById(R.id.right_layout);
	}

	private EventHandler handler = new EventHandler() {
		@SuppressWarnings("unchecked")
		public void afterEvent(final int event, final int result,
				final Object data) {
			runOnUIThread(new Runnable() {
				public void run() {
					if (pd != null && pd.isShowing()) {
						pd.dismiss();
					}
					if (result == SMSSDK.RESULT_COMPLETE) {
						if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
							// 请求支持国家列表
							onCountryListGot((ArrayList<HashMap<String, Object>>) data);
						} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
							// 获取验证码成功后的执行动作
							afterGet(result, data);
						} else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
							// 提交验证码
							afterSubmit(data);
						}
					} else {
						// 根据服务器返回的网络错误，给toast提示
						try {
							((Throwable) data).printStackTrace();
							Throwable throwable = (Throwable) data;

							JSONObject object = new JSONObject(
									throwable.getMessage());
							String des = object.optString("detail");
							if (!TextUtils.isEmpty(des) && activity != null) {
								Toast.makeText(activity, des,
										Toast.LENGTH_SHORT).show();
								return;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						// 如果木有找到资源，默认提示
						if (activity != null)
							Toast.makeText(activity, "服务器返回的网络错误",
									Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
	};

	private static final int RETRY_INTERVAL = 60;
	private int time = RETRY_INTERVAL;

	/**
	 * 获取验证码成功后,的执行动作
	 * 
	 * @param result
	 * @param data
	 */
	private void afterGet(final int result, final Object data) {
		runOnUIThread(new Runnable() {
			public void run() {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				afterVerificationCodeRequested();
				time = 60;
				countDown();
			}
		});
	}

	/**
	 * 消息过时后界面提示
	 */
	public abstract void whenTimeoutofMessage();

	/**
	 * 显示消息倒计时
	 */
	public abstract void releaseShowingMessage();

	// 倒数计时
	private void countDown() {
		runOnUIThread(new Runnable() {
			public void run() {
				time--;
				if (time == 0) {
					whenTimeoutofMessage();
					time = RETRY_INTERVAL;
				} else {
					releaseShowingMessage();
					runOnUIThread(this, 1000);
				}
			}
		}, 1000);
	}

	/**
	 * 成功提交验证码后
	 * 
	 * @param res
	 *            返回的验证数据
	 */
	public abstract void submitCheckNumSuccess(HashMap<String, Object> res);

	/**
	 * 提交验证码成功后的执行事件
	 * 
	 * @param result
	 * @param data
	 */
	private void afterSubmit(final Object data) {
		runOnUIThread(new Runnable() {
			public void run() {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				HashMap<String, Object> res = new HashMap<String, Object>();
				res.put("res", true);
				res.put("page", 2);
				res.put("phone", data);
				submitCheckNumSuccess(res);
				// setResult(res);
				// finish();
			}
		});
	}

	public boolean onFinish() {
		SMSSDK.unregisterEventHandler(handler);
		activity.unregisterReceiver(smsReceiver);
		return super.onFinish();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("RegisterPage");
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("RegisterPage");
	}

	// 分割电话号码
	public String splitPhoneNum(String phone) {
		StringBuilder builder = new StringBuilder(phone);
		builder.reverse();
		for (int i = 4, len = builder.length(); i < len; i += 5) {
			builder.insert(i, ' ');
		}
		builder.reverse();
		return builder.toString();
	}

	public String[] getCurrentCountry() {
		String mcc = getMCC();
		String[] country = null;
		if (!TextUtils.isEmpty(mcc)) {
			country = SMSSDK.getCountryByMCC(mcc);
		}

		if (country == null) {
			Log.w("SMSSDK", "no country found by MCC: " + mcc);
			country = SMSSDK.getCountry(DEFAULT_COUNTRY_ID);
		}
		return country;
	}

	private String getMCC() {
		TelephonyManager tm = (TelephonyManager) activity
				.getSystemService(Context.TELEPHONY_SERVICE);
		// 返回当前手机注册的网络运营商所在国家的MCC+MNC. 如果没注册到网络就为空.
		String networkOperator = tm.getNetworkOperator();

		// 返回SIM卡运营商所在国家的MCC+MNC. 5位或6位. 如果没有SIM卡返回空
		String simOperator = tm.getSimOperator();

		String mcc = null;
		if (!TextUtils.isEmpty(networkOperator)
				&& networkOperator.length() >= 5) {
			mcc = networkOperator.substring(0, 3);
		}

		if (TextUtils.isEmpty(mcc)) {
			if (!TextUtils.isEmpty(simOperator) && simOperator.length() >= 5) {
				mcc = simOperator.substring(0, 3);
			}
		}

		return mcc;
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

	public abstract void LeftButtonClick();

	public abstract void RightButtonClick();

	/**
	 * 是否显示标题栏右边的按钮
	 * 
	 * @param visiable
	 * @param text
	 *            按钮上的文本
	 */
	public void setRightButtonAble(boolean visiable, String text) {
		if (visiable) {
			right_txt.setText(text);
			right_layout.setOnClickListener(onclick);
			right_layout.setVisibility(View.VISIBLE);
		} else {
			right_layout.setVisibility(View.GONE);
		}
	}

	public ResultBean getResultCode(String content) {
		try {
			JSONObject json = new JSONObject(content);
			String val = json.getString("return");
			int code = Integer.parseInt(val);
			String info = ErrorConvert.getErrorDiscribe(code);
			return new ResultBean(code, info);
		} catch (Exception ex) {
			return new ResultBean(Errors.ERROR_LOCAL_PARSE,
					ErrorConvert.getErrorDiscribe(Errors.ERROR_LOCAL_PARSE));

		}
	}

	/**
	 * 设置标题
	 * 
	 * @param text
	 */
	public void setTitle(String text) {
		if (titleLayout == null) {
			return;
		}
		((TextView) titleLayout.findViewById(R.id.titletext)).setText(text);
	}

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
			showToast(error.getMessage());
		};

		public void onFinish() {
			dismissDialog();

		};
	}

	public void showToast(String val) {
		Toast.makeText(activity, val, Toast.LENGTH_LONG).show();

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
				OnReturn(result, processor);
			}
				break;
			}
		}
	};

	/**
	 * 网络 返回结果
	 * 
	 * @param content
	 */
	public void OnReturn(String content, IBaseProcess processor) {
	};

	/**
	 * 网络访问
	 * 
	 * @param processor
	 * @param obj
	 */
	public void HttpCall(BaseProcessor processor, Object obj) {
		showDialog();
		Map<String, String> map = processor.Bean2Map(obj);
		if (map == null) {
			showToast("参数错误，请联系管理员");
			return;
		}
		RequestParams params = new RequestParams(map);
		asyncHttpClient.get(processor.getUrl(), params, new MyResponseHandler(
				processor));
	}

	/**
	 * json网络访问
	 * 
	 * @param processor
	 * @param obj
	 */
	public void HttpCall(BaseProcessor processor, Object obj, String jsonName) {
		showDialog();
		try {
			JSONObject json = processor.Bean2Json(obj);
			if (json == null) {
				showToast("参数错误，请联系管理员");
				return;
			}
			Map<String, String> parammap = new HashMap<String, String>();
			parammap.put(jsonName, json.toString());
			RequestParams params = new RequestParams(parammap);
			asyncHttpClient.post(processor.getUrl(), params,
					new MyResponseHandler(processor));
		} catch (JSONException e) {
			e.printStackTrace();
			showToast("参数错误，请联系管理员");
		}

	}
}
