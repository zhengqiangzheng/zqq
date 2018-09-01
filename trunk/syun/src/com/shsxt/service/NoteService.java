package com.shsxt.service;

import com.shsxt.dao.NoteDao;
import com.shsxt.po.Note;
import com.shsxt.po.vo.NoteVo;
import com.shsxt.po.vo.ResultInfo;
import com.shsxt.util.Page;
import com.shsxt.util.StringUtil;

import java.util.List;

/**
 * Created by zq on 2018/8/21/021 9:10
 */
public class NoteService {
    private NoteDao noteDao = new NoteDao();

    public ResultInfo<Note> editNote(String typeId, String title, String content,String noteId) {
        ResultInfo<Note> resultInfo = new ResultInfo<>();
        if (StringUtil.isEmpty(typeId)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("类型名不能为空");
            return resultInfo;
        } if (StringUtil.isEmpty(title)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("标题不能为空");
            return resultInfo;
        }if (StringUtil.isEmpty(content)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("内容不能为空");
            return resultInfo;
        }

    int row=noteDao.editNote(typeId, title,  content,noteId);
        if (row>0) {
            resultInfo.setCode(1);
            resultInfo.setMsg("操作成功");

        }else {
            Note note = new Note();
            note.setContent(content);
            note.setTitle(title);
            note.setTypeId(Integer.parseInt(typeId));
            resultInfo.setCode(0);
            resultInfo.setMsg("操作失败");
            resultInfo.setResult(note);

        }
        return resultInfo;
    }

    public ResultInfo<Page<Note>> noteList(Integer userId, String pageNumstr, String pageSizestr,String dateStr,String typeId,String title) {
        ResultInfo<Page<Note>> pageResultInfo = new ResultInfo<>();
        Integer pageNum=1;
        Integer pageSize=2;
        if (StringUtil.isNotEmpty(pageNumstr)) {
            pageNum = Integer.parseInt(pageNumstr);
        }  if (StringUtil.isNotEmpty(pageSizestr)) {
            pageSize = Integer.parseInt(pageNumstr);
        }
        //通过用户id查询他的云记总数
        long total = noteDao.noteList(userId,dateStr,typeId,title);
        if (total == 0) {
            pageResultInfo.setCode(0);
            pageResultInfo.setMsg("暂未查到云记");
            return pageResultInfo;
        }//查到了云记
        int totalCount=(int)total;
        Page<Note> page=new Page<Note>(pageNum,pageSize,totalCount);
        Integer index = (pageNum - 1) * pageSize;//开始查询的下标
        List<Note>list=noteDao.findNoteListByPage(userId,index,pageSize,dateStr,typeId,title);

        page.setDatas(list);
        pageResultInfo.setCode(1);
        pageResultInfo.setResult(page);
        return pageResultInfo;



    }

    public ResultInfo<Note> findNoteById(String noteId) {
        ResultInfo<Note> resultInfo = new ResultInfo<>();
        if (StringUtil.isEmpty(noteId)) {
            resultInfo.setCode(1);
            resultInfo.setMsg("参数错误");
            return resultInfo;
        }
        Note note=noteDao.findNoteByNoteId(noteId);
        if (note==null) {
            resultInfo.setCode(0);
            resultInfo.setMsg("数据异常！");
            return resultInfo;
        }//成功
        resultInfo.setResult(note);
        resultInfo.setCode(1);
        return resultInfo;
    }

    public ResultInfo<Note> deleteNote(String noteId) {
        ResultInfo<Note> resultInfo = new ResultInfo<>();
        if (StringUtil.isEmpty(noteId)) {
            resultInfo.setMsg("参数错误");
            resultInfo.setCode(0);
            return resultInfo;
        }
        int row = noteDao.deleteNote(noteId);
        if (row>0) {
            resultInfo.setCode(1);
            resultInfo.setMsg("删除成功");
        }else {
            resultInfo.setCode(0);
            resultInfo.setMsg("删除失败");

        }
        return resultInfo;
    }

    public List<NoteVo> findNoteCountByGroup(Integer userId) {
        List<NoteVo>list=noteDao. findNoteCountByGroup(userId);
        return list;
    }

    public List<NoteVo> findNoteCountByTypeGroup(Integer userId) {
        List<NoteVo>list=noteDao.findNoteCountByTypeGroup(userId);
        return list;
    }
}
