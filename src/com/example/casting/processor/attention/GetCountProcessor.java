package com.example.casting.processor.attention;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import com.example.casting.entity.BaseBean;
import com.example.casting.entity.MyCountBean;
import com.example.casting.processor.Errors;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.CastMap;
import com.example.casting.processor.data.ResultBean;

public class GetCountProcessor extends BaseAttentionProcessor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return ProcessorID.method_attention_getCount;
	}

	@Override
	public Map<String, String> Bean2Map(Object obj) {
		// TODO Auto-generated method stub
		if( obj !=null && obj instanceof BaseBean ){
			BaseBean bean = (BaseBean)obj;
			HashMap< String, String> map = new HashMap<String, String>();
			map.put("id",bean.getId());
			map.put("pagenum", bean.getPagenum());
			return map;
		}
		return null;
	}

	@Override
	public ResultBean json2Bean(String returnstr) {
		try {
			String  result = getResultStr(returnstr);
			JSONObject json = new JSONObject(result);
			MyCountBean regist = map2Bean(new CastMap(json));
			ResultBean bean = new ResultBean(Errors.OK, regist);
			return bean;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ResultBean bean = getResultCode(returnstr);
			return bean;
			
		}
	}
	
	private MyCountBean  map2Bean(CastMap map){
		MyCountBean  bean = new MyCountBean();
		bean.setAttention(map.get("attent"));
		bean.setFans(map.get("fans") );
		bean.setMult(map.get("mutual") );
		return bean;
	}
}