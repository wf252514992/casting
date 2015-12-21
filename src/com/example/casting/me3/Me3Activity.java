package com.example.casting.me3;

import java.util.ArrayList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.casting.entity.BackgroundBean;
import com.example.casting.entity.GalleryBean;
import com.example.casting.entity.MyCountBean;
import com.example.casting.entity.RecruitBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.entity.WinnerBean;
import com.example.casting.entity.WorkBean;
import com.example.casting.login.BaseForm;
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
import com.example.casting.processor.attention.GetCountProcessor;
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
import com.example.casting_android.bean.ImageBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class Me3Activity extends BaseForm implements OnClickListener {
	View titleLayout;

	ImageView backgroundlayout;
	LinearLayout layout_content ;
	TextView txt_dynamic;
	TextView txt_attention;
	TextView txt_fans;
	TextView txt_edit;
	TextView txt_name;
	TextView txt_statue;
	CircleImageView img_pic;
	ImageView img_regist;
	UserGetProcessor usergetPro;
	GetWorksProcessor getworkPro;
	GetCountProcessor countPro;
	GetDynamicCountProcessor getDynamicPro;
	GetBackgroundProcessor getBackgroundProcessor;
	WinnerGetListProcessor getwinnerPro;
	GetListGalleryProcessor galleryPro;
	GetRecruitListProcessor recruitPro;
	Me_BaseInfoView baseinfo;
	Me_WorksView worksinfo;
	Me_CastingView castinginfo;
	Me_WinnersView winnerinfo;
	Me_SelfPlayView selfphotoinfo;
	Me_VideosView videosinfo;
	ImageLoader imageloader = ImageLoader.getInstance();
	private int subview_width = 0;
	private int subview_height = 0;

	RegistBean contentbean = new RegistBean();
	DisplayImageOptions options;

	private void initOptions() {
		options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				// 不缓存到内存
				.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me3);
		usergetPro = new UserGetProcessor();
		getworkPro = new GetWorksProcessor();
		countPro = new GetCountProcessor();
		getDynamicPro = new GetDynamicCountProcessor();
		getwinnerPro = new WinnerGetListProcessor();
		galleryPro = new GetListGalleryProcessor();
		recruitPro = new GetRecruitListProcessor();
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
		txt_edit = (TextView) findViewById(R.id.txt_edit);
		txt_fans = (TextView) findViewById(R.id.txt_fans);
		txt_name = (TextView) findViewById(R.id.txt_name);
		txt_statue = (TextView) findViewById(R.id.txt_statue);
		txt_dynamic.setOnClickListener(this);
		txt_attention.setOnClickListener(this);
		txt_edit.setOnClickListener(this);
		txt_fans.setOnClickListener(this);
		initView(titleLayout);
		setRightButtonAble(true, "设置");
		setLeftButtonAble(false, "");
		setTitle("我");
		initView();
	}

	public void initView() {
		Bitmap bmp = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_launcher);
		img_pic.setImageDrawable(new CircleImageDrawable(bmp));
		baseinfo = new Me_BaseInfoView(this);
		worksinfo = new Me_WorksView(this, true);
		castinginfo = new Me_CastingView(this);
		winnerinfo = new Me_WinnersView(this, true);
		selfphotoinfo = new Me_SelfPlayView(this);
		videosinfo = new Me_VideosView(this);
		// subview_width = (int) (screen_width * 3 / 5);
		// subview_height = (int) (screen_width * 6 / 7);
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getMyInfofromService();
	}

	private RegistBean getBean() {
		RegistBean registbean = new RegistBean();
		registbean.setId(Session.getInstance().getUser_id());
		return registbean;
	}

	/**
	 * 获取我的资料
	 */
	private void getMyInfofromService() {
		// 获取关注fans数
		RegistBean registbean = getBean();
		/**
		 * 获取用户基本信息
		 */
		HttpCall(false, usergetPro, registbean);
		/**
		 * 获取 粉丝数，
		 */
		HttpCall(false, countPro, registbean);
		/**
		 * 获取 动态数 ，
		 */
		HttpCall(false, getDynamicPro, registbean);
		/**
		 * 获取用户作品信息
		 */
		HttpCall(false, getworkPro, registbean);
		/**
		 * 获取我展示的照片
		 */
		HttpCall(false, galleryPro, registbean);
		/**
		 * 获取 我的试镜
		 */
		HttpCall(false, recruitPro, registbean);
		/**
		 * 用户获奖经历
		 */
		HttpCall(false, getwinnerPro, registbean);

		// /**
		// * 获取视频
		// */
		// HttpCall(false,getVideoProcessor,videoBean);
		/**
		 * 获取背景
		 */
		HttpCall(true, getBackgroundProcessor, registbean, "str");

	}

	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub

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
			} else if (processor.getProcessorId().equals(
					countPro.getProcessorId())) {
				MyCountBean countbean = (MyCountBean) result.getObj();
				txt_attention.setText(countbean.getAttention() + "  关注");
				txt_fans.setText(countbean.getFans() + "  粉丝");
			} else if (processor.getProcessorId().equals(
					getDynamicPro.getProcessorId())) {
				String num = result.getObj().toString();
				txt_dynamic.setText(num + "  动态");
			} else if (processor.getProcessorId().equals(
					getworkPro.getProcessorId())) {
				ArrayList<WorkBean> works = (ArrayList) result.getObj();
				if (content != null) {
					contentbean.setWorks(works);
				}
				worksinfo.updateView(contentbean);
			} else if (processor.getProcessorId().equals(
					getwinnerPro.getProcessorId())) {
				ArrayList<WinnerBean> winners = (ArrayList) result.getObj();
				if (content != null) {
					contentbean.setWinners(winners);
				}
				winnerinfo.updateView(contentbean);
			} else if (processor.getProcessorId().equals(
					galleryPro.getProcessorId())) {
				ArrayList<GalleryBean> gallerys = (ArrayList) result.getObj();
				if (contentbean != null) {
					contentbean.setGallerys(gallerys);
					selfphotoinfo.updateView(contentbean);
				}

			} else if (processor.getProcessorId().equals(
					recruitPro.getProcessorId())) {
				ArrayList<RecruitBean> recruits = (ArrayList) result.getObj();
				if (contentbean != null) {
					contentbean.setRecrutbeans(recruits);
					castinginfo.updateView(contentbean);
				}
			} else if (processor.getProcessorId().equals(
					getBackgroundProcessor.getProcessorId())) {
				BackgroundBean bean = (BackgroundBean) result.getObj();
				updateBackground(bean);
			}
		} else {
			if (processor.getProcessorId().equals(
					getBackgroundProcessor.getProcessorId())) {
				// showToast(result.getMessage());
			}
			// showToast(result.getMessage());
		}
	}

	public void updateBackground(BackgroundBean bean) {
		// TODO Auto-generated method stub
		if (bean != null) {
			String url =  bean.getUrl_works(); 
			if(url!=null && url.length()>0){
				imageloader.displayImage(
						ProcessorID.uri_headphoto +url,
						worksinfo.getBackgroundView(), options);
			}
			else {
				imageloader.displayImage(
						"drawable://"+R.drawable.me_bg01b,
						worksinfo.getBackgroundView(), options);
			}
			url =  bean.getUrl_cover(); 
			if(url!=null && url.length()>0){
				imageloader.displayImage(
						ProcessorID.uri_headphoto +url,
						backgroundlayout, options);
			}
			else
			{
				imageloader.displayImage(
						"drawable://"+R.drawable.me_bgallb,
						backgroundlayout, options);
			}
			url =  bean.getUrl_audition(); 
			if(url!=null && url.length()>0){
				imageloader.displayImage(
						ProcessorID.uri_headphoto +url,
						castinginfo.getBackgroundView(), options);
			}
			else {
				imageloader.displayImage(
						"drawable://"+R.drawable.me_bg02b,
						castinginfo.getBackgroundView(), options);
			}
			url =  bean.getUrl_userinfo(); 
			if(url!=null && url.length()>0){
				imageloader.displayImage(
						ProcessorID.uri_headphoto +url,
						baseinfo.getBackgroundView(), options);
			}
			else {
				imageloader.displayImage(
						"drawable://"+R.drawable.me_bg03b,
						baseinfo.getBackgroundView(), options);
			}
			url =  bean.getUrl_personshow(); 
			if(url!=null && url.length()>0){
				imageloader.displayImage(
						ProcessorID.uri_headphoto +url,
						selfphotoinfo.getBackgroundView(), options);
			}
			else {
				imageloader.displayImage(
						"drawable://"+R.drawable.me_bg04b,
						selfphotoinfo.getBackgroundView(), options);
			}
			
			
			url =  bean.getUrl_winners(); 
			if(url!=null && url.length()>0){
				imageloader.displayImage(
						ProcessorID.uri_headphoto +url,
						winnerinfo.getBackgroundView(), options);
			}
			else {
				imageloader.displayImage(
						"drawable://"+R.drawable.me_bg05b,
						winnerinfo.getBackgroundView(), options);
			}
			url =  bean.getUrl_video(); 
			if(url!=null && url.length()>0){
				imageloader.displayImage(
						ProcessorID.uri_headphoto +url,
						videosinfo.getBackgroundView(), options);
			}
			else {
				imageloader.displayImage(
						"drawable://"+R.drawable.me_bg06b,
						videosinfo.getBackgroundView(), options);
			}
		}
	}

	private void updateBaseInfo(RegistBean regist) {
		if (regist == null)
			return;
		contentbean = regist;
		txt_name.setText(regist.getNickname());
		txt_statue.setText("状态：" + regist.getPersonal_status());
		baseinfo.updateView(regist);
		videosinfo.updateView(contentbean);
		String certifaction = regist.getCertification();
		if (certifaction.equals("1")) {
			img_regist.setVisibility(View.VISIBLE);
		} else {
			img_regist.setVisibility(View.GONE);
		}
		// 更新头像
		UpdateTitlePhoto(regist.getUserimp());
	}

	Bitmap bmp = null;

	private void UpdateTitlePhoto(ImageBean img) {
		if (img != null) {
			if (img.getRestype() == ImageBean.type_url) {
				imageloader.displayImage(
						ProcessorID.uri_headphoto + img.getImg_res(), img_pic,
						options);
			} else if (img.getRestype() == ImageBean.type_filepath) {
				bmp = BitmapFactory.decodeFile(img.getImg_res());
				img_pic.setImageDrawable(new CircleImageDrawable(bmp));
			} else {
				bmp = BitmapFactory.decodeResource(getResources(),
						R.drawable.ic_launcher);
				img_pic.setImageDrawable(new CircleImageDrawable(bmp));
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (bmp != null) {
			bmp.recycle();
		}
		bmp = null;
	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub
		doActivity(R.string.xtszactivity);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.img_pic) {
			getMyInfofromService();
		} else if (arg0.getId() == R.id.txt_dynamic) {
			Bundle bd = new Bundle();
			bd.putSerializable(R.string.MeDynamic + "", getBean());
			doActivity(R.string.MeDynamic, bd);
		} else if (arg0.getId() == R.id.txt_attention) {
			Bundle bd = new Bundle();
			bd.putSerializable(R.string.attentionlistactivity + "", getBean());
			doActivity(R.string.attentionlistactivity, bd);
		} else if (arg0.getId() == R.id.txt_fans) {
			Bundle bd = new Bundle();
			bd.putSerializable(R.string.myfansactivity + "", getBean());
			doActivity(R.string.myfansactivity, bd);
		} else if (arg0.getId() == R.id.txt_edit) {
			Bundle bd = new Bundle();
			bd.putSerializable(R.string.userinfoeditactivity + "", contentbean);
			doActivity(R.string.userinfoeditactivity, bd);
			// }else if(arg0.getId() == R.id.txt_b){
			// doActivity(R.string.userinfoshowactivity);
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
