package com.example.casting_android.beanconvert;

public class DicConvertExample implements IDicConvert {
	public String convertSex(String dic, String dm) {
		if (dic.equals("sex")) {
			if (dm.equals("1"))
				return "男";
			else if (dm.equals("0"))
				return "女";
			return "未知";
		}
		return dm;
	}
}
