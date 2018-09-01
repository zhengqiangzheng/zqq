package com.shsxt.dao;

import com.shsxt.po.User;
import com.shsxt.util.JdbcUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.SQLException;

/**
 * Created by zq on 2018/8/15/015.
 */
public class UserDao {




    public User findUserByName(String uname) {
        QueryRunner queryRunner=new QueryRunner(JdbcUtil.getDataSource());
        String sql="select * from tb_user where uname=?";
        User user=null;
        try {
            user = queryRunner.query(sql, new BeanHandler<User>(User.class), uname);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public User findUserbyNick(String nick, Integer userId) {
        User user=null;
        String sql="select * from tb_user where nick=? and userId !=?";
        QueryRunner queryRunner = new QueryRunner(JdbcUtil.getDataSource());
        try {
            user = queryRunner.query(sql, new BeanHandler<User>(User.class), nick, userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;


    }
        //修改用户信息
    public int editUser(String nick, Integer userId, String mood, String head) {
        String sql="update tb_user set nick=?,mood=?,head=? where userId=?";
        QueryRunner runner = new QueryRunner(JdbcUtil.getDataSource());
        int row=0;
        try {
           row = runner.update(sql, nick,mood,head,userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return row;
    }
}
