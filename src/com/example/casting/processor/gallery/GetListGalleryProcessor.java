package com.example.casting.processor.gallery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.casting.entity.BaseBean;
import com.example.casting.entity.GalleryBean;
import com.example.casting.processor.BaseProcessor;
import com.example.casting.processor.Errors;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.CastMap;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.util.Session;

public class GetListGalleryProcessor extends BaseGalleryProcessor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return ProcessorID.method_video_getlist;
	}
	@Override
	public Map<String, String> Bean2Map(Object obj) {
		// TODO Auto-generated method stub
		if( obj !=null && obj instanceof BaseBean ){
			BaseBean bean = (BaseBean)obj;
			HashMap< String, String> map = new HashMap<String, String>();
			map.put("id",bean.getId());
			map.put("nickname", "");
			map.put("pagenum", bean.getPagenum());
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
			List<GalleryBean> beans = new ArrayList<GalleryBean>();
			int size = array.length();
			for( int i = 0 ; i < size ; i++){
				if( !array.isNull(i)){
					JSONObject json = (JSONObject) array.get(i);
					GalleryBean bean = map2Bean(new CastMap(json));
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
	protected GalleryBean map2Bean(CastMap map) {
		if( map!=null){
			GalleryBean bean = new GalleryBean();
			bean.setDescription(map.get(description));
			bean.setGallery_id(map.get(gallery_id));
			bean.setSubmit_time(map.get(submit_time));
			bean.setUrl(map.get(url));
			bean.setS_url(map.get(s_image_url));
			return bean ;
		}
		return null;
	}
}
