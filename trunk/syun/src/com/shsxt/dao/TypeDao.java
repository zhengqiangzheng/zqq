package com.shsxt.dao;

import com.shsxt.po.NoteType;
import com.shsxt.util.JdbcUtil;
import com.shsxt.util.StringUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * @author zq
 */
public class TypeDao {
    public List<NoteType> findListbyUserId(Integer userId) {
        String sql="select * from tb_note_type where userId=?";
        List<NoteType> typeList=null;
        QueryRunner queryRunner = new QueryRunner(JdbcUtil.getDataSource());
        try {
          typeList  = queryRunner.query(sql, new BeanListHandler<NoteType>(NoteType.class), userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return typeList;
    }
/**
 *   查询要添加的类型名是否存在
 */

    public NoteType checkTypeName(String typeName, Integer userId,String typeId)  {
        String sql = "select * from tb_note_type where typeName = ? and userId = ?";
        QueryRunner queryRunner = new QueryRunner(JdbcUtil.getDataSource());
        NoteType noteType = null;
        if (StringUtil.isNotEmpty(typeId)) {
            try {
                sql += "  and typeId != ?";
                noteType = queryRunner.query(sql, new BeanHandler<NoteType>(NoteType.class), typeName, userId, typeId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {
            try {
                noteType = queryRunner.query(sql, new BeanHandler<NoteType>(NoteType.class), typeName, userId);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return noteType;

    }

    public int adOrUpdate(String typeName, Integer userId ,String typeId) {
        int row = 0;
        String sql ="";
        if (StringUtil.isEmpty(typeId)) {
            try {
                QueryRunner queryRunner = new QueryRunner(JdbcUtil.getDataSource());
                sql="insert into tb_note_type (typeName,userId) values (?,?)";
                row = queryRunner.update(sql,typeName, userId);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {
            QueryRunner queryRunner1 = new QueryRunner(JdbcUtil.getDataSource());
            sql = "update tb_note_type set typeName = ? where typeId = ?";
            try {
                row = queryRunner1.update(sql,typeName, typeId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return row;

    }

    public long findNoteByTypeId(String typeId){
        String sql ="select count(1) from tb_note where typeId = ?";
        QueryRunner queryRunner = new QueryRunner(JdbcUtil.getDataSource());
        long noteCount=0;
        try {
            noteCount = (long)queryRunner.query(sql,new ScalarHandler(),typeId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return noteCount;
    }

    public int deleteType(String typeId) {
        int row = 0;
        String sql ="delete from tb_note_type where typeId=?";
        QueryRunner queryRunner = new QueryRunner(JdbcUtil.getDataSource());
        try {
             row = queryRunner.update(sql,typeId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }
}
