package com.shsxt.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * 字符串加密
 * @author Lisa Li
 *
 */
public class MD5Util {

	/**
	 * MD5加密
	 * @param str
	 * @return
	 */
	public static String encode(String str) {
		String value = null;
		try {
			
			// 得到md5加密的算法程序对象
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			
			// 将字符串转换成byte数组，进行Md5加密
			byte[] bytes = messageDigest.digest(str.getBytes());
			
			// 将加密过的byte数组通过Base64转换成字符串
			value =  Base64.encode(bytes);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return value;
	}
	
	public static void main(String[] args) {
		
		System.out.println(encode(encode("123456")));
		
	}
}
