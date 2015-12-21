package com.example.casting.login.regist;


import java.io.File;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View.OnClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.example.casting.entity.BackgroundBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.login.regist.ChangeBgActivity.BACKGROUND_TYPE;
import com.example.casting.login.regist.views.BaseRegistMsgView;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.Users.UserAddProcessor;
import com.example.casting.processor.Users.UserCertificatProcessor;
import com.example.casting.processor.company.CompanyAddProcessor;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.processor.gallery.SetBackgroundProcessor;
import com.example.casting.processor.outside.OutSideSNProcessor;
import com.example.casting.util.ConstantData;
import com.example.casting.util.Session;
import com.example.casting_android.R;
import com.example.casting_android.bean.ImageBean;
import com.hp.hpl.sparta.xpath.ThisNodeTest;

/**
 * 15387538103 公司注册测试手机号码
 * @author Administrator
 *
 */
public class CompanyRegistActivity extends BaseCompanyInfoActivity implements
		OnClickListener, OnCheckedChangeListener {
	Handler uihandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what ==0 ){
				String value = msg.obj.toString();
				RegistBean bean = getLocalBean();
				bean.setCertification(value);
				directioview.updateViewVal(bean);
//				if( value!=null)
//					HttpCall(SNPro, value,"str");
			}
			
		}
	};
	OutSideSNProcessor SNPro = new OutSideSNProcessor();
	CompanyAddProcessor companyaddPro = new CompanyAddProcessor();
	UserAddProcessor useraddprocessor =new UserAddProcessor();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("完善资料");
		setLeftButtonAble(true, "返回");
		changeView();
	}
	
	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub

	}

	private final int SETVALUE = 10001;

	public void Jump(int activityResId, Bundle bd) {
		if (bd != null) {
			doActivity(activityResId, bd, SETVALUE);
		} else if (bd == null) {
			doActivity(activityResId, SETVALUE);
		}
	}
	/**
	 * 保存导演信息
	 * @param registbean
	 */
	private void savelocal(RegistBean registbean) {
		registbean.setId(Session.getInstance().getUser_id());
		registbean.setPhone(Session.getInstance().getPhone());
		if(registbean.getType().equals(ConstantData.Login_Type_Company)){
			HttpCall(companyaddPro, registbean,"str");
		}else if(registbean.getType().equals(ConstantData.Login_Type_Director)){
			HttpCall(useraddprocessor, registbean,"str");
		}
		
	}
	
	
	SetBackgroundProcessor backgroundProcessor = new SetBackgroundProcessor(this);
	/**
	 * 上传照片
	 * @param filebean
	 */
	private void savePic(){
		RegistBean regist = save();
		if(regist==null)return ;
		String filepath = regist.getHead_portrait();
		if(filepath!=null){
			UploadFile(filepath);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}


	@Override
	public void OnReturn(String content ,IBaseProcess processor) {
		// TODO Auto-generated method stub
		super.OnReturn(content, processor);
		ResultBean result = processor.json2Bean(content);
		if(processor.getProcessorId().equals(useraddprocessor.getProcessorId())){
			if(result.getCode() == Errors.OK){
				
				registinfo_director = true;
				RegistBean bean = save();
				String certification = bean.getCertification();
				//注册成功后提交认证信息 ----微博认证信息
				if( getRegistStatue().equals("1")){
					if(certification!=null && certification.equals("1")){
						HttpCall(SNPro, bean.getCertification(),"str");
					}else if(certification!=null && certification.length()>2){
						//肯定认证过微博，但是很傻比又提交了 图片认证的信息
						//提交认证信息
						HttpCall(new UserCertificatProcessor(), bean,"str");
					}
				}else{
					if(certification!=null && certification.length()>2){
						//提交认证信息
						HttpCall(new UserCertificatProcessor(), bean,"str");
					}
				}
					
				Session.getInstance().setHeadportait(bean.getHead_portrait());
				Session.getInstance().setName_nick(bean.getNickname());
				Session.getInstance().Save(this);
				Jump(R.string.maintabnew,null);
				setResult(Activity.RESULT_OK);
				finish();
			}
			showToast(result.getMessage());
			
		}else if(processor.getProcessorId().equals( companyaddPro.getProcessorId())){
			if(result.getCode() == Errors.OK){
				registinfo_company = true;
				RegistBean bean = save();
				String certification = bean.getCertification();
				//注册成功后提交认证信息 ----微博认证信息
				if( getRegistStatue().equals("1")){
					if(certification!=null && certification.equals("1")){
						HttpCall(SNPro, bean.getCertification(),"str");
					}else if(certification!=null && certification.length()>2){
						//肯定认证过微博，但是很傻比又提交了 图片认证的信息
						//提交认证信息
						HttpCall(new UserCertificatProcessor(), bean,"str");
					}
				}else{
					if(certification!=null && certification.length()>2){
						//提交认证信息
						HttpCall(new UserCertificatProcessor(), bean,"str");
					}
				}
				Session.getInstance().setName_nick(bean.getNickname());
				Session.getInstance().setUsertype(ConstantData.Login_Type_Company);
				Session.getInstance().Save(this);
				Jump(R.string.maintabnew,null);
				setResult(Activity.RESULT_OK);
				finish();
			}
			showToast(result.getMessage());
			
		}else if(processor.getProcessorId().equals(  SNPro.getProcessorId())){
			if(result.getCode() == Errors.OK){
				showAlertDialog("您的资料已认证","确定",true);
			}else{
				showToast("认证失败");
			}
		}
		else if(processor.getProcessorId().equals(backgroundProcessor.getProcessorId()))
		{
			if(result.getCode() == Errors.OK)
			{
				showToast("背景设置成功");
			}
			else {
				showToast("背景设置失败");
			}
		}
	}
	
	private void showDialog(){
		Builder bd = new Builder(this);
		bd.setMessage("图像提交失败！");
		bd.setPositiveButton("继续提交",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				savePic();
			}
		});
		bd.setNegativeButton("下次提交",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
			}
		});
		bd.create().show();
	}

	@Override
	public void OnPicReturn(String filepath) {
		// TODO Auto-generated method stub
		if(filepath!=null&& filepath.length()>0){
			File file = new File(filepath);
			RegistBean bean = save();
			ImageBean img =new ImageBean(ImageBean.type_res,R.drawable.ic_launcher );
			if( file.exists()){
				img =new ImageBean(ImageBean.type_filepath,filepath);
				bean.setHead_portrait(filepath);
			}
			bean.setUserimp(img);
			updateView(bean);
			savePic();
		}
		
	}
	
	@Override
	public void onPicReturn(String filepath, int source_id) {
		if(source_id == R.id.layout_changebackground)
		{
		    if(filepath !=null && !filepath.equals(""))
		    {
		    	BackgroundBean bean = new BackgroundBean();
		    	bean.setBackType(BACKGROUND_TYPE.COVER);
		    	bean.setImg_url(filepath);
		    	HttpCall(true,backgroundProcessor,bean,"str");
		    }
		}
		else if(source_id == R.id.piclayout)
		{
			OnPicReturn(filepath);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SETVALUE) {
			if (data != null) {
				RegistBean bean = save();
				if (resultCode == R.string.updateintrouction) {
					String introduce = data
							.getStringExtra(resultCode+"");
					bean.setIntroduce(introduce);
				} else if (resultCode == R.string.updatelabelnform) {
					String labels = data.getStringExtra(resultCode+"");
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
	protected void submitClick() {
		// TODO Auto-generated method stub
		if(CheckNull()){
			savelocal(save());
		}
	}

	@Override
	public int getOperaterType() {
		// TODO Auto-generated method stub
		return BaseRegistMsgView.OperateType_New;
	}

	@Override
	protected void getRegistResult(String value) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = 0 ;
		msg.obj = value ;
		uihandler.sendMessage(msg);
	}

	@Override
	public void uploadError() {
		// TODO Auto-generated method stub
		showToast("失败");
	}

	@Override
	public void uploadSuccess(String url) {
		// TODO Auto-generated method stub
		RegistBean bean = save();
		bean.setHead_portrait(url);
		updateView(bean);
		showToast("上传成功");
	}

	@Override
	public void uploadFiald(String msg) {
		// TODO Auto-generated method stub
		showDialog();
	}
	
}
