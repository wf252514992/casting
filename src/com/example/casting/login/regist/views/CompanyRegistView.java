package com.example.casting.login.regist.views;

import com.example.casting.entity.CompanyBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.login.regist.CompanyRegistActivity;
import com.example.casting.util.ConstantData;
import com.example.casting.util.view.RegistValueView;
import com.example.casting_android.R;
import com.example.casting_android.bean.ImageBean;
import com.geniuseoe2012.lazyloaderdemo.cache.ImageLoader;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

public class CompanyRegistView extends BaseRegistMsgView {

	View view;
	// show alltime
	RegistValueView piclayout, layout_nickname, layout_companyintroduce,
			layout_companylocation, layout_companyregist, layout_phone,
			layout_companyemail, layout_legalperson;

	// show in edit;
	RegistValueView layout_companystatue, layout_changebackground;
	Context mContext;
	Handler callback;
	RegistBean registbean;
	private int optype = OperateType_New;

	ImageLoader imgloader;

	public CompanyRegistView(Context context, Handler handler, RegistBean bean,
			int type) {
		mContext = context;
		this.callback = handler;
		optype = type;
		this.registbean = bean;
		imgloader = new ImageLoader(context);
		initView();
	}

	@Override
	public RegistBean getValues() {
		if (registbean == null) {
			registbean = new RegistBean();
			registbean.setType(ConstantData.Login_Type_Company);
		}
		registbean.setNickname(layout_nickname.getTextValue());
		registbean.setHometown(layout_companylocation.getTextValue());
		registbean.setIntroduce(layout_companyintroduce.getTextValue());
		registbean.setEmail(layout_companyemail.getTextValue());
		registbean.setPhone(layout_phone.getTextValue());
		registbean.setPersonal_status(layout_companystatue.getTextValue());
		CompanyBean bean = registbean.getCompany();
		bean.setCompany_legal_representative(layout_legalperson.getTextValue());
		registbean.setCompany(bean);
		return registbean;
	}

	public void updateViewVal(RegistBean bean) {
		if (bean == null)
			return;
		if (this.registbean == null)
			this.registbean = bean;
		registbean.setCompany(bean.getCompany());
		layout_nickname.setValue(registbean.getNickname());
		layout_companylocation.setValue(registbean.getHometown());
		layout_companyintroduce.setValue(registbean.getIntroduce());
		layout_companyemail.setValue(registbean.getEmail());
		layout_phone.setValue(registbean.getPhone());
		layout_companystatue.setValue(registbean.getPersonal_status());
		layout_legalperson.setValue(bean.getCompany()
				.getCompany_legal_representative());
		
		if(bean.getCertification().equals("1")){
			layout_companyregist.setRegistimg();
		}
		setPic(bean.getUserimp());

	}

	private void initView() {
		view = LayoutInflater.from(mContext).inflate(
				R.layout.casting_regist_company, null);

		piclayout = (RegistValueView) view.findViewById(R.id.piclayout);
		layout_nickname = (RegistValueView) view
				.findViewById(R.id.layout_nickname);
		layout_companyintroduce = (RegistValueView) view
				.findViewById(R.id.layout_companyintroduce);
		layout_companylocation = (RegistValueView) view
				.findViewById(R.id.layout_companylocation);
		layout_companyregist = (RegistValueView) view
				.findViewById(R.id.layout_companyregist);
		layout_phone = (RegistValueView) view.findViewById(R.id.layout_phone);
		layout_changebackground = (RegistValueView) view
				.findViewById(R.id.layout_changebackground);
		layout_companystatue = (RegistValueView) view
				.findViewById(R.id.layout_companystatue);
		layout_companyemail = (RegistValueView) view
				.findViewById(R.id.layout_companyemail);
		layout_legalperson = (RegistValueView) view
				.findViewById(R.id.layout_legalperson);

		updateViewVal(registbean);
		if (optype == OperateType_Set) {
			// 编辑时 可以 修改背景图片
			layout_changebackground.setVisibility(View.VISIBLE);
			layout_companystatue.setVisibility(View.VISIBLE);
		} else if (optype == OperateType_Show) {
			// 显示时，不可以 编辑 按键不需要 有行为
			layout_companystatue.setVisibility(View.VISIBLE);
			return;
		}
		layout_nickname
				.setOnTextChangeListener(new NickNameTextChangeListener());
		piclayout.setOnClickListener(clickListener);
		layout_nickname.setOnClickListener(clickListener);
		layout_companyintroduce.setOnClickListener(clickListener);
		layout_companylocation.setOnClickListener(clickListener);
		layout_companyregist.setOnClickListener(clickListener);
		layout_phone.setOnClickListener(clickListener);
		layout_changebackground.setOnClickListener(clickListener);
		layout_companystatue.setOnClickListener(clickListener);
		layout_companyemail.setOnClickListener(clickListener);
		layout_legalperson.setOnClickListener(clickListener);

	}

