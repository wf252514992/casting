package com.example.casting.me3.otherwatch;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.casting.entity.AttentionBean;
import com.example.casting.entity.BackgroundBean;
import com.example.casting.entity.BaseBean;
import com.example.casting.entity.GalleryBean;
import com.example.casting.entity.MyCountBean;
import com.example.casting.entity.RecruitBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.entity.WinnerBean;
import com.example.casting.entity.WorkBean;
import com.example.casting.login.BaseForm;
import com.example.casting.mailbox.MainPrivateletterActivity;
import com.example.casting.me3.views.Me_BaseInfoView;
import com.example.casting.me3.views.Me_CastingView;
import com.example.casting.me3.views.Me_SelfPlayView;
import com.example.casting.me3.views.Me_VideosView;
import com.example.casting.me3.views.Me_WinnersView;
import com.example.casting.me3.views.Me_WorksView;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.Users.UserGetProcessor;
import com.example.casting.processor.attention.AddAttentionProcessor;
import com.example.casting.processor.attention.DelAttentionProcessor;
import com.example.casting.processor.attention.GetCountProcessor;
import com.example.casting.processor.attention.IsAttentionProcessor;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.processor.dynamic.GetDynamicCountProcessor;
import com.example.casting.processor.gallery.GetBackgroundProcessor;
import com.example.casting.processor.gallery.GetListGalleryProcessor;
import com.example.casting.processor.recruit.GetRecruitListProcessor;
import com.example.casting.processor.winners.WinnerGetListProcessor;
import com.example.casting.processor.works.GetWorksProcessor;
import com.example.casting.util.CircleImageDrawable;
import com.example.casting.util.Session;
import com.example.casting.util.view.CircleImageView;
import com.example.casting.util.view.Util;
import com.example.casting_android.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class OtherWatchMeActivity extends BaseForm implements OnClickListener {
	View titleLayout;

	ImageView backgroundlayout;
	LinearLayout layout_content;
	TextView txt_dynamic;
	TextView txt_attention;
	TextView txt_fans;
	TextView txt_statue;
	TextView txt_letter;
	TextView txt_name;
	TextView txt_msgstatue;
	ImageView img_regist;
	CircleImageView img_pic;
	UserGetProcessor usergetPro;
	GetWorksProcessor getworkPro;
	GetCountProcessor countPro;
	WinnerGetListProcessor getwinnerPro;
	GetListGalleryProcessor galleryPro;
	GetRecruitListProcessor recruitPro;
	GetDynamicCountProcessor getDynamicPro;
	GetBackgroundProcessor getBackgroundProcessor;
	Me_BaseInfoView baseinfo;
	Me_WorksView worksinfo;
	Me_CastingView castinginfo;
	Me_WinnersView winnerinfo;
	Me_SelfPlayView selfphotoinfo;
	Me_VideosView videosinfo;
	ImageLoader imgloader = ImageLoader.getInstance();
	IsAttentionProcessor isAddPro = new IsAttentionProcessor();

	private int subview_width = 0;
	private int subview_height = 0;
	BaseBean baseBean;
	private RegistBean bean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.otherwatchme3);
		usergetPro = new UserGetProcessor();
		getworkPro = new GetWorksProcessor();
		countPro = new GetCountProcessor();
		getwinnerPro = new WinnerGetListProcessor();
		galleryPro = new GetListGalleryProcessor();
		recruitPro = new GetRecruitListProcessor();
		getDynamicPro = new GetDynamicCountProcessor();
		getBackgroundProcessor = new GetBackgroundProcessor();
		initOptions();
		init();
	}

	protected void init() {
		titleLayout = (View) findViewById(R.id.titlelayout);
		layout_content = (LinearLayout) findViewById(R.id.layout_content);
		backgroundlayout = (ImageView) findViewById(R.id.backgroundlayout);
		img_pic = (CircleImageView) findViewById(R.id.img_pic);
		img_regist = (ImageView) findViewById(R.id.img_regist);
		txt_dynamic = (TextView) findViewById(R.id.txt_dynamic);
		txt_attention = (TextView) findViewById(R.id.txt_attention);
		txt_statue = (TextView) findViewById(R.id.txt_statue);
		txt_letter = (TextView) findViewById(R.id.txt_letter);
		txt_fans = (TextView) findViewById(R.id.txt_fans);
		txt_name = (TextView) findViewById(R.id.txt_name);
		txt_msgstatue = (TextView) findViewById(R.id.txt_msgstatue);
		txt_dynamic.setOnClickListener(this);
		txt_attention.setOnClickListener(this);
		txt_statue.setOnClickListener(this);
		txt_letter.setOnClickListener(this);
		txt_fans.setOnClickListener(this);
		initView(titleLayout);
		initView();
		initData(getIntent());

		AttentionBean atten = new AttentionBean();
		atten.setFollowers_id(Session.getInstance().getUser_id());
		atten.setBy_follower_id(baseBean.getId());
		HttpCall(isAddPro, atten);
	}

	public void initView() {
		Bitmap bmp = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_launcher);
		img_pic.setImageDrawable(new CircleImageDrawable(bmp));
		baseinfo = new Me_BaseInfoView(this);
		worksinfo = new Me_WorksView(this, false);
		castinginfo = new Me_CastingView(this);
		winnerinfo = new Me_WinnersView(this, false);
		selfphotoinfo = new Me_SelfPlayView(this);
		videosinfo = new Me_VideosView(this);
		subview_width = getWidth();
		subview_height = getHeight();
		LayoutParams params = new LayoutParams(subview_width, subview_height);
		layout_content.addView(worksinfo.getView(), params);
		layout_content.addView(castinginfo.getView(), params);
		layout_content.addView(baseinfo.getView(), params);
		layout_content.addView(selfphotoinfo.getView(), params);
		layout_content.addView(winnerinfo.getView(), params);
		layout_content.addView(videosinfo.getView(), params);

	}

	private void initData(Intent intent) {
		setLeftButtonAble(true, "返回");
		if (intent != null) {
			Bundle bd = intent.getExtras();
			if (bd != null
					&& bd.getSerializable(R.string.otherwatchmeactivity + "") != null) {
				bean = (RegistBean) bd
						.getSerializable(R.string.otherwatchmeactivity + "");
				setTitle(bean.getNickname());
				baseBean = bean;
			}
		}

	}

	boolean showbaseinfoflag = false;
	boolean showworkinfoflag = false;
	boolean showwinnerinfoflag = false;
	boolean showselfplayfoflag = false;
	boolean showvideoinfoflag = false;
	boolean showrecruitflag = false;
	boolean showcastinginfoflag = false;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getMyInfofromService();
	}

	/**
	 * 获取我的资料
	 */
	private void getMyInfofromService() {
		if (baseBean == null) {
			showToast("未获取到合法信息");
			return;
		}
		/**
		 * 获取 动态数，粉丝数，
		 */
		HttpCall(false, countPro, baseBean);
		/**
		 * 获取 动态数 ，
		 */
		HttpCall(false, getDynamicPro, baseBean);
		/**
		 * 获取用户基本信息
		 */
		if (!showbaseinfoflag)
			HttpCall(false, usergetPro, baseBean);
		/**
		 * 获取用户作品信息
		 */
		if (!showworkinfoflag)
			HttpCall(false, getworkPro, baseBean);
		/**
		 * 获取我展示的照片
		 */
		if (!showselfplayfoflag)
			HttpCall(false, galleryPro, baseBean);
		/**
		 * 获取 我的试镜
		 */
		if (!showrecruitflag)
			HttpCall(false, recruitPro, baseBean);

		/**
		 * 用户获奖经历
		 */
		if (!showwinnerinfoflag)
			HttpCall(false, getwinnerPro, baseBean);
		/**
		 * 获取背景
		 */
		HttpCall(true, getBackgroundProcessor, baseBean, "str");

	}

	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public void OnReturn(String content, IBaseProcess processor) {
		// TODO Auto-generated method stub
		super.OnReturn(content, processor);
		ResultBean result = processor.json2Bean(content);
		if (result.getCode() == Errors.OK) {
			if (processor.getProcessorId().equals(usergetPro.getProcessorId())) {
				RegistBean regist = (RegistBean) result.getObj();
				updateBaseInfo(regist);
				showbaseinfoflag = true;
			} else if (processor.getProcessorId().equals(
					getDynamicPro.getProcessorId())) {
				String num = result.getObj().toString();
				txt_dynamic.setText(num + "  动态");
			} else if (processor.getProcessorId().equals(
					countPro.getProcessorId())) {
				MyCountBean countbean = (MyCountBean) result.getObj();
				updateAttentionInfo(countbean);
			} else if (processor.getProcessorId().equals(
					getworkPro.getProcessorId())) {
				ArrayList<WorkBean> works = (ArrayList) result.getObj();
				bean.setWorks(works);
				worksinfo.updateView(bean);
				showworkinfoflag = true;
			} else if (processor.getProcessorId().equals(
					getwinnerPro.getProcessorId())) {
				ArrayList<WinnerBean> winners = (ArrayList) result.getObj();
				bean.setWinners(winners);
				winnerinfo.updateView(bean);
				showwinnerinfoflag = true;
			} else if (processor.getProcessorId().equals(
					galleryPro.getProcessorId())) {
				ArrayList<GalleryBean> gallerys = (ArrayList) result.getObj();
				bean.setGallerys(gallerys);
				selfphotoinfo.updateView(bean);
				showselfplayfoflag = true;
			} else if (processor.getProcessorId().equals(
					recruitPro.getProcessorId())) {
				ArrayList<RecruitBean> recruits = (ArrayList) result.getObj();
				bean.setRecrutbeans(recruits);
				castinginfo.updateView(bean);
				showrecruitflag = true;
			} else if (isAddPro.getProcessorId().equals(
					processor.getProcessorId())) {
				updateAttention("1");
			} else if (processor.getProcessorId().equals(
					addationprocessor.getProcessorId())) {
				isAdding = false;
				updateAttention("1");
			} else if (processor.getProcessorId().equals(
					delationPro.getProcessorId())) {
				isAdding = false;
				updateAttention("0");
			} else if (processor.getProcessorId().equals(
					getBackgroundProcessor.getProcessorId())) {
				BackgroundBean bean = (BackgroundBean) result.getObj();
				updateBackground(bean);
			}
		} else {
			if (processor.getProcessorId().equals(
					addationprocessor.getProcessorId())
					|| processor.getProcessorId().equals(
							delationPro.getProcessorId())) {
				isAdding = false;
			} else if (isAddPro.getProcessorId().equals(
					processor.getProcessorId())) {
				updateAttention("0");
			} else {
				// showToast(result.getMessage());
			}

		}
	}

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

	private void updateBaseInfo(RegistBean regist) {
		if (regist == null)
			return;
		bean = regist;
		txt_name.setText(regist.getNickname());
		String statue = regist.getPersonal_status();
		if (statue != null && statue.length() > 0) {
			txt_msgstatue.setText("状态：" + statue);
		} else
			txt_msgstatue.setText("状态： 无");
		String certifaction = regist.getCertification();
		if (certifaction.equals("1")) {
			img_regist.setVisibility(View.VISIBLE);
		} else {
			img_regist.setVisibility(View.GONE);
		}
		baseinfo.updateView(regist);
		videosinfo.updateView(regist);
		imgloader.displayImage(
				ProcessorID.uri_headphoto + regist.getHead_portrait(), img_pic,
				options);
	}

	RegistBean contentbean = new RegistBean();
	DisplayImageOptions options;

	private void initOptions() {
		options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				// 不缓存到内存
				.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	Bitmap bmp = null;

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (bmp != null) {
			bmp.recycle();
		}
		bmp = null;
	}

	private void updateAttentionInfo(MyCountBean bean) {
		txt_attention.setText(bean.getAttention() + "  关注");
		txt_fans.setText(bean.getFans() + "  粉丝");
	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub
	}

	String attentionstatue = "0";

	private void AddAttention() {
		if (baseBean == null)
			return;
		AttentionBean atten = new AttentionBean();
		atten.setFollowers_id(Session.getInstance().getUser_id());
		atten.setBy_follower_id(baseBean.getId());
		if (attentionstatue.equals("0")) {
			addAttention(atten);
		} else {
			delAttention(atten);
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.txt_dynamic) {
			Bundle bd = new Bundle();
			bd.putSerializable(R.string.MeDynamic + "", baseBean);
			doActivity(R.string.MeDynamic, bd);
		} else if (arg0.getId() == R.id.txt_attention) {
			Bundle bd = new Bundle();
			bd.putSerializable(R.string.attentionlistactivity + "", baseBean);
			doActivity(R.string.attentionlistactivity, bd);
		} else if (arg0.getId() == R.id.txt_fans) {
			Bundle bd = new Bundle();
			bd.putSerializable(R.string.myfansactivity + "", baseBean);
			doActivity(R.string.myfansactivity, bd);
		} else if (arg0.getId() == R.id.txt_letter) {
			Intent intent = new Intent(this, MainPrivateletterActivity.class);
			intent.putExtra("bean", bean);
			startActivity(intent);

		} else if (arg0.getId() == R.id.txt_statue) {
			// 添加关注
			AddAttention();
		}
	}

	boolean isAdding = false;

	public void addAttention(AttentionBean attentionbean) {
		if (isAdding) {
			showToast("您操作的太快了");
			return;
		}
		isAdding = true;
		HttpCall(false, addationprocessor, attentionbean, "str");
	}

	public void delAttention(AttentionBean attentionbean) {
		if (isAdding) {
			showToast("您操作的太快了");
			return;
		}
		isAdding = true;
		HttpCall(false, delationPro, attentionbean);
	}

	AddAttentionProcessor addationprocessor = new AddAttentionProcessor();
	DelAttentionProcessor delationPro = new DelAttentionProcessor();

	public void updateBackground(BackgroundBean bean) {
		// TODO Auto-generated method stub
		if (bean != null) {
			String url = bean.getUrl_audition();
			if (url != null && url.length() > 0) {
				imgloader.displayImage(ProcessorID.uri_headphoto + url,
						castinginfo.getBackgroundView(), options);
			}
			url = bean.getUrl_works();
			if (url != null && url.length() > 0) {
				imgloader.displayImage(ProcessorID.uri_headphoto + url,
						worksinfo.getBackgroundView(), options);
			}

			url = bean.getUrl_personshow();
			if (url != null && url.length() > 0) {
				imgloader.displayImage(ProcessorID.uri_headphoto + url,
						selfphotoinfo.getBackgroundView(), options);
			}

			url = bean.getUrl_userinfo();
			if (url != null && url.length() > 0) {
				imgloader.displayImage(ProcessorID.uri_headphoto + url,
						baseinfo.getBackgroundView(), options);
			}

			url = bean.getUrl_winners();
			if (url != null && url.length() > 0) {
				imgloader.displayImage(ProcessorID.uri_headphoto + url,
						winnerinfo.getBackgroundView(), options);
			}
			url = bean.getUrl_video();
			if (url != null && url.length() > 0) {
				imgloader.displayImage(ProcessorID.uri_headphoto + url,
						videosinfo.getBackgroundView(), options);
			}
			url = bean.getUrl_cover();
			if (url != null && url.length() > 0) {
				imgloader.displayImage(ProcessorID.uri_headphoto + url,
						backgroundlayout, options);
			}
		}
	}

	/***
	 * 计算面板的高宽
	 * 
	 * @return
	 */
	public int getWidth() {
		return Math.min(
				Util.Dp2Px(this,
						getResources().getDimension(R.dimen.me_panel_width)),
				screen_width * 3 / 5);
	}

	public int getHeight() {
		return Math.min(
				Util.Dp2Px(this,
						getResources().getDimension(R.dimen.me_panel_width)),
				screen_width * 29 / 35);
	}
}
