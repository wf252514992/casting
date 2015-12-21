package com.example.casting.login;


import com.example.casting_android.R;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

public class AdvertisementForm extends Activity{

	ImageView img_adv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.advertisementlayout);
		img_adv = (ImageView)findViewById(R.id.img_adv);
		final AnimationDrawable andrawable = (AnimationDrawable)img_adv.getBackground();
		img_adv.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				andrawable.start();
			}
		});
	}
	
}
