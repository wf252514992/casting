package com.example.casting.login.regist;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.example.casting.entity.BackgroundBean;
import com.example.casting.entity.FileBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.entity.WinnerBean;
import com.example.casting.entity.WorkBean;
import com.example.casting.login.regist.ChangeBgActivity.BACKGROUND_TYPE;
import com.example.casting.login.regist.CompanyEditActivity.UploadTool;
import com.example.casting.login.regist.views.DirectorRegistView;
import com.example.casting.login.regist.views.UserRegistView;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.Users.UserCertificatProcessor;
import com.example.casting.processor.Users.UserSetProcessor;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.processor.file.UploadProcessor;
import com.example.casting.processor.gallery.SetBackgroundProcessor;
import com.example.casting.processor.outside.OutSideSNProcessor;
import com.example.casting.util.PicTool;
import com.example.casting.util.Server_path;
import com.example.casting_android.R;
import com.example.casting_android.bean.ImageBean;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

public class DirectorEditActivity extends BaseWeiboRegistInfoActivity {

	View titleLayout;
	LinearLayout layout_content;// 内容布局
	DirectorRegistView directioview;
	UserSetProcessor usersetPro = new UserSetProcessor();
	SetBackgroundProcessor setBackgroundProcessor = new SetBackgroundProcessor(
			this);

	// UserGetProcessor usergetPro = new UserGetProcessor();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.casting_edit_company);
		titleLayout = (View) findViewById(R.id.titlelayout);
		layout_content = (LinearLayout) findViewById(R.id.layout_content);
		initView(titleLayout);
		setTitle("编辑资料");
		setRightButtonAble(true, "保存");
		setLeftButtonAble(true, "返回");
		setButton(btn_usedefault, View.VISIBLE);
		// RegistBean bean = new RegistBean();
		// bean.setId(Session.getInstance().getUser_id());

