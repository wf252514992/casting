package com.example.casting.me;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.TextView;

import com.example.casting.login.BaseForm;
import com.example.casting.login.start.ViewPagerScorller;
import com.example.casting.processor.ProcessorID;
import com.example.casting.util.ConstantData;
import com.example.casting.util.PicTool;
import com.example.casting.util.view.DragImageView;
import com.example.casting.util.view.Util;
import com.example.casting_android.R;
import com.example.casting_android.bean.ImageBean;
import com.geniuseoe2012.lazyloaderdemo.cache.FileCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * gallery图片显示
 * 
 * @author huangjian
 * 
 */
public class ShowGalleryActivity extends BaseForm implements OnClickListener,
		DragImageView.onMyImageViewClickListener {
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	public static final String TAG = "IMGS";
	public static final String POSTION = "POSTION";


	/** * viewpager要显示的界面 */
	ViewPager viewPager;
	ArrayList<ImageBean> imgs = new ArrayList<ImageBean>();
	int curren_item = 1;
	private int window_width, window_height;// 控件宽度
	private int state_height;// 状态栏的高度
	private ViewTreeObserver viewTreeObserver;

	ImageView imgSave;
	TextView textView;
	DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.home_videodownload)
			.showImageOnFail(R.drawable.imgbg).resetViewBeforeLoading(false)
			.cacheInMemory(false).imageScaleType(ImageScaleType.EXACTLY)
			// 不缓存到内存
			.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.showgallery);
		// imglLoader = new ImageLoader(this);
		viewPager = (ViewPager) findViewById(R.id.main_viewpager);
		imgSave = (ImageView) findViewById(R.id.btn_save);
		textView = (TextView) findViewById(R.id.tv_page);
		imgSave.setOnClickListener(this);

		/** 初始化ViewPagerScorller，将其与viewpager绑定 */
		initViewPagerScroll();
		getIntentInfo();
		// initViews();
		// 如果之前有保存用户数据
		if (savedInstanceState != null) {
			curren_item = savedInstanceState.getInt(POSTION);
		}
		viewPager.setAdapter(new ImagePagerAdapter(imgs));
		viewPager.setOnPageChangeListener(new MyViewPageOnPageChangeListener());
		viewPager.setCurrentItem(curren_item);
		textView.setText((curren_item + 1) + "/" + imgs.size());
	}

	public class MyViewPageOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			curren_item = arg0;
			textView.setText((arg0 + 1) + "/" + imgs.size());

		}

	}

	public void getIntentInfo() {
		Intent intent = getIntent();
		if (intent != null && intent.getSerializableExtra(TAG) != null) {
			imgs = (ArrayList<ImageBean>) intent.getSerializableExtra(TAG);
			String string = intent.getStringExtra(POSTION).toString();
			curren_item = Integer.parseInt(string);
		}

	}

	// public void initViews() {
	// int size = imgs.size();
	// for (int i = 0; i < size; i++) {
	// View view = LayoutInflater.from(this).inflate(R.layout.showphoto,
	// null);
	// // final DragImageView imgView = (DragImageView)
	// // view.findViewById(R.id.imgview);
	// // initImgView(imgView);
	// // ImageView imgSave = (ImageView) view.findViewById(R.id.btn_save);
	// // imgSave.setOnClickListener(this);
	// // TextView textView = (TextView)view.findViewById(R.id.tv_page);
	// // textView.setText((i+1)+"/"+size);
	// // initImgView(imgView , i);
	// views.add(view);
	//
	// }
	//
	// }
	// public void initImgView(final View view, int position) {
	//
	// final DragImageView imgView = (DragImageView) view;//
	// .findViewById(R.id.imgview);
	// // imgView.setLayoutParams(new
	// // LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
	// // screen_height-80));
	// // imgView.setFocusable(true);
	// // imgView.setClickable(true);
	// // imgView.setOnClickListener(this);
	// /** 获取可見区域高度 **/
	// // WindowManager manager = getWindowManager();
	// // window_width = manager.getDefaultDisplay().getWidth();
	// // window_height = manager.getDefaultDisplay().getHeight();
	// imgView.setmActivity(this);// 注入Activity.
	// // imgView.setOnImageViewClickListener(this);
	// ImageBean bean = imgs.get(position);
	// imgView.setOnImageViewClickListener(this);
	// bigloader.DisplayImage(ProcessorID.uri_headphoto + bean.getImg_res(),
	// imgView, false);
	// }

	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub

	}

	/**
	 * 设置ViewPager的滑动速度
	 * 
	 * */
	private void initViewPagerScroll() {
		try {
			Field mScroller = null;
			mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			ViewPagerScorller scroller = new ViewPagerScorller(
					viewPager.getContext());
			scroller.setScrollDuration(500);
			mScroller.set(viewPager, scroller);
		} catch (NoSuchFieldException e) {

		} catch (IllegalArgumentException e) {

		} catch (IllegalAccessException e) {

		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_save) {
			String url = imgs.get(curren_item).getImg_res();
			FileCache fileCache = new FileCache(this);
			String oldUrl = fileCache.getSavePath(ProcessorID.uri_headphoto
					+ url);
			String fileName = oldUrl.substring(oldUrl.lastIndexOf("/") + 1)
					+ ".jpg";
			Util.copyFile(oldUrl, ConstantData.getImgDir() + "/" + fileName,
					this);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onImageViewClick() {
		this.finish();
	}

	private class ImagePagerAdapter extends PagerAdapter {

		ArrayList<ImageBean> images = new ArrayList<ImageBean>();
		private LayoutInflater inflater;

		ImagePagerAdapter(ArrayList<ImageBean> list) {
			this.images = list;
			inflater = getLayoutInflater();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public void finishUpdate(View container) {
		}

		@Override
		public int getCount() {
			return images.size();
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {

			View imageLayout = inflater.inflate(R.layout.item_pager_image,
					view, false);
			DragImageView imageView = (DragImageView) imageLayout
					.findViewById(R.id.image);
			imageView.setmActivity(ShowGalleryActivity.this);// 注入Activity.
			imageView.setOnImageViewClickListener(ShowGalleryActivity.this);
			final ProgressBar spinner = (ProgressBar) imageLayout
					.findViewById(R.id.loading);

			imageLoader.displayImage(
					ProcessorID.uri_headphoto
							+ images.get(position).getImg_res(), imageView,
					options, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							spinner.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							String message = null;
							switch (failReason.getType()) { // 获取图片失败类型
							case IO_ERROR: // 文件I/O错误
								message = "Input/Output error";
								break;
							case DECODING_ERROR: // 解码错误
								message = "Image can't be decoded";
								break;
							case NETWORK_DENIED: // 网络延迟
								message = "Downloads are denied";
								break;
							case OUT_OF_MEMORY: // 内存不足
								message = "Out Of Memory error";
								break;
							case UNKNOWN: // 原因不明
								message = "Unknown error";
								break;
							}
							Toast.makeText(ShowGalleryActivity.this, message,
									Toast.LENGTH_SHORT).show();

							spinner.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							spinner.setVisibility(View.GONE); // 不显示圆形进度条
						}
					});

			((ViewPager) view).addView(imageLayout, 0); // 将图片增加到ViewPager
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View container) {
		}
	}

}
