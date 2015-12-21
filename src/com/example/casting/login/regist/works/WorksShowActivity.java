package com.example.casting.login.regist.works;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.example.casting.entity.WorkBean;
import com.example.casting.login.BaseForm;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.file.UploadProcessor;
import com.example.casting.util.view.RegistValueView;
import com.example.casting_android.R;
import com.example.casting_android.bean.ImageBean;
import com.nostra13.universalimageloader.core.ImageLoader;

public class WorksShowActivity extends BaseForm  {
	View titleLayout;
	View parentview;
	WorkBean work;

	ImageView imgview;
	RegistValueView  releasetimelayout, worknamelayout,
			workerlayout, directorreallayout, joinwokerslayout, beizhulayout;
	UploadProcessor filepro = new UploadProcessor();

	ImageLoader imageLoader;
//	ImageLoader imgloader ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.works_show);
//		imgloader = new ImageLoader(this);
		work = new WorkBean();
		imageLoader = ImageLoader.getInstance();
		initSubView();

	}

	protected void initSubView() {
		// TODO Auto-generated method stub
		parentview = (View) findViewById(R.id.layout_main);
		titleLayout = (View) findViewById(R.id.titlelayout);
		imgview = (ImageView) findViewById(R.id.imgview);
		releasetimelayout = (RegistValueView) findViewById(R.id.releasetimelayout);
		worknamelayout = (RegistValueView) findViewById(R.id.worknamelayout);
		workerlayout = (RegistValueView) findViewById(R.id.workerlayout);
		directorreallayout = (RegistValueView) findViewById(R.id.directorreallayout);
		joinwokerslayout = (RegistValueView) findViewById(R.id.joinwokerslayout);
		beizhulayout = (RegistValueView) findViewById(R.id.beizhulayout);
		initView(titleLayout);

		LayoutParams params = imgview.getLayoutParams();
		params.width = screen_width;
		params.height = screen_width;
		imgview.setLayoutParams(params);
		initData(getIntent());

	}

	private void setUI(WorkBean bean) {
		if (bean.getWorkpic() != null) {
			ImageBean imgbean = bean.getWorkpic();
			
			imageLoader.displayImage(ProcessorID.uri_headphoto+imgbean.getImg_res(),imgview);
		}
		releasetimelayout.setValue(bean.getRelease_time());
		worknamelayout.setValue(bean.getWorks_name());
		directorreallayout.setValue(bean.getDirector());
		joinwokerslayout.setValue(bean.getCooperation_actor());
		workerlayout.setValue(bean.getRole());
		beizhulayout.setValue(bean.getBz());
	}

	private void initData(Intent intent) {
		String title = "个人作品";
		if (intent != null) {
			Bundle bd = intent.getExtras();
			if (bd != null) {
				// 如果是编辑 获取资料，填充界面
				if (bd.getSerializable(R.string.worksshowactivity + "") != null) {
					work = (WorkBean) bd.getSerializable(R.string.worksshowactivity
							+ "");
				}
			}
		}
		setTitle(title);
		setLeftButtonAble(true, "返回");
		setUI(work);
	}

	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public void rightDialogOnclick() {
		// TODO Auto-generated method stub
		super.rightDialogOnclick();
	}
	@Override
	public void leftDialogOnclick() {
		// TODO Auto-generated method stub
		super.leftDialogOnclick();
		finish();
	}



	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub
	}


}
