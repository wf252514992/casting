package com.example.casting.processor;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.casting.processor.data.ResultBean;

public interface IBaseProcess {

	public String getService();
	public String getMethod();
	public String getProcessorId();
	public String getUrl();
	public Map<String, String> Bean2Map(Object obj);
	public JSONObject Bean2Json(Object obj)throws JSONException ;
	public ResultBean json2Bean(String returnstr);
}
