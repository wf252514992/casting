package com.example.casting.square.adapter;

import java.util.ArrayList;

import com.example.casting.entity.PlazaBean;
import com.example.casting.processor.ProcessorID;
import com.example.casting.util.PicTool;
import com.example.casting.util.Session;
import com.example.casting_android.R;
import com.example.casting_android.bean.ImageBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class HotAdapter extends BaseAdapter {

	public abstract void addAttention(PlazaBean bean, int position);

	private LayoutInflater mInflater;
	Context mContext;
	ArrayList<PlazaBean> users = new ArrayList<PlazaBean>();
	// ImageLoader imgloader;

	DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.home_videodownload)
			.showImageOnFail(R.drawable.imgbg).resetViewBeforeLoading(false)
			.cacheInMemory(false).imageScaleType(ImageScaleType.EXACTLY)
			// 不缓存到内存
			.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();

	private String action;
	ImageLoader imglLoader;

	public void setAction(String action) {
		this.action = action;
	}

	public HotAdapter(Context context, ArrayList<PlazaBean> values) {
		super();
		mContext = context;
		// imgloader = new ImageLoader(context);
		mContext = context;
		this.mInflater = LayoutInflater.from(context);
		users = values;
		imglLoader = com.nostra13.universalimageloader.core.ImageLoader
				.getInstance();

	}

	public void notifyDataSetChanged(ArrayList<PlazaBean> beans) {
		users.clear();
		if (beans != null) {
			users.addAll(beans);
		}
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return users.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return users.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.fanslistitem, null);
			holder.txt_introduce = (TextView) convertView
					.findViewById(R.id.txt_introduce);
			holder.txt_name = (TextView) convertView
					.findViewById(R.id.txt_name);
			holder.img_pic = (ImageView) convertView.findViewById(R.id.img_pic);
			holder.img_type = (ImageView) convertView
					.findViewById(R.id.img_type);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		PlazaBean bean = users.get(arg0);
		ImageBean img = bean.getImgbean();
		holder.img_pic.setImageResource(R.drawable.me_edit_txsmall);
		int type = img.getRestype();
		if (type == ImageBean.type_filepath) {
			holder.img_pic.setImageBitmap(BitmapFactory.decodeFile(img
					.getImg_res()));
		} else if (type == ImageBean.type_url) {
			// imgloader.DisplayImage(
			// ProcessorID.uri_headphoto + img.getImg_res(),
			// holder.img_pic, false);
			imglLoader.displayImage(
					ProcessorID.uri_headphoto + img.getImg_res(),
					holder.img_pic, options);
		} else if (type == ImageBean.type_res) {
			holder.img_pic.setImageResource(Integer.parseInt(img.getImg_res()));
		}
		holder.txt_name.setText(bean.getNickname());
		if (action != null) {
			holder.txt_introduce.setText(bean.getFollowers_count());
		} else {
			holder.txt_introduce.setText("粉丝:" + bean.getFollowers_count());
		}
		int statue = bean.getState();
		if (bean.getId().equals(Session.getInstance().getUser_id())) {
			holder.img_type.setImageResource(R.drawable.clist_focus_me);
		} else {
			if (statue == 0) {
				holder.img_type.setImageResource(R.drawable.clist_focus_add);
				holder.img_type.setOnClickListener(new AddAttentionListener(
						bean, arg0));
			} else {
				holder.img_type.setImageResource(R.drawable.clist_focus_check);
			}
		}

		return convertView;
	}

	private static class ViewHolder {
		// 姓名
		public TextView txt_name, txt_introduce;
		// 头像
		public ImageView img_pic;
		// 关注类型
		public ImageView img_type;
	}

	class AddAttentionListener implements OnClickListener {
		PlazaBean bean;
		int position;

		public AddAttentionListener(PlazaBean bean, int position) {
			this.bean = bean;
			this.position = position;
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (bean.getState() == 0
					&& !bean.getId().equals(Session.getInstance().getUser_id()))
				addAttention(bean, position);
		}

	}

}
