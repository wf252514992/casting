package com.example.casting.util.view;


import com.example.casting.util.CircleImageDrawable;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircleImageView extends ImageView{

	Context mContxt;
	public CircleImageView(Context context) {
		super(context);
		mContxt = context;
		// TODO Auto-generated constructor stub
	}
	public CircleImageView(Context context,AttributeSet set) {
		super(context,set);
		mContxt = context;
		// TODO Auto-generated constructor stub
	}
	public CircleImageView(Context context,AttributeSet set,int style) {
		super(context,set, style);
		mContxt = context;
		// TODO Auto-generated constructor stub
	}
	@Override
	public void setImageBitmap(Bitmap bm) {
		// TODO Auto-generated method stub
		setImageDrawable(new CircleImageDrawable(bm));
	}
	
	@Override
	public void setImageDrawable(Drawable drawable) {
		// TODO Auto-generated method stub
		super.setImageDrawable(drawable);
	}
	
	@Override
	public void setImageResource(int resId) {
		// TODO Auto-generated method stub
		Bitmap  mBgBitmap = BitmapFactory.decodeResource(mContxt.getResources(), resId);  
		setImageBitmap(mBgBitmap);
	}

}