	String errors = "";

	public boolean checkNull() {
		if (layout_nickname.isNull()) {
			errors = layout_nickname.getTag().toString() + "不能为空";
			return false;
		}
		if (layout_companyintroduce.isNull()) {
			errors = layout_companyintroduce.getTag().toString() + "不能为空";
			return false;
		}
		if (layout_phone.isNull()) {
			errors = layout_phone.getTag().toString() + "不能为空";
			return false;
		}
		if (layout_companylocation.isNull()) {
			errors = layout_companylocation.getTag().toString() + "不能为空";
			return false;
		}
		// if (layout_companyemail.isNull()) {
		// errors = layout_companyemail.getTag().toString() + "不能为空";
		// return false;
		// }
		if (layout_legalperson.isNull()) {
			errors = layout_legalperson.getTag().toString() + "不能为空";
			return false;
		}
		return true;
	}

	public String getErrors() {
		return errors;
	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if(registbean!=null && arg0.getId() == R.id.layout_directorregist && registbean.getCertification().equals("1")){
				//如果已注册，则不需要重复注册，屏蔽点击事件的发生；
				return;
			}
			int clicktype = 0;// 默认是编辑文本 0,1表示编辑标签，2.地点编辑，3，表示编辑日期
			// 4 .性别选择框 5,简介 ,6:进入认证界面
			String name = arg0.getTag().toString();
			int inputtype = InputType.TYPE_CLASS_TEXT;
			int clickto = 0;
			if (arg0.getId() == R.id.layout_nickname) {
			} else if (arg0.getId() == R.id.layout_legalperson) {
			} else if (arg0.getId() == R.id.layout_companylocation) {
			} else if (arg0.getId() == R.id.layout_companystatue) {
			} else if (arg0.getId() == R.id.layout_phone) {
				inputtype = InputType.TYPE_CLASS_PHONE;
			} else if (arg0.getId() == R.id.layout_companyemail) {
			} else if (arg0.getId() == R.id.layout_companyintroduce) {
				clicktype = 1;
				clickto = R.string.updateintrouction;
			} else if (arg0.getId() == R.id.layout_companyregist) {

				clicktype = TextViewClickBean.clicktype_weibo;
//				clicktype = 1;
//				clickto = R.string.registdetailactivity;
			} else if (arg0.getId() == R.id.layout_changebackground) {
//				clicktype = 1;
//				clickto = R.string.changebgactivity;
				clicktype = 5;
			} else if (arg0.getId() == R.id.piclayout) {
				clicktype = 5;
			}
			String val = "";
			if (clicktype == 0) {
				val = ((RegistValueView) arg0).getTextValue();
			}
			TextViewClickBean bean = new TextViewClickBean();
			bean.setResid(arg0.getId());
			bean.setKeyname(name);
			bean.setEdittype(inputtype);
			bean.setKeyval(val);
			bean.setClickto(clickto);
			bean.setClicktype(clicktype);
			Message msg = new Message();
			msg.what = CompanyRegistActivity.setSubViewChange;
			msg.obj = bean;
			callback.sendMessage(msg);
		}
	};

	public View getView() {
		return view;
	}

	public void changeSubView(int resid, String val) {
		((RegistValueView) view.findViewById(resid)).setValue(val);
	}

	private void setPic(ImageBean img) {
		if (img == null)
			return;
		piclayout.setImage(img);
	}

	protected class NickNameTextChangeListener implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			if (arg0.length() > 0 && callback != null) {
				Message msg = new Message();
				msg.obj = arg0.toString();
				msg.what = CompanyRegistActivity.checkNickName;
				callback.sendMessage(msg);
			}
		}

	}
}
