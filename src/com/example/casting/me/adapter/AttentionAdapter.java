package com.example.casting.me.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.casting.entity.AttentionBean;
import com.example.casting.processor.ProcessorID;
import com.example.casting_android.R;
import com.example.casting_android.bean.ImageBean;
import com.geniuseoe2012.lazyloaderdemo.cache.ImageLoader;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class AttentionAdapter extends BaseAdapter {

	public abstract void addAttention(AttentionBean bean);

	Context mContext;
	ArrayList<AttentionBean> uses = new ArrayList<AttentionBean>();
	private LayoutInflater mInflater;
	ImageLoader imgloader;

	public AttentionAdapter(Context context, ArrayList<AttentionBean> beans) {
		super();
		imgloader = new ImageLoader(context);
		mContext = context;
		this.mInflater = LayoutInflater.from(context);
		uses.clear();
		if(beans!=null)
			uses.addAll(beans);
	}

	public void notifyDataSetChanged(List<AttentionBean> list) {
		// TODO Auto-generated method stub
		uses.clear();
		if (list != null)
			uses.addAll(list);
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return uses.size();
	}

	@Override
	public Object getItem(int position) {
		return uses.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
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
		AttentionBean bean = uses.get(arg0);
		ImageBean img = bean.getImgbean();
		holder.img_pic.setImageResource(R.drawable.me_edit_txsmall);
		int type = img.getRestype();
		if (type == ImageBean.type_url) {
			imgloader.DisplayImage(
					ProcessorID.uri_headphoto + img.getImg_res(),
					holder.img_pic, false);
		}
		holder.txt_name.setText(bean.getNickname());
		holder.txt_introduce.setText(bean.getIntroduce());
		int statue = bean.getStatue() ;
		if(statue == 0){
			holder.img_type.setImageResource(R.drawable.clist_focus_add);
		}else if(statue==1){
			holder.img_type.setImageResource(R.drawable.clist_focus_check);
		}else if(statue==2){
			holder.img_type.setImageResource(R.drawable.clist_focus_each);
		}
		holder.img_type.setOnClickListener(new AddAttentionListener(bean));
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
		AttentionBean bean;

		public AddAttentionListener(AttentionBean bean) {
			this.bean = bean;
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			addAttention(bean);
		}

	}
}
