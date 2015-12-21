package com.example.casting.publisheddynamic;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

//import com.example.casting.MainTab;
import com.example.casting.login.BaseForm;
import com.example.casting.publisheddynamic.ImgsAdapter.OnItemClickClass;
import com.example.casting_android.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ImgsActivity extends BaseForm implements OnClickListener{

	Bundle bundle;
	ArrayList<FileTraversal> fileTraversal;
	GridView imgGridView;
	ImgsAdapter imgsAdapter;
	Util util;
	RelativeLayout relativeLayout2;
	HashMap<Integer, ImageView> hashImage;
	Button choise_button;
	ArrayList<String> filelist;
	ArrayList<String> list;
//	@TAInjectView(id = R.id.titlelayout)
	private View titleLayout;
	private  int maxNum=9;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imgs);
		titleLayout=findViewById(R.id.titlelayout);
		initView(titleLayout);
		setLeftButtonAble(true, "返回");
		setTitle("相机胶卷");
		util=new Util(this);
		Intent intent=getIntent();
		int endNum=intent.getIntExtra("endNum", 0);
		if(endNum>0){
			maxNum=endNum;
		}
		imgGridView=(GridView) findViewById(R.id.gridView1);
		bundle= getIntent().getExtras();
		fileTraversal=util.LocalImgFileList();
		list=new ArrayList<String>();
		for (int i = 0; i < fileTraversal.size(); i++) {
			FileTraversal file=fileTraversal.get(i);
			for (int j = 0; j < file.filecontent.size(); j++) {
				list.add(file.filecontent.get(j));
			}
		}
		imgsAdapter=new ImgsAdapter(this, list,onItemClickClass);
		imgGridView.setAdapter(imgsAdapter);
		relativeLayout2=(RelativeLayout) findViewById(R.id.relativeLayout2);
		choise_button=(Button) findViewById(R.id.button3);
		choise_button.setOnClickListener(this);
		hashImage=new HashMap<Integer, ImageView>();
		filelist=new ArrayList<String>();
//		imgGridView.setOnItemClickListener(this);
		
	}
	
	@SuppressLint("NewApi")
	public ImageView iconImage(String filepath,int index,CheckBox checkBox) throws FileNotFoundException{
		LinearLayout.LayoutParams params=new LayoutParams(relativeLayout2.getMeasuredHeight()-10, relativeLayout2.getMeasuredHeight()-10);
		ImageView imageView=new ImageView(this);
		imageView.setLayoutParams(params);
		imageView.setBackgroundResource(R.drawable.imgbg);
		float alpha=100;
		imageView.setAlpha(alpha);
		util.imgExcute(imageView, imgCallBack, filepath);
		imageView.setOnClickListener(new ImgOnclick(filepath,checkBox));
		return imageView;
	}
	
	ImgCallBack imgCallBack=new ImgCallBack() {
		@Override
		public void resultImgCall(ImageView imageView, Bitmap bitmap) {
			imageView.setImageBitmap(bitmap);
		}
	};
	
	class ImgOnclick implements OnClickListener{
		String filepath;
		CheckBox checkBox;
		public ImgOnclick(String filepath,CheckBox checkBox) {
			this.filepath=filepath;
			this.checkBox=checkBox;
		}
		@Override
		public void onClick(View arg0) {
			checkBox.setChecked(false);
			filelist.remove(filepath);
		}
	}
	
	ImgsAdapter.OnItemClickClass onItemClickClass=new OnItemClickClass() {
		@Override
		public void OnItemClick(View v, int Position, CheckBox checkBox) {
			String filapath=list.get(Position);
			if (checkBox.isChecked()) {
				checkBox.setChecked(false);
				filelist.remove(filapath);
			}else {
				try {
					if (filelist.size() < maxNum) {
						checkBox.setChecked(true);
						Log.i("img", "img choise position->" + Position);
						ImageView imageView = iconImage(filapath, Position,
								checkBox);
						if (imageView != null) {
							hashImage.put(Position, imageView);
							filelist.add(filapath);
						}
					} else {
						Toast.makeText(ImgsActivity.this, "最多添加9张图片",
								Toast.LENGTH_LONG).show();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	};
	
	public void tobreak(View view){
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button3:
			Intent intent=getIntent();
			String action=intent.getStringExtra("action");
			if(action!=null){
				intent = new Intent();
				intent.putStringArrayListExtra("filelist", filelist);
				setResult(Activity.RESULT_OK, intent);
			}else{
			intent=new Intent(this,EditPhotoDynamic.class);
			intent.putStringArrayListExtra("filelist", filelist);
			startActivity(intent);
			}
			finish();
			break;

		default:
			break;
		}
		
	}

	@Override
	public void LeftButtonClick() {
//		Intent intent=new Intent(this,MainTab.class);
//		startActivity(intent);
		finish();
	}

	@Override
	public void RightButtonClick() {
		
	}
	
	
}
