package com.example.casting.search.adapter;

import java.util.ArrayList;
import java.util.List;
import com.example.casting.entity.RegistBean;
import com.example.casting.processor.ProcessorID;
import com.example.casting.util.Session;
import com.example.casting_android.R;
import com.example.casting_android.bean.ImageBean;
import com.geniuseoe2012.lazyloaderdemo.cache.ImageLoader;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchAdapter extends BaseAdapter {

	List<RegistBean> myList = new ArrayList<RegistBean>();
	Context mContext ;
	private LayoutInflater mInflater;
	ImageLoader imgloader;

	public SearchAdapter(List<RegistBean> list,Context mCont){
		myList.clear();
		myList.addAll(list);
		imgloader = new ImageLoader(mCont);
		this.mInflater = LayoutInflater.from(mCont);
		mContext = mCont;
	}

	public void notifyDataSetChanged(List<RegistBean> values) {
		myList.clear();
		myList.addAll(values);
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return myList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return myList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
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
//		AttentionBean bean = myList.get(arg0);
		RegistBean bean = myList.get(arg0);
		ImageBean img = bean.getUserimp();
		int type = img.getRestype();
		if (type == ImageBean.type_url) {
			imgloader.DisplayImage(
					ProcessorID.uri_headphoto + img.getImg_res(),
					holder.img_pic, false);
		} else {
			holder.img_pic.setImageResource(R.drawable.me_edit_txsmall);
		}
		holder.txt_name.setText(bean.getNickname());
		holder.txt_introduce.setText(bean.getIntroduce());
//		int statue = 0 ;// bean.getStatue() ;
//		if( bean.getId() .equals(Session.getInstance().getUser_id()))
//		{
//			holder.img_type.setImageResource(R.drawable.clist_focus_me);
//		}else
//		{
//			if(statue == 0){
//				holder.img_type.setImageResource(R.drawable.clist_focus_add);
//			}else if(statue==1){
//				holder.img_type.setImageResource(R.drawable.clist_focus_check);
//			}else if(statue==2){
//				holder.img_type.setImageResource(R.drawable.clist_focus_each);
//			}
//		}
		holder.img_type.setVisibility(View.GONE);
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

}
