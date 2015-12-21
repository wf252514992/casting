package com.example.casting;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Video.Thumbnails;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.gui.CommonDialog;

import com.example.casting.RefreshableView.PullToRefreshListener;
import com.example.casting.ad.SlidingSwitcherView;
import com.example.casting.entity.BaseBean;
import com.example.casting.entity.CommentPraise;
import com.example.casting.entity.Dynamic;
import com.example.casting.entity.RegistBean;
import com.example.casting.listener.ListenerManager;
import com.example.casting.listener.TabHostRefreshListener;
import com.example.casting.login.BaseForm;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.Users.UserGetProcessor;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.publisheddynamic.ImgsAdapter;
import com.example.casting.publisheddynamic.VideoPlayActivity;
import com.example.casting.util.ConstantData;
import com.example.casting.util.CustomHttpClient;
import com.example.casting.util.PicTool;
import com.example.casting.util.Server_path;
import com.example.casting.util.Session;
import com.example.casting.util.view.MyGridView;
import com.example.casting_android.R;
import com.example.casting_android.bean.Expression;
import com.ta.common.TAStringUtils;
import com.ta.util.download.DownLoadCallback;
import com.ta.util.download.DownloadManager;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.RequestParams;

public class MainTab extends BaseForm implements OnClickListener {

