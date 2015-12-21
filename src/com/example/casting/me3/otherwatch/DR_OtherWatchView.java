package com.example.casting.me3.otherwatch;

import java.util.List;

import com.example.casting.entity.RegistBean;
import com.example.casting.entity.WorkBean;
import com.example.casting_android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class DR_OtherWatchView {
	private View baseinfoview;
	Context mContext;
	TextView txt_name, txt_address, txt_age, txt_introduce, txt_works,txt_strstate ;
	ImageView img_sex;
	TextView txt_letter,txt_statue;
	public DR_OtherWatchView(Context ctx,OnClickListener clickListener) {
		mContext = ctx;
		initView(ctx,clickListener);
	}


	public View getView() {
		return baseinfoview;
	}

	public void initView(Context ctx,OnClickListener clickListener) {
		baseinfoview = LayoutInflater.from(ctx).inflate(
				R.layout.otherwatchdirector, null);
		txt_name = (TextView) baseinfoview.findViewById(R.id.txt_name);
		txt_address = (TextView) baseinfoview.findViewById(R.id.txt_address);
		txt_age = (TextView) baseinfoview.findViewById(R.id.txt_age);
		txt_introduce = (TextView) baseinfoview
				.findViewById(R.id.txt_introduce);
		txt_works = (TextView) baseinfoview.findViewById(R.id.txt_works);
		txt_strstate = (TextView) baseinfoview.findViewById(R.id.txt_strstate);
		txt_letter = (TextView) baseinfoview.findViewById(R.id.txt_letter);
		txt_statue = (TextView) baseinfoview.findViewById(R.id.txt_statue);
		img_sex = (ImageView) baseinfoview.findViewById(R.id.img_sex);
		txt_letter.setOnClickListener(clickListener);
		txt_statue.setOnClickListener(clickListener);
	}

	public void updateView(RegistBean bean) {
		if (baseinfoview == null)
			return;
		if(bean!=null){
			txt_name.setText(bean.getName());
			txt_address.setText(bean.getHometown());
			txt_age.setText(bean.getAge()+"岁");
			txt_introduce.setText(bean.getIntroduce());
			txt_strstate.setText(bean.getPersonal_status());
			setworks(bean.getWorks());
			setSex(bean.getSex());
		}

	}
	
	public void updateWork(List<WorkBean> works){
		setworks(works);
	}
	
	private void setSex(String sex){
		if(sex.equals("男")){
			img_sex.setImageResource(R.drawable.mine_sex_boy);
		}else{
			img_sex.setImageResource(R.drawable.mine_sex_girl);
		}
	}
	private void setworks(List<WorkBean> works){
		if(works!=null && works.size()>0){
			WorkBean work = works.get(0);
			txt_works.setText(work.getWorks_name());
			
		}
	}

}
