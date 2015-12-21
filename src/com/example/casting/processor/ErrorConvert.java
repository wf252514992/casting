package com.example.casting.processor;

public class ErrorConvert {

	public static String getErrorDiscribe(int errorCode) {
		switch (errorCode) {
		case Errors.OK:
			return "成功";
		case Errors.BaseLoginError:
			return "用户或密码不能为空";
		case Errors.Regist_USER_ERROR:
			return "用户不存在";
		case Errors.ERROR_USER_PASS:
			return "密码错误";
		case Errors.ERROR_PHONE:
			return "手机号不能为空";
		case Errors.ERROR_NO_PHONE:
			return "不是正确的手机号";
		case Errors.ERROR_USER_IN:
			return "用户已存在";
		case Errors.ERROR_PARAM:
			return "";//参数错误
		case Errors.ERROR_PARAM_NULL:
			return "参数不能为空";
		case Errors.ERROR_JSON:
			return "JSON格式不正确";
		case Errors.ERROR_FILE:
			return "文件格式不支持";
		case Errors.ERROR_USER_ALREADY:
			return "用户已登录";
		case Errors.ERROR_USER_TYPEERROR:
			return "用户类型错误";
		case Errors.ERROR_NICKNAMEERROR:
			return "昵称已被使用";
		case Errors.ERROR_USER_NOT_LOGGED:
			return "用户未登陆";
		case Errors.BaseLocalError + 1:
			return "强转错误";
		case Errors.ERROR_LOCAL_NDO:
			return "";//本地接口未处理
		case Errors.ERROR_LOCAL_REQUESTERROR:
			return "请求失败";
		default:
			return "";//未记录的错误
		}

	}
}
