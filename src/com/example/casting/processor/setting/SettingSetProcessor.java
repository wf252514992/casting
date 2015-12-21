package com.example.casting.processor.setting;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.casting.entity.SettingBean;
import com.example.casting.processor.Errors;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.util.Session;

public class SettingSetProcessor extends BaseSetProcessor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return ProcessorID.method_set_set;
	}

	@Override
	public Map<String, String> Bean2Map(Object obj) {
		// TODO Auto-generated method stub
		if(obj instanceof SettingBean){
			SettingBean bean = (SettingBean)obj;
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(id, Session.getInstance().getUser_id());
			map.put(letter, bean.getLetter()+"");
			map.put(remind_attention, bean.getRemind_attention()+"");
			map.put(remind_comment, bean.getRemind_comment()+"");
			map.put(remind_letter, bean.getRemind_letter()+"");
			map.put(comment, bean.getComment()+"");
			return map;
		}
		return null;
	}
	@Override
	public JSONObject Bean2Json(Object obj) throws JSONException {
		// TODO Auto-generated method stub
		if(obj instanceof SettingBean){
			SettingBean bean = (SettingBean)obj;
			JSONObject map = new JSONObject();
			map.put(id, Session.getInstance().getUser_id());
			map.put(letter, bean.getLetter()+"");
			map.put(remind_attention, bean.getRemind_attention()+"");
			map.put(remind_comment, bean.getRemind_comment()+"");
			map.put(remind_letter, bean.getRemind_letter()+"");
			map.put(comment, bean.getComment()+"");
			return map;
		}
		return null;
	}

	@Override
	public ResultBean json2Bean(String returnstr) {
		try {
			String result = getResultStr(returnstr);
			JSONObject json =new JSONObject(result);
			ResultBean resultbean = new ResultBean(Errors.OK );
			return resultbean;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ResultBean bean = getResultCode(returnstr);
			return bean;

		}
	}
	
	

}
