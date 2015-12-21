package com.example.casting.processor.company;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.example.casting.entity.BaseBean;
import com.example.casting.entity.CompanyBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.processor.Errors;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.CastMap;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.util.ConstantData;

public class CompanyGetProcessor extends BaseCompanyProcessor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return ProcessorID.method_company_get;
	}

	@Override
	public Map<String, String> Bean2Map(Object obj) {
		// TODO Auto-generated method stub
		if(obj!=null&& obj instanceof BaseBean){
			BaseBean bean = (BaseBean)obj;
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id",bean.getId());
			map.put(company_name,"");
			return map;
		}
		return null;
	}

	@Override
	public ResultBean json2Bean(String returnstr) {
		try {
			String  result = getResultStr(returnstr);
			JSONObject json = new JSONObject(result);
			RegistBean regist = map2CmpBean(new CastMap(json));
			ResultBean bean = new ResultBean(Errors.OK, regist);
			return bean;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ResultBean bean = getResultCode(returnstr);
			return bean;
			
		}
	}

	private RegistBean map2CmpBean(CastMap map){
		if( map!=null){
			RegistBean registbean = new RegistBean();
			CompanyBean bean = new CompanyBean( );
			bean.setCompany_id(map.get(company_id));
			bean.setAuthor_id(map.get(author_id));
			bean.setCompany_icon(map.get(company_icon));
			bean.setCompany_legal_representative(map.get(company_legal_representative));
			registbean.setId(map.get(author_id));
			registbean.setHometown(map.get(company_address));
			registbean.setCertification(map.get(company_certification));
			registbean.setEmail(map.get(company_email));
			registbean.setHead_portrait( map.get(company_icon));
			registbean.setPhone(map.get(company_phone));
			registbean.setIntroduce(map.get(company_profile));
			registbean.setNickname( map.get(company_name) );
			registbean.setType(ConstantData.Login_Type_Company);
			registbean.setPersonal_status(map.get(company_status));
			registbean.setCertification(map.get(company_certification));
//			registbean.setIs_certification(map.get(is_certification));
			registbean.setCompany(bean);
			return registbean;
		}
		return null ;
	}
}
