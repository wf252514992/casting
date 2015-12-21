//package com.example.casting.me;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import com.example.casting.CompanydetailActivity;
//import com.example.casting.MainTab;
//import com.example.casting.ReviewActivity;
//import com.example.casting.SendAgainActivity;
//import com.example.casting.SignupedActivity;
//import com.example.casting.TextActivity;
//import com.example.casting.publisheddynamic.ImgsAdapter;
//import com.example.casting.publisheddynamic.VideoPlayActivity;
//import com.example.casting_android.R;
//import com.example.casting_android.bean.Expression;
//import android.media.MediaPlayer;
//import android.media.ThumbnailUtils;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore.Video.Thumbnails;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.Drawable;
//import android.text.Html;
//import android.text.Spannable;
//import android.text.SpannableString;
//import android.text.style.ImageSpan;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
///**
// * 
// * @author chenjiaping
// *
// */
//public class DynamicActivity extends Activity {
//
//	private static DynamicActivity sInstance;
//	private MediaPlayer player;
//	private boolean isPause=false;
////	@TAInjectView(id = R.id.titlelayout)
////	View titleLayout;
////	@TAInjectView(id = R.id.dynamicList)
////	ListView listview;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		View view = View.inflate(this, R.layout.activity_mainorentrants, null);
//		setContentView(view);
////		initView(titleLayout);
////		setLeftButtonAble(true, "取消");
////		setRightButtonAble(true, "发送");
////		setTitle("发评论");
//		init();
//	}
//	
//
//	private void init(){
//		Intent intent=getIntent();
//		String title=intent.getStringExtra("title");
//		if(title!=null){
//			TextView titletext=(TextView) findViewById(R.id.titletext);
//			titletext.setText(title);
//		}
//		Expression e = new Expression();
//		e.setCode("[#51]");
////		e.setDrableId(R.drawable.e_51);
//		ImageSpan imageSpan = new ImageSpan(BitmapFactory.decodeResource(getResources(), e.drableId));
//		SpannableString spannableString = new SpannableString(e.code);
//		spannableString.setSpan(imageSpan, 0, e.code.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		//listview
//		ListView listview=(ListView) findViewById(R.id.dynamicList);
//		ArrayList<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();
//		HashMap<String, Object> map = new HashMap<String, Object>();
//		map.put("name","副影视公司");
//		map.put("time","刚刚");
//		map.put("signuptime","2014-12-20截止");
//		map.put("text",spannableString+"现在发布试镜通知，现在发布试镜通知，现在发布试镜通知，现在发布试镜通知，现在发布试镜通知。");
//		map.put("pic", R.drawable.ic_launcher);
//		list.add(map);
//		map = new HashMap<String, Object>();
//		map.put("name","副影视公司");
//		map.put("time","刚刚");
//		map.put("signuptime","2014-12-20截止");
//		map.put("text",spannableString+"现在发布试镜通知，现在发布试镜通知，现在发布试镜通知，现在发布试镜通知，现在发布试镜通知。");
//		map.put("pic", R.drawable.ic_launcher);
//		list.add(map);
//		map = new HashMap<String, Object>();
//		map.put("name","副影视公司");
//		map.put("time","刚刚");
//		map.put("signuptime","2014-12-20截止");
//		map.put("text",spannableString+"现在发布试镜通知，现在发布试镜通知，现在发布试镜通知，现在发布试镜通知，现在发布试镜通知。");
//		map.put("pic", R.drawable.ic_launcher);
//		list.add(map);
//		map = new HashMap<String, Object>();
//		map.put("name","副影视公司");
//		map.put("time","刚刚");
//		map.put("signuptime","2014-12-20截止");
//		map.put("text","现在发布试镜通知，现在发布试镜通知，现在发布试镜通知，现在发布试镜通知，现在发布试镜通知。");
//		map.put("pic", R.drawable.ic_launcher);
//		list.add(map);
//		DynamicListAdapter adapter=new DynamicListAdapter(this, list);
//		listview.setAdapter(adapter);
//		listview.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//                 Intent intent=new Intent(DynamicActivity.this,TextActivity.class);
//                 startActivity(intent);
//			}
//			
//		});
//		
//	}
//	public static DynamicActivity getInstance() {
//		if (null == sInstance) {
//			synchronized (DynamicActivity.class) {
//				if (sInstance == null) {
//					sInstance = new DynamicActivity();
//				}
//			}
//		}
//		return sInstance;
//	}
//	class DynamicListAdapter extends BaseAdapter{
//		Context context;
//		ArrayList<HashMap<String,Object>> arrayList;
//		int expression_wh = -1;
//		public DynamicListAdapter(Context context,ArrayList<HashMap<String,Object>> arrayList){
//			super();
//			this.context=context;
//			this.arrayList=arrayList;
//			expression_wh = (int)this.context.getResources().getDimension(R.dimen.chat_expression_wh);
//		
//		}
//		@Override
//		public int getCount() {
//			return arrayList.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return arrayList.get(position);
//		}
//
//		@Override
//		public long getItemId(int span) {
//			return span;
//		}
//
//		final Html.ImageGetter imageGetter_resource = new Html.ImageGetter() {
//			public Drawable getDrawable(String source) {
//				Drawable drawable = null;
//				int rId = Integer.parseInt(source);
//				drawable = context.getResources().getDrawable(rId);
//				drawable.setBounds(0, 0, expression_wh,expression_wh);//设置显示的图像大小
//				return drawable;
//			};
//		};
//		public  String replaceSpaceToCode(String str){
//			String rt = str.replace(" ", "&nbsp;");
//			rt = rt.replace("\n", "<br/>");
//			
//			return rt;
//		}
//		@Override
//		public View getView(int position, View v, ViewGroup group) {
//			LayoutInflater inflater=LayoutInflater.from(context);
//			v=inflater.inflate(R.layout.activity_mainorentrants_item, null);
//			
//			ImageView pic=(ImageView) v.findViewById(R.id.pic);
//			TextView name=(TextView) v.findViewById(R.id.name);
//			TextView time=(TextView) v.findViewById(R.id.time);
//			Button signup=(Button) v.findViewById(R.id.signup);
//			TextView signuptime=(TextView) v.findViewById(R.id.signuptime);
//			TextView text = (TextView) v.findViewById(R.id.text);
//			Button sendagain = (Button) v.findViewById(R.id.sendagain);
//			Button review = (Button) v.findViewById(R.id.review);
//			Button praise = (Button) v.findViewById(R.id.praise);
//			com.example.casting.util.view.MyGridView imgGridView=(com.example.casting.util.view.MyGridView) v.findViewById(R.id.maingridView);
//			
//			if(arrayList.get(position).get("pic")!=null){ 
//                //TODO 框架图片加载
//				
//			}
//			pic.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
//					Intent intent=new Intent(DynamicActivity.this,CompanydetailActivity.class);
//					startActivity(intent); 
//					
//				}
//			});
//			//相册
//			Intent intent=getIntent();
//			ArrayList<String> filelist=intent.getStringArrayListExtra("filelist");
//			if(filelist!=null){
//				
//				imgGridView.setVisibility(View.VISIBLE);
//				imgGridView.setHorizontalSpacing(10);
//				imgGridView.setVerticalSpacing(10);
//				ImgsAdapter imgsAdapter=new ImgsAdapter(context, filelist,null);
//				imgsAdapter.setVisibility(false);
//				imgGridView.setAdapter(imgsAdapter);
//			}
//			//视频播放
//			playvideo(v);
//			//语音
//			 String path=intent.getStringExtra("path");
//			playVoice(v,path);
//			
//			name.setText(arrayList.get(position).get("name").toString());
//			time.setText(arrayList.get(position).get("time").toString());
//			signuptime.setText(arrayList.get(position).get("signuptime").toString());
//			String content = msgConvert(replaceSpaceToCode(arrayList.get(position).get("text").toString()));
//			text.setText(Html.fromHtml(content, imageGetter_resource, null));//TODO @、超链接等的处理
//			signup.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View arg0) {
//                     Intent intent=new Intent(DynamicActivity.this,SignupedActivity.class);
//                     startActivity(intent);
//				}
//			});
//			sendagain.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View arg0) {
//                 Intent intent=new Intent(DynamicActivity.this,SendAgainActivity.class);
//                 startActivity(intent);
//				}
//			});
//			review.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View arg0) {
//                  Intent intent=new Intent(DynamicActivity.this,ReviewActivity.class);
//                  startActivity(intent);
//				}
//			});
//			praise.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View arg0) {
//
//				}
//			});
//		
//			return v;
//		}
//		private String msgConvert(String content){
//			Log.v("_____________", "1content = " + content);
//			for (int i = 0; i < MainTab.expressionList.size(); i++) {
//				content = content.replace(MainTab.expressionList.get(i).code, "<img src=\""+MainTab.expressionList.get(i).drableId+"\" />");
//			}
//			Log.v("_____________", "2content = " + content);
//			return content;
//		}
//		private void playvideo(View v){
//			Intent intent=getIntent();
//		    final Uri uri=intent.getParcelableExtra("uri");
//		    String pathStr=intent.getStringExtra("path");
//		    String sizeStr=intent.getStringExtra("size");
//		    String timeStr=intent.getStringExtra("time");
//		    if(uri!=null){
//
//			      ImageView video=(ImageView) v.findViewById(R.id.video);
//			      RelativeLayout videomessage=(RelativeLayout) v.findViewById(R.id.videomessage);
//			      videomessage.setVisibility(View.VISIBLE);
//					
//			    		TextView size=(TextView) v.findViewById(R.id.size);
//			    		size.setText(sizeStr);
//			    		TextView time=(TextView) v.findViewById(R.id.timeStr);
//			    		time.setText(timeStr);
//			    		File file=new File(pathStr);
//			    		if(file.exists()){
//			    			// 获取视频的缩略图  
//			    	    	Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(pathStr,Thumbnails.MINI_KIND);  
//			    	        System.out.println("w"+bitmap.getWidth());  
//			    	        System.out.println("h"+bitmap.getHeight());  
//			    	        bitmap = ThumbnailUtils.extractThumbnail(bitmap, 200, 200,  
//			    	                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);  
//			    	        video.setImageBitmap(bitmap);
//			    		}
//			    	
//			    	ImageButton op=(ImageButton) v.findViewById(R.id.op);
//			    	op.setOnClickListener(new OnClickListener() {
//						
//						@Override
//						public void onClick(View arg0) {
//							Intent intent=new Intent(DynamicActivity.this,VideoPlayActivity.class);
//							intent.putExtra("uri", uri);
//							startActivity(intent);
//							
//						}
//					});
//				
//		    }
//		}
//	   private void playVoice(View v,String path){
//		  
//		   if(path!=null&&!path.equals("")){
//			   RelativeLayout voicemessage=(RelativeLayout) v.findViewById(R.id.voicemessage);
//				voicemessage.setVisibility(View.VISIBLE);
//				player = new MediaPlayer();
//				 player.reset();  
//				try{
//				    player.setDataSource(path);
//				}catch(IOException e){
//				 e.printStackTrace();
//				}
//				player.prepareAsync();
//				 ImageButton voiceimg=(ImageButton) v.findViewById(R.id.voiceimg);
//			     voiceimg.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View arg0) {
//						if (player.isPlaying() &&!isPause){  
//			                player.pause();  
//			                isPause = true;  
//			            }else{  
//			                player.start();  
//			                isPause = false;  
//			            }  
//						
//					}
//				});
//		   }
//	   }
//	}
//
//	@Override
//	public void onDestroy() { 
//        if(player.isPlaying()){  
//            player.stop();  
//        }  
//        player.release();  
//        super.onDestroy();  
//    }
////	@Override
////	public void LeftButtonClick() {
////		// TODO Auto-generated method stub
////		
////	}
////	@Override
////	public void RightButtonClick() {
////		// TODO Auto-generated method stub
////		
////	}  
//
//}
