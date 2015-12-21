package com.example.casting.processor.setting;


import com.example.casting.entity.SettingBean;
import com.example.casting.processor.BaseProcessor;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.CastMap;

public abstract class BaseSetProcessor extends BaseProcessor{
	
	@Override
	public String getService() {
		// TODO Auto-generated method stub
		return ProcessorID.service_setting;
	}
	
	public SettingBean map2bean(CastMap map){
		if(map!=null){
			SettingBean  bean = new SettingBean();
			bean.setComment(Integer.parseInt(map.get(comment)));
			bean.setLetter(Integer.parseInt(map.get(letter)));
			bean.setRemind_attention(Integer.parseInt(map.get(remind_attention)));
			bean.setRemind_comment(Integer.parseInt(map.get(remind_comment)));
			bean.setRemind_letter(Integer.parseInt(map.get(remind_letter)));
			return  bean;
		}
		return null;
	}
	/**
	 * 
	 */
	protected static final long serialVersionUID = 1L;
	protected static final String id = "id";
	protected static final String comment = "comment";
	protected static final String letter = "letter";
	protected static final String remind_comment = "remind_comment";
	protected static final String remind_letter = "remind_letter";
	protected static final String remind_attention = "remind_attention";
	
}
