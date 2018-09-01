package com.shsxt.service;

import com.shsxt.dao.TypeDao;
import com.shsxt.po.NoteType;
import com.shsxt.po.vo.ResultInfo;
import com.shsxt.util.StringUtil;

import javax.lang.model.type.NoType;
import java.util.List;

public class TypeService {

    TypeDao typeDao = new TypeDao();
//service层从dao层插数据
    public ResultInfo<List<NoteType>> findTypeList(Integer userId) {
        ResultInfo<List<NoteType>> resultInfo = new ResultInfo<>();
        List<NoteType> typeList = typeDao.findListbyUserId(userId);
        //查到数据存集合
        if (typeList!=null&&typeList.size()>0) {
            resultInfo.setCode(1);
            resultInfo.setResult(typeList);
        } else  {
            resultInfo.setCode(0);
            resultInfo.setMsg("暂未查询到类型数据！");
        }
        return resultInfo;
    }

    public ResultInfo<NoteType> checkTypeName(String typeName, Integer userId,String typeId) {
        ResultInfo<NoteType> resultInfo = new ResultInfo<>();
        if (StringUtil.isEmpty(typeName)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("类型名不能为空");
            return resultInfo;
        }NoteType noteType = typeDao.checkTypeName(typeName, userId,typeId);
            if (noteType!=null) {
                resultInfo.setCode(0);
                resultInfo.setMsg("类型名已存在");
            }else {
                resultInfo.setCode(1);
            }
        return resultInfo;
    }

    public ResultInfo<NoteType> adOrUpdate(String typeName, Integer userId,String typeId) {
        ResultInfo<NoteType> resultInfo = new ResultInfo<>();
        if (StringUtil.isEmpty(typeName)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("类型名不可为空");
            return resultInfo;
        }
        //  2、调用Dao层，验证类型名的唯一性
        NoteType noteType = typeDao.checkTypeName(typeName, userId, typeId);
        if (noteType != null) {
            resultInfo.setCode(0);
            resultInfo.setMsg("类型名称已存在，不可使用！");
            return resultInfo;
        }

        //  3、调用Dao层的更新方法，返回受影响的行数
        int row = typeDao.adOrUpdate(typeName, userId, typeId);

        //  4、判断是否更新成功
        if (row  > 0) {
            // 成功，code=1
            resultInfo.setCode(1);
            // 调用dao层，通过类型名和用户ID查询类型对象
            NoteType type = typeDao.checkTypeName(typeName, userId,typeId);
            // 将类型对象设置resultInfo的result中
            resultInfo.setResult(type);
        } else {
            // 失败，code=0错误信息
            resultInfo.setCode(0);
            resultInfo.setMsg("更新失败！");
        }

        //  5、返回resultInfo对象
        return resultInfo;
            }


    public ResultInfo deleteType(String typeId) {
        ResultInfo<NoType> resultInfo = new ResultInfo<>();
        if (StringUtil.isEmpty(typeId)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("OPPS!sth is Wrong");return resultInfo;
        }
        //noteCount大于0 就说明有记录,不能删除
        long noteCount = typeDao.findNoteByTypeId(typeId);
        if (noteCount>0) {
            resultInfo.setCode(0);
            resultInfo.setMsg("存在笔记,不能删除");
            return resultInfo;
        }
        int row = typeDao.deleteType(typeId);
        if (row > 0) {
            resultInfo.setCode(1);
            resultInfo.setMsg("删除成功");
        }else {
            resultInfo.setCode(0);
            resultInfo.setMsg("删除失败");

        }
        return resultInfo;



    }
}