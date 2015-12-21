package com.example.casting.processor;

import com.example.casting.util.Session;

public class LocalRequestData {
	/**
	 * 注册 导演信息注册
	 */
	public static String RegistAddDirector = "{\"birthday\":\"2015-01-01\",\"sex\":\"女\",\"phone\":\"13055180715\",\"weight\":\"566\",\"hometown\":\"\",\"nickname\":\"1\",\"labels\":\"\",\"type\":\"2\",\"wx\":\"55666\",\"head_portrait\":\"\",\"height\":\"155\",\"email\":\"干活哈哈\",\"address\":\"北京东城区\",\"name\":\"团团圆圆\",\"user_id\":\""+Session.getInstance().getUser_id()+"\",\"language\":\"复古u\",\"specialty\":\"刚刚好会\",\"qq\":\"455666\",\"introduction\":\"方法跟\",\"educational_background\":\"水电费个\"}";
	/**
	 * 注册 公司信息注册
	 */
	public static String RegistAddCompany = "";
	/**
	 * 注册  被招募 用户信息
	 * nickname 需要做唯一验证
	 * 
	 */
	public static String RegistAddNomal = "{\"birthday\":\"2015-01-01\",\"sex\":\"女\",\"phone\":\"13055180715\",\"weight\":\"168\",\"hometown\":\"\",\"nickname\":\"5\",\"labels\":\"小可耐,单身待解放,戏霸\",\"type\":\"1\",\"wx\":\"\",\"head_portrait\":\"\",\"height\":\"159\",\"email\":\"86352\",\"address\":\"北京东城区\",\"name\":\"模拟\",\"user_id\":\""+Session.getInstance().getUser_id()+"\",\"language\":\"嘿嘿嘿嘿\",\"specialty\":\"哦家\",\"qq\":\"22388\",\"introduction\":\"我看看\",\"educational_background\":\"默默\"}";

	public static String RegistAddDirector2 = "{\"certification\":\"\",\"birthday\":\"2010-05-05\",\"sex\":\"女\",\"phone\":\"13055180715\",\"weight\":\"65kg\",\"hometown\":\"内蒙古呼和浩特新城区\",\"nickname\":\"测试2222\",\"labels\":\"小可耐,戏霸,萌萌哒\",\"type\":\"2\",\"wx\":\"\",\"head_portrait\"" +
			":\"\",\"height\":\"185cm\",\"email\":\"252514992\",\"address\":\"天津和平区\",\"name\":\"迷你裙\",\"user_id\":\""+Session.getInstance().getUser_id()+"\",\"language\":\"英语，德语，汉语\",\"specialty\":\"武术\",\"qq\":\"252514992\",\"introduction\":\"好好学习题目好\",\"educational_background\":\"民航学院\"}";
}
