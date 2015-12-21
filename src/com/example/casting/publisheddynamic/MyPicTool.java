package com.example.casting.publisheddynamic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.casting.util.Base64Coder;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;

public class MyPicTool {

	public static boolean mExternalStorageAvailable = false,
			mExternalStorageWriteable = false;
	/*
	压缩图片，处理某些手机拍照角度旋转的问题
	*/
	public static String compressImage(Context context,String filePath,String dir,File outputFile) throws FileNotFoundException {

	        try {
				Bitmap bm = getSmallBitmap(filePath);

				int degree = readPictureDegree(filePath);

				if(degree!=0){//旋转照片角度
				    bm=rotateBitmap(bm,degree);
				}

				checkSdCard(dir);
				FileOutputStream out = new FileOutputStream(outputFile);

				bm.compress(Bitmap.CompressFormat.JPEG, 30, out);
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
	public static Bitmap getSmallBitmap(String filePath) {
	        final BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(filePath, options);

	        // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, 480, 800);

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
	             inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
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
		options.inDensity=40;
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
			image.compress(Bitmap.CompressFormat.JPEG, 80, baos);
			int density = 100;
			if(baos.toByteArray().length > 0 && baos.toByteArray().length / 1024 >= memery){
				while(baos.toByteArray().length > 0 && baos.toByteArray().length / 1024 >= memery) {	//循环判断如果压缩后图片是否大于500kb,大于继续压缩	
					baos.reset();
					image.compress(Bitmap.CompressFormat.JPEG, density, baos);
					density -= 10;
				}
			}
			return BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length);
		 }
	 public static Bitmap getSmallBitmap(String url,int width,int height,int memery)
	 {
		return compressBitmap(getYsBitmap(url,width,height),memery);
	 }
}
