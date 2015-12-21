package com.example.casting.processor.attention;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.casting.entity.AttentionBean;
import com.example.casting.entity.RecruitBean;
import com.example.casting.processor.Errors;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.CastMap;
import com.example.casting.processor.data.ResultBean;

/**
 * 关注
 * 
 * @author Administrator
 * 
 */
public class IsAttentionProcessor extends BaseAttentionProcessor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return ProcessorID.method_attention_isadd;
	}

	@Override
	public Map<String, String> Bean2Map(Object obj) {
		// TODO Auto-generated method stub
		if (obj != null && obj instanceof AttentionBean) {
			AttentionBean bean = (AttentionBean) obj;
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("from_id", bean.getFollowers_id());
			map.put("to_id", bean.getBy_follower_id());
			return map;
		}
		return null;
	}


	@Override
	public ResultBean json2Bean(String returnstr) {
		try {
			String result = getResultStr(returnstr);
			JSONObject json = new JSONObject(result);
			String value = getStatue(new CastMap(json));
			if(value.equals("0")){
				//未关注
				ResultBean resultbean = new ResultBean(Errors.BaseLocalError);
				return resultbean;
			}else{
				//已关注
				ResultBean resultbean = new ResultBean(Errors.OK, value);
				return resultbean;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ResultBean bean = getResultCode(returnstr);
			return bean;

		}
		
	}

	private String getStatue(CastMap map){
		if( map !=null){
			// 我关注对方的状态 0=未关注，1=已关注
			 String att = map.get(attention);
			// 对方关注我的状态0=未关注，1=已关注
			 String beatt = map.get(attention_me);

			if (att.equals("0")) {
				// 我未关注对方
				return "0";
			} else if (att.equals("1")) {
				if (beatt.equals("0")) {
					// 对方为关注我
					return "1";
				} else if (beatt.equals("1")) {
					// 相互关注
					return "2";
				}
			}
		}
		return "0";
	}
}
