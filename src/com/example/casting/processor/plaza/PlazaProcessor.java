package com.example.casting.processor.plaza;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import com.example.casting.entity.PlazaBean;
import com.example.casting.processor.Errors;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.CastMap;
import com.example.casting.processor.data.ResultBean;

/**
 * 榜单关注
 * 
 * @author wf
 * 
 */
public class PlazaProcessor extends BasePlazzaProcessor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return ProcessorID.method_plazaget;
	}

	@Override
	public Map<String, String> Bean2Map(Object obj) {
		// TODO Auto-generated method stub
		if(obj instanceof PlazaBean){
			PlazaBean bean = (PlazaBean) obj;
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", bean.getId());
			map.put("pagenum", bean.getPagenum()+"");
			if(bean.getSearchtype().equals("3")){
				map.put("sex", bean.getSex());
			}else if(bean.getSearchtype().equals("2")){
				map.put("labels", bean.getSex());
			}else if(bean.getSearchtype().equals("1")){
				map.put("minAge", bean.getMinage());
				map.put("maxAge", bean.getMaxage());
			}
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
			List<PlazaBean> beans = new ArrayList<PlazaBean>();
			for( int i = 0 ; i < array.length() ; i++){
				if( !array.isNull(i)){
					JSONObject json = (JSONObject) array.get(i);
					PlazaBean bean = map2Bean(new CastMap(json));
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

}
