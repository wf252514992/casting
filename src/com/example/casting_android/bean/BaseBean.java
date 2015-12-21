package com.example.casting_android.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.example.casting_android.beanconvert.JwtBeanName;


public class BaseBean {
	
	//调用方法名
	private String methodName;
	
	private String resultType;//表明本bean是由哪一个Processor处理过
	
	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	
	public  String toString()
	{
		Object obj = this ;
		Field[] m_fields = obj.getClass().getSuperclass().getDeclaredFields();
		Field[] p_fields = obj.getClass().getDeclaredFields();

		Field[] fields = new Field[m_fields.length + p_fields.length];
		for (int i = 0; i < p_fields.length; i++) {
			fields[i] = p_fields[i];
		}
		for (int i = 0; i < m_fields.length; i++) {
			fields[p_fields.length + i] = m_fields[i];
		} 
		String fieldName = "";
		String cnName ="" ;
		String result ="" ;
		String value ="" ;
		int l = getRegex().length() ;
		for (int i = 0; i < fields.length; i++) {
			fieldName = fields[i].getName(); 
			JwtBeanName jwtBean = fields[i].getAnnotation(JwtBeanName.class);
			if(jwtBean!=null)
				cnName = jwtBean.cnName() ;
			else
				cnName = fieldName ;
			value = String.valueOf(getFieldValueByName(fieldName,obj));
			result += getRegex() + cnName+":"+value ; 
			}
		
		if(result.length()>l) result = result.substring(l);
		return result ;
	}
	
	private String getRegex()
	{
		return "," ;
	}
	private HashMap getHashMap(String str)
	{
		HashMap<String,String> map = new HashMap();
		String[] arr = str.split(getRegex());
		 String key ="" ;
		 String value = "";
		 String preKey = "";
		
		for(int i =0;i<arr.length;i++)
		{
			 
			if(arr[i].endsWith(":"))
			{
				arr[i] +=" ";
			}
			String[] keyValue = arr[i].split(":"); 
			 if(keyValue.length>1)
			 {
				 preKey = key = keyValue[0];
				 value = keyValue[1].trim(); 
			 }else
			 {
				  key = preKey ;
				  value += getRegex()+ keyValue[0];
			 }
			 map.put(key, value);
		}
		return map ;
	}
	
	public  Object setObject(String str)
	{
		HashMap map = getHashMap(str);
		Field[] m_fields = this.getClass().getSuperclass().getDeclaredFields();
		Field[] p_fields = this.getClass().getDeclaredFields();
		
		Field[] fields = new Field[m_fields.length + p_fields.length];
		for (int i = 0; i < p_fields.length; i++) {
			fields[i] = p_fields[i];
		}
		for (int i = 0; i < m_fields.length; i++) {
			fields[p_fields.length + i] = m_fields[i];
		} 
		String fieldName = "";
		String cnName ="" ; 
		String value ="" ;
		for (int i = 0; i < fields.length; i++) {
			fieldName = fields[i].getName(); 
			JwtBeanName jwtBean = fields[i].getAnnotation(JwtBeanName.class);
			if(jwtBean!=null)
				cnName = jwtBean.cnName() ;
			else
				cnName = fieldName ;
			value = map.get(cnName)!=null ? String.valueOf(map.get(cnName)) : "";
			
			 String firstLetter = fieldName.substring(0, 1).toUpperCase();  
	         String setter = "set" + firstLetter + fieldName.substring(1);  
	       //  this.getClass().getDeclaredMethod(name, parameterTypes)
			Method method;
			try {
				if(!"setSerialVersionUID".equalsIgnoreCase(setter))
				{
				method = this.getClass().getMethod(setter,
						new Class[] { String.class });
				if(method!=null)
					method.invoke(this, new Object[] { value });
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			 
		} 
		
		return this ;
	}
	 
    
    /**
	 * 根据属性名获取属性值
	 * */
    private Object getFieldValueByName(String fieldName, Object o) {
        try {  
            String firstLetter = fieldName.substring(0, 1).toUpperCase();  
            String getter = "get" + firstLetter + fieldName.substring(1);  
            Method method = o.getClass().getMethod(getter, new Class[] {});  
            Object value = method.invoke(o, new Object[] {});  
            return value;  
        } catch (Exception e) {  
            return null;  
        }  
    } 

	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * @param methodName the methodName to set
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
}