	private long lastTime = -1;
	// @TAInjectView(id = R.id.layout_content)
	LinearLayout layout_content;
	// @TAInject
	private AsyncHttpClient asyncHttpClient;
	private View view;
	private String id;
	// private MediaPlayer player;
	private AnimationDrawable ad;
	private DownloadManager downloadManager;
	public static List<Expression> expressionList = new ArrayList<Expression>();
	private int refreshNum = 1;
	private ArrayList<Dynamic> dys = new ArrayList<Dynamic>();
	private int lastItem;
	private DynamicListAdapter adapter;
	private RefreshableView refreshableView;
	private UserGetProcessor getPro;
	private ListView listview;
	private Button relative;
	private int selectedItem = 0;
	private ImageView open;
	private String action;// 不为空则显示“我的招募”
	private MediaPlayer player;
	private com.example.casting.ad.SlidingSwitcherView slidingLayout;
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);

			// dismissDialog();
			String msgStr = (String) msg.obj;
			if (msgStr != null) {
				try {
					JSONArray obj = new JSONArray(msgStr);
					if (obj != null && obj.length() != 0) {

						int length = obj.length();
						int i = 0;
						while (i <= length - 1) {

							JSONObject item = obj.getJSONObject(i);

							if (item.getString("nickname") != null) {
								Dynamic dy = new Dynamic();
								dy.setNickname(item.getString("nickname"));
								dy.setHead_portrait(item
										.getString("head_portrait"));
								// dy.setForwarding_content(item.getString("forwarding_content"));
								// dy.setForwardingId(item.getString("forwarding_id"));
								// dy.setRecruits(item.getString("recruits"));
								dy.setDynamicTime(item
										.getString("dynamic_time"));
								dy.setType(item.getString("type"));
								if (item.getString("is_praise").equals("0")) {
									dy.setIs_praise(false);
								} else {
									dy.setIs_praise(true);
								}
								if (item.getString("type").equals("1")) {
									dy.setRecruit_state(item
											.getString("recruit_state"));
									dy.setUpToDate(item.getString("up_to_date"));
								}

								String type = item.getString("file_type");
								dy.setFileType(type);
								dy.setContent(item.getString("content"));
								dy.setId(item.getString("id"));
								dy.setForwardeds(item.getString("forwardeds"));
								dy.setPraises(item.getString("praises"));
								dy.setComments(item.getString("comments"));
								dy.setUsertype(item.getString("user_type"));
								dy.setDynamicId(item.getString("dynamic_id"));
								dy.setCertification(item.getString("certification"));
								if (type.equals("video")) {
									String video_url = item
											.getString("video_url");
									dy.setVideoUrl(video_url);
									dy.setDuration(item.getString("duration"));
									if (!video_url.equals("")) {
										String url = Server_path.serverfile_path
												+ video_url;
										downloadManager.addHandler(url);
									}

								} else if (type.equals("image")) {
									String image_url = item
											.getString("image_url");
									dy.setImageUrl(image_url);
									String s_image_url = item
											.getString("s_image_url");
									dy.setSImageUrl(s_image_url);
									if (!image_url.equals("")) {
										String[] as = image_url.split(",");
										for (int j = 0; j < as.length; j++) {
											String url = Server_path.serverfile_path
													+ as[j];
											downloadManager.addHandler(url);
										}
									}
									// if (!s_image_url.equals("")) {
									// String[] as = s_image_url.split(",");
									// for (int j = 0; j < as.length; j++) {
									// String url = Server_path.serverfile_path
									// + as[j];
									// downloadManager.addHandler(url);
									// }
									// }
								} else if (type.equals("voice")) {
									String voice_url = item
											.getString("voice_url");
									dy.setVoiceUrl(voice_url);
									if (!voice_url.equals("")) {
										String url = Server_path.serverfile_path
												+ voice_url;
										downloadManager.addHandler(url);
									}

								}
								// TODO 下载图片
								dys.add(dy);
							}

							i++;
						}
						if (dys.size() > 0) {
							adapter.notifyDataSetChanged();

						}

					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				} catch (Error e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(MainTab.this, "网络异常，请稍后重试！", Toast.LENGTH_LONG)
						.show();
			}
			if (refreshableView != null) {
				refreshableView.finishRefreshing();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		ListenerManager.addFreshDynimacListener(this);
		player = new MediaPlayer();
		downloadManager = DownloadManager.getDownloadManager();
		LayoutInflater inflater = LayoutInflater.from(this);
		view = inflater.inflate(R.layout.activity_mainorentrants, null);
		setContentView(view);
		slidingLayout=(SlidingSwitcherView) findViewById(R.id.slidingLayout);
//		slidingLayout.setVisibility(View.VISIBLE);
		if (action != null) {
			slidingLayout.setVisibility(View.GONE);
			RelativeLayout ding=(RelativeLayout) findViewById(R.id.ding);
			ding.setVisibility(View.GONE);
			View titlelayout=findViewById(R.id.titlelayout);
			titlelayout.setVisibility(View.VISIBLE);
			initView(titlelayout);
			setLeftButtonAble(true, "返回");
			setTitle("报名者");
		}
		layout_content = (LinearLayout) findViewById(R.id.layout_content);
		asyncHttpClient = new AsyncHttpClient();
		id = Session.getInstance().getUser_id();
		Bundle b = getIntent().getExtras();
		if (b != null) {
			RegistBean baseBean = (RegistBean) b.getSerializable(MainTab.class.getName());
			id = baseBean.getId();
		}
		listview = (ListView) view.findViewById(R.id.dynamicList);
		relative = (Button) findViewById(R.id.relative);
		relative.setOnClickListener(this);
		open = (ImageView) findViewById(R.id.open);
		initExpression();
		Intent intent = getIntent();
		action = intent.getStringExtra("action");
//		Tool tool = new Tool();
//		tool.setConfig(id, refreshNum + "");// TODO
//		tool.execute(Server_path.getListDynamicManage);
		
//		freshDynamic();
		
		//获取广告
		getAdManage();
	}
	
	//获取广告
	Button ad2;
	Button ad3;
	Button ad4;
	Bitmap bitmap2;
	Bitmap bitmap3;
	Bitmap bitmap4;
	OnClickListener adClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String url=(String)v.getTag();
			Intent intent = new Intent();
			intent.setData(Uri.parse(url));
			intent.setAction(Intent.ACTION_VIEW);
			startActivity(intent); 
		}
	};
	public void getAdManage(){
		
		new AsyncTask<String, Integer, String>() {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				String result;
				try {
					Map<String, String> myparams = new HashMap<String, String>();
					result = post(params[0], myparams);
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
				return result;
			}
			

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
//				super.onPostExecute(result);
//				{"ad_id":"1","ad1":"files\/342\/NDBhZDY2ODEtZjUxOS00MTdmLWFmZjItMDBiOWJlNTBjYjE3.jpg","ad2":"files\/335\/YjE5NTUzYmMtZDcwMy00N2UzLWFjZjEtZjczOGY1ZWYwMWYx.jpg","ad2_url":"http:\/\/www.baidu.com","ad3":"files\/335\/YjE5NTUzYmMtZDcwMy00N2UzLWFjZjEtZjczOGY1ZWYwMWYx.jpg","ad3_url":"http:\/\/www.baidu.com","ad4":"files\/335\/YjE5NTUzYmMtZDcwMy00N2UzLWFjZjEtZjczOGY1ZWYwMWYx.jpg","ad4_url":"http:\/\/www.baidu.com","submit_time":"2015-04-22 23:54:36.0"}
				try {
					JSONObject obj = new JSONObject(result);
					String ad2Str=obj.optString("ad2");
					String ad3Str=obj.optString("ad3");
					String ad4Str=obj.optString("ad4");
					
					String ad2_url=obj.optString("ad2_url");
					String ad3_url=obj.optString("ad3_url");
					String ad4_url=obj.optString("ad4_url");
//					Server_path.serverfile_path;
					if(ad2Str!=null&&ad3Str!=null&&ad4Str!=null&&!"".equals(ad2Str)&&!"".equals(ad3Str)&&!"".equals(ad4Str)){
						slidingLayout.setVisibility(View.VISIBLE);
						slidingLayout.startAutoPlay();
						ad2=(Button)findViewById(R.id.ad2);
						ad3=(Button)findViewById(R.id.ad3);
						ad4=(Button)findViewById(R.id.ad4);
						ad2.setOnClickListener(adClickListener);
						ad3.setOnClickListener(adClickListener);
						ad4.setOnClickListener(adClickListener);
						
						bitmap2=BitmapFactory.decodeStream(new java.net.URL(Server_path.serverfile_path+ad2Str).openConnection().getInputStream());
						ad2.setBackgroundDrawable(new BitmapDrawable(bitmap2));
						ad2.setTag(ad2_url);
						bitmap3=BitmapFactory.decodeStream(new java.net.URL(Server_path.serverfile_path+ad3Str).openConnection().getInputStream());
						ad3.setBackgroundDrawable(new BitmapDrawable(bitmap3));
						ad3.setTag(ad3_url);
						bitmap4=BitmapFactory.decodeStream(new java.net.URL(Server_path.serverfile_path+ad4Str).openConnection().getInputStream());
						ad4.setTag(ad4_url);
						ad4.setBackgroundDrawable(new BitmapDrawable(bitmap4));
					}else{
						slidingLayout.setVisibility(View.GONE);
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch(Error er){
					er.printStackTrace();
				}
			}
			
		}.execute(Server_path.getAdmanage);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}
	@Override
	protected void onResume() {
		super.onResume();
		
		if(action!=null&&action.equals("text")){
//			action=null;
		}else{
			dys.clear();
			dys = new ArrayList<Dynamic>();
			refreshNum = 1;
			Tool tool = new Tool();
			
			tool.setConfig(id, refreshNum + "");// TODO
			tool.execute(Server_path.getListDynamicManage);
		}
	
	}

	@Override
	protected void onRestart() {
//		dys = new ArrayList<Dynamic>();
//		refreshNum = 1;
//		Tool tool = new Tool();
//		tool.setConfig(id, refreshNum + "");// TODO
//		tool.execute(Server_path.getListDynamicManage);
		super.onRestart();
	}

	/**
	 * 初始化表情信息
	 */
	public void initExpression() {
		Expression exp1 = new Expression(R.drawable.s001, "/s001");
		Expression exp2 = new Expression(R.drawable.s002, "/s002");
		Expression exp3 = new Expression(R.drawable.s003, "/s003");
		Expression exp4 = new Expression(R.drawable.s004, "/s004");
		Expression exp5 = new Expression(R.drawable.s005, "/s005");
		Expression exp6 = new Expression(R.drawable.s006, "/s006");
		Expression exp7 = new Expression(R.drawable.s007, "/s007");
		Expression exp8 = new Expression(R.drawable.s008, "/s008");
		Expression exp9 = new Expression(R.drawable.s009, "/s009");
		Expression exp10 = new Expression(R.drawable.s010, "/s010");

		Expression exp11 = new Expression(R.drawable.s011, "/s011");
		Expression exp12 = new Expression(R.drawable.s012, "/s012");
		Expression exp13 = new Expression(R.drawable.s013, "/s013");
		Expression exp14 = new Expression(R.drawable.s014, "/s014");
		Expression exp15 = new Expression(R.drawable.s015, "/s015");
		Expression exp16 = new Expression(R.drawable.s016, "/s016");
		Expression exp17 = new Expression(R.drawable.s017, "/s017");
		Expression exp18 = new Expression(R.drawable.s018, "/s018");
		Expression exp19 = new Expression(R.drawable.s019, "/s019");
		Expression exp20 = new Expression(R.drawable.s020, "/s020");

		Expression exp21 = new Expression(R.drawable.s021, "/s021");
		Expression exp22 = new Expression(R.drawable.s022, "/s022");
		Expression exp23 = new Expression(R.drawable.s023, "/s023");
		Expression exp24 = new Expression(R.drawable.s024, "/s024");
		Expression exp25 = new Expression(R.drawable.s025, "/s025");
		Expression exp26 = new Expression(R.drawable.s026, "/s026");
		Expression exp27 = new Expression(R.drawable.s027, "/s027");
		Expression exp28 = new Expression(R.drawable.s028, "/s028");
		Expression exp29 = new Expression(R.drawable.s029, "/s029");
		Expression exp30 = new Expression(R.drawable.s030, "/s030");

		Expression exp31 = new Expression(R.drawable.s031, "/s031");
		Expression exp32 = new Expression(R.drawable.s032, "/s032");
		Expression exp33 = new Expression(R.drawable.s033, "/s033");
		Expression exp34 = new Expression(R.drawable.s034, "/s034");
		Expression exp35 = new Expression(R.drawable.s035, "/s035");
		Expression exp36 = new Expression(R.drawable.s036, "/s036");
		Expression exp37 = new Expression(R.drawable.s037, "/s037");
		Expression exp38 = new Expression(R.drawable.s038, "/s038");
		Expression exp39 = new Expression(R.drawable.s039, "/s039");
		Expression exp40 = new Expression(R.drawable.s040, "/s040");

		Expression exp41 = new Expression(R.drawable.s041, "/s041");
		Expression exp42 = new Expression(R.drawable.s042, "/s042");
		Expression exp43 = new Expression(R.drawable.s043, "/s043");
		Expression exp44 = new Expression(R.drawable.s044, "/s044");
		Expression exp45 = new Expression(R.drawable.s045, "/s045");
		Expression exp46 = new Expression(R.drawable.s046, "/s046");
		Expression exp47 = new Expression(R.drawable.s047, "/s047");
		Expression exp48 = new Expression(R.drawable.s048, "/s048");
		Expression exp49 = new Expression(R.drawable.s049, "/s049");
		Expression exp50 = new Expression(R.drawable.s050, "/s050");

		Expression exp51 = new Expression(R.drawable.s051, "/s051");
		Expression exp52 = new Expression(R.drawable.s052, "/s052");
		Expression exp53 = new Expression(R.drawable.s053, "/s053");
		Expression exp54 = new Expression(R.drawable.s054, "/s054");
		Expression exp55 = new Expression(R.drawable.s055, "/s055");
		Expression exp56 = new Expression(R.drawable.s056, "/s056");
		Expression exp57 = new Expression(R.drawable.s057, "/s057");
		Expression exp58 = new Expression(R.drawable.s058, "/s058");
		Expression exp59 = new Expression(R.drawable.s059, "/s059");
		Expression exp60 = new Expression(R.drawable.s060, "/s060");

		Expression exp61 = new Expression(R.drawable.s061, "/s061");
		Expression exp62 = new Expression(R.drawable.s062, "/s062");
		Expression exp63 = new Expression(R.drawable.s063, "/s063");
		Expression exp64 = new Expression(R.drawable.s064, "/s064");
		Expression exp65 = new Expression(R.drawable.s065, "/s065");
		Expression exp66 = new Expression(R.drawable.s066, "/s066");
		Expression exp67 = new Expression(R.drawable.s067, "/s067");
		Expression exp68 = new Expression(R.drawable.s068, "/s068");
		Expression exp69 = new Expression(R.drawable.s069, "/s069");
		Expression exp70 = new Expression(R.drawable.s070, "/s070");

		Expression exp71 = new Expression(R.drawable.s071, "/s071");
		Expression exp72 = new Expression(R.drawable.s072, "/s072");
		Expression exp73 = new Expression(R.drawable.s073, "/s073");
		Expression exp74 = new Expression(R.drawable.s074, "/s074");
		Expression exp75 = new Expression(R.drawable.s075, "/s075");
		Expression exp76 = new Expression(R.drawable.s076, "/s076");
		Expression exp77 = new Expression(R.drawable.s077, "/s077");
		Expression exp78 = new Expression(R.drawable.s078, "/s078");
		Expression exp79 = new Expression(R.drawable.s079, "/s079");
		Expression exp80 = new Expression(R.drawable.s080, "/s080");

		Expression exp81 = new Expression(R.drawable.s081, "/s081");
		Expression exp82 = new Expression(R.drawable.s082, "/s082");
		Expression exp83 = new Expression(R.drawable.s083, "/s083");
		Expression exp84 = new Expression(R.drawable.s084, "/s084");
		Expression exp85 = new Expression(R.drawable.s085, "/s085");

		expressionList.add(exp1);
		expressionList.add(exp2);
		expressionList.add(exp3);
		expressionList.add(exp4);
		expressionList.add(exp5);
		expressionList.add(exp6);
		expressionList.add(exp7);
		expressionList.add(exp8);
		expressionList.add(exp9);
		expressionList.add(exp10);

		expressionList.add(exp11);
		expressionList.add(exp12);
		expressionList.add(exp13);
		expressionList.add(exp14);
		expressionList.add(exp15);
		expressionList.add(exp16);
		expressionList.add(exp17);
		expressionList.add(exp18);
		expressionList.add(exp19);
		expressionList.add(exp20);

		expressionList.add(exp21);
		expressionList.add(exp22);
		expressionList.add(exp23);
		expressionList.add(exp24);
		expressionList.add(exp25);
		expressionList.add(exp26);
		expressionList.add(exp27);
		expressionList.add(exp28);
		expressionList.add(exp29);
		expressionList.add(exp30);

		expressionList.add(exp31);
		expressionList.add(exp32);
		expressionList.add(exp33);
		expressionList.add(exp34);
		expressionList.add(exp35);
		expressionList.add(exp36);
		expressionList.add(exp37);
		expressionList.add(exp38);
		expressionList.add(exp39);
		expressionList.add(exp40);

		expressionList.add(exp41);
		expressionList.add(exp42);
		expressionList.add(exp43);
		expressionList.add(exp44);
		expressionList.add(exp45);
		expressionList.add(exp46);
		expressionList.add(exp47);
		expressionList.add(exp48);
		expressionList.add(exp49);
		expressionList.add(exp50);

		expressionList.add(exp51);
		expressionList.add(exp52);
		expressionList.add(exp53);
		expressionList.add(exp54);
		expressionList.add(exp55);
		expressionList.add(exp56);
		expressionList.add(exp57);
		expressionList.add(exp58);
		expressionList.add(exp59);
		expressionList.add(exp60);

		expressionList.add(exp61);
		expressionList.add(exp62);
		expressionList.add(exp63);
		expressionList.add(exp64);
		expressionList.add(exp65);
		expressionList.add(exp66);
		expressionList.add(exp67);
		expressionList.add(exp68);
		expressionList.add(exp69);
		expressionList.add(exp70);

		expressionList.add(exp71);
		expressionList.add(exp72);
		expressionList.add(exp73);
		expressionList.add(exp74);
		expressionList.add(exp75);
		expressionList.add(exp76);
		expressionList.add(exp77);
		expressionList.add(exp78);
		expressionList.add(exp79);
		expressionList.add(exp80);

		expressionList.add(exp81);
		expressionList.add(exp82);
		expressionList.add(exp83);
		expressionList.add(exp84);
		expressionList.add(exp85);
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

	public void onBackPressed() {
		if (action != null) {
			finish();
		} else {

			if (System.currentTimeMillis() - lastTime < 2000) {
				ListenerManager.notifyCloseLoginActivity();
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				// CastingApplication.getInstance().exits();
				// 获取进程ID
				android.os.Process.killProcess(android.os.Process.myPid());
				return;
			}
			lastTime = System.currentTimeMillis();
			showToast("再按一次退出！");

		}
	}

	@Override
	public void LeftButtonClick() {
		if (action != null) {
			finish();
		}
	}

	@Override
	public void RightButtonClick() {

	}

	class Tool extends AsyncTask<String, Integer, String> {
		// private int dialogId=1;
		private String id, pagenum;

		public void setConfig(String id, String pagenum) {
			this.id = id;
			this.pagenum = pagenum;
		}

		@Override
		protected void onPreExecute() {
			showWaitDialog();
		}

		protected String doInBackground(String... params) {

			String result = null;

			Map<String, String> myparams = new HashMap<String, String>();

			myparams.put("id", id);// param.put("id", "1");
			myparams.put("pagenum", pagenum);// param.put("pagenum", "1");
			// myparams.put("response",
			// "application/json");//param.put("pagenum", "1");
			try {
				result = post(params[0], myparams);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			return result;
		}

		protected void onPostExecute(String result) {
			dismissDialog();
			if (result != null) {
				try {
					JSONArray obj = new JSONArray(result);
					if (obj != null) {
						// [{"nickname":"test","forwarding_content":"","forwarding_id":"",
						// "recruits":"0","dynamic_time":"2015-02-09 11:19:55.0",
						// "s_image_url":"","voice_url":"","type":"0","is_praise":"0",
						// "up_to_date":"","is_forwarding":"0","file_type":"image",
						// "content":"","id":"1","forwardeds":"0"," duration":"000000000000",
						// "title":"","dynamic_id":"151","video_url":"","imags_image_url						// "files\/1\/images\/N2M3YjVhZDMtYjA1Zi00YmRiLWJjNGMtNjcxODQyMTNjMDQ5.jpg,files\/1\/images\/images\/Y2I3YjIyNmUtNTJlMS00NjEwLWI0NDEtZDVjNDkwYWRjMDU4.jpg","praises":"0","comments":"0"},
						int length = obj.length();
						int i = 0;
						while (i <= length - 1) {

							JSONObject item = obj.getJSONObject(i);

							if (item.getString("nickname") != null) {
								Dynamic dy = new Dynamic();
								dy.setNickname(item.getString("nickname"));
								String head_portrait = item
										.getString("head_portrait");
								dy.setHead_portrait(head_portrait);
								String url = Server_path.serverfile_path
										+ head_portrait;
								downloadManager.addHandler(url);
								 dy.setForwarding_content(item.getString("forwarding_content"));
								// dy.setForwardingId(item.getString("forwarding_id"));
								// dy.setRecruits(item.getString("recruits"));
								dy.setDynamicTime(item
										.getString("dynamic_time"));
								dy.setType(item.getString("type"));
								if (item.getString("is_praise").equals("0")) {
									dy.setIs_praise(false);
								} else {
									dy.setIs_praise(true);
								}
								if (item.getString("type").equals("1")) {
									dy.setRecruit_state(item
											.getString("recruit_state"));
									dy.setUpToDate(item.getString("up_to_date"));
								}

								// if(item.getString("is_forwarding").equals("0")){
								// dy.setForwarding(false);
								// }else{
								// dy.setForwarding(true);
								// }
								String type = item.getString("file_type");
								dy.setFileType(type);
								dy.setContent(item.getString("content"));
								dy.setId(item.getString("id"));
								dy.setForwardeds(item.getString("forwardeds"));
								dy.setPraises(item.getString("praises"));
								dy.setComments(item.getString("comments"));
								dy.setUsertype(item.getString("user_type"));
								dy.setDynamicId(item.getString("dynamic_id"));
								dy.setCertification(item.getString("certification"));
								if (type.equals("video")) {
									String video_url = item
											.getString("video_url");
									dy.setVideoUrl(video_url);
									if (!video_url.equals("")) {
										url = Server_path.serverfile_path
												+ video_url;
										downloadManager.addHandler(url);
									}

								} else if (type.equals("image")) {
									String image_url = item
											.getString("image_url");
									dy.setImageUrl(image_url);
									String s_image_url = item
											.getString("s_image_url");
									dy.setSImageUrl(s_image_url);
									if (!image_url.equals("")) {
										String[] as = image_url.split(",");
										for (int j = 0; j < as.length; j++) {
											url = Server_path.serverfile_path
													+ as[j];
											downloadManager.addHandler(url);
										}
									}
									// if (!s_image_url.equals("")) {
									// String[] as = s_image_url.split(",");
									// for (int j = 0; j < as.length; j++) {
									// String url = Server_path.serverfile_path
									// + as[j];
									// downloadManager.addHandler(url);
									// }
									// }
								} else if (type.equals("voice")) {
									String voice_url = item
											.getString("voice_url");
									dy.setVoiceUrl(voice_url);
									dy.setDuration(item.getString("duration"));
									if (!voice_url.equals("")) {
										url = Server_path.serverfile_path
												+ voice_url;
										downloadManager.addHandler(url);
									}

								}
								// TODO 下载图片
								dys.add(dy);
							}

							i++;
						}

						if (dys.size() > 0) {

							listview.setOnScrollListener(new OnScrollListener() {

								@Override
								public void onScrollStateChanged(
										AbsListView view, int scrollState) {

									if (lastItem == adapter.getCount() - 1
											&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
										addMoreData();
									}
								}

								@Override
								public void onScroll(AbsListView view,
										int firstVisibleItem,
										int visibleItemCount, int totalItemCount) {

									lastItem = firstVisibleItem - 1
											+ visibleItemCount;
								}
							});

							if (action != null) {
								slidingLayout.setVisibility(View.GONE);
								ArrayList<Dynamic> eds = new ArrayList<Dynamic>();
								if (dys != null && dys.size() > 0) {
									for (int j = 0; j < dys.size(); j++) {
										if (dys.get(j).getType().equals("1")
												&& dys.get(j).getId()
														.equals(id)) {
											eds.add(dys.get(j));
										}
									}
									adapter = new DynamicListAdapter(
											MainTab.this, eds);
									listview.setAdapter(adapter);
									adapter.notifyDataSetChanged();
									relative.setText("我的招募");
									relative.setBackgroundResource(0);
									dialog.dismiss();
									selectedItem = 1;
									open.setVisibility(View.GONE);
								}
							} else {
								adapter = new DynamicListAdapter(MainTab.this,
										dys);
								listview.setAdapter(adapter);
								adapter.notifyDataSetChanged();
							}
							refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);
							refreshableView.setOnRefreshListener(
									new PullToRefreshListener() {
										@Override
										public void onRefresh() {

											// showWaitDialog();
											new Thread() {
												public void run() {

													try {
														Thread.sleep(3000);
													} catch (InterruptedException e) {
														e.printStackTrace();
													}

													// dys = new
													// ArrayList<Dynamic>();
													String result = null;
													Map<String, String> myparams = new HashMap<String, String>();
													myparams.put("id", id);// param.put("id",
																			// "1");
													myparams.put("pagenum", "1");// param.put("pagenum",
																					// "1");
													// myparams.put("response",
													// "application/json");//param.put("pagenum",
													// "1");
													try {
														result = post(
																Server_path.getListDynamicManage,
																myparams);
													} catch (IOException e) {
														e.printStackTrace();
													}
													dys = new ArrayList<Dynamic>();
													Message msg = new Message();
													msg.obj = result;
													mHandler.sendMessage(msg);

												};
											}.start();

										}
									}, 0);
							listview.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int position, long id) {
									Intent intent = new Intent(MainTab.this,
											TextActivity.class);
									intent.putExtra("dy", dys.get(position));
									startActivityForResult(intent,0);
								}

							});
						}

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(MainTab.this, "网络请求失败，请稍后重试！", 1).show();
			}

		}

	}

	/**
	 * ͨ
	 * 
	 * @param actionUrl
	 * @param params
	 * @param upload_file
	 * @return
	 * @throws IOException
	 */
	public String post(String actionUrl, Map<String, String> params)
			throws IOException {
		HttpClient httpClient = CustomHttpClient.getHttpClient();
		HttpPost post = new HttpPost(actionUrl);
		List<BasicNameValuePair> postData = new ArrayList<BasicNameValuePair>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			postData.add(new BasicNameValuePair(entry.getKey(), entry
					.getValue()));
		}
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postData,
				HTTP.UTF_8);
		post.setEntity(entity);
		HttpResponse response = httpClient.execute(post);

		try {
			HttpEntity httpEntity = response.getEntity();
			InputStream is = httpEntity.getContent();
			StringBuffer sb = new StringBuffer();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			String action = null;
			try {
				JSONObject jsonResult = new JSONObject(sb.toString());
				action = jsonResult.getString("return");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			byte bitmapArray[] = Base64.decode(action, 0);
			String content = new String(bitmapArray);
			return content;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void addMoreData() {
		// showWaitDialog();
		new Thread() {
			public void run() {

				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				refreshNum = refreshNum + 1;
				String result = null;
				Map<String, String> myparams = new HashMap<String, String>();
				myparams.put("id", id);// param.put("id", "1");
				myparams.put("pagenum", refreshNum + "");// param.put("pagenum",
															// "1");
				// myparams.put("response",
				// "application/json");//param.put("pagenum", "1");
				try {
					result = post(Server_path.getListDynamicManage, myparams);
				} catch (IOException e) {
					e.printStackTrace();
				}
				Message msg = new Message();
				msg.obj = result;
				mHandler.sendMessage(msg);

			};
		}.start();
	}

	/**
	 * 获取我和我关注的动态列表
	 */
	// private String getList(String id,String pagenum) {
	// String content = null;
	// RequestParams param = new RequestParams();
	// param.put("id", id);//param.put("id", "1");
	// param.put("pagenum", pagenum);//param.put("pagenum", "1");
	// asyncHttpClient
	// .post(Server_path.getListDynamicManage,
	// param, new AsyncHttpResponseHandler() {
	//
	// public void onSuccess(String content) {
	// super.onSuccess(content);
	// String action = null;
	// try {
	// JSONObject jsonResult = new JSONObject(
	// content);
	// action = jsonResult.getString("return");
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// byte bitmapArray[] = Base64.decode(action, 0);
	// content=new String(bitmapArray);
	// }
	// });
	// return content;
	//
	// }
	class DynamicListAdapter extends BaseAdapter {
		Context context;
		ArrayList<Dynamic> arrayList;
		int expression_wh = -1;

		public DynamicListAdapter(Context context, ArrayList<Dynamic> arrayList) {
			super();
			this.context = context;
			this.arrayList = arrayList;
			expression_wh = (int) this.context.getResources().getDimension(
					R.dimen.chat_expression_wh);
			// downloadManager = DownloadManager.getDownloadManager();
			downloadManager.setDownLoadCallback(new DownLoadCallback() {
				@Override
				public void onSuccess(String url) {
					// Toast.makeText(MainTab.this, "下载完成",
					// Toast.LENGTH_LONG).show();
				}

				@Override
				public void onLoading(String url, long totalSize,
						long currentSize, long speed) {
				}
			});

		}

		@Override
		public int getCount() {
			return arrayList.size();
		}

		@Override
		public Object getItem(int position) {
			return arrayList.get(position);
		}

		@Override
		public long getItemId(int span) {
			return span;
		}

		final Html.ImageGetter imageGetter_resource = new Html.ImageGetter() {
			public Drawable getDrawable(String source) {
				Drawable drawable = null;
				int rId = Integer.parseInt(source);
				drawable = context.getResources().getDrawable(rId);
				drawable.setBounds(0, 0, expression_wh, expression_wh);// 设置显示的图像大小
				return drawable;
			};
		};

		public String replaceSpaceToCode(String str) {
			String rt = str.replace(" ", "&nbsp;");
			rt = rt.replace("\n", "<br/>");

			return rt;
		}

		private String msgConvert(String content) {
			Log.v("_____________", "1content = " + content);
			for (int i = 0; i < expressionList.size(); i++) {
				content = content.replace(expressionList.get(i).code,
						"<img src=\"" + expressionList.get(i).drableId
								+ "\" />");
			}
			Log.v("_____________", "2content = " + content);
			return content;
		}

		@Override
		public View getView(final int position, View v, ViewGroup group) {
			// final boolean isPraise=;
			// Holder holder=null;
			// if(v==null){
			// holder=new Holder();
			// LayoutInflater inflater = LayoutInflater.from(context);
			// v = inflater.inflate(R.layout.activity_mainorentrants_item,
			// null);
			//
			// holder.pic = (ImageView) v.findViewById(R.id.pic);
			// holder.name = (TextView) v.findViewById(R.id.name);
			// holder.time = (TextView) v.findViewById(R.id.time);
			// holder.signup = (Button) v.findViewById(R.id.signup);
			// holder.signuptime = (TextView) v.findViewById(R.id.signuptime);
			// holder.text = (TextView) v.findViewById(R.id.text);
			// holder.sendagain = (RelativeLayout) v
			// .findViewById(R.id.sendagain);
			// holder.sendagainnum = (TextView) v
			// .findViewById(R.id.sendagainnum);
			// holder.review = (RelativeLayout) v
			// .findViewById(R.id.review);
			// holder.reviewnum = (TextView) v.findViewById(R.id.reviewnum);
			// holder.praise = (RelativeLayout) v
			// .findViewById(R.id.praise);
			// holder.praisenum = (TextView) v
			// .findViewById(R.id.praisenum);
			// holder.praiseimg = (ImageView) v
			// .findViewById(R.id.praiseimg);
			// holder.maingridView = (MyGridView) v
			// .findViewById(R.id.maingridView);
			//
			// v.setTag(holder);
			// }else {
			// holder = (Holder)v.getTag();
			// }
			LayoutInflater inflater = LayoutInflater.from(context);
			v = inflater.inflate(R.layout.activity_mainorentrants_item, null);

			ImageView pic = (ImageView) v.findViewById(R.id.pic);
			ImageView img_cer = (ImageView) v.findViewById(R.id.img_cer);
			TextView name = (TextView) v.findViewById(R.id.name);
			TextView time = (TextView) v.findViewById(R.id.time);
			final Button signup = (Button) v.findViewById(R.id.signup);
			TextView signuptime = (TextView) v.findViewById(R.id.signuptime);
			TextView text = (TextView) v.findViewById(R.id.text);
			TextView forwarding_text = (TextView) v.findViewById(R.id.forwarding_text);
			RelativeLayout sendagain = (RelativeLayout) v
					.findViewById(R.id.sendagain);
			TextView sendagainnum = (TextView) v
					.findViewById(R.id.sendagainnum);
			RelativeLayout review = (RelativeLayout) v
					.findViewById(R.id.review);
			TextView reviewnum = (TextView) v.findViewById(R.id.reviewnum);
			RelativeLayout praise = (RelativeLayout) v
					.findViewById(R.id.praise);
			final TextView praisenum = (TextView) v
					.findViewById(R.id.praisenum);
			final ImageView praiseimg = (ImageView) v
					.findViewById(R.id.praiseimg);
			MyGridView maingridView = (MyGridView) v
					.findViewById(R.id.maingridView);
			
			
			pic.setOnClickListener(new MyPicClickListener(arrayList
					.get(position)));
			String picPath = DownloadManager.FILE_ROOT
					+ TAStringUtils
							.getFileNameFromUrl(Server_path.serverfile_path
									+ arrayList.get(position)
											.getHead_portrait());
			pic.setImageBitmap(BitmapFactory.decodeFile(picPath));
			String cer=arrayList.get(position).getCertification();
			if(cer!=null&&cer.equals("1")){
				img_cer.setVisibility(View.VISIBLE);
			}
			name.setText(arrayList.get(position).getNickname());
			String timeStr = arrayList.get(position).getDynamicTime();
			Date date = new Date();
		    SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    try {
				date = simple.parse(timeStr.substring(0, timeStr.indexOf(".")));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			time.setText(DateUtils.fromToday(date));
			if (arrayList.get(position).getType().equals("1")) {
				String uptodate = arrayList.get(position).getUpToDate();
				if (!uptodate.equals("")) {
					signuptime.setText(uptodate.substring(0,
							uptodate.indexOf(" "))
							+ "截止报名");
				}
				signuptime.setVisibility(View.VISIBLE);
				signup.setVisibility(View.VISIBLE);
				String usertype = Session.getInstance().getUsertype();// 用户类型:普通用户和导演(1/2)
				// 招募方有报名者，被招募方没有报名者
				if (usertype != null && usertype.equals("1")) {
					if (Integer.parseInt(arrayList.get(position)
							.getRecruit_state()) >= 1) { // >=1时为已试镜
						signup.setText("");
						signup.setBackgroundResource(R.drawable.main_btnlogodown);

					} else {
						signup.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// 需setDynamicId,setContent,setCommentType
								CommentPraise cp = new CommentPraise();
								cp.setCommentType("2");
								cp.setDynamicId(arrayList.get(position)
										.getDynamicId());
								cp.setContent("");
								add(cp, id, signup);
							}
						});
					}
				} else {
					signup.setText("报名名单");
					signup.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Intent intent = new Intent(MainTab.this,
									SignupedActivity.class);
							intent.putExtra("dynamicId", arrayList
									.get(position).getDynamicId());
							startActivity(intent);

						}
					});
				}
			}

			String contentStr = arrayList.get(position).getContent();

			String content = msgConvert(replaceSpaceToCode(contentStr));
			Spanned sp = Html.fromHtml(content, imageGetter_resource, null);
			text.setText(sp);// TODO
			text.setMovementMethod(LinkMovementMethod.getInstance());
			
