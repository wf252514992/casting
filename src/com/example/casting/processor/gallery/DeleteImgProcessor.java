package com.example.casting.processor.gallery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.casting.entity.FileBean;
import com.example.casting.entity.GalleryBean;
import com.example.casting.processor.Errors;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.publisheddynamic.EditPhotoDynamic;
import com.example.casting.util.Session;

public class DeleteImgProcessor extends BaseGalleryProcessor
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return ProcessorID.method_person_play_del;
	}

	@Override
	public Map<String, String> Bean2Map(Object obj) {
		return null;
	}

	@Override
	public JSONObject Bean2Json(Object obj) throws JSONException {
		if( obj instanceof GalleryBean){
			GalleryBean bean = (GalleryBean)obj;
			JSONObject map = new JSONObject();
			List<String> values = new ArrayList<String>();
			values.add(bean.getGallery_id());
			map.put("gallery_id",values);
			return map;
		}
		return super.Bean2Json(obj);
	}
	@Override
	public ResultBean json2Bean(String returnstr) {
		try {
			String  result = getResultStr(returnstr);
			JSONObject json = new JSONObject(result);
			ResultBean bean = new ResultBean(Errors.OK, json);
			return bean;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ResultBean bean = getResultCode(returnstr);
			return bean;
			
		}
	}

}
