package com.shsxt.servlet;

import com.shsxt.po.Note;
import com.shsxt.po.User;
import com.shsxt.po.vo.NoteVo;
import com.shsxt.po.vo.ResultInfo;
import com.shsxt.service.NoteService;
import com.shsxt.util.Page;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by zq on 2018/8/21/021 22:47
 */
@WebServlet("/index")
public class IndexServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //得到用户的行为,如searchDate,
        String actionName = req.getParameter("actionName");
        //将用户的行为存起来,对用户的日期等的分页有作用
        req.setAttribute("actionName",actionName);
        if ("searchDate".equals(actionName)) {
            String dateStr = req.getParameter("dateStr");
            req.setAttribute("dateStr", dateStr);
            noteList(req,resp,dateStr,null,null);

        }else if("searchType".equals(actionName)){
            String typeId = req.getParameter("typeId");
            req.setAttribute("typeId",typeId);

            noteList(req,resp,null,typeId,null);

        }else if ("searchKey".equals(actionName)){
            String title = req.getParameter("title");
            req.setAttribute("title",title);
            noteList(req,resp,null,null,title);




        }
        else {
            noteList(req, resp,null,null,null);
        }

        req.setAttribute("changePage","note/list.jsp");
       req.getRequestDispatcher("index.jsp").forward(req,resp);
    }

    private void noteList(HttpServletRequest req, HttpServletResponse resp, String dateStr,String typeId,String title) {
        String pageNum = req.getParameter("pageNum");
        String pageSize = req.getParameter("pageSize");

        User user = (User) req.getSession().getAttribute("user");
        Integer userId = user.getUserId();
        ResultInfo<Page<Note>> pageResultInfo = new NoteService().noteList(userId, pageNum, pageSize,dateStr,typeId,title);
        req.setAttribute("pageInfo",pageResultInfo);
        //从数据库传过来的数据
        List<NoteVo> dateInfo = new NoteService().findNoteCountByGroup(userId);
        req.getSession().setAttribute("dateInfo",dateInfo);
        List<NoteVo>typeInfo=new NoteService().findNoteCountByTypeGroup(userId);
        req.getSession().setAttribute("typeInfo",typeInfo);

    }

}
