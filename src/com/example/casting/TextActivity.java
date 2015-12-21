package com.example.casting;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.example.casting.MainTab.MyURLSpan;
import com.example.casting.MainTab.DynamicListAdapter.MyPicClickListener;
import com.example.casting.entity.CommentPraise;
import com.example.casting.entity.Dynamic;
import com.example.casting.entity.RegistBean;
import com.example.casting.listener.ListenerManager;
import com.example.casting.login.BaseForm;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.Users.UserGetProcessor;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.publisheddynamic.ImgsAdapter;
import com.example.casting.publisheddynamic.VideoPlayActivity;
import com.example.casting.util.ConstantData;
import com.example.casting.util.PicTool;
import com.example.casting.util.Server_path;
import com.example.casting.util.Session;
import com.example.casting.util.view.MyGridView;
import com.example.casting_android.R;
import com.ta.common.TAStringUtils;
import com.ta.util.download.DownloadManager;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.RequestParams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.provider.MediaStore.Video.Thumbnails;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 正文 界面
 * 
 * @author chenjiaping
 * 
 */
public class TextActivity extends BaseForm implements OnClickListener {
	// @TAInjectView(id = R.id.titlelayout)
	private View titleLayout;
	// @TAInjectView(id = R.id.review)
	// private ListView listview;
	private TextView menu1, menu2, menu3;
	private ImageView praiseimg;
	// @TAInject
	private AsyncHttpClient asyncHttpClient;
	private Dynamic dy;
	private String id;
	private AnimationDrawable ad;
	private DownloadManager downloadManager = DownloadManager
			.getDownloadManager();
	private UserGetProcessor getPro;
	private int forwards, reviews, praises;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.text, null);
		setContentView(view);
		titleLayout = findViewById(R.id.titlelayout);
		initView(titleLayout);
		setLeftButtonAble(true, "返回");
