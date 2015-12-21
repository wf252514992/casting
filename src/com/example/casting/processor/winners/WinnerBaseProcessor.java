package com.example.casting.processor.winners;

import com.example.casting.entity.WinnerBean;
import com.example.casting.processor.BaseProcessor;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.CastMap;
import com.example.casting_android.bean.ImageBean;

public abstract class WinnerBaseProcessor extends BaseProcessor{

	
	
	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;

	@Override
	public String getService() {
		// TODO Auto-generated method stub
		return ProcessorID.service_winner;
	}
	/**
	 * 经历id
	 */
	public String winners_id = "winners_id";
	/**
	 * 获奖年份
	 */
	public String winners_time = "winners_time";
	/**
	 * 获得奖项
	 */
	public String winners_name = "winners_name";
	/**
	 * 获奖作品
	 */
	public String winners_works_name = "winners_works_name";
	/**
	 * 提交时间
	 */
	public String submit_time = "winners_works_name";
	/**
	 * 作品头像
	 */
	public String winners_images ="winners_images";
	/**
	 * 备注
	 */
	public String remark = "remark";
	protected WinnerBean map2bean(CastMap map) {
		if(map!=null){
			WinnerBean bean = new WinnerBean();
			bean.setSubmit_time(map.get(submit_time));
			bean.setWinners_id(map.get(winners_id));
			bean.setWinners_name(map.get(winners_name));
			bean.setWinners_time(map.get(winners_time));
			bean.setWinners_works_name(map.get(winners_works_name));
			bean.setWinners_images(map.get(winners_images));
			bean.setBz(map.get(remark));
			String imgurl =map.get(winners_images);
			if(imgurl!=null && imgurl.length()>0){
				ImageBean img = new ImageBean(ImageBean.type_url, imgurl);
				bean.setWinnerspic(img);
			}
			return bean;
		}
		return null;
	}
}
