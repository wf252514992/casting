package com.imgselect;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ViewHolder
{
	private final SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;

	Context context;
	private ViewHolder(Context context, ViewGroup parent, int layoutId,
			int position)
	{
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		// setTag
		mConvertView.setTag(this);
		this.context = context;
	}

	/**
	 * 拿到一个ViewHolder对象
	 * 
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @param position
	 * @return
	 */
	public static ViewHolder get(Context context, View convertView,
			ViewGroup parent, int layoutId, int position)
	{
		ViewHolder holder = null;
		if (convertView == null)
		{
			holder = new ViewHolder(context, parent, layoutId, position);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
			holder.mPosition = position;
		}
		return holder;
	}

	public View getConvertView()
	{
		return mConvertView;
	}

	/**
	 * 通过控件的Id获取对于的控件，如果没有则加入views
	 * 
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId)
	{
		View view = mViews.get(viewId);
		if (view == null)
		{
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * 为TextView设置字符串
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewId, String text)
	{
		TextView view = getView(viewId);
		view.setText(text);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setImageResource(int viewId, int drawableId)
	{
		ImageView view = getView(viewId);
		view.setImageResource(drawableId);

		return this;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setImageBitmap(int viewId, Bitmap bm)
	{
		ImageView view = getView(viewId);
		view.setImageBitmap(bm);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setImageByUrl(int viewId, String url,DisplayImageOptions option)
	{
//		ImageLoader.getInstance(3,Type.LIFO).loadImage(url, (ImageView) getView(viewId));
		String urlString = "file://"+url;
		ImageLoader.getInstance().displayImage(urlString,(ImageView) getView(viewId),option,new AnimateFirstDisplayListener());
		return this;
	}

	public int getPosition()
	{
		return mPosition;
	}
	  private static class AnimateFirstDisplayListener implements ImageLoadingListener {

			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				System.out.println(arg0+"-----onLoadingCancelled");
			}

			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				System.out.println(arg0+"-----onLoadingComplete");
			}

			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				System.out.println(arg0+"-----onLoadingFailed" +arg2.getCause());
				 String message = null;  
	             switch (arg2.getType()) {     // 获取图片失败类型  
	                 case IO_ERROR:              // 文件I/O错误  
	                     message = "Input/Output error";  
	                     break;  
	                 case DECODING_ERROR:        // 解码错误  
	                     message = "Image can't be decoded";  
	                     break;  
	                 case NETWORK_DENIED:        // 网络延迟  
	                     message = "Downloads are denied";  
	                     break;  
	                 case OUT_OF_MEMORY:         // 内存不足  
	                     message = "Out Of Memory error";  
	                     break;  
	                 case UNKNOWN:               // 原因不明  
	                     message = "Unknown error";  
	                     break;  
	             }  
	             System.out.println("---error----"+message);
			}

			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				System.out.println(arg0+"-----onLoadingStarted");
			}  
	          
	  
	    }  
}
