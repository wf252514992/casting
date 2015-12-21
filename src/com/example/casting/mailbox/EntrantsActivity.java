package com.example.casting.mailbox;

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

import cn.smssdk.gui.CommonDialog;

import com.example.casting.DateUtils;
import com.example.casting.MainTab;
import com.example.casting.RefreshableView;
import com.example.casting.ReviewActivity;
import com.example.casting.SendAgainActivity;
import com.example.casting.SignupedActivity;
import com.example.casting.TextActivity;
import com.example.casting.RefreshableView.PullToRefreshListener;
import com.example.casting.entity.BaseBean;
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
import com.ta.util.download.DownLoadCallback;
import com.ta.util.download.DownloadManager;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.RequestParams;

import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore.Video.Thumbnails;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
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
 * 报名者界面
 * 
 * @author chenjiaping TODO 下拉刷新
 * 
 */
public class EntrantsActivity extends BaseForm {

	// @TAInjectView(id = R.id.titlelayout)
	View titleLayout;
	private static EntrantsActivity sInstance;
	// @TAInject
	private AsyncHttpClient asyncHttpClient;
	private String id;
	private DownloadManager downloadManager;
	private AnimationDrawable ad;
	private int lastItem;
	private DynamicListAdapter adapter;
	private int refreshNum = 1;
	private RefreshableView refreshableView;
	private ArrayList<Dynamic> dys = new ArrayList<Dynamic>();
	private UserGetProcessor getPro;
	private MediaPlayer player;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		downloadManager = DownloadManager.getDownloadManager();
		setContentView(R.layout.activity_mainorentrants);
		RelativeLayout ding = (RelativeLayout) findViewById(R.id.ding);
		ding.setVisibility(View.GONE);
		TextView txt = (TextView) findViewById(R.id.txt);
		txt.setVisibility(View.GONE);
		titleLayout = findViewById(R.id.titlelayout);
		initView(titleLayout);
		setLeftButtonAble(true, "返回");
		Intent intent=getIntent();
		String title=intent.getStringExtra("title");
		if(title!=null&&title.equals("我的试镜")){
			setTitle(title);
		}else{
		    setTitle("报名者");
		}
		asyncHttpClient = new AsyncHttpClient();
		// TODO 区分
		id = Session.getInstance().getUser_id();
		Bundle b = intent.getExtras();
		if (b != null) {
			RegistBean baseBean = (RegistBean) b.getSerializable(EntrantsActivity.class.getName());
			id = baseBean.getId();
		}
		titleLayout.setVisibility(View.VISIBLE);
		Button relative = (Button) findViewById(R.id.relative);
		relative.setVisibility(View.GONE);
		// init();
		getCommentList(id, "2", refreshNum + "");
	}

	// private void init() {
	// // listview
	// ListView listview = (ListView) findViewById(R.id.dynamicList);
	// ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,
	// Object>>();
	// HashMap<String, Object> map = new HashMap<String, Object>();
	// map.put("name", "副影视公司");
	// map.put("time", "刚刚");
	// map.put("signuptime", "2014-12-20截止");
	// map.put("text", "现在发布试镜通知，现在发布试镜通知，现在发布试镜通知，现在发布试镜通知，现在发布试镜通知。");
	// map.put("pic", R.drawable.ic_launcher);
	// list.add(map);
	// map = new HashMap<String, Object>();
	// map.put("name", "副影视公司");
	// map.put("time", "刚刚");
	// map.put("signuptime", "2014-12-20截止");
	// map.put("text", "现在发布试镜通知，现在发布试镜通知，现在发布试镜通知，现在发布试镜通知，现在发布试镜通知。");
	// map.put("pic", R.drawable.ic_launcher);
	// list.add(map);
	// map = new HashMap<String, Object>();
	// map.put("name", "副影视公司");
	// map.put("time", "刚刚");
	// map.put("signuptime", "2014-12-20截止");
	// map.put("text", "现在发布试镜通知，现在发布试镜通知，现在发布试镜通知，现在发布试镜通知，现在发布试镜通知。");
	// map.put("pic", R.drawable.ic_launcher);
	// list.add(map);
	// map = new HashMap<String, Object>();
	// map.put("name", "副影视公司");
	// map.put("time", "刚刚");
	// map.put("signuptime", "2014-12-20截止");
	// map.put("text", "现在发布试镜通知，现在发布试镜通知，现在发布试镜通知，现在发布试镜通知，现在发布试镜通知。");
	// map.put("pic", R.drawable.ic_launcher);
	// list.add(map);
	// DynamicListAdapter adapter = new DynamicListAdapter(this, list);
	// listview.setAdapter(adapter);
	// listview.setOnItemClickListener(new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	// long arg3) {
	// Intent intent = new Intent(EntrantsActivity.this,
	// TextActivity.class);
	// startActivity(intent);
	// }
	//
	// });
	//
	// }

	public static EntrantsActivity getInstance() {
		if (null == sInstance) {
			synchronized (EntrantsActivity.class) {
				if (sInstance == null) {
					sInstance = new EntrantsActivity();
				}
			}
		}
		return sInstance;
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
	 * 获取某用户的评论、赞、试镜
	 * 
	 * @param id
	 *            - 用户id
	 * @param type
	 *            - 类型：评论，赞，试镜，值：0/1/2
	 * @param pagenum
	 *            - 页数
	 */
	private void getCommentList(final String id, final String type,
			String pagenum) {
//		showWaitDialog();
		RequestParams param = new RequestParams();
		param.put("id", id); // param.put("dynamic_id", "3");
		param.put("type", type);
		param.put("pagenum", pagenum); // param.put("pagenum", "1");
		asyncHttpClient.post(Server_path.getCommentListCommentPraise, param,
				new AsyncHttpResponseHandler() {

					public void onSuccess(String content) {
						if (refreshableView != null) {
							refreshableView.finishRefreshing();
						}
						super.onSuccess(content);
//						dismissDialog();
						String action = null;
						try {
							JSONObject jsonResult = new JSONObject(content);
							action = jsonResult.getString("return");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						byte bitmapArray[] = Base64.decode(action, 0);
						String str = new String(bitmapArray);
						System.out.println(str);
						String result = new String(bitmapArray);
						if (result != null) {
							try {
								JSONArray obj = new JSONArray(result);
								if (obj != null) {
									if (obj.length() > 0) {

										int length = obj.length();
										int i = 0;
										while (i <= length - 1) {

											JSONObject item = obj
													.getJSONObject(i);

											// [{"comment_id":"136","comment_content":"","praise":"",
											// "audition":"","comment_date":"2015-02-14 12:15:00.0",
											// "comment_type":"2","dynamic_id":"174","id":"1","title":"",
											// "content":"好好好好好好好好好好好","file_type":"",
											// "is_forwarding":"0","forwarding_id":"","forwarding_content":"",
											// "image_url":"","video_url":"","voice_url":"","s_image_url":"",
											// "duration":"000000000000","up_to_date":"2015-04-13 00:00:00.0",
											// "type":"1","dynamic_time":"2015-02-13 09:43:29.0","nickname":"test",
											// "is_praise":"0","comments":"0","praises":"0","recruits":"2",
											// "forwardeds":"0","recruit_state":"2"},
											if (item.getString("nickname") != null) {
												Dynamic dy = new Dynamic();
												dy.setNickname(item
														.getString("nickname"));
												dy.setHead_portrait(item
														.getString("head_portrait"));
												// dy.setForwarding_content(item.getString("forwarding_content"));
												// dy.setForwardingId(item.getString("forwarding_id"));
												// dy.setRecruits(item.getString("recruits"));
												dy.setDynamicTime(item
														.getString("dynamic_time"));
												dy.setType(item
														.getString("type"));
												if (item.getString("is_praise")
														.equals("0")) {
													dy.setIs_praise(false);
												} else {
													dy.setIs_praise(true);
												}
												if (item.getString("type")
														.equals("1")) {
													dy.setRecruit_state(item
															.getString("recruit_state"));
													dy.setUpToDate(item
															.getString("up_to_date"));
												}

												// if(item.getString("is_forwarding").equals("0")){
												// dy.setForwarding(false);
												// }else{
												// dy.setForwarding(true);
												// }
												String type = item
														.getString("file_type");
												dy.setFileType(type);
												dy.setContent(item
														.getString("content"));
												dy.setId(item.getString("id"));
												dy.setForwardeds(item
														.getString("forwardeds"));
												dy.setPraises(item
														.getString("praises"));
												dy.setComments(item
														.getString("comments"));
												// dy.setUsertype(item.getString("user_type"));
												dy.setDynamicId(item
														.getString("dynamic_id"));
												if (type.equals("video")) {
													dy.setVideoUrl(item
															.getString("video_url"));
													dy.setDuration(item
															.getString("duration"));
												} else if (type.equals("image")) {
													dy.setImageUrl(item
															.getString("image_url"));
													dy.setSImageUrl(item
															.getString("s_image_url"));
												} else if (type.equals("voice")) {
													dy.setVoiceUrl(item
															.getString("voice_url"));
												}
												// TODO 下载图片
												dys.add(dy);
											}

											i++;
										}
										ListView listview = (ListView) findViewById(R.id.dynamicList);
										if (dys.size() > 0) {
											listview.setOnScrollListener(new OnScrollListener() {

												@Override
												public void onScrollStateChanged(
														AbsListView view,
														int scrollState) {

													if (lastItem == adapter
															.getCount() - 1
															&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
														refreshNum = refreshNum + 1;
														getCommentList(id,
																type,
																refreshNum + "");
													}
												}

												@Override
												public void onScroll(
														AbsListView view,
														int firstVisibleItem,
														int visibleItemCount,
														int totalItemCount) {

													lastItem = firstVisibleItem
															- 1
															+ visibleItemCount;
												}
											});
											if (adapter == null) {
												adapter = new DynamicListAdapter(
														EntrantsActivity.this,
														dys);
												listview.setAdapter(adapter);
											}
											adapter.notifyDataSetChanged();
											refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);
											refreshableView
													.setOnRefreshListener(
															new PullToRefreshListener() {
																@Override
																public void onRefresh() {
																	if (refreshableView != null) {
																		refreshableView
																				.finishRefreshing();
																	}
																	// Looper.prepare();
																	// dys = new
																	// ArrayList<Dynamic>();
																	// refreshNum=1;
																	// getCommentList(id,
																	// "1",
																	// refreshNum+"");
																}
															}, 0);
											listview.setOnItemClickListener(new OnItemClickListener() {

												@Override
												public void onItemClick(
														AdapterView<?> arg0,
														View arg1,
														int position, long id) {
													Intent intent = new Intent(
															EntrantsActivity.this,
															TextActivity.class);
													intent.putExtra("dy",
															dys.get(position));
													startActivity(intent);
												}

											});
										}
									} else {
										if (refreshNum != 1) {
//											Toast.makeText(
//													EntrantsActivity.this,
//													"亲，没有更多动态了！",
//													Toast.LENGTH_LONG).show();
										} else {
											TextView nomessage = (TextView) findViewById(R.id.nomessage);
											nomessage
													.setVisibility(View.VISIBLE);
										}
									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				});
	}

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
					// Toast.makeText(EntrantsActivity.this, "下载完成",
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
			for (int i = 0; i < MainTab.expressionList.size(); i++) {
				content = content.replace(MainTab.expressionList.get(i).code,
						"<img src=\"" + MainTab.expressionList.get(i).drableId
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
//				if (usertype != null && usertype.equals("1")) {
//					if (Integer.parseInt(arrayList.get(position)
//							.getRecruit_state()) >= 1) { // >=1时为已试镜
						signup.setText("");
						signup.setBackgroundResource(R.drawable.main_btnlogodown);

//					} else {
//						signup.setOnClickListener(new OnClickListener() {
//
//							@Override
//							public void onClick(View arg0) {
//								// 需setDynamicId,setContent,setCommentType
//								CommentPraise cp = new CommentPraise();
//								cp.setCommentType("2");
//								cp.setDynamicId(arrayList.get(position)
//										.getDynamicId());
//								cp.setContent("");
//								add(cp, id, signup);
//							}
//						});
//					}
//				} else {
//					signup.setText("报名名单");
//					signup.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View arg0) {
//							Intent intent = new Intent(EntrantsActivity.this,
//									SignupedActivity.class);
//							intent.putExtra("dynamicId", arrayList
//									.get(position).getDynamicId());
//							startActivity(intent);
//
//						}
//					});
//				}
			}

			String contentStr = arrayList.get(position).getContent();

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
					Intent intent = new Intent(EntrantsActivity.this,
							SendAgainActivity.class);
					intent.putExtra("dy", arrayList.get(position));
					startActivity(intent);
				}
			});
			review.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(EntrantsActivity.this,
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
					Intent intent = new Intent(context, VideoPlayActivity.class);
					intent.putExtra("uri", pathStr);// TODO 修改参数
					context.startActivity(intent);

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

	/**
	 * 我要评论或者试镜或者点赞，取消赞（点赞和取消赞是一样的）
	 * 
	 * @param cp
	 *            CommentPraise对象，需setDynamicId,setContent,setCommentType
	 *            评论类型：评论，赞，试镜，值：0/1/2
	 * @param id
	 */

	public void add(CommentPraise cp, String id, final Button signup) {
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
								Toast.makeText(EntrantsActivity.this,
										"恭喜您报名成功！", Toast.LENGTH_LONG).show();
								signup.setBackgroundResource(R.drawable.main_btnlogodown);
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
	@Override
	public void LeftButtonClick() {
		// Intent intent = new Intent(this, MainTab.class);
		// // intent.putExtra("action", "mail");
		// startActivity(intent);
		finish();
	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub

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
	

}
