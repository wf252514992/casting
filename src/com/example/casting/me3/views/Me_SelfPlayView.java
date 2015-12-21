package com.example.casting.me3.views;

import java.io.Serializable;
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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.casting.entity.GalleryBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.login.BaseForm;
import com.example.casting.me.MeImgs;
import com.example.casting.me.MyVideoActvity;
import com.example.casting.me.ShowGalleryActivity;
import com.example.casting.me3.SelfPlayActivity;
import com.example.casting.me3.views.adapter.ImageAdapter;
import com.example.casting.processor.ProcessorID;
import com.example.casting.util.view.HalfCircleImageView;
import com.example.casting.util.view.Util;
import com.example.casting_android.R;
import com.example.casting_android.bean.ImageBean;
import com.geniuseoe2012.lazyloaderdemo.cache.ImageLoader;
import com.sina.weibo.sdk.api.share.Base;

public class Me_SelfPlayView implements OnClickListener,OnItemClickListener {
	private View baseinfoview;
	TextView txt_onclick;
	GridView lst_selfplay;
	Context mContext;
	ImageView layout_backgroud;
	ImageAdapter imgadapter = null;
	RegistBean registbean = null;
	ImageBean defaultbean = new ImageBean(ImageBean.type_res,
			R.drawable.add_zhanshi);
	List<GalleryBean> gallerylist = new ArrayList<GalleryBean>();

	public Me_SelfPlayView(Context ctx) {
		mContext = ctx;
		initView(ctx);
	}

	public ImageView getBackgroundView() {
		return layout_backgroud;
	}

	public Me_SelfPlayView(Context ctx, List<GalleryBean> beans) {
		mContext = ctx;
		gallerylist.clear();
		gallerylist.addAll(beans);
		initView(ctx);
	}

	public View getView() {
		return baseinfoview;
	}

	public void initView(Context ctx) {
		baseinfoview = LayoutInflater.from(ctx).inflate(R.layout.me_photos,
				null);
		layout_backgroud = (HalfCircleImageView) baseinfoview
				.findViewById(R.id.layout_backgroud);
		Bitmap  mBgBitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.me_bg04b);  
		layout_backgroud.setImageDrawable(new BitmapDrawable(mBgBitmap));
		lst_selfplay = (GridView) baseinfoview.findViewById(R.id.lst_selfplay);
		lst_selfplay.setOnItemClickListener(this);
		txt_onclick = (TextView) baseinfoview.findViewById(R.id.txt_onclick);
		layout_backgroud.setOnClickListener(this);
		txt_onclick.setText("个人展示");
		txt_onclick.setBackgroundResource(R.drawable.rect_25_blue);
		imgadapter = new ImageAdapter(ctx, gallery2img(gallerylist));
		lst_selfplay.setAdapter(imgadapter);
	}

	public void updateView(RegistBean bean) {
		if (baseinfoview == null)
			return;
		registbean = bean;
		List<GalleryBean> works = registbean.getGallerys();
		gallerylist.clear();
		gallerylist.addAll(works);
		imgadapter.notifyDataSetChanged(gallery2img(gallerylist));
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		// Toast.makeText(mContext, "Me_worksview", 0).show();
		if(registbean==null)return;
		Intent intent = new Intent(mContext,MeImgs.class);
		Bundle bd = new Bundle();
		bd.putSerializable(R.string.meimgs +"", registbean);
		intent.putExtras(bd);
		mContext.startActivity(intent);
//		mContext.startActivity(new Intent(mContext, MeImgs.class));
	}

	private ArrayList<ImageBean> gallery2img(List<GalleryBean> gallerys) {
		ArrayList<ImageBean> imags = new ArrayList<ImageBean>();
		if (gallerys == null)
			return imags;
		for (int i = 0; i < gallerys.size(); i++) {
			if (i == 6) {
				break;
			}
			GalleryBean bean = gallerys.get(i);
			ImageBean img = new ImageBean(ImageBean.type_url, bean.getUrl());
			imags.add(img);
		}
		// imags.add(defaultbean);
		return imags;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(mContext,ShowGalleryActivity.class);
		intent.putExtra(ShowGalleryActivity.TAG,(Serializable)gallery2img(gallerylist));
		intent.putExtra(ShowGalleryActivity.POSTION, position+"");
        mContext.startActivity(intent);		
	}
}
