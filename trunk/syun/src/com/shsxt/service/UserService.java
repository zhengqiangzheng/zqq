package com.shsxt.service;

import com.shsxt.dao.UserDao;
import com.shsxt.po.User;
import com.shsxt.po.vo.ResultInfo;
import com.shsxt.util.MD5Util;
import com.shsxt.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.UUID;


/**
 * Created by zq on 2018/8/15/015.
 */
public class UserService {

private UserDao userDao=new UserDao();

    public  ResultInfo<User> userLogin(String uname, String upwd) {
        //service做逻辑判断
        ResultInfo<User>resultInfo=new ResultInfo<>();

        if (StringUtil.isEmpty(uname)||StringUtil.isEmpty(upwd)){
            resultInfo.setCode(0);
            resultInfo.setMsg("用户名密码不能为空");
            return resultInfo;
        }

        User userByName = userDao.findUserByName(uname);
        if (userByName==null){
            resultInfo.setMsg("用户名不存在");
            resultInfo.setCode(0);
            return resultInfo;
        }
        upwd= MD5Util.encode(MD5Util.encode(upwd));  //数据库将密码加密了两次
        if(!upwd.equals(userByName.getUpwd())){ //密码错误
            resultInfo.setCode(0);
            resultInfo.setMsg("密码错误");
            return resultInfo;
        }
        resultInfo.setCode(1);
        resultInfo.setResult(userByName);
        return resultInfo;



    }

    public ResultInfo checkNick(String nick, Integer userId) {
        ResultInfo<User> resultInfo = new ResultInfo();
        //判断参数,userID不用判断因为是重session作用域传过来的
        if (StringUtil.isEmpty(nick)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("昵称不存在");
            return resultInfo;
        }
        //昵称不为空,从数据库中判断昵称是否存在
        //用userid为判断条件,nick为查询条件从dao层获得一个user对象
     User user=userDao.findUserbyNick(nick, userId);
        if (user != null) {
        resultInfo.setCode(0);       //查到了有这个昵称的用户说明昵称不可用
        resultInfo.setMsg("昵称已存在");
        return resultInfo;
        }//昵称为空昵称不存在就可用
        resultInfo.setCode(1);

        return resultInfo;
    }

    public ResultInfo<User> userEdit(HttpServletRequest req)  {
        ResultInfo<User> resultInfo = new ResultInfo<>();
        String nick = req.getParameter("nick");
        String mood = req.getParameter("mood");
        User user1 = (User)req.getSession().getAttribute("user");
        Integer userId = user1.getUserId();//得到用户id



        //昵称不能为空
        if (StringUtil.isEmpty(nick)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("昵称不能为空");
            return resultInfo;
        }//昵称不为空后到数据库中判断昵称是否可用
        User user=userDao.findUserbyNick(nick,userId);
        if (user!=null){
            resultInfo.setCode(0);
            resultInfo.setMsg("昵称不可用");
            return resultInfo;
        }
        //昵称可用了我们就可以上传头像了
        String head = user1.getHead();//得到头像的名,如果没有上传就用原来的
        //判断头像
        Part part = null;//得到part对象
        try {
            part = req.getPart("img");
            String submittedFileName = part.getSubmittedFileName();//得到文件名
            head=submittedFileName;
            String path = req.getServletContext().getRealPath("/WEB-INF/upload/" + submittedFileName);
            part.write(path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }

        int row = userDao.editUser(nick, userId, mood, head);
        if (row>0) { //修改成功
            resultInfo.setCode(1);
            user1.setHead(head);
            user1.setMood(mood);
            user1.setNick(nick);//覆盖之前从session作用域中传来的user对象
            resultInfo.setResult(user1);
            resultInfo.setMsg("修改成功");
        }else {
            resultInfo.setCode(0);
            resultInfo.setMsg("修改失败");

        }


        return resultInfo;
    }
    private String getFileName(String header) {
        String suffix = header.substring(header.lastIndexOf("."), header.length() - 1);
        String fileName = UUID.randomUUID().toString().replace("-", "");
        return fileName + suffix;
    }
}