		directioview = new DirectorRegistView(this, handler, null,
				getOperaterType());
		layout_content.addView(directioview.getView());
		RegistBean registbean = initData(getIntent());
		updateView(registbean);
		// HttpCall( usergetPro, bean);
		// HttpCall(false , getworkPro, bean);
		// HttpCall(false , getwinPro, bean);

	}

	Handler uihandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				String value = msg.obj.toString();
				RegistBean bean = getLocalBean();
				bean.setCertification(value);
				directioview.updateViewVal(bean);
			}

		}
	};

	OutSideSNProcessor SNPro = new OutSideSNProcessor();

	private RegistBean initData(Intent intent) {
		if (intent != null) {
			Bundle bd = intent.getExtras();
			if (bd != null
					&& bd.getSerializable(R.string.directoreditactivity + "") != null) {
				RegistBean bean = (RegistBean) bd
						.getSerializable(R.string.directoreditactivity + "");
				return bean;
			}
		}
		return null;
	}

	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub
		submitClick();
	}

	private void updateView(RegistBean registbean) {
		directioview.updateViewVal(registbean);
	}

	// ArrayList<WorkBean> workslist = new ArrayList<WorkBean>();
	// ArrayList<WinnerBean> winnerslist = new ArrayList<WinnerBean>();
	@Override
	public void OnReturn(String content, IBaseProcess processor) {
		// TODO Auto-generated method stub
		super.OnReturn(content, processor);
		ResultBean result = processor.json2Bean(content);
		// if (processor.getMethod().equals(usergetPro.getMethod())) {
		// if(result.getCode() == Errors.OK){
		// RegistBean registbean = (RegistBean) result.getObj();
		// //更新 注册信息
		// updateView(registbean);
		// }else{
		// showToast(result.getMessage());
		// }
		// }
		if (processor.getProcessorId().equals(usersetPro.getProcessorId())) {
			if (result.getCode() == Errors.OK) {
				RegistBean bean = getLocalBean();
				String certification = bean.getCertification();
				// 注册成功后提交认证信息 ----微博认证信息
				if (getRegistStatue().equals("1")) {
					if (certification != null && certification.equals("1")) {
						HttpCall(SNPro, bean.getCertification(), "str");
					} else if (certification != null
							&& certification.length() > 2) {
						// 肯定认证过微博，但是很傻比又提交了 图片认证的信息
						// 提交认证信息
						HttpCall(new UserCertificatProcessor(), bean,"str");
					}
				} else {
					if (certification != null && certification.length() > 2) {
						// 提交认证信息
						HttpCall(new UserCertificatProcessor(), bean,"str");
					}
				}
				finish();
				showToast("保存成功");
			} else {
				showToast(result.getMessage());
			}
		} else if (processor.getProcessorId().equals(
				setBackgroundProcessor.getProcessorId())) {
			if (result.getCode() == Errors.OK) {
				showToast("背景设置成功");
			} else {
				showToast("背景设置失败");
			}
		} else if (processor.getProcessorId().equals(SNPro.getProcessorId())) {
			if (result.getCode() == Errors.OK) {
				showAlertDialog("您的资料已认证", "确定", true);
			} else {
				showToast("认证失败");
			}
		}
	}

	private void showDialog() {
		Builder bd = new Builder(this);
		bd.setMessage("图像上传失败！");
		bd.setPositiveButton("继续上传", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				savePic(getLocalBean().getUserimp());
			}
		});
		bd.setNegativeButton("下次上传", null);
		bd.create().show();
	}

	/**
	 * 上传照片
	 * 
	 * @param filebean
	 */
	private void savePic(ImageBean img) {
		if (img != null && img.getRestype() == ImageBean.type_filepath) {
			String filepath = img.getImg_res();
			if (filepath != null) {
				UploadFile(filepath);
			}
		}
	}

	@Override
	public RegistBean getLocalBean() {
		// TODO Auto-generated method stub
		return directioview.getValues();
	}

	protected void submitClick() {
		// TODO Auto-generated method stub
		savelocal(getLocalBean());
	}

	private void savelocal(RegistBean registbean) {
		if (CheckNull()) {
			HttpCall(usersetPro, registbean, "str");
		}
	}

	public boolean CheckNull() {
		if (!directioview.checkNull()) {
			showToast(directioview.getErrors());
			return false;
		}
		return true;
	}

	@Override
	public void changeText(String val, int resid) {
		// TODO Auto-generated method stub
		directioview.changeSubView(resid, val);
	}

	private final int SETVALUE = 10001;

	public void Jump(int activityResId, Bundle bd) {
		if (bd != null) {
			doActivity(activityResId, bd, SETVALUE);
		} else if (bd == null) {
			doActivity(activityResId, SETVALUE);
		}
	}

	@Override
	public View getWheelViewParent() {
		// TODO Auto-generated method stub
		return layout_content;
	}

	@Override
	public int toCompanyOrDirector() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SETVALUE) {
			if (data != null) {
				RegistBean bean = getLocalBean();
				if (resultCode == R.string.updateintrouction) {
					String introduce = data.getStringExtra(resultCode + "");
					bean.setIntroduce(introduce);
				} else if (resultCode == R.string.updatelabelnform) {
					String labels = data.getStringExtra(resultCode + "");
					bean.setLabel(labels);
				} else if (resultCode == R.string.registdetailactivity) {
					String certifactionpath = data.getStringExtra(resultCode+"");
					bean.setCertification(certifactionpath);
				}
				updateView(bean);
			}
		}
	}

	@Override
	public View getParentView() {
		// TODO Auto-generated method stub
		return getWheelViewParent();
	}

	@Override
	public void OnPicReturn(String filepath) {
		// TODO Auto-generated method stub
		if (filepath != null && filepath.length() > 0) {
			File file = new File(filepath);
			RegistBean bean = getLocalBean();
			ImageBean img = new ImageBean(ImageBean.type_res,
					R.drawable.ic_launcher);
			if (file.exists()) {
				img = new ImageBean(ImageBean.type_filepath, filepath);
				savePic(img);
			}
			bean.setUserimp(img);
			updateView(bean);
		}
	}

	public int getOperaterType() {
		// TODO Auto-generated method stub
		return UserRegistView.OperateType_Set;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		super.onClick(arg0);
		if (arg0.getId() == R.id.btn_usedefault_pic) {
			// 设置背景
			BackgroundBean bean = new BackgroundBean();
			bean.setBackType(BACKGROUND_TYPE.COVER);
			// bean.setImg_resource(R.drawable.me_bgallb);
			// HttpCall(true,setBackgroundProcessor,bean,"str");
			startUploadImg(bean);
			disPicDialog();
		}
	}

	@Override
	public void onPicReturn(String filepath, int source_id) {
		if (source_id == R.id.layout_changebackground) {
			if (filepath != null && !filepath.equals("")) {
				BackgroundBean bean = new BackgroundBean();
				bean.setBackType(BACKGROUND_TYPE.COVER);
				bean.setImg_url(filepath);
				startUploadImg(bean);
				// HttpCall(true,setBackgroundProcessor,bean,"str");
			}
		} else {
			OnPicReturn(filepath);
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void getRegistResult(String value) {
		// // TODO Auto-generated method stub
		// Message msg = new Message();
		// msg.what = 0;
		// msg.obj = value;
		// uihandler.sendMessage(msg);
	}

	class UploadTool extends AsyncTask<String, Integer, String> {
		BackgroundBean uploadbean;

		public UploadTool(BackgroundBean bean) {
			uploadbean = bean;
		}

		protected String doInBackground(String... params) {
			try {
				String url = uploadbean.getImg_url();
				if (url.length() == 0)
					return "";
				File outputFile = new File(Server_path.SavePath
						+ url.substring(url.lastIndexOf("/") + 1));
				String path = PicTool.compressImage(DirectorEditActivity.this,
						url, Server_path.SavePath, outputFile);
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
			if (result != null) {
				uploadbean.setImg_data(result);
				HttpCall(true, setBackgroundProcessor, uploadbean, "str");
				return;

			}
		}
	}

	private void startUploadImg(BackgroundBean bean) {
		UploadTool tool = new UploadTool(bean);
		tool.execute();
	}

	@Override
	public void uploadError() {
		// TODO Auto-generated method stub
		showToast("失败");
	}

	@Override
	public void uploadSuccess(String url) {
		// TODO Auto-generated method stub
		RegistBean bean = getLocalBean();
		bean.setHead_portrait(url);
		updateView(bean);
		showToast("上传成功!");
	}

	@Override
	public void uploadFiald(String msg) {
		// TODO Auto-generated method stub
		showDialog();
	}
}
