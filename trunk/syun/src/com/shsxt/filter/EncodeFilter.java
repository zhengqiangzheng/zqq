package com.shsxt.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;


/**
 * 处理字符乱码的过滤器
 * 
 * 		Tomcat8及以上  
 * 			GET请求     不需要处理
 * 			POST请求	需要处理
 * 					设置接收数据的编码	request.setCharacterEncoding("UTF-8");
 * 					设置响应数据的编码	response.setContentType("text/html;charset=UTF-8");
 * 
 * 		Tomcat7及以下
 * 			GET请求	需要处理
 * 					设置接收参数的编码	new String(request.getParameter("name").getBytes("ISO-8859-1"),"UTF-8");
 * 					设置响应数据的编码	response.setContentType("text/html;charset=UTF-8");
 * 			POST请求	需要处理
 * 					设置接收数据的编码	request.setCharacterEncoding("UTF-8");
 * 					设置响应数据的编码	response.setContentType("text/html;charset=UTF-8");
 * 
 * @author Lisa Li
 *
 */

@WebFilter("/*")
public class EncodeFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("init...");
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		
		// 基于HTTP
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse)resp;
		
		// 设置接收和响应的字符编码
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		// 得到请求方式
		String  method = request.getMethod();  // get  GET  post POST
		
		// 判断是否是GET请求
		if ("GET".equals(method.toUpperCase())) { // GET请求
			// 得到服务器的版本   Apache Tomcat/8.0.45
			String serverInfo = request.getServletContext().getServerInfo();
			System.out.println(serverInfo);
			// 得到服务器的版本号
			String versionStr = serverInfo.substring(serverInfo.indexOf("/")+1, serverInfo.indexOf("/")+2);
			// System.out.println(versionStr);
			Integer version = Integer.parseInt(versionStr);
			// 如果Tomcat的版本是8以下，需要处理乱码
			if (version < 8) {
				// 重写getParameter()方法处理GET乱码
				MyWapper myWrapper = new MyWapper(request);
				// 放行
				chain.doFilter(myWrapper, response);
				return;
			}
			
		}
		
		
		// 放行 
		chain.doFilter(request, response);
		
	}
	
	
	/**
	 * 定义内部类，继承HttpServletRequest的包装类HttpServletRequestWapper，重写getParameter(String name)方法
	 * 	注意点：
	 * 		将request参数的作用范围提升
	 * @author Lisa Li
	 *
	 */
	class MyWapper extends HttpServletRequestWrapper{

		private HttpServletRequest request;
		public MyWapper(HttpServletRequest request) {
			super(request);
			this.request = request;
		}

		@Override
		public String getParameter(String name) {
			String value = "";
			try {
				// 解决get乱码
				value = new String(request.getParameter(name).getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return value;
		}
		
	}

	@Override
	public void destroy() {
		System.out.println("destory...");
		
	}

}
