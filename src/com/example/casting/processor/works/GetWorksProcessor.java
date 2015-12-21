package com.example.casting.processor.works;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.casting.entity.BaseBean;
import com.example.casting.entity.WorkBean;
import com.example.casting.processor.Errors;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.CastMap;
import com.example.casting.processor.data.ResultBean;
import com.example.casting_android.bean.ImageBean;

public class GetWorksProcessor extends WorkBaseProcessor {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return ProcessorID.method_works_getList;
	}

	@Override
	public Map<String, String> Bean2Map(Object obj) {
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
			String result = getResultStr(returnstr);
			JSONArray array = new JSONArray(result);
			List<WorkBean> beans = new ArrayList<WorkBean>();
			for( int i = 0 ; i < array.length() ; i++){
				if( !array.isNull(i)){
					JSONObject json = (JSONObject) array.get(i);
					WorkBean bean = map2bean(new CastMap(json));
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
	
	private WorkBean map2bean(CastMap map){
		if( map!=null){
			WorkBean bean = new WorkBean();
			bean.setCooperation_actor(map.get(cooperation_actor));
			bean.setDirector(map.get(director));
			bean.setRelease_time(map.get(release_time));
			bean.setRole(map.get(role));
			bean.setSubmit_time(map.get(submit_time));
			bean.setWorks_id(map.get(works_id));
			bean.setWorks_name(map.get(works_name));
			bean.setWorks_images(map.get(works_images));
			bean.setId(map.get(id));
			bean.setBz(map.get(remark));
			String imgurl = map.get(works_images);
			if(imgurl!=null&& imgurl.length()>0){
				ImageBean img =new ImageBean(ImageBean.type_url,imgurl);
				bean.setWorkpic(img);
			}
			return bean;
		}
		return null;
	}

}
