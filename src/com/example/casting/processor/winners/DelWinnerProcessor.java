package com.example.casting.processor.winners;

import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.casting.entity.WinnerBean;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.util.Session;

public class DelWinnerProcessor extends WinnerBaseProcessor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return ProcessorID.method_winner_del;
	}

	@Override
	public Map<String, String> Bean2Map(Object obj) {
		return null;
	}
	@Override
	public JSONObject Bean2Json(Object obj) throws JSONException {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		json.put(winners_id ,obj.toString());
		return json;
	}
	@Override
	public ResultBean json2Bean(String returnstr) {
		return getResultCode(returnstr);
	}

}
