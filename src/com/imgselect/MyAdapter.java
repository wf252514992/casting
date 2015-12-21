package com.imgselect;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.casting_android.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class MyAdapter extends CommonAdapter<String>
{

	/**
	 * 用户选择的图片，存储为图片的完整路径
	 */
	public static List<String> mSelectedImage = new ArrayList<String>();
	public List<String> getmSelectedImage() {
		return mSelectedImage;
	}
	DisplayImageOptions option;
	/**可选择多少张*/
	int sum = 1;
	/**
	 * 文件夹路径
	 */
	private String mDirPath;
	Context context;

	public MyAdapter(Context context, List<String> mDatas, int itemLayoutId,
			String dirPath,int sum )
	{
		super(context, mDatas, itemLayoutId);
		this.mDirPath = dirPath;
		this.context = context;
		this.sum = sum;
	}

	@Override
	public void convert(final com.imgselect.ViewHolder helper, final String item)
	{
		//设置no_pic
		helper.setImageResource(R.id.id_item_image, R.drawable.pictures_no);
		//设置no_selected
				helper.setImageResource(R.id.id_item_select,
						R.drawable.picture_unselected);
		//设置图片
		helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item,getOption());
		
		final ImageView mImageView = helper.getView(R.id.id_item_image);
		final ImageView mSelect = helper.getView(R.id.id_item_select);
		if(sum==1)
		{
			mSelect.setVisibility(View.GONE);
		}
		mImageView.setColorFilter(null);
		//设置ImageView的点击事件
		mImageView.setOnClickListener(new OnClickListener()
		{
			//选择，则将图片变暗，反之则反之
			@Override
			public void onClick(View v)
			{

				// 已经选择过该图片
				if (mSelectedImage.contains(mDirPath + "/" + item))
				{
					mSelectedImage.remove(mDirPath + "/" + item);
					mSelect.setImageResource(R.drawable.picture_unselected);
					mImageView.setColorFilter(null);
				} else
				// 未选择该图片
				{
					if(mSelectedImage.size()>=sum)
					{
						Toast.makeText(context,"最多不能超过"+sum+"张", Toast.LENGTH_SHORT).show();
						return;
					}
					mSelectedImage.add(mDirPath + "/" + item);
					mSelect.setImageResource(R.drawable.pictures_selected);
					mImageView.setColorFilter(Color.parseColor("#77000000"));
					Toast.makeText(context,"您已选了"+mSelectedImage.size()+"张", Toast.LENGTH_SHORT).show();
				}

			}
		});
		
		/**
		 * 已经选择过的图片，显示出选择过的效果
		 */
		if (mSelectedImage.contains(mDirPath + "/" + item))
		{
			mSelect.setImageResource(R.drawable.pictures_selected);
			mImageView.setColorFilter(Color.parseColor("#77000000"));
		}

	}
		private DisplayImageOptions getOption(){
	        option = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.home_videodownload)
			.showImageOnFail(R.drawable.imgbg)
			.resetViewBeforeLoading(false).cacheInMemory(false)
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
			// 不缓存到内存
			.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
			.build();
	        return option;

	    }

}
