package com.example.casting.processor.Users;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.casting.entity.RegistBean;
import com.example.casting.processor.LocalRequestData;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.util.ConstantData;
import com.example.casting.util.Session;

public class UserAddProcessor extends BaseUserProcess {

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
		return ProcessorID.method_adduser;
	}

	@Override
	public Map<String, String> Bean2Map(Object obj) { 
		return null;
	}

	@Override
	public JSONObject Bean2Json(Object obj) throws JSONException{
		// TODO Auto-generated method stub
		if(isLocal)
			return new JSONObject(LocalRequestData.RegistAddDirector2);
		if(obj instanceof RegistBean){
			RegistBean bean = (RegistBean)obj;
			JSONObject map = new JSONObject();
			map.put(user_id, bean.getId());
			map.put( uset_type ,bean.getType() );
			map.put( name ,bean.getName());
			map.put( nickname,bean.getNickname());
			map.put( phone,bean.getPhone());
			map.put( sex,bean.getSex());
			map.put( address,bean.getAddress());
			map.put( hometown,bean.getHometown());
			map.put( heigth,bean.getHeight());
			map.put( education,bean.getEducation());
			map.put( weight,bean.getHeavy());
			map.put( language,bean.getLanguage());
			map.put( specialty,bean.getSpecial());
			map.put( introduction,bean.getIntroduce());
			map.put( head_portrait,bean.getHead_portrait());
			map.put( birthday,bean.getBirth());
			map.put( labels,bean.getLabelVal());
			map.put( email,bean.getEmail());
			map.put( qq,bean.getQq());
			map.put( wx,bean.getWechat());
//			if(bean.getCertification()!=null && bean.getCertification().length()>0){
//				map.put(certification,bean.getCertification());
//			}
			return map;
//			return new JSONObject(LocalRequest.RegistComanpy);
		}
		return null;
	
	}
	@Override
	public ResultBean json2Bean(String returnstr) {
		return getResultCode(returnstr);
	}

}
