package com.example.casting.login.regist.views;

import java.util.ArrayList;
import com.example.casting.entity.RegistBean;
import com.example.casting.login.regist.CompanyRegistActivity;
import com.example.casting.util.ConstantData;
import com.example.casting.util.Session;
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

public class UserRegistView extends BaseRegistMsgView {

	View view;
	Context mContext;
	Handler callback;
	RegistBean userbean;
	ArrayList<String> labels = new ArrayList<String>();
	ImageLoader imgloader;
	private int optype = OperateType_New;

	RegistValueView layout_nickname, piclayout, layout_name, layout_sex,
			layout_birth, layout_hometown, layout_address, layout_height,
			layout_weigh, layout_education, layout_language,
			layout_introduction, layout_special, layout_label, layout_email,
			layout_qq, layout_wechat;

	RegistValueView layout_statue, layout_changebackground;

	public UserRegistView(Context context, Handler handler, RegistBean bean,
			int type) {
		mContext = context;
		callback = handler;
		optype = type;
		this.userbean = bean;
		initView();
		imgloader = new ImageLoader(context);

	}

	public RegistBean getValues() {
		if (userbean == null) {
			userbean = new RegistBean();
			userbean.setType(ConstantData.Login_Type_Nomal + "");
		}
		userbean.setNickname(layout_nickname.getTextValue());
		userbean.setName(layout_name.getTextValue());
		userbean.setSex(layout_sex.getTextValue());
		userbean.setAddress(layout_address.getTextValue());
		userbean.setHometown(layout_hometown.getTextValue());
		userbean.setIntroduce(layout_introduction.getTextValue());
		userbean.setBirth(layout_birth.getTextValue());
		userbean.addLabel(labels);
		userbean.setEmail(layout_email.getTextValue());
		userbean.setQq(layout_qq.getTextValue());
		userbean.setWechat(layout_wechat.getTextValue());
		userbean.setHeight(layout_height.getTextValue());
		userbean.setHeavy(layout_weigh.getTextValue());
		userbean.setEducation((layout_education.getTextValue()));
		userbean.setLanguage(layout_language.getTextValue());
		userbean.setSpecial(layout_special.getTextValue());
		userbean.setPersonal_status(layout_statue.getTextValue());
		return userbean;
	}

	public void updateViewVal(RegistBean bean) {
		if (bean == null)
			return;
		this.userbean = bean;
		layout_nickname.setValue(bean.getNickname());
		layout_name.setValue(bean.getName());
		layout_sex.setValue(bean.getSex());
		layout_address.setValue(bean.getAddress());
		layout_hometown.setValue(bean.getHometown());
		layout_introduction.setValue(bean.getIntroduce());
		layout_birth.setValue(bean.getBirth());
		layout_email.setValue(bean.getEmail());
		layout_qq.setValue(bean.getQq());
		layout_wechat.setValue(bean.getWechat());
		layout_height.setValue(bean.getHeight());
		layout_weigh.setValue(bean.getHeavy());
		layout_education.setValue(bean.getEducation());
		layout_language.setValue(bean.getLanguage());
		layout_special.setValue(bean.getSpecial());
		layout_statue.setValue(bean.getPersonal_status());
		if(userbean!=null && userbean.getId().equals(Session.getInstance().getUser_id())){
			layout_qq.setVisibility(View.VISIBLE);
			layout_wechat.setVisibility(View.VISIBLE);
		}else{
			layout_qq.setVisibility(View.GONE);
			layout_wechat.setVisibility(View.GONE);
		}
		setPic(bean.getUserimp());
		labels.clear();
		labels.addAll(bean.getLabel());
		layout_label.setLabel(labels);
		// setLabelView(labels);
	}

	private void initView() {
		view = LayoutInflater.from(mContext).inflate(
				R.layout.casting_regist_user, null);
		layout_nickname = (RegistValueView) view
				.findViewById(R.id.layout_nickname);
		piclayout = (RegistValueView) view.findViewById(R.id.piclayout);
		layout_name = (RegistValueView) view.findViewById(R.id.layout_name);
		layout_sex = (RegistValueView) view.findViewById(R.id.layout_sex);
		layout_birth = (RegistValueView) view.findViewById(R.id.layout_birth);
		layout_changebackground = (RegistValueView) view
				.findViewById(R.id.layout_changebackground);
		layout_statue = (RegistValueView) view.findViewById(R.id.layout_statue);
		layout_hometown = (RegistValueView) view
				.findViewById(R.id.layout_hometown);
		layout_address = (RegistValueView) view
				.findViewById(R.id.layout_address);
		layout_height = (RegistValueView) view.findViewById(R.id.layout_height);
		layout_weigh = (RegistValueView) view.findViewById(R.id.layout_weigh);
		layout_education = (RegistValueView) view
				.findViewById(R.id.layout_education);
		layout_language = (RegistValueView) view
				.findViewById(R.id.layout_language);
		layout_introduction = (RegistValueView) view
				.findViewById(R.id.layout_introduction);
		layout_special = (RegistValueView) view
				.findViewById(R.id.layout_special);
		layout_label = (RegistValueView) view.findViewById(R.id.layout_label);
		layout_email = (RegistValueView) view.findViewById(R.id.layout_email);
		layout_qq = (RegistValueView) view.findViewById(R.id.layout_qq);
		layout_wechat = (RegistValueView) view.findViewById(R.id.layout_wechat);

		updateViewVal(userbean);
		if (optype == OperateType_Set) {
			// 编辑时 可以 修改背景图片
			layout_changebackground.setVisibility(View.VISIBLE);
			layout_statue.setVisibility(View.VISIBLE);
		} else if (optype == OperateType_Show) {
			// 显示时，不可以 编辑 按键不需要 有行为
			layout_statue.setVisibility(View.VISIBLE);
			return;
		}
		layout_nickname
				.setOnTextChangeListener(new NickNameTextChangeListener());
		layout_nickname.setOnClickListener(clickListener);
		piclayout.setOnClickListener(clickListener);
		layout_name.setOnClickListener(clickListener);
		layout_sex.setOnClickListener(clickListener);
		layout_birth.setOnClickListener(clickListener);
		layout_changebackground.setOnClickListener(clickListener);
		layout_statue.setOnClickListener(clickListener);
		layout_hometown.setOnClickListener(clickListener);
		layout_address.setOnClickListener(clickListener);
		layout_height.setOnClickListener(clickListener);
		layout_weigh.setOnClickListener(clickListener);
		layout_education.setOnClickListener(clickListener);
		layout_language.setOnClickListener(clickListener);
		layout_introduction.setOnClickListener(clickListener);
		layout_special.setOnClickListener(clickListener);
		layout_label.setOnClickListener(clickListener);
		layout_email.setOnClickListener(clickListener);
		layout_qq.setOnClickListener(clickListener);
		layout_wechat.setOnClickListener(clickListener);

	}

