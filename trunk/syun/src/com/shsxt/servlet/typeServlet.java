package com.shsxt.servlet;

import com.shsxt.po.NoteType;
import com.shsxt.po.User;
import com.shsxt.po.vo.ResultInfo;
import com.shsxt.service.TypeService;
import com.shsxt.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/type")
public class typeServlet extends HttpServlet {
    private TypeService typeService = new TypeService();
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String actionName = req.getParameter("actionName");
        //进入用户中心
        if ("typeCenter".equals(actionName)) {
            typeCenter(req, resp);
        } else if ("checkTypeName".equals(actionName)) {
            checkTypeName(req, resp);
        } else if ("adOrUpdate".equals(actionName)) {
            adOrUpdate(req, resp);
        } else if ("deleteType".equals(actionName)) {
            deleteType(req, resp);

        } else {
            typeCenter(req, resp);
        }
    }

    private void deleteType(HttpServletRequest req, HttpServletResponse resp) {
        String typeId = req.getParameter("typeId");
        System.out.println(typeId);
        ResultInfo<NoteType> resultInfo=typeService.deleteType(typeId);
        JsonUtil.toJson(resultInfo,resp);


    }

    /**添加修改操作*/
    private void adOrUpdate(HttpServletRequest req, HttpServletResponse resp) {
        String typeName = req.getParameter("typeName");
        String typeId = req.getParameter("typeId");
        User user = (User)req.getSession().getAttribute("user");
        Integer userId = user.getUserId();
        ResultInfo<NoteType> resultInfo = typeService.adOrUpdate(typeName, userId,typeId);
        System.out.println(typeId);
        JsonUtil.toJson(resultInfo,resp);


    }

    private void checkTypeName(HttpServletRequest req, HttpServletResponse resp) {
        User user = (User) req.getSession().getAttribute("user");
        Integer userId = user.getUserId();
        String typeName = req.getParameter("typeName");
        String typeId = req.getParameter("typeId");
        ResultInfo<NoteType> resultInfo = typeService.checkTypeName(typeName,userId,typeId);

//        req.setAttribute("resultInfo",resultInfo);//将从service得到的resultinfo存到作用域中
        JsonUtil.toJson(resultInfo,resp);



}
    private void typeCenter(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute("user");
        Integer userId = user.getUserId();      //得到userid查询他的信息
        ResultInfo<List<NoteType>> resultInfo = typeService.findTypeList(userId);
        req.setAttribute("resultInfo", resultInfo);//
        req.setAttribute("changePage","type/list.jsp");
        req.getRequestDispatcher("index.jsp").forward(req,resp);

    }
}
