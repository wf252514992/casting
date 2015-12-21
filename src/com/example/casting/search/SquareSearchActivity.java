package com.example.casting.search;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.provider.MediaStore.Video.Thumbnails;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.casting.DateUtils;
import com.example.casting.MainTab;
import com.example.casting.ReviewActivity;
import com.example.casting.SendAgainActivity;
import com.example.casting.SignupedActivity;
import com.example.casting.entity.CommentPraise;
import com.example.casting.entity.Dynamic;
import com.example.casting.entity.RegistBean;
import com.example.casting.entity.SearchBean;
import com.example.casting.listener.ListenerManager;
import com.example.casting.login.BaseForm;
import com.example.casting.processor.Errors;
import com.example.casting.processor.IBaseProcess;
import com.example.casting.processor.Users.UserGetProcessor;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.processor.plaza.SearchProcessor;
import com.example.casting.publisheddynamic.ImgsAdapter;
import com.example.casting.publisheddynamic.VideoPlayActivity;
import com.example.casting.search.adapter.SearchAdapter;
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

public class SquareSearchActivity extends BaseForm implements OnClickListener,
		OnItemClickListener, OnEditorActionListener {

	private int currentPage = 1;
	ImageView img_selector;
	EditText edt_search;
	Button btn_search;
	ListView lstview_hot;
	View titlelayout;
	View popview;
	ListView lstview_popselector;

	List<RegistBean> list = new ArrayList<RegistBean>();
	SearchAdapter listAdapter;
	private DownloadManager downloadManager;
	private ArrayList<Dynamic> dys = new ArrayList<Dynamic>();
	private UserGetProcessor getPro;
	private AnimationDrawable ad;
	private MediaPlayer player;
	private AsyncHttpClient asyncHttpClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		titlelayout = findViewById(R.id.titlelayout);
		img_selector = (ImageView) findViewById(R.id.img_selector);
		edt_search = (EditText) findViewById(R.id.edt_search);
		btn_search = (Button) findViewById(R.id.btn_search);
		lstview_hot = (ListView) findViewById(R.id.lstview_hot);
		btn_search.setOnClickListener(this);
		img_selector.setOnClickListener(this);
		listAdapter = new SearchAdapter(list, this);
		lstview_hot.setAdapter(listAdapter);
		lstview_hot.setOnItemClickListener(this);
		edt_search.setOnEditorActionListener(this);
		initView(titlelayout);
		setLeftButtonAble(true, "返回");
		setTitle("搜索");
		downloadManager = DownloadManager.getDownloadManager();
		asyncHttpClient = new AsyncHttpClient();

	}

	@Override
	public void LeftButtonClick() {
		// TODO Auto-generated method stub

		finish();
	}

	@Override
	public void RightButtonClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.img_selector) {
			showPopwindow(img_selector);
		} else if (R.id.btn_search == arg0.getId()) {
			edt_search.setText("");
			list.clear();
			listAdapter.notifyDataSetChanged();
		}
	}

	PopupWindow ppwindow;

	private void showPopwindow(View parent) {
		if (popview == null) {
			LayoutInflater inflater = LayoutInflater.from(this);
			// 引入窗口配置文件
			popview = inflater.inflate(R.layout.searchpop, null);
			lstview_popselector = (ListView) popview
					.findViewById(R.id.list_selector);
			PoplistAdapter adapter = new PoplistAdapter();
			lstview_popselector.setAdapter(adapter);
			lstview_popselector.setOnItemClickListener(this);
			ppwindow = new PopupWindow(popview, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			ppwindow.setBackgroundDrawable(new BitmapDrawable());
			ppwindow.setOutsideTouchable(true);
			// 设置此参数获得焦点，否则无法点击
			ppwindow.setFocusable(true);
		}
		ppwindow.showAsDropDown(parent);
	}

	// 默认的搜索是 搜索全部
	private int index = 0;
	final String[] values = { "搜索用户", "搜索动态" };
	List<String> myList = Arrays.asList(values);

	class PoplistAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return myList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return myList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			TextView txtview = new TextView(SquareSearchActivity.this);
			txtview.setGravity(Gravity.CENTER);
			txtview.setPadding(0, 5, 0, 5);
			txtview.setTextSize(16l);
			int color = android.R.color.darker_gray;
			if (index == arg0) {
				color = R.color.white;
			}
			txtview.setTextColor(getResources().getColor(color));
			txtview.setText(myList.get(arg0));
			return txtview;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.list_selector) {
			index = arg2;
			edt_search.setHint(myList.get(index));
			ppwindow.dismiss();
		} else if (arg0.getId() == R.id.lstview_hot) {
			RegistBean user = list.get(arg2);
			Bundle bd = new Bundle();
			if (user.getId().equals(Session.getInstance().getUser_id())) {
				ListenerManager.notifyTabHostChange();
				finish();
			} else {
				if (user.getType().equals(ConstantData.Login_Type_Director)) {
					int resourceid = R.string.otherwatchdirectoractivity;
					bd.putSerializable(resourceid + "", user);
					doActivity(resourceid, bd);

				} else if (user.getType().equals(ConstantData.Login_Type_Nomal)) {
					int resourceid = R.string.otherwatchmeactivity;
					bd.putSerializable(resourceid + "", user);
					doActivity(resourceid, bd);
				} else if (user.getType().equals(
						ConstantData.Login_Type_Company)) {
					int resourceid = R.string.otherwatchcompanyactivity;
					bd.putSerializable(resourceid + "", user);
					doActivity(resourceid, bd);
				}
			}
		}

	}

	SearchProcessor searchpro = new SearchProcessor();

	private void search(String name) {
		SearchBean bean = new SearchBean();
		bean.setPagenum(currentPage + "");
		bean.setSearchstr(name);
		bean.setType(getType());
		HttpCall(searchpro, bean);
	}

	private String getType() {
		// 用户搜索 的类型==0，动态搜索的类型 == 9
		if (index == 0) {
			return "0";
		} else {
			return "3";
		}
	}

	@Override
	public void OnReturn(String content, IBaseProcess processor) {
		// TODO Auto-generated method stub
		super.OnReturn(content, processor);
		ResultBean result = processor.json2Bean(content);
		if (result.getCode() == Errors.OK) {
			if (processor.getProcessorId().equals(searchpro.getProcessorId())) {
				
				if(Integer.parseInt(getType())==3){
					doDynamic(content);
				} else {
					list.clear();
					List<RegistBean> registbeans = (List<RegistBean>) result
							.getObj();
					for (RegistBean bean : registbeans) {
						list.add(bean);
					}
					listAdapter = new SearchAdapter(list, this);
					lstview_hot.setAdapter(listAdapter);
					listAdapter.notifyDataSetChanged(list);
				}
			}else if (processor.getProcessorId().equals(getPro.getProcessorId())) {
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

		} else {
			showToast("未查询到相关记录");
		}
	}
	private void doDynamic(String result){
		String action = null;
		try {
			JSONObject jsonResult = new JSONObject(result);
			action = jsonResult.getString("return");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		byte bitmapArray[] = Base64.decode(action, 0);
		String content = new String(bitmapArray);
		if (content != null) {
			try {
				JSONArray obj = new JSONArray(content);
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
//								dy.setRecruit_state(item
//										.getString("recruit_state"));
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
						   DynamicListAdapter adapter = new DynamicListAdapter(SquareSearchActivity.this,
									dys);
						   lstview_hot.setAdapter(adapter);
							adapter.notifyDataSetChanged();
	                 }
				}
			}catch(Exception e){
				
			}
		}
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
					// Toast.makeText(SquareSearchActivity.this, "下载完成",
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
				if (usertype != null && usertype.equals("1")) {
//					if (Integer.parseInt(arrayList.get(position)
//							.getRecruit_state()) >= 1) { // >=1时为已试镜
//						signup.setText("");
//						signup.setBackgroundResource(R.drawable.main_btnlogodown);

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
//								add(cp, arrayList.get(position).getId(), signup);
//							}
//						});
//					}
				} else {
					signup.setText("报名名单");
					signup.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Intent intent = new Intent(SquareSearchActivity.this,
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
				ImgsAdapter imgsAdapter = new ImgsAdapter(context, filelist,
						null);// TODO imageUrl不是参数，而是下载到本地的路径; 转换成List<String>
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
					Intent intent = new Intent(SquareSearchActivity.this,
							SendAgainActivity.class);
					intent.putExtra("dy", arrayList.get(position));
					startActivity(intent);
				}
			});
			review.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(SquareSearchActivity.this,
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
						add(cp, arrayList.get(position).getId(), null);
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
						add(cp, arrayList.get(position).getId(), null);// TODO
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
									Toast.makeText(SquareSearchActivity.this, "恭喜您报名成功！",
											Toast.LENGTH_LONG).show();
									signup.setBackgroundResource(R.drawable.main_btnlogodown);
									signup.setText("");
								}

							}
						}

					});

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
	@Override
	public boolean onEditorAction(TextView v, int arg1, KeyEvent arg2) {
		// TODO Auto-generated method stub
		if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
			InputMethodManager imm = (InputMethodManager) v.getContext()
					.getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			if (edt_search.length() == 0) {
				showToast("请输入搜索信息");
				return false;
			}
			search(edt_search.getText().toString());
		}

		return false;
	}

}