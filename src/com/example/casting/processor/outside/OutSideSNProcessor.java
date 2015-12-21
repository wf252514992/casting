package com.example.casting.processor.outside;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.casting.processor.BaseProcessor;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.util.Session;

public class OutSideSNProcessor extends BaseProcessor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getService() {
		// TODO Auto-generated method stub
		return ProcessorID.service_outside;
	}

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return ProcessorID.method_outside_add;
	}

	@Override
	public Map<String, String> Bean2Map(Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject Bean2Json(Object obj) throws JSONException {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		json.put("id", Session.getInstance().getUser_id());
		json.put("info", obj.toString());
		json.put("title","");
		json.put("type","1");
		return json;
	}
	@Override
	public ResultBean json2Bean(String returnstr) {
		// TODO Auto-generated method stub
		return getResultCode(returnstr);
	}

}
