package com.example.casting.processor.company;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.casting.entity.CompanyBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.processor.Errors;
import com.example.casting.processor.LocalRequestData;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.util.ConstantData;

public class CompanyAddProcessor extends BaseCompanyProcessor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return ProcessorID.method_company_add;
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
			map.put(company_name,bean.getNickname());
			map.put(company_profile,bean.getIntroduce());
			map.put(company_address,bean.getHometown());
			map.put(company_phone,bean.getPhone());
			map.put(company_email,bean.getEmail());
			CompanyBean combean =  bean.getCompany();
			map.put(company_legal_representative, combean.getCompany_legal_representative());
			map.put(company_icon, bean.getHead_portrait());
//			if(bean.getCertification()!=null && bean.getCertification().length()>0){
//				map.put(company_certification,bean.getCertification());
//			}
			map.put(author_id,bean.getId());
			return map;
		}
		return null;
	}
	@Override
	public ResultBean json2Bean(String returnstr) {
		return getResultCode(returnstr);
	}

}
