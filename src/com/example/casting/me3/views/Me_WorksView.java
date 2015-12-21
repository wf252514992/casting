package com.example.casting.me3.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.casting.entity.RegistBean;
import com.example.casting.entity.WorkBean;
import com.example.casting.login.regist.works.WorksActivity;
import com.example.casting.util.view.HalfCircleImageView;
import com.example.casting_android.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Me_WorksView implements OnClickListener {
	private View baseinfoview;
	TextView txt_onclick, txt_content;
	Context mContext;
	ImageView layout_backgroud ;
	RegistBean worksBean;
	public ImageView getBackgroundView() {
		return layout_backgroud;
	}
	boolean isEdit = false;
	ArrayList<WorkBean> worklist = new ArrayList<WorkBean>();
	public Me_WorksView(Context ctx,boolean edit) {
		mContext = ctx;
		this.isEdit = edit;
		initView(ctx);
	}

	public View getView() {
		return baseinfoview;
	}

	public void initView(Context ctx) {
		baseinfoview = LayoutInflater.from(ctx).inflate(R.layout.me_baseinfo,
				null);
		layout_backgroud = (HalfCircleImageView) baseinfoview.findViewById(R.id.layout_backgroud);
		Bitmap  mBgBitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.me_bg01b);  
		layout_backgroud.setImageDrawable(new BitmapDrawable(mBgBitmap));
		txt_content = (TextView) baseinfoview.findViewById(R.id.txt_content);
		txt_onclick = (TextView) baseinfoview.findViewById(R.id.txt_onclick);
		layout_backgroud.setOnClickListener(this);
		txt_onclick.setText("个人作品");
		txt_onclick.setBackgroundResource(R.drawable.rect_25_yellow);
	}

	public void updateView(RegistBean rgbbean) {
		if(rgbbean==null) return ;
		worksBean = rgbbean;
		List<WorkBean> works = rgbbean.getWorks();
		worklist.clear();
		worklist.addAll(works);
		if (baseinfoview == null)
			return;
		String text = "";
		for (int i = 0; i < works.size(); i++) {
			if (i == 3) {
				text += "...\n";
				break;
			}
			WorkBean bean = works.get(i);
			text += "《"+bean.getWorks_name() + "》  " + bean.getRelease_time()+"\n";

		}
		txt_content.setText(text);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(isEdit){
			Bundle bd = new Bundle();
			bd.putSerializable(R.string.worksactivity+"",worksBean);
			Intent intent = new Intent(mContext,WorksActivity.class);
			intent.putExtras(bd);
			mContext.startActivity(intent);
		}else{
			Bundle bd = new Bundle();
			bd.putSerializable(R.string.worksactivity+"",worksBean);
			Intent intent = new Intent(mContext,WorksActivity.class);
			intent.putExtras(bd);
			mContext.startActivity(intent);
		}
		
	}

}
