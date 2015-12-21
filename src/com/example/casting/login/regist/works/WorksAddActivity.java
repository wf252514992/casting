package com.example.casting.login.regist.works;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.json.JSONObject;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.casting.entity.FileBean;
import com.example.casting.entity.WorkBean;
import com.example.casting.login.regist.BaseRegistForm;
import com.example.casting.login.regist.views.TextViewClickBean;
import com.example.casting.processor.BaseProcessor;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.processor.file.UploadProcessor;
import com.example.casting.processor.works.AddWorksProcessor;
import com.example.casting.processor.works.SetWorksProcessor;
import com.example.casting.util.view.RegistValueView;
import com.example.casting_android.R;
import com.example.casting_android.bean.ImageBean;

public class WorksAddActivity extends BaseRegistForm implements OnClickListener {
	View titleLayout;
	View parentview;
	WorkBean work;

	RegistValueView workspiclayout, releasetimelayout, worknamelayout,
			workerlayout, directorreallayout, joinwokerslayout, beizhulayout;
	BaseProcessor worksPro;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me3_works_add);
		work = new WorkBean();
		initSubView();

	}

	protected void initSubView() {
		// TODO Auto-generated method stub
		parentview = (View) findViewById(R.id.layout_main);
		titleLayout = (View) findViewById(R.id.titlelayout);
		workspiclayout = (RegistValueView) findViewById(R.id.workspiclayout);
		releasetimelayout = (RegistValueView) findViewById(R.id.releasetimelayout);
		worknamelayout = (RegistValueView) findViewById(R.id.worknamelayout);
		workerlayout = (RegistValueView) findViewById(R.id.workerlayout);
		directorreallayout = (RegistValueView) findViewById(R.id.directorreallayout);
		joinwokerslayout = (RegistValueView) findViewById(R.id.joinwokerslayout);
		beizhulayout = (RegistValueView) findViewById(R.id.beizhulayout);
		initView(titleLayout);
		workspiclayout.setOnClickListener(viewclick);
		releasetimelayout.setOnClickListener(viewclick);
		worknamelayout.setOnClickListener(viewclick);
		workerlayout.setOnClickListener(viewclick);
		directorreallayout.setOnClickListener(viewclick);
		joinwokerslayout.setOnClickListener(viewclick);
		beizhulayout.setOnClickListener(viewclick);

		initData(getIntent());

	}

	private void setUI(WorkBean bean) {
		if (bean.getWorkpic() != null) {
			workspiclayout.setImage(bean.getWorkpic());
		}
		releasetimelayout.setValue(bean.getRelease_time());
		worknamelayout.setValue(bean.getWorks_name());
		directorreallayout.setValue(bean.getDirector());
		joinwokerslayout.setValue(bean.getCooperation_actor());
		workerlayout.setValue(bean.getRole());
		beizhulayout.setValue(bean.getBz());
	}

	private void initData(Intent intent) {
		String title = "添加个人作品";
		String righttxt = "添加";
		if (intent != null) {
			Bundle bd = intent.getExtras();
			if (bd != null) {
				// 如果是编辑 获取资料，填充界面
				if (bd.getSerializable(R.string.worksactivity + "") != null) {
					work = (WorkBean) bd.getSerializable(R.string.worksactivity
							+ "");
				}
				worksPro = new SetWorksProcessor();
				title = "修改个人作品";
				righttxt = "保存";
			} else {
				worksPro = new AddWorksProcessor();
			}
		}
		setTitle(title);
		setLeftButtonAble(true, "返回");
		setRightButtonAble(true, righttxt);

		setUI(work);
	}

	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub
		showFinishDialog("是否放弃编辑？", "继续编辑", "放弃");
		// showAlertDialog("是否放弃编辑？", "放弃", "继续编辑");
	}

	@Override
	public void rightDialogOnclick() {
		// TODO Auto-generated method stub
		super.rightDialogOnclick();
	}

	@Override
	public void leftDialogOnclick() {
		// TODO Auto-generated method stub
		super.leftDialogOnclick();
		finish();
	}

	private boolean CheckNull() {
		if (releasetimelayout.isNull()) {
			showToast("请输入上映年份!");
			return false;
		} else if (worknamelayout.isNull()) {
			showToast("请输入剧名!");
			return false;
			// } else if (workerlayout.isNull()) {
			// showToast("请输入饰演角色!");
			// return false;
		} else if (directorreallayout.isNull()) {
			showToast("请输入导演!");
			return false;
		} else if (joinwokerslayout.isNull()) {
			showToast("请输入合作演员!");
			return false;
			// } else if (!checkTime()) {
			// return false;
		}
		return true;
	}

	private boolean checkTime() {
		String time = releasetimelayout.getTextValue().trim();
		Calendar canlendar = Calendar.getInstance();
		try {
			int t = Integer.parseInt(time);
			if (t < 1800) {
				showToast("上映年份不合法！");
				return false;
			} else if (t > canlendar.get(Calendar.YEAR)) {
				showToast("上映年份不合法！");
				return false;
			}
			return true;
		} catch (Exception ex) {
			showToast("上映年份不合法！");
			return false;
		}
	}

	private WorkBean setValue2bean() {
		work.setDirector(directorreallayout.getTextValue().toString());
		work.setCooperation_actor(joinwokerslayout.getTextValue().toString());
		work.setRelease_time(releasetimelayout.getTextValue().toString());
		work.setRole(workerlayout.getTextValue().toString());
		work.setWorks_name(worknamelayout.getTextValue().toString());
		work.setBz(beizhulayout.getTextValue());
		return work;

	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub
		if (CheckNull()) {
			HttpCall(worksPro, setValue2bean(), "str");
		}
	}

	@Override
	public void OnReturn(String content, IBaseProcess processor) {
		// TODO Auto-generated method stub
		super.OnReturn(content, processor);
		ResultBean resultbean = processor.json2Bean(content);
		if (processor.getProcessorId().equals(worksPro.getProcessorId())) {
			if (resultbean.getCode() == Errors.OK) {
				setBack();
				showToast("保存成功!");
			} else {
				showToast(resultbean.getMessage());
			}
		}
	}

	private void showDialog() {
		Builder bd = new Builder(this);
		bd.setMessage("图像数据保存失败！");
		bd.setPositiveButton("继续保存", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				savePic();
			}
		});
		bd.setNegativeButton("下次保存", null);
		bd.create().show();
	}

	private void showFinishDialog(String value, String textleft, String right) {
		Builder bd = new Builder(this);
		bd.setMessage(value);
		bd.setPositiveButton(right, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		bd.setNegativeButton(textleft, null);
		bd.create().show();
	}

	/**
	 * 上传照片
	 * 
	 * @param filebean
	 */
	private void savePic() {
		ImageBean img = setValue2bean().getWorkpic();
		if (img != null && img.getRestype() == ImageBean.type_filepath) {
			String filepath = img.getImg_res();
			if (filepath != null) {
				UploadFile(filepath);
			}
		}
	}

	private void setBack() {
		Bundle bd = new Bundle();
		bd.putSerializable(R.string.worksaddactivity + "", setValue2bean());
		Intent intent = new Intent();
		intent.putExtras(bd);
		setResult(R.string.worksaddactivity, intent);
		finish();
	}

	private OnClickListener viewclick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (arg0.getId() == R.id.workspiclayout) {
				showSelectPicDialog();
				return;
			} else {

				int clicktype = 0;// 默认是编辑文本 0,1表示编辑标签，2.地点编辑，3，表示编辑日期
				// 4 .性别选择框 5,简介 ,6:进入认证界面
				if (arg0.getId() == R.id.releasetimelayout) {
					clicktype = 3;
				}
				String name = arg0.getTag().toString();
				String val = ((RegistValueView) arg0).getTextValue();
				TextViewClickBean bean = new TextViewClickBean();
				bean.setResid(arg0.getId());
				bean.setKeyname(name);
				bean.setKeyval(val);
				bean.setClicktype(clicktype);

				dealSubViewClick(bean);
			}
		}
	};

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		super.onClick(arg0);

	}

	@Override
	public View getParentView() {
		// TODO Auto-generated method stub
		return parentview;
	}

	@Override
	public String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy_MM_dd_HH_mm_ss");
		return dateFormat.format(date) + ".jpg";
	}

	@Override
	public void OnPicReturn(String filepath) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = UpdatePic;
		msg.obj = filepath;
		handler.sendMessage(msg);

	}

	private static final int UpdatePic = 10001;
	Handler handler = new Handler() {

		public void dispatchMessage(android.os.Message msg) {
			switch (msg.what) {
			case UpdatePic: {
				try {
					String filepath = msg.obj.toString();
					ImageBean imgbean = new ImageBean(ImageBean.type_filepath,
							filepath);
					work.setWorkpic(imgbean);
					workspiclayout.setImage(imgbean);
					savePic();
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
				break;
			}
		};
	};

	@Override
	public String getDirs() {
		// TODO Auto-generated method stub
		return "/workpic/";
	}

	@Override
	public void changeText(String val, int resid) {
		// TODO Auto-generated method stub
		((RegistValueView) findViewById(resid)).setValue(val);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void uploadError() {
		// TODO Auto-generated method stub
		showToast("失败");
	}

	@Override
	public void uploadSuccess(String url) {
		// TODO Auto-generated method stub
		ImageBean img = new ImageBean(ImageBean.type_url, url);
		work.setWorkpic(img);
		work.setWorks_images(url);
		showToast("上传成功");
	}

	@Override
	public void uploadFiald(String msg) {
		// TODO Auto-generated method stub
		showDialog();
	}
}
