package com.example.casting.processor.Users;


import com.example.casting.entity.RegistBean;
import com.example.casting.processor.BaseProcessor;
import com.example.casting.processor.data.CastMap;

public abstract class BaseUserProcess extends BaseProcessor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户id
	 */
	public String user_id = "user_id";
	/**
	 * 用户类型:普通用户和导演(1/2)
	 */
	public String uset_type = "type";
	/**
	 * 实名
	 */
	public String name = "name";
	/**
	 * 匿名
	 */
	public String nickname = "nickname";
	/**
	 * 手机号码
	 */
	public String phone = "phone";
	/**
	 * 
	 */
	public String sex = "sex";
	/**
	 * 
	 */
	public String address = "address";
	/**
	 * 籍贯
	 */
	public String hometown = "hometown";
	/**
	 * 
	 */
	public String heigth = "height";
	/**
	 * 学历
	 */
	public String education = "educational_background";
	/**
	 * 
	 */
	public String weight = "weight";
	/**
	 * 
	 */
	public String language = "language";
	/**
	 * 爱好，特长
	 */
	public String specialty = "specialty";
	/**
	 * 
	 */
	public String age = "age";
	/**
	 * 简介
	 */
	public String introduction = "introduction";
	/**
	 * 头像
	 */
	public String head_portrait = "head_portrait";
	/**
	 * 
	 */
	public String birthday = "birthday";
	/**
	 * 标签
	 */
	public String labels = "labels";
	/**
	 * 
	 */
	public String email = "email";
	/**
	 * 
	 */
	public String qq = "qq";
	/**
	 * 
	 */
	public String wx = "wx";
	/**
	 * 注册时间
	 */
	public String registration_time = "registration_time";
	/**
	 * 认证（ 营业执照等图片链接）
	 */
	public String certification = "certification";
	/**
	 * 个人状态
	 */
	public String personal_status = "personal_status";
	/**
	 *  是否发送认证
	 */
	public String is_certification = "is_certification";
	protected RegistBean map2Bean(CastMap map) {
		try{
			RegistBean bean = new RegistBean();
			bean.setHometown(map.get(hometown));
			bean.setAddress(map.get(address));
			bean.setBirth(map.get(birthday));
			bean.setEducation(map.get(education));
			bean.setEmail(map.get(email));
			bean.setHeavy(map.get(weight));
			bean.setHeight(map.get(heigth));
			bean.setIntroduce(map.get(introduction));
			bean.setLabel(map.get(labels));
			bean.setLanguage(map.get(language));
			bean.setName(map.get(name));
			bean.setCertification(map.get(certification));
			bean.setPersonal_status(map.get(personal_status ));
			bean.setNickname(map.get(nickname));
			bean.setPhone(map.get(phone));
			bean.setQq(map.get(qq));
			bean.setId(map.get(user_id));
			bean.setSex(map.get(sex));
			bean.setSpecial(map.get(specialty));
			bean.setType(map.get(uset_type));
			bean.setWechat(map.get(wx));
			bean.setAge(map.get(age));
			bean.setHead_portrait(map.get(head_portrait));
//			bean.setIs_certification(map.get(is_certification));
			return bean;
		}catch(Exception ex){
			return null;
		}
		
	}
}
