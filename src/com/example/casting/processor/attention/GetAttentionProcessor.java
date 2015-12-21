package com.example.casting.processor.attention;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import com.example.casting.entity.AttentionBean;
import com.example.casting.entity.BaseBean;
import com.example.casting.processor.Errors;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.CastMap;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.util.Session;

public class GetAttentionProcessor extends BaseAttentionProcessor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return ProcessorID.method_attention_getattention;
	}

	@Override
	public Map<String, String> Bean2Map(Object obj) {
		// TODO Auto-generated method stub
		if(obj!=null && obj instanceof BaseBean){
			BaseBean bean = (BaseBean)obj;
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", bean.getId() );
			map.put("pagenum",bean.getPagenum());
			return map;
		}
		return null;
	}

	@Override
	public ResultBean json2Bean(String returnstr) {
		try {
			String result = getResultStr(returnstr);
			JSONArray array = new JSONArray(result);
			List<AttentionBean> beans = new ArrayList<AttentionBean>();
			for( int i = 0 ; i < array.length() ; i++){
				if( !array.isNull(i)){
					JSONObject json = (JSONObject) array.get(i);
					AttentionBean bean = map2bean(new CastMap(json));
					beans.add(bean);
				}
			}
			
			ResultBean resultbean = new ResultBean(Errors.OK, beans);
			return resultbean;
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ResultBean bean = getResultCode(returnstr);
			return bean;

		}
	}

}
