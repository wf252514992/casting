package com.example.casting.processor.gallery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.example.casting.entity.BackgroundBean;
import com.example.casting.entity.FileBean;
import com.example.casting.entity.GalleryBean;
import com.example.casting.processor.BaseProcessor;
import com.example.casting.processor.Errors;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.CastMap;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.publisheddynamic.EditPhotoDynamic;
import com.example.casting.util.PicTool;
import com.example.casting.util.Session;
import com.example.casting.util.view.Util;
import com.example.casting_android.R;
/**
 * 设置背景图片
 * @author  huangjian
 *
 */
public class SetBackgroundProcessor extends BaseProcessor
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Context context;
	public SetBackgroundProcessor (Context context)
	{
		this.context = context;
	}

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return ProcessorID.method_setbackground;
	}

	@Override
	public Map<String, String> Bean2Map(Object obj) {
		return null;
	}

	@Override
	public JSONObject Bean2Json(Object obj) throws JSONException {
		if( obj instanceof BackgroundBean){
			BackgroundBean bean = (BackgroundBean)obj;
			JSONObject map = new JSONObject();
			map.put("id", Session.getInstance().getUser_id());
			map.put("type",bean.getImg_type());
			map.put("background",bean.getBackgroundType());
			map.put("imageStr",bean.getImg_data() );
			return map;
		}
		return super.Bean2Json(obj);
	}
	@Override
	public ResultBean json2Bean(String returnstr) {
		try {
			String result = getResultStr(returnstr);
			JSONObject json = new JSONObject(result);
			BackgroundBean bean =  map2Bean(new CastMap(json));
			ResultBean resultbean = new ResultBean(Errors.OK, bean);
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
			bean.setImg_url(map.get("url"));
			return bean ;
		}
		return null;
	}
}
