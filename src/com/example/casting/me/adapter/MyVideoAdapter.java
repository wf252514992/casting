package com.example.casting.me.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore.Video.Thumbnails;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.casting.entity.GalleryBean;
import com.example.casting.entity.VideoBean;
import com.example.casting.processor.ProcessorID;
import com.example.casting.publisheddynamic.VideoPlayActivity;
import com.example.casting.util.PicTool;
import com.example.casting.util.Server_path;
import com.example.casting_android.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.ta.common.TAStringUtils;
import com.ta.util.download.DownloadManager;

public class MyVideoAdapter extends BaseAdapter
{

	private Context context;
	private List<VideoBean> videoBeans = new ArrayList<VideoBean>();
	/**图片是否可删除状态，false时不限是叉叉图片，不能点击删除，true时则可删除*/
	private boolean state_delete = false;
	OnItemClickListener onItemClickListener;
	public boolean isState_delete() {
		return state_delete;
	}
	public void setState_delete(boolean state_delete) {
		this.state_delete = state_delete;
	}
	public MyVideoAdapter(Context context,List<VideoBean> videoBeans)
	{
		this.context = context;
		this.videoBeans = videoBeans;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return videoBeans.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return videoBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null)
		{
		   viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_video,
					null);
			viewHolder.imgView = (ImageView) convertView.findViewById(R.id.img_video);
			viewHolder.button = (ImageButton) convertView.findViewById(R.id.open);
		    convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		final VideoBean bean = videoBeans.get(position);
		show(viewHolder.imgView,bean.getUrl());
		viewHolder.button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String url = Server_path.serverfile_path+bean.getUrl();
				String pathString = DownloadManager.FILE_ROOT
						+ TAStringUtils.getFileNameFromUrl(url);
				File file = new File(pathString);
				if (file.exists()) {

					Intent intent = new Intent(context, VideoPlayActivity.class);
					intent.putExtra("uri", pathString);
					intent.putExtra("action", "main");
					// TODO 修改参数
					context.startActivity(intent);

				
				}
			}
		});
		return convertView;
	}

	public Bitmap getBitmap(String url)
	{
		Bitmap bmp = BitmapFactory.decodeFile(url);
		return bmp;
	}
	public void removeItem(int position)
	{
		videoBeans.remove(position);
		notifyDataSetChanged();
	}
	
	public void removeAll()
	{
		videoBeans.clear();
		notifyDataSetChanged();
	}
	public class ViewHolder
	{
		ImageView imgView;
		ImageButton button;
		
	}
	public void show(ImageView v,String pathStr)
	{
		if (pathStr != null && !pathStr.equals("")) {
			String url = Server_path.serverfile_path + pathStr;
			String path = DownloadManager.FILE_ROOT
					+ TAStringUtils.getFileNameFromUrl(url);
			File file = new File(path);
			if (file.exists()) {
				// 获取视频的缩略图
				Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path,
						Thumbnails.MINI_KIND);
				int degree = PicTool.readPictureDegree(path);

				if(degree!=0){//旋转照片角度
					bitmap=PicTool.rotateBitmap(bitmap,degree);
				}
				bitmap = ThumbnailUtils.extractThumbnail(bitmap, 200, 200,
						ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
				v.setImageBitmap(bitmap);
			}
		}
	
	}
	
		
}
