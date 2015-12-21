package com.example.casting.publisheddynamic;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.example.casting.BigActivity;
import com.example.casting.entity.GalleryBean;
import com.example.casting.me.ShowGalleryActivity;
import com.example.casting.processor.ProcessorID;
import com.example.casting.util.Server_path;
import com.example.casting_android.R;
import com.example.casting_android.bean.ImageBean;
import com.ta.common.TAStringUtils;
import com.ta.util.download.DownloadManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ImgsAdapter extends BaseAdapter {

	Context context;
	List<String> data;
	public Bitmap bitmaps[];
	Util util;
	OnItemClickClass onItemClickClass;
	private int index=-1;
	private boolean Visibility=true;
	private String action;
	private GridView imgGridView;
	
	List<View> holderlist;
//	com.geniuseoe2012.lazyloaderdemo.cache.ImageLoader imgloader;
	public ImgsAdapter(Context context,List<String> data,OnItemClickClass onItemClickClass) {
		this.context=context;
		this.data=data;
		this.onItemClickClass=onItemClickClass;
		bitmaps=new Bitmap[data.size()];
		util=new Util(context);
		holderlist=new ArrayList<View>();
//		imgloader = new com.geniuseoe2012.lazyloaderdemo.cache.ImageLoader(context);
	}
	private String[] urls = null;
	public ImgsAdapter(Context context,List<String> data,OnItemClickClass onItemClickClass,String[] startUrls) {
		this.context=context;
		this.data=data;
		this.onItemClickClass=onItemClickClass;
		bitmaps=new Bitmap[data.size()];
		util=new Util(context);
		holderlist=new ArrayList<View>();
		urls= startUrls;
//		imgloader = new com.geniuseoe2012.lazyloaderdemo.cache.ImageLoader(context);
	}
	 public void setVisibility(boolean Visibility){
		 this.Visibility=Visibility;
	 }
	 public void setAction(String action,GridView imgGridView){
		 this.action=action;
		 this.imgGridView=imgGridView;
	 }
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View arg1, ViewGroup arg2) {
		Holder holder;
		if (position != index && position > index) {
			index=position;
			if(!Visibility){
				arg1=LayoutInflater.from(context).inflate(R.layout.mainimgsitem, null);
			}else{
				arg1=LayoutInflater.from(context).inflate(R.layout.imgsitem, null);
			}
			
			holder=new Holder();
			holder.imageView=(ImageView) arg1.findViewById(R.id.imageView1);
			holder.checkBox=(CheckBox) arg1.findViewById(R.id.checkBox1);
			holder.ib_delete=(ImageButton) arg1.findViewById(R.id.ib_delete);
			if(!Visibility){
				holder.checkBox.setVisibility(View.GONE);
				if(position==data.size()-1){
					//TODO 添加图片事件
				}
			}
			arg1.setTag(holder);
			holderlist.add(arg1);
		}else {
			holder= (Holder)holderlist.get(position).getTag();
			arg1=holderlist.get(position);
		}
		if (bitmaps[position] == null) {
//			if(!Visibility){
//			imgloader.DisplayImage(data.get(position), holder.imageView, false);
//			}else{
			util.imgExcute(holder.imageView,new ImgClallBackLisner(position), data.get(position));
//			}
		}
		else {
			holder.imageView.setImageBitmap(bitmaps[position]);
		}
		if(Visibility){
			if(action!=null){
				holder.checkBox.setVisibility(View.GONE);
			  if (!data.get(position).equals("")) {
				holder.ib_delete.setVisibility(View.VISIBLE);
				holder.ib_delete.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						if(action!=null){
							data.remove(position);
							EditPhotoDynamic.endNum=EditPhotoDynamic.endNum+1;
							if(!data.get(data.size()-1).equals("")&&data.size()==8){
								data.add("");
							}

							ImgsAdapter imgsAdapter = new ImgsAdapter(context, data, null);
							imgsAdapter.setAction("edit",imgGridView);
							imgGridView.setAdapter(imgsAdapter);
						}
						
					}
				});
			  }
			}else{
		      arg1.setOnClickListener(new OnPhotoClick(position, holder.checkBox));
			}
		}
		if(!Visibility){
			
			
		holder.imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
					File file = new File(data.get(position));
					if (file.exists()) {

//						Intent intent=new Intent(context,BigActivity.class);
//						Bundle b=new Bundle();
//						b.putString("picName", data.get(position));
//						intent.putExtras(b);
//						context.startActivity(intent);
						Intent intent = new Intent(context,ShowGalleryActivity.class);
						intent.putExtra(ShowGalleryActivity.TAG,(Serializable)gallery2img(urls));
						intent.putExtra(ShowGalleryActivity.POSTION, position+"");
						context.startActivity(intent);	
					}
				
			}
		});
		}
		return arg1;
	}
	
	class Holder{
		ImageView imageView;
		CheckBox checkBox;
		ImageButton ib_delete;
	}

	public class ImgClallBackLisner implements ImgCallBack{
		int num;
		public ImgClallBackLisner(int num) {
			this.num=num;
		}
		
		@Override
		public void resultImgCall(ImageView imageView, Bitmap bitmap) {
			bitmaps[num]=bitmap;
			imageView.setImageBitmap(bitmap);
		}
	}

	public interface OnItemClickClass{
		public void OnItemClick(View v,int Position,CheckBox checkBox);
	}
	
	class OnPhotoClick implements OnClickListener{
		int position;
		CheckBox checkBox;
		
		public OnPhotoClick(int position,CheckBox checkBox) {
			this.position=position;
			this.checkBox=checkBox;
		}
		@Override
		public void onClick(View v) {
			if (data!=null && onItemClickClass!=null ) {
				onItemClickClass.OnItemClick(v, position, checkBox);
			}
		}
	}
	private ArrayList<ImageBean> gallery2img(String[] imgs) {
		ArrayList<ImageBean> imags = new ArrayList<ImageBean>();
		if (urls == null)
			return imags;
		for (int i = 0; i < imgs.length; i++) {
			if (i == 9) {
				break;
			}
			String  url = imgs[i];
			ImageBean img = new ImageBean(ImageBean.type_url, url);
			imags.add(img);
		}
		// imags.add(defaultbean);
		return imags;
	}
}
