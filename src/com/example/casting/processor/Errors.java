package com.example.casting.processor;

public class Errors {
	/**
	 * 返回成功
	 */
	public static final int OK = 1;
	/**
	 * 服务端错误
	 */
	public static final int BaseLoginError = 40000;
	/**
	 * 本地错误
	 */
	public static final int BaseLocalError = 20000;
	/**
	 * 用户不存在
	 */
	public static final int Regist_USER_ERROR = BaseLoginError + 1; 
	/**
	 * 密码错误
	 */
	public static final int ERROR_USER_PASS = BaseLoginError + 2;
	/**
	 * 手机号不能为空
	 */
	public static final int ERROR_PHONE = BaseLoginError + 3;
	/**
	 * 不是正确的手机号
	 */
	public static final int ERROR_NO_PHONE = BaseLoginError + 4;
	/**
	 * 用户已存在
	 */
	public static final int ERROR_USER_IN = BaseLoginError + 5;
	/**
	 * 参数错误
	 */
	public static final int ERROR_PARAM = BaseLoginError + 6;
	/**
	 * 参数不能为空
	 */
	public static final int ERROR_PARAM_NULL = BaseLoginError + 7;
	/**
	 * JSON格式不正确
	 */
	public static final int ERROR_JSON = BaseLoginError + 8;
	/**
	 * 文件格式不支持
	 */
	public static final int ERROR_FILE = BaseLoginError + 9;
	/**
	 * 用户已登录
	 */
	public static final int ERROR_USER_ALREADY = BaseLoginError*10 + 10;
	
	/**
	 *用户未登陆
	 */
	public static final int ERROR_USER_NOT_LOGGED= BaseLoginError*10+11;
	/**
	 * 用户类型错误
	 */
	public static final int ERROR_USER_TYPEERROR = BaseLoginError+12;
	/**
	 * 昵称已被使用
	 */
	public static final int ERROR_NICKNAMEERROR = BaseLoginError+13;
	/**
	 * 本地转换异常
	 */
	public static final int ERROR_LOCAL_PARSE = BaseLocalError+1;
	/**
	 * 本地转换异常
	 */
	public static final int ERROR_LOCAL_NDO = -11;
	/**
	 * 发送请求失败
	 */
	public static final int ERROR_LOCAL_REQUESTERROR = -12;
	
}
