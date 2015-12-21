package com.example.casting.publisheddynamic;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;
import com.example.casting.login.BaseForm;
import com.example.casting_android.R;

public class VideoPlayActivity extends BaseForm {
	private VideoView video;
	private String uri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                   WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setContentView(R.layout.videoplay);
		init();
	}

	private void init() {
		Intent intent = getIntent();
		uri = intent.getStringExtra("uri");

		video = (VideoView) findViewById(R.id.videoview);
		video.setVideoPath(uri);
//		File file = new File(uri);
//		if (file.exists()) {
//			// 获取视频的缩略图
//			Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(uri,
//					Thumbnails.MINI_KIND);
//			int degree = PicTool.readPictureDegree(uri);
//
//			if(degree!=0){//旋转照片角度
//				bitmap=PicTool.rotateBitmap(bitmap,degree);
//			}
//		}

		MediaController mediaController = new MediaController(this);
		// 设置VideView与MediaController建立关联
		video.setMediaController(mediaController);
		// 设置MediaController与VideView建立关联
		mediaController.setMediaPlayer(video);
		// 让VideoView获取焦点
		video.requestFocus();
		video.setVisibility(View.VISIBLE);
		video.start();
	}

	@Override
	public void LeftButtonClick() {
//		Intent intent = new Intent(this, EditViewDynamic.class);
//		intent.putExtra("uri", uri);
//		startActivity(intent);
		finish();
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	@Override
	public void RightButtonClick() {

	}

	
}
