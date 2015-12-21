package com.example.casting.login.regist;

import java.io.File;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.casting.entity.BackgroundBean;
import com.example.casting.entity.BaseBean;
import com.example.casting.entity.FileBean;
import com.example.casting.entity.WinnerBean;
import com.example.casting.login.BaseTakePhoneForm;
import com.example.casting.me.MeImgs;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.processor.gallery.GetBackgroundProcessor;
import com.example.casting.processor.gallery.SetBackgroundProcessor;
import com.example.casting.util.PicTool;
import com.example.casting.util.Server_path;
import com.example.casting.util.Session;
import com.example.casting_android.R;
import com.geniuseoe2012.lazyloaderdemo.cache.ImageLoader;

public class ChangeBgActivity extends BaseTakePhoneForm {
	public static final int RESULT_BACK_SET = 10000;
	View titleLayout;
	LinearLayout layout_base;
	/** 记录当前是哪个背景设置 */
	BACKGROUND_TYPE current_set;
	GetBackgroundProcessor getBackgroundProcessor;
	BackgroundBean backgroundBean;
	ImageView img_conver, img_person_work, img_screen_test, img_person_info,
			img_personal_show, img_win_experience, img_my_video;
	ImageLoader imageLoader;
	SetBackgroundProcessor setBackgroundProcessor;

