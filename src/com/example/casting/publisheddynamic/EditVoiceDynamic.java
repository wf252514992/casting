package com.example.casting.publisheddynamic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import cn.smssdk.gui.CommonDialog;
import com.example.casting.MainTab;
import com.example.casting.RelationalshipActivity;
import com.example.casting.entity.Dynamic;
import com.example.casting.login.BaseForm;
import com.example.casting.util.Server_path;
import com.example.casting.util.Session;
import com.example.casting_android.R;
import com.example.casting_android.bean.Expression;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.RequestParams;

/**
 * 发布招募动态界面
 * @author chenjiaping
 *
 */

public class EditVoiceDynamic extends BaseForm implements OnClickListener{
	/** Called when the activity is first created. */
	// TranslateAnimation showAction, hideAction;
    private MediaRecorder recorder;
    private String currnetPath;
    private static final String RECORD_DIR = "casting_record";
	private File dir = new File(Environment.getExternalStorageDirectory(),
			RECORD_DIR);
	private long name =  System.currentTimeMillis();
	private MediaPlayer player;
	private boolean isPause=false;
	private AnimationDrawable ad;
	private ImageView voice;
//	@TAInjectView(id = R.id.titlelayout)
	View titleLayout;
	private TextView duration;
//	private static int second=0;
	long time = 0;
	Toas toas;
//	@TAInject
   	private AsyncHttpClient asyncHttpClient;
   	
 // --表情--//
 	int columns = 6, rows = 3, pageExpressionCount = 3 * 6 - 1;
 	ViewPager vp_id;
 	LinearLayout ll_expression;
 	private ImageButton expression;
 	private RelativeLayout voicerel;
 	private LinearLayout ll_vp_selected_index;
 	ArrayList<GridView> grids;
 	MyPagerAdapter myPagerAdapter;
// 	private TextView adress;
//	private ImageView adressimg;
	// private LinearLayout location;
	// private boolean mIsStart;
//	private LocationClient mLocClient;
//	private Vibrator mVibrator01 = null;
	private EditText et_id;
	private PopupWindow popupregistWindow;
	private LinearLayout voiceimg;
	private RelativeLayout voicemessage;
//	Handler handler=new Handler(){  
//		  
//        @Override  
//        public void handleMessage(Message msg) {  
//            // TODO Auto-generated method stub   
//            super.handleMessage(msg);  
//            duration.setText(second+"''");  
//        }  
//          
//    };  
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editvoice);
		titleLayout=findViewById(R.id.titlelayout);
		initView(titleLayout);
		setLeftButtonAble(true, "返回");
		setRightButtonAble(true, "确定");
		setTitle("发动态");
		asyncHttpClient=new AsyncHttpClient();
		init();
