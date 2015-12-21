package com.example.casting.publisheddynamic;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import cn.smssdk.gui.CommonDialog;
import com.example.casting.MainTab;
import com.example.casting.RelationalshipActivity;
import com.example.casting.entity.Dynamic;
import com.example.casting.login.BaseForm;
import com.example.casting.util.ConstantData;
import com.example.casting.util.PicTool;
import com.example.casting.util.Server_path;
import com.example.casting.util.Session;
import com.example.casting_android.R;
import com.example.casting_android.bean.Expression;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.RequestParams;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;

/**
 * 发布图片动态界面
 * 
 * @author chenjiaping
 * 
 */

public class EditPhotoDynamic extends BaseForm implements
		android.view.View.OnClickListener {

	// @TAInjectView(id = R.id.titlelayout)
	View titleLayout;
	// @TAInject
	private AsyncHttpClient asyncHttpClient;
	public  ArrayList<String> filelist;
	private EditText editdynamicedit;

	// --表情--//
	int columns = 6, rows = 3, pageExpressionCount = 3 * 6 - 1;
	ViewPager vp_id;
	LinearLayout ll_expression;
	public LinearLayout ll_vp_selected_index;
	ArrayList<GridView> grids;
	MyPagerAdapter myPagerAdapter;
	private GridView imgGridView;
	public static int endNum=0;
	private ImgsAdapter imgsAdapter;
	private Uri imageUri;
	private String fileName;

	// private TextView adress;
	// private ImageView adressimg;
	// private LinearLayout location;
	// private boolean mIsStart;
	// private LocationClient mLocClient;
	// private Vibrator mVibrator01 = null;
	// private JSONArray array;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.publisheddynamicedit);
		titleLayout = findViewById(R.id.titlelayout);

		initView(titleLayout);
		setLeftButtonAble(true, "返回");
		setRightButtonAble(true, "确定");
		setTitle("发动态");
		asyncHttpClient = new AsyncHttpClient();
		init();
		// getAddress();
	}

	private void init() {
		Intent intent = getIntent();
		// final EditText editText=(EditText)
		// findViewById(R.id.editdynamicedit);
		// // 获取编辑框焦点
		// editText.setFocusable(true);
		// 打开软键盘 对于刚跳到一个新的界面就要弹出软键盘的情况上述代码
		// 可能由于界面为加载完全而无法弹出软键盘。此时应该适当的延迟弹出软键盘如998毫秒（保证界面的数据加载完成）
		// Timer timer = new Timer();
		// timer.schedule(new TimerTask()
		// {
		//
		// public void run()
		// {
		// InputMethodManager inputManager =
		// (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		// inputManager.showSoftInput(editText, 0);
		// }
		//
		// },
		// 198);
		LinearLayout share = (LinearLayout) findViewById(R.id.share);
		share.setOnClickListener(this);

		imgGridView = (GridView) findViewById(R.id.gridView1);
		filelist = intent.getStringArrayListExtra("filelist");
		
		// TODO 加图片时
		endNum = 9 - filelist.size();
		if(filelist.size()<9){
			filelist.add("");
		}
		
		if (filelist != null && filelist.size() > 0) {
			
			imgGridView.setVisibility(View.VISIBLE);
			imgsAdapter = new ImgsAdapter(this, filelist, null);
			imgsAdapter.setAction("edit", imgGridView);
			imgGridView.setAdapter(imgsAdapter);
		}
		imgGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (position == filelist.size() - 1) {
					// TODO 添加按钮事件
					if (endNum > 0) {
						Intent intent = new Intent(EditPhotoDynamic.this, ImgsActivity.class);
						intent.putExtra("action", "add");
						intent.putExtra("endNum", endNum);
						startActivityForResult(intent, 2);
					} else {
						Toast.makeText(EditPhotoDynamic.this, "最多添加9张图片",
								Toast.LENGTH_LONG).show();
					}
				}

			}

		});

		ImageButton atsend = (ImageButton) findViewById(R.id.atsend);
		atsend.setOnClickListener(this);
		editdynamicedit = (EditText) findViewById(R.id.editdynamicedit);
		editdynamicedit.setOnClickListener(this);
		ImageButton expression = (ImageButton) findViewById(R.id.expression);
		expression.setOnClickListener(this);
		ll_vp_selected_index = (LinearLayout) findViewById(R.id.ll_vp_selected_index);
		ll_expression = (LinearLayout) findViewById(R.id.ll_expression);
		vp_id = (ViewPager) findViewById(R.id.vp_id);
		vp_id.setOnPageChangeListener(new MyOnPageChangeListener());
		ImageButton btn_photos = (ImageButton) findViewById(R.id.btn_photos);
		btn_photos.setOnClickListener(this);
		ImageButton btn_voice = (ImageButton) findViewById(R.id.btn_voice);
		btn_voice.setVisibility(View.GONE);
		
		editdynamicedit
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
		// adress = (TextView) findViewById(R.id.adress);
		// adressimg = (ImageView) findViewById(R.id.adressimg);

	}

	// private void getAddress() {
	// // mIsStart = false;
	//
	// mLocClient = CastingApplication.mLocationClient;
	// CastingApplication.mTv = adress;
	// mVibrator01 = (Vibrator) getApplication().getSystemService(
	// Service.VIBRATOR_SERVICE);
	// CastingApplication.mVibrator01 = mVibrator01;
	// // location.setOnClickListener(this);
	// if (!ConnectionUtil.isNetworkAvailable(getApplicationContext())) {
	// adressimg.setImageResource(R.drawable.release_location);
	// } else {
	// adressimg.setImageResource(R.drawable.release_locationselected);
	// }
	// setLocationOption();
	// mLocClient.start();
	// // mIsStart = true;
	// if (mLocClient != null && mLocClient.isStarted()) {
	// setLocationOption();
	// mLocClient.requestLocation();
	// }
	// setLocationOption();
	// mLocClient.requestPoi();
	//
	// }
	// private void setLocationOption() {
	// LocationClientOption option = new LocationClientOption();
	// option.setOpenGps(true);
	// option.setServiceName("com.baidu.location.service_v2.9");
	// option.setPoiExtraInfo(true);
	// option.setAddrType("all");
	// option.setPriority(LocationClientOption.GpsFirst);
	// option.setPoiNumber(10);
	// option.disableCache(true);
	// mLocClient.setLocOption(option);
	// }
	@Override
	public void LeftButtonClick() {
		// Intent intent=new Intent(this,MainTabNew.class);
		// startActivity(intent);
		finish();
	}

	@Override
	public void RightButtonClick() {
		UploadTool tool = new UploadTool();
		tool.execute();

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
	public String encodeBase64File(String path) throws Exception {
		File file = new File(path);
		FileInputStream inputFile = new FileInputStream(file);
		byte[] buffer = new byte[(int) file.length()];
		inputFile.read(buffer);
		inputFile.close();
		return Base64.encodeToString(buffer, Base64.DEFAULT);
	}

	/**
	 * 字节数据
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String encodeBase64Byte(byte[] data) throws Exception {
		return Base64.encodeToString(data, Base64.DEFAULT);
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
//							Toast.makeText(EditPhotoDynamic.this, ""+s_image_url, Toast.LENGTH_LONG).show();
							// 文件类型,值：image,video,voice
							if (dy.getFileType() != null
									&& dy.getFileType().equals("image")) {
								dy.setImageUrl(url);
								dy.setSImageUrl(s_image_url);
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
							Toast.makeText(EditPhotoDynamic.this,
									"发布失败，请稍后重试！", Toast.LENGTH_LONG).show();
							// e.printStackTrace();
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

	class UploadTool extends AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {

			JSONArray array = new JSONArray();

//			if(filelist.size()==1){
//
//				JSONObject obj = new JSONObject();
//				try {
//					obj.put("type", "jpg");
//					String path=filelist.get(0).substring(
//							filelist.get(0).lastIndexOf("/") + 1);
//					obj.put("content", encodeBase64File(Server_path.SavePath
//							+path));
//				} catch (JSONException e) {
//					e.printStackTrace();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				array.put(obj);
//				// File file=new File(path);
//				// paths.put(filelist.get(i).substring(filelist.get(i).lastIndexOf("/")+1),
//				// file);
//			
//			}else{
			if (filelist != null && filelist.size() > 0) {
				filelist.remove("");
				for (int i = 0; i < filelist.size(); i++) {
					File outputFile = new File(Server_path.SavePath
							+ filelist.get(i).substring(
									filelist.get(i).lastIndexOf("/") + 1));
					String path = null;

					try {

						path = MyPicTool.compressImage(EditPhotoDynamic.this,
								filelist.get(i), Server_path.SavePath,
								outputFile);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
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
//		}

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
				// dy.setLocation(adress.getText().toString());
				dy.setLocation("");
				TextView shareRange = (TextView) findViewById(R.id.shareRange);
				// 公开0，我关注的人1，仅自己可见2
				String shareRangeStr = shareRange.getText().toString();
				if (shareRangeStr.equals("公开")) {
					dy.setOpen_permissions("0");
				} else if (shareRangeStr.equals("我关注的人")) {
					dy.setOpen_permissions("1");
				} else if (shareRangeStr.equals("仅自己可见")) {
					dy.setOpen_permissions("2");
				}

				String content = editdynamicedit.getText().toString();
				dy.setContent(content);
				dy.setType("0");// 动态类型:普通动态和招募动态,值：0,1

				if (filelist != null && filelist.size() > 0) {
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
			String location = dy.getLocation();
			if (location.equals("显示位置")) {
				params.put("location", "");
			} else {
				params.put("location", dy.getLocation());
			}
			params.put("open_permissions", dy.getOpen_permissions());
			params.put("type", dy.getType());// params.put("type", "1");
			if (dy.getImageUrl() != null) {
				params.put("file_type", "image");
//				params.put("image_url", dy.getImageUrl());
				params.put("s_image_url", dy.getSImageUrl());
			}
			if (dy.getVideoUrl() != null) {
				params.put("file_type", "video");
//				params.put("video_url", dy.getVideoUrl());
				params.put("s_image_url", dy.getSImageUrl());
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
							String dynamic_id = jsonResult.getString("dynamic_id");
							String row = jsonResult.getString("row");
							if(row!=null&&row.equals("1")){
							Toast.makeText(EditPhotoDynamic.this, "恭喜您发布成功！",
									Toast.LENGTH_LONG).show();
							// Intent intent=new
							// Intent(EditPhotoDynamic.this,MainTabNew.class);
							// startActivity(intent);
							finish();
						} else {
							Toast.makeText(EditPhotoDynamic.this,
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

	/**
	 * 
	 * 上传多个文件请求,用户上传文件，文件限制为"jpg,gif,png,mp4,wav"。
	 * 
	 * @param id
	 * @param paths
	 *            Map<String, Object> paths = new HashMap<String, Object>();
	 *            paths.put("1.jpg",new
	 *            File("/mnt/sdcard/sina/weibo/weibo_filter/1.jpg"));
	 *            paths.put("2.jpg",new
	 *            File("/mnt/sdcard/sina/weibo/weibo_filter/2.jpg"));
	 * @return
	 */
	@SuppressLint("SdCardPath")
	private String upload(String id, Map<String, Object> paths) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("id", id);// param.put("id", "1");
		// param.put("response", "application/json");

		try {
			return post(Server_path.uploadFile, param, paths);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String post(String actionUrl, Map<String, String> params,
			Map<String, Object> upload_file) throws IOException {
		String BOUNDARY = UUID.randomUUID().toString();
		String PREFIX = "--";
		String LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";
		URL uri = new URL(actionUrl);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		conn.setReadTimeout(5000);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type",
				(new StringBuilder(String.valueOf(MULTIPART_FROM_DATA)))
						.append(";boundary=").append(BOUNDARY).toString());
		StringBuilder sb = new StringBuilder();
		for (Iterator iterator = params.entrySet().iterator(); iterator
				.hasNext(); sb.append(LINEND)) {
			java.util.Map.Entry entry = (java.util.Map.Entry) iterator.next();
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append((new StringBuilder(
					"Content-Disposition: form-data; name=\""))
					.append((String) entry.getKey()).append("\"")
					.append(LINEND).toString());
			sb.append((new StringBuilder("Content-Type: text/plain; charset="))
					.append(CHARSET).append(LINEND).toString());
			sb.append((new StringBuilder("Content-Transfer-Encoding: 8bit"))
					.append(LINEND).toString());
			sb.append(LINEND);
			sb.append((String) entry.getValue());
		}

		DataOutputStream outStream = new DataOutputStream(
				conn.getOutputStream());
		outStream.write(sb.toString().getBytes());
		InputStream in = null;
		if (upload_file != null) {
			for (Iterator iterator1 = upload_file.entrySet().iterator(); iterator1
					.hasNext(); outStream.write(LINEND.getBytes())) {
				java.util.Map.Entry file = (java.util.Map.Entry) iterator1
						.next();
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);
				sb1.append((new StringBuilder(
						"Content-Disposition: form-data; name=\"file\"; filename=\""))
						.append((String) file.getKey()).append("\"")
						.append(LINEND).toString());
				sb1.append((new StringBuilder(
						"Content-Type: application/octet-stream; charset="))
						.append(CHARSET).append(LINEND).toString());
				sb1.append(LINEND);
				outStream.write(sb1.toString().getBytes());
				InputStream is = new FileInputStream((File) file.getValue());
				byte buffer[] = new byte[1024];
				for (int len = 0; (len = is.read(buffer)) != -1;)
					outStream.write(buffer, 0, len);

				is.close();
			}

			byte end_data[] = (new StringBuilder(String.valueOf(PREFIX)))
					.append(BOUNDARY).append(PREFIX).append(LINEND).toString()
					.getBytes();
			outStream.write(end_data);
			outStream.flush();
			int res = conn.getResponseCode();
			if (res == 200) {
				in = conn.getInputStream();
				StringBuilder sb2 = new StringBuilder();
				int ch;
				while ((ch = in.read()) != -1)
					sb2.append((char) ch);
				return sb2.toString();
			}
			outStream.close();
			conn.disconnect();
		}
		return null;
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
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(this.getCurrentFocus()
								.getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				ll_expression.setVisibility(View.VISIBLE);
			} else {
				ll_expression.setVisibility(View.GONE);
			}
			break;
		case R.id.editdynamicedit:
			if (ll_expression.getVisibility() == View.VISIBLE) {
				ll_expression.setVisibility(View.GONE);
			}
			break;
		case R.id.btn_photos:
			if (endNum > 0) {
				intent = new Intent(this, ImgsActivity.class);
				intent.putExtra("action", "add");
				intent.putExtra("endNum", endNum);
				startActivityForResult(intent, 2);
			} else {
				Toast.makeText(EditPhotoDynamic.this, "最多添加9张图片",
						Toast.LENGTH_LONG).show();
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
			ImageView shareRangeimg = (ImageView) findViewById(R.id.shareRangeimg);
			shareRangeimg.setImageResource(R.drawable.release_globeselected);
		}
		if (resultCode == Activity.RESULT_OK && requestCode == 1) {
			String str = data.getStringExtra("Name");// str即为回传的值
			editdynamicedit.append("@" + str + " ");
		}
		if (resultCode == Activity.RESULT_OK && requestCode == 2) {
			filelist.remove(filelist.size() - 1);
			ArrayList<String> addfilelist = data
					.getStringArrayListExtra("filelist");
			for (int i = 0; i < addfilelist.size(); i++) {
				filelist.add(addfilelist.get(i));
			}
			if(filelist.size()<9){
				filelist.add("");
			}
			if (filelist != null && filelist.size() > 0) {
				ImgsAdapter imgsAdapter = new ImgsAdapter(this, filelist, null);
				imgsAdapter.setAction("edit", imgGridView);
				imgGridView.setAdapter(imgsAdapter);
			}
		}

//		if (requestCode==3) {
//			Uri selectedImage = data.getData();
//			String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//			Cursor cursor = getContentResolver().query(selectedImage,
//					filePathColumn, null, null, null);
//			cursor.moveToFirst();
//
//			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//			String picturePath = cursor.getString(columnIndex);
//			filelist.remove(filelist.size() - 1);
//			filelist.add(picturePath);
//			filelist.add("");
//			imgsAdapter.notifyDataSetChanged();
//			cursor.close();
//		}
		// 拍照
//		if (requestCode==4){
//			String IMAGE_FILE_LOCATION = ConstantData
//			.getCastingDir() + "Camera/";
//			File file = new File(IMAGE_FILE_LOCATION+fileName);
//			if (file.exists()) {
//				filelist.remove(filelist.size() - 1);
//				filelist.add(IMAGE_FILE_LOCATION+fileName);
//				filelist.add("");
//				imgsAdapter.notifyDataSetChanged();
//			}
//
//		}
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
							int index = editdynamicedit.getSelectionStart();
							Editable edit = editdynamicedit.getEditableText();// 获取EditText的文字
							String content_all = edit.toString();
							String content_forward = content_all.substring(0,
									index);
							Log.v("_____________", "content_all = "
									+ content_all + "|content_forward = "
									+ content_forward + "|");
							String reg = "^[A-Za-z0-9]+$";// TODO 正则表达式
							reg = reg.replace("/", "");
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
								ImageSpan imageSpan = new ImageSpan(
										EditPhotoDynamic.this, e.drableId);
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

	private void show() {
		new AlertDialog.Builder(this)
				.setTitle("选择照片")
				.setItems(new String[] { "拍照", "从相册选取", "取消" },
						new OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								String IMAGE_FILE_LOCATION = ConstantData
										.getCastingDir() + "Camera/";
								switch (which) {
								case 0:
									fileName = System.currentTimeMillis()
											+ ".png";

									imageUri = Uri.parse(IMAGE_FILE_LOCATION
											+ fileName);

									Intent intent = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);
									intent.putExtra(MediaStore.EXTRA_OUTPUT,
											imageUri);

									startActivityForResult(intent, 4);

									break;

								case 1:

									Intent i = new Intent(
											Intent.ACTION_PICK,
											android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
									startActivityForResult(i, 3);
									break;

								case 2:

									break;
								}
							}
						}).show();
	}

}
