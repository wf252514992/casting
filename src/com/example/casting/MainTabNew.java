package com.example.casting;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import com.example.casting.listener.CloseAllListener;
import com.example.casting.listener.ListenerManager;
import com.example.casting.listener.TabHostChangeListener;
import com.example.casting.login.BaseTabForm;
import com.example.casting.mailbox.MailboxActivity;
import com.example.casting.me3.CompanyInfoActivity;
import com.example.casting.me3.DirectorInfoActivity;
import com.example.casting.me3.Me3Activity;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.publisheddynamic.PublishedDynamic;
import com.example.casting.square.SquareActivity;
import com.example.casting.util.ConstantData;
import com.example.casting.util.Session;
import com.example.casting_android.R;
import com.example.jpush.ExampleUtil;

public class MainTabNew extends BaseTabForm implements CloseAllListener,
		TabHostChangeListener {
	public static int atNumber = 0, reviewNumber = 0, praiseNumber = 0,
			recruitNumber = 0;

	public MainTabNew() {
		super(R.layout.tab, R.drawable.di);
		// TODO Auto-generated constructor stub

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ListenerManager.addCloseListener(this);
		ListenerManager.addTabHostListener(this);
		SharedPreferences share = getSharedPreferences("alias",
				Activity.MODE_PRIVATE);
		String alias = share.getString("alias", "");
		if (alias != null && alias.equals("success")) {

		} else {
			setAlias();
		}
		initTabHost();
		initJpushData();
	}

	private void initJpushData() {
		Intent intent = getIntent();
		if (null != intent) {
			Bundle bundle = getIntent().getExtras();
			if (bundle != null) {
				// String title =
				// bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
				String content = bundle.getString(JPushInterface.EXTRA_ALERT);
				// {"from_id"："123","to_id":"124","type":"类型","content":"呵呵","send_time":"2015-01-29 09:10:33"}
				try {
					JSONObject jsonResult = new JSONObject(content);
					String type = jsonResult.getString("type");
					// TODO @我的 0，私信1 别名 ,评论2,赞3,试镜4
					if (type != null && type.equals("2")) {
						reviewNumber = reviewNumber + 1;
					} else if (type != null && type.equals("3")) {
						praiseNumber = praiseNumber + 1;
					} else if (type != null && type.equals("4")) {
						recruitNumber = recruitNumber + 1;
					} else if (type != null && type.equals("0")) {
						atNumber = atNumber + 1;
					}
					// Button num = (Button) findViewById(R.id.num);
					// num.setBackgroundResource(R.drawable.alert_small);
					// num.setText(number); TODO 所有的number加起来，包括私信
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
	private void setAlias() {
		String alias = Session.getInstance().getUser_id();
		if (TextUtils.isEmpty(alias)) {
			return;
		}
		if (!ExampleUtil.isValidTagAndAlias(alias)) {
			return;
		}

		// 调用 Handler 来异步设置别名
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
	}

	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			// String logs ;
			switch (code) {
			case 0:
				// logs = "Set tag and alias success";
				// Log.i(TAG, logs);
				SharedPreferences share = getSharedPreferences("alias",
						Activity.MODE_PRIVATE);
				share.edit().putString("alias", "success").commit();
				// 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
				break;
			case 6002:
				// logs =
				// "Failed to set alias and tags due to timeout. Try again after 60s.";
				// Log.i(TAG, logs);
				// 延迟 60 秒来调用 Handler 设置别名
				mHandler.sendMessageDelayed(
						mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
				break;
			default:
				// logs = "Failed with errorCode = " + code;
				// Log.e(TAG, logs);
			}
			// ExampleUtil.showToast(logs, getApplicationContext());
		}
	};
	public static String TAG = "JpushTestDemo";
	private static final int MSG_SET_ALIAS = 1001;
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SET_ALIAS:
				Log.d(TAG, "Set alias in handler.");
				// 调用 JPush 接口来设置别名。
				JPushInterface.setAliasAndTags(getApplicationContext(),
						(String) msg.obj, null, mAliasCallback);
				break;
			default:
				Log.i(TAG, "Unhandled msg - " + msg.what);
			}
		}
	};

	@Override
	public List<MyTab> getMyTabList() {
		// TODO Auto-generated method stub
		List<MyTab> myTabList = new ArrayList<MyTab>();
		myTabList.add(new MyTab(0, R.drawable.home, R.drawable.homelight, "",
				MainTab.class));
		myTabList.add(new MyTab(0, R.drawable.mail, R.drawable.maillight, "",
				MailboxActivity.class));
		myTabList.add(new MyTab(1, R.drawable.addshouye,
				R.drawable.releasehigh, "", PublishedDynamic.class));
		myTabList.add(new MyTab(0, R.drawable.star, R.drawable.starlight, "",
				SquareActivity.class));
		if (Session.getInstance().getUsertype()
				.equals(ConstantData.Login_Type_Director)) {
			myTabList.add(new MyTab(0, R.drawable.me, R.drawable.meligh, "",
					DirectorInfoActivity.class));
		} else if (Session.getInstance().getUsertype()
				.equals(ConstantData.Login_Type_Company)) {
			myTabList.add(new MyTab(0, R.drawable.me, R.drawable.meligh, "我",
					CompanyInfoActivity.class));
		} else {
			myTabList.add(new MyTab(0, R.drawable.me, R.drawable.meligh, "我",
					Me3Activity.class));
		}
		return myTabList;
	}

	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeActivity() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public int getActivityid() {
		// TODO Auto-generated method stub
		return R.string.maintabnew;
	}

	@Override
	public void changeView(int index) {
		// TODO Auto-generated method stub
		changeView();
	}

	private long lastTime = -1;

	public void onBackPressed() {
		if (System.currentTimeMillis() - lastTime < 2000) {
			ListenerManager.notifyCloseLoginActivity();
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			// CastingApplication.getInstance().exits();
			// 获取进程ID
			android.os.Process.killProcess(android.os.Process.myPid());
			return;
		}
		lastTime = System.currentTimeMillis();
		showToast("再按一次退出！");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onBackPressed();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void startAddActivity() {
		// TODO Auto-generated method stub
		if(Session.getInstance().getUsertype().equals(ConstantData.Login_Type_Nomal)){
			Intent intent=new Intent(this, PublishedDynamic.class);
			intent.putExtra("action", "normal");
			startActivity(intent);
		}else{
			Intent intent=new Intent(this, PublishedDynamic.class);
			startActivity(intent);
		}
		
	}
}