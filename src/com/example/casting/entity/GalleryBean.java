package com.example.casting.entity;

import java.util.ArrayList;

public class GalleryBean extends BaseBean{


	/**
     * 是否是空白部分
     */
    public boolean isBlankPlace = false;
	public boolean isBlankPlace() {
		return isBlankPlace;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String gallery_id = "";
	private String description = "";
	private String url = "";
	/**
	 * 缩略图url
	 */
	private String s_url="";
	public String getS_url() {
		return s_url;
	}
	public void setS_url(String s_url) {
		this.s_url = s_url;
	}
	private String submit_time = "";
	private  ArrayList<String> urls = new ArrayList<String>();
	public void setUrls(ArrayList<String> urls) {
		this.urls = urls;
	}
	public String getGallery_id() {
		return gallery_id;
	}
	public void setGallery_id(String gallery_id) {
		this.gallery_id = gallery_id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSubmit_time() {
		return submit_time;
	}
	public void setSubmit_time(String submit_time) {
		this.submit_time = submit_time;
	}
	public String getUrls()
	{
		StringBuffer urlString = new StringBuffer();
		int size = urls.size();
		if(urls != null && size>0)
		{
			for(int i = 0;i<size;i++)
			{
				urlString.append(urls.get(i));
				if(i!=size-1)
				{
					urlString.append(",");
				}
			}
		}
		return urlString.toString();
			
	}
	public GalleryBean (boolean isBlankPlace)
	{
		this.isBlankPlace = isBlankPlace;
	}
	public GalleryBean ()
	{
	}
	/**
     * 产生一个空白位置
     */
    public static GalleryBean newBlankMenu() {
        return new GalleryBean(true);
    }

	
}
