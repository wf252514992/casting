package com.example.casting.login.regist;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.casting.entity.FileBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.entity.WinnerBean;
import com.example.casting.entity.WorkBean;
import com.example.casting.login.regist.views.UserRegistView;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.Users.UserGetProcessor;
import com.example.casting.processor.Users.UserSetProcessor;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.processor.file.UploadProcessor;
import com.example.casting.util.Session;
import com.example.casting_android.R;
import com.example.casting_android.bean.ImageBean;

public class UserInfoEditActivity extends BaseUserInfoActivity {

	UserSetProcessor usersetPro = new UserSetProcessor();

	// UserGetProcessor usergetPro = new UserGetProcessor();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView(titleLayout);
		setTitle("个人资料");
		setRightButtonAble(true, "保存");
		setLeftButtonAble(true, "返回");
		// RegistBean bean = new RegistBean();
		// bean.setId(Session.getInstance().getUser_id());
		// HttpCall(usergetPro, bean);
		RegistBean registbean = initData(getIntent());
		updateView(registbean);

	}

	private RegistBean initData(Intent intent) {
		if (intent != null) {
			Bundle bd = intent.getExtras();
			if (bd != null
					&& bd.getSerializable(R.string.userinfoeditactivity + "") != null) {
				RegistBean bean = (RegistBean) bd
						.getSerializable(R.string.userinfoeditactivity + "");
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
		// }else
		// if(processor.getProcessorId().equals(getworkPro.getProcessorId())){
		// ArrayList<WorkBean> works = (ArrayList<WorkBean>)result.getObj();
		// RegistBean registbean = saveViewValue();
		// if(registbean!=null){
		// registbean.setWorks(works);
		// updateView(registbean);
		// }
		// workslist.clear();
		// workslist.addAll(works);
		// }else
		// if(processor.getProcessorId().equals(getwinPro.getProcessorId())){
		// ArrayList<WinnerBean> winners = (ArrayList)result.getObj();
		// RegistBean registbean = saveViewValue();
		// if(registbean!=null){
		// registbean.setWinners(winners);
		// updateView(registbean);
		// }
		// winnerslist.clear();
		// winnerslist.addAll(winners);
		// } else
		if (processor.getMethod().equals(usersetPro.getMethod())) {
			if (result.getCode() == Errors.OK) {
				finish();
				showToast("保存成功");
			} else {
				showToast(result.getMessage());
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
		bd.setNegativeButton("下次上传", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
			}
		});
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
//				File file = new File(filepath);
//				if (file.exists()) {
//					FileBean fileb = new FileBean();
//					fileb.setDatatype(FileBean.DT_FILEPATH);
//					fileb.setEndtag("jpg");
//					fileb.setFieldata(filepath);
//					HttpPost(filepro, fileb);
//					return true;
//				}
			}
		}
//		return false;
	}

	@Override
	public RegistBean getLocalBean() {
		// TODO Auto-generated method stub
		return saveViewValue();
	}

	@Override
	protected void submitClick() {
		// TODO Auto-generated method stub
		savelocal(saveViewValue());
	}

	private void savelocal(RegistBean registbean) {
		if (checkNull(registbean)) {
			HttpCall(usersetPro, registbean, "str");
		}
	}

	@Override
	public void changeText(String val, int resid) {
		// TODO Auto-generated method stub
		userView.changeSubView(resid, val);
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
		return contentlayout;
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
				RegistBean bean = saveViewValue();
				if (resultCode == R.string.updateintrouction) {
					String introduce = data.getStringExtra(resultCode + "");
					bean.setIntroduce(introduce);
				} else if (resultCode == R.string.updatelabelnform) {
					String labels = data.getStringExtra(resultCode + "");
					bean.setLabel(labels);
				} else if (resultCode == R.string.registdetailactivity) {
					// String filepath =
					// data.getStringExtra(RegistDetailActivity.class.getName());
					// registbean.
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
			RegistBean bean = saveViewValue();
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

	@Override
	public int getOperaterType() {
		// TODO Auto-generated method stub
		return UserRegistView.OperateType_Set;
	}

	public void CheckNickName(String nickName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void uploadError() {
		// TODO Auto-generated method stub
		showToast("失败");
	}

	@Override
	public void uploadSuccess(String url) {
		// TODO Auto-generated method stub
		RegistBean bean = saveViewValue();
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
