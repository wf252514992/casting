package com.example.casting.processor.works;

import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.casting.entity.WorkBean;
import com.example.casting.processor.Errors;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.util.Session;

public class SetWorksProcessor extends WorkBaseProcessor {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return ProcessorID.method_works_set;
	}

	@Override
	public Map<String, String> Bean2Map(Object obj) {
		return null;
	}
	
	@Override
	public JSONObject Bean2Json(Object obj) throws JSONException {
		if(obj!=null &&  obj instanceof WorkBean ){
			WorkBean  bean = (WorkBean) obj;
			JSONObject json = new JSONObject();
			json.put(id, Session.getInstance().getUser_id());
			json.put(works_id, bean.getWorks_id());
			json.put(works_name, bean.getWorks_name());
			json.put(release_time,bean.getRelease_time());
			json.put(role, bean.getRole());
			json.put(director,bean.getDirector());
			json.put(cooperation_actor,bean.getCooperation_actor());
			json.put(works_images, bean.getWorks_images() );
			json.put(remark,bean.getBz());
			return json;
		}
		return null;
	}

	@Override
	public ResultBean json2Bean(String returnstr) {
		return getResultCode(returnstr);
	}

}
