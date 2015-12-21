package com.example.casting.me3.otherwatch;

import java.util.ArrayList;
import java.util.List;
import com.example.casting_android.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.example.casting.entity.AttentionBean;
import com.example.casting.entity.BackgroundBean;
import com.example.casting.entity.BaseBean;
import com.example.casting.entity.GalleryBean;
import com.example.casting.entity.MyCountBean;
import com.example.casting.entity.RecruitBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.entity.WinnerBean;
import com.example.casting.entity.WorkBean;
import com.example.casting.mailbox.MainPrivateletterActivity;
import com.example.casting.me3.BaseMeActivity;
import com.example.casting.me3.views.CP_SelfPlayView;
import com.example.casting.me3.views.CP_WorksView;
import com.example.casting.me3.views.DR_ContactView;
import com.example.casting.me3.views.DR_WinnerView;
import com.example.casting.processor.BaseProcessor;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.company.CompanyGetProcessor;
import com.example.casting.util.CircleImageDrawable;
import com.example.casting.util.Session;
import com.example.casting.util.view.CircleImageView;
import com.example.casting.util.view.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class OtherWatchCompanyActivity extends BaseMeActivity implements
		OnCheckedChangeListener, OnClickListener {

	private View baseinfoview;
	TextView txt_name, txt_nickname, txt_introduce, txt_onclick, txt_strstatue;
	TextView txt_letter, txt_statue;
	RadioButton rdb_selfplay,rdb_selfwork,rdb_winner,rdb_userinfo;
	CircleImageView img_pic;
	View titlelayout;
	ImageView  img_regist,backgroundlayout;
	LinearLayout baseinfolayout, contentlayout;
	LinearLayout dynamiclayout, attentionlayout, fanslayout;
	TextView txt_dynamic, txt_attention, txt_fans;
	TextView txt_address;
	RadioGroup group_hot;
	CP_SelfPlayView selfplayview;
	DR_ContactView contactView;
	DR_WinnerView winnerView;
	CP_WorksView workView;
	// CP_OtherWatchView cp_baseview;
	String attentionstatue = "0";
	ImageLoader imgloader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me3_company);
		titlelayout = findViewById(R.id.titlelayout);
		imgloader = ImageLoader.getInstance();
		backgroundlayout = (ImageView) findViewById(R.id.backgroundlayout);
		baseinfolayout = (LinearLayout) findViewById(R.id.baseinfolayout);
		dynamiclayout = (LinearLayout) findViewById(R.id.dynamiclayout);
		attentionlayout = (LinearLayout) findViewById(R.id.attentionlayout);
		fanslayout = (LinearLayout) findViewById(R.id.fanslayout);
		contentlayout = (LinearLayout) findViewById(R.id.contentlayout);
		txt_dynamic = (TextView) findViewById(R.id.txt_dynamic);
		txt_attention = (TextView) findViewById(R.id.txt_attention);
		txt_fans = (TextView) findViewById(R.id.txt_fans);
		txt_address = (TextView) findViewById(R.id.txt_address);
		img_pic = (CircleImageView) findViewById(R.id.img_pic);
		img_regist = (ImageView) findViewById(R.id.img_regist);
		group_hot = (RadioGroup) findViewById(R.id.group_hot);
		group_hot.setOnCheckedChangeListener(this);
		Bitmap bmp = BitmapFactory.decodeResource(getResources(),
				R.drawable.me_edit_txsmall);
		img_pic.setImageDrawable(new CircleImageDrawable(bmp));

		baseinfoview = LayoutInflater.from(this).inflate(
				R.layout.otherwatchcompany, null);
		txt_name = (TextView) baseinfoview.findViewById(R.id.txt_name);
		txt_nickname = (TextView) baseinfoview.findViewById(R.id.txt_nickname);
		txt_introduce = (TextView) baseinfoview
				.findViewById(R.id.txt_introduce);
		txt_letter = (TextView) baseinfoview.findViewById(R.id.txt_letter);
		txt_strstatue = (TextView) baseinfoview
				.findViewById(R.id.txt_strstatue);
		txt_statue = (TextView) baseinfoview.findViewById(R.id.txt_statue);
		txt_letter.setOnClickListener(this);
		txt_statue.setOnClickListener(this);
		dynamiclayout.setOnClickListener(this);
		attentionlayout.setOnClickListener(this);
		fanslayout.setOnClickListener(this);
		// cp_baseview = new CP_OtherWatchView(this,this);
		baseinfolayout.addView(baseinfoview);
		selfplayview = new CP_SelfPlayView(this, false);
		contactView = new DR_ContactView(this, null);
		winnerView = new DR_WinnerView(this, null, false);
		workView = new CP_WorksView(this, null, false);
		
		rdb_selfplay = (RadioButton)findViewById(R.id.rdo_personshow);
		rdb_selfwork = (RadioButton)findViewById(R.id.rdo_personwork);
		rdb_winner = (RadioButton)findViewById(R.id.rdo_personwinner);
		rdb_userinfo = (RadioButton)findViewById(R.id.rdo_personphone);
		
		rdb_selfplay.setCompoundDrawables(Util.setDrawable(this,R.drawable.me_personplay,18, 18),null, null, null);
		rdb_selfwork.setCompoundDrawables(Util.setDrawable(this,R.drawable.me_personwork,18, 18),null, null, null);
		rdb_winner.setCompoundDrawables(Util.setDrawable(this,R.drawable.me_personwinner,18, 18),null, null, null);
		rdb_userinfo.setCompoundDrawables(Util.setDrawable(this,R.drawable.me_personcommunication,18, 18),null, null, null);
		
		initView(titlelayout);
		setTitle("我");
		setLeftButtonAble(true, "返回");
		changeView(selfplayview.getView());

		AttentionBean atten = new AttentionBean();
		atten.setFollowers_id(Session.getInstance().getUser_id());
		atten.setBy_follower_id(getRequestUser().getId());
		IsAddAttention(atten);

	}

	private void changeView(View view) {
		contentlayout.removeAllViews();
		contentlayout.addView(view);
	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub
	}

	boolean isrequest = false;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (isrequest)
			return;
		getMyInfofromService();
		isrequest = true;

	}

	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub
		finish();
	}

	BaseBean baseBean;

	public BaseBean getRequestUser() {
		// TODO Auto-generated method stub
		if (baseBean == null) {
			initData(getIntent());
		}
		return baseBean;
	}

	RegistBean bean;

	private void initData(Intent intent) {
		if (intent != null) {
			Bundle bd = intent.getExtras();
			if (bd != null
					&& bd.getSerializable(R.string.otherwatchcompanyactivity
							+ "") != null) {
				bean = (RegistBean) bd
						.getSerializable(R.string.otherwatchcompanyactivity
								+ "");
				setTitle(bean.getNickname());
				baseBean = bean;
			}
		}

	}

	public void updateBaseinfo(RegistBean bean) {
		// TODO Auto-generated method stub
		if (bean != null) {
			registbean = bean;
			updateView(bean);
			txt_address.setText(bean.getHometown());
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

	public void updateAttentionInfo(MyCountBean bean) {
		// TODO Auto-generated method stub
		if (bean != null) {
			txt_attention.setText(bean.getAttention());
			txt_fans.setText(bean.getFans());
		}
	}

	public void updateWorksinfo(ArrayList<WorkBean> works) {
		// TODO Auto-generated method stub
		if (works != null) {
			if(registbean!=null){
				registbean.setWorks(works);
				workView.updateView(registbean);
			}
		}
	}

	public void updateWinnerinfo(ArrayList<WinnerBean> winners) {
		// TODO Auto-generated method stub
		if (winners != null) {
			if(registbean!=null){
				registbean.setWinners(winners);
				winnerView.updateView(registbean);
			}
		}
	}

	public void updateGalleryinfo(ArrayList<GalleryBean> gallerys) {
		// TODO Auto-generated method stub
		if(registbean!=null){
			registbean.setGallerys(gallerys);
			selfplayview.updateView(registbean);
		}
	}

	public void updateCastinginfo(List<RecruitBean> recruits) {
		// TODO Auto-generated method stub

	}

	RegistBean registbean;

	boolean running = false;

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
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
	public BaseProcessor initUserProcessor() {
		// TODO Auto-generated method stub
		return new CompanyGetProcessor();
	}

	private void AddAttention() {
		BaseBean bean = getRequestUser();
		AttentionBean atten = new AttentionBean();
		atten.setFollowers_id(Session.getInstance().getUser_id());
		atten.setBy_follower_id(bean.getId());
		if (attentionstatue.equals("0")) {
			addAttention(atten);
		} else {
			delAttention(atten);
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.txt_statue) {
			// 关注
			AddAttention();
		} else if (arg0.getId() == R.id.txt_letter) {
			// 私信
			Intent intent = new Intent(this, MainPrivateletterActivity.class);
			intent.putExtra("bean", bean);
			startActivity(intent);
		} else if (arg0.getId() == R.id.attentionlayout) {
			Bundle bd = new Bundle();
			bd.putSerializable(R.string.attentionlistactivity + "", baseBean);
			doActivity(R.string.attentionlistactivity, bd);
		} else if (arg0.getId() == R.id.fanslayout) {
			// 粉丝
			Bundle bd = new Bundle();
			bd.putSerializable(R.string.myfansactivity + "", baseBean);
			doActivity(R.string.myfansactivity, bd);
		} else if (arg0.getId() == R.id.dynamiclayout) {
			Bundle bd = new Bundle();
			bd.putSerializable(R.string.MeDynamic + "",registbean);
			doActivity(R.string.MeDynamic,bd);
		}
	}

	@Override
	public void updateAttention(String statue) {
		attentionstatue = statue;
		if (statue.equals("0")) {
			// 未关注
			txt_statue.setText("未关注");
			txt_statue.setBackgroundResource(R.drawable.rect_8_attent_no);
		} else {
			// 已关注
			txt_statue.setText("已关注");
			txt_statue.setBackgroundResource(R.drawable.rect_8_attent_yes);
		}

	}

	public void updateView(RegistBean bean) {
		if (bean != null) {
			txt_name.setText(bean.getName());
			txt_nickname.setText(bean.getCompany()
					.getCompany_legal_representative());
			txt_introduce.setText(bean.getIntroduce());
			txt_strstatue.setText(bean.getPersonal_status());
		}

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
			if(!bean.getUrl_cover().equals(""))
			   imgloader.displayImage(ProcessorID.uri_headphoto + bean.getUrl_cover(),backgroundlayout, getOptions());
			else {
			    imageLoader.displayImage("drawable://"+R.drawable.me_bgztitle, backgroundlayout,getOptions());	
			}
			//			if (bgloader == null) {
//				bgloader = new BackgroundImageLoader(this,false);
//			}
//			bgloader.DisplayImage(
//					ProcessorID.uri_headphoto + bean.getUrl_cover(),
//					backgroundlayout, false);
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
