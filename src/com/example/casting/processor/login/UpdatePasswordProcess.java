package com.example.casting.processor.login;

import java.util.HashMap;
import java.util.Map;

import com.example.casting.processor.BaseProcessor;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.util.MD5andKL;

public class UpdatePasswordProcess extends BaseProcessor {

	@Override
	public String getService() {
		// TODO Auto-generated method stub
		return ProcessorID.service_login;
	}

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return ProcessorID.method_updatepwd;
	}

	@Override
	public Map<String, String> Bean2Map(Object obj) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("phone", "13055180715");
		map.put("old", MD5andKL.MD5("123456789"));
		map.put("password", MD5andKL.MD5("987654321"));
		return map;
	}

	@Override
	public ResultBean json2Bean(String returnstr) {
		// TODO Auto-generated method stub
		return null;
	}


}
