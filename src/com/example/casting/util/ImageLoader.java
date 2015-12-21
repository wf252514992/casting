package com.example.casting.util;

import android.os.Bundle;
import android.os.Handler;

import com.example.casting.processor.BaseProcessor;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.processor.img.ImageLoadProcessor;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;

public class ImageLoader {
//	private static final ImageLoader imagloader= new ImageLoader();
	private boolean loadering = false;
	public ImageLoader() {

		asyncHttpClient = new AsyncHttpClient();
	}

	private AsyncHttpClient asyncHttpClient;

	public ResultBean LoadImg(ImageLoadProcessor processor) {
		if (processor != null && !loadering) {
			loadering= true;
			asyncHttpClient.get(processor.getUrl(), new MyResponseHandler(processor));
		}
		return null;
	}

	public static final int  GETRESULT = 10110;
	Handler myviewHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GETRESULT: {
				Bundle bd = (Bundle) msg.obj;
				String result = bd.getString("value");
//				BaseProcessor processor = (BaseProcessor)bd.getSerializable("processor");
//				OnReturn(result, processor);
			}break;
			}
		}
	};
	class MyResponseHandler extends AsyncHttpResponseHandler{
		BaseProcessor processor ;
		public MyResponseHandler(BaseProcessor processor){
			this.processor = processor;
		}
		public void onSuccess(String content) {
			super.onSuccess(content);
			//得到返回数据
			Bundle bd =new Bundle();
			bd.putString("value", content);
			bd.putSerializable("processor", processor);
//			Message msg = new Message() ;
//			msg.what = GETRESULT;
//			msg.obj = bd;
//			subhandler.sendMessage(msg);
		};

		public void onFailure(Throwable error) {
			String errors = error.getMessage();
//			showToast(error.getMessage());
		};

		public void onFinish() {
			loadering = false;
		};
	}

}
