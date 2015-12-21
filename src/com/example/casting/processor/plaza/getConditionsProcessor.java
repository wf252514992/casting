package com.example.casting.processor.plaza;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import com.example.casting.entity.ConditionBean;
import com.example.casting.processor.Errors;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.CastMap;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.util.Session;

public class getConditionsProcessor extends BasePlazzaProcessor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String type = "type";
	private final String  plaza_id = "plaza_id";
	private final String lab = "lab";
	private final String min_age = "min_age";
	private final String max_age = "max_age";
	private final String plaza_time = "plaza_time";
	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return ProcessorID.method_plazagetconditions;
	}

	@Override
	public Map<String, String> Bean2Map(Object obj) {
		/**
		 * id - 用户idpagenum - 页数 不填为1type - 类型 条件类型：时间段1，标签2，不填查询所有
		 */
		if( obj instanceof ConditionBean ){
			ConditionBean bean = (ConditionBean)obj;
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", Session.getInstance().getUser_id());
			map.put("pagenum", bean.getPagenum());
			map.put("type", bean.getType());
			return map;
		}
		return null;
	}

	@Override
	public ResultBean json2Bean(String returnstr) {
		try {
			String result = getResultStr(returnstr);
			JSONArray array = new JSONArray(result);
			List<ConditionBean> beans = new ArrayList<ConditionBean>();
			for( int i = 0 ; i < array.length() ; i++){
				if( !array.isNull(i)){
					JSONObject json = (JSONObject) array.get(i);
					ConditionBean bean = map2bean(new CastMap(json));
					beans.add(bean);
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
	
	private ConditionBean map2bean(CastMap map){
		if(map==null)return null;
		ConditionBean bean = new ConditionBean();
		bean.setLabel(map.get(lab));
		bean.setMaxage(map.get(max_age));
		bean.setMinage(map.get(min_age));
		bean.setPlaza_time(map.get(plaza_time));
		bean.setPlazaid(map.get(plaza_id));
		bean.setType(map.get(type));
		return bean;
	}

}
