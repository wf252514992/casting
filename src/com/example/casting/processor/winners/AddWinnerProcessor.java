package com.example.casting.processor.winners;

import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.casting.entity.WinnerBean;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.util.Session;

public class AddWinnerProcessor extends WinnerBaseProcessor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return ProcessorID.method_winner_add;
	}

	@Override
	public Map<String, String> Bean2Map(Object obj) {
		return null;
	}

	@Override
	public JSONObject Bean2Json(Object obj) throws JSONException {
		// TODO Auto-generated method stub
		if( obj!= null && obj instanceof WinnerBean){
			WinnerBean bean = (WinnerBean)obj;
			JSONObject json = new JSONObject();
			json.put(winners_time ,bean.getWinners_time()  );
			json.put(winners_name , bean.getWinners_name());
			json.put(winners_works_name , bean.getWinners_works_name());
			json.put("id", Session.getInstance().getUser_id());
			json.put(winners_images,bean.getWinners_images());
			json.put(remark, bean.getBz());
			return json;
		}
		return null;
	}
	@Override
	public ResultBean json2Bean(String returnstr) {
		return getResultCode(returnstr);
	}

}
