package com.example.casting.processor.file;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.example.casting.entity.FileBean;
import com.example.casting.entity.RegistBean;
import com.example.casting.processor.BaseProcessor;
import com.example.casting.processor.Errors;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.CastMap;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.publisheddynamic.EditPhotoDynamic;
import com.example.casting.util.PicTool;
import com.example.casting.util.Session;
import com.example.casting.util.view.Util;

public class UploadProcessor extends BaseProcessor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getService() {
		// TODO Auto-generated method stub
		return ProcessorID.service_file;
	}

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return ProcessorID.method_upload64;
	}

	@Override
	public Map<String, String> Bean2Map(Object obj) {
		// TODO Auto-generated method stub
		if( obj instanceof FileBean){
			FileBean file = (FileBean)obj;
//			String data = "";
//			if(file.getDatatype()==FileBean.DT_FILEPATH){
//				try {
//					if(Util.isBmp(file.getFieldata()))
//					{
//						data = EditPhotoDynamic.encodeBase64Byte(PicTool.compressByte(PicTool.getYsBitmap(file.getFieldata(), 480, 640),80));
//					}
//					else
//					{
//						data = EditPhotoDynamic.encodeBase64File(file.getFieldata());
//					}
//				
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}else{
//				data = file.getFieldata();
//			}
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", Session.getInstance().getUser_id());
			map.put("type", file.getEndtag());
			map.put("imageStr", file.getFieldata());
			return map;
		}
		return null;
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