	public View getView() {
		return view;
	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			int clicktype = 0;// 默认是编辑文本 0,1表示编辑跳转，2.地点编辑，3，表示编辑日期
			// 4 .性别选择框 ，5.照片选择
			String name = arg0.getTag().toString();
			int inputtype = InputType.TYPE_CLASS_TEXT;
			int clickto = 0;
			if (arg0.getId() == R.id.layout_nickname) {
			} else if (arg0.getId() == R.id.layout_name) {
			} else if (arg0.getId() == R.id.layout_statue) {
			} else if (arg0.getId() == R.id.layout_height) {
				inputtype = InputType.TYPE_CLASS_NUMBER;
			} else if (arg0.getId() == R.id.layout_weigh) {
				inputtype = InputType.TYPE_CLASS_NUMBER;
			} else if (arg0.getId() == R.id.layout_email) {
			} else if (arg0.getId() == R.id.layout_wechat) {
			} else if (arg0.getId() == R.id.layout_qq) {
			} else if (arg0.getId() == R.id.layout_label) {
				clicktype = 1;
				clickto = R.string.updatelabelnform;
			} else if (arg0.getId() == R.id.layout_address
					|| arg0.getId() == R.id.layout_hometown) {
				clicktype = 2;
			} else if (arg0.getId() == R.id.layout_birth) {
				clicktype = 3;
			} else if (arg0.getId() == R.id.layout_sex) {
				clicktype = 4;
			} else if (arg0.getId() == R.id.layout_introduction) {
				clicktype = 1;
				clickto = R.string.updateintrouction;
			} else if (arg0.getId() == R.id.piclayout) {
				clicktype = 5;
			} else if (arg0.getId() == R.id.layout_changebackground) {
				clicktype = 1;
				clickto = R.string.changebgactivity;
			}
			String val = "";
			if (clicktype == 0) {
				val = ((RegistValueView) arg0).getTextValue();
			}
			TextViewClickBean bean = new TextViewClickBean();
			bean.setResid(arg0.getId());
			bean.setKeyname(name);
			bean.setKeyval(val);
			bean.setClickto(clickto);
			bean.setEdittype(inputtype);
			bean.setClicktype(clicktype);
			Message msg = new Message();
			msg.what = CompanyRegistActivity.setSubViewChange;
			msg.obj = bean;
			callback.sendMessage(msg);
		}
	};

	public void changeSubView(int resid, String val) {
		((RegistValueView) view.findViewById(resid)).setValue(val);
	}

	public boolean checkNull() {
		if (layout_nickname.isNull()) {
			errorinfo = layout_nickname.getTag().toString() + "不能为空";
			return false;
		}
		if (layout_name.isNull()) {
			errorinfo = layout_name.getTag().toString() + "不能为空";
			return false;
		}
		if (layout_sex.isNull()) {
			errorinfo = layout_sex.getTag().toString() + "不能为空";
			return false;
		}
		if (layout_birth.isNull()) {
			errorinfo = layout_birth.getTag().toString() + "不能为空";
			return false;
		}
		if (layout_hometown.isNull()) {
			errorinfo = layout_hometown.getTag().toString() + "不能为空";
			return false;
		}
		if (layout_address.isNull()) {
			errorinfo = layout_address.getTag().toString() + "不能为空";
			return false;
		}
		if (layout_height.isNull()) {
			errorinfo = layout_height.getTag().toString() + "不能为空";
			return false;
		}
		if (layout_weigh.isNull()) {
			errorinfo = layout_weigh.getTag().toString() + "不能为空";
			return false;
		}
		if (layout_education.isNull()) {
			errorinfo = layout_education.getTag().toString() + "不能为空";
			return false;
		}
		if (layout_language.isNull()) {
			errorinfo = layout_language.getTag().toString() + "不能为空";
			return false;
		}
		if (layout_introduction.isNull()) {
			errorinfo = layout_introduction.getTag().toString() + "不能为空";
			return false;
		}
		if (layout_special.isNull()) {
			errorinfo = layout_special.getTag().toString() + "不能为空";
			return false;
		}
		if (labels.size() == 0) {
			errorinfo = "请选择标签";
			return false;
		}

		return true;
	}

	private String errorinfo = "";

	public String errorInfo() {
		return errorinfo;
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
