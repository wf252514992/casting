package com.example.casting_android.beanconvert;

import java.lang.reflect.Field;
/**
 * 
 * @author daiyu
 *
 */
public class BeanFieldName {
	public static String getCnName(Object obj, String fieldName) {
		try {

			Field field = obj.getClass().getDeclaredField(fieldName);
			JwtBeanName jwtBean = field.getAnnotation(JwtBeanName.class);
			return jwtBean.cnName();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fieldName;
	}
}
