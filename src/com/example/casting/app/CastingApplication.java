package com.example.casting.app;

import java.io.File;

import android.graphics.Bitmap.CompressFormat;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.smssdk.SMSSDK;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.example.casting.util.ConnectionUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.ta.TAApplication;

public class CastingApplication extends TAApplication{

	private static CastingApplication sInstance;
	public static LocationClient mLocationClient = null;
	public GeofenceClient mGeofenceClient;
	private String mData;  
	public MyLocationListenner myListener = new MyLocationListenner();
	public static TextView mTv;
	public NotifyLister mNotifyer=null;
	public static Vibrator mVibrator01;
	public static String TAG = "LocTestDemo";
	public static CastingApplication getInstance() {
		if (null == sInstance) {
			synchronized (CastingApplication.class) {
				if (sInstance == null) {
					sInstance = new CastingApplication();
				}
			}
		}
		return sInstance;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		SMSSDK.initSDK(this, "469d233d9340", "019f143b9ba3d5549e4d37fb3b4d7279");
		JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        

		mLocationClient = new LocationClient( this );
		mLocationClient.setAK("697f50541f8d4779124896681cb6584d");
		mLocationClient.registerLocationListener( myListener );
		mGeofenceClient = new GeofenceClient(this);
		initImageLoader();
		CrashHandler crashHandler = CrashHandler.getInstance();
		// 注册crashHandler
		crashHandler.init(this);
		// 发�?以前没发送的报告(可�?)
		crashHandler.sendPreviousReportsToServer();
	}

	public void logMsg(String str) {
		try {
			mData = str;
			if ( mTv != null )
				mTv.setText(mData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return ;
			StringBuffer sb = new StringBuffer(1);
			
			   if (!ConnectionUtil.isNetworkAvailable(getApplicationContext())) {
				   sb.append("显示位置");
				}else{
					String city=location.getCity();
					if(city!=null&&!city.equals("")&&!city.equals("null")){
					sb.append(city);
					}else{
						sb.append("显示位置");
					}
				}
				
			logMsg(sb.toString());
			Log.i(TAG, sb.toString());
		}
		
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null){
				return ; 
			}
			StringBuffer sb = new StringBuffer(1);
			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation){
				String city=poiLocation.getCity();
				if(city!=null&&!city.equals("")&&city.equals("null")){
					sb.append(city);
				}else{
					sb.append("显示位置");
				}
				
			} 

			logMsg(sb.toString());
		}
	}
	
	public class NotifyLister extends BDNotifyListener{
		public void onNotify(BDLocation mlocation, float distance){
			mVibrator01.vibrate(1000);
		}
	}
	 private void initImageLoader(){
	    	File cacheDir = StorageUtils.getCacheDirectory(this);
//	    	  ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(this);
	        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
	                .threadPoolSize(3) // default
	                .threadPriority(Thread.NORM_PRIORITY - 2) // default
	                .diskCacheExtraOptions(480, 800, null)  
	                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
	                .denyCacheImageMultipleSizesInMemory()
//	                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
	                .memoryCache(new WeakMemoryCache())
	                .memoryCacheSize(2 * 1024 * 1024)
	                .memoryCacheSizePercentage(13) // default
	                .diskCache(new UnlimitedDiscCache(cacheDir)) // default
	                .diskCacheSize(50 * 1024 * 1024)
	                .diskCacheFileCount(100)
	                .writeDebugLogs()
	                .build();
	        ImageLoader.getInstance().init(config);
	    }
}
