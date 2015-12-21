package com.example.casting.processor.works;

import com.example.casting.processor.BaseProcessor;
import com.example.casting.processor.ProcessorID;

public abstract class WorkBaseProcessor extends BaseProcessor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getService() {
		// TODO Auto-generated method stub
		return ProcessorID.service_works;
	}

	/**
	 * 用户id
	 */
	public String id = "id";
	/**
	 * 作品id
	 */
	public String works_id = "works_id";
	/**
	 * 剧名
	 */
	public String works_name = "works_name";
	/**
	 * 上映时间
	 */
	public String release_time = "release_time";
	/**
	 * 扮演角色
	 */
	public String role = "role";
	/**
	 * 导演
	 */
	public String director = "director";
	/**
	 * 合作演员
	 */
	public String cooperation_actor = "cooperation_actor";
	/**
	 * 提交时间
	 */
	public String submit_time = "submit_time";
	/**
	 * 作品图像
	 */
	public String works_images = "works_images";
	/**
	 * 备注
	 */
	public String remark = "remark";
}
