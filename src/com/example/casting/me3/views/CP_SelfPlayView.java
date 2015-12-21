package com.example.casting.me3.views;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.example.casting.entity.GalleryBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.login.BaseForm;
import com.example.casting.me.MeImgs;
import com.example.casting.me.MyVideoActvity;
import com.example.casting.me.ShowGalleryActivity;
import com.example.casting.me3.views.adapter.ImageAdapter;
import com.example.casting.util.Session;
import com.example.casting_android.R;
import com.example.casting_android.bean.ImageBean;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CP_SelfPlayView implements OnClickListener,OnItemClickListener{
	GridView lst_selfplay;
	View baseinfoview ;
	ImageAdapter imgadapter = null;
	TextView txt_add;
	Context mContext;
	boolean isEdit = false;
	List<GalleryBean> gallerylist = new ArrayList<GalleryBean>() ;
	ArrayList<ImageBean> imgBeans = new ArrayList<ImageBean>();
	LinearLayout layout_more;
	RegistBean registBean;
	ImageBean defaultbean = new ImageBean(ImageBean.type_res,R.drawable.add_zhanshi);
	boolean edit = false;
	public CP_SelfPlayView(Context ctx , boolean isEdit){
		mContext = ctx;
		edit = isEdit;
		initView(ctx);
	}
	
	public CP_SelfPlayView(Context ctx,List<GalleryBean> beans) {
		mContext = ctx;
		gallerylist.clear();
		gallerylist.addAll(beans);
		initView(ctx);
	}
	
	
	public View getView() {
		return baseinfoview;
	}

	public void initView(Context ctx) {
		baseinfoview = LayoutInflater.from(ctx).inflate(R.layout.dr_photo,
				null);
		layout_more = (LinearLayout) baseinfoview
				.findViewById(R.id.layout);
		lst_selfplay = (GridView) baseinfoview.findViewById(R.id.lst_selfplay);
		txt_add = (TextView) baseinfoview.findViewById(R.id.txt_add);
		imgBeans = gallery2img(gallerylist);
		imgadapter = new ImageAdapter(ctx, imgBeans,(int)(BaseForm.screen_width/3-3),(int)(BaseForm.screen_width/3-3));
		lst_selfplay.setAdapter(imgadapter);
		if(edit){
			txt_add.setText("  添加公司展示");
		}else{
			txt_add.setText("  查看更多公司展示");
			txt_add.setCompoundDrawables(null, null, null, null);
		}
		layout_more.setOnClickListener(this);
		lst_selfplay.setOnItemClickListener(this);
	}
	private ArrayList<ImageBean> gallery2img(List<GalleryBean> gallerys) {
		ArrayList<ImageBean> imags = new ArrayList<ImageBean>();
		if(gallerys==null)return imags;
		int count = 0;
		for(GalleryBean bean : gallerys){
			if(count == 3)
				break;
			ImageBean img = new ImageBean(ImageBean.type_url,bean.getUrl());
			imags.add(img);
			count++;
		}
//		imags.add(defaultbean);
		return imags;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
//		if(arg0.getId() == R.id.lst_selfplay)
//		{
//			Intent intent = new Intent(mContext,ShowGalleryActivity.class);
//			intent.putExtra(ShowGalleryActivity.TAG,geti)
//			
//		}
		Intent intent = new Intent(mContext,MeImgs.class);
		Bundle bd = new Bundle();
		bd.putSerializable(R.string.meimgs+"", registBean);
		intent.putExtras(bd);
		mContext.startActivity(intent);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
//		ImageBean bean = gallery2img(gallerylist).get(position);
//		//查看图片详情
//		Intent intent = new Intent();
//		intent.setClass(mContext, ShowBigPhotoActivity.class);
//		intent.putExtra(ShowBigPhotoActivity.URL, bean.getImg_res());
//		mContext.startActivity(intent);
		Intent intent = new Intent(mContext,ShowGalleryActivity.class);
		intent.putExtra(ShowGalleryActivity.TAG,(Serializable)gallery2img(gallerylist));
		intent.putExtra(ShowGalleryActivity.POSTION, position+"");
        mContext.startActivity(intent);		
	}
	public void updateView(RegistBean bean) {
		if (baseinfoview == null)
			return;
		registBean = bean;
		List<GalleryBean> beans = registBean.getGallerys();
		gallerylist.clear();
		gallerylist.addAll(beans);
		imgadapter.notifyDataSetChanged(gallery2img(gallerylist));
	}

}
