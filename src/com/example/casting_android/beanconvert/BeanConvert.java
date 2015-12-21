package com.example.casting_android.beanconvert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BeanConvert {
	IDicConvert dicConvert;

	public BeanConvert(IDicConvert ic) {
		this.dicConvert = ic;
	}

	/**
	 * 
	 * @param obj
	 *            根据已有对象克隆出一个新对象，并对字段进行字典转换
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public Object runMethod(Object obj) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, IOException,
			ClassNotFoundException {
		// 获得一个克隆对象
		Object _obj = copy(obj);

		List<DicConvertObj> list = new ArrayList<DicConvertObj>();

		if (_obj != null) {
			for (Method m : _obj.getClass().getMethods()) {
				if (m.isAnnotationPresent(JwtDic.class)
						&& m.getReturnType() == String.class) {

					JwtDic jwtDic = m.getAnnotation(JwtDic.class);
					String name = jwtDic.dic();
					String value = (String) m.invoke(_obj, null);

					String setMethodName = "";
					if (m.getName().startsWith("get")) {
						setMethodName = m.getName().replaceFirst("get", "set");
					}

					DicConvertObj dco = new DicConvertObj();
					dco.setDicName(name);
					dco.setValue(value);
					dco.setSetMethodName(setMethodName);
					list.add(dco);

				}
			}
			
			// 重新设置值
			for (DicConvertObj dco : list) {
				for (Method m : _obj.getClass().getMethods()) {
					if (m.getName().equals(dco.getSetMethodName())) {
						// if (dco.getDicName().equals("sex")) {
						String value = this.dicConvert.convertSex(
								dco.getDicName().trim(), dco.getValue()==null?"":dco.getValue().trim());
						// System.out.println("value 2 : " + value);
						m.invoke(_obj, value);
						// }
					}
				}
			}
		}
		return _obj;
	}

	/**
	 * 深度克隆对象
	 * 
	 * @param obj
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Object copy(Object obj) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(obj);
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(
				bos.toByteArray()));
		Object newObj = ois.readObject();

		ois.close();
		oos.close();
		bos.close();

		return newObj;
	}

	public static void main(String[] args) {
		BeanExample cj = new BeanExample();
		cj.setSex("1");

		BeanConvert dc = new BeanConvert(new DicConvertExample());
		try {
			BeanExample cj2 = (BeanExample) dc.runMethod(cj);
			System.out.println("   原对象性别：" + cj.getSex());
			System.out.println("clone对象性别：" + cj2.getSex());

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
