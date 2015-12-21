package com.example.casting.processor.recruit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.casting.entity.BaseBean;
import com.example.casting.entity.RecruitBean;
import com.example.casting.processor.BaseProcessor;
import com.example.casting.processor.Errors;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.CastMap;
import com.example.casting.processor.data.ResultBean;
import com.example.casting.util.Session;

/**
 * 获取我的招募信息
 * 
 * @author wf
 * 
 */
public class GetRecruitListProcessor extends BaseProcessor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getService() {
		// TODO Auto-generated method stub
		return ProcessorID.service_recruit;
	}

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return ProcessorID.method_recruit_getList;
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
		try {
			String result = getResultStr(returnstr);
			JSONArray array = new JSONArray(result);
			List<RecruitBean> beans = new ArrayList<RecruitBean>();
			for (int i = 0; i < array.length(); i++) {
				if (!array.isNull(i)) {
					JSONObject json = (JSONObject) array.get(i);
					RecruitBean bean = map2bean(new CastMap(json));
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

	/**
	 * 
	 * [{"dynamic_id" :"3","id":"1","title":"","content":"test","file_type"
	 * :"","image_url" :"","video_url":"","voice_url":"","s_image_url":"",
	 * "up_to_date":"" ,"type":"1","dynamic_time":"2015-01-04 09:46:54.0"}]
	 * 
	 */

	/**
	 * 动态id
	 */
	private final static String dynamic_id = "dynamic_id";
	/**
	 * 标题
	 */
	private final static String title = "title";
	/**
	 * 内容
	 */
	private final static String content = "content";
	/**
	 * 文件类型,值：image,video,voice
	 */
	private final static String file_type = "file_type";
	private final static String image_url = "image_url";
	private final static String video_url = "video_url";
	private final static String voice_url = "voice_url";
	/**
	 * 缩略图地址
	 */
	private final static String s_image_url = "s_image_url";
	/**
	 * 招募截至日期
	 */
	private final static String up_to_date = "up_to_date";
	/**
	 * 动态类型:普通动态和招募动态,值：0,1
	 */
	private final static String type = "type";
	/**
	 * 发布时间
	 */
	private final static String dynamic_time = "dynamic_time";
	
	private RecruitBean map2bean(CastMap map){
		if(map!=null){
			RecruitBean bean = new RecruitBean();
			bean.setContent(map.get(content));
			bean.setDynamic_id(map.get(dynamic_id));
			bean.setDynamic_time(map.get(dynamic_time));
			bean.setFile_type(map.get(file_type));
			bean.setImage_url(map.get(image_url));
			bean.setS_image_url(map.get(s_image_url));
			bean.setTitle(map.get(title));
			bean.setType(map.get(type));
			bean.setUp_to_date(map.get(up_to_date));
			bean.setVideo_url(map.get(video_url));
			bean.setVoice_url(map.get(voice_url));
			return bean;
		}
		return null;
	}
}
