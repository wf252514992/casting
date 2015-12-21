package com.example.casting.publisheddynamic;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.util.Base64;
import android.webkit.WebView;
import com.example.casting.util.Server_path;
import com.example.casting.util.ThinkAndroidBaseActivity;
import com.ta.util.download.DownloadManager;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.RequestParams;

public class VideoManage extends ThinkAndroidBaseActivity {
	
	private AsyncHttpClient asyncHttpClient;
	private DownloadManager downloadManager;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showWebView = new WebView(this);
		setContentView(showWebView);
		asyncHttpClient=new AsyncHttpClient();
		downloadManager = DownloadManager.getDownloadManager();
//		getPhotoList("1", "1");
//		getPhotoCount("1");
//		getVideoList("1",  "1");
		getVideoCount("1" );
	}

	/**
	 * 获取我的相册，动态里上传过的照片 
	 */
	//TODO 
	private void getPhotoList(String id,String pagenum) {
		RequestParams param = new RequestParams();
		param.put("id", id);//param.put("id", "1");
		param.put("pagenum", pagenum);//param.put("pagenum", "1");
		asyncHttpClient
				.post(Server_path.getPhoto,
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
								String result=new String(bitmapArray);
								if (result != null&&!result.equals("")) {
                                  try{
									JSONArray obj = new JSONArray(result);
									if (obj != null) {
										// [{"nickname":"test","forwarding_content":"","forwarding_id":"",
										// "recruits":"0","dynamic_time":"2015-02-09 11:19:55.0",
										// "s_image_url":"","voice_url":"","type":"0","is_praise":"0",
										// "up_to_date":"","is_forwarding":"0","file_type":"image",
										// "content":"","id":"1","forwardeds":"0"," duration":"000000000000",
										// "title":"","dynamic_id":"151","video_url":"","image_url":
										// "files\/1\/images\/N2M3YjVhZDMtYjA1Zi00YmRiLWJjNGMtNjcxODQyMTNjMDQ5.jpg,files\/1\/images\/images\/Y2I3YjIyNmUtNTJlMS00NjEwLWI0NDEtZDVjNDkwYWRjMDU4.jpg","praises":"0","comments":"0"},
										int length = obj.length();
										int i = 0;
										while (i <= length - 1) {
											JSONObject item = obj.getJSONObject(i);
											if (item.getString("nickname") != null) {
												String type = item.getString("file_type");
												if (type.equals("image")) {
													String image_url=item.getString("image_url");
				                                    if(!image_url.equals("")){
				                                    	String[] as = image_url.split(",");
														  for (int j = 0; j < as.length; j++) {
														   String url=Server_path.serverfile_path+as[j];
														   downloadManager.addHandler(url);
														  }
				                                    }
												}
											}
											i++;
										}
									}
								  }catch(Exception e){
									  
								  }
								}
								
							}
						});
	}
	/**
	 * 获取我的相册，动态里上传过的照片的数量
 
	 */
	//TODO 
	private void getPhotoCount(String id) {
		RequestParams param = new RequestParams();
		param.put("id", id);//param.put("id", "1");
		asyncHttpClient
				.post(Server_path.getPhotoCount,
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
								showWebView(action);
							}

						});
	}
	/**
	 * 获取我的视频，动态里上传过的视频 
	 */
	//TODO 
	private void getVideoList(String id,String pagenum) {
		RequestParams param = new RequestParams();
		param.put("id", id);//param.put("id", "1");
		param.put("pagenum", pagenum);//param.put("pagenum", "1");
		asyncHttpClient
				.post(Server_path.getVideo,
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
								String result=new String(bitmapArray);
								if (result != null&&!result.equals("")) {
                                  try{
									JSONArray obj = new JSONArray(result);
									if (obj != null) {
										// [{"nickname":"test","forwarding_content":"","forwarding_id":"",
										// "recruits":"0","dynamic_time":"2015-02-09 11:19:55.0",
										// "s_image_url":"","voice_url":"","type":"0","is_praise":"0",
										// "up_to_date":"","is_forwarding":"0","file_type":"image",
										// "content":"","id":"1","forwardeds":"0"," duration":"000000000000",
										// "title":"","dynamic_id":"151","video_url":"","image_url":
										// "files\/1\/images\/N2M3YjVhZDMtYjA1Zi00YmRiLWJjNGMtNjcxODQyMTNjMDQ5.jpg,files\/1\/images\/images\/Y2I3YjIyNmUtNTJlMS00NjEwLWI0NDEtZDVjNDkwYWRjMDU4.jpg","praises":"0","comments":"0"},
										int length = obj.length();
										int i = 0;
										while (i <= length - 1) {
											JSONObject item = obj.getJSONObject(i);
											if (item.getString("nickname") != null) {
												String type = item.getString("file_type");
												if (type.equals("video")) {
													String video_url=item.getString("video_url");
													if(!video_url.equals("")){
														String url=Server_path.serverfile_path+video_url;
														   downloadManager.addHandler(url);
													}
													 
												} 
											}
											i++;
										}
										List<String> data = new ArrayList<String>();
									}
								  }catch(Exception e){
									  
								  }
								}
								
							}
						});
	}
							
	 /**
	    * 获取我的视频，动态里上传过的的数量
	    */
	//TODO 
	private void getVideoCount(String id) {
		RequestParams param = new RequestParams();
		param.put("id", id);//param.put("id", "1");
		asyncHttpClient
				.post(Server_path.getVideoCount,
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
								showWebView(action);
							}

						});
	}
	private void showWebView(String content) {
		showWebView.getSettings().setDefaultTextEncodingName("utf-8");
		showWebView.loadDataWithBaseURL(null, content, "text/html", "utf-8",
				null);
	}

	WebView showWebView;
}
