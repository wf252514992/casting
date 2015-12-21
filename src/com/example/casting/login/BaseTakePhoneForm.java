package com.example.casting.login;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import com.example.casting.entity.FileBean;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.processor.file.UploadProcessor;
import com.example.casting.util.ConstantData;
import com.example.casting.util.PicTool;
import com.example.casting.util.Server_path;
import com.example.casting_android.R;

/**
 * p拍照，系统选择照片的基类
 * 
 * @author Administrator
 * 
 */
public abstract class BaseTakePhoneForm extends BaseForm implements
		OnClickListener {

	public abstract View getParentView();

	public abstract void OnPicReturn(String filepath);

	public void onPicReturn(String filepath, int source_id) {
		OnPicReturn(filepath);
	}

	protected Button btn_usedefault, btn_take, btn_cancel, btn_select;
	/** 点击弹出框的控件id */
	private int source_id;

	public String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy_MM_dd_HH_mm_ss");
		return dateFormat.format(date) + ".jpg";
	}

	public String getDirs() {
		return "";
	}

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String dirstr = ConstantData.getCastingDir() + "/Camera/" + getDirs();
		File dir = new File(dirstr);
		dir.mkdirs();// 创建照片的存储目录
		mCurrentPhotoFile = new File(dirstr, getPhotoFileName());// 给新照的照片文件命名
		initPopView();
	};

	PopupWindow popupWindow;

	private void initPopView() {
		View contentView = getLayoutInflater().inflate(R.layout.popview_s_pic,
				null);
		popupWindow = new PopupWindow(contentView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		btn_take = (Button) contentView.findViewById(R.id.btn_take);
		btn_select = (Button) contentView.findViewById(R.id.btn_select);
		btn_cancel = (Button) contentView.findViewById(R.id.btn_cancel);
		btn_usedefault = (Button) contentView
				.findViewById(R.id.btn_usedefault_pic);
		btn_take.setOnClickListener(this);
		btn_select.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		btn_usedefault.setOnClickListener(this);
		popupWindow.setFocusable(true);// 取得焦点
		// 设置点击窗口外边窗口消失
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		/** 设置PopupWindow弹出和退出时候的动画效果 */
		popupWindow.setAnimationStyle(R.style.anim_popup_dir);
		// popupWindow.setAnimationStyle(R.style.animation);
		// popupWindow.setTouchInterceptor(new OnTouchListener()
		// {
		// @Override
		// public boolean onTouch(View v, MotionEvent event)
		// {
		// if (event.getAction() == MotionEvent.ACTION_OUTSIDE)
		// {
		// disPicDialog();
		// return true;
		// }
		// return false;
		// }
		// });
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// 设置背景颜色变暗
				sunBackground();
			}
		});
	}

	public void darkBackground() {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = .3f;
		getWindow().setAttributes(lp);

	}

	public void sunBackground() {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 1.0f;
		getWindow().setAttributes(lp);
	}

	/**
	 * 拍照，选择系统照片对话框
	 */
	public void showSelectPicDialog() {
		// 设置背景颜色变暗
		if (popupWindow == null) {
			initPopView();
		}
		darkBackground();
		if (!popupWindow.isShowing()) {
			/** 设置PopupWindow弹出后的位置 */
			popupWindow.showAtLocation(getParentView(), Gravity.BOTTOM, 0, 0);
		}

	}

	/**
	 * 拍照，选择系统照片对话框 (适用于一个界面有两个控件需要显示弹框的情况)
	 * sourceId:标识是哪个按钮点击之后显示的对话框，方便数据返回后做处理
	 */
	public void showSelectPicDialog(int sourceId) {
		this.source_id = sourceId;
		// 根据资源id判断是否需要显示设置默认背景按钮
		if (source_id == R.id.layout_changebackground) {
			setButton(btn_usedefault, View.VISIBLE);
		} else {
			setButton(btn_usedefault, View.GONE);
		}
		// 设置背景颜色变暗
		if (popupWindow == null) {
			initPopView();
		}
		darkBackground();
		if (!popupWindow.isShowing()) {
			/** 设置PopupWindow弹出后的位置 */
			popupWindow.showAtLocation(getParentView(), Gravity.BOTTOM, 0, 0);
		}

	}

	public void disPicDialog() {
		if (popupWindow.isShowing()) {
			popupWindow.dismiss();// 关闭
		}
	}

	private static final int PHOTO_PICKED_WITH_DATA = 11110;
	private static final int CAMERA_WITH_DATA = 11111;

	// protected OnClickListener myClick = new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// // TODO Auto-generated method stub
	// if (arg0.getId() == R.id.btn_take) {
	// // 用户点击了从照相机获取
	// String status = Environment.getExternalStorageState();
	// if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
	// doTakePhoto();// 用户点击了从照相机获取
	// } else {
	// showToast("没有SD卡");
	// }
	// } else if (arg0.getId() == R.id.btn_select) {
	// // 从图库取
	// doPickPhotoFromGallery();
	// }
	// disPicDialog();
	//
	// }
	// };

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case PHOTO_PICKED_WITH_DATA:
				// 下面就是显示照片了
				handler.sendEmptyMessage(0);
				System.out.println("set new photo");
				break;
			case CAMERA_WITH_DATA:
				doCropPhoto(mCurrentPhotoFile);
				break;
			}
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				if (source_id != 0) {
					onPicReturn(mCurrentPhotoFile.getAbsolutePath(), source_id);
				} else {
					OnPicReturn(mCurrentPhotoFile.getAbsolutePath());
				}

			}
		};
	};
	private File mCurrentPhotoFile;// 照相机拍照得到的图片

	// 封装请求Gallery的intent
	public Intent getPhotoPickIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		// intent.putExtra("aspectX", 1);
		// intent.putExtra("aspectY", 1);
		// intent.putExtra("outputX", 240);
		// intent.putExtra("outputY", 240);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(mCurrentPhotoFile));
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		return intent;
	}

	/**
	 * 拍照获取图片
	 * 
	 */
	protected void doTakePhoto() {
		try {
			final Intent intent = getTakePickIntent();
			startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			showToast("图片不存在");
		}
	}

	public Intent getTakePickIntent() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(mCurrentPhotoFile));
		return intent;
	}

	// 请求Gallery程序
	protected void doPickPhotoFromGallery() {
		try {
			final Intent intent = getPhotoPickIntent();
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			showToast("图片不存在");
		}
	}

	protected void doCropPhoto(File f) {
		try {
			// 启动gallery去剪辑这个照片
			final Intent intent = getCropImageIntent(Uri.fromFile(f));
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (Exception e) {
			showToast("图片不存在");
		}
	}

	/**
	 * Constructs an intent for image cropping. 调用图片剪辑程序
	 */
	public Intent getCropImageIntent(Uri photoUri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(photoUri, "image/*");
		intent.putExtra("crop", "true");
		// intent.putExtra("aspectX", 1);
		// intent.putExtra("aspectY", 1);
		// intent.putExtra("outputX", 240);
		// intent.putExtra("outputY", 240);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(mCurrentPhotoFile));
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		return intent;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/**
	 * 设置按钮的显示情况
	 * 
	 * @param view
	 * @param visibility
	 */
	public void setButton(View view, int visibility) {
		view.setVisibility(visibility);
	}

	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == R.id.btn_take) {
			// 用户点击了从照相机获取
			String status = Environment.getExternalStorageState();
			if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
				doTakePhoto();// 用户点击了从照相机获取
			} else {
				showToast("没有SD卡");
			}
		} else if (arg0.getId() == R.id.btn_select) {
			// 从图库取
			doPickPhotoFromGallery();
		}
		disPicDialog();
	}

	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub

	}

	/**
	 * encodeBase64File:(将文件转成base64 字符串). <br/>
	 * 
	 * @author
	 * @param path
	 *            文件路径
	 * @return
	 * @throws Exception
	 * @since JDK 1.6
	 */
	public String encodeBase64File(String path) throws Exception {
		File file = new File(path);
		FileInputStream inputFile = new FileInputStream(file);
		byte[] buffer = new byte[(int) file.length()];
		inputFile.read(buffer);
		inputFile.close();
		return Base64.encodeToString(buffer, Base64.DEFAULT);
	}

	/**
	 * 上传图片
	 * 
	 * @param filename
	 */
	public void UploadFile(String filename) {
		UploadTool upload = new UploadTool(filename);
		upload.execute();
	}

	private class UploadTool extends AsyncTask<String, Integer, String> {
		String url;

		public UploadTool(String list) {
			url = list;
		}

		protected String doInBackground(String... params) {
			try {
				File outputFile = new File(Server_path.SavePath
						+ url.substring(url.lastIndexOf("/") + 1));
				String path = PicTool.compressImage(BaseTakePhoneForm.this,
						url, Server_path.SavePath, outputFile);
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
				HttpPost(true, filepro, fileb);
				return;

			}
			takephotohandler.sendEmptyMessage(uploaderror);
		}
	}

	UploadProcessor filepro = new UploadProcessor();
	private final int uploaderror = 11123;
	private final int uploadSuccess = 11334;
	private final int uploadFaild = 34223;
	Handler takephotohandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case uploaderror:
				uploadError();
				break;
			case uploadSuccess:
				String url = msg.obj.toString();
				uploadSuccess(url);
				break;
			case uploadFaild:
				String showtxt = msg.obj.toString();
				uploadFiald(showtxt);
				break;
			default:
				break;
			}
		}

	};

	public abstract void uploadError() ;

	public abstract void uploadSuccess(String url) ;

	public abstract void uploadFiald(String msg) ;

	@Override
	public void OnReturn(String content, IBaseProcess processor) {
		// TODO Auto-generated method stub
		ResultBean result = processor.json2Bean(content);
		String mess = result.getMessage();
		if (processor.getProcessorId().equals(filepro.getProcessorId())) {
			
			if (result.getCode() == Errors.OK) {
				// 返回 url 设置bean
				JSONObject json = (JSONObject) result.getObj();
				try {
					String url = json.getString("url");
					Message msg = new Message();
					msg.what =uploadSuccess;
					msg.obj = url;
					takephotohandler.sendMessage(msg);
					return;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					mess ="解析出错" ;
				}

			}
			Message msg = new Message();
			msg.what =uploadFaild;
			msg.obj = mess;
			takephotohandler.sendMessage(msg);
			
			return;
		}
		super.OnReturn(content, processor);
		
	}

}
