package com.example.casting.processor.login;

import java.util.HashMap;
import java.util.Map;


import com.example.casting.entity.LoginBean;
import com.example.casting.processor.BaseProcessor;
import com.example.casting.processor.ErrorConvert;
import com.example.casting.processor.Errors;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.util.MD5andKL;

public class FindPasswordProcess extends BaseProcessor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getService() {
		// TODO Auto-generated method stub
		return ProcessorID.service_login;
	}

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return ProcessorID.method_findpwd;
	}

	@Override
	public Map<String, String> Bean2Map(Object obj) {
		if(obj instanceof LoginBean){
			LoginBean bean = (LoginBean)obj;
			Map<String, String> map = new HashMap<String, String>();
			map.put("phone", bean.getPhone());
			map.put("password", MD5andKL.MD5(bean.getPass()));
			return map;
		}
		return null;
		
	}

	@Override
	public ResultBean json2Bean(String returnstr) {
		// TODO Auto-generated method stub
		try {
			String result = getEnCodeResult(returnstr);
			if(result.equals("1")){
				ResultBean bean = new ResultBean();
				bean.setCode( Errors.OK);
				bean.setMessage(ErrorConvert.getErrorDiscribe(Errors.OK));
				return bean;
			}else{
				return getResultCode(returnstr);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return getResultCode(returnstr);
		}
		
	}


}
