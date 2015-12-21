package com.example.casting.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.os.AsyncTask;
import android.os.Environment;
public class DownTools extends AsyncTask<String, Integer, String> {
	private boolean mExternalStorageAvailable = false,
			mExternalStorageWriteable = false;
	private String picNameStr;
	public static String savePath;
	private String sdcardPath;

	public DownTools(String sdcardPath,
			String picName) {
		this.sdcardPath = sdcardPath;
		this.picNameStr = picName;
	}

	@Override
	protected String doInBackground(String... params) {
		downPic(params[0]);
		return null;

	}
 
	
	private void downPic(String path) {
		try {
			path = new String(path.getBytes("utf-8"), "ISO-8859-1");
		} catch (UnsupportedEncodingException e1) {
		
			e1.printStackTrace();
		}

		File sdcardFileDir = new File(sdcardPath);

		InputStream is = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			is = conn.getInputStream();
			bis = new BufferedInputStream(is);
//			
//			 String sdStatus = Environment.getExternalStorageState();
//             if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // ���sd�Ƿ����
//                
//                 return;
//             }
			checkSdCard(sdcardPath);
			if (mExternalStorageAvailable && mExternalStorageWriteable) {
				File file = new File(sdcardFileDir, picNameStr);
				fos = new FileOutputStream(file);
				savePath = sdcardPath + '/' + picNameStr;
			}
			bos = new BufferedOutputStream(fos);
			byte[] b = new byte[512];
			int count = bis.read(b);
			while (count != -1) {
				bos.write(b, 0, count);
				bos.flush();
				count = bis.read(b);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
				if (bis != null)
					bis.close();
				if (fos != null)
					fos.close();
				if (bos != null)
					bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}
    
	@Override
	protected void onPostExecute(String result) {
		
	}

	public File checkSdCard(String path) {
		File destDir = null;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mExternalStorageAvailable = mExternalStorageWriteable = true;
			destDir = new File(path);
			if (!destDir.exists()) {
				destDir.mkdirs();

			}
			File file = new File(path + ".nomedia");
			if (!file.exists()) {
				file.mkdirs();

			}

		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		return destDir;

	}
	
	
	@Override
	protected void onPreExecute() {
	    
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
	
	}

}
