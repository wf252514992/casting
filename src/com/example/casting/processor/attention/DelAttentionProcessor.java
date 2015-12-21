package com.example.casting.processor.attention;

import java.util.HashMap;
import java.util.Map;

import com.example.casting.entity.AttentionBean;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.ResultBean;

public class DelAttentionProcessor extends BaseAttentionProcessor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return ProcessorID.method_attention_del;
	}

	@Override
	public Map<String, String> Bean2Map(Object obj) {
		// TODO Auto-generated method stub
		if (obj != null && obj instanceof AttentionBean) {
			AttentionBean bean = (AttentionBean) obj;
			HashMap<String, String> map = new HashMap<String, String>();
//			map.put(followers_id, );
//			map.put(by_follower_id,);
			map.put("id", bean.getFollowers_id());
			map.put("to_id",  bean.getBy_follower_id());
			return map;
		}
		
		return null;
	}

	@Override
	public ResultBean json2Bean(String returnstr) {
		// TODO Auto-generated method stub
		return getResultCode(returnstr);
	}

}
