package com.example.casting.me3.otherwatch;


import java.util.List;

import com.example.casting.entity.RegistBean;
import com.example.casting.entity.WorkBean;
import com.example.casting_android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class CP_OtherWatchView {
	private View baseinfoview;
	Context mContext;
	TextView txt_name, txt_nickname,  txt_introduce ,txt_works,txt_strstatue,
			 txt_onclick;
	TextView txt_letter,txt_statue;
	
	public CP_OtherWatchView(Context ctx,OnClickListener clicklistener) {
		mContext = ctx;
		initView(ctx,clicklistener);
	}


	public View getView() {
		return baseinfoview;
	}

	public void initView(Context ctx,OnClickListener clicklistener) {
		
		baseinfoview = LayoutInflater.from(ctx).inflate(
				R.layout.otherwatchcompany, null);
		txt_name = (TextView) baseinfoview.findViewById(R.id.txt_name);
		txt_nickname = (TextView) baseinfoview.findViewById(R.id.txt_nickname);
		txt_introduce = (TextView) baseinfoview
				.findViewById(R.id.txt_introduce);
		
		txt_works = (TextView) baseinfoview.findViewById(R.id.txt_works);
		txt_strstatue = (TextView) baseinfoview.findViewById(R.id.txt_strstatue);
		txt_letter = (TextView) baseinfoview.findViewById(R.id.txt_letter);
		txt_statue = (TextView) baseinfoview.findViewById(R.id.txt_statue);
		txt_letter.setOnClickListener(clicklistener);
		txt_statue.setOnClickListener(clicklistener);
	}

	public void updateView(RegistBean bean) {
		if (baseinfoview == null)
			return;
		if(bean!=null){
			txt_name.setText(bean.getName());
			txt_nickname.setText(bean.getCompany().getCompany_legal_representative());
			txt_introduce.setText(bean.getIntroduce());
			txt_strstatue.setText(bean.getPersonal_status());
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
