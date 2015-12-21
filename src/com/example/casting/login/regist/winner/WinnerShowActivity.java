package com.example.casting.login.regist.winner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import com.example.casting.entity.WinnerBean;
import com.example.casting.login.BaseForm;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.file.UploadProcessor;
import com.example.casting.util.view.RegistValueView;
import com.example.casting_android.R;
import com.example.casting_android.bean.ImageBean;
import com.nostra13.universalimageloader.core.ImageLoader;

public class WinnerShowActivity extends BaseForm {
	View titleLayout;
	View parentview;
	ImageView imgview;
	RegistValueView gettimelayout, winnernamelayout, winworklayout, beizhulayout;
	WinnerBean winner;
	UploadProcessor filepro = new UploadProcessor();
	ImageLoader imgLoader ;
//	ImageLoader imgloader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.winners_show);
		imgLoader = ImageLoader.getInstance();
		winner = new WinnerBean();
		initSubView();
	}

	protected void initSubView() {
		// TODO Auto-generated method stub
		titleLayout = (View) findViewById(R.id.titlelayout);
		parentview = (View) findViewById(R.id.mainlayout);
		imgview = (ImageView) findViewById(R.id.imgview);
		gettimelayout = (RegistValueView) findViewById(R.id.gettimelayout);
		winnernamelayout = (RegistValueView) findViewById(R.id.winnernamelayout);
		winworklayout = (RegistValueView) findViewById(R.id.winworklayout);
		beizhulayout = (RegistValueView) findViewById(R.id.beizhulayout);
		LayoutParams params = imgview.getLayoutParams();
		params.width = screen_width;
		params.height = screen_width;
		imgview.setLayoutParams(params);
		
		initView(titleLayout);
		setTitle("获奖经历详情");
		setLeftButtonAble(true, "返回");
		initData(getIntent());
	}

	private void initData(Intent intent) {
		if (intent != null) {
			Bundle bd = intent.getExtras();
			if (bd != null) {
				// 如果是编辑 获取资料，填充界面
				if (bd.getSerializable(R.string.winnershowactivity + "") != null) {
					winner = (WinnerBean) bd
							.getSerializable(R.string.winnershowactivity + "");
				}
			}
		}
		setUI(winner);
	}

	private void setUI(WinnerBean bean) {
		if (bean.getWinnerspic() != null) {
			ImageBean imgbean = bean.getWinnerspic();
			imgLoader.displayImage(ProcessorID.uri_headphoto + imgbean.getImg_res(), imgview);
		}
		gettimelayout.setValue(bean.getWinners_time());
		winnernamelayout.setValue(bean.getWinners_name());
		winworklayout.setValue(bean.getWinners_works_name());
		beizhulayout.setValue(bean.getBz());
	}

	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
