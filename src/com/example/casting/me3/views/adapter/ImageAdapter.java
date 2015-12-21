package com.example.casting.me3.views.adapter;

import java.util.ArrayList;

import com.example.casting.entity.GetPHotoBean;
import com.example.casting.login.BaseForm;
import com.example.casting.me.ShowGalleryActivity;
import com.example.casting.processor.ProcessorID;
import com.example.casting.util.PicTool;
import com.example.casting_android.R;
import com.example.casting_android.bean.ImageBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageAdapter extends BaseAdapter {
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	
	private Context mContext;

	ArrayList<ImageBean> imgs = new ArrayList<ImageBean>();

	int imgHeight,imgWidth;
//	public ImageAdapter(Context context, ArrayList<ImageBean> imglist) {
//		this.mContext = context;
//		imgloader = new ImageLoader(context);
//		imgs.clear();
//		imgs.addAll(imglist);
//	}
	public ImageAdapter(Context context, ArrayList<ImageBean> imglist,int img_height,int img_width) {
		this.mContext = context;
		imgs.clear();
		imgs.addAll(imglist);
		this.imgHeight = img_height;
		this.imgWidth = img_width;
		initOptions();
		
	}
	public ImageAdapter(Context context, ArrayList<ImageBean> imglist) {
		this.mContext = context;
		imgs.clear();
		imgs.addAll(imglist);
		initOptions();
	}
	DisplayImageOptions options;
	public void initOptions(){
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.home_videodownload)
//		.showImageOnFail(R.drawable.imgbg)
		.resetViewBeforeLoading(false).cacheInMemory(false)
		.imageScaleType(ImageScaleType.EXACTLY)
		.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
		.build();
	}
	@Override
	public int getCount() {
		return imgs.size();
	}

	public void notifyDataSetChanged( ArrayList<ImageBean> list) {
		// TODO Auto-generated method stub
		imgs.clear();
		imgs.addAll(list);
		super.notifyDataSetChanged();
	}
	@Override
	public Object getItem(int position) {
		return imgs.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 定义一个ImageView,显示在GridView里
		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(mContext);
			int width = parent.getMeasuredWidth();
			imageView.setLayoutParams(new GridView.LayoutParams(
					width/3-3,width/3-3));
		
			// new
														// GridView.LayoutParams(85,
														// 85)
//			imageView.setPadding(0, 5, 0, 0);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		} else {
			imageView = (ImageView) convertView;
		}
		System.out.println(parent.getMeasuredWidth());
		ImageBean imgbean = imgs.get(position);
		imageView.setImageBitmap(PicTool.ReadBitmapById(mContext, R.drawable.home_videodownload));
		if (imgbean.getRestype() == ImageBean.type_filepath) {
			imageView.setImageBitmap(BitmapFactory.decodeFile(imgbean
					.getImg_res()));
		} else if (imgbean.getRestype() == ImageBean.type_res) {
			imageView.setImageResource(Integer.parseInt(imgbean
					.getImg_res()));
		} else if (imgbean.getRestype() == ImageBean.type_url) {
			imageLoader.displayImage(ProcessorID.uri_headphoto+imgbean.getImg_res(), imageView, options );
		} else {
			imageView.setImageResource(R.drawable.ic_launcher);
		}
		return imageView;
	}


}
