package com.example.casting.login.regist.wbregist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.casting.login.BaseForm;
import com.example.casting.util.ConstantData;
import com.example.casting_android.R;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

public class WeiBoRegistForm extends BaseForm implements OnClickListener {

	TextView txt_wbshow;
	private AuthInfo mAuthInfo;

	/** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能 */
	private Oauth2AccessToken mAccessToken;

	/** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
	private SsoHandler mSsoHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weiboregist);
		txt_wbshow = (TextView) findViewById(R.id.txt_wbshow);
		// 创建微博实例
		// mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY,
		// Constants.REDIRECT_URL, Constants.SCOPE);
		// 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
		mAuthInfo = new AuthInfo(this, ConstantData.APP_KEY,
				ConstantData.REDIRECT_URL, ConstantData.SCOPE);
		mSsoHandler = new SsoHandler(this, mAuthInfo);
		txt_wbshow.setOnClickListener(this);

	}

	RequestListener userRequestlistener = new RequestListener() {

		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onComplete(String arg0) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub

	}

	/**
	 * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
	 * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
	 * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
	 * SharedPreferences 中。
	 */
	class AuthListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			// 从 Bundle 中解析 Token
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken.isSessionValid()) {
				// 显示 Token
				updateTokenView(false);

				// 保存 Token 到 SharedPreferences
				AccessTokenKeeper.writeAccessToken(WeiBoRegistForm.this,
						mAccessToken);
				showToast("成功");
			} else {
				// 以下几种情况，您会收到 Code：
				// 1. 当您未在平台上注册的应用程序的包名与签名时；
				// 2. 当您注册的应用程序包名与签名不正确时；
				// 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
				String code = values.getString("code");
				String message = "失败";
				if (!TextUtils.isEmpty(code)) {
					message = message + "\nObtained the code: " + code;
				}
				showToast(message);
			}
		}

		@Override
		public void onCancel() {
			showToast("用户取消");
		}

		@Override
		public void onWeiboException(WeiboException e) {
			showToast("Auth exception : " + e.getMessage());
		}
	}

	/**
	 * 显示当前 Token 信息。
	 * 
	 * @param hasExisted
	 *            配置文件中是否已存在 token 信息并且合法
	 */
	private void updateTokenView(boolean hasExisted) {
		// String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
		// .format(new java.util.Date(mAccessToken.getExpiresTime()));
		// String format =
		// getString(R.string.weibosdk_demo_token_to_string_format_1);
		// txt_wbshow
		// .setText(String.format(format, mAccessToken.getToken(), date));
		//
		// String message = String.format(format, mAccessToken.getToken(),
		// date);
		// if (hasExisted) {
		// message = getString(R.string.weibosdk_demo_token_has_existed)
		// + "\n" + message;
		// }
		// txt_wbshow.setText(message);
	}

	/**
	 * 当 SSO 授权 Activity 退出时，该函数被调用。
	 * 
	 * @see {@link Activity#onActivityResult}
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// SSO 授权回调
		// 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		mSsoHandler.authorize(new AuthListener());

	}

}
