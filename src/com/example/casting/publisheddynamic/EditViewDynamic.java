package com.example.casting.publisheddynamic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore.Video.Thumbnails;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import cn.smssdk.gui.CommonDialog;
import com.example.casting.MainTab;
import com.example.casting.RelationalshipActivity;
import com.example.casting.entity.Dynamic;
import com.example.casting.login.BaseForm;
import com.example.casting.util.PicTool;
import com.example.casting.util.Server_path;
import com.example.casting.util.Session;
import com.example.casting_android.R;
import com.example.casting_android.bean.Expression;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.RequestParams;

/**
 * 发布图片动态界面
 * @author chenjiaping
 * 
 */

public class EditViewDynamic extends BaseForm implements OnClickListener{
    private String  uri;
//    @TAInjectView(id = R.id.titlelayout)
	View titleLayout;
    private File file;
//    @TAInject
   	private AsyncHttpClient asyncHttpClient;
   	
 // --表情--//
 	int columns = 6, rows = 3, pageExpressionCount = 3 * 6 - 1;
 	ViewPager vp_id;
 	LinearLayout ll_expression;
 	public LinearLayout ll_vp_selected_index;
 	ArrayList<GridView> grids;
 	MyPagerAdapter myPagerAdapter;
 	
// 	private TextView adress;
//	private ImageView adressimg;
	// private LinearLayout location;
	// private boolean mIsStart;
//	private LocationClient mLocClient;
//	private Vibrator mVibrator01 = null;
	private EditText et_id;
	private ImageButton expression;
	private ImageButton ib_delete;
	private RelativeLayout videomessage;
	private ImageView video;
	private ImageButton op;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.publisheddynamicedit);
		titleLayout=findViewById(R.id.titlelayout);
		initView(titleLayout);
		setLeftButtonAble(true, "返回");
		setRightButtonAble(true, "确定");
		setTitle("发动态");
		asyncHttpClient=new AsyncHttpClient();
		init();
//		getAddress();
	}
	@SuppressLint("NewApi")
	private void init(){
	      Intent intent=getIntent();
	      uri=intent.getStringExtra("uri");
	      video=(ImageView) findViewById(R.id.video);
	      videomessage=(RelativeLayout) findViewById(R.id.videomessage);
	      videomessage.setVisibility(View.VISIBLE);
			
	    		file=new File(uri);
	    		if(file.exists()){
	    			// 获取视频的缩略图  
	    	    	Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(uri,Thumbnails.MINI_KIND); 
//	    	    	ThumbnailUtils.
	    	    	int degree = PicTool.readPictureDegree(uri);

					if(degree!=0){//旋转照片角度
						bitmap=PicTool.rotateBitmap(bitmap,degree);
					}
	    	        bitmap = ThumbnailUtils.extractThumbnail(bitmap, 200, 200,  
	    	                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);  
	    	        video.setImageBitmap(bitmap);
	    		}
	    	
	    	op=(ImageButton) findViewById(R.id.op);
	    	op.setOnClickListener(this);
		    et_id=(EditText) findViewById(R.id.editdynamicedit);
		    et_id
			.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						// 此处为得到焦点时的处理内容
						ll_expression.setVisibility(View.GONE);
					} else {
						// 此处为失去焦点时的处理内容
					}
				}
			});
