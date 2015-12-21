package com.example.casting.me;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.casting.entity.FileBean;
import com.example.casting.entity.GalleryBean;
import com.example.casting.entity.GetPHotoBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.login.BaseTakePhoneForm;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.processor.file.UploadProcessor;
import com.example.casting.processor.gallery.AddImgProcessor;
import com.example.casting.processor.gallery.DeleteImgProcessor;
import com.example.casting.processor.gallery.GetListGalleryProcessor;
import com.example.casting.publisheddynamic.ImgCallBack;
import com.example.casting.publisheddynamic.Util;
import com.example.casting.util.ConstantData;
import com.example.casting.util.PicTool;
import com.example.casting.util.Server_path;
import com.example.casting.util.Session;
import com.example.casting.util.view.MyGridViewAdapter;
import com.example.casting.util.view.refresh.PullToRefreshBase;
import com.example.casting.util.view.refresh.PullToRefreshBase.OnRefreshListener;
import com.example.casting.util.view.refresh.PullToRefreshGridView;
import com.example.casting_android.R;
import com.example.casting_android.bean.ImageBean;
import com.imgselect.MainActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

public class MeImgs extends BaseTakePhoneForm implements OnItemClickListener,
		OnItemLongClickListener, OnClickListener {

	PullToRefreshGridView imgGridView;
	MyGridViewAdapter imgsAdapter;
	GridView gridView;
	Util util;
	RegistBean registbean;
	private ArrayList<String> filelist;
	private List<GalleryBean> data = new ArrayList<GalleryBean>();
	private View titleLayout;
	// private RelativeLayout layout_bottom, layout_base, layout_shade;
	// private Button btn_takephoto, btn_selectImg, btn_cancle;
	private GetListGalleryProcessor galleryPro;
	private DeleteImgProcessor deleteImgPro;
	private int currentItem;
	AddImgProcessor addImgProcessor;
	UploadProcessor uploadProcessor;
	ArrayList<String> nativeUrls = new ArrayList<String>();
	int uploadCount = 0;
	ArrayList<String> networkPicUrls = new ArrayList<String>();
	GalleryBean galleryBean;
	RelativeLayout layout_base;
	boolean isRefresh = false;
	boolean isLoadMore = false;
	int currenPage = 1;
	int sum = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();

		setLeftButtonAble(true, "返回");
		setTitle("个人展示");
		registbean = initData(getIntent());
		if (registbean != null
				&& registbean.getId()
						.equals(Session.getInstance().getUser_id())) {
			// 查看别人的个人展示是 屏蔽掉添加按钮
			setRightButtonAble(true, "添加");

		}
		if (registbean != null
				&& registbean.getType().equals(ConstantData.Login_Type_Company)) {
			setTitle("公司展示");
		}
		initBeanAndProcessor();

		initRefreshListener();
		// HttpCall(true,galleryPro, getBean());
	}

	public void initView() {
		View view = View.inflate(this, R.layout.photogrally, null);
		imgGridView = (PullToRefreshGridView) view.findViewById(R.id.gridView1);
		imgGridView.setScrollLoadEnabled(true);
		layout_base = (RelativeLayout) view.findViewById(R.id.layout_base);
		layout_base.setFocusable(true);
		layout_base.setClickable(true);

		layout_base.setOnClickListener(this);
		gridView = imgGridView.getRefreshableView();
		// RelativeLayout relativeLayout2 = (RelativeLayout) view
		// .findViewById(R.id.relativeLayout2);
		// relativeLayout2.setVisibility(View.GONE);
		// layout_base = (RelativeLayout) view.findViewById(R.id.layout_base);
		setContentView(view);
		titleLayout = findViewById(R.id.titlelayout);
		initView(titleLayout);

		filelist = new ArrayList<String>();
		imgsAdapter = new MyGridViewAdapter(MeImgs.this, data);
		imgsAdapter.setState_delete(false);
		gridView.setAdapter(imgsAdapter);
		gridView.setOnItemClickListener(this);
		gridView.setOnItemLongClickListener(this);
	}

	private RegistBean initData(Intent intent) {
		if (intent != null) {
			Bundle bd = intent.getExtras();
			if (bd != null && bd.getSerializable(R.string.meimgs + "") != null) {
				RegistBean bean = (RegistBean) bd
						.getSerializable(R.string.meimgs + "");
				return bean;
			}
		}
		return null;
	}

	public void initBeanAndProcessor() {
		galleryPro = new GetListGalleryProcessor();
		deleteImgPro = new DeleteImgProcessor();
		PauseOnScrollListener listener = new PauseOnScrollListener(
				ImageLoader.getInstance(), false, true);
		imgGridView.setOnScrollListener(listener);
		addImgProcessor = new AddImgProcessor();
		uploadProcessor = new UploadProcessor();
	}

	public void initRefreshListener() {
		imgGridView.setOnRefreshListener(new OnRefreshListener<GridView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				isRefresh = true;
				currenPage = 1;
				requestData(false);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				System.out.println("---上拉加载更多------");
				isLoadMore = true;
				requestData(true);
			}

		});
		imgGridView.setLastUpdatedLabel(com.example.casting.util.view.Util
				.getCurrentTime());
		imgGridView.doPullRefreshing(true, 500);
	}

	public void requestData(boolean isLoadMore) {
		HttpCall(false, galleryPro, getBean(isLoadMore));
	}

	@Override
	public void OnReturn(String content, IBaseProcess processor) {
		super.OnReturn(content, processor);
		ResultBean bean = processor.json2Bean(content);
		System.out.println("msg:" + bean.getMessage() + "   processor:"
				+ processor.getMethod());
		if (bean.getCode() == Errors.OK) {
			if (processor.getProcessorId().equals(galleryPro.getProcessorId())) {
				System.out.println("---galleryPro----");
				List<GalleryBean> gallerys = (List) bean.getObj();
				if (gallerys != null && gallerys.size() > 0) {
					if (isLoadMore)
						currenPage++;
					else {
						data.clear();
					}
					data.addAll(gallerys);
					imgsAdapter.notifyDataSetChanged();
				} else {
					if (isLoadMore) {
						showToast(getString(R.string.no_data));
					} else {
						showToast(getString(R.string.nodata));
					}
				}
				reFreshOrLoadComplete();
			} else if (processor.getProcessorId().equals(
					deleteImgPro.getProcessorId())) {
				System.out.println("---deleteImgPro----");
				imgsAdapter.removeItem(currentItem);
			} else if (processor.getProcessorId().equals(
					addImgProcessor.getProcessorId())) {
				System.out.println("---sendimglist over----");
				HttpCall(true, galleryPro, getBean(false));
			}

		} else {
			if (processor.getProcessorId().equals(
					addImgProcessor.getProcessorId())) {
				HttpCall(true, galleryPro, getBean(false));
			} else if (processor.getProcessorId().equals(
					galleryPro.getProcessorId())) {
				reFreshOrLoadComplete();
			}
			showToast(bean.getMessage());
		}
	}

	private GetPHotoBean getBean(Boolean isLoadMore) {
		if (registbean == null) {
			return null;
		}
		GetPHotoBean bean = new GetPHotoBean();
		bean.setId(registbean.getId());
		if (isLoadMore) {
			bean.setPagenum(currenPage + 1 + "");
		} else {
			bean.setPagenum(currenPage + "");
		}
		return bean;
	}

	@SuppressLint("NewApi")
	public ImageView iconImage(String filepath, int index, CheckBox checkBox)
			throws FileNotFoundException {
		LinearLayout.LayoutParams params = new LayoutParams(150, 150);
		ImageView imageView = new ImageView(this);
		imageView.setLayoutParams(params);
		imageView.setBackgroundResource(R.drawable.imgbg);
		float alpha = 100;
		imageView.setAlpha(alpha);
		util.imgExcute(imageView, imgCallBack, filepath);
		imageView.setOnClickListener(new ImgOnclick(filepath, checkBox));
		return imageView;
	}

	ImgCallBack imgCallBack = new ImgCallBack() {
		@Override
		public void resultImgCall(ImageView imageView, Bitmap bitmap) {
			imageView.setImageBitmap(bitmap);
		}
	};

	class ImgOnclick implements OnClickListener {
		String filepath;
		CheckBox checkBox;

		public ImgOnclick(String filepath, CheckBox checkBox) {
			this.filepath = filepath;
			this.checkBox = checkBox;
		}

		@Override
		public void onClick(View arg0) {
			checkBox.setChecked(false);
			filelist.remove(filepath);
		}
	}

	@Override
	public void LeftButtonClick() {
		finish();
	}

	@Override
	public void RightButtonClick() {
		// 添加按钮事件
		showSelectPicDialog();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		if (registbean != null
				&& registbean.getId()
						.equals(Session.getInstance().getUser_id())) {
			imgsAdapter.setState_delete(true);
			imgsAdapter.notifyDataSetChanged();
		}
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		currentItem = position;
		GalleryBean bean = (GalleryBean) imgsAdapter.getItem(position);
		// if(adapter.isItemBlank(position))
		// {
		// resetImgs();
		// }
		if (imgsAdapter.isState_delete()) {
			if (bean.getGallery_id() != null
					&& !bean.getGallery_id().equals(""))
				HttpCall(true, deleteImgPro,
						(GalleryBean) imgsAdapter.getItem(position), "str");
			else {
				showToast("相片id不能为空");
			}
		} else {
			// 查看图片详情
			Intent intent = new Intent();
			// intent.setClass(this, ShowBigPhotoActivity.class);
			// intent.putExtra(ShowBigPhotoActivity.URL, bean.getUrl());
			// startActivity(intent);
			intent.setClass(this, ShowGalleryActivity.class);
			intent.putExtra(ShowGalleryActivity.TAG, gallery2img(data));
			intent.putExtra(ShowGalleryActivity.POSTION, position + "");
			startActivity(intent);
		}

	}

	private ArrayList<ImageBean> gallery2img(List<GalleryBean> gallerys) {
		ArrayList<ImageBean> imags = new ArrayList<ImageBean>();
		int count = 0;
		for (GalleryBean bean : gallerys) {
			ImageBean img = new ImageBean(ImageBean.type_url, bean.getUrl());
			imags.add(img);
			count++;
		}
		return imags;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			if (data.getStringArrayListExtra(MainActivity.TAG) != null) {
				nativeUrls.clear();
				nativeUrls = data.getStringArrayListExtra(MainActivity.TAG);
				disPicDialog();
				if (nativeUrls != null && nativeUrls.size() > 0) {
					Message msg = new Message();
					msg.what = dealPiclist;
					msg.obj = nativeUrls;
					uploadHandler.sendMessage(msg);
				}
			}
		}
		// else if(requestCode == TAKE_PHOTO)
		// {
		// disPicDialog();
		// String sdStatus = Environment.getExternalStorageState();
		// if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
		// return;
		// }
		// File file = new File(takePhoto_url);
		// if(file.exists())
		// {
		// nativeUrls.clear();
		// nativeUrls.add(takePhoto_url);
		// disPicDialog();
		// UploadPic uploadPic = new UploadPic(nativeUrls);
		// networkPicUrls = new ArrayList<String>();
		// new Thread(uploadPic).start();
		// showWaitDialog();
		// }
		// }
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			resetImgs();
		}
		return super.onKeyDown(keyCode, event);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// HttpCall(true,galleryPro, getBean());
	}

	@Override
	public View getParentView() {
		return imgGridView;
	}

	/**
	 * 调用系统相机拍照返回
	 */
	@Override
	public void OnPicReturn(String filepath) {
		nativeUrls.clear();
		nativeUrls.add(filepath);
		disPicDialog();
		if (nativeUrls != null && nativeUrls.size() > 0) {
			Message msg = new Message();
			msg.what = dealPiclist;
			msg.obj = nativeUrls;
			uploadHandler.sendMessage(msg);
		}
	}

	// public class UploadPic implements Runnable
	// {
	// List<String> nativPicUrls = new ArrayList<String>();
	// public UploadPic(List<String> urls)
	// {
	// nativPicUrls = urls;
	// }
	// @Override
	// public void run() {
	// try
	// {
	// Looper.prepare();
	// for(int i=0;i<nativPicUrls.size();i++)
	// {
	// System.out.println("--------"+i+"-------");
	// savePic(nativPicUrls.get(i));
	// }
	// Looper.loop();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// finally{
	// dismissDialog();
	// }
	// }
	//
	// }
	/**
	 * 请求网络上传图片
	 * 
	 * @param filepath
	 *            文件路径
	 * @return
	 */
	// private boolean savePic(String filepath) {
	// sum++;
	// System.out.println("up pic ----filepath"+filepath+sum);
	// if (filepath != null) {
	// File file = new File(filepath);
	// if (file.exists()) {
	// System.out.println("up pic ----filepath----exits"+filepath+sum);
	// FileBean fileb = new FileBean();
	// fileb.setDatatype(FileBean.DT_FILEPATH);
	// fileb.setEndtag(filepath.substring(filepath.indexOf(".") + 1));
	// fileb.setFieldata(filepath);
	// HttpPost(true,uploadProcessor, fileb);
	// return true;
	// }
	// }
	// return false;
	// }

	public void senPicToGallery(ArrayList<String> pics) {
		// 发送图片路径到服务端
		if (galleryBean == null)
			galleryBean = new GalleryBean();
		if (pics != null && pics.size() > 0) {
			galleryBean.setDescription("个人展示新照片");
			galleryBean.setUrls(pics);
			galleryBean.setPagenum("1");
			HttpCall(addImgProcessor, galleryBean, "str");
		} else
			Toast.makeText(this, "图片不能为空", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btn_select) {
			disPicDialog();
			Intent intent = new Intent();
			intent.setClass(MeImgs.this, MainActivity.class);
			intent.putExtra(MainActivity.IMG_COUNT, 8);
			startActivityForResult(intent, 0);
		} else if (v.getId() == R.id.btn_take) {
			disPicDialog();
			// 用户点击了从照相机获取
			String status = Environment.getExternalStorageState();
			if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
				doTakePhoto();// 用户点击了从照相机获取
			} else {
				showToast("没有SD卡");
			}
		} else if (v.getId() == R.id.layout_base) {
			resetImgs();
		}

	}

	public class UploadFileTask implements Runnable {
		public String fileUrl;

		public UploadFileTask(String file_url) {
			this.fileUrl = file_url;
		}

		public String getFileUrl() {
			return fileUrl;
		}

		@Override
		public void run() {
			if (fileUrl != null && !fileUrl.equals("")) {
				File file = new File(fileUrl);
				if (file.exists()) {
					FileBean fileb = new FileBean();
					fileb.setDatatype(FileBean.DT_FILEPATH);
					fileb.setEndtag(fileUrl.substring(fileUrl.indexOf(".") + 1));
					fileb.setFieldata(fileUrl);
					HttpPost(false, uploadProcessor, fileb);
				}
			}
		}
	}

	/**
	 * 开始图片上传
	 */
	public void startUpload() {
		disPicDialog();
		showWaitDialog(false);
		networkPicUrls.clear();
		uploadCount = -1;
		uploadHandler.sendEmptyMessage(uploadNext);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		imgsAdapter.removeAll();
	}

	public void reFreshOrLoadComplete() {
		if (isLoadMore) {
			imgGridView.onPullUpRefreshComplete();
			imgGridView.setHasMoreData(true);
		} else if (isRefresh) {
			imgGridView.onPullDownRefreshComplete();
			imgGridView.setLastUpdatedLabel(com.example.casting.util.view.Util
					.getCurrentTime());
		}
		isLoadMore = false;
		isRefresh = false;
	}

	private final int uploadPic = 11110;
	private final int dealPiclist = 11111;
	private final int uploadNext = 11112;
	Handler uploadHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case dealPiclist:
				// 开始处理图片
				startUpload();
				break;
			case uploadPic:
				// 图片上传
				String fileUrl = msg.obj.toString();
				if (fileUrl != null && !fileUrl.equals("")) {
					File file = new File(fileUrl);
					if (file.exists()) {
						FileBean fileb = new FileBean();
						fileb.setDatatype(FileBean.DT_FILEPATH);
						fileb.setEndtag(fileUrl.substring(fileUrl.indexOf(".") + 1));
						fileb.setFieldata(fileUrl);
						HttpPost(false, uploadProcessor, fileb);
						return;
					}
				}
				uploadHandler.sendEmptyMessage(uploadNext);
				break;
			case uploadNext: {
				uploadCount++;
				startUploadImg();
			}
				break;
			default:
				break;
			}
		}

	};

	private void startUploadImg() {
		if (nativeUrls != null && nativeUrls.size() > uploadCount) {
			/**
			 * 上传
			 */
			String path = nativeUrls.get(uploadCount);
			UploadFile(path);
		} else {
			/**
			 * 如果上传完成 进行界面刷新
			 */
			if (uploadCount == nativeUrls.size() && networkPicUrls.size() > 0) {
				senPicToGallery(networkPicUrls);
			}
			stopUpload();
		}
	}

	/**
	 * 停止下载
	 */
	private void stopUpload() {
		uploadCount = 0;
		nativeUrls.clear();
		networkPicUrls.clear();
		showToast("上传完成");
		dismissDialog();
	}

	class UploadTool extends AsyncTask<String, Integer, String> {
		String url;

		public UploadTool(String list) {
			url = list;
		}

		protected String doInBackground(String... params) {
			try {
				File outputFile = new File(Server_path.SavePath
						+ url.substring(url.lastIndexOf("/") + 1));
				String path = PicTool.compressImage(MeImgs.this, url,
						Server_path.SavePath, outputFile);
				return encodeBase64File(path);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "";
		}

		@Override
		protected void onPreExecute() {
		}

		protected void onPostExecute(String result) {
			if (result != null && result.length() > 0) {
				FileBean fileb = new FileBean();
				fileb.setDatatype(FileBean.DT_BYTE);
				fileb.setEndtag(url.substring(url.indexOf(".") + 1));
				fileb.setFieldata(result);
				HttpPost(false, uploadProcessor, fileb);
				return;

			}
			uploadHandler.sendEmptyMessage(uploadNext);
		}
	}

	/** 重置Item 消除删除按钮 */
	public void resetImgs() {
		if (imgsAdapter.isState_delete()) {
			// 如果照片处于可删除状态，返回将取消可删除状态
			imgsAdapter.setState_delete(false);
			imgsAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void uploadFiald(String msg) {
		// TODO Auto-generated method stub
		overUpload();
	}

	private void overUpload() {
		// tool= null;
		uploadHandler.sendEmptyMessage(uploadNext);

	}

	@Override
	public void uploadSuccess(String url) {
		// TODO Auto-generated method stub
		networkPicUrls.add(url);
		overUpload();
	}

	@Override
	public void uploadError() {
		// TODO Auto-generated method stub
		uploadHandler.sendEmptyMessage(uploadNext);
	}

}