package com.example.casting.me3.views;

import java.util.List;

import com.example.casting.entity.RegistBean;
import com.example.casting.entity.WorkBean;
import com.example.casting.login.regist.CompanyEditActivity;
import com.example.casting.login.regist.DirectorEditActivity;
import com.example.casting_android.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class CP_BaseInfoView implements OnClickListener {
	private View baseinfoview;
	Context mContext;
	TextView txt_name, txt_nickname, txt_introduce, txt_works, txt_statue,
			txt_onclick;

	RegistBean registbean = new RegistBean();
	public CP_BaseInfoView(Context ctx) {
		mContext = ctx;
		initView(ctx);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Bundle bd =new Bundle();
		bd.putSerializable(R.string.companyeditactivity+"",registbean);
		Intent intent =new Intent(mContext, CompanyEditActivity.class);
		intent.putExtras(bd);
		mContext.startActivity(intent);
	}

	public View getView() {
		return baseinfoview;
	}

	public void initView(Context ctx) {
		baseinfoview = LayoutInflater.from(ctx).inflate(
				R.layout.me3_company_baseinfo, null);
		txt_name = (TextView) baseinfoview.findViewById(R.id.txt_name);
		txt_nickname = (TextView) baseinfoview.findViewById(R.id.txt_nickname);
		txt_introduce = (TextView) baseinfoview
				.findViewById(R.id.txt_introduce);
		txt_works = (TextView) baseinfoview.findViewById(R.id.txt_works);
		txt_statue = (TextView) baseinfoview.findViewById(R.id.txt_statue);
		txt_onclick = (TextView) baseinfoview.findViewById(R.id.txt_onclick);
		txt_onclick.setOnClickListener(this);
	}

	public void updateView(RegistBean bean) {
		if (baseinfoview == null)
			return;
		if (bean != null) {
			registbean = bean;
			txt_name.setText(bean.getNickname());
//			txt_nickname.setText(bean.getCompany()
//					.getCompany_legal_representative());
			txt_introduce.setText(bean.getIntroduce());
			txt_statue.setText(bean.getPersonal_status());
			setworks(bean.getWorks());
		}

	}
	private void setworks(List<WorkBean> works) {
		if (works != null && works.size() > 0) {
			WorkBean work = works.get(0);
			txt_works.setText(work.getWorks_name());

		}
	}
}
