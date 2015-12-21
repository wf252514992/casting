package com.example.casting.processor;



public class ProcessorID {
	//https://code.csdn.net/linfeng1009/casting_andriod.git
	//http://121.41.35.226:8080/casting/
		public static final String uri_headphoto ="http://121.41.35.226:8080/casting/";// "http://192.168.1.44:8080/casting/";
		public static final String uri = uri_headphoto+"services";
		public static final String service_login = "LoginManage";
		public static final String service_regist = "RegisteManage";
		public static final String service_user = "UserManage";
		public static final String service_company = "CompanysManage";
		public static final String service_plaza = "PlazaManage";
		public static final String service_attetion = "AttentionManage";
		public static final String service_setting = "SettingsManage";
		public static final String service_file = "FileManage";
		public static final String service_works = "WorksManage";
		public static final String service_winner = "WinnersManage";
		public static final String service_recruit = "RecruitManage";
		public static final String service_feedback = "FeedbackManage";
		public static final String service_gallery = "GalleryManage";
		public static final String service_outside = "OutSideManage";
		public static final String service_dynamic = "DynamicManage";
		public static final String service_video = "VideoManage";
		public static final String service_background="BackgroudManage";
		
		/**
		 * 登录
		 */
		public static final String method_login = "login";
		/**
		 * 注册用户
		 */
		public static final String method_regist = "reg";
		/**
		 * 找回密码,设置密码
		 */
		public static final String method_findpwd = "setPassword";
		/**
		 * 修改密码
		 */
		public static final String method_updatepwd = "updatePassword";
		/**
		 * 查询用户信息
		 */
		public static final String method_getuser = "get";
		/**
		 * 修改用户信息
		 */
		public static final String method_setuser = "set";
		/**
		 * 新增用户
		 */
		public static final String method_adduser = "add";
		/**
		 * 用户认证
		 */
		public static final String method_certification = "certification";
		
		/**
		 * 榜单关注
		 */
		public static final String method_plazaget = "get";
		/**
		 *模糊搜索
		 */
		public static final String method_plazasearch = "search";
		
		/**
		 * 获取条件
		 */
		public static final String method_plazagetconditions = "getConditions";
		/**
		 * 获取我的粉丝
		 */
		public static final String method_attention_getFans = "getFans";
		/**
		 * 获取我的关注数量或关注我的数量,互相关注数量
		 */
		public static final String method_attention_getCount = "getCount";
		/**
		 * 取消对某人的关注
		 */
		public static final String method_attention_del = "del";
		/**
		 * 获取我的关注
		 */
		public static final String method_attention_getattention = "getAttent";
		/**
		 * 关注某人
		 */
		public static final String method_attention_add = "add";
		/**
		 * 是否关注某人
		 */
		public static final String method_attention_isadd = "isAttention";
		/**
		 * 获取公司资料
		 */
		public static final String method_company_get = "get";
		/**
		 * 增加公司资料
		 */
		public static final String method_company_add = "add";
		/**
		 * 修改公司资料
		 */
		public static final String method_company_set = "set";
		/**
		 * 修改设置
		 */
		public static final String method_set_set = "set";
		/**
		 *获取设置
		 */
		public static final String method_set_get = "get";

		/**
		 * 文件上传
		 * id - 用户id   
		 * type - 文件后缀名，例：jpg,png 
		 * imageStr - 图片转成Base64字符串
		 */
		public static final String method_upload64 = "uploadBase64";
		/**
		 * 获取作品
		 */
		public static final String method_works_getList = "getList";
		/**
		 * 获取获奖
		 */
		public static final String method_winner_getList = "getList";
		/**
		 * 获取我的招募
		 */
		public static final String method_recruit_getList = "getList";
		/**
		 * 意见反馈
		 */
		public static final String method_feedback_add = "add";
		/**
		 * 添加作品
		 */
		public static final String method_works_add = "add";
		/**
		 * 修改作品
		 */
		public static final String method_works_set = "set";
		/**添加个人展示照片*/
		public static final String method_person_play_add = "add";
		/**删除个人展示照片*/
		public static final String method_person_play_del = "del";
		/**
		 * 添加获奖信息
		 */
		public static final String method_winner_add = "add";
		/**
		 * 修改获奖信息
		 */
		public static final String method_winner_set = "set";
		/**
		 * 删除获奖信息
		 */
		public static final String method_winner_del = "del";
		/**
		 * 修改展示的照片
		 */
		public static final String method_gallery_getlist = "getList";
		/**
		 * 新浪微博认证
		 */
		public static final String method_outside_add = "add";
		/**
		 * 获取我的视频
		 */
		public static final String method_video_getlist = "getList";
		
		public static final String method_del = "del";
		public static final String method_getCount = "getCount";
		/**
		 * 获取各个模块的背景图
		 */
		public static final String method_getbackground="get";
		/**
		 * 设置背景图
		 */
		public static final String method_setbackground="set";
		
}
