package com.example.casting.publisheddynamic;


import pl.droidsonroids.gif.GifImageView;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

public class Toas extends PopupWindow {

	PopupWindow pop;
	Context c;
	int id = 101;
	boolean canceldismiss;

	public Toas(Context c) {
		super(c);
		this.c = c;
	}

	void initView() {
		GifImageView image = new GifImageView(c);
		// image.setId(id);
		// image.setImageResource(resId);
		// image.setAnimation(AnimationUtils
		// .loadAnimation(c, R.anim.traf_ridsound));
		pop = new PopupWindow(image);

		pop.setOutsideTouchable(true);

	}

	// public void setContentView(int resId) {
	// View view = null;
	// if (pop != null && (view = pop.getContentView()) != null) {
	// if (view instanceof ImageView)
	// ((ImageView) view).setImageResource(resId);
	// }
	// }

	public void showToasShort(View v, int resId, final long time, int width,
			int height) {
		if (pop == null)
			initView();
		GifImageView image = (GifImageView) pop.getContentView();
		image.setImageResource(resId);
		pop.setWidth(width);
		pop.setHeight(height);
		pop.showAtLocation(v, Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
		hand.sendEmptyMessageDelayed(0, time);
		Log.e("yung", pop.toString());

	}

	public void showToas(View v, int resId, int width, int height) {

		if (pop == null)
			initView();
		GifImageView image = (GifImageView) pop.getContentView();
		image.setImageResource(resId);
		pop.setWidth(width);
		pop.setHeight(height);
		pop.showAtLocation(v, Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
		Log.e("yung", pop.toString());

	}

	public Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (pop != null && pop.isShowing() && !canceldismiss) {
				Log.e("yung", "canceldismiss: " + canceldismiss);
				pop.dismiss();
			}
		};
	};
}
