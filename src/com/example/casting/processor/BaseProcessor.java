package com.example.casting.processor;

import java.io.Serializable;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Base64;
import com.example.casting.processor.data.ResultBean;

public abstract class BaseProcessor implements IBaseProcess ,Serializable{

	public static boolean isLocal = false;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final String splitTag = "/";
	public final String endTag = "?response=application/json";
	
	@Override
	public String getProcessorId() {
		// TODO Auto-generated method stub
		return getService()+getMethod();
	}
	@Override
	public String getUrl() {
		String baseurl = ProcessorID.uri;
		String service = getService();
		String method = getMethod();
		return baseurl+splitTag+service+splitTag+method+endTag;
	}
	public Header[] getHeader(){
		Header[] header = { 
                new BasicHeader("Content-Type", "application/json") }; 
		return header;
	}
	public String getResultStr(String returnStr) throws  Exception{
		String resultstr =getEnCodeResult(returnStr);
		return new String(Base64.decode(resultstr, 0));
	}
	
	public String getEnCodeResult(String returnStr) throws  Exception{
		JSONObject json = new JSONObject(returnStr);
		String resultstr = json.getString("return");
		return resultstr;
		
	}
	public ResultBean getResultCode(String content) {
		try {
			String val = getEnCodeResult(content);
			int code = Integer.parseInt(val);
			String info = ErrorConvert.getErrorDiscribe(code);
			return new ResultBean(code, info);
		} catch (Exception ex) {
			return new ResultBean(Errors.ERROR_LOCAL_PARSE,
					ErrorConvert.getErrorDiscribe(Errors.ERROR_LOCAL_PARSE));

		}
	}
	
	@Override
	public JSONObject Bean2Json(Object obj)throws JSONException  {
		// TODO Auto-generated method stub
		return null;
	}
}
