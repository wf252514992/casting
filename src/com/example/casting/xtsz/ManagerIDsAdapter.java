package com.example.casting.xtsz;

import java.util.ArrayList;
import com.example.casting.entity.RegistBean;
import com.example.casting.processor.ProcessorID;
import com.example.casting.util.Session;
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

public class ManagerIDsAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<RegistBean> valuelist;
	Context mContext;
	ImageLoader imgloader;

	
	public void notifyDataSetChanged(ArrayList<RegistBean> list) {
		this.valuelist = list;
		super.notifyDataSetChanged();
	}
	public ManagerIDsAdapter(Context context, ArrayList<RegistBean> list) {
		super();
		mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.valuelist = list;
		imgloader = new ImageLoader(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return valuelist.size();
	}

	@Override
	public Object getItem(int position) {
		return valuelist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position ;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.iditem, null);
			holder.txtName = (TextView) convertView
					.findViewById(R.id.txt_myname);
			holder.img_user = (ImageView) convertView
					.findViewById(R.id.imgview_photo);
			holder.imgview_tag = (ImageView) convertView
					.findViewById(R.id.imgview_tag);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		RegistBean bean = valuelist.get( arg0 );
		ImageBean img = bean.getUserimp();
		int type = img.getRestype();
		if (type == ImageBean.type_filepath) {
			holder.img_user.setImageBitmap(BitmapFactory.decodeFile(img
					.getImg_res()));
		} else if (type == ImageBean.type_url) {
			imgloader.DisplayImage(
					ProcessorID.uri_headphoto + img.getImg_res(),
					holder.img_user, false);
		} else if (type == ImageBean.type_res) {
			holder.img_user
					.setImageResource(Integer.parseInt(img.getImg_res()));
		} else {
			holder.img_user.setImageResource(R.drawable.castadd_idmanager);
		}
		if(bean.getId().equals( Session.getInstance().getUser_id()) ){
			holder.imgview_tag.setVisibility(View.VISIBLE);
		}else{
			holder.imgview_tag.setVisibility(View.GONE);
		}
		holder.txtName.setText(bean.getNickname());
		return convertView;
	}

	final class ViewHolder {
		public TextView txtName;
		public ImageView img_user;
		public ImageView imgview_tag;
		
	}

}
