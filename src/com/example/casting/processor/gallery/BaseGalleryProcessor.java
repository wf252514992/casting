package com.example.casting.processor.gallery;

import com.example.casting.entity.GalleryBean;
import com.example.casting.processor.BaseProcessor;
import com.example.casting.processor.ProcessorID;
import com.example.casting.processor.data.CastMap;

public abstract class BaseGalleryProcessor extends BaseProcessor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getService() {
		// TODO Auto-generated method stub
		return ProcessorID.service_gallery;
	}
	/**
	 * 展示id
	 */
	protected static final String gallery_id = "gallery_id";
	/**
	 * 描述
	 */
	protected static final String description = "descriptions";
	/**
	 * 图片url,最多8张
	 */
	protected static final String url = "url";
	/**
	 * 提交时间
	 */
	protected static final String submit_time = "submit_time";

	/**图片缩略图*/
    protected static final String s_image_url = "s_image_url";
	
	protected GalleryBean map2Bean(CastMap map) {
		if( map!=null){
			GalleryBean bean = new GalleryBean();
			bean.setDescription(map.get(description));
			bean.setGallery_id(map.get(gallery_id));
			bean.setSubmit_time(map.get(submit_time));
			bean.setUrl(map.get(url));
			bean.setS_url(map.get(s_image_url));
			return bean ;
		}
		return null;
	}
	

}
