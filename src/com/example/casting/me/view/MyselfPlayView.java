package com.example.casting.me.view;

import java.util.ArrayList;

import com.example.casting.me3.views.adapter.ImageAdapter;
import com.example.casting_android.R;
import com.example.casting_android.bean.ImageBean;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class MyselfPlayView {

	private View selfplayview;

	GridView lst_selfplay;

	public MyselfPlayView(Context ctx) {
		initView(ctx);
	}

	public View getView() {
		return selfplayview;
	}

	private void initView(Context ctx) {
		selfplayview = LayoutInflater.from(ctx).inflate(
				R.layout.myview_selfplay, null);
		lst_selfplay = (GridView) selfplayview.findViewById(R.id.lst_selfplay);
		ArrayList<ImageBean> imgs = new ArrayList<ImageBean>();
		ImageBean bean1 =new ImageBean(ImageBean.type_res, R.drawable.mine_basicinf_bg+"");
		ImageBean bean2 =new ImageBean(ImageBean.type_res, R.drawable.mine_basicinf_bg+"");
		ImageBean bean3 =new ImageBean(ImageBean.type_res, R.drawable.mine_basicinf_bg+"");
		ImageBean bean4 =new ImageBean(ImageBean.type_res, R.drawable.mine_basicinf_bg+"");
		ImageBean bean5 =new ImageBean(ImageBean.type_res, R.drawable.mine_basicinf_bg+"");
		ImageBean bean6 =new ImageBean(ImageBean.type_res, R.drawable.mine_basicinf_bg+"");
		ImageBean bean7 =new ImageBean(ImageBean.type_res, R.drawable.mine_basicinf_bg+"");
		ImageBean bean8 =new ImageBean(ImageBean.type_res, R.drawable.mine_basicinf_bg+"");
		imgs.add(bean1);
		imgs.add(bean2);
		imgs.add(bean3);
		imgs.add(bean4);
		imgs.add(bean5);
		imgs.add(bean6);
		imgs.add(bean7);
		imgs.add(bean8);
		ImageAdapter imgadapter = new ImageAdapter(ctx, imgs,GridView.LayoutParams.WRAP_CONTENT,GridView.LayoutParams.WRAP_CONTENT);
		lst_selfplay.setAdapter(imgadapter);
	}

}
