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
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

public class DirectorRegistView extends BaseRegistMsgView {
	View view;
	Context mContext;
	Handler callback;
	RegistBean directorbean;
	ArrayList<String> labels = new ArrayList<String>();
	ImageLoader imgloader;
	private int optype = OperateType_New;
	RegistValueView piclayout, layout_nickname, layout_name, layout_sex,
			layout_birth, layout_directorregist, layout_hometown,
			layout_address, layout_height, layout_weigh, layout_education,
			layout_language, layout_introduction, layout_special, layout_label,
			layout_email, layout_qq, layout_wechat;

	RegistValueView layout_statue, layout_changebackground;

	public DirectorRegistView(Context context, Handler handler,
			RegistBean bean, int type) {
		mContext = context;
		callback = handler;
		optype = type;
		imgloader = new ImageLoader(context);
		this.directorbean = bean;
		initView();
	}

	public RegistBean getValues() {
		if (directorbean == null) {
			directorbean = new RegistBean();
			directorbean.setType(ConstantData.Login_Type_Director + "");
		}
		directorbean.setNickname(layout_nickname.getTextValue());
		directorbean.setName(layout_name.getTextValue());
		directorbean.setSex(layout_sex.getTextValue());
		directorbean.setAddress(layout_address.getTextValue());
		directorbean.setIntroduce(layout_introduction.getTextValue());
		directorbean.setBirth(layout_birth.getTextValue());
		directorbean.addLabel(labels);
		directorbean.setEmail(layout_email.getTextValue());
		directorbean.setQq(layout_qq.getTextValue());
		directorbean.setWechat(layout_wechat.getTextValue());
		directorbean.setHeight(layout_height.getTextValue());
		directorbean.setHeavy(layout_weigh.getTextValue());
		directorbean.setHometown(layout_hometown.getTextValue());
		directorbean.setEducation((layout_education.getTextValue()));
		directorbean.setLanguage(layout_language.getTextValue());
		directorbean.setSpecial(layout_special.getTextValue());
		directorbean.setPersonal_status(layout_statue.getTextValue());
		return directorbean;
	}

	public void updateViewVal(RegistBean bean) {
		if (bean == null)
			return;
		this.directorbean = bean;
		labels.clear();
		labels.addAll(directorbean.getLabel());
		layout_nickname.setValue(bean.getNickname());
		layout_name.setValue(bean.getName());
		layout_sex.setValue(bean.getSex());
		layout_address.setValue(bean.getAddress());
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
		layout_label.setLabel(labels);
		layout_hometown.setValue(bean.getHometown());
		layout_statue.setValue(bean.getPersonal_status());
		if(directorbean!=null && directorbean.getId().equals(Session.getInstance().getUser_id())){
			layout_qq.setVisibility(View.VISIBLE);
			layout_wechat.setVisibility(View.VISIBLE);
		}else{
			layout_qq.setVisibility(View.GONE);
			layout_wechat.setVisibility(View.GONE);
		}
		if(bean.getCertification().equals("1")){
			layout_directorregist.setRegistimg();
		}
		setPic(bean.getUserimp());

	}

