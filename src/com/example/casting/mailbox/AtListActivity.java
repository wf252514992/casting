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
import com.example.casting.SendAgainActivity;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @列表界面
 * @author chenjiaping  TODO 数据的显示问题，下拉刷新，被评论者头像
 * 
 */
public class AtListActivity extends BaseForm {

//	@TAInjectView(id = R.id.titlelayout) 
	View titleLayout;
//	@TAInject
	private AsyncHttpClient asyncHttpClient;
	private String id;
	private int lastItem;
	private AtListAdapter adapter;
	private int refreshNum = 1;
	private  RefreshableView refreshableView;
	private  ArrayList<Dynamic> dys = new ArrayList<Dynamic>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.atlist);
		titleLayout=findViewById(R.id.titlelayout);
		initView(titleLayout);
		setLeftButtonAble(true, "返回");
		setTitle("@我的");
		asyncHttpClient=new AsyncHttpClient();
		id=Session.getInstance().getUser_id();
		getAtDynamic(refreshNum+"", Session.getInstance().getName_nick());//TODO
		// init();
	}

	// private void init(){
	// //listview
	// ListView listview=(ListView) findViewById(R.id.atList);
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
	// AtListAdapter adapter=new AtListAdapter(this, list);
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
	class AtListAdapter extends BaseAdapter {
		Context context;
		ArrayList<Dynamic> arrayList;

		public AtListAdapter(Context context, ArrayList<Dynamic> arrayList) {
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
			LayoutInflater inflater = LayoutInflater.from(context);
			v = inflater.inflate(R.layout.revieworpraiselist_item, null);

			ImageView pic = (ImageView) v.findViewById(R.id.pic);
			TextView name = (TextView) v.findViewById(R.id.name);
			TextView time = (TextView) v.findViewById(R.id.time);
			TextView text = (TextView) v.findViewById(R.id.text);
			ImageView atpic = (ImageView) v.findViewById(R.id.atpic);
			TextView atname = (TextView) v.findViewById(R.id.atname);
			TextView attext = (TextView) v.findViewById(R.id.attext);
			// TextView sendagainnum = (TextView) v
			// .findViewById(R.id.sendagainnum);
			// TextView reviewnum = (TextView) v.findViewById(R.id.reviewnum);
			// final TextView praisenum = (TextView) v
			// .findViewById(R.id.praisenum);
			final ImageView praiseimg = (ImageView) v
					.findViewById(R.id.praiseimg);
			RelativeLayout sendagain = (RelativeLayout) v
					.findViewById(R.id.sendagain);
			RelativeLayout review = (RelativeLayout) v
					.findViewById(R.id.review);
			RelativeLayout praise = (RelativeLayout) v
					.findViewById(R.id.praise);
			ImageButton replay = (ImageButton) v.findViewById(R.id.replay);
			replay.setVisibility(View.GONE);
			String url = Server_path.serverfile_path
					+ arrayList.get(position).getHead_portrait();
			String path = DownloadManager.FILE_ROOT
					+ TAStringUtils.getFileNameFromUrl(url);
			pic.setImageBitmap(BitmapFactory.decodeFile(path));
			// 相册
			String imageUrl = arrayList.get(position).getImageUrl();
			// 视频播放
			String videoUrl = arrayList.get(position).getVideoUrl();
			if (imageUrl != null && !imageUrl.equals("")) {
				String[] as = imageUrl.split(",");
				url = Server_path.serverfile_path + as[0];
				path = DownloadManager.FILE_ROOT
						+ TAStringUtils.getFileNameFromUrl(url);
				atpic.setImageBitmap(BitmapFactory.decodeFile(path));
			} else if (videoUrl != null && !videoUrl.equals("")) {
				url = Server_path.serverfile_path + videoUrl;
				path = DownloadManager.FILE_ROOT
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
					atpic.setImageBitmap(bitmap);
				}
				// TODO videoUrl不是参数，而是下载到本地的路径;2,大小参数的修改
			}
			name.setText(arrayList.get(position).getNickname());// TODO换成getCommentname
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
			text.setVisibility(View.VISIBLE);
			String forwarding_content=arrayList.get(position).getForwarding_content();
			if(forwarding_content!=null&&!forwarding_content.equals("")){
				String content = msgConvert(replaceSpaceToCode(forwarding_content));
				text.setText(Html.fromHtml(content, imageGetter_resource, null));
			}else{
				 text.setText("转发动态");		
			}
//			atname.setText(arrayList.get(position).getNickname());
			String content = msgConvert(replaceSpaceToCode(arrayList.get(position)
					.getContent()));
			attext.setText(Html.fromHtml(content, imageGetter_resource, null));// TODO
																				// @、超链接等的处理
			if (arrayList.get(position).isIs_praise()) {
				praiseimg
						.setBackgroundResource(R.drawable.main_toolbar_praise_);
			} else {
				praiseimg.setBackgroundResource(R.drawable.main_toolbar_unlike);
			}
			sendagain.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(AtListActivity.this,
							SendAgainActivity.class);
					intent.putExtra("dy", arrayList.get(position));
					startActivity(intent);
				}
			});
			review.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(AtListActivity.this,
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
						add(cp, id, null);// TODO
						praiseimg
								.setBackgroundResource(R.drawable.main_toolbar_unlike);
						arrayList.get(position).setIs_praise(false);
						// int num = Integer.parseInt(arrayList.get(position)
						// .getPraises()) - 1;
						// praisenum.setText(num + "");
						// arrayList.get(position).setPraises(num + "");
					} else {
						// 需setDynamicId,setContent,setCommentType
						CommentPraise cp = new CommentPraise();
						cp.setCommentType("1");
						cp.setDynamicId(arrayList.get(position).getDynamicId());
						cp.setContent("");
						add(cp, "1", null);// TODO
						praiseimg
								.setBackgroundResource(R.drawable.main_toolbar_praise_);
						arrayList.get(position).setIs_praise(true);
						// int num = Integer.parseInt(arrayList.get(position)
						// .getPraises()) + 1;
						// praisenum.setText(num + "");
						// arrayList.get(position).setPraises(num + "");
					}

				}
			});

			return v;
		}

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
								Toast.makeText(AtListActivity.this,
										"恭喜您报名成功！", Toast.LENGTH_LONG).show();
								signup.setBackgroundResource(R.drawable.main_btnlogodown);
							}

						}
					}

				});

	}

	// class AtListAdapter extends BaseAdapter{
	// Context context;
	// ArrayList<HashMap<String,Object>> arrayList;
	//
	// public AtListAdapter(Context context,ArrayList<HashMap<String,Object>>
	// arrayList){
	// super();
	// this.context=context;
	// this.arrayList=arrayList;
	//
	// }
	// @Override
	// public int getCount() {
	// return arrayList.size();
	// }
	//
	// @Override
	// public Object getItem(int position) {
	// return arrayList.get(position);
	// }
	//
	// @Override
	// public long getItemId(int span) {
	// return span;
	// }
	//
	// @Override
	// public View getView(int position, View v, ViewGroup group) {
	// LayoutInflater inflater=LayoutInflater.from(context);
	// v=inflater.inflate(R.layout.atlist_item, null);
	//
	// ImageView pic=(ImageView) v.findViewById(R.id.pic);
	// TextView name=(TextView) v.findViewById(R.id.name);
	// TextView time=(TextView) v.findViewById(R.id.time);
	// TextView text = (TextView) v.findViewById(R.id.text);
	// ImageView atpic=(ImageView) v.findViewById(R.id.atpic);
	// TextView atname=(TextView) v.findViewById(R.id.atname);
	// TextView attext = (TextView) v.findViewById(R.id.attext);
	//
	// RelativeLayout sendagain = (RelativeLayout)
	// v.findViewById(R.id.sendagain);
	// RelativeLayout review = (RelativeLayout) v.findViewById(R.id.review);
	// RelativeLayout praise = (RelativeLayout) v.findViewById(R.id.praise);
	// if(arrayList.get(position).get("pic")!=null){
	// //TODO 框架图片加载
	//
	// }
	// if(arrayList.get(position).get("atpic")!=null){
	// //TODO 框架图片加载
	//
	// }
	// name.setText(arrayList.get(position).get("name").toString());
	// time.setText(arrayList.get(position).get("time").toString());
	// text.setText(arrayList.get(position).get("text").toString());
	// atname.setText(arrayList.get(position).get("atname").toString());
	// attext.setText(arrayList.get(position).get("attext").toString());
	//
	// sendagain.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// Intent intent=new Intent(AtListActivity.this,SendAgainActivity.class);
	// startActivity(intent);
	// }
	// });
	// review.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// Intent intent=new Intent(AtListActivity.this,ReviewActivity.class);
	// startActivity(intent);
	// }
	// });
	// praise.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	//
	// }
	// });
	//
	// return v;
	// }
	//
	// }

	@Override
	public void LeftButtonClick() {
//		Intent intent = new Intent(this, MainTab.class);
//		// intent.putExtra("action", "mail");
//		startActivity(intent);
		finish();
	}

	@Override
	public void RightButtonClick() {

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
	private void getAtDynamic( String pagenum, String nickname) {
//		showWaitDialog();
		RequestParams param = new RequestParams();
//		param.put("id", id); // param.put("dynamic_id", "3");
		param.put("pagenum", pagenum); // param.put("pagenum", "1");
		param.put("nickname", nickname);
		asyncHttpClient.post(Server_path.getAtDynamic, param,
				new AsyncHttpResponseHandler() {

					public void onSuccess(String content) {
						super.onSuccess(content);
						if(refreshableView!=null){
							refreshableView.finishRefreshing();
						}
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
									if(obj.length()>0){
										//TODO 测试当有数据时，返回的字段是否是这些字段
										int length = obj.length();
										int i = 0;
										while (i <= length - 1) {

											JSONObject item = obj.getJSONObject(i);
											
//											[{"dynamic_id":"48","id":"145","title" :"","content": "@abc :Chhffj" ,
//												"file_type":"","is_forwarding":"1","forwarding_id":"44", 
//												"forwarding_content": "\/s001\/s002\/s003","image_url":"",
//												"video_url":"","voice_url":"","s_image_url":"","up_to_date":"",
//												"type":"0","dynamic_time":"2015-01 - 2 7 14:53:02.0",
//												"nickname":"abc","head_portrait":"","phone":" 1234556788"}]

											if (item.getString("nickname") != null) {
												Dynamic dy = new Dynamic();
												dy.setNickname(item
														.getString("nickname"));
												 dy.setHead_portrait(item.getString("head_portrait"));
												 dy.setForwarding_content(item.getString("forwarding_content"));
												// dy.setForwardingId(item.getString("forwarding_id"));
												// dy.setRecruits(item.getString("recruits"));
												dy.setDynamicTime(item
														.getString("dynamic_time"));
												dy.setType(item.getString("type"));
												if (item.getString("is_praise")
														.equals("0")) {
													dy.setIs_praise(false);
												} else {
													dy.setIs_praise(true);
												}
												if (item.getString("type").equals(
														"1")) {
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
//												dy.setCommentDate(item
//														.getString("comment_date"));
//												if (item.getString("comment_type")
//														.equals("0")) {
//													dy.setCommentContent(item
//															.getString("comment_content"));
//												}

//												dy.setForwardeds(item
//														.getString("forwardeds"));
												dy.setPraises(item
														.getString("praises"));
												dy.setComments(item
														.getString("comments"));
												dy.setDynamicId(item
														.getString("dynamic_id"));
//												dy.setUsertype(item.getString("user_type"));
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
										ListView listview=(ListView) findViewById(R.id.atList);
										if (dys.size() > 0) {
											listview.setOnScrollListener(new OnScrollListener() {

												@Override
												public void onScrollStateChanged(
														AbsListView view, int scrollState) {
													
													if (lastItem == adapter.getCount()- 1 && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
														refreshNum=refreshNum+1;
														getAtDynamic(refreshNum+"", Session.getInstance().getName_nick());
													}
												}

												@Override
												public void onScroll(AbsListView view,
														int firstVisibleItem,
														int visibleItemCount, int totalItemCount) {

													lastItem = firstVisibleItem - 1 + visibleItemCount;
												}
											});
											if(adapter==null){
											adapter = new AtListAdapter(
													AtListActivity.this, dys);
											listview.setAdapter(adapter);
											}
											adapter.notifyDataSetChanged();
											 refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);
											 refreshableView.setOnRefreshListener(new PullToRefreshListener() {
											 @Override
											 public void onRefresh() {
												 if (refreshableView != null) {
														refreshableView.finishRefreshing();
													}
//												 Looper.prepare();
//												 dys = new ArrayList<Dynamic>();
//												 refreshNum=1;
//												 getAtDynamic(refreshNum+"", Session.getInstance().getName_nick());
											 }
											 }, 0);
											listview.setOnItemClickListener(new OnItemClickListener() {

												@Override
												public void onItemClick(
														AdapterView<?> arg0,
														View arg1, int position,
														long id) {
													Intent intent = new Intent(
															AtListActivity.this,
															TextActivity.class);
													intent.putExtra("dy",
															dys.get(position));
													startActivity(intent);
												}

											});
										}
									}else{
										if(refreshNum!=1){
											Toast.makeText(AtListActivity.this, "亲，没有更多消息了！", Toast.LENGTH_LONG).show();
									    }else{
										TextView nomessage=(TextView) findViewById(R.id.nomessage);
										nomessage.setVisibility(View.VISIBLE);
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

}
