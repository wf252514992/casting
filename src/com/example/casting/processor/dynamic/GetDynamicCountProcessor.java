package com.example.casting.processor.dynamic;

import java.util.HashMap;
import java.util.Map;
import com.example.casting.entity.RegistBean;
import com.example.casting.processor.BaseProcessor;
import com.example.casting.processor.Errors;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.ResultBean;

public class GetDynamicCountProcessor extends BaseProcessor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getService() {
		// TODO Auto-generated method stub
		return ProcessorID.service_dynamic;
	}

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return ProcessorID.method_getCount;
	}

	@Override
	public Map<String, String> Bean2Map(Object obj) {
		// TODO Auto-generated method stub
		if(obj !=null && obj instanceof RegistBean ){
			RegistBean registbean = (RegistBean) obj;
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id",registbean.getId());
			map.put("nickname", registbean.getNickname());
			return map;
		}
		return null;
	}

	@Override
	public ResultBean json2Bean(String returnstr) {
		try {
			String result = getResultStr(returnstr);
			Integer.parseInt(result);//如果能正常转换则证明返回的是正确的 动态数，否则 未 错误信息
			StringBuffer sb = new StringBuffer(result);
			ResultBean resultbean = new ResultBean(Errors.OK, sb);
			return resultbean;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ResultBean bean = getResultCode(returnstr);
			return bean;

		}
	}

}
