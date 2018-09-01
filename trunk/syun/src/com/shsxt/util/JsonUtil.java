package com.shsxt.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

public class JsonUtil {

	/**
	 * 将对象转换为JSON字符串，并响应给前台（ajax的回调函数）
	 * @param object
	 * @param response
	 */
	public static void toJson(Object object, HttpServletResponse response) {
		// 设置响应类型和编码
		response.setContentType("application/json;charset=UTF-8");
		// 将对象转换成json字符串
		String json = JSON.toJSONString(object);
		// 得到输出流
		try {
			PrintWriter out = response.getWriter();
			// 响应JSON字符串
			out.write(json);
			// 关闭流
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
