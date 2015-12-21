package com.example.casting.me3.otherwatch;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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

public class OtherWatchDirectorActivity extends BaseMeActivity implements
		OnCheckedChangeListener, OnClickListener {

	private View baseinfoview;
	Context mContext;
	TextView txt_name, txt_address, txt_age, txt_introduce, txt_works,
			txt_strstate;
	ImageView img_sex;
	TextView txt_letter, txt_statue;
	ImageView backgroundlayout;
	RadioButton rdb_selfplay,rdb_selfwork,rdb_winner,rdb_userinfo;
	CircleImageView img_pic;
	View titlelayout;
	ImageView img_regist;
	LinearLayout baseinfolayout, contentlayout;
	LinearLayout dynamiclayout, attentionlayout, fanslayout;
	TextView txt_dynamic, txt_attention, txt_fans;
	TextView txt_school;
	RadioGroup group_hot;
	DR_SelfPlayView selfplayview;
	DR_ContactView contactView;
	DR_WinnerView winnerView;
	DR_WorksView workView;
	// DR_OtherWatchView dr_baseview;
	RegistBean registbean;
	ImageLoader imgloLoader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me3_director);
		imgloLoader = ImageLoader.getInstance();
		titlelayout = findViewById(R.id.titlelayout);
		baseinfolayout = (LinearLayout) findViewById(R.id.baseinfolayout);
		backgroundlayout = (ImageView) findViewById(R.id.backgroundlayout);
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
		baseinfoview = LayoutInflater.from(this).inflate(
				R.layout.otherwatchdirector, null);
		txt_name = (TextView) baseinfoview.findViewById(R.id.txt_name);
		txt_address = (TextView) baseinfoview.findViewById(R.id.txt_address);
		txt_age = (TextView) baseinfoview.findViewById(R.id.txt_age);
		txt_introduce = (TextView) baseinfoview
				.findViewById(R.id.txt_introduce);
		txt_works = (TextView) baseinfoview.findViewById(R.id.txt_works);
		txt_strstate = (TextView) baseinfoview.findViewById(R.id.txt_strstate);
		txt_letter = (TextView) baseinfoview.findViewById(R.id.txt_letter);
		txt_statue = (TextView) baseinfoview.findViewById(R.id.txt_statue);
		img_sex = (ImageView) baseinfoview.findViewById(R.id.img_sex);
		txt_letter.setOnClickListener(this);
		txt_statue.setOnClickListener(this);
		dynamiclayout.setOnClickListener(this);
		attentionlayout.setOnClickListener(this);
		fanslayout.setOnClickListener(this);
		group_hot.setOnCheckedChangeListener(this);
		Bitmap bmp = BitmapFactory.decodeResource(getResources(),
				R.drawable.me_edit_txsmall);
		img_pic.setImageDrawable(new CircleImageDrawable(bmp,img_pic.getWidth(),img_pic.getHeight()));
		// dr_baseview = new DR_OtherWatchView(this,this);
		// baseinfolayout.addView(dr_baseview.getView());
		baseinfolayout.addView(baseinfoview);
		selfplayview = new DR_SelfPlayView(this, false);
		contactView = new DR_ContactView(this, null);
		winnerView = new DR_WinnerView(this, null, false);
		workView = new DR_WorksView(this, null, false);
		
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

	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub
		finish();
	}

	private void changeView(View view) {
		contentlayout.removeAllViews();
		contentlayout.addView(view);
	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub
	}

	boolean request = false;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (request)
			return;
		getMyInfofromService();
		request = true;
	}

	@Override
	public void updateBaseinfo(RegistBean bean) {
		// TODO Auto-generated method stub
		if (bean != null) {
			registbean = bean;
			updateView(bean);
			contactView.updateView(bean);
			txt_school.setText(bean.getEducation());
			String certifaction = bean.getCertification();
			if (certifaction.equals("1")) {
				img_regist.setVisibility(View.VISIBLE);
			} else {
				img_regist.setVisibility(View.GONE);
			}
			setPhoto(bean.getHead_portrait(), img_pic);
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
			updateWork(works);
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

	BaseBean baseBean;

	@Override
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
					&& bd.getSerializable(R.string.otherwatchdirectoractivity
							+ "") != null) {
				bean = (RegistBean) bd
						.getSerializable(R.string.otherwatchdirectoractivity
								+ "");
				setTitle(bean.getNickname());
				baseBean = bean;
			}
		}
	}

	@Override
	public BaseProcessor initUserProcessor() {
		// TODO Auto-generated method stub
		return new UserGetProcessor();
	}

	@Override
	public void updateAttention(String statue) {
		// TODO Auto-generated method stub
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

	String attentionstatue = "0";

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
		} else if (arg0.getId() == R.id.fanslayout) {
			// 粉丝
			Bundle bd = new Bundle();
			bd.putSerializable(R.string.myfansactivity + "", baseBean);
			doActivity(R.string.myfansactivity, bd);
		} else if (arg0.getId() == R.id.dynamiclayout) {
			Bundle bd = new Bundle();
			bd.putSerializable(R.string.MeDynamic + "",baseBean);
			doActivity(R.string.MeDynamic,bd);
		} else if (arg0.getId() == R.id.attentionlayout) {
			// 关注
			Bundle bd = new Bundle();
			bd.putSerializable(R.string.attentionlistactivity + "", baseBean);
			doActivity(R.string.attentionlistactivity, bd);

		}
	}

	public void updateView(RegistBean bean) {
		if (baseinfoview == null)
			return;
		if (bean != null) {
			txt_name.setText(bean.getNickname());
			txt_address.setText(bean.getHometown());
			txt_age.setText(bean.getAge() + "岁");
			txt_introduce.setText(bean.getIntroduce());
			txt_strstate.setText(bean.getPersonal_status());
			setworks(bean.getWorks());
			setSex(bean.getSex());
		}
	}

	public void updateWork(List<WorkBean> works) {
		setworks(works);
	}

	private void setSex(String sex) {
		if (sex.equals("男")) {
			img_sex.setImageResource(R.drawable.mine_sex_boy);
		} else {
			img_sex.setImageResource(R.drawable.mine_sex_girl);
		}
	}

	private void setworks(List<WorkBean> works) {
		if (works != null && works.size() > 0) {
			WorkBean work = works.get(0);
			txt_works.setText(work.getWorks_name());

		}
	}

	@Override
	public void updateDynamicNum(String num) {
		// TODO Auto-generated method stub
		txt_dynamic.setText(num);
	}


	@Override
	public void updateBackground(BackgroundBean bean) {
		// TODO Auto-generated method stub
		if (bean != null) {
			if(!bean.getUrl_cover().equals(""))
			{
				imgloLoader.displayImage(ProcessorID.uri_headphoto + bean.getUrl_cover(),backgroundlayout,getOptions());
			}
			else {
			    imageLoader.displayImage("drawable://"+R.drawable.me_bgztitle, backgroundlayout,getOptions());	
			}
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
