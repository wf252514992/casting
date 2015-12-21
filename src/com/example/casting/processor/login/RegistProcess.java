package com.example.casting.processor.login;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.example.casting.entity.LoginBean;
import com.example.casting.processor.BaseProcessor;
import com.example.casting.processor.ErrorConvert;
import com.example.casting.processor.Errors;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.util.MD5andKL;

public class RegistProcess extends BaseProcessor{

	@Override
	public String getService() {
		// TODO Auto-generated method stub
		return ProcessorID.service_regist;
	}

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return ProcessorID.method_regist;
	}

	@Override
	public Map<String, String> Bean2Map(Object obj) {
		if(obj instanceof LoginBean){
			LoginBean bean = (LoginBean)obj;
			Map<String, String> map = new HashMap<String, String>();
			map.put("user", bean.getPhone());
			map.put("password", MD5andKL.MD5( bean.getPass() ));
			return map;
		}
		return null;
	}

	@Override
	public ResultBean json2Bean(String returnstr) {
		try {
			String resultstr = getResultStr(returnstr);
			JSONObject json = new JSONObject(resultstr);
			ResultBean result = new ResultBean();
			result.setCode(Errors.OK);
			result.setMessage(ErrorConvert.getErrorDiscribe(Errors.OK));
			result.setObj(json);
			return result;
		} catch (Exception ex) {
			return getResultCode(returnstr);
		}
	}

}
