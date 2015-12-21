package com.example.casting.login.regist;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.casting.entity.FileBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.login.BaseTakePhoneForm;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.processor.file.UploadProcessor;
import com.example.casting.util.ConstantData;
import com.example.casting_android.R;

public class RegistDetailActivity extends BaseTakePhoneForm implements
		OnClickListener {
	View titleLayout;
	LinearLayout parent;
	TextView txt_companyintroduction;
	ImageView img_regist;
	Button btn_regist_submit;

	String cerfiticationPath = "";
	String cerfiticationLocalPath = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.casting_regist_detail);
		initSubView();

	}

	private void initData(Bundle bd) {
		String title = getString(R.string.directorregist);
		String btnval = "上传相关电影职员表";
		if (bd != null
				&& bd.getSerializable(R.string.registdetailactivity+"") != null) {
			RegistBean registBean = (RegistBean) bd
					.getSerializable(R.string.registdetailactivity+"");
			cerfiticationPath = registBean.getCertification();
			if (registBean.getType().equals(ConstantData.Login_Type_Company)) {
				title = getString(R.string.companyregist2);
				btnval = "上传公司证件";
			}
		}
		setTitle(title);
		btn_regist_submit.setText(btnval);
		if(cerfiticationPath.length()>0)//已经上传的 不需要再次上传
			showToast("您已提交过认证信息");
//			showWaitResultDialog();
	}

	private void initSubView() {
		titleLayout = (View) findViewById(R.id.titlelayout);
		parent = (LinearLayout) findViewById(R.id.contextlayout);
		txt_companyintroduction = (TextView) findViewById(R.id.txt_companyintroduction);
		img_regist = (ImageView) findViewById(R.id.img_regist);
		btn_regist_submit = (Button) findViewById(R.id.btn_regist_submit);
		initView(titleLayout);
		setLeftButtonAble(true, "返回");
		btn_regist_submit.setOnClickListener(this);
		img_regist.setOnClickListener(this);
		initData(getIntent().getExtras());
		
	}

	@Override
	public void LeftButtonClick() {
		finish();
	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		super.onClick(arg0);
		if (R.id.img_regist == arg0.getId()) {
			showSelectPicDialog();
		} else if(R.id.btn_regist_submit == arg0.getId()){
			uploadFile();
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void uploadFile() {
//		if(cerfiticationPath.length()>0){
//			return ;
//		}
		if (cerfiticationLocalPath.length() > 0) {
			savePic(cerfiticationLocalPath);
		}
	}


	/**
	 * 上传照片
	 * 
	 * @param filebean
	 */
	private void savePic(String filepath) {
		if (filepath != null) {
			UploadFile(filepath);
		}
	}

	private void showWaitResultDialog(){
		String value = "您的认证资料正在审核中......";
		String btntext = "确定";
		showAlertDialog(value, btntext, false);
	}
	@Override
	public View getParentView() {
		// TODO Auto-generated method stub
		return parent;
	}

	@Override
	public void OnPicReturn(String filepath) {
		// TODO Auto-generated method stub
		if (filepath != null) {
			File file = new File(filepath);
			if (file.exists()) {
				img_regist.setImageBitmap(BitmapFactory.decodeFile(filepath));
				cerfiticationLocalPath = filepath;
			}
		}
	}

	@Override
	public void OnReturn(String content, IBaseProcess processor) {
		// TODO Auto-generated method stub
		super.OnReturn(content, processor);
	}

	@Override
	public void leftDialogOnclick() {
		// TODO Auto-generated method stub
		super.leftDialogOnclick();
		if(cerfiticationPath.length()>0){
			if(cerfiticationLocalPath.length()>0 ){
				Intent intent = new Intent();
				Bundle bd = new Bundle();
				bd.putSerializable(R.string.registdetailactivity+"", cerfiticationPath);
				intent.putExtras(bd);
				setResult(R.string.registdetailactivity,intent);
				finish();
			}else{
				//等待审核 不应该 再修改
				finish();
			}
		}else{
			
		}
		
//		if (PageTag.equals(Page_Company)) {
//			// 上传认证 证书
//		} else if (PageTag.equals(Page_Director)) {
//			// 上传导演资料
//		}
	}
	
	@Override
	public void rightDialogOnclick() {
		// TODO Auto-generated method stub
		super.rightDialogOnclick();
		savePic(cerfiticationLocalPath);
	}

	@Override
	public void uploadError() {
		// TODO Auto-generated method stub
		showToast("失败");
	}

	@Override
	public void uploadSuccess(String url) {
		// TODO Auto-generated method stub
		cerfiticationPath = url;
		String value = "您的资料已经提交，审核工作将需要3个工作日，具体审核结果，请关注官方账号发送给你的私信";
		String btntext = "确定";
		showAlertDialog(value, btntext, false);
	}

	@Override
	public void uploadFiald(String msg) {
		// TODO Auto-generated method stub
		String value = "认证资料上传失败!";
		String left = "下次提交";
		String right = "继续提交";
		showAlertDialog(value, left,right, true);
	}

}
