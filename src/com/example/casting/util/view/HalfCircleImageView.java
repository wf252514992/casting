package com.example.casting.util.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class HalfCircleImageView extends ImageView {

	private final int radio = 15;
	Context mContxt;

	public HalfCircleImageView(Context context) {
		super(context);
		mContxt = context;
		// TODO Auto-generated constructor stub
	}

	public HalfCircleImageView(Context context, AttributeSet set) {
		super(context, set);
		mContxt = context;
		// TODO Auto-generated constructor stub
	}

	public HalfCircleImageView(Context context, AttributeSet set, int style) {
		super(context, set, style);
		mContxt = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		// TODO Auto-generated method stub
		setImageDrawable(new BitmapDrawable(createFramedPhoto(bm,radio)));
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		// TODO Auto-generated method stub
		super.setImageDrawable(drawable);
	}

	@Override
	public void setImageResource(int resId) {
		// TODO Auto-generated method stub
		Bitmap mBgBitmap = BitmapFactory.decodeResource(mContxt.getResources(),
				resId);
		setImageBitmap(mBgBitmap);
	}

	private Bitmap createFramedPhoto(Bitmap image, float outerRadiusRat) {
		int x = image.getWidth();
		int y = image.getHeight();
		// 根据源文件新建一个darwable对象
		Drawable imageDrawable = new BitmapDrawable(image);

		// 新建一个新的输出图片
		Bitmap output = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		// 新建一个矩形
		RectF outerRect = new RectF(0, 0, x, y);

		// 产生一个红色的圆角矩形
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.WHITE);
		canvas.drawRoundRect(outerRect, outerRadiusRat, outerRadiusRat, paint);

		// 将源图片绘制到这个圆角矩形上
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		imageDrawable.setBounds(0, 0, x, y);
		canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
		imageDrawable.draw(canvas);
		canvas.restore();
		return output;
	}
}
