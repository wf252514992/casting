package com.example.casting_android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.casting.login.LoginForm;
import com.example.casting.util.CustomHttpClient;
import com.example.casting.util.Server_path;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AdActivity extends Activity {
	TextView tg;
	Bitmap bitmap;
	ImageView ad1;
	Thread thread;
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			tg();
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ad);
		ad1 = (ImageView) findViewById(R.id.ad1);
		tg = (TextView) findViewById(R.id.tg);
		tg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tg();
			}
		});
		getAdManage();
	}
	
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
				try {
					JSONObject obj = new JSONObject(result);
					String ad1Str=obj.optString("ad1");
					ImageLoader.getInstance().displayImage(Server_path.serverfile_path+ad1Str, ad1);
//					bitmap=BitmapFactory.decodeStream(new java.net.URL(Server_path.serverfile_path+ad1Str).openConnection().getInputStream());
//					tg.setBackgroundDrawable(new BitmapDrawable(bitmap));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}.execute(Server_path.getAdmanage);
	}

	private boolean gotologin = false;
	public void tg() {
		if(gotologin)return;
		gotologin= true;
		Intent intent = new Intent(this, LoginForm.class);
		startActivity(intent);
		finish();
		
	}
	
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendMessage(new Message());
				
			}
		});
		thread.start();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		try{
			thread=null;
			bitmap.recycle();
		}catch(Exception e){
		}
		super.onDestroy();
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
}
