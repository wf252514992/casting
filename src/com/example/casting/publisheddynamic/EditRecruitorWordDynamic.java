package com.example.casting.publisheddynamic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
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
 * 发布招募动态界面
 * 
 * @author chenjiaping
 * 
 */

public class EditRecruitorWordDynamic extends BaseForm implements
		OnClickListener {
	/** Called when the activity is first created. */
	// TranslateAnimation showAction, hideAction;
	Animation showAction, hideAction;
	RelativeLayout menu;
	boolean menuShowed;
	public static EditRecruitorWordDynamic self;
	EditText et_id;
	// --表情--//
	int columns = 6, rows = 3, pageExpressionCount = 3 * 6 - 1;
	ViewPager vp_id;
	LinearLayout ll_expression;
	public LinearLayout ll_vp_selected_index;
	ArrayList<GridView> grids;
	MyPagerAdapter myPagerAdapter;
//	TextView tv_chat_title;
//	@TAInjectView(id = R.id.titlelayout)
	View titleLayout;
	private static final int DATE_DIALOG_ID = 1;
	private static final int SHOW_DATAPICK = 0;
	private int yearStr, monthStr, day;
	private TextView signuptime;
	private String op;
	private ImageButton expression;
//	@TAInject
	private AsyncHttpClient asyncHttpClient;
	private ArrayList<String> filelist;
	private GridView imgGridView;
	private RelativeLayout voicerel;
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
	private TextView duration;
//	private static int second=0;
	long time = 0;
	Toas toas;
//	private TextView adress;
//	private ImageView adressimg;
	// private LinearLayout location;
	// private boolean mIsStart;
//	private LocationClient mLocClient;
//	private Vibrator mVibrator01 = null;

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
		initVoice();
//		getAddress();
	}

	
	/**
	 * 
	 * 处理日期控件的Handler
	 */

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case SHOW_DATAPICK:

				showDialog(DATE_DIALOG_ID);

				break;

			}

		}

	};

	private void init() {
		Intent intent = getIntent();
		op = intent.getStringExtra("op");
		if (op != null && op.equals("recruit")) {
			signuptime = (TextView) findViewById(R.id.signuptime);
			signuptime.setVisibility(View.VISIBLE);
			signuptime.setOnClickListener(this);
			Calendar c = Calendar.getInstance();
			yearStr = c.get(Calendar.YEAR); // 获取当前年份
			monthStr = c.get(Calendar.MONTH);// 获取当前月份
			day = c.get(Calendar.DAY_OF_MONTH);// 获取当前月份的日期号码
		}
		et_id = (EditText) findViewById(R.id.editdynamicedit);
		et_id
		.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					// 此处为得到焦点时的处理内容
					ll_expression.setVisibility(View.GONE);
					voicerel.setVisibility(View.GONE);
				} else {
					// 此处为失去焦点时的处理内容
				}
			}
		});
		// 获取编辑框焦点
		et_id.setFocusable(true);
		// 打开软键盘 对于刚跳到一个新的界面就要弹出软键盘的情况上述代码
		// 可能由于界面为加载完全而无法弹出软键盘。此时应该适当的延迟弹出软键盘如998毫秒（保证界面的数据加载完成）
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			public void run() {
				InputMethodManager inputManager = (InputMethodManager) et_id
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(et_id, 0);
			}

		}, 198);
		LinearLayout share = (LinearLayout) findViewById(R.id.share);
		share.setOnClickListener(this);
		ImageButton atsend = (ImageButton) findViewById(R.id.atsend);
		atsend.setOnClickListener(this);
		expression = (ImageButton) findViewById(R.id.expression);
		expression.setOnClickListener(this);

		self = this;

		ll_vp_selected_index = (LinearLayout) findViewById(R.id.ll_vp_selected_index);
		ll_expression = (LinearLayout) findViewById(R.id.ll_expression);
		vp_id = (ViewPager) findViewById(R.id.vp_id);
		vp_id.setOnPageChangeListener(new MyOnPageChangeListener());

		
		et_id.setOnClickListener(this);
		ImageButton btn_photos=(ImageButton) findViewById(R.id.btn_photos);
		btn_photos.setOnClickListener(this);
		ImageButton btn_voice=(ImageButton) findViewById(R.id.btn_voice);
		btn_voice.setOnClickListener(this);
		imgGridView = (GridView) findViewById(R.id.gridView1);
		voicerel=(RelativeLayout) findViewById(R.id.voicerel);
		filelist = new ArrayList<String> ();