//		setRightButtonAble(true, "分享");
		setTitle("正文");
		asyncHttpClient = new AsyncHttpClient();
		id = Session.getInstance().getUser_id();
		init();
	}

	private void init() {
		Intent intent = getIntent();
		dy = (Dynamic) intent.getSerializableExtra("dy");
		ImageView pic = (ImageView) findViewById(R.id.pic);
		TextView name = (TextView) findViewById(R.id.name);
		TextView time = (TextView) findViewById(R.id.time);
		final Button signup = (Button) findViewById(R.id.signup);
		TextView signuptime = (TextView) findViewById(R.id.signuptime);
		TextView text = (TextView) findViewById(R.id.text);
		RelativeLayout sendagain = (RelativeLayout) findViewById(R.id.sendagain);
		menu1 = (TextView) findViewById(R.id.menu1);
		RelativeLayout reviewrel = (RelativeLayout) findViewById(R.id.reviewrel);
		menu2 = (TextView) findViewById(R.id.menu2);
		RelativeLayout praise = (RelativeLayout) findViewById(R.id.praise);
		menu3 = (TextView) findViewById(R.id.menu3);
		praiseimg = (ImageView) findViewById(R.id.praiseimg);
		String url = Server_path.serverfile_path + dy.getHead_portrait();
		String path = DownloadManager.FILE_ROOT
				+ TAStringUtils.getFileNameFromUrl(url);
		pic.setImageBitmap(BitmapFactory.decodeFile(path));
		pic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

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
		});
		ImageView img_cer = (ImageView) findViewById(R.id.img_certext);
		String cer=dy.getCertification();
		if(cer!=null&&cer.equals("1")){
			img_cer.setVisibility(View.VISIBLE);
		}
		name.setText(dy.getNickname());
		String timeStr = dy.getDynamicTime();
		Date date = new Date();
	    SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    try {
			date = simple.parse(timeStr.substring(0, timeStr.indexOf(".")));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		time.setText(DateUtils.fromToday(date));
		if (dy.getType().equals("1")) {
			String uptodate = dy.getUpToDate();
			if (uptodate != null && !uptodate.equals("")
					&& !uptodate.equals("null")) {
				signuptime.setText(uptodate.substring(0, uptodate.indexOf(" "))
						+ "截止报名");
			}
			signuptime.setVisibility(View.VISIBLE);
			signup.setVisibility(View.VISIBLE);
			String usertype = Session.getInstance().getUsertype();// 用户类型:普通用户和导演(1/2)
			// 招募方有报名者，被招募方没有报名者
			if (usertype != null && usertype.equals("1")) {
				if (Integer.parseInt(dy
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
							cp.setDynamicId(dy
									.getDynamicId());
							cp.setContent("");
							add(cp, id, signup, 0);
						}
					});
				}
			} else {
				signup.setText("报名名单");
				signup.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(TextActivity.this,
								SignupedActivity.class);
						intent.putExtra("dynamicId", dy.getDynamicId());
						startActivity(intent);

					}
				});
			}
		}
		String contentStr = dy.getContent();
		String content = msgConvert(replaceSpaceToCode(contentStr));
		Spanned sp = Html.fromHtml(content, imageGetter_resource, null);
		text.setText(sp);// TODO
		text.setMovementMethod(LinkMovementMethod.getInstance());
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
		if(dy.getForwardeds()!=null&&!dy.getForwardeds().equals("")){
			forwards = Integer.parseInt(dy.getForwardeds());
		}
		
		reviews = Integer.parseInt(dy.getComments());
		praises = Integer.parseInt(dy.getPraises());
		menu1.setText("转发  " + forwards);
		menu2.setText("评论  " + reviews);
		menu3.setText("赞  " + praises);
		if (contentStr.equals("")) {
			text.setVisibility(View.GONE);
		}
		// 相册
		String imageUrl = dy.getImageUrl();
		if (imageUrl != null && !imageUrl.equals("")) {

			List<String> filelist = new ArrayList<String>();
			String[] as = imageUrl.split(",");
			for (int j = 0; j < as.length; j++) {
				url = Server_path.serverfile_path + as[j];
				path = DownloadManager.FILE_ROOT
						+ TAStringUtils.getFileNameFromUrl(url);
				filelist.add(path);
			}

			MyGridView maingridView = (MyGridView) findViewById(R.id.maingridView);
			maingridView.setVisibility(View.VISIBLE);
			// maingridView.setPadding(3, 3, 3,
			// 0);
			// maingridView.setHorizontalSpacing(5);
			// maingridView.setVerticalSpacing(3);
			ImgsAdapter imgsAdapter = new ImgsAdapter(this, filelist, null,as);// TODO
																			// imageUrl不是参数，而是下载到本地的路径;
																			// 转换成List<String>
			imgsAdapter.setVisibility(false);
			maingridView.setAdapter(imgsAdapter);
		}
		// 视频播放
		String videoUrl = dy.getVideoUrl();
		 String duration=dy.getDuration();
		if (videoUrl != null && !videoUrl.equals("")) {
			url = Server_path.serverfile_path + videoUrl;
			path = DownloadManager.FILE_ROOT
					+ TAStringUtils.getFileNameFromUrl(url);
			playvideo(path);// TODO videoUrl不是参数，而是下载到本地的路径;2,大小参数的修改
		}
		// 语音
		String voiceUrl = dy.getVoiceUrl();
		if (voiceUrl != null && !voiceUrl.equals("")) {
			url = Server_path.serverfile_path + voiceUrl;
			path = DownloadManager.FILE_ROOT
					+ TAStringUtils.getFileNameFromUrl(url);
			playVoice(path,duration);

		}
		if (dy.isIs_praise()) {
			praiseimg.setBackgroundResource(R.drawable.main_toolbar_praise_);
		} else {
			praiseimg.setBackgroundResource(R.drawable.main_toolbar_unlike);
		}
		sendagain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(TextActivity.this,
						SendAgainActivity.class);
				intent.putExtra("dy", dy);
				intent.putExtra("action", "text");
				startActivityForResult(intent, 0);
			}
		});
		reviewrel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(TextActivity.this,
						ReviewActivity.class);
				intent.putExtra("dynamicId", dy.getDynamicId());
				intent.putExtra("actionstr", "text");
				startActivityForResult(intent, 1);
			}
		});
		praise.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (dy.isIs_praise()) {
					// 需setDynamicId,setContent,setCommentType
					CommentPraise cp = new CommentPraise();
					cp.setCommentType("1");
					cp.setDynamicId(dy.getDynamicId());
					cp.setContent("");
					add(cp, id, null, 1);// TODO

				} else {
					// 需setDynamicId,setContent,setCommentType
					CommentPraise cp = new CommentPraise();
					cp.setCommentType("1");
					cp.setDynamicId(dy.getDynamicId());
					cp.setContent("");

					add(cp, id, null, 2);// TODO

				}

			}
		});
		menu1.setOnClickListener(this);
		menu2.setOnClickListener(this);
		menu3.setOnClickListener(this);

		getForwardList(dy.getDynamicId(), "1");
		// TODO
		menu1.setTextColor(Color.parseColor("#000000"));
		menu2.setTextColor(Color.parseColor("#686868"));
		menu3.setTextColor(Color.parseColor("#686868"));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK && requestCode == 0) {// 转发成功返回
			getForwardList(dy.getDynamicId(), "1");// TODO
			menu1.setTextColor(Color.parseColor("#000000"));
			menu2.setTextColor(Color.parseColor("#686868"));
			menu3.setTextColor(Color.parseColor("#686868"));
			forwards = forwards + 1;
			menu1.setText("转发  " + forwards);
		} else if (resultCode == Activity.RESULT_OK && requestCode == 1) {// 评论成功返回
			getListAsynPost(dy.getDynamicId(), "1", "review");// TODO
			menu2.setTextColor(Color.parseColor("#000000"));
			menu1.setTextColor(Color.parseColor("#686868"));
			menu3.setTextColor(Color.parseColor("#686868"));
			reviews = reviews + 1;
			menu2.setText("评论  " + reviews);
		}
	}

	/**
	 * 
	 * @param v
	 * @param pathStr
	 * @param sizeStr
	 * @param timeStr
	 */
	private void playvideo(final String pathStr) {

		ImageView video = (ImageView) findViewById(R.id.video);
		RelativeLayout videomessage = (RelativeLayout) findViewById(R.id.videomessage);
		videomessage.setVisibility(View.VISIBLE);

		// TextView size=(TextView) findViewById(R.id.size);
		// size.setText(sizeStr);
		// TextView time=(TextView) findViewById(R.id.timeStr);
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

		ImageButton op = (ImageButton) findViewById(R.id.op);
		op.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(TextActivity.this,
						VideoPlayActivity.class);
				intent.putExtra("uri", pathStr);// TODO 修改参数
				intent.putExtra("action", "main");
				startActivity(intent);

			}
		});

	}

	private void playVoice(String path,String durationStr) {
		if (path != null && !path.equals("")) {

			RelativeLayout voicemessage = (RelativeLayout) findViewById(R.id.voicemessage);
			voicemessage.setVisibility(View.VISIBLE);
			final ImageView voice = (ImageView) findViewById(R.id.voice);
			final MediaPlayer player = new MediaPlayer();
			player.reset();
			try {
				player.setDataSource(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
			player.prepareAsync();
			if(durationStr!=null&&!durationStr.equals("")){
				TextView duration=(TextView) findViewById(R.id.duration);
				duration.setText(durationStr.substring(10)+ "''");
			}
			LinearLayout voiceimg = (LinearLayout) findViewById(R.id.voiceimg);
			voiceimg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

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

	final Html.ImageGetter imageGetter_resource = new Html.ImageGetter() {
		public Drawable getDrawable(String source) {
			Drawable drawable = null;
			int rId = Integer.parseInt(source);
			drawable = getResources().getDrawable(rId);
			int expression_wh = (int) getResources().getDimension(
					R.dimen.chat_expression_wh);
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
		for (int i = 0; i < MainTab.expressionList.size(); i++) {
			content = content.replace(MainTab.expressionList.get(i).code,
					"<img src=\"" + MainTab.expressionList.get(i).drableId
							+ "\" />");
		}
		Log.v("_____________", "2content = " + content);
		return content;
	}

	/**
	 * 我要评论或者试镜或者点赞，取消赞（点赞和取消赞是一样的）
	 * 
	 * @param cp
	 *            CommentPraise对象，需setDynamicId,setContent,setCommentType
	 *            评论类型：评论，赞，试镜，值：0/1/2
	 * @param id
	 */

	public void add(CommentPraise cp, final String id, final Button signup,
			final int isPraise) {
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
								Toast.makeText(TextActivity.this, "恭喜您报名成功！",
										Toast.LENGTH_LONG).show();
								signup.setBackgroundResource(R.drawable.main_btnlogodown);
								signup.setText("");
							}

							if (isPraise == 1) {
								praiseimg
										.setBackgroundResource(R.drawable.main_toolbar_unlike);
								dy.setIs_praise(false);
								int num = Integer.parseInt(dy.getPraises()) - 1;
								menu3.setText("赞  " + num + "");
								dy.setPraises(num + "");
								getListAsynPost(dy.getDynamicId(), id, "praise");
							} else if (isPraise == 2) {
								praiseimg
										.setBackgroundResource(R.drawable.main_toolbar_praise_);
								dy.setIs_praise(true);
								int num = Integer.parseInt(dy.getPraises()) + 1;
								menu3.setText("赞  " + num + "");
								dy.setPraises(num + "");
								getListAsynPost(dy.getDynamicId(), id, "praise");
							}
						}

					}
				});

	}

	class ReviewlistAdapter extends BaseAdapter {
		private Context context;
		private ArrayList<CommentPraise> arrayList;
		private boolean isPraise;

		public ReviewlistAdapter(Context context,
				ArrayList<CommentPraise> arrayList) {
			super();
			this.context = context;
			this.arrayList = arrayList;

		}

		public void setIsPraise(boolean isPraise) {
			this.isPraise = isPraise;
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

		@Override
		public View getView(int position, View v, ViewGroup group) {
			LayoutInflater inflater = LayoutInflater.from(context);
			v = inflater.inflate(R.layout.review_item, null);

			ImageView pic = (ImageView) v.findViewById(R.id.pic);
			TextView name = (TextView) v.findViewById(R.id.name);
			TextView time = (TextView) v.findViewById(R.id.time);
			TextView text = (TextView) v.findViewById(R.id.text);
			TextView praiseName = (TextView) v.findViewById(R.id.praiseName);

			String picPath = DownloadManager.FILE_ROOT
					+ TAStringUtils
							.getFileNameFromUrl(Server_path.serverfile_path
									+ arrayList.get(position)
											.getHead_portrait());
			pic.setImageBitmap(BitmapFactory.decodeFile(picPath));
			String nameStr = arrayList.get(position).getNickname();
			name.setText(nameStr);
			String timeStr = arrayList.get(position).getCommentDate();
			Date date = new Date();
		    SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    try {
				date = simple.parse(timeStr.substring(0, timeStr.indexOf(".")));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			time.setText(DateUtils.fromToday(date));
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
			if (isPraise) {
				name.setVisibility(View.GONE);
				time.setVisibility(View.GONE);
				// text.setVisibility(View.GONE);
				text.setText("\n");
				praiseName.setVisibility(View.VISIBLE);
				praiseName.setText(arrayList.get(position).getNickname());
			}
			return v;
		}

	}

	@Override
	public void LeftButtonClick() {
		Intent intent=new Intent();
		intent.putExtra("action", "text");
		setResult(1, intent);
		finish();
	}

	@Override
	public void RightButtonClick() {

		// TODO Auto-generated method stub
		// ShareSDK.initSDK(context);
//		showShare(getString(R.string.share), "http://sharesdk.cn", "我是分享文本",
//				"/storage/emulated/0/ba.jpg", "http://sharesdk.cn", "我是测试评论文本",
//				getString(R.string.app_name), "http://sharesdk.cn");
		/*
		 * ShareParams sp = new ShareParams(); sp.setText("测试分享的文本");
		 * sp.setImagePath("/storage/emulated/0/ba.jpg");
		 * 
		 * Platform weibo = ShareSDK.getPlatform(context, SinaWeibo.NAME);
		 * weibo.setPlatformActionListener(new PlatformActionListener() {
		 * 
		 * @Override public void onError(Platform arg0, int arg1, Throwable
		 * arg2) { // TODO Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void onComplete(Platform arg0, int arg1,
		 * HashMap<String, Object> arg2) { // TODO Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void onCancel(Platform arg0, int arg1) { // TODO
		 * Auto-generated method stub
		 * 
		 * } }); // 设置分享事件回调 // 执行图文分享 weibo.share(sp);
		 */
		// weixinShare("com.tencent.mm");

	}

	/**
	 * 
	 * @param title
	 *            标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
	 * @param titleUrl
	 *            是标题的网络链接，仅在人人网和QQ空间使用
	 * @param Content
	 *            是分享文本，所有平台都需要这个字段
	 * @param imagePath
	 *            是图片的本地路径，Linked-In以外的平台都支持此参数
	 * @param url
	 *            仅在微信（包括好友和朋友圈）中使用
	 * @param comment
	 *            是我对这条分享的评论，仅在人人网和QQ空间使用
	 * @param site
	 *            是分享此内容的网站名称，仅在QQ空间使用
	 * @param siteUrl
	 *            是分享此内容的网站地址，仅在QQ空间使用
	 */
	public void showShare(String title, String titleUrl, String Content,
			String imagePath, String url, String comment, String site,
			String siteUrl) {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(title);
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(titleUrl);
		// text是分享文本，所有平台都需要这个字段
		oks.setText(Content);
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath(imagePath);// 确保SDcard下面存在此张图片

		oks.setImageUrl("http://bbs.szonline.net/UploadFile/album/2011/6/71220/8/20110608093106_50329.jpg");
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(url);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment(comment);
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(site);
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl(siteUrl);

		// 启动分享GUI
		oks.show(this);
	}

	@Override
	public void onBackPressed() {
		Intent intent=new Intent();
		intent.putExtra("action", "text");
		setResult(1, intent);
		finish();
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu1:

			getForwardList(dy.getDynamicId(), "1");// TODO
			menu1.setTextColor(Color.parseColor("#000000"));
			menu2.setTextColor(Color.parseColor("#686868"));
			menu3.setTextColor(Color.parseColor("#686868"));

			break;
		case R.id.menu2:
			getListAsynPost(dy.getDynamicId(), "1", "review");// TODO
			menu2.setTextColor(Color.parseColor("#000000"));
			menu1.setTextColor(Color.parseColor("#686868"));
			menu3.setTextColor(Color.parseColor("#686868"));

			break;
		case R.id.menu3:

			getListAsynPost(dy.getDynamicId(), "1", "praise");// TODO
			menu1.setTextColor(Color.parseColor("#686868"));
			menu2.setTextColor(Color.parseColor("#686868"));
			menu3.setTextColor(Color.parseColor("#000000"));
			break;

		default:
			break;
		}

	}

	/**
	 * 获取某动态的评论、赞、试镜
	 * 
	 * @param dynamic_id
	 *            动态id
	 * @param pagenum
	 *            页数
	 */
	private void getListAsynPost(String dynamic_id, String pagenum,
			final String act) {

		RequestParams param = new RequestParams();
		param.put("dynamic_id", dynamic_id); // param.put("dynamic_id", "3");
		param.put("pagenum", pagenum); // param.put("pagenum", "1");
		asyncHttpClient.post(Server_path.getListCommentPraise, param,
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
						String str = new String(bitmapArray);
						System.out.println(".......str:" + str);
						try {
							JSONArray obj = new JSONArray(str);
							if (obj != null) {

								// [{"comment_id":"12","dynamic_id":"156","id":"1","content":"好好好好好好好好好好好",
								// "praise":"","audition":"","comment_date":"2015-02-11 17:20:53.0",
								// "comment_type":"0","nickname":"test","head_portrait":""},
								// {"comment_id":"39","dynamic_id":"156","id":"1","content":"","praise":"",
								// "audition":"","comment_date":"2015-02-11 20:47:34.0",
								// "comment_type":"1","nickname":"test","head_portrait":""}]
								ArrayList<CommentPraise> cps = new ArrayList<CommentPraise>();
								ArrayList<CommentPraise> praiseCps = new ArrayList<CommentPraise>();
								int length = obj.length();
								int i = 0;
								while (i <= length - 1) {

									JSONObject item = obj.getJSONObject(i);
									if (act != null && act.equals("review")) {
										if (item.getString("comment_type")
												.equals("0")) {
											CommentPraise cp = new CommentPraise();

											// String
											// comment_id=item.getString("comment_id");
											// String
											// dynamic_id=item.getString("dynamic_id");
											// String id=item.getString("id");
											cp.setContent(item
													.getString("content"));
											cp.setPraise(item
													.getString("praise"));// 赞
																			// 停用
											cp.setAudition(item
													.getString("audition"));// 试镜报名
																			// 停用
											cp.setHead_portrait(item
													.getString("head_portrait"));// 头像路径
											cp.setNickname(item
													.getString("nickname"));
											cp.setCommentDate(item
													.getString("comment_date"));
											cps.add(cp);
										}
									} else if (act != null
											&& act.equals("praise")) {
										if (item.getString("comment_type")
												.equals("1")) {
											CommentPraise cp = new CommentPraise();

											// String
											// comment_id=item.getString("comment_id");
											// String
											// dynamic_id=item.getString("dynamic_id");
											// String id=item.getString("id");
											cp.setContent(item
													.getString("content"));
											cp.setPraise(item
													.getString("praise"));// 赞
																			// 停用
											cp.setAudition(item
													.getString("audition"));// 试镜报名
																			// 停用
											cp.setHead_portrait(item
													.getString("head_portrait"));// 头像路径
											cp.setNickname(item
													.getString("nickname"));
											cp.setCommentDate(item
													.getString("comment_date"));
											praiseCps.add(cp);
										}
									}
									String url = Server_path.serverfile_path
											+ item.getString("head_portrait");
									downloadManager.addHandler(url);
									i++;
								}
								ListView listview = (ListView) findViewById(R.id.review);
								if (act != null && act.equals("review")) {
									ReviewlistAdapter adapter = new ReviewlistAdapter(
											TextActivity.this, cps);
									listview.setAdapter(adapter);
									adapter.notifyDataSetChanged();
								} else if (act != null && act.equals("praise")) {
									ReviewlistAdapter adapter = new ReviewlistAdapter(
											TextActivity.this, praiseCps);
									adapter.setIsPraise(true);
									listview.setAdapter(adapter);
									adapter.notifyDataSetChanged();
								}

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				});
	}

	/**
	 * 获取指定动态的转发列表
	 */
	private void getForwardList(String dynamic_id, String pagenum) {
		RequestParams param = new RequestParams();
		param.put("dynamic_id", dynamic_id);// param.put("dynamic_id", "3");
		param.put("pagenum", pagenum);// param.put("pagenum", "1");
		asyncHttpClient.post(Server_path.getForwardListDynamicManage, param,
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
						String str = new String(bitmapArray);
						System.out.println(".......str:" + str);
						try {
							JSONArray obj = new JSONArray(str);
							if (obj != null) {

								ArrayList<CommentPraise> cps = new ArrayList<CommentPraise>();
								int length = obj.length();
								int i = 0;
								while (i <= length - 1) {

									JSONObject item = obj.getJSONObject(i);
									CommentPraise cp = new CommentPraise();

									// String
									// comment_id=item.getString("comment_id");
									// String
									// dynamic_id=item.getString("dynamic_id");
									// String id=item.getString("id");
									cp.setContent(item.getString("content"));
									cp.setHead_portrait(item
											.getString("head_portrait"));
									cp.setNickname(item.getString("nickname"));
									cp.setCommentDate(item
											.getString("dynamic_time"));
									cps.add(cp);
									String url = Server_path.serverfile_path
											+ item.getString("head_portrait");
									downloadManager.addHandler(url);
									i++;
								}
								ListView listview = (ListView) findViewById(R.id.review);
								ReviewlistAdapter adapter = new ReviewlistAdapter(
										TextActivity.this, cps);
								listview.setAdapter(adapter);
								adapter.notifyDataSetChanged();

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				});
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
}
