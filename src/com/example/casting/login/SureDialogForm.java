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
import android.widget.TextView;
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
 * 确认弹框
 * @author Administrator
 * 
 */
public abstract class SureDialogForm extends BaseForm implements
		OnClickListener {

	public abstract void sure();
	public abstract void cancle();



	protected Button btn_cancle,btn_sure;
	TextView tv_title;
	String title;

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPopView();
	};

	PopupWindow popupWindow;

	public void setPopWindowTitle(String title)
	{
		tv_title.setText(title);
	}
	private void initPopView() {
		View contentView = getLayoutInflater().inflate(R.layout.dialog_delete,
				null);
		popupWindow = new PopupWindow(contentView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		btn_cancle = (Button) contentView.findViewById(R.id.btn_cancle);
		btn_sure = (Button) contentView.findViewById(R.id.btn_sure);
		btn_cancle.setOnClickListener(this);
		btn_sure.setOnClickListener(this);
		tv_title = (TextView) contentView.findViewById(R.id.title);
		tv_title.setText(title);
		popupWindow.setFocusable(true);// 取得焦点
		// 设置点击窗口外边窗口消失
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		/** 设置PopupWindow弹出和退出时候的动画效果 */
		popupWindow.setAnimationStyle(R.style.anim_popup_dir);
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// 设置背景颜色变暗
				sunBackground();
			}
		});
	}
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
	public abstract View getParentView();
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


	public void disPicDialog() {
		if (popupWindow.isShowing()) {
			popupWindow.dismiss();// 关闭
		}
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == R.id.btn_cancle) {
			cancle();
		} else if (arg0.getId() == R.id.btn_sure) {
			sure();
		}
		disPicDialog();
	}

	@Override
	public void LeftButtonClick() {

	}

	@Override
	public void RightButtonClick() {

	}


}
