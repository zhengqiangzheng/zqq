package com.shsxt.dao;

import com.shsxt.po.Note;
import com.shsxt.po.vo.NoteVo;
import com.shsxt.util.JdbcUtil;
import com.shsxt.util.StringUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by zq on 2018/8/21/021 9:10
 */
public class NoteDao {
    QueryRunner queryRunner = new QueryRunner(JdbcUtil.getDataSource());

    public int editNote(String typeId, String title, String content,String noteId) {
        int row = 0;
        if (StringUtil.isNotEmpty(noteId)) {
            String sql = "update tb_note set typeId=?,title=?,content=? where noteId=?";
            try {
                row = queryRunner.update(sql, typeId, title,content, noteId);
            } catch (SQLException e) {
                e.printStackTrace();
            }


        } else  {
            String sql="insert into tb_note(typeId,title,content,pubTime) values(?,?,?,now())";
            try {
                row = queryRunner.update(sql, typeId, title, content);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return row;
    }
    //根据用户id查询该用户的云记数目
    public long noteList(Integer userId,String dateStr,String typeId,String title) {
        String sql = "select count(1) from tb_note n inner join tb_note_type t on n.typeId=t.typeId where userId=?";
        long total=0;
        if (StringUtil.isEmpty(dateStr)){
            try {
                total =(Long)queryRunner.query(sql,new ScalarHandler(),userId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (StringUtil.isNotEmpty(typeId)) {
            sql+="and n.typeId =?";
            try {
                total=(long)queryRunner.query(sql,new ScalarHandler(),userId,typeId);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        if (StringUtil.isNotEmpty(dateStr)){
            sql += " and DATE_FORMAT(pubTime,'%Y年%m月') = ? ";
            try {
                total = (long) queryRunner.query(sql, new ScalarHandler(), userId, dateStr);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        if (StringUtil.isNotEmpty(title)){
            sql+="and title like concat('%',?,'%')";
            try {
                total = (long) queryRunner.query(sql, new ScalarHandler(), userId, title);
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
            /*sql += " and DATE_FORMAT(pubTime,'%Y年%m月') = ? ";
            try {
                total=(long)queryRunner.query(sql, new ScalarHandler(), userId, dateStr);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return total;*/
        return total;

    }

    //分页查询
    public List<Note> findNoteListByPage(Integer userId, Integer index, Integer pageSize,String dateStr,String typeId,String title) {
        String sql = "select noteId,title,pubTime from tb_note n inner join  tb_note_type t on n.typeId=t.typeId where userId=? ";
        List<Note> List = null;

        if (StringUtil.isNotEmpty(dateStr)){
            sql+="and DATE_FORMAT(pubTime,'%Y年%m月') = ? limit ?,? ";
            try {
                List = queryRunner.query(sql, new BeanListHandler<Note>(Note.class),userId,dateStr, index, pageSize);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }if (StringUtil.isNotEmpty(typeId)){
            sql+="and n.typeId=? limit ?,?";
            try {
                List = queryRunner.query(sql, new BeanListHandler<Note>(Note.class),userId,typeId, index, pageSize);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }if (StringUtil.isNotEmpty(title)){
            try {
                sql+="and title like concat('%',?,'%') limit ?,? ";
                System.out.println(sql);
                List = queryRunner.query(sql, new BeanListHandler<Note>(Note.class),userId,title,index, pageSize);
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
        else {

                try {
                    sql+="limit ?,?";
                    List = queryRunner.query(sql, new BeanListHandler<Note>(Note.class),userId, index, pageSize);
                } catch (SQLException e) {
                    e.printStackTrace();
                }


        }

        return List;
    }

    public Note findNoteByNoteId(String noteId) {
        String sql = "select noteId,title,pubTime,n.typeId as typeId,typeName,content from tb_note n inner join tb_note_type t on n.typeId = t.typeId where noteId =?";
        Note note=null;
        try {
            note = queryRunner.query(sql, new BeanHandler<Note>(Note.class),noteId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return note;


    }

    public int deleteNote(String noteId) {
        String sql = "delete  from tb_note where noteId=?";
        int row = 0;
        try {
         row=   queryRunner.update(sql, noteId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  row;
    }

    public List<NoteVo> findNoteCountByGroup(Integer userId) {
        String sql="select count(noteId) AS noteCount,DATE_FORMAT(pubTime ,'%Y年%m月') as groupName from tb_note n inner join tb_note_type t on n.typeId=t.typeId where userId=? GROUP BY DATE_FORMAT(pubTime ,'%Y年%m月');   ";
        List<NoteVo> list=null;
        try {
        list = queryRunner.query(sql, new BeanListHandler<NoteVo>(NoteVo.class), userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<NoteVo> findNoteCountByTypeGroup(Integer userId) {
        String sql="SELECT  t.typeId as typeId ,typeName as groupName,COUNT(n.noteId)AS noteCount  from tb_note_type t LEFT JOIN tb_note n ON t.typeId =n.typeId WHERE userId=? GROUP BY t.typeId";
        List<NoteVo> list=null;
        try {
            list=queryRunner.query(sql, new BeanListHandler<NoteVo>(NoteVo.class),userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;

    }
}
