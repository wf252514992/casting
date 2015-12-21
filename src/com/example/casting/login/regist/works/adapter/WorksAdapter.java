package com.example.casting.login.regist.works.adapter;

import java.util.ArrayList;

import com.example.casting.entity.WorkBean;
import com.example.casting.processor.ProcessorID;
import com.example.casting_android.R;
import com.example.casting_android.bean.ImageBean;
import com.geniuseoe2012.lazyloaderdemo.cache.ImageLoader;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WorksAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	Context mContext;
	ArrayList<WorkBean> users = new ArrayList<WorkBean>();
	ImageLoader imgloader;

	boolean showAll = false;
	public WorksAdapter(Context context, ArrayList<WorkBean> values,boolean showall) {
		super();
		this.showAll = showall;
		mContext = context;
		imgloader = new ImageLoader(context);
		mContext = context;
		this.mInflater = LayoutInflater.from(context);
		if(values!=null && values.size()>0){
			if(showAll){
				users.addAll(values);
			}else
				users.add(values.get(0));
		}
	}

	public void notifyDataSetChanged(ArrayList<WorkBean> beans) {
		users.clear();
		if(beans!=null && beans.size()>0){
			if(showAll){
				users.addAll(beans);
			}else
				users.add(beans.get(0));
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
			convertView = mInflater.inflate(R.layout.works_show_item, null);
			holder.txt_time = (TextView) convertView
					.findViewById(R.id.txt_time);
			holder.txt_name = (TextView) convertView
					.findViewById(R.id.txt_name);
			holder.txt_detail = (TextView) convertView
					.findViewById(R.id.txt_detail);
			holder.img_pic = (ImageView) convertView.findViewById(R.id.img_pic);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		WorkBean bean = users.get(arg0);
		ImageBean img = bean.getWorkpic();
		int type = img.getRestype();
		if (type == ImageBean.type_filepath) {
			holder.img_pic.setImageBitmap(BitmapFactory.decodeFile(img
					.getImg_res()));
		} else if (type == ImageBean.type_url) {
			imgloader.DisplayImage(
					ProcessorID.uri_headphoto + img.getImg_res(),
					holder.img_pic, false);
		} else if (type == ImageBean.type_res) {
			holder.img_pic.setImageResource(Integer.parseInt(img.getImg_res()));
		} else {
			holder.img_pic.setImageResource(R.drawable.ic_launcher);
		}
		holder.txt_name.setText(bean.getWorks_name());
		holder.txt_time.setText(getTiem(bean.getRelease_time()));
		String value = " 导演："+bean.getDirector()+"\n"
				+ " 合作演员："+bean.getCooperation_actor() ;
		holder.txt_detail.setText(value);
		return convertView;
	}

	private String getTiem(String time){
		time = time.trim();
		if(time.length()>4){
			return time.substring(0,4);
		}
		return time;
	}
	private static class ViewHolder {
		// 姓名
		public TextView txt_name, txt_time,txt_detail;
		// 头像
		public ImageView img_pic;
	}


}
