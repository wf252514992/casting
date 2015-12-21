package com.example.casting.mailbox;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.smssdk.gui.CommonDialog;

import com.example.casting.DateUtils;
import com.example.casting.MainTab;
import com.example.casting.RefreshableView;
import com.example.casting.ReviewActivity;
import com.example.casting.TextActivity;
import com.example.casting.RefreshableView.PullToRefreshListener;
import com.example.casting.entity.CommentPraise;
import com.example.casting.entity.Dynamic;
import com.example.casting.login.BaseForm;
import com.example.casting.util.PicTool;
import com.example.casting.util.Server_path;
import com.example.casting.util.Session;
import com.example.casting_android.R;
import com.ta.common.TAStringUtils;
import com.ta.util.download.DownloadManager;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.RequestParams;

import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore.Video.Thumbnails;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Html;
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
import android.widget.TextView;
import android.widget.Toast;

/**
 * 评论和赞界面
 * 
 * @author chenjiaping TODO 数据的显示问题，下拉刷新，被评论者头像
 * 
 */
public class ReviewOrPraiseListActivity extends BaseForm {

//	@TAInjectView(id = R.id.titlelayout)
	View titleLayout;
//	@TAInject
	private AsyncHttpClient asyncHttpClient;
	private String op;
	private String id;
	private DownloadManager downloadManager;
	private int lastItem;
	private ReviewListAdapter adapter;
	private int refreshNum = 1;
	private  RefreshableView refreshableView;
	private  ArrayList<Dynamic> dys = new ArrayList<Dynamic>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		downloadManager = DownloadManager.getDownloadManager();
		setContentView(R.layout.revieworpraiselist);
		titleLayout=findViewById(R.id.titlelayout);
		initView(titleLayout);
		setLeftButtonAble(true, "返回");
		asyncHttpClient=new AsyncHttpClient();
		id=Session.getInstance().getUser_id();
		Intent intent = getIntent();
		op = intent.getStringExtra("op");
		if (op.equals("Review")) {
			setTitle("评论");
			getCommentList(id, "0", refreshNum+"");//TODO 
		} else if (op.equals("Praise")) {
			setTitle("赞");
			getCommentList(id, "1", refreshNum+"");//TODO 
		}
		// init();

	}

