package com.shsxt.servlet;

import com.shsxt.po.User;
import com.shsxt.po.vo.ResultInfo;
import com.shsxt.service.UserService;
import com.shsxt.util.JsonUtil;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * Created by zq on 2018/8/15/015.
 */

@WebServlet("/user")
@MultipartConfig
public class LoginServlet extends HttpServlet {
    private UserService userService = new UserService();
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        req.setCharacterEncoding("utf-8");
//        resp.setContentType("text/html;charset=utf-8");

        String actionName = req.getParameter("actionName");
        if ("login".equals(actionName)) {
          ResultInfo resultInfo=  userLogin(req, resp);
            JsonUtil.toJson(resultInfo,resp);

        }
       //退出登录
       else if ("logout".equals(actionName)) {
          userLoginout(req,resp);
        }

        //过滤器传过来的autologin
        else if ("autoLogin".equals(actionName)) {
            ResultInfo result = userLogin(req, resp);
            if (result.getCode() == 1) {
                resp.sendRedirect("index");
            } else {
                resp.sendRedirect("login.jsp");
            }

        }
        else if ("userCenter".equals(actionName)) {
            userCenter(req, resp);
        } //用户图像
        else if ("userHead".equals(actionName)) {
            userHead(req, resp);

        }
        //验证昵称是否可用
        else  if ("checkNick".equals(actionName)){
            userNick(req, resp);

        }
        //用户信息修改
        else if ("updateUser".equals(actionName)) {
            userEdit(req, resp);
        }


    }



    private void userEdit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //将request对象传过去,进行文件的上传
        ResultInfo<User> resultInfo=userService.userEdit(req);
       //
        req.setAttribute("resultInfo",resultInfo);
        if (resultInfo.getCode()==1) {
            //上传成功,更换该用户session的值
            resultInfo.setMsg("修改成功");
            req.getSession().setAttribute("user",resultInfo.getResult());

        }//上传失败

        req.setAttribute("changePage","user/info.jsp");
        req.getRequestDispatcher("index.jsp").forward(req,resp);



    }

    private void userNick(HttpServletRequest req, HttpServletResponse resp) {
        //接受参数,调用service方法,返回resultinfo对象
        String nick = req.getParameter("nick");
        //得到session,得到user对象,得到userID 然后在dao层中把不查自己
       User user =(User) req.getSession().getAttribute("user");//转为user对象
        //从service层中得到resultinfo对象,然后使用jsonutil方法反回前台
        Integer userId = user.getUserId();

        ResultInfo rsesultInfo=userService.checkNick(nick,userId);
        JsonUtil.toJson(rsesultInfo,resp);//将得到的result对象传到前台给回调函数验证


    }

    private void userHead(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //导入jar包,将得到的图片的地址加载,避免使用流的方式拷贝图片
        String path = req.getParameter("fn");//src="user?actionName=userhead&fn=${user.head},fn是图片的名字
        String realPath = req.getServletContext().getRealPath("/WEB-INF/upload/");
        System.out.println(realPath);
        String filePath = realPath+"\\"+path;
        System.out.println(filePath);
        File file = new File(filePath);//得到该路径下的文件的对象
        //判断文件是否存在
        if (file.isFile()&&file.exists()) {
            String substring = path.substring(path.indexOf(".")+1, path.length());//截取文件名的后缀
            if ("png".equalsIgnoreCase(substring)) {
                resp.setContentType("image/png;charset=UTF-8");
            } else if ("jpg".equalsIgnoreCase(substring) || "jpeg".equalsIgnoreCase(substring)) {
                resp.setContentType("image/jpeg;charset=UTF-8");
            } else if ("gif".equalsIgnoreCase(substring)) {
                resp.setContentType("image/gif;charset=UTF-8");
            }
            // 响应图片(利用commons-io的jar包)
            FileUtils.copyFile(file, resp.getOutputStream());

        }



    }

    private void userCenter(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置动态页面显示的值
        req.setAttribute("changePage", "user/info.jsp");//req中有changge,下面用jstl表达式跳转

        req.getRequestDispatcher("index.jsp").forward(req, resp);

    }

    //用户注销
    private void userLoginout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate();//session销毁
        req.getRequestDispatcher("login.jsp").forward(req,resp);
        Cookie cookie = new Cookie("user", null);//销毁user对象
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
        resp.sendRedirect("login.jsp");

    }
//用户登录模块
    private ResultInfo userLogin(HttpServletRequest req, HttpServletResponse resp) {
        //servlet层一般接受参数,在做简单的页面跳转

        String uname = req.getParameter("uname");//用户名
        String upwd = req.getParameter("upwd");//密码
        String rem = req.getParameter("rem");//是否选中
        ResultInfo <User>result= userService.userLogin(uname,upwd);//调用用户登录方法,将从前台闯过来的给他
        System.out.println(result);
        if (result.getCode()==1){               //登录成功
            if ("1".equals(rem)) {              //记住我点了√
                String   value=uname+"-"+upwd;  //将用户名密码存到cookie中
                Cookie cookie=new Cookie("user",value);
                cookie.setMaxAge(3*24*60*60);//三天
                 resp.addCookie(cookie);
            }
            req.getSession().setAttribute("user",result.getResult());//将user对象存入session中


            System.out.println();
        }
//        JsonUtil.toJson(result,resp);
        return result;

                }
                }
