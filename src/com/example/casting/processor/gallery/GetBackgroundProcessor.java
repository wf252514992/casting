package com.example.casting.processor.gallery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.casting.entity.BackgroundBean;
import com.example.casting.entity.BaseBean;
import com.example.casting.entity.FileBean;
import com.example.casting.entity.GalleryBean;
import com.example.casting.processor.BaseProcessor;
import com.example.casting.processor.Errors;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.CastMap;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.publisheddynamic.EditPhotoDynamic;
import com.example.casting.util.Session;

public class GetBackgroundProcessor extends BaseProcessor
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return ProcessorID.method_getbackground;
	}

	@Override
	public Map<String, String> Bean2Map(Object obj) {
		return null;
	}

	@Override
	public JSONObject Bean2Json(Object obj) throws JSONException {
		if( obj instanceof BaseBean){
			BaseBean bean = (BaseBean)obj;
			JSONObject map = new JSONObject();
			map.put("id", bean.getId());
			return map;
		}
		return super.Bean2Json(obj);
	}
	@Override
	public ResultBean json2Bean(String returnstr) {
		try {
			String result = getResultStr(returnstr);
			JSONObject object = new JSONObject(result);
			ResultBean resultbean = null ;
				if( object != null){
					BackgroundBean bean = map2Bean(new CastMap(object));
					resultbean = new ResultBean(Errors.OK, bean);
				}
				return resultbean;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ResultBean bean = getResultCode(returnstr);
			return bean;

		}
	}

	@Override
	public String getService() {
		return ProcessorID.service_background;
	}
	protected BackgroundBean map2Bean(CastMap map) {
		if( map!=null){
			BackgroundBean bean = new BackgroundBean();
			bean.setUrl_audition(map.get("audition_background"));
			bean.setUrl_cover(map.get("custom_background"));
			bean.setUrl_personshow(map.get("gallery_background"));
			bean.setUrl_userinfo(map.get("user_background"));
		    bean.setUrl_video(map.get("video_background"));
		    bean.setUrl_winners(map.get("winners_background"));
		    bean.setUrl_works(map.get("works_background"));
			return bean ;
		}
		return null;
	}
}
