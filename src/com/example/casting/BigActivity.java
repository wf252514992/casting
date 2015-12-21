package com.example.casting;

import com.example.casting_android.R;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class BigActivity extends Activity{
	private BigActivity activity;
	private String picName;

	public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  requestWindowFeature(Window.FEATURE_NO_TITLE);
		  setContentView(R.layout.bigphoto);
		  activity = this;
		  Bundle bundle = this.getIntent().getExtras();
		  if(bundle!=null){
		   picName = bundle.getString("picName"); //图片名
		  }
		  ImageView img = (ImageView)this.findViewById(R.id.large_image );
		  img.setImageBitmap(BitmapFactory.decodeFile(picName));
		  img.setOnClickListener(new View.OnClickListener() { // 点击返回
		   public void onClick(View paramView) {
		    activity.finish();
		   }
		  });
		 }
}
