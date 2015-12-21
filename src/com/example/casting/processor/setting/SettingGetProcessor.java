package com.example.casting.processor.setting;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import com.example.casting.entity.SettingBean;
import com.example.casting.processor.Errors;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.CastMap;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.util.Session;

public class SettingGetProcessor extends BaseSetProcessor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return ProcessorID.method_set_get;
	}

	@Override
	public Map<String, String> Bean2Map(Object obj) {
		// TODO Auto-generated method stub
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(id, Session.getInstance().getUser_id());
		return map;
	}

	@Override
	public ResultBean json2Bean(String returnstr) {
		try {
			String result = getResultStr(returnstr);
			JSONObject json =new JSONObject(result);
			SettingBean bean = map2bean(new CastMap(json));
			ResultBean resultbean = new ResultBean(Errors.OK, bean);
			return resultbean;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ResultBean bean = getResultCode(returnstr);
			return bean;

		}
	}
	
	

}
