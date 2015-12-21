package com.example.casting.publisheddynamic;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.webkit.WebView;

import com.example.casting.entity.Dynamic;
import com.example.casting.util.Server_path;
import com.example.casting.util.ThinkAndroidBaseActivity;
import com.ta.util.http.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import org.json.JSONException;
import org.json.JSONObject;

public class DynamicManage extends ThinkAndroidBaseActivity {
	class UploadTool extends AsyncTask<String, Integer, String> {
     
		private String id;
		private Dynamic dy; 
		private Map<String, Object> paths;
		public void setPaths(String id,Dynamic dy,Map<String, Object> paths){
			this.id=id;
			this.dy=dy;
			this.paths=paths;
		}
		protected String doInBackground(String... params) {
			String content = upload(id,paths);
			return content;
		}

		protected void onPostExecute(String result) {
			if (result != null){
				try {
					result = new String(result.getBytes("ISO-8859-1"), "utf-8");
					String action = null;
					try {
						JSONObject jsonResult = new JSONObject(result);
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
						e.printStackTrace();
					}
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
			}
		}

	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showWebView = new WebView(this);
		setContentView(showWebView);
	}

	protected void onAfterOnCreate(Bundle savedInstanceState) {
		showWebView = new WebView(this);
		setContentView(showWebView);
	}

	protected void onAfterSetContentView() {
		// add(null, true);
		UploadTool tool=new UploadTool();
		
		String id="1";
		
		Dynamic dy=new Dynamic();
//        dy.setIsForwarding(true);
//        dy.setForwardingId("1");
//        dy.setForwarding_content("此条动态是转发动态");
        dy.setFileType("image");//文件类型,值：image,video,voice
        dy.setContent("现在发布动态");
        dy.setType("0");//动态类型:普通动态和招募动态,值：0,1
		
		Map<String, Object> paths = new HashMap<String, Object>();
        paths.put("1.jpg",new File("/mnt/sdcard/sina/weibo/weibo_filter/1.jpg"));
        paths.put("2.jpg",new File("/mnt/sdcard/sina/weibo/weibo_filter/2.jpg"));
        
		tool.setPaths(id,dy,paths);
		tool.execute();
		super.onAfterSetContentView();
	}

	/**
	 * 上传一个文件请求,用户上传文件，文件限制为"jpg,gif,png,mp4,wav"。
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
	private void uploadOne(final String id,File file,final Dynamic dy) {
		RequestParams param = new RequestParams();
		param.put("id", id);//param.put("id", "1");
		param.put("response", "application/json");
		try {
			param.put("file", file);
		} catch (FileNotFoundException e) {
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
									e.printStackTrace();
								}
							}

						});
	}

	/**
	 * 
	 * 上传多个文件请求,用户上传文件，文件限制为"jpg,gif,png,mp4,wav"。
	 * @param id
	 * @param paths   Map<String, Object> paths = new HashMap<String, Object>();
		              paths.put("1.jpg",new File("/mnt/sdcard/sina/weibo/weibo_filter/1.jpg"));
		              paths.put("2.jpg",new File("/mnt/sdcard/sina/weibo/weibo_filter/2.jpg"));
	 * @return
	 */
	@SuppressLint("SdCardPath")
	private String upload(String id, Map<String, Object> paths) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("id", id);//param.put("id", "1");
		param.put("response", "application/json");
		
		try {
			return post(
					Server_path.uploadFile,
					param, paths);
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
			params.put("type", dy.getType());//params.put("type", "1");
			if(dy.getType().equals("0")){ 
			    params.put("up_to_date", dy.getUpToDate());
			 }
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
			if (dy.getIsForwarding()) {
				params.put("is_forwarding", "1");
				params.put("forwarding_id", dy.getForwardingId());
				params.put("forwarding_content", dy.getForwarding_content());
			}
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
						String action = null;
						try {
							JSONObject jsonResult = new JSONObject(
									content);
							action = jsonResult.getString("return");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						      showWebView(content);
					}

				});
	}
	 /**
     * 删除某条动态
     */
	private void delete(String dynamic_id) {
		RequestParams param = new RequestParams();
		param.put("dynamic_id", dynamic_id);//param.put("dynamic_id", "26");
		asyncHttpClient
				.post(Server_path.deleteDynamicManage,
						param, new AsyncHttpResponseHandler() {

							public void onSuccess(String content) {
								super.onSuccess(content);
								//TODO      showWebView(content);
							}

						});
	}
	/**
	 * 获取指定用户的动态列表
	 */
	//TODO 
	private void getUserList(String id, String pagenum) {
		RequestParams param = new RequestParams();
		param.put("id", id);//param.put("id", "1");
		param.put("pagenum", pagenum);//param.put("pagenum", "1");
		asyncHttpClient
				.post(Server_path.getUserListDynamicManage,
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
								//TODO      showWebView(new String(bitmapArray));
							}

						});
	}
    /**
     * 获取我和我关注的动态列表
     */
	private void getList(String id,String pagenum) {
		RequestParams param = new RequestParams();
		param.put("id", id);//param.put("id", "1");
		param.put("pagenum", pagenum);//param.put("pagenum", "1");
		asyncHttpClient
				.post(Server_path.getListDynamicManage,
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
								//TODO      showWebView(new String(bitmapArray));
							}

						});
	}
    /**
     * 获取指定动态的转发列表
     */
	private void getForwardList(String dynamic_id, String pagenum) {
		RequestParams param = new RequestParams();
		param.put("dynamic_id", dynamic_id);//param.put("dynamic_id", "3");
		param.put("pagenum", pagenum);//param.put("pagenum", "1");
		asyncHttpClient
				.post(Server_path.getForwardListDynamicManage,
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
								//TODO      showWebView(new String(bitmapArray));
							}

						});
	}
    /**
     * 获取动态详情
     */
	private void getAsynPost(String dynamic_id) {
		RequestParams param = new RequestParams();
		param.put("dynamic_id", dynamic_id);//param.put("dynamic_id", "26");
		asyncHttpClient
				.post(Server_path.getDynamicManage,
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
								//TODO      showWebView(new String(bitmapArray));
							}

						});
	}
    /**
     * 获取我动态的数量
     */
	private void getCountAsynPost(String id) {
		RequestParams param = new RequestParams();
		param.put("id", id);//param.put("id", "1");
		asyncHttpClient
				.post(Server_path.getCountDynamicManage,
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
								//TODO      showWebView(action);
							}

						});
	}

	private void showWebView(String content) {
		showWebView.getSettings().setDefaultTextEncodingName("utf-8");
		showWebView.loadDataWithBaseURL(null, content, "text/html", "utf-8",
				null);
	}

	WebView showWebView;
	private AsyncHttpClient asyncHttpClient;

}