//		// 获取编辑框焦点
//			editText.setFocusable(true);
//			//打开软键盘   对于刚跳到一个新的界面就要弹出软键盘的情况上述代码
////			可能由于界面为加载完全而无法弹出软键盘。此时应该适当的延迟弹出软键盘如998毫秒（保证界面的数据加载完成）
//			Timer timer = new Timer();  
//		     timer.schedule(new TimerTask()  
//		     {  
//		           
//		         public void run()  
//	 	         {  
//		             InputMethodManager inputManager =  
//		                 (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
//		              inputManager.showSoftInput(editText, 0);  
//		         }  
//		           
//		     },  
//		         198);  
		     LinearLayout share=(LinearLayout) findViewById(R.id.share);
		     share.setOnClickListener(this);
		     ImageButton atsend = (ImageButton) findViewById(R.id.atsend);
				atsend.setOnClickListener(this);
				expression = (ImageButton) findViewById(R.id.expression);
				expression.setOnClickListener(this);
				ll_vp_selected_index = (LinearLayout) findViewById(R.id.ll_vp_selected_index);
				ll_expression = (LinearLayout) findViewById(R.id.ll_expression);
				vp_id = (ViewPager) findViewById(R.id.vp_id);
				vp_id.setOnPageChangeListener(new MyOnPageChangeListener());

				
				et_id.setOnClickListener(this);
				ib_delete=(ImageButton) findViewById(R.id.ib_delete);
				ib_delete.setOnClickListener(this);
				ImageButton btn_photos=(ImageButton) findViewById(R.id.btn_photos);
				btn_photos.setVisibility(View.GONE);
				ImageButton btn_voice=(ImageButton) findViewById(R.id.btn_voice);
				btn_voice.setVisibility(View.GONE);
//				adress = (TextView) findViewById(R.id.adress);
//				adressimg = (ImageView) findViewById(R.id.adressimg);
		    
	  }
//	private void getAddress() {
//		// mIsStart = false;
//
//		mLocClient = CastingApplication.mLocationClient;
//		CastingApplication.mTv = adress;
//		mVibrator01 = (Vibrator) getApplication().getSystemService(
//				Service.VIBRATOR_SERVICE);
//		CastingApplication.mVibrator01 = mVibrator01;
//		// location.setOnClickListener(this);
//		if (!ConnectionUtil.isNetworkAvailable(getApplicationContext())) {
//			adressimg.setImageResource(R.drawable.release_location);
//		} else {
//			adressimg.setImageResource(R.drawable.release_locationselected);
//		}
//		setLocationOption();
//		mLocClient.start();
//		// mIsStart = true;
//		if (mLocClient != null && mLocClient.isStarted()) {
//			setLocationOption();
//			mLocClient.requestLocation();
//		}
//		setLocationOption();
//		mLocClient.requestPoi();
//
//	}