//	Dialog dialog;
//	/**
//	 * 等待框show
//	 */
//	public void showWaitDialog() {
//		if (dialog == null) {
//			dialog = CommonDialog.ProgressDialog(this);
//
//			// dialog = new ProgressDialog(this);
//			// dialog.setCancelable(false);
//		}
//		if (!dialog.isShowing())
//			dialog.show();
//	}
//
//	/**
//	 * 等待框 dismiss
//	 */
//	public void dismissDialog() {
//		if (dialog != null && dialog.isShowing())
//			dialog.dismiss();
//	}
	
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
	private void getCommentList(final String id, final String type, String pagenum) {
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

											// 0 评论
											// [{"comment_id":"134","comment_content":"嗯嗯嗯嗯嗯",
											// "praise":"","audition":"","comment_date":"2015-02-14 12:14:20.0",
											// "comment_type":"0","dynamic_id":"177","id":"1","title":"",
											// "content":"","file_type":"image","is_forwarding":"0",
											// "forwarding_id":"","forwarding_content":"","image_url":"",
											// "video_url":"","voice_url":"","s_image_url":"",
											// "duration":"000000000000","up_to_date":"",
											// "type":"0","dynamic_time":"2015-02-13 17:07:03.0",
											// "nickname":"test","is_praise":"1","comments":"1","praises":"1",
											// "recruits":"0","forwardeds":"0","recruit_state":"0"},

											// 1 赞
											// [{"comment_id":"19","comment_type":"1","dynamic_id":"155","id":"1",
											// "comment_date":"2015-02-11 20:11:32.0","user_id":"1","nickname":"test",
											// "head_portrait":"","content":"啦咯啦咯啦咯啦咯啦咯啦咯了",
											// "duration":"000000000000","dynamic_time":"2015-02-11 16:52:25.0",
											// "file_type":"","forwarding_content":"好好好好好好好好好好好",
											// "forwarding_id":"","image_url":"","is_forwarding":"1","s_image_url":"",
											// "title":"","type":"0","up_to_date":"","video_url":"","voice_url":"",
											// "is_praise":"1","comments":"0","praises":"1","recruits":"0",
											// "recruit_state":"0","forwardeds":"0"},
											if (item.getString("nickname") != null) {
												Dynamic dy = new Dynamic();
												dy.setNickname(item
														.getString("nickname"));
												dy.setCommenter_nickname(item
														.getString("commenter_nickname"));
												dy.setCommenter_head_portrait(item
														.getString("commenter_head_portrait"));

												String url = Server_path.serverfile_path
														+ item.getString("commenter_head_portrait");
												downloadManager.addHandler(url);

												String head_portrait = item
														.getString("head_portrait");
												dy.setHead_portrait(head_portrait);
												url = Server_path.serverfile_path
														+ head_portrait;
												downloadManager.addHandler(url);
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
												dy.setCommentDate(item
														.getString("comment_date"));
												if (item.getString(
														"comment_type").equals(
														"0")) {
													dy.setCommentContent(item
															.getString("comment_content"));
												}

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
										ListView listview = (ListView) findViewById(R.id.reviewList);
										if (dys.size() > 0) {
											listview.setOnScrollListener(new OnScrollListener() {

												@Override
												public void onScrollStateChanged(
														AbsListView view,
														int scrollState) {

													if (lastItem == adapter
															.getCount() - 1
															&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
//														dys.clear();
//														dys = new ArrayList<Dynamic>();
														refreshNum = refreshNum + 1;
														getCommentList(id,type,refreshNum + "");
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
											if(adapter==null){
												adapter = new ReviewListAdapter(
														ReviewOrPraiseListActivity.this,
														dys);
												listview.setAdapter(adapter);
											}
											adapter.notifyDataSetChanged();
											refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);
											refreshableView.setOnRefreshListener(
												   new PullToRefreshListener() {
														@Override
														public void onRefresh() {
															if (refreshableView != null) {
																refreshableView.finishRefreshing();
															}
//														  Looper.prepare();
//														  dys = new ArrayList<Dynamic>();
//														  refreshNum = 1;
//														  getCommentList(id,"1",refreshNum+ "");
														}
													}, 0);
											listview.setOnItemClickListener(new OnItemClickListener() {

												@Override
												public void onItemClick(
														AdapterView<?> arg0,
														View arg1,
														int position, long id) {
													Intent intent = new Intent(
															ReviewOrPraiseListActivity.this,
															TextActivity.class);
													intent.putExtra("dy",
															dys.get(position));
													startActivity(intent);
												}

											});
										}

									} else {
										if(refreshNum!=1){
												Toast.makeText(ReviewOrPraiseListActivity.this, "亲，没有更多消息了！", Toast.LENGTH_LONG).show();
										}else{
										TextView nomessage = (TextView) findViewById(R.id.nomessage);
										nomessage.setVisibility(View.VISIBLE);
										if (op.equals("Review")) {
											nomessage.setText("您的动态暂时还没有评论！");
										} else {
											nomessage.setText("您的动态暂时还没有被赞过！");
										}
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

	// private void init(){
	// //listview
	// ListView listview=(ListView) findViewById(R.id.reviewList);
	// ArrayList<HashMap<String,Object>> list=new
	// ArrayList<HashMap<String,Object>>();
	// HashMap<String, Object> map = new HashMap<String, Object>();
	// map.put("name","小红");
	// map.put("time","刚刚");
	// map.put("text","视频拍的蛮好！");
	// map.put("pic", R.drawable.ic_launcher);
	// map.put("atpic", R.drawable.ic_launcher);
	// map.put("atname","@副导演");
	// map.put("attext","现在发布视镜通知现在发布视镜通知现在发布视镜通知");
	// list.add(map);
	// map = new HashMap<String, Object>();
	// map.put("name","小红");
	// map.put("time","刚刚");
	// map.put("text","视频拍的蛮好！");
	// map.put("pic", R.drawable.ic_launcher);
	// map.put("atpic", R.drawable.ic_launcher);
	// map.put("atname","@副导演");
	// map.put("attext","现在发布视镜通知现在发布视镜通知现在发布视镜通知");
	// list.add(map);
	// map = new HashMap<String, Object>();
	// map.put("name","小红");
	// map.put("time","刚刚");
	// map.put("text","视频拍的蛮好！");
	// map.put("pic", R.drawable.ic_launcher);
	// map.put("atpic", R.drawable.ic_launcher);
	// map.put("atname","@副导演");
	// map.put("attext","现在发布视镜通知现在发布视镜通知现在发布视镜通知");
	// list.add(map);
	// map = new HashMap<String, Object>();
	// map.put("name","小红");
	// map.put("time","刚刚");
	// map.put("text","视频拍的蛮好！");
	// map.put("pic", R.drawable.ic_launcher);
	// map.put("atpic", R.drawable.ic_launcher);
	// map.put("atname","@副导演");
	// map.put("attext","现在发布视镜通知现在发布视镜通知现在发布视镜通知");
	// list.add(map);
	// ReviewListAdapter adapter=new ReviewListAdapter(this, list);
	// listview.setAdapter(adapter);
	// listview.setOnItemClickListener(new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	// long arg3) {
	//
	// }
	//
	// });
	//
	// }

	class ReviewListAdapter extends BaseAdapter {
		Context context;
		ArrayList<Dynamic> arrayList;

		public ReviewListAdapter(Context context, ArrayList<Dynamic> arrayList) {
			super();
			this.context = context;
			this.arrayList = arrayList;

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
		public View getView(final int position, View v, ViewGroup group) {
			Holder holder=null;
			if(v==null){
				holder=new Holder();
				LayoutInflater inflater = LayoutInflater.from(context);
				v = inflater.inflate(R.layout.revieworpraiselist_item, null);

				holder.pic = (ImageView) v.findViewById(R.id.pic);
				holder.name = (TextView) v.findViewById(R.id.name);
				holder.time = (TextView) v.findViewById(R.id.time);
				holder.text = (TextView) v.findViewById(R.id.text);
				holder.atpic = (ImageView) v.findViewById(R.id.atpic);
				holder.atname = (TextView) v.findViewById(R.id.atname);
				holder.attext = (TextView) v.findViewById(R.id.attext);
				holder.sendagainnum = (TextView) v.findViewById(R.id.sendagainnum);
				holder.reviewnum = (TextView) v.findViewById(R.id.reviewnum);
				holder.praisenum = (TextView) v
						.findViewById(R.id.praisenum);
				holder.praiseimg = (ImageView) v
						.findViewById(R.id.praiseimg);
				holder.replay = (ImageButton) v.findViewById(R.id.replay);
				v.setTag(holder);
			}else {
				holder = (Holder)v.getTag(); 
			}
			String pic =arrayList.get(position).getCommenter_head_portrait();
			if(pic!=null&&!pic.equals("")){
				String url=Server_path.serverfile_path+pic;
				   String path=DownloadManager.FILE_ROOT+TAStringUtils.getFileNameFromUrl(url);
				   Bitmap bitmap=BitmapFactory.decodeFile(path);
				   if(bitmap!=null){
					   holder.pic.setImageBitmap(bitmap);
				   }
			}
			// 相册
			String imageUrl = arrayList.get(position).getImageUrl();
			// 视频播放
			String videoUrl = arrayList.get(position).getVideoUrl();
			String picStr = arrayList.get(position).getHead_portrait();
			// String duration=arrayList.get(position).getDuration();
			if (imageUrl != null && !imageUrl.equals("")) {
				String[] as = imageUrl.split(",");
				String url = Server_path.serverfile_path + as[0];
				String path = DownloadManager.FILE_ROOT
						+ TAStringUtils.getFileNameFromUrl(url);
				holder.atpic.setImageBitmap(BitmapFactory.decodeFile(path));
			} else if (videoUrl != null && !videoUrl.equals("")) {
				String url = Server_path.serverfile_path + videoUrl;
				String path = DownloadManager.FILE_ROOT
						+ TAStringUtils.getFileNameFromUrl(url);
				File file = new File(path);
				if (file.exists()) {
					// 获取视频的缩略图
					Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path,
							Thumbnails.MINI_KIND);
					int degree = PicTool.readPictureDegree(path);

					if(degree!=0){//旋转照片角度
						bitmap=PicTool.rotateBitmap(bitmap,degree);
					}
					bitmap = ThumbnailUtils.extractThumbnail(bitmap, 200, 200,
							ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
					holder.atpic.setImageBitmap(bitmap);
				}
				// TODO videoUrl不是参数，而是下载到本地的路径;2,大小参数的修改
			} else if (picStr != null && !picStr.equals("")) {

				String picPath = DownloadManager.FILE_ROOT
						+ TAStringUtils
								.getFileNameFromUrl(Server_path.serverfile_path
										+ picStr);
				holder.atpic.setImageBitmap(BitmapFactory.decodeFile(picPath));
			}
			holder.name.setText(arrayList.get(position).getCommenter_nickname());
			// TODO 换成getCommentname
			String timeStr = arrayList.get(position).getDynamicTime();
			Date date = new Date();
		    SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    try {
				date = simple.parse(timeStr.substring(0, timeStr.indexOf(".")));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			holder.time.setText(DateUtils.fromToday(date));
			if (op.equals("Review")) {
				holder.text.setVisibility(View.VISIBLE);
				String content = msgConvert(replaceSpaceToCode(arrayList.get(position).getCommentContent()));
				holder.text.setText(Html.fromHtml(content, imageGetter_resource, null));//TODO @、超链接等的处理
			}else{
				holder.text.setVisibility(View.VISIBLE);
				holder.text.setText("赞了这条动态");
			}
//			holder.atname.setText(arrayList.get(position).getNickname());
			String content = msgConvert(replaceSpaceToCode(arrayList.get(position).getContent()));
			holder.attext.setText(Html.fromHtml(content, imageGetter_resource, null));//TODO @、超链接等的处理
			holder.sendagainnum.setText(arrayList.get(position).getForwardeds());
			holder.reviewnum.setText(arrayList.get(position).getComments());
			holder.praisenum.setText(arrayList.get(position).getPraises());
			if (arrayList.get(position).isIs_praise()) {
				holder.praiseimg
						.setBackgroundResource(R.drawable.main_toolbar_praise_);
			} else {
				holder.praiseimg.setBackgroundResource(R.drawable.main_toolbar_unlike);
			}
			holder.replay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					
					 Intent intent=new Intent(ReviewOrPraiseListActivity.this,ReviewActivity.class);
					 intent.putExtra("dynamicId", arrayList.get(position)
								.getDynamicId());
					 if (op.equals("Review")) {
					 intent.putExtra("action", "回复评论");
					 } else if (op.equals("Praise")) {
						 intent.putExtra("action", "回复赞");
					 }
					 startActivity(intent);
					
				}
			});

			LinearLayout bott=(LinearLayout) v.findViewById(R.id.bott);
			bott.setVisibility(View.GONE);
			return v;
		}

	}
	class Holder{
		public ImageView pic ;
		TextView name;
		TextView time;
		TextView text;
		ImageView atpic;
		TextView atname;
		TextView attext;
		TextView sendagainnum;
		TextView reviewnum;
		TextView praisenum;
		ImageView praiseimg;
		ImageButton replay;
	}

	final Html.ImageGetter imageGetter_resource = new Html.ImageGetter() {
		public Drawable getDrawable(String source) {
			Drawable drawable = null;
			int rId = Integer.parseInt(source);
			drawable = getResources().getDrawable(rId);
			int expression_wh = (int)getResources().getDimension(R.dimen.chat_expression_wh);
			drawable.setBounds(0, 0, expression_wh,expression_wh);//设置显示的图像大小
			return drawable;
		};
	};
	public  String replaceSpaceToCode(String str){
		String rt = str.replace(" ", "&nbsp;");
		rt = rt.replace("\n", "<br/>");
		
		return rt;
	}
	private String msgConvert(String content){
		Log.v("_____________", "1content = " + content);
		for (int i = 0; i < MainTab.expressionList.size(); i++) {
			content = content.replace(MainTab.expressionList.get(i).code, "<img src=\""+MainTab.expressionList.get(i).drableId+"\" />");
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
								Toast.makeText(ReviewOrPraiseListActivity.this,
										"恭喜您报名成功！", Toast.LENGTH_LONG).show();
								signup.setBackgroundResource(R.drawable.main_btnlogodown);
							}

						}
					}

				});

	}

	@Override
	public void LeftButtonClick() {
//		Intent intent=new Intent(this,MainTabNew.class);
//		startActivity(intent);
		finish();
	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub

	}

}
