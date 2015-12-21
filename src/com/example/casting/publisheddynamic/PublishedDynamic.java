package com.example.casting.publisheddynamic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.casting.login.BaseForm;
import com.example.casting.util.Session;
import com.example.casting_android.R;

/**
 * 发布动态菜单界面
 * @author chenjiaping
 *
 */
public class PublishedDynamic extends BaseForm implements OnClickListener{
	/** Called when the activity is first created. */
	// TranslateAnimation showAction, hideAction;
    private String action;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		Intent intent=getIntent();
		action=intent.getStringExtra("action");
		if(action!=null&&action.equals("normal")){
			setContentView(R.layout.main_addform);
		}else{
		   setContentView(R.layout.publisheddynamic);
		}
//		animation();
		init();
	}
	private void init(){
		if(action!=null&&action.equals("normal")){
			
			LinearLayout word=(LinearLayout) findViewById(R.id.word);
			LinearLayout photos=(LinearLayout) findViewById(R.id.photos);
			LinearLayout takephoto=(LinearLayout) findViewById(R.id.takephoto);
			LinearLayout voice=(LinearLayout) findViewById(R.id.voice);
			LinearLayout cancel=(LinearLayout) findViewById(R.id.cancel);
			cancel.setOnClickListener(this);
			
			word.setOnClickListener(this);
			photos.setOnClickListener(this);
			takephoto.setOnClickListener(this);
			voice.setOnClickListener(this);
		}else{
		RelativeLayout recruit=(RelativeLayout) findViewById(R.id.recruit);
		recruit.setOnClickListener(this);
		RelativeLayout word=(RelativeLayout) findViewById(R.id.word);
		RelativeLayout photos=(RelativeLayout) findViewById(R.id.photos);
		RelativeLayout takephoto=(RelativeLayout) findViewById(R.id.takephoto);
		RelativeLayout voice=(RelativeLayout) findViewById(R.id.voice);
		RelativeLayout cancel=(RelativeLayout) findViewById(R.id.cancel);
		cancel.setOnClickListener(this);
		
		word.setOnClickListener(this);
		photos.setOnClickListener(this);
		takephoto.setOnClickListener(this);
		voice.setOnClickListener(this);
		}
		
	}
    private void animation(){
    	RelativeLayout menu = (RelativeLayout) findViewById(R.id.menu);
		// 这里是 TranslateAnimation 动画
    	Animation showAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		// 这里是 ScaleAnimation 动画
		// showAction = new ScaleAnimation(
		// 1.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
		// Animation.RELATIVE_TO_SELF, 0.0f);
		showAction.setDuration(500);

		// 这里是 TranslateAnimation 动画
		Animation hideAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
		// 这里是 ScaleAnimation 动画
		// hideAction = new ScaleAnimation(
		// 1.0f, 1.0f, 1.0f, 0.0f,
		// Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
		// 0.0f);
		hideAction.setDuration(500);
		boolean menuShowed = false;
		if(menu!=null){
			menu.setVisibility(View.GONE);
			if (menuShowed) {
				menuShowed = false;
				menu.startAnimation(hideAction);
				menu.setVisibility(View.GONE);
				
			} else {
				menuShowed = true;
				menu.startAnimation(showAction);
				menu.setVisibility(View.VISIBLE);
			}
		}
    }
	@Override
	public void LeftButtonClick() {
	}

	@Override
	public void RightButtonClick() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.recruit:
//			ImageView recruitimg=(ImageView) findViewById(R.id.recruitimg);
//			Animation myAnimation_Scale= AnimationUtils.loadAnimation(this,R.anim.my_scale_action);  
//			recruitimg.startAnimation(myAnimation_Scale);  
            Intent intent=new Intent(this,EditRecruitorWordDynamic.class);
            intent.putExtra("op", "recruit");
            startActivity(intent);
            finish();
			break;
		case R.id.word:
//			ImageView wordimg=(ImageView) findViewById(R.id.wordimg);
//			myAnimation_Scale= AnimationUtils.loadAnimation(this,R.anim.my_scale_action);  
//			wordimg.startAnimation(myAnimation_Scale);
			intent=new Intent(this,EditRecruitorWordDynamic.class);
            startActivity(intent);
            finish();
			break;
		case R.id.photos:
//			ImageView photosimg=(ImageView) findViewById(R.id.photosimg);
//			myAnimation_Scale= AnimationUtils.loadAnimation(this,R.anim.my_scale_action);  
//			photosimg.startAnimation(myAnimation_Scale);
			intent=new Intent(this,ImgsActivity.class);
            startActivity(intent);
            finish();
			break;
		case R.id.takephoto:
//			ImageView takephotoimg=(ImageView) findViewById(R.id.takephotoimg);
//			myAnimation_Scale= AnimationUtils.loadAnimation(this,R.anim.my_scale_action);  
//			takephotoimg.startAnimation(myAnimation_Scale);
//			intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//			startActivityForResult(intent, 0);
			intent=new Intent(this,RecordVideo.class);
            startActivity(intent);
            finish();
			break;
		case R.id.voice:
//			ImageView voiceimg=(ImageView) findViewById(R.id.voiceimg);
//			myAnimation_Scale= AnimationUtils.loadAnimation(this,R.anim.my_scale_action);  
//			voiceimg.startAnimation(myAnimation_Scale);
			intent=new Intent(this,EditVoiceDynamic.class);
            startActivity(intent);
            finish();
			break;
		case R.id.cancel:
			finish();
			break;
		default:
			break;
		}

	}
	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (resultCode == Activity.RESULT_OK && requestCode == 0) {
//			Uri uri = data.getData();
//			Intent intent=new Intent(this,EditViewDynamic.class);
//			intent.putExtra("uri", uri);
//		    startActivity(intent);
//		}
//	}

}