//	private void setLocationOption() {
//		LocationClientOption option = new LocationClientOption();
//		option.setOpenGps(true);
//		option.setServiceName("com.baidu.location.service_v2.9");
//		option.setPoiExtraInfo(true);
//		option.setAddrType("all");
//		option.setPriority(LocationClientOption.GpsFirst);
//		option.setPoiNumber(10);
//		option.disableCache(true);
//		mLocClient.setLocOption(option);
//	}
//	public static String formatDuring(long mss) {
//		 
////		  long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
//		  long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
//		  long seconds = (mss % (1000 * 60)) / 1000;
////		  String hoursStr;
//		  String minutesStr;
//		  String secondsStr;
////		  if(hours>=10){
////			  hoursStr=hours+"";
////		  }else{
////			  hoursStr="0"+hours;
////		  }
//		  if(minutes>=10){
//			  minutesStr=minutes+"";
//		  }else{
//			  minutesStr="0"+minutes;
//		  }
//		  if(seconds>=10){
//			  secondsStr=seconds+"";
//		  }else{
//			  secondsStr="0"+seconds;
//		  }
//		  return   minutesStr + ":"
//		    + secondsStr;
//		}
	@Override
	public void LeftButtonClick() {
		finish();
	}

	@Override
	public void RightButtonClick() {
		//TODO
	        if(file.exists()){
				String id=Session.getInstance().getUser_id();
				Dynamic dy=new Dynamic();
		        dy.setFileType("video");//文件类型,值：image,video,voice
		        EditText editdynamicedit=(EditText) findViewById(R.id.editdynamicedit);
				String content=editdynamicedit.getText().toString();
		        dy.setContent(content);
//		        dy.setLocation(adress.getText().toString());
		        dy.setLocation("");
			    TextView shareRange = (TextView) findViewById(R.id.shareRange);
			    // 公开0，我关注的人1，仅自己可见2
				String shareRangeStr=shareRange.getText().toString();
				if(shareRangeStr.equals("公开")){
					dy.setOpen_permissions("0");
				}else if(shareRangeStr.equals("我关注的人")){
					dy.setOpen_permissions("1");
				}else if(shareRangeStr.equals("仅自己可见")){
					dy.setOpen_permissions("2");
				}
		        dy.setType("0");//动态类型:普通动态和招募动态,值：0,1
//				[ { "type": "后缀名", "content": "图片转Base64" },
//				  { "type": "后缀名", "content": "图片转Base64" } ] }
	            JSONArray array=new JSONArray();
	            JSONObject obj=new JSONObject();
	            try {
					obj.put("type", "mp4");
					obj.put("content", encodeBase64File(uri));
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
	            array.put(obj);
	           upload(id, array, dy);
		        }
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
	public static String encodeBase64File(String path) throws Exception {
		File file = new File(path);
		FileInputStream inputFile = new FileInputStream(file);
		byte[] buffer = new byte[(int) file.length()];
		inputFile.read(buffer);
		inputFile.close();
		return Base64.encodeToString(buffer, Base64.DEFAULT);
	}
	public static byte[] bmp2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 60, baos);
		return baos.toByteArray();
	}
	public static String Bitmap2base64(Bitmap bmp) {
		byte[] bts = bmp2Bytes(bmp);
		return Base64.encodeToString(bts, Base64.DEFAULT);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.share:
			Intent intent = new Intent(this, ShareRangeActivity.class);
			TextView shareRange = (TextView) findViewById(R.id.shareRange);
			intent.putExtra("shareRange", shareRange.getText().toString());
			startActivityForResult(intent, 0);
			break;
		case R.id.atsend:
			intent = new Intent(this, RelationalshipActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.expression:
			if (ll_expression.getVisibility() == View.GONE) {
				expression.setBackgroundResource(R.drawable.keyborad_btn);
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(this.getCurrentFocus()
								.getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				ll_expression.setVisibility(View.VISIBLE);
			} else {
				expression.setBackgroundResource(R.drawable.express_btn);
				ll_expression.setVisibility(View.GONE);
			}
			break;
		case R.id.editdynamicedit:
			if (ll_expression.getVisibility() == View.VISIBLE) {
				ll_expression.setVisibility(View.GONE);
			}
			break;
		case R.id.op:
			intent=new Intent(this,VideoPlayActivity.class);
			intent.putExtra("uri", uri);
			startActivity(intent);
			break;
		case R.id.ib_delete:
			if(file.exists()){
				file.delete();
				video.setVisibility(View.INVISIBLE);
				videomessage.setBackgroundResource(R.drawable.add_btn);
				op.setVisibility(View.GONE);
				ib_delete.setVisibility(View.GONE);
				videomessage.setOnClickListener(this);
			}
			break;
		case R.id.videomessage:
			intent=new Intent(this,RecordVideo.class);
            startActivity(intent);
            finish();
			break;
		default:
			break;
		}
	}
	Dialog dialog;
	/**
	 * 等待框show
	 */
	public void showWaitDialog() {
		if (dialog == null) {
			dialog = CommonDialog.ProgressDialog(this);

			// dialog = new ProgressDialog(this);
			// dialog.setCancelable(false);
		}
		if (!dialog.isShowing())
			dialog.show();
	}

	/**
	 * 等待框 dismiss
	 */
	public void dismissDialog() {
		if (dialog != null && dialog.isShowing())
			dialog.dismiss();
	}
	/**
	 * 上传文件请求,用户上传文件，文件限制为"jpg,gif,png,mp4,wav"。
	 * 
	 * @param id
	 * @param file
	 * @param dy
	 *            Dynamic dy=new Dynamic(); dy.setIsForwarding(true);
	 *            dy.setForwardingId("1");
	 *            dy.setForwarding_content("此条动态是转发动态");
	 *            dy.setFileType("image");//文件类型,值：image,video,voice
	 *            dy.setContent("现在发布动态");
	 *            dy.setType("0");//动态类型:普通动态和招募动态,值：0,1
	 */
	private void upload(final String id, JSONArray file, final Dynamic dy) {
		showWaitDialog();
		JSONObject params = new JSONObject();
		RequestParams param = new RequestParams();
		// str - json字符串,例:{ "file":
		// [ { "type": "后缀名", "content": "图片转Base64" },
		// { "type": "后缀名", "content": "图片转Base64" } ] }
		try {
			params.put("id", id);// params.put("id", "1");
			params.put("file", file);
			param.put("str", params.toString());
			param.put("response", "application/json");
		} catch (Exception e) {
			e.printStackTrace();
		}
		asyncHttpClient.post(Server_path.uploadFile, param,
				new AsyncHttpResponseHandler() {

					public void onSuccess(String content) {
						super.onSuccess(content);
						String action = null;
						try {
							JSONObject jsonResult = new JSONObject(content);
							action = jsonResult.getString("return");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						byte bitmapArray[] = Base64.decode(action, 0);
						try {
							JSONObject jsonResult = new JSONObject(new String(
									bitmapArray));
							String url = jsonResult.getString("url");
							String s_image_url = jsonResult.getString("s_image_url");
							// 文件类型,值：image,video,voice
							if (dy.getFileType() != null
									&& dy.getFileType().equals("image")) {
//								dy.setImageUrl(url);
								dy.setSImageUrl(s_image_url);
							} else if (dy.getFileType() != null
									&& dy.getFileType().equals("video")) {
								dy.setVideoUrl(url);
								dy.setSImageUrl(s_image_url);
							} else if (dy.getFileType() != null
									&& dy.getFileType().equals("voice")) {
								dy.setVoiceUrl(url);
							}
							add(dy, id);
						} catch (JSONException e) {
							dismissDialog();
							Toast.makeText(EditViewDynamic.this,
									"发布失败，请稍后重试！", Toast.LENGTH_LONG).show();
							// e.printStackTrace();
						}
					}

				});
	}
	 /**
     * 发布动态 
     * @param dy
     * @param id
     */
	private void add(final Dynamic dy,final String id) {
		JSONObject params = new JSONObject();
		RequestParams param = new RequestParams();
		try {
			params.put("id", id);//params.put("id", "1");
			params.put("content", dy.getContent());
			String location=dy.getLocation();
			if(location.equals("显示位置")){
				params.put("location","");
			}else{
				params.put("location", dy.getLocation());
			}
			params.put("open_permissions", dy.getOpen_permissions());
			params.put("type", dy.getType());//params.put("type", "1");
			if (dy.getImageUrl() != null) { 
				params.put("file_type", "image");
				params.put("image_url", dy.getImageUrl());
			}
			if (dy.getVideoUrl() != null) { 
				params.put("file_type", "video");
				params.put("s_image_url", dy.getVideoUrl());
			}
			if (dy.getVoiceUrl() != null) { 
				params.put("file_type", "voice");
				params.put("voice_url", dy.getVoiceUrl());
			}
//			if (dy.getIsForwarding()) {
//				params.put("is_forwarding", "1");
//				params.put("forwarding_id", dy.getForwardingId());
//				params.put("forwarding_content", dy.getForwarding_content());
//			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		param.put("str", params.toString());
		param.put("response", "application/json");
		asyncHttpClient.post(
				Server_path.addDynamicManage,
				param, new AsyncHttpResponseHandler() {

					public void onSuccess(String content) {
						super.onSuccess(content);
						//TODO 发送视频缩略图
						String action = null;
						try {
							JSONObject jsonResult = new JSONObject(
									content);
							action = jsonResult.getString("return");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						byte bitmapArray[] = Base64.decode(action, 0);
						try {
							JSONObject jsonResult = new JSONObject(new String(
									bitmapArray));
							String dynamic_id = jsonResult.getString("dynamic_id");
							String row = jsonResult.getString("row");
							if(row!=null&&row.equals("1")){
								// 获取视频的缩略图  
				    	    	Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(uri,Thumbnails.MINI_KIND); 
//				    	    	ThumbnailUtils.
				    	    	int degree = PicTool.readPictureDegree(uri);

								if(degree!=0){//旋转照片角度
									bitmap=PicTool.rotateBitmap(bitmap,degree);
								}
				    	        bitmap = ThumbnailUtils.extractThumbnail(bitmap, 200, 200,  
				    	                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);  
				    	        
								uploadVideoPic(id, Bitmap2base64(bitmap), dynamic_id);
							   }else{
								dismissDialog();
								Toast.makeText(EditViewDynamic.this, "发布失败，请稍后重试！", Toast.LENGTH_LONG).show();
							   }
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
					}

				});
	}
	private void uploadVideoPic(final String id, String file, String dynamic_id) {
		JSONObject params = new JSONObject();
		RequestParams param = new RequestParams();
		// str - json字符串,例:{ "file":
		// [ { "type": "后缀名", "content": "图片转Base64" },
		// { "type": "后缀名", "content": "图片转Base64" } ] }
		try {
			params.put("id", id);// params.put("id", "1");
			params.put("dynamic_id", dynamic_id);
			params.put("type","jpg");
			params.put("imageStr", file);
			param.put("str", params.toString());
			param.put("response", "application/json");
		} catch (Exception e) {
			e.printStackTrace();
		}
		asyncHttpClient.post(Server_path.uploadVideoFile, param,
				new AsyncHttpResponseHandler() {

					public void onSuccess(String content) {
						dismissDialog();
						super.onSuccess(content);
						String action = null;
						try {
							JSONObject jsonResult = new JSONObject(
									content);
							action = jsonResult.getString("return");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						byte bitmapArray[] = Base64.decode(action, 0);
							JSONObject jsonResult;
							try {
								jsonResult = new JSONObject(new String(
										bitmapArray));
								String state = jsonResult.getString("state");
								if(state!=null&&state.equals("success")){
									Toast.makeText(EditViewDynamic.this, "恭喜您发布成功！", Toast.LENGTH_LONG).show();

									finish();
								
								}else{
									Toast.makeText(EditViewDynamic.this, "发布失败，请稍后重试！", Toast.LENGTH_LONG).show();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
							
						
					    
					}

				});
	}
	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && requestCode == 0) {
				String str = data.getStringExtra("shareRange");// str即为回传的值
				TextView shareRange = (TextView) findViewById(R.id.shareRange);
				shareRange.setText(str);
				ImageView shareRangeimg=(ImageView) findViewById(R.id.shareRangeimg);
				shareRangeimg.setImageResource(R.drawable.release_globeselected);
		}
		if (resultCode == Activity.RESULT_OK && requestCode == 1) {
			String str = data.getStringExtra("Name");// str即为回传的值
			et_id.append("@" + str+" ");
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (null == myPagerAdapter) {
			List<List<Expression>> lists = initGridViewData();// 填充GridView数据
			grids = new ArrayList<GridView>();
			int gv_padding_lr = (int) getResources().getDimension(
					R.dimen.chat_gv_padding_lr);
			int gv_padding_bt = (int) getResources().getDimension(
					R.dimen.chat_gv_padding_bt);
			int gv_spacing = (int) getResources().getDimension(
					R.dimen.chat_gv_spacing);
			int chat_dot_margin_lr = (int) getResources().getDimension(
					R.dimen.chat_dot_margin_lr);
			int chat_dot_wh = (int) getResources().getDimension(
					R.dimen.chat_dot_wh);
			for (int i = 0; i < lists.size(); i++) {
				List<Expression> l = lists.get(i);
				if (null != l) {
					// --生成当前GridView--//
					final GridView gv = new GridView(this);
					gv.setLayoutParams(new LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
					gv.setNumColumns(columns);
					gv.setGravity(Gravity.CENTER);
					gv.setPadding(gv_padding_lr, gv_padding_bt, gv_padding_lr,
							0);
					gv.setHorizontalSpacing(gv_spacing);
					gv.setVerticalSpacing(gv_spacing);
					ExpressionImageAdapter expressionImageAdapter = new ExpressionImageAdapter(
							this, l);
					gv.setAdapter(expressionImageAdapter);
					// --点击列表事件处理--//
					gv.setOnItemClickListener(new OnItemClickListener() {
						/*
						 * (non-Javadoc)
						 * 
						 * @see android.widget.AdapterView.OnItemClickListener#
						 * onItemClick(android.widget.AdapterView,
						 * android.view.View, int, long)
						 */
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							Expression e = (Expression) gv
									.getItemAtPosition(arg2);
							Log.v("_____________", "点击表情_" + e.code);
							int index = et_id.getSelectionStart();
							Editable edit = et_id.getEditableText();// 获取EditText的文字
							String content_all = edit.toString();
							String content_forward = content_all.substring(0,
									index);
							Log.v("_____________", "content_all = "
									+ content_all + "|content_forward = "
									+ content_forward + "|");
							String reg = "^[A-Za-z0-9]+$";//TODO  正则表达式
							reg=reg.replace("/",""); 
							if (e.getDrableId() < 0) {// 点击删除按钮
								if (index > 0) {
									boolean delExpression = false;
									Pattern p = Pattern.compile(reg);
									Matcher matcher = p
											.matcher(content_forward);
									// 因为这里表情代码最长为5，所以这里减5
									boolean found = false;
									if (content_forward.length() >= 4) {// 如果光标前字符少于4个，说明不可能为表情
										if (content_forward.length() == 4) {
											found = matcher
													.find(content_forward
															.length() - 4);
										} else {
											found = matcher
													.find(content_forward
															.length() - 5);
										}
										if (found) {
											String flag = matcher.group();
											if (content_forward.substring(
													content_forward.length()
															- flag.length(),
													content_forward.length())
													.equals(flag)) {
												delExpression = true;
												edit.delete(
														index - flag.length(),
														index);
											}
										}
									}
									if (!delExpression) {
										edit.delete(index - 1, index);
									}
								}
							} else {
								ImageSpan imageSpan = new ImageSpan(EditViewDynamic.this,e.drableId);
								SpannableString spannableString = new SpannableString(
										e.code);
								spannableString.setSpan(imageSpan, 0,
										e.code.length(),
										Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

								Log.v("_____________", "spannableString="
										+ spannableString.toString()
										+ "__index=" + index);

								if (index < 0 || index >= edit.length()) {
									edit.append(spannableString);
								} else {
									edit.insert(index, spannableString);
								}
							}
						}
					});
					grids.add(gv);
					// --生成索引图--//
					ImageView iv = new ImageView(this);
					android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(
							chat_dot_wh, chat_dot_wh);
					lp.leftMargin = chat_dot_margin_lr;
					lp.rightMargin = chat_dot_margin_lr;
					iv.setLayoutParams(lp);
					if (i == 0) {
						iv.setBackgroundResource(R.drawable.page_focused);
					} else {
						iv.setBackgroundResource(R.drawable.page_unfocused);
					}
					ll_vp_selected_index.addView(iv);
				}
			}
			myPagerAdapter = new MyPagerAdapter(grids);
			vp_id.setAdapter(myPagerAdapter);
		}

		// tv_chat_title.setText(Html.fromHtml("<img src=\""+2130837523+"\" />",
		// imageGetter_resource, null));
	}


	/**
	 * 填充GridView所需要的数据
	 */
	private List<List<Expression>> initGridViewData() {
		List<List<Expression>> lists = new ArrayList<List<Expression>>();
		List<Expression> list = null;
		for (int i = 0; i < MainTab.expressionList.size(); i++) {
			if (i % pageExpressionCount == 0) {// 一页数据已填充完成
				if (null != list) {
					list.add(new Expression(-1, "backSpace"));// 添加删除键
					lists.add(list);
				}
				list = new ArrayList<Expression>();
			}
			list.add(MainTab.expressionList.get(i));
			// 最后一个表情，并且不是当前页最后一个表情时，后面添加删除键
			if (i >= MainTab.expressionList.size() - 1) {
				list.add(new Expression(-1, "backSpace"));// 添加删除键
				lists.add(list);
			}
		}
		return lists;
	}
}