//			Matcher slashMatcher = Pattern.compile("/").matcher(string);
//			   int mIdx = 0;
//			   while(slashMatcher.find()) {
//			     mIdx++;
//			}
			
			if (contentStr.indexOf("@") != -1 && contentStr.indexOf(" ") != -1
					&& contentStr.indexOf("@") <=contentStr.length()-1 && contentStr.indexOf(" ") <=contentStr.length()-1) {
				SpannableStringBuilder spanBuilder = new SpannableStringBuilder(
						sp);
				
				int i=0;
				int j=0;
				int max=contentStr.split("@").length;
				for(int n=1;n<max;n++){
					if(i==-1||j==-1)break;
					if(i!=0)
						i=contentStr.indexOf("@",j);
					else
						i=contentStr.indexOf("@",i);
					j=contentStr.indexOf(" ",i);
					if(j<0)
						break;
					String str=contentStr.substring(i+1, j);
					MyURLSpan myURLSpan = new MyURLSpan(str);
					spanBuilder.setSpan(myURLSpan, i,j,	Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
					i++;
				}
				text.setText(spanBuilder);
				
			} // @、超链接等的处理
			if (contentStr.equals("")) {
				text.setVisibility(View.GONE);
			}
			
			String forwarding_contentStr = arrayList.get(position).getForwarding_content();

			if(forwarding_contentStr!=null&&!forwarding_contentStr.equals("")){
				forwarding_text.setVisibility(View.VISIBLE);
				content = msgConvert(replaceSpaceToCode(forwarding_contentStr));
				sp = Html.fromHtml(content, imageGetter_resource, null);
				forwarding_text.setText(sp);// TODO
				forwarding_text.setMovementMethod(LinkMovementMethod.getInstance());
				if (forwarding_contentStr.indexOf("@") != -1 && forwarding_contentStr.indexOf(" ") != -1
						&& forwarding_contentStr.indexOf("@") <=forwarding_contentStr.length()-1 && forwarding_contentStr.indexOf(" ") <=forwarding_contentStr.length()-1) {
					SpannableStringBuilder spanBuilder = new SpannableStringBuilder(
							sp);
					MyURLSpan myURLSpan = new MyURLSpan(forwarding_contentStr.substring(
							forwarding_contentStr.indexOf("@") + 1, forwarding_contentStr.indexOf(" ")));
					spanBuilder.setSpan(myURLSpan, forwarding_contentStr.indexOf("@"),
							forwarding_contentStr.indexOf(" "),
							Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
					forwarding_text.setText(spanBuilder);
				} // @、超链接等的处理
			}
			
			// 相册
			String imageUrl = arrayList.get(position).getImageUrl();
			if (imageUrl != null && !imageUrl.equals("")) {

				List<String> filelist = new ArrayList<String>();
				String[] as = imageUrl.split(",");
				for (int j = 0; j < as.length; j++) {
					String url = Server_path.serverfile_path + as[j];
					String path = DownloadManager.FILE_ROOT
							+ TAStringUtils.getFileNameFromUrl(url);
					filelist.add(path);
				}

				maingridView.setVisibility(View.VISIBLE);
				// maingridView.setPadding(3, 3, 3,
				// 0);
				// maingridView.setHorizontalSpacing(5);
				// maingridView.setVerticalSpacing(3);
				ImgsAdapter imgsAdapter = new ImgsAdapter(context, filelist, null,as);
						// TODO imageUrl不是参数，而是下载到本地的路径; 转换成List<String>
				imgsAdapter.setVisibility(false);
				maingridView.setAdapter(imgsAdapter);
			}
			// 视频播放
			String videoUrl = arrayList.get(position).getVideoUrl();
			// String duration=arrayList.get(position).getDuration();
			if (videoUrl != null && !videoUrl.equals("")) {
				String url = Server_path.serverfile_path + videoUrl;
				String path = DownloadManager.FILE_ROOT
						+ TAStringUtils.getFileNameFromUrl(url);
				playvideo(v, path);
				// TODO videoUrl不是参数，而是下载到本地的路径;2,大小参数的修改
			}
			// 语音
			String voiceUrl = arrayList.get(position).getVoiceUrl();
			if (voiceUrl != null && !voiceUrl.equals("")) {
				String url = Server_path.serverfile_path + voiceUrl;
				String path = DownloadManager.FILE_ROOT
						+ TAStringUtils.getFileNameFromUrl(url);
				playVoice(v, path, arrayList.get(position).getDuration());

			}
			sendagainnum.setText(arrayList.get(position).getForwardeds());
			reviewnum.setText(arrayList.get(position).getComments());
			praisenum.setText(arrayList.get(position).getPraises());
			if (arrayList.get(position).isIs_praise()) {
				praiseimg
						.setBackgroundResource(R.drawable.main_toolbar_praise_);
			} else {
				praiseimg.setBackgroundResource(R.drawable.main_toolbar_unlike);
			}
			sendagain.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(MainTab.this,
							SendAgainActivity.class);
					intent.putExtra("dy", arrayList.get(position));
					startActivity(intent);
				}
			});
			review.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(MainTab.this,
							ReviewActivity.class);
					intent.putExtra("dynamicId", arrayList.get(position)
							.getDynamicId());
					startActivity(intent);
				}
			});
			praise.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					if (arrayList.get(position).isIs_praise()) {
						// 需setDynamicId,setContent,setCommentType
						CommentPraise cp = new CommentPraise();
						cp.setCommentType("1");
						cp.setDynamicId(arrayList.get(position).getDynamicId());
						cp.setContent("");
						add(cp, id, null);
						praiseimg
								.setBackgroundResource(R.drawable.main_toolbar_unlike);
						arrayList.get(position).setIs_praise(false);
						int num = Integer.parseInt(arrayList.get(position)
								.getPraises()) - 1;
						praisenum.setText(num + "");
						arrayList.get(position).setPraises(num + "");
					} else {
						// 需setDynamicId,setContent,setCommentType
						CommentPraise cp = new CommentPraise();
						cp.setCommentType("1");
						cp.setDynamicId(arrayList.get(position).getDynamicId());
						cp.setContent("");
						add(cp, id, null);// TODO
						praiseimg
								.setBackgroundResource(R.drawable.main_toolbar_praise_);
						arrayList.get(position).setIs_praise(true);
						int num = Integer.parseInt(arrayList.get(position)
								.getPraises()) + 1;
						praisenum.setText(num + "");
						arrayList.get(position).setPraises(num + "");
					}

				}
			});

			return v;
		}

		
		
		/**
		 * 
		 * @param v
		 * @param pathStr
		 * @param sizeStr
		 * @param timeStr
		 */
		private void playvideo(View v, final String pathStr) {

			ImageView video = (ImageView) v.findViewById(R.id.video);
			RelativeLayout videomessage = (RelativeLayout) v
					.findViewById(R.id.videomessage);
			videomessage.setVisibility(View.VISIBLE);

			// TextView size=(TextView) v.findViewById(R.id.size);
			// size.setText(sizeStr);
			// TextView time=(TextView) v.findViewById(R.id.timeStr);
			// time.setText(timeStr);
			File file = new File(pathStr);
			if (file.exists()) {
				// 获取视频的缩略图
				Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(pathStr,
						Thumbnails.MINI_KIND);
				int degree = PicTool.readPictureDegree(pathStr);

				if (degree != 0) {// 旋转照片角度
					bitmap = PicTool.rotateBitmap(bitmap, degree);
				}
				bitmap = ThumbnailUtils.extractThumbnail(bitmap, 200, 200,
						ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
				video.setImageBitmap(bitmap);
			}

			ImageButton op = (ImageButton) v.findViewById(R.id.op);
			op.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					File file = new File(pathStr);
					if (file.exists()) {

						Intent intent = new Intent(context,
								VideoPlayActivity.class);
						intent.putExtra("uri", pathStr);
						intent.putExtra("action", "main");
						// TODO 修改参数
						context.startActivity(intent);

					}
				}
			});

		}

		private void playVoice(View v, final String path,String durationStr) {
			if (path != null && !path.equals("")) {

				RelativeLayout voicemessage = (RelativeLayout) v
						.findViewById(R.id.voicemessage);
				voicemessage.setVisibility(View.VISIBLE);
				final ImageView voice = (ImageView) v.findViewById(R.id.voice);
				LinearLayout voiceimg = (LinearLayout) v
						.findViewById(R.id.voiceimg);
				if(durationStr!=null&&!durationStr.equals("")){
					TextView duration=(TextView) v.findViewById(R.id.duration);
					duration.setText(durationStr.substring(10)+ "''");
				}
				voiceimg.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						
						try {
							player.reset();
							player.setDataSource(path);
							player.prepare();

						} catch (IOException e) {
							e.printStackTrace();
							return;
						}
						if (player != null)
							player.setOnCompletionListener(new OnCompletionListener() {

								@Override
								public void onCompletion(MediaPlayer arg0) {
									// if(ad!=null){
									ad.stop();
									voice.setBackgroundResource(R.drawable.audio3_l);
									// }
								}
							});
						voice.setBackgroundResource(R.drawable.audio_list_l);
						ad = (AnimationDrawable) voice.getBackground();
						if (player.isPlaying()) {
							ad.stop();
							player.stop();
							player.release();
							voice.setBackgroundResource(R.drawable.audio3_l);
							// isPause = true;
						} else {
							ad.start();
							player.start();
							// isPause = false;

						}
					}
				});

			}
		}

		
		protected class MyPicClickListener implements OnClickListener {

			Dynamic dy;

			public MyPicClickListener(Dynamic beam) {
				dy = beam;
			}

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (dy != null) {
					RegistBean registbean = new RegistBean();
					registbean.setId(dy.getId());
					registbean.setType(dy.getUsertype());
					registbean.setNickname(dy.getNickname());
					registbean.setHead_portrait(dy.getHead_portrait());
					Bundle bd = new Bundle();
					if (registbean.getId().equals(
							Session.getInstance().getUser_id())) {
						ListenerManager.notifyTabHostChange();
					} else {
						int resourceid = R.string.otherwatchmeactivity;
						if (registbean.getType().equals(
								ConstantData.Login_Type_Director)) {
							resourceid = R.string.otherwatchdirectoractivity;
							bd.putSerializable(resourceid + "", registbean);
							doActivity(resourceid, bd);

						} else if (registbean.getType().equals(
								ConstantData.Login_Type_Nomal)) {
							resourceid = R.string.otherwatchmeactivity;
							bd.putSerializable(resourceid + "", registbean);
							doActivity(resourceid, bd);
						} else if (registbean.getType().equals(
								ConstantData.Login_Type_Company)) {
							resourceid = R.string.otherwatchcompanyactivity;
							bd.putSerializable(resourceid + "", registbean);
							doActivity(resourceid, bd);
						}
					}
				}
			}

		}
	}

	class Holder {
		ImageView pic;
		TextView name;
		TextView time;
		Button signup;
		TextView signuptime;
		TextView text;
		RelativeLayout sendagain;
		TextView sendagainnum;
		RelativeLayout review;
		TextView reviewnum;
		RelativeLayout praise;
		TextView praisenum;
		ImageView praiseimg;
		MyGridView maingridView;
	}

	@Override
	protected void onDestroy() {
		// if(player.isPlaying()){
		// player.stop();
		// }
		// player.release();
//		ListenerManager.removeFreshDynimacListener(this);
		try{
			bitmap2.recycle();
			bitmap3.recycle();
			bitmap4.recycle();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		super.onDestroy();
	}

	/**
	 * 我要评论或者试镜或者点赞，取消赞（点赞和取消赞是一样的）
	 * 
	 * @param cp
	 *            CommentPraise对象，需setDynamicId,setContent,setCommentType
	 *            评论类型：评论，赞，试镜，值：0/1/2
	 * @param id
	 */

	public void add(CommentPraise cp, final String id, final Button signup) {
		JSONObject params = new JSONObject();
		RequestParams param = new RequestParams();
		try {
			params.put("dynamic_id", cp.getDynamicId()); // params.put("dynamic_id",
															// "3");
			params.put("id", id); // params.put("id", "1");
			params.put("content", cp.getContent());
			params.put("comment_type", cp.getCommentType());// params.put("comment_type",
															// "2");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		param.put("str", params.toString());
		param.put("response", "application/json");
		asyncHttpClient.post(Server_path.addCommentPraise, param,
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
						if (action.equals("1")) {
							if (signup != null) {
								Toast.makeText(MainTab.this, "恭喜您报名成功！",
										Toast.LENGTH_LONG).show();
								signup.setBackgroundResource(R.drawable.main_btnlogodown);
								signup.setText("");
							}

						}
					}

				});

	}

	@Override
	public void OnReturn(String content, IBaseProcess processor) {
		// TODO Auto-generated method stub
		super.OnReturn(content, processor);
		ResultBean result = processor.json2Bean(content);
		if (processor.getProcessorId().equals(getPro.getProcessorId())) {
			if (result.getCode() == Errors.Regist_USER_ERROR) {
				// 用户不存在，则可以注册
				Toast.makeText(this, "用户不存在", Toast.LENGTH_SHORT).show();
			} else {

				if (result.getCode() == Errors.OK) {

					RegistBean regist = (RegistBean) result.getObj();
					// TODO Auto-generated method stub
					Bundle bd = new Bundle();
					if (regist.getId().equals(Session.getInstance().getUser_id())) {
						ListenerManager.notifyTabHostChange();
					} else {
						int resourceid = R.string.otherwatchmeactivity;
						if (regist.getType().equals(
								ConstantData.Login_Type_Director)) {
							resourceid = R.string.otherwatchdirectoractivity;
							bd.putSerializable(resourceid + "", regist);
							doActivity(resourceid, bd);

						} else if (regist.getType().equals(
								ConstantData.Login_Type_Nomal)) {
							resourceid = R.string.otherwatchmeactivity;
							bd.putSerializable(resourceid + "", regist);
							doActivity(resourceid, bd);
						} else if (regist.getType().equals(
								ConstantData.Login_Type_Company)) {
							resourceid = R.string.otherwatchcompanyactivity;
							bd.putSerializable(resourceid + "", regist);
							doActivity(resourceid, bd);
						}
					}
				} else {
					showToast(result.getMessage());
				}
			}
		}
	}

	class MyURLSpan extends ClickableSpan {

		private String mUrl;

		MyURLSpan(String url) {
			mUrl = url;
		}

		@Override
		public void onClick(View widget) {

			getPro = new UserGetProcessor();
			RegistBean bean = new RegistBean();
			bean.setNickname(mUrl);
			HttpCall(getPro, bean);

		}
		@Override
		 public void updateDrawState(TextPaint ds) {
		 super.updateDrawState(ds);
		 ds.setUnderlineText(false);
		 ds.setColor(Color.parseColor("#0091ff"));
		 }
	}

	@Override
	public void onClick(View arg0) {

		if (arg0.getId() == R.id.relative) {
			if (action != null) {
			} else {

				open.setBackgroundResource(R.drawable.navigationbar_arrow_up);
				final String[] items = { "全部动态", "招募动态", "普通动态" };

				Dialog dialog = new AlertDialog.Builder(this)
				// .setSingleChoiceItems(items, checkedItem, listener)
				// .setSingleChoiceItems(itemsId, checkedItem, listener)
				// .setSingleChoiceItems(adapter, checkedItem, listener)
				// .setSingleChoiceItems(cursor, checkedItem, labelColumn,
				// listener)
				// labelColumn如果数据源是数据集
				// 数据集中的某一列会作为列表对话框的数据加载的列表框中，该参数表示该列的名称(字段名称)
						.setSingleChoiceItems(items, selectedItem,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if (which == 0) {
											adapter = new DynamicListAdapter(
													MainTab.this, dys);
											listview.setAdapter(adapter);
											adapter.notifyDataSetChanged();
											relative.setBackgroundResource(R.drawable.logo);
											relative.setText("");
											dialog.dismiss();
											selectedItem = 0;
											open.setBackgroundResource(R.drawable.navigationbar_arrow_down);
										} else if (which == 1) {
											ArrayList<Dynamic> eds = new ArrayList<Dynamic>();
											if (dys != null && dys.size() > 0) {
												for (int i = 0; i < dys.size(); i++) {
													if (dys.get(i).getType()
															.equals("1")) {
														eds.add(dys.get(i));
													}
												}
												adapter = new DynamicListAdapter(
														MainTab.this, eds);
												listview.setAdapter(adapter);
												adapter.notifyDataSetChanged();
												relative.setText(items[which]);
												relative.setBackgroundResource(0);
												dialog.dismiss();
												selectedItem = 1;
												open.setBackgroundResource(R.drawable.navigationbar_arrow_down);
											}
										} else if (which == 2) {
											ArrayList<Dynamic> eds = new ArrayList<Dynamic>();
											if (dys != null && dys.size() > 0) {
												for (int i = 0; i < dys.size(); i++) {
													if (!dys.get(i).getType()
															.equals("1")) {
														eds.add(dys.get(i));
													}
												}
												adapter = new DynamicListAdapter(
														MainTab.this, eds);
												listview.setAdapter(adapter);
												adapter.notifyDataSetChanged();
												relative.setText(items[which]);
												relative.setBackgroundResource(0);
												dialog.dismiss();
												selectedItem = 2;
												open.setBackgroundResource(R.drawable.navigationbar_arrow_down);
											}
										}

									}
								}).show();

			}
		}

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 1 && requestCode == 0) {
			action=data.getStringExtra("action");
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

//	@Override
//	public void freshDynamic() {
//		// TODO Auto-generated method stub
//		//刷新界面
//		
//		dys.clear();
//		dys = new ArrayList<Dynamic>();
//		refreshNum = 1;
//		Tool tool = new Tool();
//		tool.setConfig(id, refreshNum + "");// TODO
//		tool.execute(Server_path.getListDynamicManage);
//	}

//	@Override
//	public int getListenerId() {
//		// TODO Auto-generated method stub
//		return R.string.maintab;
//	}
}
