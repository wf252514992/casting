package com.example.casting.me3.views;

import java.util.List;

import com.example.casting.entity.RegistBean;
import com.example.casting.entity.WorkBean;
import com.example.casting.login.regist.DirectorEditActivity;
import com.example.casting_android.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class DR_BaseInfoView implements OnClickListener {
	private View baseinfoview;
	Context mContext;
	TextView txt_name, txt_address, txt_age, txt_introduce, txt_works,
			txt_onclick,txt_state;
	ImageView img_sex;
	TextView txt_letter, txt_statue;

	RegistBean registbean = new RegistBean();
	public DR_BaseInfoView(Context ctx) {
		mContext = ctx;
		initView(ctx);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Bundle bd =new Bundle();
		bd.putSerializable(R.string.directoreditactivity+"",registbean);
		Intent intent =new Intent(mContext, DirectorEditActivity.class);
		intent.putExtras(bd);
		mContext.startActivity(intent);
	}

	public View getView() {
		return baseinfoview;
	}

	public void initView(Context ctx) {
		baseinfoview = LayoutInflater.from(ctx).inflate(
				R.layout.me3_director_baseinfo, null);
		txt_name = (TextView) baseinfoview.findViewById(R.id.txt_name);
		txt_address = (TextView) baseinfoview.findViewById(R.id.txt_address);
		txt_age = (TextView) baseinfoview.findViewById(R.id.txt_age);
		txt_introduce = (TextView) baseinfoview
				.findViewById(R.id.txt_introduce);
		txt_works = (TextView) baseinfoview.findViewById(R.id.txt_works);
		txt_state = (TextView) baseinfoview.findViewById(R.id.txt_state);
		txt_onclick = (TextView) baseinfoview.findViewById(R.id.txt_onclick);
		img_sex = (ImageView) baseinfoview.findViewById(R.id.img_sex);
		txt_onclick.setOnClickListener(this);
	}

	public void updateView(RegistBean bean) {
		if (baseinfoview == null)
			return;
		if (bean != null) {
			registbean = bean;
			txt_name.setText(bean.getNickname());
			txt_address.setText(bean.getHometown());
			txt_age.setText(bean.getAge() + "岁");
			txt_introduce.setText(bean.getIntroduce());
			txt_state.setText(bean.getPersonal_status());
			setworks(bean.getWorks());
			setSex(bean.getSex());
		}

	}

	public void updateWork(List<WorkBean> works) {
		setworks(works);
	}

	private void setSex(String sex) {
		if (sex.equals("男")) {
			img_sex.setImageResource(R.drawable.mine_sex_boy);
		} else {
			img_sex.setImageResource(R.drawable.mine_sex_girl);
		}
	}

	private void setworks(List<WorkBean> works) {
		if (works != null && works.size() > 0) {
			WorkBean work = works.get(0);
			txt_works.setText(work.getWorks_name());

		}
	}

}
