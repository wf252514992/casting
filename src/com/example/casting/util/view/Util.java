package com.example.casting.util.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import com.example.casting.login.BaseForm;

import cn.jpush.android.data.l;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.widget.Toast;

public class Util {

	public static final String castingDir = "/casting";
	public static final String castingPicDir = "/pic/";
	public static final String castingfileDir = "/filerev";
	public static final String castingDBdir = "/db";

	/** 获取sd卡路径 */
	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		if (sdDir != null)
			return sdDir.toString();
		else
			return null;
	}

	public static String getDBDir() {
		String dirpath = getSDPath();
		if (dirpath != null) {
			String dbdir = dirpath + castingDir + castingDBdir;
			File file = new File(dbdir);
			if (!file.exists())
				file.mkdirs();
			return dbdir;
		}
		return dirpath;
	}

	/**
	 * 拍照时自动生成的照片路径
	 * 
	 * @return
	 */
	public static String createImgName() {
		String uriString = "";
		if (getSDPath() != null) {
			File file = new File(getSDPath() + castingPicDir);
			if (!file.exists()) {
				file.mkdirs();
			}
			uriString = file.getAbsolutePath() + "/" + getCurrentTime()
					+ ".jpg";
		}
		return uriString;
	}

	public static String getCurrentTime() {
		SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeString = formate.format(System.currentTimeMillis());
		return timeString;

	}

	/**
	 * 根据文件名判断是否是图片
	 * 
	 * @param imgTypeStr
	 *            文件名
	 * @return
	 */
	public static boolean isBmp(String fileName) {
		if (fileName != null && !fileName.equals("")) {
			if (fileName.indexOf(".jpg") > -1 || fileName.indexOf(".gif") > -1
					|| fileName.indexOf(".jpeg") > -1
					|| fileName.indexOf(".png") > -1
					|| fileName.indexOf(".swf") > -1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public static void copyFile(String oldPath, String newPath, Context context) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件不存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				if (context != null) {
					Toast.makeText(context, "已保存到" + newPath,
							Toast.LENGTH_SHORT).show();
				}
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}

	/** 尺寸dp转为px */
	public static int Dp2Px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	/** 尺寸px转为dp */
	public static int Px2Dp(Context context, float px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	/** 改变图片的高宽 */
	public static Drawable setDrawable(Context context, int resource,
			int width, int height) {
		Drawable drawable = context.getResources().getDrawable(resource);
		int bmpWidth = (int) (width * BaseForm.density);
		int heigth = (int) (height * BaseForm.density);
		drawable.setBounds(0, 0, bmpWidth, heigth);
		return drawable;
	}

	/**
	 * 删除文件
	 * 
	 * @param file
	 */
	public static void delete(File file,Context context) {
		if (file.isFile()) {
			file.delete();
			return;
		}

		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				delete(childFiles[i],context);
			}
			file.delete();
			if(context != null)
			{
				Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
			}
		}
	}

}
