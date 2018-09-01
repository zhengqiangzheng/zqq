package com.shsxt.servlet;

import com.shsxt.po.Note;
import com.shsxt.po.NoteType;
import com.shsxt.po.User;
import com.shsxt.po.vo.ResultInfo;
import com.shsxt.service.NoteService;
import com.shsxt.service.TypeService;
import com.shsxt.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/note")

/**
 * Created by zq on 2018/8/21/021 9:09
 */
public class NoteServlet extends HttpServlet {
        private NoteService noteService = new NoteService();
        @Override
        protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String actionName = req.getParameter("actionName");
            if ("view".equals(actionName)) {
                viewNote(req, resp);

            } else if ("edit".equals(actionName)) {
                editNote(req, resp);

            } else if ("detail".equals(actionName)) {
                detailView(req, resp);
            } else if ("delete".equals(actionName)) {
                deleteNote(req, resp);
            }

    }

    private void deleteNote(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String noteId = req.getParameter("noteId");
     ResultInfo<Note> resultInfo=   noteService.deleteNote(noteId);
     //删除成功,回到首页
     if (resultInfo.getCode()==1) {
         resp.sendRedirect("index");
        }else {
         req.setAttribute("resultInfo",resultInfo);
         req.getRequestDispatcher("note?actionName=detail&noteId=" + noteId);

     }


    }

    private void detailView(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            //接受参数
        String noteId = req.getParameter("noteId");

        ResultInfo<Note> resultInfo = noteService.findNoteById(noteId);
        req.setAttribute("resultInfo", resultInfo);
        req.setAttribute("changePage","note/detail.jsp");

        req.getRequestDispatcher("index.jsp").forward(req,resp);


    }

    private void editNote(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            //接受参数
        String typeId = req.getParameter("typeId");
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        String noteId = req.getParameter("noteId");
        ResultInfo<Note> resultInfo = noteService.editNote(typeId, title, content,noteId);
        if (resultInfo.getCode()==1) {
            resp.sendRedirect("index");
        } else {
            req.setAttribute("result",resultInfo);
            req.getRequestDispatcher("note?actionName=view").forward(req,resp);
        }

    }

    private void viewNote(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String noteId = req.getParameter("noteId");
        if (StringUtil.isNotEmpty(noteId)) {
            ResultInfo<Note> noteInfo = noteService.findNoteById(noteId);
            req.setAttribute("result",noteInfo);
        }

        User user = (User) req.getSession().getAttribute("user");
        Integer userId = user.getUserId();
        ResultInfo<List<NoteType>> resultInfo = new TypeService().findTypeList(userId);
        req.setAttribute("resultInfo",resultInfo);

        req.setAttribute("changePage","note/view.jsp");
        req.getRequestDispatcher("index.jsp").forward(req,resp);

    }
}
