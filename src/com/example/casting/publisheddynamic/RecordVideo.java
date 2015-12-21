package com.example.casting.publisheddynamic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.casting.util.ConstantData;
import com.example.casting.util.Server_path;
import com.example.casting_android.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Description: 使用MediaRecorder录制视频
 * 
 */
public class RecordVideo extends Activity {

	// private static final int startprepare = 100000;
	// /**发送*/
	// private Button xxfs_shiping_send_btn =null;
	// /**取消拍照按钮*/
	// private Button xxfs_shiping_stop = null;
	/** 执行录制按钮 */
	private Button xxfs_shiping_record = null;
	/***/
	private SurfaceView surfaceView = null;
	/***/
	private SurfaceHolder surfaceHolder = null;
	
	// 屏幕宽和高
	private int screenWidth, screenHeight;
	// 定义系统所用的照相机
	private Camera camera;
	// 摄像头是否已经在预览状态. true:预览状态 ;false:没有预览状态
	private boolean isPreview;

	// private XxfsMessage picMsg ;
	/** 视频录制 */
	private MediaRecorder media = null;
	/** 视频地址 */
	private String mediapath = "";

	public static final int Res_Ready = 41000;
	public static final int Res_Cancel = 41001;

	private long startrecordtime = 0;
	private Timer timer;
	
	
	//TODO  判断当前拍摄模式  0为照片，1为视频
	private int cameraType = 0;
	private Bitmap bitmap;
	
	
	/***
	 * 录音时间
	 */
	private TextView txt_time;
	private String MyMsgPath = "";
	private Button canelBtn, sureBtn;
	private Context context = RecordVideo.this;
	private int cameraPosition = 1;//0代表前置摄像头，1代表后置摄像头
	private ImageButton camera_position,camera_light;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_record);
		surfaceView = (SurfaceView) findViewById(R.id.xxfs_surfaceView);
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// 获取屏幕的宽和高
		screenWidth = wm.getDefaultDisplay().getWidth();
		screenHeight = wm.getDefaultDisplay().getHeight();
		
		camera_position=(ImageButton) findViewById(R.id.camera_position);
		camera_light=(ImageButton) findViewById(R.id.camera_light);
		surfaceHolder = surfaceView.getHolder();
		// 为surfaceHolder添加一个回调监听器
		surfaceHolder.addCallback(new Callback() {

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				// 打开并初始化摄像头
				initCamera();
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// 如果camera不为null，释放摄像头
				if (camera != null) {
					if (isPreview) {
						camera.stopPreview();
						camera.release();
						camera = null;
					}
				}
			}
		});
		// 设置SurfaceView自己不维护缓冲区
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//
		xxfs_shiping_record = (Button) findViewById(R.id.xxfs_shiping_record);
		txt_time = (TextView) findViewById(R.id.txt_time);
		PhotoBtnListener btnlistener = new PhotoBtnListener();
		xxfs_shiping_record.setOnClickListener(btnlistener);
		mediapath = Server_path.SaveVideoPath;
		File dir = new File(mediapath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		mediapath = dir.getAbsolutePath();
		setTimeToTextView(0);
		
		//TODO  取消和确认按钮 
		canelBtn = (Button) findViewById(R.id.canelBtn);
		sureBtn = (Button) findViewById(R.id.sureBtn);
		canelBtn.setOnClickListener(clickListener);
		sureBtn.setOnClickListener(clickListener);
		camera_position.setOnClickListener(btnlistener);
		camera_light.setOnClickListener(btnlistener);
		//TODO  滑动切换
		surfaceView.setOnTouchListener(new OnTouchListener() {
			float startx = 0.0f;
			float starty = 0.0f;
			float endx = 0.0f;
			float endy = 0.0f;

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				float x = event.getX();
				float y = event.getY();
				WindowManager wm = RecordVideo.this.getWindowManager();
				int width = wm.getDefaultDisplay().getWidth();
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					startx = x;
					starty = y;
				}


				if (event.getAction() == MotionEvent.ACTION_UP) {
					endx = x;
					endy = y;
					if (Math.abs(endx - startx) > width / 3
							&& Math.abs(endx - startx) > Math
									.abs(endy - starty)) {

						if (endx - startx < 0) {
							setTypeSelect(0);
						} else {
							setTypeSelect(1);
						}
					}
				}

				return true;
			}

		});
	}

	/**
	 * TODO  滑动切换
	 * @param type
	 *            0为照片，1为视频
	 */
	@SuppressLint("NewApi")
	private void setTypeSelect(int type) {
		if (!runing) {
			TextView phoneType = (TextView) findViewById(R.id.phoneType);
			TextView videoType = (TextView) findViewById(R.id.videoType);
			if (type == 1) {
				// 开始预览
				camera.startPreview();
				phoneType.setTextColor(context.getResources().getColor(
						R.color.yellow));
				phoneType.setText("照片");
				videoType.setText("视频");
				videoType.setTextColor(context.getResources().getColor(
						R.color.white));
				phoneType.setCompoundDrawablesRelativeWithIntrinsicBounds(0,
						R.drawable.yellow_little_circle, 0, 0);
				videoType.setCompoundDrawablesRelativeWithIntrinsicBounds(0,
						R.drawable.white_little_circle, 0, 0);
				cameraType = 0;
				txt_time.setVisibility(View.INVISIBLE);
				xxfs_shiping_record.setBackgroundResource(R.drawable.take_phone_bg);
			} else {
				videoType.setCompoundDrawablesRelativeWithIntrinsicBounds(0,
						R.drawable.yellow_little_circle, 0, 0);
				phoneType.setCompoundDrawablesRelativeWithIntrinsicBounds(0,
						R.drawable.white_little_circle, 0, 0);
				videoType.setTextColor(context.getResources().getColor(
						R.color.yellow));
				phoneType.setTextColor(context.getResources().getColor(
						R.color.white));
				phoneType.setText("照片");
				videoType.setText("视频");
				cameraType = 1;
				txt_time.setVisibility(View.VISIBLE);
				xxfs_shiping_record.setBackgroundResource(R.drawable.take_video_bg);
			}
		}
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.sureBtn:
				if(cameraType==0){

					// 创建一个位于SD卡上的文件
					String path=Server_path.SavePath+
							System.currentTimeMillis()
							+ ".jpg";
					File file = new File(path);
					File dir=new File(Server_path.SavePath);
					if(!dir.exists()){
						dir.mkdirs();
					}
					FileOutputStream outStream = null;
					try {
						// 打开指定文件对应的输出流
						outStream = new FileOutputStream(file);
						bitmap = rotateBitmap(bitmap, 90);
						// 把位图输出到指定文件中
						boolean flag=bitmap.compress(CompressFormat.JPEG,
								60, outStream);
						outStream.close();
						if(flag){
							// TODO 跳转到发布图片动态界面

							ArrayList<String> filelist=new ArrayList<String>();
							filelist.add(path);
							Intent intent=new Intent(RecordVideo.this,EditPhotoDynamic.class);
							intent.putStringArrayListExtra("filelist", filelist);
							startActivity(intent);
							finish();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				
				}else if(cameraType==1){
				Intent intent = new Intent(RecordVideo.this,
						EditViewDynamic.class);
				intent.putExtra("uri", MyMsgPath);
				startActivity(intent);
				finish();
				}
				break;

			case R.id.canelBtn:
				finish();
				break;

			default:
				break;
			}
		}
	};

	/**
	 * 旋转图片
	 * 
	 * @param bitmap
	 * @param degress
	 *            角度
	 * @return
	 */
	public  Bitmap rotateBitmap(Bitmap bitmap, int degress) {
		if (bitmap != null) {
			Matrix m = new Matrix();
			m.postRotate(degress);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), m, true);
			return bitmap;
		}
		return bitmap;
	}
	private void setVideoPath() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String date = format.format(new Date());
		MyMsgPath = mediapath + "/" + date + ".mp4";
	}

	private void setTimeToTextView(long time) {
		SimpleDateFormat format = new SimpleDateFormat("mm:ss");
		String value = format.format(new Date(time));
		txt_time.setText(value);
	}

	public void surfaceCreated(SurfaceHolder holder) {
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	boolean runing = false;

	private class PhotoBtnListener implements View.OnClickListener {

		public void onClick(View v) {
			// stopMedia();
			switch (v.getId()) {
			// case R.id.xxfs_shiping_stop:
			// stopMedia();
			// // showResult();
			// // showPrepareVideoNoZip();
			// break;
			case R.id.xxfs_shiping_record: {
				txt_time.setVisibility(View.GONE);
				if (cameraType==0) {
					//TODO  这里写拍照的方法
					if (camera != null) {
						// 拍照
						camera.takePicture(null, null, myjpegCallback);
					}
				
				}else {
					camera_light.setVisibility(View.GONE);
					camera_position.setVisibility(View.GONE);
					if (runing) {
						xxfs_shiping_record.setBackgroundResource(R.drawable.take_video);
						stopMedia();
					} else {
						xxfs_shiping_record.setBackgroundResource(R.drawable.take_video_press);
						startMedia();
					}
				}

			}
				break;
				
			case R.id.camera_position:
                //切换前后摄像头
                int cameraCount = 0;
                CameraInfo cameraInfo = new CameraInfo();
                cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数

                for(int i = 0; i < cameraCount; i++) {
                    Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
                    if(cameraPosition == 1) {
                        //现在是后置，变更为前置
                    	turnLightOff(camera);
                    	camera_light.setVisibility(View.GONE);
                        if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置  
                            camera.stopPreview();//停掉原来摄像头的预览
                            camera.release();//释放资源
                            camera = null;//取消原来摄像头
                            camera = Camera.open(i);//打开当前选中的摄像头
                            try {
                                camera.setPreviewDisplay(surfaceHolder);//通过surfaceview显示取景画面
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                         // 如果是竖屏
            				if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            					camera.setDisplayOrientation(90);
            				} else {
            					camera.setDisplayOrientation(0);
            				}
                            camera.startPreview();//开始预览
                            cameraPosition = 0;
                            break;
                        }
                    } else {
                        //现在是前置， 变更为后置
                        if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置  
                            camera.stopPreview();//停掉原来摄像头的预览
                            camera.release();//释放资源
                            camera = null;//取消原来摄像头
                            camera = Camera.open(i);//打开当前选中的摄像头
                            try {
                                camera.setPreviewDisplay(surfaceHolder);//通过surfaceview显示取景画面
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                         // 如果是竖屏
            				if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            					camera.setDisplayOrientation(90);
            				} else {
            					camera.setDisplayOrientation(0);
            				}
                            camera.startPreview();//开始预览
                            cameraPosition = 1;
                            break;
                        }
                    }

                }
                break;	
			case R.id.camera_light:
				camera_light.setBackgroundResource(R.drawable.on);
				turnLight(camera);
				
				break;

			// case R.id.xxfs_shiping_send_btn:{
			// //发送预览
			// // showPrepareVideo();
			// showPrepareVideoNoZip();
			// }break;
			// case R.id.btn_back:{
			// //返回
			// finish();
			// }break;
			}
		}
	}
	/**
	 * 控制闪光灯
	 * @param mCamera
	 */
	public static void turnLight(Camera mCamera) {
		if (mCamera == null) {
			return;
		}
		Parameters parameters = mCamera.getParameters();
		if (parameters == null) {
			return;
		}
		List<String> flashModes = parameters.getSupportedFlashModes();
		// Check if camera flash exists
		if (flashModes == null) {
			// Use the screen as a flashlight (next best thing)
			return;
		}
		String flashMode = parameters.getFlashMode();
		if (!Parameters.FLASH_MODE_TORCH.equals(flashMode)) {
			// Turn on the flash
			if (flashModes.contains(Parameters.FLASH_MODE_TORCH)) {
				parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
				mCamera.setParameters(parameters);
			} else {
			}
		}else if (!Parameters.FLASH_MODE_OFF.equals(flashMode)) {
			// Turn off the flash
			if (flashModes.contains(Parameters.FLASH_MODE_OFF)) {
				parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
				mCamera.setParameters(parameters);
			} else {
				Log.e("FLASH_MODE_OFF", "FLASH_MODE_OFF not supported");
			}
		}
	}
	/**
	 * 关闭闪光灯
	 * @param mCamera
	 */
	public static void turnLightOff(Camera mCamera) {
		if (mCamera == null) {
			return;
		}
		Parameters parameters = mCamera.getParameters();
		if (parameters == null) {
			return;
		}
		List<String> flashModes = parameters.getSupportedFlashModes();
		String flashMode = parameters.getFlashMode();
		// Check if camera flash exists
		if (flashModes == null) {
			return;
		}
		if (!Parameters.FLASH_MODE_OFF.equals(flashMode)) {
			// Turn off the flash
			if (flashModes.contains(Parameters.FLASH_MODE_OFF)) {
				parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
				mCamera.setParameters(parameters);
			} else {
				Log.e("FLASH_MODE_OFF", "FLASH_MODE_OFF not supported");
			}
		}
	}
	/**
	 * 初始化摄像头
	 */
	private void initCamera() {
		if (!isPreview) {
			camera = Camera.open();
		}
		if (camera != null && !isPreview) {
			try {
				Parameters parameters = camera.getParameters();
				// 设置闪光灯为自动状态
				parameters.setFlashMode(Parameters.FLASH_MODE_AUTO);
				camera.setParameters(parameters);
				// 设置预览照片的大小
				parameters.setPreviewSize(screenWidth, screenHeight);
				// 设置每秒显示4帧
				parameters.setPreviewFrameRate(4);
				// 设置图片格式
				parameters.setPictureFormat(PixelFormat.JPEG);
				// 设置JPG照片的质量
				parameters.set("jpeg-quality", 70);
				// 设置照片大小
				parameters.setPictureSize(screenWidth/6, screenHeight/6);
				// 通过SurfaceView显示取景画面
				camera.setPreviewDisplay(surfaceHolder);
				// 如果是竖屏
				if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
					camera.setDisplayOrientation(90);
				} else {
					camera.setDisplayOrientation(0);
				}
				// 开始预览
				camera.startPreview();
				// 自动对焦
//				camera.autoFocus(null);
				isPreview = true;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	
	PictureCallback myjpegCallback = new PictureCallback() {

		@Override
		public void onPictureTaken(final byte[] data, Camera camera) {
			// 根据拍照所得的数据创建位图
			bitmap = BitmapFactory.decodeByteArray(data, 0,
					data.length);
//			ImageView showView = (ImageView) findViewById(R.id.save_image);
			// 显示刚刚拍得的照片
//			showView.setVisibility(View.VISIBLE);
//			showView.setImageBitmap(bitmap);
			// 重新预览
			camera.stopPreview();
//			camera.startPreview();
//			isPreview = true;
		}
	};

	
	/***
	 * 开始视频录制
	 */
	private void startMedia() {
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {

				while (media != null) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				synchronized (RecordVideo.class) {
					if (runing)
						return;
					runing = true;
					setVideoPath();
					media = new MediaRecorder();
					if (camera == null) {
						camera = Camera.open();
					}
//					camera.setDisplayOrientation(90);
					// 如果是竖屏
					if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
						camera.setDisplayOrientation(90);
						media.setOrientationHint(90);
					} else {
						camera.setDisplayOrientation(0);
						media.setOrientationHint(0);
					}
					camera.unlock();
					media.setCamera(camera);

					/* 设置音频输入源 */
					media.setAudioSource(MediaRecorder.AudioSource.MIC);
					/* 设置视频输入源 */
					media.setVideoSource(MediaRecorder.VideoSource.CAMERA);
					/* 设置音频输出格式 */
					media.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
					/* 设置音频编码方式 */
					media.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
					/* 设置视频编码方式 */
					media.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
					// mr.setVideoFrameRate(28);
					media.setVideoSize(640, 480);
					// media.setOrientationHint(90);

					media.setPreviewDisplay(surfaceHolder.getSurface());

					media.setOutputFile(MyMsgPath);
					try {
						media.prepare();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					timer = new Timer();
					timer.schedule(new MyTimerTask(), 0, 1 * 1000);
					startrecordtime = System.currentTimeMillis();
					media.start();
					
				}
			}

		});
		th.start();

		// xxfs_shiping_stop.setEnabled(true);
		// xxfs_shiping_record.setText("重录");
	}

	/**
	 * 停止录音
	 */
	private void stopMedia() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		synchronized (RecordVideo.class) {

			if (media != null) {

				Log.i("Main", "释放录像资源");
				try {
					media.stop();
				} catch (Exception ex) {

				}
				media.reset();
				media.release();
				media = null;

			}
			if (camera != null) {
				camera.release();
				camera = null;
			}

			runing = false;
			
		}

		// xxfs_shiping_record.setText("录制");
	}

	@Override
	protected void onDestroy() {
		stopMedia();
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
		/*
		 * if (media != null) { media.release(); media = null; }
		 */
		stopMedia();
	}

	private class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			if (runing) {
				Message msg = new Message();
				msg.what = updatetime;
				handler.sendMessage(msg);
			}
		}

	}

	private static final int updatetime = 100001;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case updatetime: {
				long lasttime = System.currentTimeMillis();
				long currenttime = lasttime - startrecordtime;
				if (currenttime < 30 * 1000) {
					// 限制视频大小 30s 以内
					setTimeToTextView(currenttime);
				} else {
					stopMedia();
					// showResult();
					// showPrepareVideoNoZip();
				}
			}
				break;
			}
		}
	};
	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// //如果是发送则关闭当前进行发送操作；
	// if(requestCode == startprepare ){
	// if(resultCode == XxfsVideoPlayDialog.Res_Ready){
	// try{
	// String path = data.getStringExtra("resname");
	// sendVideo(path);
	// }catch(Exception ex){
	// finish();
	// }
	// }
	// }
	//
	// }

}