	/**
	 * 
	 * @author win COVER,//封面 PERSON_WORK,//个人作品 MY_SCRREN_TEST,//我的试镜
	 *         PERSON_INFO,//个人资料 PERSONAL_SHOW,//个人展示 WIN_EXPERIENCE,//获奖经历
	 *         MY_VIDEO;//我的视频
	 */
	public enum BACKGROUND_TYPE {
		/***
		 * 封面
		 */
		COVER,
		/**
		 * 个人作品
		 */
		PERSON_WORK,
		/**
		 * 我的试镜
		 */
		MY_SCRREN_TEST,
		/** 个人资料 */
		PERSON_INFO,
		/** 个人展示 */
		PERSONAL_SHOW,
		/** 获奖经历 */
		WIN_EXPERIENCE,
		/** 我的视频 */
		MY_VIDEO;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_background_set);
		titleLayout = (View) findViewById(R.id.titlelayout);
		initView(titleLayout);
		layout_base = (LinearLayout) findViewById(R.id.layout_base);
		img_conver = (ImageView) findViewById(R.id.img_background);
		img_person_work = (ImageView) findViewById(R.id.img_personworks);
		img_screen_test = (ImageView) findViewById(R.id.img_myaudition);
		img_person_info = (ImageView) findViewById(R.id.img_personinfo);
		img_personal_show = (ImageView) findViewById(R.id.img_personshow);
		img_win_experience = (ImageView) findViewById(R.id.img_winner_experience);
		img_my_video = (ImageView) findViewById(R.id.img_myvidio);
		setButton(btn_usedefault, View.VISIBLE);
		setTitle("更换背景");
		setLeftButtonAble(true, "返回");
		getBackgroundProcessor = new GetBackgroundProcessor();
		getBackground();
		backgroundBean = new BackgroundBean();
		setBackgroundProcessor = new SetBackgroundProcessor(this);
		imageLoader = new ImageLoader(this);
		// 从服务端查询现有的各个背景图
	}

	private final int uploadover = 11110;
	Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			switch (msg.what) {
			case uploadover:{
				//结束
				showToast("上传失败");
			}break;
			case 1000:
				if (msg.obj != null) {
					showToast("设置成功");
					BackgroundBean bean = (BackgroundBean) msg.obj;
					setBackGroundByPage(bean.getImg_url());
				}
				break;
			case 1001:
				if (msg.obj != null) {
					BackgroundBean bean = (BackgroundBean) msg.obj;
					setImage(bean);
				}
				break;
			}
		}

	};

	public void getBackground() {
		BaseBean bean = new BaseBean();
		bean.setId(Session.getInstance().getUser_id());
		HttpCall(true, getBackgroundProcessor, bean, "str");
	}

	@Override
	public void LeftButtonClick() {
		finish();
	}

	@Override
	public void RightButtonClick() {
	}

	public void onClick(View arg0) {
		showSelectPicDialog();
		int source_id = arg0.getId();
		if (source_id == R.id.img_background) {
			current_set = BACKGROUND_TYPE.COVER;
			showSelectPicDialog();
		} else if (source_id == R.id.img_personworks) {
			current_set = BACKGROUND_TYPE.PERSON_WORK;
			showSelectPicDialog();
		} else if (source_id == R.id.img_myaudition) {
			current_set = BACKGROUND_TYPE.MY_SCRREN_TEST;
			showSelectPicDialog();
		} else if (source_id == R.id.img_personinfo) {
			current_set = BACKGROUND_TYPE.PERSON_INFO;
			showSelectPicDialog();
		} else if (source_id == R.id.img_personshow) {
			current_set = BACKGROUND_TYPE.PERSONAL_SHOW;
			showSelectPicDialog();
		} else if (source_id == R.id.img_winner_experience) {
			current_set = BACKGROUND_TYPE.WIN_EXPERIENCE;
			showSelectPicDialog();
		} else if (source_id == R.id.img_myvidio) {
			current_set = BACKGROUND_TYPE.MY_VIDEO;
			showSelectPicDialog();
		}
		if (arg0.getId() == R.id.btn_take) {
			// 用户点击了从照相机获取
			String status = Environment.getExternalStorageState();
			if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
				doTakePhoto();// 用户点击了从照相机获取
			} else {
				showToast("没有SD卡");
			}
			disPicDialog();
		} else if (arg0.getId() == R.id.btn_select) {
			// 从图库取
			doPickPhotoFromGallery();
			disPicDialog();
		} else if (arg0.getId() == R.id.btn_cancel) {
			disPicDialog();
		} else if (arg0.getId() == R.id.btn_usedefault_pic) {
			setDefaultBackGround();
			disPicDialog();

		}
	}

	@Override
	public View getParentView() {
		return layout_base;
	}

	@Override
	public void OnPicReturn(String filepath) {
		backgroundBean.setBackType(current_set);
		backgroundBean.setImg_url(filepath);
		setBackGround(backgroundBean);
	}

	@Override
	public void OnReturn(String content, IBaseProcess processor) {
		// TODO Auto-generated method stub
		super.OnReturn(content, processor);
		ResultBean bean = processor.json2Bean(content);
		if (bean.getCode() == Errors.OK) {
			if (processor.getMethod() == setBackgroundProcessor.getMethod()) {
				Message msg = handler.obtainMessage();
				msg.what = 1000;
				msg.obj = bean.getObj();
				handler.sendMessage(msg);
			} else if (processor.getMethod() == getBackgroundProcessor
					.getMethod()) {
				Message msg = handler.obtainMessage();
				msg.what = 1001;
				msg.obj = bean.getObj();
				handler.sendMessage(msg);
			}

		} else {
			showToast(bean.getMessage());
		}
	}

	/***
	 * 从提交图片界面返回
	 */
	public void setBackGroundByPage(String url) {
		if (url != null && !url.equals("")) {
			String urlStr = ProcessorID.uri_headphoto + url;
			if (current_set == BACKGROUND_TYPE.COVER) {
				imageLoader.DisplayImage(urlStr, img_conver, false);
			} else if (current_set == BACKGROUND_TYPE.MY_SCRREN_TEST) {
				imageLoader.DisplayImage(urlStr, img_screen_test, false);
			} else if (current_set == BACKGROUND_TYPE.MY_VIDEO) {
				imageLoader.DisplayImage(urlStr, img_my_video, false);
			} else if (current_set == BACKGROUND_TYPE.PERSON_INFO) {
				imageLoader.DisplayImage(urlStr, img_person_info, false);
			} else if (current_set == BACKGROUND_TYPE.PERSON_WORK) {
				imageLoader.DisplayImage(urlStr, img_person_work, false);
			} else if (current_set == BACKGROUND_TYPE.PERSONAL_SHOW) {
				imageLoader.DisplayImage(urlStr, img_personal_show, false);
			} else if (current_set == BACKGROUND_TYPE.WIN_EXPERIENCE) {
				imageLoader.DisplayImage(urlStr, img_win_experience, false);
			}
		} else {
			if (current_set == BACKGROUND_TYPE.COVER) {
				img_conver.setImageBitmap(PicTool.ReadBitmapById(this,
						R.drawable.me_bgallb));
			} else if (current_set == BACKGROUND_TYPE.MY_SCRREN_TEST) {
				img_my_video.setImageBitmap(PicTool.ReadBitmapById(this,
						R.drawable.me_bg02b));
			} else if (current_set == BACKGROUND_TYPE.MY_VIDEO) {
				img_my_video.setImageBitmap(PicTool.ReadBitmapById(this,
						R.drawable.me_bg06b));
			} else if (current_set == BACKGROUND_TYPE.PERSON_INFO) {
				img_person_info.setImageBitmap(PicTool.ReadBitmapById(this,
						R.drawable.me_bg03b));
			} else if (current_set == BACKGROUND_TYPE.PERSON_WORK) {
				img_person_work.setImageBitmap(PicTool.ReadBitmapById(this,
						R.drawable.me_bg01b));
			} else if (current_set == BACKGROUND_TYPE.PERSONAL_SHOW) {
				img_personal_show.setImageBitmap(PicTool.ReadBitmapById(this,
						R.drawable.me_bg04b));
			} else if (current_set == BACKGROUND_TYPE.WIN_EXPERIENCE) {
				img_win_experience.setImageBitmap(PicTool.ReadBitmapById(this,
						R.drawable.me_bg05b));
			}
		}
	}

	/**
	 * 设置默认背景
	 */
	public void setDefaultBackGround() {
		BackgroundBean bean = new BackgroundBean();
		bean.setBackType(current_set);
		bean.setImg_url("");
		setBackGround(bean);
	}

	/**
	 * 网络提交请求
	 */
	public void setBackGround(BackgroundBean bean) {
		// if(!bean.getImg_url().equals("") || bean.getImg_resource()!=0)
		startUploadImg(bean);
		// HttpCall(true,setBackgroundProcessor,bean,"str");

	}

	/** 从服务端查询到背景地址后 ，更新所有背景 */
	public void setImage(BackgroundBean bean) {
		if (bean != null) {
			if (!bean.getUrl_cover().equals(""))
				imageLoader.DisplayImage(
						ProcessorID.uri_headphoto + bean.getUrl_cover(),
						img_conver, false);
			if (!bean.getUrl_audition().equals(""))
				imageLoader.DisplayImage(
						ProcessorID.uri_headphoto + bean.getUrl_audition(),
						img_screen_test, false);
			if (!bean.getUrl_video().equals(""))
				imageLoader.DisplayImage(
						ProcessorID.uri_headphoto + bean.getUrl_video(),
						img_my_video, false);
			if (!bean.getUrl_userinfo().equals(""))
				imageLoader.DisplayImage(
						ProcessorID.uri_headphoto + bean.getUrl_userinfo(),
						img_person_info, false);
			if (!bean.getUrl_works().equals(""))
				imageLoader.DisplayImage(
						ProcessorID.uri_headphoto + bean.getUrl_works(),
						img_person_work, false);
			if (!bean.getUrl_personshow().equals(""))
				imageLoader.DisplayImage(
						ProcessorID.uri_headphoto + bean.getUrl_personshow(),
						img_personal_show, false);
			if (!bean.getUrl_winners().equals(""))
				imageLoader.DisplayImage(
						ProcessorID.uri_headphoto + bean.getUrl_winners(),
						img_win_experience, false);
		}

	}

	class UploadTool extends AsyncTask<String, Integer, String> {
		BackgroundBean uploadbean;

		public UploadTool(BackgroundBean bean) {
			uploadbean = bean;
		}

		protected String doInBackground(String... params) {
			try {
				String url = uploadbean.getImg_url();
				if(url.length()==0)return "";//设置默认值
				File outputFile = new File(Server_path.SavePath
						+ url.substring(url.lastIndexOf("/") + 1));
				String path = PicTool.compressImage(ChangeBgActivity.this, url,
						Server_path.SavePath, outputFile);
				return encodeBase64File(path);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
		}

		protected void onPostExecute(String result) {
			if (result != null ) {
				uploadbean.setImg_data(result);
				HttpCall(true, setBackgroundProcessor, uploadbean, "str");
				return;

			}
			handler.sendEmptyMessage(uploadover);
		}
	}


	private void startUploadImg(BackgroundBean bean) {
		UploadTool tool = new UploadTool(bean);
		tool.execute();
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
