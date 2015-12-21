package com.example.casting.login.regist;


import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.example.casting.entity.RegistBean;
import com.example.casting.login.regist.views.BaseRegistMsgView;
import com.example.casting.login.regist.views.CompanyRegistView;
import com.example.casting.login.regist.views.DirectorRegistView;
import com.example.casting.login.regist.views.TextViewClickBean;
import com.example.casting.login.regist.views.UserRegistView;
import com.example.casting.login.regist.wbregist.AccessTokenKeeper;
import com.example.casting.util.ConstantData;
import com.example.casting_android.R;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;

/**
 * 15387538103 公司注册测试手机号码
 * @author Administrator
 *
 */
public abstract class BaseCompanyInfoActivity extends BaseCompanyRegistActivity implements
		OnClickListener, OnCheckedChangeListener {
	View titleLayout;
	LinearLayout layout_content;// 内容布局
	RadioGroup rdo_regist;
	Button btn_submit;

	public abstract int getOperaterType();
	protected abstract void submitClick() ;
	
	private int id = position_director;
	CompanyRegistView companyview;
	DirectorRegistView directioview;
	BaseRegistMsgView baseView;
	boolean registinfo_director = false;
	boolean registinfo_company = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.casting_regist_real);
		titleLayout = (View)findViewById(R.id.titlelayout);
		layout_content = (LinearLayout)findViewById(R.id.layout_content);
		rdo_regist =(RadioGroup)findViewById(R.id.rdo_regist);
		btn_submit = (Button)findViewById(R.id.btn_submit);
		initView(titleLayout);
		rdo_regist.setOnCheckedChangeListener(this);
		btn_submit.setOnClickListener(this);
		
		directioview = new DirectorRegistView(this, handler, null, getOperaterType());
		companyview = new CompanyRegistView(this, handler, null, getOperaterType());
		
		if(getOperaterType() == UserRegistView.OperateType_Set
				|| getOperaterType() == UserRegistView.OperateType_Show){
			btn_submit.setVisibility(View.GONE);
		}
	}

	protected  void requestWeibo(){
		if(mSsoHandler==null){
			initWeibRegist();
		}
		mSsoHandler.authorize(new AuthListener());
	};

	/**
	 * 导演和公司信息切换
	 */
	protected void changeView() {
		layout_content.removeAllViews();
		layout_content.addView(getView());
	}

	protected View getView() {
		if (id == position_director) {
			return directioview.getView();
		} else {
			return companyview.getView();
		}
	}

	/**
	 * 
	 * @param type 更新类型 0=all， 1==导演，2=公司
	 * @param director
	 */
	public void updateView( RegistBean director){
		if(director==null)return;
		if(director.getType().equals(ConstantData.Login_Type_Director)){
			directioview.updateViewVal(director);
		}else {
			companyview.updateViewVal(director);
		}
			
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		super.onClick(arg0);
		if (arg0.getId() == R.id.btn_submit) {
			submitClick();
		}
	}


	protected RegistBean save() {
		if(position_company == id){
			return companyview.getValues();
			
		}else if(position_director == id){
			return  directioview.getValues();
		}
		return null;
	}
	
	public boolean CheckNull(){
		if(position_company == id){
			if(!companyview.checkNull()){
				showToast(companyview.getErrors());
				return false;
			}
			
		}else if(position_director == id){
			if(!directioview.checkNull()){
				showToast(directioview.getErrors());
				return false;
			}
		}
		return true;
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}



	@Override
	public void changeText(String val, int resid) {
		// TODO Auto-generated method stub
		if (id == position_company) {
			companyview.changeSubView(resid, val);
		} else {
			directioview.changeSubView(resid, val);
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		if (arg0.getCheckedRadioButtonId() == R.id.rdo_company) {
			id = position_company;
		} else if (arg0.getCheckedRadioButtonId() == R.id.rdo_director) {
			id = position_director;
		}
		changeView();
	}
	

	public RegistBean getLocalBean() {
		// TODO Auto-generated method stub
		return save();
	}


	@Override
	public View getWheelViewParent() {
		// TODO Auto-generated method stub
		return layout_content;
	}
	
	@Override
	public int toCompanyOrDirector(){
		return id;
	}

	@Override
	public View getParentView() {
		// TODO Auto-generated method stub
		return getWheelViewParent();
	}

	PopupWindow popupregistWindow;
	@Override
	protected void dealSubViewClick(TextViewClickBean bean) {
		// TODO Auto-generated method stub
		if (bean != null) {
			int type = bean.getClicktype();
			if(type == TextViewClickBean.clicktype_weibo ){
				showSelectRegistDialog();
				return;
			}
		}
		super.dealSubViewClick(bean);
		
	}
	protected  void  initWeiboRegist() {
		View contentView = getLayoutInflater().inflate(R.layout.popview_s_pic,
				null);
		popupregistWindow = new PopupWindow(contentView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT,false);
		popupregistWindow.setFocusable(true);// 取得焦点
		 //设置点击窗口外边窗口消失 
		popupregistWindow.setOutsideTouchable(true); 
		/** 设置PopupWindow弹出和退出时候的动画效果 */
		popupregistWindow.setAnimationStyle(R.style.anim_popup_dir);
		Button btn_take = (Button) contentView.findViewById(R.id.btn_take);
		Button btn_select = (Button) contentView.findViewById(R.id.btn_select);
		Button btn_cancel = (Button) contentView.findViewById(R.id.btn_cancel);
		btn_take.setText("微博授权认证");
		btn_select.setText("上传资料认证");
		btn_take.setOnClickListener(myClick);
		btn_select.setOnClickListener(myClick);
		btn_cancel.setOnClickListener(myClick);
		popupregistWindow.setOnDismissListener(new OnDismissListener()
		{
			@Override
			public void onDismiss()
			{
				// 设置背景颜色变暗
				sunBackground();
			}
		});
	}
	OnClickListener myClick = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (arg0.getId() == R.id.btn_take) {
				// 微博授权认证
				requestWeibo();
			} else if (arg0.getId() == R.id.btn_select) {
				// 上传资料认证
				Bundle bd = new Bundle();
				bd.putSerializable(R.string.registdetailactivity+"",save());
				Jump(R.string.registdetailactivity, bd);
			}
			dissmissDialog();
		
		}
	};	
	/**
	 * 授权认证
	 */
	public void showSelectRegistDialog() {
		if(popupregistWindow==null){
			initWeiboRegist();
		}
		darkBackground();
		if (!popupregistWindow.isShowing()) {
			/** 设置PopupWindow弹出后的位置 */
			popupregistWindow.showAtLocation(getParentView(), Gravity.BOTTOM, 0, 0);
		}

	}
	public void dissmissDialog() {
		sunBackground();
		if (popupregistWindow.isShowing()) {
			popupregistWindow.dismiss();// 关闭
		}
	}
	@Override
	public String getDirs() {
		// TODO Auto-generated method stub
		return "/registdir/";
	}
	
	private AuthInfo mAuthInfo;

	/** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能 */
	private Oauth2AccessToken mAccessToken;

	/** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
	private SsoHandler mSsoHandler;
	
	private void initWeibRegist(){
		mAuthInfo = new AuthInfo(this, ConstantData.APP_KEY,
				ConstantData.REDIRECT_URL, ConstantData.SCOPE);
		mSsoHandler = new SsoHandler(this, mAuthInfo);
	}
	public String getRegistStatue(){
		return wbRegistresult;
	}
	private String wbRegistresult = "0";
	RequestListener userRequestlistener = new RequestListener() {

		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub

			showToast("微博认证异常");
		}

		@Override
		public void onComplete(String arg0) {
			// TODO Auto-generated method stub
			String isvericify = "0";
			try {
				JSONObject json = new JSONObject(arg0);
				String result =json.getString("verified");
				if(result.equals("true")){
					isvericify = "1";
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			wbRegistresult = isvericify;
			getRegistResult(isvericify);
		}
	};
	protected abstract void getRegistResult(String value);
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
				// 保存 Token 到 SharedPreferences
				AccessTokenKeeper.writeAccessToken(BaseCompanyInfoActivity.this,
						mAccessToken);
				Long uuid = Long.parseLong(mAccessToken.getUid());
				UsersAPI users = new UsersAPI(BaseCompanyInfoActivity.this,
						ConstantData.APP_KEY,mAccessToken);
				users.show(uuid, userRequestlistener);

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
				showToast(  message );
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
}
