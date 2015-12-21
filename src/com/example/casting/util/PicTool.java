package com.example.casting.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.helpers.DefaultHandler;

import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

public class PicTool {

	private static final int defaultwidth = 1024;
	private static final int defaultheight = 768;
	public static boolean mExternalStorageAvailable = false,
			mExternalStorageWriteable = false;
	/*
	压缩图片，处理某些手机拍照角度旋转的问题
	*/
	public static String compressImage(Context context,String filePath,String dir,File outputFile) throws FileNotFoundException {

	        try {
				Bitmap bm = getSmallBitmap(filePath,defaultwidth,defaultheight);

				int degree = readPictureDegree(filePath);

				if(degree!=0){//旋转照片角度
				    bm=rotateBitmap(bm,degree);
				}

				checkSdCard(dir);
				FileOutputStream out = new FileOutputStream(outputFile);

				bm.compress(Bitmap.CompressFormat.JPEG,70, out);
			} catch (Exception e) {
				e.printStackTrace();
			}

	        return outputFile.getPath();
	    }
	public static File checkSdCard(String path) {
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
	// 根据路径获得图片并压缩，返回bitmap用于显示
	public static Bitmap getSmallBitmap(String filePath,int maxwidth,int maxheight) {
	        final BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(filePath, options);

	        // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, maxwidth,maxheight);

	        // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;

	    try {
	    	options.inPreferredConfig = Bitmap.Config.RGB_565;    
	    	options.inPurgeable = true;   
	    	options.inInputShareable = true;   
	        return BitmapFactory.decodeFile(filePath, options);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
			
		}
	    }
	//计算图片的缩放值
	public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {
	             final int heightRatio = Math.round((float) height/ (float) reqHeight);
	             final int widthRatio = Math.round((float) width / (float) reqWidth);
	             inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
	    }
	        return inSampleSize;
	}
	public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270;
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
	public static Bitmap rotateBitmap(Bitmap bitmap,int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress); 
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }
	public static String Bitmap2base64JPEG(Bitmap bmp) {
		byte[] bts = bmp2BytesJPEG(bmp);
		return new String(Base64Coder.encode(bts));
	}
	public static byte[] bmp2BytesJPEG(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 60, baos);
		return baos.toByteArray();
	}
	 /**
     * 压缩照片
     */
    public static  Bitmap getYsBitmap(String imagePath, int width, int height) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inDensity=80;
		BitmapFactory.decodeFile(imagePath, options);
		int widthRadio = (int) Math.ceil(options.outWidth / width);//227
		int heightRadio = (int) Math.ceil(options.outHeight / height);//442
		if (widthRadio > 1 && heightRadio > 1) {
			if (widthRadio > heightRadio) {
				options.inSampleSize = widthRadio;
			} else {
				options.inSampleSize = heightRadio;
			}
		}
		options.inJustDecodeBounds = false;
//		Bitmap bmp = BitmapFactory.decodeFile(imagePath,options);
//		return Bitmap.createScaledBitmap(bmp, width, height, false);
		Bitmap mp = BitmapFactory.decodeFile(imagePath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		mp.compress(Bitmap.CompressFormat.JPEG, 80, baos);
		byte[] bb = baos.toByteArray();
		System.out.println(bb.length/1024);
				
		return BitmapFactory.decodeFile(imagePath,options);
	}
    /**
     * 对图片大小压缩
     * @param image 图片
     * @param memery 目标大小
     * @return
     */
	 public static Bitmap compressBitmap(Bitmap image, int memery ) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			int density = 100;
			int imgLength=0;
			if(baos.toByteArray().length > 0 && baos.toByteArray().length / 1024 >= memery){
				while(imgLength > 0 && imgLength / 1024 >= memery) {	//循环判断如果压缩后图片是否大于500kb,大于继续压缩	
					if(imgLength/1024/memery > 1 )
					{
						density -= 20;
					}
					else 
					{
						density -= 10;
					}
					baos.reset();
					image.compress(Bitmap.CompressFormat.JPEG, density, baos);
					imgLength = baos.toByteArray().length;
				}
			}
			return BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length);
		 }
	    /**
	     * 对图片大小压缩
	     * @param image 图片
	     * @param memery 目标大小
	     * @return
	     */
		 public static byte[] compressByte(Bitmap image, int memery ) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				int density = 100;
				if(baos.toByteArray().length > 0 && baos.toByteArray().length / 1024 >= memery){
					while(baos.toByteArray().length > 0 && baos.toByteArray().length / 1024 >= memery) {	//循环判断如果压缩后图片是否大于500kb,大于继续压缩	
						System.out.println("压缩前："+baos.toByteArray().length/1024);
						density -= 10;
						baos.reset();
						image.compress(Bitmap.CompressFormat.JPEG, density, baos);
						System.out.println("压缩后："+baos.toByteArray().length/1024);
					}
				}
				return baos.toByteArray();
			 }
	 public static Bitmap getSmallBitmap(String url,int width,int height,int memery)
	 {
		return compressBitmap(getYsBitmap(url,width,height),memery);
	 }
		/***
		 * 等比例压缩图片
		 * 
		 * @param bitmap
		 * @param screenWidth
		 * @param screenHight
		 * @return
		 */
		public static Bitmap getBitmap(Bitmap bitmap, int screenWidth,
				int screenHight) {
			int w = bitmap.getWidth();
			int h = bitmap.getHeight();
			Matrix matrix = new Matrix();
			float scale = (float) screenWidth / w;
			float scale2 = (float) screenHight / h;

			// scale = scale < scale2 ? scale : scale2;

			// 保证图片不变形.
			matrix.postScale(scale, scale);
			// w,h是原图的属性.
			return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		}
		/***
		 * 根据资源文件获取Bitmap
		 * 
		 * @param context
		 * @param drawableId
		 * @return
		 */
		public static Bitmap ReadBitmapByUrl(Context context, String url) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Config.RGB_565;
			options.inInputShareable = true;
			options.inPurgeable = true;
			Bitmap mp = BitmapFactory.decodeFile(url,options);
			return mp;
		}
		/***
		 * 根据资源文件获取Bitmap
		 * 
		 * @param context
		 * @param drawableId
		 * @return
		 */
		public static Bitmap ReadBitmapByUrl(Context context, String url,int inSampleSize) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Config.ARGB_8888;
			options.inInputShareable = true;
			options.inPurgeable = true;
			options.inSampleSize = inSampleSize;
			Bitmap mp = BitmapFactory.decodeFile(url,options);
			return mp;
		}
		/***
		 * 根据资源文件获取Bitmap
		 * 
		 * @param context
		 * @param drawableId
		 * @return
		 */
		public static Bitmap ReadBitmapById(Context context, int drawableId) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Config.RGB_565;
			options.inInputShareable = true;
			options.inPurgeable = true;
			InputStream stream = context.getResources().openRawResource(drawableId);
			Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
			return bitmap;
		}
}
