package com.example.casting.me3;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.example.casting.entity.BackgroundBean;
import com.example.casting.entity.BaseBean;
import com.example.casting.entity.GalleryBean;
import com.example.casting.entity.MyCountBean;
import com.example.casting.entity.RecruitBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.entity.WinnerBean;
import com.example.casting.entity.WorkBean;
import com.example.casting.me3.views.DR_BaseInfoView;
import com.example.casting.me3.views.DR_ContactView;
import com.example.casting.me3.views.DR_SelfPlayView;
import com.example.casting.me3.views.DR_WinnerView;
import com.example.casting.me3.views.DR_WorksView;
import com.example.casting.processor.BaseProcessor;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.Users.UserGetProcessor;
import com.example.casting.util.CircleImageDrawable;
import com.example.casting.util.Session;
import com.example.casting.util.view.CircleImageView;
import com.example.casting.util.view.Util;
import com.example.casting_android.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class DirectorInfoActivity extends BaseMeActivity implements
		OnCheckedChangeListener, OnClickListener {

	View titlelayout;
	ImageView  img_regist;
	CircleImageView img_pic;
	LinearLayout baseinfolayout, contentlayout;
	LinearLayout dynamiclayout, attentionlayout, fanslayout;
	TextView txt_dynamic, txt_attention, txt_fans;
	TextView txt_school;
	RadioGroup group_hot;
	ImageView backgrounlayout;
	DR_SelfPlayView selfplayview;
	DR_ContactView contactView;
	DR_WinnerView winnerView;
	DR_WorksView workView;

	DR_BaseInfoView dr_baseview;
	RegistBean registbean;
	RadioButton rdb_selfplay,rdb_selfwork,rdb_winner,rdb_userinfo;
	
	ImageLoader imgloLoader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me3_e_director);
		imgloLoader = ImageLoader.getInstance();
		titlelayout = findViewById(R.id.titlelayout);
		backgrounlayout = (ImageView) findViewById(R.id.backgroundlayout);
		baseinfolayout = (LinearLayout) findViewById(R.id.baseinfolayout);
		dynamiclayout = (LinearLayout) findViewById(R.id.dynamiclayout);
		attentionlayout = (LinearLayout) findViewById(R.id.attentionlayout);
		fanslayout = (LinearLayout) findViewById(R.id.fanslayout);
		contentlayout = (LinearLayout) findViewById(R.id.contentlayout);
		txt_dynamic = (TextView) findViewById(R.id.txt_dynamic);
		txt_attention = (TextView) findViewById(R.id.txt_attention);
		txt_fans = (TextView) findViewById(R.id.txt_fans);
		txt_school = (TextView) findViewById(R.id.txt_school);
		img_pic = (CircleImageView) findViewById(R.id.img_pic);
		img_regist = (ImageView) findViewById(R.id.img_regist);
		group_hot = (RadioGroup) findViewById(R.id.group_hot);
		group_hot.setOnCheckedChangeListener(this);
		Bitmap bmp = BitmapFactory.decodeResource(getResources(),
				R.drawable.me_edit_txsmall);
		img_pic.setImageDrawable(new CircleImageDrawable(bmp));

		rdb_selfplay = (RadioButton)findViewById(R.id.rdo_personshow);
		rdb_selfwork = (RadioButton)findViewById(R.id.rdo_personwork);
		rdb_winner = (RadioButton)findViewById(R.id.rdo_personwinner);
		rdb_userinfo = (RadioButton)findViewById(R.id.rdo_personphone);
		
		rdb_selfplay.setCompoundDrawables(Util.setDrawable(this,R.drawable.me_personplay,18, 18),null, null, null);
		rdb_selfwork.setCompoundDrawables(Util.setDrawable(this,R.drawable.me_personwork,18, 18),null, null, null);
		rdb_winner.setCompoundDrawables(Util.setDrawable(this,R.drawable.me_personwinner,18, 18),null, null, null);
		rdb_userinfo.setCompoundDrawables(Util.setDrawable(this,R.drawable.me_personcommunication,18, 18),null, null, null);
		
		dynamiclayout.setOnClickListener(this);
		attentionlayout.setOnClickListener(this);
		fanslayout.setOnClickListener(this);
		dr_baseview = new DR_BaseInfoView(this);
		baseinfolayout.addView(dr_baseview.getView());
		selfplayview = new DR_SelfPlayView(this, true);
		contactView = new DR_ContactView(this, null);
		winnerView = new DR_WinnerView(this, null, true);
		workView = new DR_WorksView(this, null, true);
		initView(titlelayout);
		setTitle("我");
		setLeftButtonAble(false, "");
		setRightButtonAble(true, "设置");

		changeView(selfplayview.getView());
	}

	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub

	}

	private void changeView(View view) {
		contentlayout.removeAllViews();
		contentlayout.addView(view);
	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub
		doActivity(R.string.xtszactivity);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getMyInfofromService();
	}

	@Override
	public void updateBaseinfo(RegistBean bean) {
		// TODO Auto-generated method stub
		if (bean != null) {
			registbean = bean;
			dr_baseview.updateView(bean);
			txt_school.setText(bean.getEducation());
			String certifaction = bean.getCertification();
			if (certifaction.equals("1")) {
				img_regist.setVisibility(View.VISIBLE);
			} else {
				img_regist.setVisibility(View.GONE);
			}
			setPhoto(bean.getHead_portrait(), img_pic);
			contactView.updateView(bean);
		}
	}

	@Override
	public void updateAttentionInfo(MyCountBean bean) {
		// TODO Auto-generated method stub
		if (bean != null) {
			txt_attention.setText(bean.getAttention());
			txt_fans.setText(bean.getFans());
		}

	}

	@Override
	public void updateWorksinfo(ArrayList<WorkBean> works) {
		// TODO Auto-generated method stub
		if (works != null) {
			dr_baseview.updateWork(works);
			if(registbean!=null){
				registbean.setWorks(works);
				workView.updateView(registbean);
			}
		}

	}

	@Override
	public void updateWinnerinfo(ArrayList<WinnerBean> winners) {
		// TODO Auto-generated method stub
		if (winners != null) {
			if(registbean!=null){
				registbean.setWinners(winners);
				winnerView.updateView(registbean);
			}
			
		}

	}

	@Override
	public void updateGalleryinfo(ArrayList<GalleryBean> gallerys) {
		// TODO Auto-generated method stub
		if(registbean!=null){
			registbean.setGallerys(gallerys);
			selfplayview.updateView(registbean);
		}
	}

	@Override
	public void updateCastinginfo(List<RecruitBean> recruits) {
		// TODO Auto-generated method stub

	}

	boolean running = false;

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		if (!running) {
			running = true;
			int id = arg0.getCheckedRadioButtonId();
			if (id == R.id.rdo_personshow) {
				// 个人展示
				changeView(selfplayview.getView());
			} else if (id == R.id.rdo_personwork) {
				// 个人作品
				changeView(workView.getView());
			} else if (id == R.id.rdo_personwinner) {
				// 个人获奖
				changeView(winnerView.getView());
			} else if (id == R.id.rdo_personphone) {
				// 联系方式
				changeView(contactView.getView());
			}
			running = false;
		}

	}

	@Override
	public BaseBean getRequestUser() {
		RegistBean registbean = new RegistBean();
		registbean.setId(Session.getInstance().getUser_id());
		return registbean;
	}

	@Override
	public BaseProcessor initUserProcessor() {
		// TODO Auto-generated method stub
		return new UserGetProcessor();
	}

	@Override
	public void updateAttention(String statue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.fanslayout) {
			Bundle bd = new Bundle();
			bd.putSerializable(R.string.myfansactivity + "", registbean);
			doActivity(R.string.myfansactivity, bd);
		} else if (arg0.getId() == R.id.attentionlayout) {
			Bundle bd = new Bundle();
			bd.putSerializable(R.string.attentionlistactivity + "", registbean);
			doActivity(R.string.attentionlistactivity, bd);
		} else if (arg0.getId() == R.id.dynamiclayout) {
			Bundle bd = new Bundle();
			bd.putSerializable(R.string.MeDynamic + "",registbean);
			doActivity(R.string.MeDynamic,bd);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		} else
			return super.onKeyDown(keyCode, event);
	}

	@Override
	public void updateDynamicNum(String num) {
		// TODO Auto-generated method stub
		txt_dynamic.setText(num);
	}

//	BackgroundImageLoader bgloader;

	@Override
	public void updateBackground(BackgroundBean bean) {
		// TODO Auto-generated method stub
		if (bean != null) {
//			if (bgloader == null) {
//				bgloader = new BackgroundImageLoader(this,false);
//			}
			if(!bean.getUrl_cover().equals(""))
			{
			    imageLoader.displayImage(ProcessorID.uri_headphoto + bean.getUrl_cover(), backgrounlayout,getOptions());	
			}
			else {
			    imageLoader.displayImage("drawable://"+R.drawable.me_bgztitle, backgrounlayout,getOptions());	
			}
		}
	}
	private DisplayImageOptions getOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				// 不缓存到内存
				.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		return options;
	}
}
