package com.shsxt.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.shsxt.po.User;
import com.shsxt.util.StringUtil;


/**
 * 非法访问拦截
 * 	1、静态资源 放行
 * 	2、指定页面 放行
 * 	3、指定行为 放行
 * 	4、登录状态 放行
 * 	5、判断cookie是否有值，如果有自动登录
 */
@WebFilter("/*")
public class syFilter implements Filter {

    public syFilter() {

    }

    public void destroy() {

    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        // 基于HTTP
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        // 得到请求的路径
        String path = request.getRequestURI();  // 端口号之后的路径（到？结束）

        // 1、静态资源 放行
        if (path.contains("/statics")) {
            chain.doFilter(request, response);
            return;
        }


        // 2、指定页面 放行 （不需要登录状态就可以访问的页面直接放行 login.jsp、register.jsp）
        if (path.contains("/login.jsp")) {
            chain.doFilter(request, response);
            return;
        }

        // 3、指定行为 放行 （登录操作）
        String actionName = request.getParameter("actionName");
        if ("login".equals(actionName)) {
            chain.doFilter(request, response);
            return;
        }

        // 4、登录状态 放行 （session作用域中的user对象不为空时为登录状态）
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        // 判断user对象是否为空
        if (user != null) {
            chain.doFilter(request, response);
            return;
        }


        // 5、判断cookie是否有值，如果有自动登录
        Cookie[] cookies = request.getCookies();
        // 判断cookie数组是否为空
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if ("user".equals(name)) { // 登录成功后记住密码存的
                    String  value  = cookie.getValue(); // shsxt-123456
                    if (StringUtil.isNotEmpty(value)) {
                        // 得到用户名和密码
                        String uname = value.split("-")[0];
                        String upwd = value.split("-")[1];
                        // 调用登录方法
                        request.getRequestDispatcher("user?actionName=autoLogin&uname="+ uname + "&upwd=" + upwd).forward(request, response);
                        // 不能放行已经是在这个页面上了再放就是继续重复等会报错

                        return;
                    }
                }
            }
        }







        // 如果user对象为空，拦截请求跳转到登录页面
        response.sendRedirect("login.jsp");

    }

    public void init(FilterConfig fConfig) throws ServletException {

    }

}
