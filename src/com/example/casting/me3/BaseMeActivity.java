package com.example.casting.me3;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

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
import com.example.casting.processor.BaseProcessor;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.attention.AddAttentionProcessor;
import com.example.casting.processor.attention.DelAttentionProcessor;
import com.example.casting.processor.attention.GetCountProcessor;
import com.example.casting.processor.attention.IsAttentionProcessor;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.processor.dynamic.GetDynamicCountProcessor;
import com.example.casting.processor.gallery.GetBackgroundProcessor;
import com.example.casting.processor.gallery.GetListGalleryProcessor;
import com.example.casting.processor.gallery.SetBackgroundProcessor;
import com.example.casting.processor.recruit.GetRecruitListProcessor;
import com.example.casting.processor.winners.WinnerGetListProcessor;
import com.example.casting.processor.works.GetWorksProcessor;
import com.example.casting.util.Session;
import com.example.casting_android.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public abstract class BaseMeActivity extends BaseForm {

	private BaseProcessor userPro;
	private GetWorksProcessor getworkPro;
	private GetCountProcessor countPro;
	private WinnerGetListProcessor getwinnerPro;
	private GetListGalleryProcessor galleryPro;
	private GetRecruitListProcessor recruitPro;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	AddAttentionProcessor addationprocessor = new AddAttentionProcessor();
	DelAttentionProcessor delationPro = new DelAttentionProcessor();
	IsAttentionProcessor isAddPro = new IsAttentionProcessor();
	GetDynamicCountProcessor getDynamicPro = new GetDynamicCountProcessor();
	GetBackgroundProcessor getBackgroundProcessor;
	private PageBean pgbean = new PageBean();;
	public abstract BaseProcessor initUserProcessor();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		userPro = initUserProcessor();
		getworkPro = new GetWorksProcessor();
		countPro = new GetCountProcessor();
		getwinnerPro = new WinnerGetListProcessor();
		galleryPro = new GetListGalleryProcessor();
		recruitPro = new GetRecruitListProcessor();
		getBackgroundProcessor = new GetBackgroundProcessor();
		initOptions();
		//
	}

	public void IsAddAttention(AttentionBean atten) {
		HttpCall(isAddPro, atten);
	}

	boolean isAdding = false;

	public void addAttention(AttentionBean attentionbean) {
		if (isAdding) {
			showToast("您操作的太快了");
			return;
		}
		isAdding = true;
		HttpCall(true, addationprocessor, attentionbean, "str");
	}

	public void delAttention(AttentionBean attentionbean) {
		if (isAdding) {
			showToast("您操作的太快了");
			return;
		}
		isAdding = true;
		HttpCall(true, delationPro, attentionbean);
	}

	public abstract BaseBean getRequestUser();

	/**
	 * 获取我的资料
	 */
	public void getMyInfofromService() {
		BaseBean baseBean = getRequestUser();
		if (baseBean == null) {
			showToast("未获取到合法信息");
			return;
		}
		/**
		 * 获取用户基本信息
		 */
		HttpCall(false, userPro, baseBean);
		/**
		 * 获取关注数，粉丝数，
		 */
		HttpCall(false, countPro, baseBean);
		/**
		 * 获取 动态数 ，
		 */
		HttpCall(false, getDynamicPro, baseBean);
		/**
		 * 获取用户作品信息
		 */
		HttpCall(false, getworkPro, baseBean);
		/**
		 * 获取背景
		 */
		HttpCall(false, getBackgroundProcessor, baseBean, "str");
		/**
		 * 获取我展示的照片
		 */
		HttpCall(false, galleryPro, baseBean);
		/**
		 * 获取 我的试镜
		 */
		HttpCall(false, recruitPro, baseBean);
		/**
		 * 用户获奖经历
		 */
		HttpCall(getwinnerPro, baseBean);
	}

	public void setPhoto(String url, ImageView view) {
		imageLoader.displayImage(ProcessorID.uri_headphoto + url, view,options);
	}
	DisplayImageOptions options;
	private void initOptions(){
		options = new DisplayImageOptions.Builder()
		.resetViewBeforeLoading(true).cacheInMemory(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		// 不缓存到内存
		.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
		.build();
	}
	@Override
	public void OnReturn(String content, IBaseProcess processor) {
		// TODO Auto-generated method stub
		super.OnReturn(content, processor);
		ResultBean result = processor.json2Bean(content);
		if (result.getCode() == Errors.OK) {
			Message msg = new Message();
			if (processor.getProcessorId().equals(userPro.getProcessorId())) {
				msg.obj = result.getObj();
				msg.what = updateBaseinfo;
			} else if (processor.getProcessorId().equals(
					countPro.getProcessorId())) {
				msg.obj = result.getObj();
				msg.what = updateAttention;
			} else if (processor.getProcessorId().equals(
					getworkPro.getProcessorId())) {
				msg.obj = result.getObj();
				msg.what = updateWorks;

			} else if (processor.getProcessorId().equals(
					getwinnerPro.getProcessorId())) {
				msg.obj = result.getObj();
				msg.what = updateWinner;

			} else if (processor.getProcessorId().equals(
					galleryPro.getProcessorId())) {
				msg.obj = result.getObj();
				msg.what = updateGallery;
			} else if (processor.getProcessorId().equals(
					recruitPro.getProcessorId())) {
				msg.obj = result.getObj();
				msg.what = updateCasting;
			} else if (processor.getProcessorId().equals(
					addationprocessor.getProcessorId())) {
				isAdding = false;
				msg.obj = 1;
				// 添加关注
				msg.what = AddAttention;
			} else if (processor.getProcessorId().equals(
					delationPro.getProcessorId())) {
				isAdding = false;
				msg.obj = 0;
				// 添加关注
				msg.what = AddAttention;
			} else if (isAddPro.getProcessorId().equals(
					processor.getProcessorId())) {
				msg.obj = 1;
				// 已关注
				msg.what = AddAttention;
			} else if (processor.getProcessorId().equals(
					getDynamicPro.getProcessorId())) {
				if (result.getObj() != null) {
					String num = result.getObj().toString();
					msg.obj = num;
					msg.what = updateDynamic;
				}

			} else if (processor.getProcessorId().equals(
					getBackgroundProcessor.getProcessorId())) {
				msg.obj = result.getObj();
				msg.what = updateBackground;
			}
			UIhandler.sendMessage(msg);
		} else {
			if (processor.getProcessorId().equals(
					addationprocessor.getProcessorId())
					|| processor.getProcessorId().equals(
							delationPro.getProcessorId())) {
				isAdding = false;
			} else if (processor.getProcessorId().equals(
					isAddPro.getProcessorId())) {
				Message msg = new Message();
				msg.obj = 0;
				// 已关注
				msg.what = AddAttention;
				UIhandler.sendMessage(msg);
			} else if (processor.getProcessorId().equals(
					getBackgroundProcessor.getProcessorId())) {
				showToast(result.getMessage());
			}
			// showToast(result.getMessage());
		}
	}

	public PageBean getPageInfo(){
		return pgbean;
	}
	private static final int updateCount = 11110;
	private static final int updateBaseinfo = 11112;
	private static final int updateCasting = 111113;
	private static final int updateWinner = 11114;
	private static final int updateWorks = 111115;
	private static final int updateGallery = 11116;
	private static final int updateAttention = 11117;
	private static final int updateDynamic = 11119;
	private static final int AddAttention = 11118;
	private static final int updateBackground = 111120;
	Handler UIhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case updateBackground: {
				BackgroundBean backbean = (BackgroundBean) msg.obj;
				pgbean.setBagrounds(backbean);
				updateBackground(backbean);
			}
				break;
			case AddAttention: {
				BaseBean baseBean = getRequestUser();
				if (baseBean == null) {
					return;
				}
				HttpCall(countPro, baseBean);
				updateAttention(msg.obj.toString());
			}
				break;
			case updateDynamic: {
				String Dymnum = msg.obj.toString();
				pgbean.setDymnum(Dymnum);
				updateDynamicNum(Dymnum);
			}
				break;
			case updateCount: {
				MyCountBean bean = (MyCountBean) msg.obj;
				pgbean.setCountbean(bean);
				updateAttentionInfo(bean);

			}
				break;
			case updateCasting: {
				ArrayList<RecruitBean> recruits = (ArrayList<RecruitBean>) msg.obj;
				pgbean.setRecruitbean(recruits);
				updateCastinginfo(recruits);

			}
				break;
			case updateWinner: {
				ArrayList<WinnerBean> winns = (ArrayList<WinnerBean>) msg.obj;
				pgbean.setWinners(winns);
				updateWinnerinfo(winns);

			}
				break;
			case updateWorks: {
				ArrayList<WorkBean> works = (ArrayList<WorkBean>) msg.obj;
				pgbean.setWorks(works);
				updateWorksinfo(works);

			}
				break;
			case updateGallery: {
				ArrayList<GalleryBean> gallery = (ArrayList<GalleryBean>) msg.obj;
				pgbean.setGallerys(gallery);
				updateGalleryinfo(gallery);

			}
				break;
			case updateBaseinfo: {
				RegistBean regist = (RegistBean) msg.obj;
				pgbean.setBasebean(regist);
				updateBaseinfo(regist);

			}
				break;
			case updateAttention: {
				MyCountBean countbean = (MyCountBean) msg.obj;
				updateAttentionInfo(countbean);
			}
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 获取了基本信息
	 * 
	 * @param bean
	 */
	public abstract void updateBaseinfo(RegistBean bean);

	/**
	 * 获取关注，粉丝数目信息
	 * 
	 * @param bean
	 */
	public abstract void updateAttentionInfo(MyCountBean bean);

	/**
	 * 获取动态数据
	 * 
	 * @param num
	 */
	public abstract void updateDynamicNum(String num);

	/**
	 * 获取作品信息
	 * 
	 * @param works
	 */
	public abstract void updateWorksinfo(ArrayList<WorkBean> works);

	/**
	 * 获取获奖信息
	 * 
	 * @param winners
	 */
	public abstract void updateWinnerinfo(ArrayList<WinnerBean> winners);

	/**
	 * 获取个人展示
	 * 
	 * @param gallerys
	 */
	public abstract void updateGalleryinfo(ArrayList<GalleryBean> gallerys);

	/**
	 * 获取招募信息
	 * 
	 * @param recruits
	 */
	public abstract void updateCastinginfo(List<RecruitBean> recruits);

	/**
	 * 更新关注状态
	 * 
	 * @param statue
	 */
	public abstract void updateAttention(String statue);

	/**
	 * 更新背景图片
	 * 
	 * @param bean
	 */
	public abstract void updateBackground(BackgroundBean bean);

	class PageBean {
		RegistBean basebean;
		ArrayList<WorkBean> works;
		ArrayList<WinnerBean> winners;
		ArrayList<GalleryBean> gallerys;
		BackgroundBean bagrounds;
		String titleurl = "";
		String dymnum = "";
		MyCountBean countbean;
		ArrayList<RecruitBean> recruitbean;
		
		public RegistBean getBasebean() {
			return basebean;
		}

		public void setBasebean(RegistBean basebean) {
			this.basebean = basebean;
		}

		public ArrayList<WorkBean> getWorks() {
			return works;
		}

		public void setWorks(ArrayList<WorkBean> works) {
			this.works = works;
		}

		public ArrayList<WinnerBean> getWinners() {
			return winners;
		}

		public void setWinners(ArrayList<WinnerBean> winners) {
			this.winners = winners;
		}

		public ArrayList<GalleryBean> getGallerys() {
			return gallerys;
		}

		public void setGallerys(ArrayList<GalleryBean> gallerys) {
			this.gallerys = gallerys;
		}

		public BackgroundBean getBagrounds() {
			return bagrounds;
		}

		public void setBagrounds(BackgroundBean bagrounds) {
			this.bagrounds = bagrounds;
		}

		public String getTitleurl() {
			return titleurl;
		}

		public void setTitleurl(String titleurl) {
			this.titleurl = titleurl;
		}

		public String getDymnum() {
			return dymnum;
		}

		public void setDymnum(String dymnum) {
			this.dymnum = dymnum;
		}

		public MyCountBean getCountbean() {
			return countbean;
		}

		public void setCountbean(MyCountBean countbean) {
			this.countbean = countbean;
		}

		public ArrayList<RecruitBean> getRecruitbean() {
			return recruitbean;
		}

		public void setRecruitbean(ArrayList<RecruitBean> recruitbean) {
			this.recruitbean = recruitbean;
		}

		

	}
}
