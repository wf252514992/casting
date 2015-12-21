package com.example.casting.xtsz;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableRow;

import com.example.casting.listener.CloseAllListener;
import com.example.casting.listener.ListenerManager;
import com.example.casting.login.BaseTakePhoneForm;
import com.example.casting_android.R;

public class XtszActivity extends BaseTakePhoneForm implements OnClickListener,CloseAllListener{

	View titleLayout;
	View mainlayout;
	TableRow txt_id;
	TableRow txt_uploadmypic;
	TableRow txt_notifaction;
	TableRow txt_ideal;
	TableRow txt_clearcatch;
	TableRow txt_checkversion;
	TableRow txt_aboutus;
	Button btn_submit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xtszlist);
		titleLayout =(View) findViewById(R.id.titlelayout);
		mainlayout =(View) findViewById(R.id.mainlayout);
		txt_id =(TableRow) findViewById(R.id.txt_id);
		txt_uploadmypic =(TableRow) findViewById(R.id.txt_uploadmypic);
		txt_notifaction =(TableRow) findViewById(R.id.txt_notifaction);
		txt_ideal =(TableRow) findViewById(R.id.txt_ideal);
		txt_clearcatch =(TableRow) findViewById(R.id.txt_clearcatch);
		txt_checkversion =(TableRow) findViewById(R.id.txt_checkversion);
		txt_aboutus =(TableRow) findViewById(R.id.txt_aboutus);
		btn_submit =(Button) findViewById(R.id.btn_submit);
		txt_uploadmypic.setVisibility(View.GONE);
		initView(titleLayout);
		setLeftButtonAble(true, "返回");
		txt_id.setOnClickListener(this);
		txt_notifaction.setOnClickListener(this);
		txt_uploadmypic.setOnClickListener(this);
		txt_ideal.setOnClickListener(this);
		txt_clearcatch.setOnClickListener(this);
		txt_checkversion.setOnClickListener(this);
		txt_aboutus.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		setTitle("软件设置");
		ListenerManager.addCloseListener(this);
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
		ListenerManager.removeCloseListener(this);
	}
	
	
	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		if(arg0.getId() == R.id.txt_id){
			doActivity(R.string.manageridsactivity);
		}else if(arg0.getId() == R.id.txt_notifaction){
			doActivity(R.string.notifactionactivity);
		}else if(arg0.getId() == R.id.txt_ideal){
			doActivity(R.string.feedbackactivity);
		}else if(arg0.getId() == R.id.txt_clearcatch ){
			String value = getResources().getString(R.string.clearcachenotify);
			showAlertDialog(value);
		}else if(arg0.getId() == R.id.txt_checkversion ){
			showToast("当前已是最新版本");
		}else if(arg0.getId() == R.id.txt_uploadmypic){
			showSelectPicDialog();
		}else if(arg0.getId() == R.id.txt_aboutus ){
			doActivity(R.string.aboutmeactivity);
		}else if(arg0.getId() == R.id.btn_submit ){
//			existApp(false);
			ListenerManager.notifyCloseActivity();
			finish();
		}
	}
	
	
	DialogInterface.OnClickListener onclick = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
	};

	@Override
	public View getParentView() {
		// TODO Auto-generated method stub
		return mainlayout;
	}


	/**
	 * 用当前时间给取得的图片命名
	 * 
	 */
	public String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy_MM_dd_HH_mm_ss");
		return dateFormat.format(date) + ".jpg";
	}

	@Override
	public void OnPicReturn(String filepath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDirs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void closeActivity() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public int getActivityid() {
		// TODO Auto-generated method stub
		return R.string.xtszactivity;
	}

	@Override
	public void uploadError() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uploadSuccess(String url) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uploadFiald(String msg) {
		// TODO Auto-generated method stub
		
	}
}
