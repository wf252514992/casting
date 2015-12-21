package com.example.casting.login.regist.winner;

import java.io.File;
import java.text.SimpleDateFormat;
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
import com.example.casting.entity.FileBean;
import com.example.casting.entity.WinnerBean;
import com.example.casting.login.regist.BaseRegistForm;
import com.example.casting.login.regist.views.TextViewClickBean;
import com.example.casting.processor.BaseProcessor;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.processor.file.UploadProcessor;
import com.example.casting.processor.winners.AddWinnerProcessor;
import com.example.casting.processor.winners.SetWinnerProcessor;
import com.example.casting.util.view.RegistValueView;
import com.example.casting_android.R;
import com.example.casting_android.bean.ImageBean;

public class WinnerAddActivity extends BaseRegistForm implements
		OnClickListener {
	View titleLayout;
	View parentview;
	// LinearLayout imglayout;
	// ImageView img_winnerspic ;
	// TextView txt_wintime;
	// TextView txt_winname;
	// TextView txt_winwork;
	RegistValueView winnerspiclayout, gettimelayout, winnernamelayout,
			winworklayout, beizhulayout;
	WinnerBean winner;
	BaseProcessor winnerPro;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me3_winners_add);
		winner = new WinnerBean();
		initSubView();
	}

	protected void initSubView() {
		// TODO Auto-generated method stub
		titleLayout = (View) findViewById(R.id.titlelayout);
		parentview = (View) findViewById(R.id.mainlayout);
		winnerspiclayout = (RegistValueView) findViewById(R.id.winnerspiclayout);
		gettimelayout = (RegistValueView) findViewById(R.id.gettimelayout);
		winnernamelayout = (RegistValueView) findViewById(R.id.winnernamelayout);
		winworklayout = (RegistValueView) findViewById(R.id.winworklayout);
		beizhulayout = (RegistValueView) findViewById(R.id.beizhulayout);

		initView(titleLayout);
		initData(getIntent());
		winnerspiclayout.setOnClickListener(viewclick);
		gettimelayout.setOnClickListener(viewclick);
		winnernamelayout.setOnClickListener(viewclick);
		winworklayout.setOnClickListener(viewclick);
		beizhulayout.setOnClickListener(viewclick);
	}

	private void initData(Intent intent) {
		String title = "添加获奖经历";
		String righttxt = "添加";

		if (intent != null) {
			Bundle bd = intent.getExtras();
			if (bd != null) {
				// 如果是编辑 获取资料，填充界面
				if (bd.getSerializable(R.string.winneractivity + "") != null) {
					winner = (WinnerBean) bd
							.getSerializable(R.string.winneractivity + "");
				}
				winnerPro = new SetWinnerProcessor();
				title = "修改获奖经历";
				righttxt = "保存";
			} else {
				winnerPro = new AddWinnerProcessor();
			}
		}
		setTitle(title);
		setLeftButtonAble(true, "返回");
		setRightButtonAble(true, righttxt);
		setUI(winner);
	}

	private void setUI(WinnerBean bean) {
		if (bean.getWinnerspic() != null) {
			winnerspiclayout.setImage(bean.getWinnerspic());
		}
		gettimelayout.setValue(bean.getWinners_time());
		winnernamelayout.setValue(bean.getWinners_name());
		winworklayout.setValue(bean.getWinners_works_name());
		beizhulayout.setValue(bean.getBz());
	}

	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub
		showFinishDialog("是否放弃编辑？", "继续编辑", "放弃");
	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub
		if (CheckNull()) {
			HttpCall(winnerPro, setValue2Bean(), "str");
		}
	}

	private boolean CheckNull() {
		if (gettimelayout.isNull()) {
			showToast("请输入获奖年份!");
			return false;
		} else if (winnernamelayout.isNull()) {
			showToast("请输入获得奖项!");
			return false;
		} else if (winworklayout.isNull()) {
			showToast("请输入获奖作品!");
			return false;
			// }else if(!checkTime()){
			// return false;
		}
		return true;
	}

	// private boolean checkTime(){
	// String time = txt_wintime.getText().toString();
	// Calendar canlendar = Calendar.getInstance();
	// try{
	// int t = Integer.parseInt(time);
	// if( t >canlendar.get(Calendar.YEAR)){
	// showToast("获奖时间不合法！");
	// return false;
	// }
	// return true;
	// }catch(Exception ex){
	// showToast("获奖时间不合法！");
	// return false;
	// }
	// }
	private WinnerBean setValue2Bean() {
		winner.setWinners_name(winnernamelayout.getTextValue());
		winner.setWinners_time(gettimelayout.getTextValue().toString());
		winner.setWinners_works_name(winworklayout.getTextValue().toString());
		winner.setBz(beizhulayout.getTextValue());
		return winner;
	}

	@Override
	public void OnReturn(String content, IBaseProcess processor) {
		// TODO Auto-generated method stub
		super.OnReturn(content, processor);
		ResultBean resultbean = processor.json2Bean(content);
		if (processor.getProcessorId().equals(winnerPro.getProcessorId())) {
			if (resultbean.getCode() == Errors.OK) {
				// 需要上传 图像
				setBack();
				showToast("保存成功!");
			} else {
				showToast(resultbean.getMessage());
			}
		}
	}

	private void showDialog() {
		Builder bd = new Builder(this);
		bd.setMessage("图像保存失败！");
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

	/**
	 * 上传照片
	 * 
	 * @param filebean
	 */
	private void savePic() {
		ImageBean img = setValue2Bean().getWinnerspic();
		if (img != null && img.getRestype() == ImageBean.type_filepath) {
			String filepath = img.getImg_res();
			if (filepath != null) {
				UploadFile(filepath);
			}
		}
	}

	private void setBack() {
		Bundle bd = new Bundle();
		bd.putSerializable(R.string.winneraddactivity + "", setValue2Bean());
		Intent intent = new Intent();
		intent.putExtras(bd);
		setResult(R.string.winneraddactivity, intent);
		finish();
	}

	private OnClickListener viewclick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (arg0.getId() == R.id.winnerspiclayout) {
				showSelectPicDialog();
				return;
			} else {
				int clicktype = 0;// 默认是编辑文本 0,1表示编辑标签，2.地点编辑，3，表示编辑日期
				// 4 .性别选择框 5,简介 ,6:进入认证界面
				if (arg0.getId() == R.id.gettimelayout) {
					clicktype = 3;
				}
				// 4 .性别选择框 5,简介 ,6:进入认证界面
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
					winner.setWinnerspic(imgbean);
					winnerspiclayout.setImage(imgbean);
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
		return "/winnerpic/";
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

	@Override
	public void uploadError() {
		// TODO Auto-generated method stub
		showToast("失败");
	}

	@Override
	public void uploadSuccess(String url) {
		// TODO Auto-generated method stub
		ImageBean img = new ImageBean(ImageBean.type_url, url);
		winner.setWinnerspic(img);
		winner.setWinners_images(url);
		showToast("保存成功!");
	}

	@Override
	public void uploadFiald(String msg) {
		// TODO Auto-generated method stub
		showDialog();
	}

}
