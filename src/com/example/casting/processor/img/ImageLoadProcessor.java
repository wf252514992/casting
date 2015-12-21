package com.example.casting.processor.img;

import java.util.Map;

import org.json.JSONArray;

import com.example.casting.processor.BaseProcessor;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.ResultBean;

public class ImageLoadProcessor extends BaseProcessor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String imgpath = "";
	public ImageLoadProcessor(String imgpath){
		this.imgpath = imgpath;
	}
	@Override
	public String getService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return imgpath;
	}

	@Override
	public Map<String, String> Bean2Map(Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultBean json2Bean(String returnstr) {
		// TODO Auto-generated method stub
		try {
			String result = getResultStr(returnstr);
			JSONArray array = new JSONArray(result);
//			List<PlazaBean> beans = new ArrayList<PlazaBean>();
//			for( int i = 0 ; i < array.length() ; i++){
//				if( !array.isNull(i)){
//					JSONObject json = (JSONObject) array.get(i);
//					PlazaBean bean = map2Bean(new CastMap(json));
//					beans.add(bean);
//				}
//			}
			
//			ResultBean resultbean = new ResultBean(Errors.OK, beans);
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ResultBean bean = getResultCode(returnstr);
			return bean;

		}
	}
	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return ProcessorID.uri_headphoto+getMethod();
	}
}
