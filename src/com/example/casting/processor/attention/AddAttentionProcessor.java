package com.example.casting.processor.attention;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.casting.entity.AttentionBean;
import com.example.casting.processor.Errors;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.ResultBean;

/**
 * 关注
 * 
 * @author Administrator
 * 
 */
public class AddAttentionProcessor extends BaseAttentionProcessor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return ProcessorID.method_attention_add;
	}

	@Override
	public Map<String, String> Bean2Map(Object obj) {
		// TODO Auto-generated method stub
		if (obj != null && obj instanceof AttentionBean) {
			AttentionBean bean = (AttentionBean) obj;
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(followers_id, bean.getFollowers_id());
			map.put(by_follower_id, bean.getBy_follower_id());
			return map;
		}
		return null;
	}

	@Override
	public JSONObject Bean2Json(Object obj) throws JSONException {
		// TODO Auto-generated method stub
		if (obj != null && obj instanceof AttentionBean) {
			AttentionBean bean = (AttentionBean) obj;
			JSONObject json = new JSONObject();
			json.put(followers_id, bean.getFollowers_id());
			json.put(by_follower_id, bean.getBy_follower_id());
			return json;
		}
		return super.Bean2Json(obj);
	}

	@Override
	public ResultBean json2Bean(String returnstr) {
		try {
			String result = getResultStr(returnstr);
			if (result.equals("1")) {
				ResultBean bean = new ResultBean(Errors.OK, null);
				return bean;
			} else {
				ResultBean bean = getResultCode(returnstr);
				return bean;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ResultBean bean = getResultCode(returnstr);
			return bean;

		}
	}

}
