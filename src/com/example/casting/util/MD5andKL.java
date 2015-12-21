package com.example.casting.util;

import java.security.MessageDigest;

/**
 * @Title: MD5andKL.java
 * @Package com.hn122.tool
 * @author:wf
 * @date: 2010-12-20 上午09:59:40
 * @Description: MD加密与李夏的文件一致
 * @version V1.0
 */
public class MD5andKL {

	// MD5加码。32位
	public static String MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];

		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	// 可逆的加密算法
	public static String KL(String inStr) {
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++)
			a[i] = (char) (a[i] ^ 't');

		String s = new String(a);
		return s.toLowerCase();
	}

	// 加密后解密
	public static String JM(String inStr) {
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++)
			a[i] = (char) (a[i] ^ 't');

		String k = new String(a);
		return k;
	}

	// 适应.net服务端的编码方式加密md5
	public static String netMD5(String str) {
		try {
			byte[] bys = str.getBytes("UTF-16LE");
			return BzMD5(bys).toUpperCase();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	// 实现标准的md5加密
	public static String BzMD5(byte[] bytes) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] hash = digest.digest(bytes);
			return toHex(hash);
		} catch (Exception ex) {
			return "";
		}
	}

	private final static char[] HEX = "0123456789abcdef".toCharArray();

	public static String toHex(byte[] bys) {
		char[] chs = new char[bys.length * 2];
		for (int i = 0, k = 0; i < bys.length; i++) {
			chs[k++] = HEX[(bys[i] & 0xf0) >> 4];
			chs[k++] = HEX[bys[i] & 0xf];
		}
		return new String(chs);
	}
}
