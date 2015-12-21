//package com.example.sharedemo;
//
//import java.util.HashMap;
//import java.util.List;
//
//import com.example.casting_android.R;
//
//import cn.sharesdk.framework.Platform;
//import cn.sharesdk.framework.Platform.ShareParams;
//import cn.sharesdk.framework.PlatformActionListener;
//import cn.sharesdk.framework.ShareSDK;
//import cn.sharesdk.onekeyshare.OnekeyShare;
//import cn.sharesdk.sina.weibo.SinaWeibo;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.ResolveInfo;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.View.OnClickListener;
//
//public class MainActivity extends Activity {
//	Context context=MainActivity.this;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		findViewById(R.id.shareTxt).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
////				ShareSDK.initSDK(context);
//				showShare(getString(R.string.share), "http://sharesdk.cn",
//						"我是分享文本", "/storage/emulated/0/ba.jpg", "http://sharesdk.cn",
//						"我是测试评论文本", getString(R.string.app_name),
//						"http://sharesdk.cn");
//				/*ShareParams sp = new ShareParams();
//				sp.setText("测试分享的文本");
//				sp.setImagePath("/storage/emulated/0/ba.jpg");
//
//				Platform weibo = ShareSDK.getPlatform(context, SinaWeibo.NAME);
//				weibo.setPlatformActionListener(new PlatformActionListener() {
//					
//					@Override
//					public void onError(Platform arg0, int arg1, Throwable arg2) {
//						// TODO Auto-generated method stub
//						
//					}
//					
//					@Override
//					public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
//						// TODO Auto-generated method stub
//						
//					}
//					
//					@Override
//					public void onCancel(Platform arg0, int arg1) {
//						// TODO Auto-generated method stub
//						
//					}
//				}); // 设置分享事件回调
//				// 执行图文分享
//				weibo.share(sp);*/
////				weixinShare("com.tencent.mm");
//				
//			}
//		});
//	}
//
//	/**
//	 * 
//	 * @param title
//	 *            标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//	 * @param titleUrl
//	 *            是标题的网络链接，仅在人人网和QQ空间使用
//	 * @param Content
//	 *            是分享文本，所有平台都需要这个字段
//	 * @param imagePath
//	 *            是图片的本地路径，Linked-In以外的平台都支持此参数
//	 * @param url
//	 *            仅在微信（包括好友和朋友圈）中使用
//	 * @param comment
//	 *            是我对这条分享的评论，仅在人人网和QQ空间使用
//	 * @param site
//	 *            是分享此内容的网站名称，仅在QQ空间使用
//	 * @param siteUrl
//	 *            是分享此内容的网站地址，仅在QQ空间使用
//	 */
//	public void showShare(String title, String titleUrl, String Content,
//			String imagePath, String url, String comment, String site,
//			String siteUrl) {
//		ShareSDK.initSDK(this);
//		OnekeyShare oks = new OnekeyShare();
//		// 关闭sso授权
//		oks.disableSSOWhenAuthorize();
//
//		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//		oks.setTitle(title);
//		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//		oks.setTitleUrl(titleUrl);
//		// text是分享文本，所有平台都需要这个字段
//		oks.setText(Content);
//		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
////		oks.setImagePath(imagePath);// 确保SDcard下面存在此张图片
//		
//		oks.setImageUrl("http://bbs.szonline.net/UploadFile/album/2011/6/71220/8/20110608093106_50329.jpg");
//		// url仅在微信（包括好友和朋友圈）中使用
//		oks.setUrl(url);
//		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
//		oks.setComment(comment);
//		// site是分享此内容的网站名称，仅在QQ空间使用
//		oks.setSite(site);
//		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
//		oks.setSiteUrl(siteUrl);
//
//		// 启动分享GUI
//		oks.show(this);
//	}
//	
//	private void weixinShare(String type) {
//		// TODO Auto-generated method stub
//		      boolean found = false;
//		      Intent share = new Intent(android.content.Intent.ACTION_SEND);
//		      share.setType("image/*");
//		      // gets the list of intentsthat can be loaded.
//		      List<ResolveInfo> resInfo =getPackageManager().queryIntentActivities(
//		           share, 0);
//		      if (!resInfo.isEmpty()) {
//		        for (ResolveInfo info : resInfo) {
//		        	System.out.println("111:"+info.activityInfo.packageName);
//		           if (info.activityInfo.packageName.toLowerCase().contains(type)
//		                 || info.activityInfo.name.toLowerCase().contains(type)) {
//		              share.putExtra(Intent.EXTRA_SUBJECT, "subject");
//		              share.putExtra(Intent.EXTRA_TEXT, "your text");
//
//		              //share.putExtra(Intent.EXTRA_STREAM,
//		              // Uri.fromFile(newFile(myPath))); // Optional, just
//		              // // if you wanna
//		              // // share an
//		              // // image.
//		              share.setPackage(info.activityInfo.packageName);
//		              found = true;
//		              break;
//		           }
//		        }
//		        if (!found)
//		           return;
//		        startActivity(Intent.createChooser(share, "Select"));
//		      }
//	}
//}
