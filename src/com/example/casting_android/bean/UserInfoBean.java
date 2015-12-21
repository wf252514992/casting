package com.example.casting_android.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.example.casting_android.R;
import com.example.casting_android.beanconvert.BeanFieldName;
import com.example.casting_android.beanconvert.JwtBeanName;

public class UserInfoBean implements Serializable, IDisplay {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JwtBeanName(cnName = "姓名")
	private String XM="";
	@JwtBeanName(cnName = "性别")
	private String XB="";
	@JwtBeanName(cnName = "年龄")
	private String JL= "";
	@JwtBeanName(cnName = "关系")
	private String GX= "0";
	@JwtBeanName(cnName = "粉丝")
	private String FS= "";
	@JwtBeanName(cnName = "角色")//用户类型:普通用户和导演(1/2)
	private String JS = "1";
	@JwtBeanName(cnName = "头像")
	private ImageBean img = new ImageBean(ImageBean.type_res, R.drawable.ic_launcher+"") ;
	@JwtBeanName(cnName = "昵称")
	private String NICKNAME = "";
	@JwtBeanName(cnName = "所在地")
	private String SZD = "";
	@JwtBeanName(cnName = "简介")
	private String JJ = "";
	@JwtBeanName(cnName = "生日")
	private String BIRTH = "";
	@JwtBeanName(cnName = "标签")
	private String[] LABEL ={} ;
	@JwtBeanName(cnName = "邮箱")
	private String EMAIL = "";
	@JwtBeanName(cnName = "QQ")
	private String QQ = "";
	@JwtBeanName(cnName = "微信")
	private String WECHAT = "";
	@JwtBeanName(cnName = "注册时间")
	private String ZCSJ = "";
	@JwtBeanName(cnName = "用户id")
	private String userid = "";
	public String getXM() {
		return XM;
	}


	public void setXM(String xM) {
		XM = xM;
	}


	public String getNICKNAME() {
		return NICKNAME;
	}


	public String getUserid() {
		return userid;
	}


	public void setUserid(String userid) {
		this.userid = userid;
	}


	public int getStatue(){
		String gx = getGX();
		try{
			int statue = Integer.parseInt(gx);
			return statue ;
		}catch(Exception ex){
			if( gx.equals("已关注")){
				return 1;
			}else if( gx.equals("相互关注"))
			{
				return 2 ;
			}else return 0 ;
		}
	}
	public void setNICKNAME(String nICKNAME) {
		NICKNAME = nICKNAME;
	}


	public String getXB() {
		return XB;
	}




	public String getSZD() {
		return SZD;
	}


	public void setSZD(String sZD) {
		SZD = sZD;
	}


	public String getJJ() {
		return JJ;
	}


	public void setJJ(String jJ) {
		JJ = jJ;
	}


	public String getBIRTH() {
		return BIRTH;
	}


	public void setBIRTH(String bIRTH) {
		BIRTH = bIRTH;
	}


	public String[] getLABEL() {
		return LABEL;
	}


	public void setLABEL(String[] lABEL) {
		LABEL = lABEL;
	}


	public String getEMAIL() {
		return EMAIL;
	}


	public void setEMAIL(String eMAIL) {
		EMAIL = eMAIL;
	}


	public String getQQ() {
		return QQ;
	}


	public void setQQ(String qQ) {
		QQ = qQ;
	}


	public String getWECHAT() {
		return WECHAT;
	}


	public void setWECHAT(String wECHAT) {
		WECHAT = wECHAT;
	}


	public String getZCSJ() {
		return ZCSJ;
	}


	public void setZCSJ(String zCSJ) {
		ZCSJ = zCSJ;
	}


	public void setXB(String xB) {
		XB = xB;
	}


	public String getJL() {
		return JL;
	}


	public void setJL(String jL) {
		JL = jL;
	}


	public String getGX() {
		return GX;
	}


	public void setGX(String gX) {
		GX = gX;
	}




	public String getFS() {
		return FS;
	}


	public void setFS(String fS) {
		FS = fS;
	}


	public String getJS() {
		return JS;
	}


	public void setJS(String jS) {
		JS = jS;
	}


	public ImageBean getImg() {
		return img;
	}


	public void setImg(ImageBean img) {
		this.img = img;
	}




	@Override
	public Map<String, String> toHashMap() {
		// TODO Auto-generated method stub
		Map<String, String> data = new LinkedHashMap<String, String>();
		data.put(BeanFieldName.getCnName(this, "NICKNAME"), NICKNAME);
		data.put(BeanFieldName.getCnName(this, "XM"), XM);
		data.put(BeanFieldName.getCnName(this, "XB"), XB);
		data.put(BeanFieldName.getCnName(this, "SZD"), SZD);
		data.put(BeanFieldName.getCnName(this, "JJ"), JJ);
		data.put(BeanFieldName.getCnName(this, "BIRTH"), BIRTH);
		String labels = "";
		for( String l : LABEL){
			labels+=l+"|";
		}
		data.put(BeanFieldName.getCnName(this, "LABEL"), labels);
		data.put(BeanFieldName.getCnName(this, "EMAIL"), EMAIL);
		data.put(BeanFieldName.getCnName(this, "QQ"), QQ);
		data.put(BeanFieldName.getCnName(this, "WECHAT"), WECHAT);
		data.put(BeanFieldName.getCnName(this, "ZCSJ"), ZCSJ);
		return data;
	}

}
