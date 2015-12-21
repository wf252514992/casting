package com.example.casting.processor.plaza;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import com.example.casting.entity.PlazaBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.entity.SearchBean;
import com.example.casting.processor.Errors;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.Users.BaseUserProcess;
import com.example.casting.processor.data.CastMap;
import com.example.casting.processor.data.ResultBean;

/**
 * 榜单关注
 * 
 * @author wf
 * 
 */
public class SearchProcessor extends BaseUserProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getService() {
		// TODO Auto-generated method stub
		return ProcessorID.service_plaza;
	}

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return ProcessorID.method_plazasearch;
	}

	@Override
	public Map<String, String> Bean2Map(Object obj) {
		// TODO Auto-generated method stub
		if(obj instanceof SearchBean){
			SearchBean bean = (SearchBean) obj;
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("search", bean.getSearchstr());
			map.put("pagenum", bean.getPagenum()+"");
			map.put("type", bean.getType());
			return map;
		}
		return null;
	}

	@Override
	public ResultBean json2Bean(String returnstr) {
		// TODO Auto-generated method stub
		try {
			String result = getResultStr(returnstr);
			JSONArray array = new JSONArray(result);
			List<RegistBean> beans = new ArrayList<RegistBean>();
			for( int i = 0 ; i < array.length() ; i++){
				if( !array.isNull(i)){
					JSONObject json = (JSONObject) array.get(i);
					RegistBean regist = map2Bean(new CastMap(json));
					beans.add(regist);
				}
			}
			ResultBean resultbean = new ResultBean(Errors.OK, beans);
			return resultbean;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ResultBean bean = getResultCode(returnstr);
			return bean;

		}
	}

	

}
