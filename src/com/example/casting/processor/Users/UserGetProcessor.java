package com.example.casting.processor.Users;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.example.casting.entity.BaseBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.processor.Errors;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.CastMap;
import com.example.casting.processor.data.ResultBean;

public class UserGetProcessor extends BaseUserProcess{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getService() {
		// TODO Auto-generated method stub
		return ProcessorID.service_user;
	}

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return ProcessorID.method_getuser;
	}

	@Override
	public Map<String, String> Bean2Map(Object obj) {
		if( obj !=null && obj instanceof RegistBean ){
			RegistBean bean = (RegistBean)obj;
			HashMap< String, String> map = new HashMap<String, String>();
			map.put("id",bean.getId());
			map.put(nickname,bean.getNickname());
			return map;
		}
		return null;
	}

	@Override
	public ResultBean json2Bean(String returnstr) {
		// TODO Auto-generated method stub
		try {
			String  result = getResultStr(returnstr);
			JSONObject json = new JSONObject(result);
			RegistBean regist = map2Bean(new CastMap(json));
			ResultBean bean = new ResultBean(Errors.OK, regist);
			return bean;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ResultBean bean = getResultCode(returnstr);
			return bean;
			
		}
		
	}

}
