package com.example.casting.util;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;

public class CornerImageDrawable extends Drawable {

	private Paint mPaint;
	private int mWidth;
	private int mheight;
	// private Bitmap mBitmap ;

	private float mRoundPixels;

	public CornerImageDrawable(Bitmap bitmap, int roundPixels, int width,
			int height) {
		// mBitmap = bitmap ;
		BitmapShader bitmapShader = new BitmapShader(bitmap, TileMode.CLAMP,
				TileMode.CLAMP);
		float scalex = width * 1.0f / bitmap.getWidth();//Math.max(width * 1.0f / bitmap.getWidth(), height  
            //    * 1.0f / bitmap.getHeight());
		float scaley = height * 1.0f / bitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.setScale(scalex, scaley);
		bitmapShader.setLocalMatrix(matrix);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setShader(bitmapShader);
		mPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		mWidth = width;
		mheight = height;
		mRoundPixels = roundPixels;

	}

	@Override
	public void draw(Canvas canvas) {
		Rect rect = new Rect(0, 0, mWidth, mheight);
		RectF rectf = new RectF(rect);
		canvas.drawRoundRect(rectf, mRoundPixels, mRoundPixels, mPaint);
	}

	@Override
	public int getIntrinsicWidth() {
		return mWidth;
	}

	@Override
	public int getIntrinsicHeight() {
		return mWidth;
	}

	@Override
	public void setAlpha(int alpha) {
		mPaint.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		mPaint.setColorFilter(cf);
	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}

}
