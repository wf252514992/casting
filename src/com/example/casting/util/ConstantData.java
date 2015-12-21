package com.example.casting.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import android.os.Environment;

import com.example.casting.entity.ConditionBean;
import com.example.casting.util.view.Util;

public class ConstantData {
	/**
	 * statue = (1=普通用户，2=导演，3=公司)
	 */
	public static final String Login_Type_Nomal = "1";
	public static final String Login_Type_Director = "2";
	public static final String Login_Type_Company = "3";
	
	public static final String[] months = { "01", "02", "03", "04", "05", "06",
			"07", "08", "09", "10", "11", "12" };

	public static String[] years = null;
	
	public static ArrayList<ConditionBean> conditionList = new ArrayList<ConditionBean>();
	
	public static void setConditions(ArrayList<ConditionBean> list){
		conditionList.clear();
		conditionList.addAll(list);
	}
	
	 public static String getCastingDir(){
		 String urlStr ="";
	    	if (hasSDCard()) {
	    		 urlStr = Environment.getExternalStorageDirectory().getAbsolutePath() + CastingDir;
			} else {
				urlStr = Environment.getDataDirectory().getAbsolutePath() + "/data/"+CastingDir ; // filePath: /data/data/
			}
	    	File file = new File(urlStr);
			if(!file.exists())
			{
				file.mkdirs();
			}
    		return urlStr;
	    }
	 /**
		 * 用户保存照片的路径
		 * @return
		 */
		public static String getImgDir()
		{
			File file = new File(getCastingDir()+Util.castingPicDir);
			if(!file.exists())
			{
				file.mkdirs();
			}
			return file.getAbsolutePath();
		}
	public static String[] getYears() {
		if( years == null ){
			int size = 100;
			years = new String[size];
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			for (int i = size - 1; i > -1; i--) {
				years[i] = year - i + "";
			}
		}
		return years;
	}
	static Calendar calendar = Calendar.getInstance();
	public static String[] getDays(String year, String month ,int type) {
		try {
			int lastDay = 31;
			if(type!=0){
				int _year = Integer.parseInt(year);
				int _month = Integer.parseInt(month);
				calendar.set(_year, _month, 1);
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				calendar.setTime(calendar.getTime());
				lastDay = calendar.get(Calendar.DAY_OF_MONTH);
			}
			String[] days = new String[lastDay];
			
			for (int i = 1; i < lastDay + 1; i++) {
				if (i < 10) {
					days[i - 1] = "0" + i;
				} else
					days[i - 1] = i + "";

			}
			return days;
		} catch (Exception ex) {
			return new String[] {};
		}
	}
	public static boolean hasSDCard() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED)) {
			return false;
		} 
		return true;
	}
	 /** 新浪微博注册码   当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY */
    public static final String APP_KEY      = "2562228427";

   
    private static final String CastingDir = "/casting/";
    /** 
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * 
     * <p>
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
     * 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
    public static final String REDIRECT_URL ="https://api.weibo.com/oauth2/default.html";// "http://www.sina.com";

    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。
     * 
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     * 
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     * 
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String SCOPE = 
            "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
    
    /**
     * 正对当前项目的 pagekagename 的 签名
     */
    public static final String signMD5 = "1bf0cbd4bf2b5474bec4001f8bff8f36";
    
    /**
     * 新浪微博注册 密码
     */
    public static final String sinaSecret="505db02b1fd3ad403c4934bb21e2221f";
}
