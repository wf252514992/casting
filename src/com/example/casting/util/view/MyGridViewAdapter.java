package com.example.casting.util.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.casting.entity.GalleryBean;
import com.example.casting.processor.ProcessorID;
import com.example.casting.util.PicTool;
import com.example.casting_android.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class MyGridViewAdapter extends BaseAdapter {
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private Context context;
	private List<GalleryBean> galleryBeans = new ArrayList<GalleryBean>();
	/** 图片是否可删除状态，false时不限是叉叉图片，不能点击删除，true时则可删除 */
	private boolean state_delete = false;

	public boolean isState_delete() {
		return state_delete;
	}

	public void setState_delete(boolean state_delete) {
		this.state_delete = state_delete;
	}

	/**
	 * 专门放空白位置
	 */
	private ArrayList<GalleryBean> emptyMenuData;

	public MyGridViewAdapter(Context context, List<GalleryBean> galleryBeans) {
		this.context = context;
		this.galleryBeans = galleryBeans;
		emptyMenuData = new ArrayList<GalleryBean>();
		options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.home_videodownload)
//				.showImageOnFail(R.drawable.imgbg)
				.resetViewBeforeLoading(false)
				.cacheInMemory(false).imageScaleType(ImageScaleType.EXACTLY)
				// 不缓存到内存
				.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		// autoFillBlankPlace();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return galleryBeans.size() + emptyMenuData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return galleryBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	DisplayImageOptions options;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.grid_item2, null);
			viewHolder.imgView = (ImageView) convertView.findViewById(R.id.img);
			viewHolder.imgView_delete = (ImageView) convertView
					.findViewById(R.id.delete_markView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (state_delete) {
			viewHolder.imgView_delete.setVisibility(View.VISIBLE);
		} else {
			viewHolder.imgView_delete.setVisibility(View.GONE);
		}
		GalleryBean bean = null;
		if (position >= galleryBeans.size()) {
			int emptyPosition = position - galleryBeans.size();
			bean = emptyMenuData.get(emptyPosition);
		} else {
			bean = galleryBeans.get(position);
		}
		if (bean.isBlankPlace) {
			viewHolder.imgView.setImageBitmap(PicTool.ReadBitmapById(context,
					R.drawable.empty));
			viewHolder.imgView.setAlpha(0);
		} else {
			viewHolder.imgView.setImageResource(R.drawable.me_zhanshidownloadb);
			if (!bean.getS_url().equals("")) {
				imageLoader.displayImage(
						ProcessorID.uri_headphoto + bean.getS_url(),
						viewHolder.imgView, options);

			}
		}
		return convertView;
	}

	public void removeItem(int position) {
		galleryBeans.remove(position);
		notifyDataSetChanged();
	}

	public void removeAll() {
		galleryBeans.clear();
		notifyDataSetChanged();
	}

	public class ViewHolder {
		ImageView imgView, imgView_delete;

	}

	/**
	 * 指定位置是不是空白View
	 */
	public boolean isItemBlank(int position) {
		return position >= galleryBeans.size();
	}

	// 自动填充空白位置
	private void autoFillBlankPlace() {
		/* 如果多一个，要补两个 , 就是 3 - 余数 */
		int 余数 = galleryBeans.size() % 3;
		if (余数 == 0) {
			// 表示完整一行， 清理掉空白
			emptyMenuData.clear();
		} else if (余数 > 0) {
			int blankCount = 3 - 余数;
			if (blankCount > 0) {
				// ** 需要填充
				/* 先把之前的空白View清理掉，否则插入位置不对 */
				// 创建一个副本,只放有效菜单
				// 给新引用
				emptyMenuData.clear();
				for (int i = 0; i < blankCount; i++) {
					emptyMenuData.add(GalleryBean.newBlankMenu());
				}
			}
		}
	}
	// @Override
	// public void notifyDataSetChanged() {
	// // TODO Auto-generated method stub
	// super.notifyDataSetChanged();
	// autoFillBlankPlace();
	// }

}