//		adress = (TextView) findViewById(R.id.adress);
//		adressimg = (ImageView) findViewById(R.id.adressimg);
		// location=(LinearLayout) findViewById(R.id.location);

	}
	private void initVoice(){
		voicerel=(RelativeLayout) findViewById(R.id.voicerel);
		 voice=(ImageView) findViewById(R.id.voice);
		 duration=(TextView) findViewById(R.id.duration);
//	     Button voice2=(Button) findViewById(R.id.voice2);
//	     voice2.setOnClickListener(this);
	     ImageButton getVoice=(ImageButton) findViewById(R.id.getVoice);
	     
	     getVoice.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				 switch (event.getAction()) {
				
					case MotionEvent.ACTION_DOWN:
						time = System.currentTimeMillis();
						if (toas == null)
						toas = new Toas(EditRecruitorWordDynamic.this);
						// 当用户第一次录音少于三秒会弹出dialog录音然后延迟一秒钟自动关闭
						// 而在这一秒钟用户再次点击录音显示的dialog就会被关闭此时设置取消一秒延迟关闭
						// Log.e("yung", "isShowing:" + toas.isShowing());
						// if (toas.isShowing())
						toas.canceldismiss = true;
						toas.showToas(arg0, R.drawable.traf_ridsound,
								(int) (400 * 1),
								(int) (360 * 1));
//						second=0;
						recorder = new MediaRecorder();
						recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
						recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
						recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
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
//						 TimerTask timerTask=new TimerTask() {  
//							 
//					            @Override  
//					            public void run() {
					            	
					                // TODO Auto-generated method stub   
//					                second++;  
//					                if(second==60){  

//										if (recorder != null) {
//											try {
//												recorder.stop();
//											} catch (RuntimeException stopException) {
//												stopException.printStackTrace();
//												File file = new File(currnetPath);
//												if (file.exists()) {
//													file.delete();
//												}
//											}finally{
//											recorder.release();
//											recorder = null;
////											RelativeLayout getVoiceMessage=(RelativeLayout) findViewById(R.id.getVoiceMessage);
////											getVoiceMessage.setVisibility(View.GONE);
////											RelativeLayout nogetVoiceMessage=(RelativeLayout) findViewById(R.id.nogetVoiceMessage);
////											nogetVoiceMessage.setVisibility(View.VISIBLE);
//											RelativeLayout voicemessage=(RelativeLayout) findViewById(R.id.voicemessage);
//											voicemessage.setVisibility(View.VISIBLE);
//											
//											
//											player = new MediaPlayer();
//											 player.reset();  
//											try{
//											    player.setDataSource(getPath());
//
//											}catch(IOException e){
//											 e.printStackTrace();
//											}
//											player.prepareAsync();
//											player.setOnCompletionListener(new OnCompletionListener() {
//												
//												@Override
//												public void onCompletion(MediaPlayer arg0) {
//													ad.stop();
//													voice.setBackgroundResource(R.drawable.messages_audio_animation_from3);
//												}
//											});
//											}
//										}
//					                }  
					                 
//					            }  
//					        };  
//					         Timer  timer=new Timer();  
//					         timer.schedule(timerTask, 1000,1000); 
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
							}finally{
							recorder.release();
							recorder = null;
//							RelativeLayout getVoiceMessage=(RelativeLayout) findViewById(R.id.getVoiceMessage);
//							getVoiceMessage.setVisibility(View.GONE);
//							RelativeLayout nogetVoiceMessage=(RelativeLayout) findViewById(R.id.nogetVoiceMessage);
//							nogetVoiceMessage.setVisibility(View.VISIBLE);
							RelativeLayout voicemessage=(RelativeLayout) findViewById(R.id.voicemessage);
							voicemessage.setVisibility(View.VISIBLE);
							long length = (System.currentTimeMillis() - time) / 1000;
							duration.setText(length+"''"); 
							
//							handler.sendEmptyMessage(1); 
							
							player = new MediaPlayer();
							 player.reset();  
							try{
							    player.setDataSource(getPath());

							}catch(IOException e){
							 e.printStackTrace();
							}
							player.prepareAsync();
							player.setOnCompletionListener(new OnCompletionListener() {
								
								@Override
								public void onCompletion(MediaPlayer arg0) {
									ad.stop();
									voice.setBackgroundResource(R.drawable.audio3_l);
								}
							});
							}
						}
						break;
					}
                 return true;
         }
		});
	     LinearLayout voiceimg=(LinearLayout) findViewById(R.id.voiceimg);
	     voiceimg.setOnClickListener(this);
	}

	private String getPath() {

		if (!dir.exists()) {
			dir.mkdirs();
		}

		File path = new File(dir, name + ".wav");
		return path.getAbsolutePath();
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

	@Override
	public void LeftButtonClick() {
		finish();
	}

	@Override
	public void RightButtonClick() {
		
		String content = et_id.getText().toString();
		 TextView shareRange = (TextView) findViewById(R.id.shareRange);
	     // 公开0，我关注的人1，仅自己可见2
		String shareRangeStr=shareRange.getText().toString();
		if (op != null && op.equals("recruit")) {
			if (signuptime.getText().toString().equals("未设置截止日期")) {
				Toast.makeText(this, "请设置截止日期！", Toast.LENGTH_LONG).show();
				return;
			}
		}
		
		 
	        
		if (filelist != null && filelist.size() > 0) {
			//图片动态上传
			UploadTool tool=new UploadTool(this,filelist);
			if(op==null){
				tool.setConfig(shareRangeStr, content,"word","");	
			}else{
				tool.setConfig(shareRangeStr, content,op,signuptime.getText().toString());
			}
             		
			 tool.execute();
		}else if(currnetPath!=null){
			 File file = new File(currnetPath);
			 if(file.exists()){
					//语音动态上传
					uploadVoice();
				}
		}else{
			
		if (content.trim().equals("")) {
			Toast.makeText(this, "请输入招募信息！", Toast.LENGTH_LONG).show();
			et_id.setFocusable(true);
			et_id.setFocusableInTouchMode(true);
			return;
		}
		
		Dynamic dy = new Dynamic();
		dy.setContent(content);
//		dy.setLocation(adress.getText().toString());
		dy.setLocation("");
	   
		if(shareRangeStr.equals("公开")){
			dy.setOpen_permissions("0");
		}else if(shareRangeStr.equals("我关注的人")){
			dy.setOpen_permissions("1");
		}else if(shareRangeStr.equals("仅自己可见")){
			dy.setOpen_permissions("2");
		}
		if (op != null && op.equals("recruit")) {
			dy.setUpToDate(signuptime.getText().toString());
			dy.setType("1");// 动态类型:普通动态和招募动态,值：0,1
		} else {
			dy.setType("0");
		}
		String id=Session.getInstance().getUser_id();
		add(dy, id);// TODO
		}
	}
	private void uploadVoice(){
        	
    		String id=Session.getInstance().getUser_id();
    		Dynamic dy=new Dynamic();
            dy.setFileType("voice");//文件类型,值：image,video,voice
    		String content=et_id.getText().toString();
            dy.setContent(content);
//            dy.setLocation(adress.getText().toString());
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
			if (op != null && op.equals("recruit")) {
				dy.setUpToDate(signuptime.getText().toString());
				dy.setType("1");// 动态类型:普通动态和招募动态,值：0,1
			} else {
				dy.setType("0");
			}//动态类型:普通动态和招募动态,值：0,1
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
									addVoice(dy,id);
								} catch (JSONException e) {
									dismissDialog();
									Toast.makeText(EditRecruitorWordDynamic.this, "发布失败，请稍后重试！", Toast.LENGTH_LONG).show();
//									e.printStackTrace();
								}
							}

						});
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
	 * 发布动态
	 * 
	 * @param dy
	 * @param id
	 */
	private void add(Dynamic dy, String id) {
		showWaitDialog();
		JSONObject params = new JSONObject();
		RequestParams param = new RequestParams();
		try {
			params.put("id", id);// params.put("id", "1");
			params.put("content", dy.getContent());
			String location=dy.getLocation();
			if(location.equals("显示位置")){
				params.put("location","");
			}else{
				params.put("location", dy.getLocation());
			}
			params.put("open_permissions", dy.getOpen_permissions());
			params.put("type", dy.getType());// params.put("type", "1");
			if (op != null && op.equals("recruit")) {
				params.put("up_to_date", dy.getUpToDate());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		param.put("str", params.toString());
		param.put("response", "application/json");
		asyncHttpClient.post(Server_path.addDynamicManage, param,
				new AsyncHttpResponseHandler() {

					public void onSuccess(String content) {
						super.onSuccess(content);
						dismissDialog();
						// TODO showWebView(content);
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
//							String dynamic_id = jsonResult.getString("dynamic_id");
							String row = jsonResult.getString("row");
							if(row!=null&&row.equals("1")){
							Toast.makeText(EditRecruitorWordDynamic.this, "恭喜您发布成功！",
									Toast.LENGTH_LONG).show();
							// Intent intent=new
							// Intent(EditPhotoDynamic.this,MainTabNew.class);
							// startActivity(intent);
							finish();
						} else {
							Toast.makeText(EditRecruitorWordDynamic.this,
									"发布失败，请稍后重试！", Toast.LENGTH_LONG).show();
						}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

				});
	}
	 /**
     * 发布动态 
     * @param dy
     * @param id
     */
	private void addVoice(Dynamic dy,String id) {
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
							Toast.makeText(EditRecruitorWordDynamic.this, "恭喜您发布成功！",
									Toast.LENGTH_LONG).show();
							// Intent intent=new
							// Intent(EditPhotoDynamic.this,MainTabNew.class);
							// startActivity(intent);
							finish();
						} else {
							Toast.makeText(EditRecruitorWordDynamic.this,
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
				voicerel.setVisibility(View.GONE);
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
		case R.id.signuptime:
			Message msg = new Message();
			msg.what = SHOW_DATAPICK;
			handler.sendMessage(msg);

			break;
		case R.id.btn_photos:
			if(currnetPath!=null){
				File file = new File(currnetPath);
				 if(file.exists()){
						Toast.makeText(EditRecruitorWordDynamic.this, "语音或图片，您只能选择其中一种上传！", Toast.LENGTH_LONG).show();
						return;
				 }
			}
			intent=new Intent(this,ImgsActivity.class);
			intent.putExtra("action", "add");
            startActivityForResult(intent,2);
			break;
		case R.id.btn_voice:
			if (filelist != null && filelist.size() > 0) {
				Toast.makeText(EditRecruitorWordDynamic.this, "语音或图片，您只能选择其中一种上传！", Toast.LENGTH_LONG).show();
				return;
			}
			if(voicerel.getVisibility() == View.GONE){
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(this.getCurrentFocus()
						.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				voicerel.setVisibility(View.VISIBLE);
				ll_expression.setVisibility(View.GONE);
			}else if(voicerel.getVisibility() == View.VISIBLE){
				voicerel.setVisibility(View.GONE);
				ll_expression.setVisibility(View.GONE);
			}
			

			
			break;
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
		// case R.id.location:
		//
		// if (!mIsStart) {
		// adressimg.setImageResource(R.drawable.release_locationselected);
		// setLocationOption();
		// mLocClient.start();
		// mIsStart = true;
		//
		// } else {
		// adressimg.setImageResource(R.drawable.release_location);
		// mLocClient.stop();
		// mIsStart = false;
		// adress.setText("显示位置");
		//
		// }
		// break;
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
		if (resultCode == Activity.RESULT_OK && requestCode == 2) {
			if(filelist.size()!=0){
				filelist.remove(filelist.size()-1);
			}
			ArrayList<String> addfilelist =data.getStringArrayListExtra("filelist");
			for (int i = 0; i < addfilelist.size(); i++) {
				filelist.add(addfilelist.get(i));
			}
			filelist.add("");
			if (filelist != null && filelist.size() > 1) {
				ImgsAdapter imgsAdapter = new ImgsAdapter(this, filelist, null);
				imgsAdapter.setAction("edit",imgGridView);
				imgGridView.setVisibility(View.VISIBLE);
				imgGridView.setAdapter(imgsAdapter);
			}
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
								ImageSpan imageSpan = new ImageSpan(EditRecruitorWordDynamic.this,e.drableId);
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

	/**
	 * 
	 * 更新日期
	 */

	private void updateDisplay() {
		String str = new StringBuilder().append(yearStr).append("-").append(

		(monthStr + 1) < 10 ? "0" + (monthStr + 1) : (monthStr + 1))
				.append("-").append(

				(day < 10) ? "0" + day : day).toString();
		signuptime.setText(str);
	}

	/**
	 * 
	 * 日期控件的事件
	 */

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,

		int dayOfMonth) {

			yearStr = year;

			monthStr = monthOfYear;

			day = dayOfMonth;

			updateDisplay();

		}

	};

	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {

		case DATE_DIALOG_ID:

			return new DatePickerDialog(this, mDateSetListener, yearStr,
					monthStr,

					day);
		}

		return null;

	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {

		switch (id) {

		case DATE_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(yearStr, monthStr, day);
			break;

		}

	}
	 @Override  
	    protected void onDestroy() { 
	        if(player!=null&&player.isPlaying()){  
	            player.stop();  
	            player.release(); 
	        }  
	        
	        super.onDestroy();  
	    }  
	 class UploadTool extends AsyncTask<String, Integer, String> {

		    private  ArrayList<String> filelist;
		    private AsyncHttpClient asyncHttpClient;
		    private Context context;
		    private String shareRangeStr,content,op,signuptimeStr;
		    public UploadTool(Context context,ArrayList<String> filelist){
		    	this.filelist=filelist;
		    	this.context=context;
		    	asyncHttpClient = new AsyncHttpClient();
		    }
		    public void setConfig(String shareRangeStr,String content,String op,String signuptimeStr){
		    	this.shareRangeStr=shareRangeStr;
		    	this.content=content;
		    	this.op=op;
		    	this.signuptimeStr=signuptimeStr;
		    }
			protected String doInBackground(String... params) {

				JSONArray array = new JSONArray();

				if (filelist != null && filelist.size() > 0) {
					for (int i = 0; i < filelist.size() - 1; i++) {
						File outputFile = new File(Server_path.SavePath
								+ filelist.get(i).substring(
										filelist.get(i).lastIndexOf("/") + 1));
						String path = null;

						try {

							path = PicTool.compressImage(context,
									filelist.get(i), Server_path.SavePath, outputFile);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}

						if (path != null) {
							JSONObject obj = new JSONObject();
							try {
								obj.put("type", "jpg");
								obj.put("content", encodeBase64File(path));
							} catch (JSONException e) {
								e.printStackTrace();
							} catch (Exception e) {
								e.printStackTrace();
							}
							array.put(obj);
							// File file=new File(path);
							// paths.put(filelist.get(i).substring(filelist.get(i).lastIndexOf("/")+1),
							// file);
						}
					}

				}
				
			
				return array.toString();
			}

			@Override
			protected void onPreExecute() {
				showWaitDialog();
			}

			protected void onPostExecute(String result) {

				if (result != null) {
					String id = Session.getInstance().getUser_id();
					Dynamic dy = new Dynamic();
					dy.setFileType("image");// 文件类型,值：image,video,voice
//					dy.setLocation(adress.getText().toString());
					dy.setLocation("");
					// 公开0，我关注的人1，仅自己可见2
					if(shareRangeStr.equals("公开")){
						dy.setOpen_permissions("0");
					}else if(shareRangeStr.equals("我关注的人")){
						dy.setOpen_permissions("1");
					}else if(shareRangeStr.equals("仅自己可见")){
						dy.setOpen_permissions("2");
					}
					
					dy.setContent(content);
					if (op != null && op.equals("recruit")) {
						dy.setUpToDate(signuptimeStr);
						dy.setType("1");// 动态类型:普通动态和招募动态,值：0,1
					} else {
						dy.setType("0");
					}
					// 动态类型:普通动态和招募动态,值：0,1

					if (filelist != null && filelist.size() > 1) {
		                JSONArray array = null;
						try {
							array = new JSONArray(result);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						upload(id, array, dy);
					}
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
			public  String encodeBase64File(String path) throws Exception {
				File file = new File(path);
				FileInputStream inputFile = new FileInputStream(file);
				byte[] buffer = new byte[(int) file.length()];
				inputFile.read(buffer);
				inputFile.close();
				return Base64.encodeToString(buffer, Base64.DEFAULT);
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
									// 文件类型,值：image,video,voice
									if (dy.getFileType() != null
											&& dy.getFileType().equals("image")) {
										dy.setImageUrl(url);
									} else if (dy.getFileType() != null
											&& dy.getFileType().equals("video")) {
										dy.setVideoUrl(url);
									} else if (dy.getFileType() != null
											&& dy.getFileType().equals("voice")) {
										dy.setVoiceUrl(url);
									}
									add(dy, id);
								} catch (JSONException e) {
									dismissDialog();
									Toast.makeText(context,
											"发布失败，请稍后重试！", Toast.LENGTH_LONG).show();
									// e.printStackTrace();
								}
							}

						});
			}
			/**
			 * 发布动态
			 * 
			 * @param dy
			 * @param id
			 */
			private void add(Dynamic dy, String id) {
				JSONObject params = new JSONObject();
				RequestParams param = new RequestParams();
				try {
					params.put("id", id);// params.put("id", "1");
					params.put("content", dy.getContent());
					String location=dy.getLocation();
					if(location.equals("显示位置")){
						params.put("location","");
					}else{
						params.put("location", dy.getLocation());
					}
					params.put("open_permissions", dy.getOpen_permissions());
					params.put("type", dy.getType());// params.put("type", "1");
					if (dy.getImageUrl() != null) {
						params.put("file_type", "image");
						params.put("s_image_url", dy.getImageUrl());
					}
					if (dy.getVideoUrl() != null) {
						params.put("file_type", "video");
						params.put("video_url", dy.getVideoUrl());
					}
					if (dy.getVoiceUrl() != null) {
						params.put("file_type", "voice");
						params.put("voice_url", dy.getVoiceUrl());
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				param.put("str", params.toString());
				// param.put("response", "application/json");
				asyncHttpClient.post(Server_path.addDynamicManage, param,
						new AsyncHttpResponseHandler() {

							public void onSuccess(String content) {
								super.onSuccess(content);
								dismissDialog();
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
//									String dynamic_id = jsonResult.getString("dynamic_id");
									String row = jsonResult.getString("row");
									if(row!=null&&row.equals("1")){
									Toast.makeText(EditRecruitorWordDynamic.this, "恭喜您发布成功！",
											Toast.LENGTH_LONG).show();
									// Intent intent=new
									// Intent(EditPhotoDynamic.this,MainTabNew.class);
									// startActivity(intent);
									finish();
								} else {
									Toast.makeText(EditRecruitorWordDynamic.this,
											"发布失败，请稍后重试！", Toast.LENGTH_LONG).show();
								}
								} catch (JSONException e) {
									e.printStackTrace();
								}

							}

						});
			}
			Dialog dialog;

			/**
			 * 等待框show
			 */
			public void showWaitDialog() {
				if (dialog == null) {
					dialog = CommonDialog.ProgressDialog(context);

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
		}
}