//		getAddress();
	}
	private void init(){
		Intent intent=getIntent();
		String recruit=intent.getStringExtra("recruit");
		  if(recruit!=null&&!recruit.equals("")){
			  TextView signuptime=(TextView) findViewById(R.id.signuptime);
			  signuptime.setVisibility(View.VISIBLE);
		  }
		  et_id=(EditText) findViewById(R.id.editdynamic);
//		// 获取编辑框焦点
//			editText.setFocusable(true);
//			//打开软键盘   对于刚跳到一个新的界面就要弹出软键盘的情况上述代码
////			可能由于界面为加载完全而无法弹出软键盘。此时应该适当的延迟弹出软键盘如998毫秒（保证界面的数据加载完成）
//			Timer timer = new Timer();  
//		     timer.schedule(new TimerTask()  
//		     {  
//		           
//		         public void run()  
//		         {  
//		             InputMethodManager inputManager =  
//		                 (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
//		             inputManager.showSoftInput(editText, 0);  
//		         }  
//		           
//		     },  
//		         198);  
		  voicerel=(RelativeLayout) findViewById(R.id.voicerel);
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
//				adress = (TextView) findViewById(R.id.adress);
//				adressimg = (ImageView) findViewById(R.id.adressimg);
		     voice=(ImageView) findViewById(R.id.voice);
			 duration=(TextView) findViewById(R.id.duration);
//		     Button voice2=(Button) findViewById(R.id.voice2);
//		     voice2.setOnClickListener(this);
		     ImageButton getVoice=(ImageButton) findViewById(R.id.getVoice);
		     
		     getVoice.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View arg0, MotionEvent event) {
					 switch (event.getAction()) {
					
						case MotionEvent.ACTION_DOWN:
							time = System.currentTimeMillis();
							if (toas == null)
							toas = new Toas(EditVoiceDynamic.this);
							// 当用户第一次录音少于三秒会弹出dialog录音然后延迟一秒钟自动关闭
							// 而在这一秒钟用户再次点击录音显示的dialog就会被关闭此时设置取消一秒延迟关闭
							// Log.e("yung", "isShowing:" + toas.isShowing());
							// if (toas.isShowing())
							toas.canceldismiss = true;
							toas.showToas(arg0, R.drawable.traf_ridsound,
									(int) (400 * 1),
									(int) (360 * 1));
//							second=0;
							recorder = new MediaRecorder();
							recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
							recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
							recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
							currnetPath = getPath();
							recorder.setOutputFile(currnetPath);
							try {
								recorder.prepare();
								
							} catch (IllegalStateException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
							recorder.start();
//							 TimerTask timerTask=new TimerTask() {  
//								 
//						            @Override  
//						            public void run() {
						            	
						                // TODO Auto-generated method stub   
//						                second++;  
//						                if(second==60){  

//											if (recorder != null) {
//												try {
//													recorder.stop();
//												} catch (RuntimeException stopException) {
//													stopException.printStackTrace();
//													File file = new File(currnetPath);
//													if (file.exists()) {
//														file.delete();
//													}
//												}finally{
//												recorder.release();
//												recorder = null;
////												RelativeLayout getVoiceMessage=(RelativeLayout) findViewById(R.id.getVoiceMessage);
////												getVoiceMessage.setVisibility(View.GONE);
////												RelativeLayout nogetVoiceMessage=(RelativeLayout) findViewById(R.id.nogetVoiceMessage);
////												nogetVoiceMessage.setVisibility(View.VISIBLE);
//												RelativeLayout voicemessage=(RelativeLayout) findViewById(R.id.voicemessage);
//												voicemessage.setVisibility(View.VISIBLE);
//												
//												
//												player = new MediaPlayer();
//												 player.reset();  
//												try{
//												    player.setDataSource(getPath());
//
//												}catch(IOException e){
//												 e.printStackTrace();
//												}
//												player.prepareAsync();
//												player.setOnCompletionListener(new OnCompletionListener() {
//													
//													@Override
//													public void onCompletion(MediaPlayer arg0) {
//														ad.stop();
//														voice.setBackgroundResource(R.drawable.messages_audio_animation_from3);
//													}
//												});
//												}
//											}
//						                }  
						                 
//						            }  
//						        };  
//						         Timer  timer=new Timer();  
//						         timer.schedule(timerTask, 1000,1000); 
							break;
						case MotionEvent.ACTION_UP:
							toas.canceldismiss = false;
							toas.hand.sendEmptyMessage(0);
					if (recorder != null) {
						try {
							recorder.stop();
						} catch (RuntimeException stopException) {
							stopException.printStackTrace();
							File file = new File(currnetPath);
							if (file.exists()) {
								file.delete();
							}
						} finally {
							recorder.release();
							recorder = null;
							// RelativeLayout getVoiceMessage=(RelativeLayout)
							// findViewById(R.id.getVoiceMessage);
							// getVoiceMessage.setVisibility(View.GONE);
							// RelativeLayout nogetVoiceMessage=(RelativeLayout)
							// findViewById(R.id.nogetVoiceMessage);
							// nogetVoiceMessage.setVisibility(View.VISIBLE);
							long length = (System.currentTimeMillis() - time) / 1000;
							if (length <= 2) {
								Toast.makeText(EditVoiceDynamic.this,
										"亲，语音时长一定要大于2秒钟哦！", Toast.LENGTH_SHORT)
										.show();
								break;
							} else {
								if (length > 60) {
									Toast.makeText(EditVoiceDynamic.this,
											"亲，语音时长不能超过60秒钟哦！",
											Toast.LENGTH_SHORT).show();
									break;
								} else {
									voicemessage = (RelativeLayout) findViewById(R.id.voicemessage);
									voicemessage.setVisibility(View.VISIBLE);
									duration.setText(length + "''");

									// handler.sendEmptyMessage(1);

									player = new MediaPlayer();
									player.reset();
									try {
										player.setDataSource(getPath());

									} catch (IOException e) {
										e.printStackTrace();
									}
									player.prepareAsync();
									player.setOnCompletionListener(new OnCompletionListener() {

										@Override
										public void onCompletion(
												MediaPlayer arg0) {
											ad.stop();
											voice.setBackgroundResource(R.drawable.audio3_l);
										}
									});
								}
							}
						}
					}
							break;
						}
                     return true;
             }
			});
		     voiceimg=(LinearLayout) findViewById(R.id.voiceimg);
		     voiceimg.setOnClickListener(this);
		     voiceimg.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View arg0) {
					
					// 设置背景颜色变暗
					if (popupregistWindow == null) {
						showDialog();
					}
					darkBackground();
					if (!popupregistWindow.isShowing()) {
						/** 设置PopupWindow弹出后的位置 */
						popupregistWindow.showAtLocation(arg0, Gravity.BOTTOM, 0, 0);
					}
					return false;
				}
			});
	  }
	public void darkBackground() {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = .3f;
		getWindow().setAttributes(lp);

	}
	private void showDialog(){
		View contentView = getLayoutInflater().inflate(R.layout.popview_s_pic,
				null);
		popupregistWindow = new PopupWindow(contentView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		popupregistWindow.setFocusable(true);// 取得焦点
		popupregistWindow.setBackgroundDrawable(new BitmapDrawable());
		 //设置点击窗口外边窗口消失 
		popupregistWindow.setOutsideTouchable(true); 
		/** 设置PopupWindow弹出和退出时候的动画效果 */
		popupregistWindow.setAnimationStyle(R.style.anim_popup_dir);
		Button btn_take = (Button) contentView.findViewById(R.id.btn_take);
		Button btn_select = (Button) contentView.findViewById(R.id.btn_select);
		Button btn_cancel = (Button) contentView.findViewById(R.id.btn_cancel);
		btn_take.setText("确定删除录音？");
		btn_select.setText("确定");
		btn_take.setOnClickListener(myClick);
		btn_select.setOnClickListener(myClick);
		btn_cancel.setOnClickListener(myClick);
		popupregistWindow.setOnDismissListener(new OnDismissListener()
		{
			@Override
			public void onDismiss()
			{
				// 设置背景颜色变暗
				sunBackground();
			}
		});
		
	}
	public void dissmissDialog() {
		if (popupregistWindow.isShowing()) {
			popupregistWindow.dismiss();// 关闭
		}
	}
	OnClickListener myClick = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (arg0.getId() == R.id.btn_take) {
				
				
			} else if (arg0.getId() == R.id.btn_select) {
				voicemessage.setVisibility(View.GONE);
				File file = new File(currnetPath);
		        if(file.exists()){
		        	file.delete();
		        }
			}
			dissmissDialog();
		
		}
	};	
	public void sunBackground() {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 1.0f;
		getWindow().setAttributes(lp);
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
	private String getPath() {
		
		if (!dir.exists()) {
			dir.mkdirs();
		}

		File path = new File(dir, name + ".wav");
		return path.getAbsolutePath();
	}
	@Override
	public void LeftButtonClick() {
		finish();
	}

	@Override
	public void RightButtonClick() {
		
        File file = new File(currnetPath);
        if(file.exists()){
        	
    		String id=Session.getInstance().getUser_id();
    		Dynamic dy=new Dynamic();
            dy.setFileType("voice");//文件类型,值：image,video,voice
            EditText editdynamic=(EditText) findViewById(R.id.editdynamic);
    		String content=editdynamic.getText().toString();
            dy.setContent(content);
//            dy.setLocation(adress.getText().toString());
            dy.setLocation("");
            String durationStr=duration.getText().toString();
            dy.setDuration(durationStr.substring(0, durationStr.indexOf("'")));
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
//			[ { "type": "后缀名", "content": "图片转Base64" },
//			  { "type": "后缀名", "content": "图片转Base64" } ] }
            JSONArray array=new JSONArray();
            JSONObject obj=new JSONObject();
            try {
				obj.put("type", "wav");
				obj.put("content", encodeBase64File(currnetPath));
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
	* @author 
	* @param path 文件路径
	* @return
	* @throws Exception
	* @since JDK 1.6
	*/
	public static String encodeBase64File(String path) throws Exception {
	File  file = new File(path);
	FileInputStream inputFile = new FileInputStream(file);
	byte[] buffer = new byte[(int)file.length()];
	inputFile.read(buffer);
	        inputFile.close();
	        return Base64.encodeToString(buffer,Base64.DEFAULT);
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
	 * @param id
	 * @param file
	 * @param dy    Dynamic dy=new Dynamic();
        dy.setIsForwarding(true);
        dy.setForwardingId("1");
        dy.setForwarding_content("此条动态是转发动态");
        dy.setFileType("image");//文件类型,值：image,video,voice
        dy.setContent("现在发布动态");
        dy.setType("0");//动态类型:普通动态和招募动态,值：0,1
	 */
	private void upload(final String id,JSONArray file,final Dynamic dy) {
		showWaitDialog();
		JSONObject params = new JSONObject();
		RequestParams param = new RequestParams();
//		str - json字符串,例:{  "file": 
//			[ { "type": "后缀名", "content": "图片转Base64" },
//			  { "type": "后缀名", "content": "图片转Base64" } ] }
		try {
		params.put("id", id);//params.put("id", "1");
		params.put("file", file);
		param.put("str", params.toString());
		param.put("response", "application/json");
		} catch (Exception e) {
			e.printStackTrace();
		}
		asyncHttpClient
				.post(Server_path.uploadFile,
						param, new AsyncHttpResponseHandler() {

							public void onSuccess(String content) {
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
								try {
									JSONObject jsonResult = new JSONObject(new String(
											bitmapArray));
									String url = jsonResult.getString("url");
									//文件类型,值：image,video,voice
									if(dy.getFileType()!=null&&dy.getFileType().equals("image")){
										dy.setImageUrl(url);
									}else if(dy.getFileType()!=null&&dy.getFileType().equals("video")){
										dy.setVideoUrl(url);
									}else if(dy.getFileType()!=null&&dy.getFileType().equals("voice")){
										dy.setVoiceUrl(url);
									}
									add(dy,id);
								} catch (JSONException e) {
									dismissDialog();
									Toast.makeText(EditVoiceDynamic.this, "发布失败，请稍后重试！", Toast.LENGTH_LONG).show();
//									e.printStackTrace();
								}
							}

						});
	}
	 /**
     * 发布动态 
     * @param dy
     * @param id
     */
	private void add(Dynamic dy,String id) {
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
				params.put("video_url", dy.getVideoUrl());
			}
			if (dy.getVoiceUrl() != null) { 
				params.put("file_type", "voice");
				params.put("voice_url", dy.getVoiceUrl());
			}
			    params.put("duration", dy.getDuration());
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
						dismissDialog();
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
//							String dynamic_id = jsonResult.getString("dynamic_id");
							String row = jsonResult.getString("row");
							if(row!=null&&row.equals("1")){
							Toast.makeText(EditVoiceDynamic.this, "恭喜您发布成功！",
									Toast.LENGTH_LONG).show();
							// Intent intent=new
							// Intent(EditPhotoDynamic.this,MainTabNew.class);
							// startActivity(intent);
							finish();
						} else {
							Toast.makeText(EditVoiceDynamic.this,
									"发布失败，请稍后重试！", Toast.LENGTH_LONG).show();
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
				voicerel.setVisibility(View.GONE);
			} else {
				expression.setBackgroundResource(R.drawable.express_btn);
				ll_expression.setVisibility(View.GONE);
				voicerel.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.editdynamicedit:
			ll_expression.setVisibility(View.GONE);
			voicerel.setVisibility(View.GONE);
//			if (ll_expression.getVisibility() == View.VISIBLE) {
//				ll_expression.setVisibility(View.GONE);
//				voicerel.setVisibility(View.VISIBLE);
//			}
			break;
//		case R.id.voice2:
//			RelativeLayout getVoiceMessage=(RelativeLayout) findViewById(R.id.getVoiceMessage);
//			getVoiceMessage.setVisibility(View.VISIBLE);
//			RelativeLayout nogetVoiceMessage=(RelativeLayout) findViewById(R.id.nogetVoiceMessage);
//			nogetVoiceMessage.setVisibility(View.GONE);
//			break;
		case R.id.voiceimg:
			
			voice.setBackgroundResource(R.drawable.audio_list_l);
			ad = (AnimationDrawable) voice.getBackground();
			if (player.isPlaying() &&!isPause){  
				ad.stop();
                player.pause();  
                isPause = true;  
            }else{  
            	ad.start();
                player.start();  
                isPause = false;  
            }  
			break;
		default:
			break;
		}
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
								ImageSpan imageSpan = new ImageSpan(EditVoiceDynamic.this,e.drableId);
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
	 @Override  
	    protected void onDestroy() { 
	        if(player!=null&&player.isPlaying()){  
	            player.stop();  
	            player.release();  
	        }  
	        super.onDestroy();  
	    }  
}
