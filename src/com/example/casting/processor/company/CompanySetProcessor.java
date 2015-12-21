package com.example.casting.processor.company;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.casting.entity.RegistBean;
import com.example.casting.processor.LocalRequestData;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.util.Session;

public class CompanySetProcessor extends BaseCompanyProcessor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return ProcessorID.method_company_set;
	}

	@Override
	public Map<String, String> Bean2Map(Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject Bean2Json(Object obj) throws JSONException {
		if(isLocal)
			return new JSONObject(LocalRequestData.RegistAddCompany);
		if(obj instanceof RegistBean){
			RegistBean bean = (RegistBean)obj;
			JSONObject map = new JSONObject();
			map.put(company_id,bean.getCompany().getCompany_id());
			map.put(company_name,bean.getNickname());
			map.put(company_profile,bean.getIntroduce());
			map.put(company_address,bean.getHometown());
			map.put(company_phone,bean.getPhone());
			map.put(company_email,bean.getEmail());
			map.put(company_legal_representative,bean.getCompany().getCompany_legal_representative());
			map.put(company_icon, bean.getHead_portrait());
			map.put(company_status, bean.getPersonal_status());
			map.put(author_id,Session.getInstance().getUser_id());
			return map;
		}
		return null;
	}
	@Override
	public ResultBean json2Bean(String returnstr) {
		return getResultCode(returnstr);
	}

}