	private void initView() {
		view = LayoutInflater.from(mContext).inflate(
				R.layout.casting_regist_director, null);

		piclayout = (RegistValueView) view.findViewById(R.id.piclayout);
		layout_nickname = (RegistValueView) view
				.findViewById(R.id.layout_nickname);
		layout_name = (RegistValueView) view.findViewById(R.id.layout_name);
		layout_sex = (RegistValueView) view.findViewById(R.id.layout_sex);
		layout_birth = (RegistValueView) view.findViewById(R.id.layout_birth);
		layout_directorregist = (RegistValueView) view
				.findViewById(R.id.layout_directorregist);
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
		updateViewVal(directorbean);
		if (optype == OperateType_Set) {
			// 编辑时 可以 修改背景图片
			layout_changebackground.setVisibility(View.VISIBLE);
			layout_statue.setVisibility(View.VISIBLE);
		} else if (optype == OperateType_Show) {
			// 显示时，不可以 编辑 按键不需要 有行为
			layout_statue.setVisibility(View.VISIBLE);
			return;
		}
		piclayout.setOnClickListener(clickListener);
		layout_nickname.setOnClickListener(clickListener);
		layout_name.setOnClickListener(clickListener);
		layout_sex.setOnClickListener(clickListener);
		layout_birth.setOnClickListener(clickListener);
		layout_directorregist.setOnClickListener(clickListener);
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
			if(directorbean!=null && arg0.getId() == R.id.layout_directorregist && directorbean.getCertification().equals("1")){
				//如果已注册，则不需要重复注册，屏蔽点击事件的发生；
				return;
			}
			int clicktype = 0;// 默认是编辑文本 0,1表示编辑标签，2.地点编辑，3，表示编辑日期
			// 4 .性别选择框 5,简介 ,6:进入认证界面
			String name = arg0.getTag().toString();
			int inputtype = InputType.TYPE_CLASS_TEXT;
			int clickto = TextViewClickBean.clicktype_edt;

			if (arg0.getId() == R.id.layout_nickname) {
			} else if (arg0.getId() == R.id.layout_name) {
			} else if (arg0.getId() == R.id.layout_statue) {
			} else if (arg0.getId() == R.id.layout_weigh) {
				inputtype = InputType.TYPE_CLASS_NUMBER;
			} else if (arg0.getId() == R.id.layout_height) {
				inputtype = InputType.TYPE_CLASS_NUMBER;
			} else if (arg0.getId() == R.id.layout_email) {
			} else if (arg0.getId() == R.id.layout_wechat) {
			} else if (arg0.getId() == R.id.layout_qq) {
			} else if (arg0.getId() == R.id.layout_changebackground) {
//				clicktype = 1;
//				clickto = R.string.changebgactivity;
				clicktype = TextViewClickBean.clicktype_photo;
			} else if (arg0.getId() == R.id.layout_label) {
				clicktype = TextViewClickBean.clicktype_label;
				clickto = R.string.updatelabelnform;
			} else if (arg0.getId() == R.id.layout_address
					|| arg0.getId() == R.id.layout_hometown) {
				clicktype = TextViewClickBean.clicktype_address;
			} else if (arg0.getId() == R.id.layout_birth) {
				clicktype = TextViewClickBean.clicktype_date;
			} else if (arg0.getId() == R.id.layout_sex) {
				clicktype = TextViewClickBean.clicktype_sex;
			} else if (arg0.getId() == R.id.layout_introduction) {
				clicktype = TextViewClickBean.clicktype_label;
				clickto = R.string.updateintrouction;
			} else if (arg0.getId() == R.id.layout_directorregist) {
				clicktype = TextViewClickBean.clicktype_weibo;
			} else if (arg0.getId() == R.id.piclayout) {
				clicktype = TextViewClickBean.clicktype_photo;
			}
			String val = "";
			if (clicktype == TextViewClickBean.clicktype_edt) {
				val = ((RegistValueView) arg0).getTextValue();
			}
			TextViewClickBean bean = new TextViewClickBean();
			bean.setResid(arg0.getId());
			bean.setKeyname(name);
			bean.setKeyval(val);
			bean.setClickto(clickto);
			bean.setClicktype(clicktype);
			bean.setEdittype(inputtype);
			Message msg = new Message();
			msg.what = CompanyRegistActivity.setSubViewChange;
			msg.obj = bean;
			callback.sendMessage(msg);
		}
	};
	String notificationstr = "";

	public String getErrors() {
		return notificationstr;
	}

	public boolean checkNull() {
		if (layout_nickname.isNull()) {
			notificationstr = layout_nickname.getTag().toString() + "不能为空";
			return false;
		}
		if (layout_name.isNull()) {
			notificationstr = layout_name.getTag().toString() + "不能为空";
			return false;
		}
		if (layout_sex.isNull()) {
			notificationstr = layout_sex.getTag().toString() + "不能为空";
			return false;
		}
		if (layout_birth.isNull()) {
			notificationstr = layout_birth.getTag().toString() + "不能为空";
			return false;
		}
		if (layout_hometown.isNull()) {
			notificationstr = layout_hometown.getTag().toString() + "不能为空";
			return false;
		}
		if (layout_address.isNull()) {
			notificationstr = layout_address.getTag().toString() + "不能为空";
			return false;
		}
		if (layout_height.isNull()) {
			notificationstr = layout_height.getTag().toString() + "不能为空";
			return false;
		}
		if (layout_weigh.isNull()) {
			notificationstr = layout_weigh.getTag().toString() + "不能为空";
			return false;
		}
		if (layout_education.isNull()) {
			notificationstr = layout_education.getTag().toString() + "不能为空";
			return false;
		}
		if (layout_language.isNull()) {
			notificationstr = layout_language.getTag().toString() + "不能为空";
			return false;
		}
		if (layout_introduction.isNull()) {
			notificationstr = layout_introduction.getTag().toString() + "不能为空";
			return false;
		}
		if (layout_special.isNull()) {
			notificationstr = layout_special.getTag().toString() + "不能为空";
			return false;
		}
		if (labels.size() == 0) {
			notificationstr = "请选择标签";
			return false;
		}

		return true;

	}

	public void changeSubView(int resid, String val) {
		((RegistValueView) view.findViewById(resid)).setValue(val);
	}

	private void setPic(ImageBean img) {
		if (img == null)
			return;
		piclayout.setImage(img);
	}
}
