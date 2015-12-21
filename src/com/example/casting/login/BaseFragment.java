package com.example.casting.login;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import cn.smssdk.gui.CommonDialog;

import com.example.casting.login.findpwd.FindPWDSuccessForm;
import com.example.casting.login.findpwd.FindPWDThirdForm;
import com.example.casting.login.regist.CompanyRegistActivity;
import com.example.casting.login.regist.RegistDetailActivity;
import com.example.casting.login.regist.UpdateIntrouction;
import com.example.casting.login.regist.UpdateLabelForm;
import com.example.casting.login.regist.UpdateLabelNForm;
import com.example.casting.login.regist.UserInfoActivity;
import com.example.casting.me.AttentionListActivity;
import com.example.casting.me.MeActivity;
import com.example.casting.me.MyDataActivity;
import com.example.casting.me.MyFansActivity;
import com.example.casting.me.MyInfoActivity;
import com.example.casting.processor.BaseProcessor;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.square.SquareActivity;
import com.example.casting.xtsz.AboutMeActivity;
import com.example.casting.xtsz.FeedBackActivity;
import com.example.casting.xtsz.ManagerIDsActivity;
import com.example.casting.xtsz.NotifactionActivity;
import com.example.casting.xtsz.Set_AboutmeActivity;
import com.example.casting.xtsz.Set_AttentionActivity;
import com.example.casting.xtsz.Set_FansActivity;
import com.example.casting.xtsz.Set_ReviewActivity;
import com.example.casting.xtsz.XtszActivity;
import com.example.casting_android.R;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.RequestParams;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public abstract class BaseFragment extends Fragment {
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

	public abstract void LeftButtonClick();

	public abstract void RightButtonClick();

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
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		DisplayMetrics metric = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
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
			dialog = CommonDialog.ProgressDialog(getActivity());

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

	/**
	 * 网络访问
	 * 
	 * @param processor
	 * @param obj
	 */
	public void HttpCall(BaseProcessor processor, Object obj) {
		showWaitDialog();
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
		showWaitDialog();

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

	/**
	 * 显示消息
	 * 
	 * @param val
	 */
	public void showToast(String val) {
		Toast.makeText(getActivity(), val, Toast.LENGTH_LONG).show();
	}

	/**
	 * 网络 返回结果
	 * 
	 * @param content
	 */
	public void OnReturn(String content, IBaseProcess processor) {
	};
	/**
	 * 
	 * @param activity 需跳转的activity
	 * @param bd  参数
	 * @param requestcode 
	 */
	public void doActivity(int activity , Bundle bd,int requestcode){
		Intent intent = getIntent(activity);
		intent.putExtras(bd);
		startActivityForResult(intent, requestcode);
	}
	/**
	 * 
	 * @param activity
	 * @param requestcode
	 */
	public void doActivity(int activity,int requestcode){
		Intent intent = getIntent(activity);
		startActivityForResult(intent, requestcode);
	}
	public void doActivity(int activity , Bundle bd){
		Intent intent = getIntent(activity);
		intent.putExtras(bd);
		startActivity(intent);
		
	}
	public void doActivity(int activity){
		Intent intent = getIntent(activity);
		startActivity(intent);
	}
	private Intent getIntent(int activity){
		Intent intent = new Intent();
		switch (activity) {
		case R.string.castingfirstform:
			intent.setClass(getActivity(), CastingFirstForm.class);
			break;
		case R.string.userinfoactivity:
			intent.setClass(getActivity(), UserInfoActivity.class);
			break;
		case R.string.xtszactivity:
			intent.setClass(getActivity(), XtszActivity.class);
			break;
		case R.string.findpwdthirdform:
			intent.setClass(getActivity(), FindPWDThirdForm.class);
			break;
		case R.string.findpwdsuccessform:
			intent.setClass(getActivity(), FindPWDSuccessForm.class);
			break;
		case R.string.manageridsactivity:
			intent.setClass(getActivity(), ManagerIDsActivity.class);
			break;
		case R.string.loginform:
			intent.setClass(getActivity(), LoginForm.class);
			break;
		case R.string.notifactionactivity:
			intent.setClass(getActivity(), NotifactionActivity.class);
			break;
		case R.string.feedbackactivity:
			intent.setClass(getActivity(), FeedBackActivity.class);
			break;
		case R.string.aboutmeactivity:
			intent.setClass(getActivity(), AboutMeActivity.class);
			break;
		case R.string.set_aboutmeactivity:
			intent.setClass(getActivity(), Set_AboutmeActivity.class);
			break;
		case R.string.set_attentionactivity:
			intent.setClass(getActivity(), Set_AttentionActivity.class);
			break;
		case R.string.set_fansactivity:
			intent.setClass(getActivity(), Set_FansActivity.class);
			break;
		case R.string.set_reviewactivity:
			intent.setClass(getActivity(), Set_ReviewActivity.class);
			break;
		case R.string.squareactivity:
			intent.setClass(getActivity(), SquareActivity.class);
			break;
		case R.string.meactivity:
			intent.setClass(getActivity(), MeActivity.class);
			break;
		case R.string.myinfoactivity:
			intent.setClass(getActivity(), MyInfoActivity.class);
			break;
		case R.string.mydataactivity:
			intent.setClass(getActivity(), MyDataActivity.class);
			break;
		case R.string.attentionlistactivity:
			intent.setClass(getActivity(), AttentionListActivity.class);
			break;
		case R.string.updateintrouction:
			intent.setClass(getActivity(), UpdateIntrouction.class);
			break;
		case R.string.updatelabelnform:
			intent.setClass(getActivity(), UpdateLabelNForm.class);
			break;
		case R.string.registdetailactivity:
			intent.setClass(getActivity(), RegistDetailActivity.class);
			break;
		case R.string.companyregistactivity:
			intent.setClass(getActivity(), CompanyRegistActivity.class);
			break;
		case R.string.myfansactivity:
			intent.setClass(getActivity(), MyFansActivity.class);
			break;
		default:
			intent.setClass(getActivity(), LoginForm.class);
			break;
		}
		return intent;
	}
}
