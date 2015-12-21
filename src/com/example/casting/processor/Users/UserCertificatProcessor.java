package com.example.casting.processor.Users;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.casting.entity.RegistBean;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.ResultBean;

public class UserCertificatProcessor extends BaseUserProcess{

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
		return ProcessorID.method_certification;
	}

	@Override
	public Map<String, String> Bean2Map(Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {"id":"1","type":"jpg","imageStr":"base64码"(或者"image_url":"base64码" 二选一)}
	 */
	@Override
	public JSONObject Bean2Json(Object obj) throws JSONException {
		if(obj instanceof RegistBean){
			RegistBean bean = (RegistBean)obj;
			JSONObject map = new JSONObject();
			map.put( "id", bean.getId());
			map.put( "type" ,"jpg" );
			map.put( "image_url" ,bean.getCertification());
			return map;
		}
		return null;
	}
	@Override
	public ResultBean json2Bean(String returnstr) {
		// TODO Auto-generated method stub
		return getResultCode(returnstr);
	}

}
