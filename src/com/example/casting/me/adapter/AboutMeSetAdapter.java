package com.example.casting.me.adapter;

import java.util.ArrayList;

import com.example.casting_android.R;
import com.example.casting_android.bean.ImageBean;
import com.example.casting_android.bean.UserInfoBean;
import com.ta.TAApplication;
import com.ta.util.bitmap.TABitmapCacheWork;
import com.ta.util.bitmap.TABitmapCallBackHanlder;
import com.ta.util.bitmap.TADownloadBitmapHandler;
import com.ta.util.extend.draw.DensityUtils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutMeSetAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	String[] strlist = { "公司资料", "我的招募", "我的视频", "我的相册", "软件设置" };
	int[] piclist = { R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher };
	Context mContext;

	public AboutMeSetAdapter(Context context ) {
		super();
		mContext = context;
		this.mInflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return strlist.length;
	}

	@Override
	public Object getItem(int position) {
		return strlist[position];
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
			convertView = mInflater.inflate(R.layout.iditem, null);
			holder.txtName = (TextView) convertView
					.findViewById(R.id.txt_myname);
			holder.img_user = (ImageView) convertView
					.findViewById(R.id.imgview_photo);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String value = strlist[arg0];
		int pic = piclist[arg0];
		holder.img_user.setImageResource(pic);
		holder.txtName.setText(value);
		return convertView;
	}

	final class ViewHolder {
		public TextView txtName;
		public ImageView img_user;
	}

}
