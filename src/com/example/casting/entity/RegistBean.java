package com.example.casting.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.casting_android.R;
import com.example.casting_android.bean.ImageBean;

public class RegistBean extends BaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String name="";
	private String nickname="";
	private String age = "";
	//分为 个人=2，导演 =1
	private String type="";
	private String sex="";
	private String birth="";
	private String address="";
	private String height="";
	private String heavy="";
	private String education="";
	private String language="";
	private String introduce="";
	private String special="";
	private String personal_status = "";
	private ArrayList<String> labels=new ArrayList<String>();
	private String qq="";
	private String email="";
	private String wechat="";
	private String phone ="";
	private String hometown="";
	private String head_portrait ="";
	private String registtime = "";
	private String certification = "";
	private String password = "";
	private CompanyBean company = new CompanyBean();
	private String is_certification="";
	private ArrayList<WorkBean> works = new ArrayList<WorkBean>();
	private ArrayList<WinnerBean> winners = new ArrayList<WinnerBean>();
	private ArrayList<GalleryBean> gallerys = new ArrayList<GalleryBean>();
	private ArrayList<RecruitBean> recrutbeans = new ArrayList<RecruitBean>();
	private ImageBean userimp ;
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		try {
			Date date = format.parse(birth);
			format = new SimpleDateFormat("yyyy-MM-dd");
			birth = format.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.birth = birth;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getHeavy() {
		return heavy;
	}
	public void setHeavy(String heavy) {
		this.heavy = heavy;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public String getSpecial() {
		return special;
	}
	public void setSpecial(String special) {
		this.special = special;
	}
	public ArrayList<String> getLabel() {
		return labels;
	}
	public void addLabel(ArrayList<String> label) {
		this.labels.clear();
		this.labels.addAll(label);
	}
	
	public String getRegisttime() {
		return registtime;
	}
	public void setRegisttime(String registtime) {
		this.registtime = registtime;
	}
	public String getCertification() {
		return certification;
	}
	public void setCertification(String certification) {
		this.certification = certification;
	}
	public void addOneLabel(String label){
		this.labels.add(label);
	}
	public String getLabelVal(){
		String label = "";
		for(String la :labels){
			label+=la+",";
		}
		if(label.endsWith(",")){
			label = label.substring(0,label.length()-1);
		}
		return label;
	}
	public void setLabel(String labels){
		this.labels.clear();
		if(labels.length()>0 ){
			if( labels.indexOf(",")>-1){
				String[] values = labels.split(",");
				for(String val : values){
					if(val.length()>0)
						addOneLabel(val);
				}
			}else{
				addOneLabel(labels);
			}
			
		}
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWechat() {
		return wechat;
	}
	public void setWechat(String wechat) {
		this.wechat = wechat;
	}
	public String getHometown() {
		return hometown;
	}
	public void setHometown(String hometown) {
		this.hometown = hometown;
	}
	
	public String getHead_portrait() {
		return head_portrait;
	}
	public void setHead_portrait(String head_portrait) {
		this.head_portrait = head_portrait;
	}
	public ArrayList<WorkBean> getWorks() {
		return works;
	}
	public void setWorks(ArrayList<WorkBean> works) {
		this.works = works;
	}
	public ArrayList<WinnerBean> getWinners() {
		return winners;
	}
	public void setWinners(ArrayList<WinnerBean> winners) {
		this.winners = winners;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public ImageBean getUserimp() {
		if(userimp==null){
			if( head_portrait.length()>0){
				userimp = new ImageBean(ImageBean.type_url, head_portrait);
			}else{
				userimp = new ImageBean(ImageBean.type_res,R.drawable.me_edit_txsmall);
			}
		}
		return userimp;
	}
	public void setUserimp(ImageBean userimp) {
		this.userimp = userimp;
	}
	public CompanyBean getCompany() {
		return company;
	}
	public void setCompany(CompanyBean company) {
		this.company = company;
	}
	public String getPersonal_status() {
		return personal_status;
	}
	public void setPersonal_status(String personal_status) {
		this.personal_status = personal_status;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
//	public String getIs_certification() {
//		return is_certification;
//	}
//	public void setIs_certification(String is_certification) {
//		this.is_certification = is_certification;
//	}
	public ArrayList<GalleryBean> getGallerys() {
		return gallerys;
	}
	public void setGallerys(ArrayList<GalleryBean> gallerys) {
		this.gallerys = gallerys;
	}
	public ArrayList<RecruitBean> getRecrutbeans() {
		return recrutbeans;
	}
	public void setRecrutbeans(ArrayList<RecruitBean> recrutbeans) {
		this.recrutbeans = recrutbeans;
	}
	
	
}
