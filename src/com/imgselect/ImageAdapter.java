package com.imgselect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.casting_android.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ImageAdapter extends BaseAdapter{

	 Context context;
	 String dirPath = "";
	/**
	 * 用户选择的图片，存储为图片的完整路径
	 */
	public List<String> mSelectedImage = new ArrayList<String>();
	public List<String> getmSelectedImage() {
		return mSelectedImage;
	}
//	private String imgUrl = "";
//	public String getImgUrl() {
//		return imgUrl;
//	}
//	public void setImgUrl(String imgUrl) {
//		this.imgUrl = imgUrl;
//	}
	DisplayImageOptions option;
	List<ImgBean> mDatas;
//	ImageViewOnClickListener listener;
	/**
	 * 文件夹路径
	 */
	private String mDirPath;
	public ImageAdapter(Context context, List<ImgBean> mDatas, int itemLayoutId,
			String dirPath)
	{
		this.context = context;
		this.mDirPath = dirPath;
		this.mDatas = mDatas;
		this.dirPath = dirPath;
//		initImageLoader();
//		initOption();
		
		
	}
	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

//	public void setSelected(int positon)
//	{
//		for(int i=0;i<mDatas.size();i++)
//		{
//			if(positon == i)
//			{
//				 mDatas.get(i).setState_Selected(true);
//			}
//			else {
//				mDatas.get(i).setState_Selected(false);
//			}
//		}
//		notifyDataSetChanged();
//	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
	    ViewHolder viewHolder = null;
		if(convertView == null)
		{
		   viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.grid_item,
					null);
			viewHolder.img = (ImageView) convertView.findViewById(R.id.id_item_image);
			viewHolder.img_select = (ImageView) convertView.findViewById(R.id.id_item_select);
			 convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ImgBean imgBean = (ImgBean) getItem(position);
		String urlString = "file://"+mDirPath + "/" +imgBean.getUrl().toString();
		ImageLoader.getInstance().displayImage(urlString,viewHolder.img,getOption(),new AnimateFirstDisplayListener());
//		if(imgBean.isState_Selected())
//		{
//			viewHolder.img_select.setImageResource(MResource.getIdByName(context,"drawable","pictures_selected"));
//			viewHolder.img.setColorFilter(Color.parseColor("#77000000"));
//		}
//		else {
//			viewHolder.img_select.setImageResource(MResource.getIdByName(context,"drawable","picture_unselected"));
//			viewHolder.img.setColorFilter(null);
//		}
		return convertView;
	}
//	public static interface ImageViewOnClickListener
//	{
//		void onImageViewClick(int position, View imgView);
//	}
//	public void setOnImageViewClickListener(ImageViewOnClickListener listener)
//	{
//	  this.listener = listener;	
//	}
	private DisplayImageOptions getOption(){
        option = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.home_videodownload)
		.showImageOnFail(R.drawable.imgbg)
		.resetViewBeforeLoading(true).cacheInMemory(false)
		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		// 不缓存到内存
		.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
		.build();
        return option;

    }

	public class ViewHolder
	{
		ImageView img,img_select;
	}
	/** 
     * 图片加载第一次显示监听器 
     * @author Administrator 
     * 
     */  
    private static class AnimateFirstDisplayListener implements ImageLoadingListener {

		@Override
		public void onLoadingCancelled(String arg0, View arg1) {
			System.out.println(arg0+"-----onLoadingCancelled");
		}

		@Override
		public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
			System.out.println(arg0+"-----onLoadingComplete");
		}

		@Override
		public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
			System.out.println(arg0+"-----onLoadingFailed" +arg2.getCause());
			 String message = null;  
             switch (arg2.getType()) {     // 获取图片失败类型  
                 case IO_ERROR:              // 文件I/O错误  
                     message = "Input/Output error";  
                     break;  
                 case DECODING_ERROR:        // 解码错误  
                     message = "Image can't be decoded";  
                     break;  
                 case NETWORK_DENIED:        // 网络延迟  
                     message = "Downloads are denied";  
                     break;  
                 case OUT_OF_MEMORY:         // 内存不足  
                     message = "Out Of Memory error";  
                     break;  
                 case UNKNOWN:               // 原因不明  
                     message = "Unknown error";  
                     break;  
             }  
		}

		@Override
		public void onLoadingStarted(String arg0, View arg1) {
			System.out.println(arg0+"-----onLoadingStarted");
		}  
          
  
    }  
}
