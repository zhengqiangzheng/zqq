package com.shsxt.dao;

import com.shsxt.util.DBUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 基础的数据库访问层
 * 1、更新操作（添加操作、修改操作、删除操作）
 * 2、查询某一个字段（查询总数量）
 * 3、查询指定JavaBean的列表
 * 4、查询指定JavaBean的对象
 * @author Lisa Li
 *
 */
public class BaseDao {

	/**
	 * 更新操作（添加操作、修改操作、删除操作）
	 * @param sql
	 * @param params
	 * @return
	 */
	public int excuteUpdate(String sql, List<Object> params) {
		int row = 0;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			// 得到数据库连接
			connection = DBUtil.getConnection();
			// 设置事务不自动提交
			connection.setAutoCommit(false);
			// 预编译
			preparedStatement = connection.prepareStatement(sql);
			// 设置参数
			// 判断参数集合是否为空
			if (params != null && params.size() > 0) {
				for(int i = 0; i < params.size(); i++) {
					preparedStatement.setObject(i+1, params.get(i));
				}
			}
			// 执行更新，返回受影响的行数
			row = preparedStatement.executeUpdate();
			
			if (row >0) {
				connection.commit();
			} else {
				connection.rollback();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				
			}
		} finally {
			// 关闭资源
			DBUtil.close(null, preparedStatement, connection);
		}
		return row;
	}
	
	/**
	 * 查询某一个字段（查询总数量）
	 * @param sql
	 * @param params
	 * @return
	 */
	public Object findSingleValue(String sql, List<Object> params) {
		Object object = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			// 得到数据库连接
			connection = DBUtil.getConnection();
			// 预编译
			preparedStatement = connection.prepareStatement(sql);
			// 设置参数
			// 判断参数集合是否为空
			if (params != null && params.size() > 0) {
				for(int i = 0; i < params.size(); i++) {
					preparedStatement.setObject(i+1, params.get(i));
				}
			}
			// 执行查询，返回结果集
			resultSet = preparedStatement.executeQuery();
			// 分析结果集，得到指定字段
			if(resultSet.next()) {
				object = resultSet.getObject(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			DBUtil.close(resultSet, preparedStatement, connection);
		}
		
		return object;
	}
	
	
	/**
	 * 查询指定java对象的列表
	 * @param sql
	 * @param params
	 * @param cls
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List queryRows(String sql, List<Object> params, Class cls) {
		List list = new ArrayList<>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			// 得到数据库连接
			connection = DBUtil.getConnection();
			// 预编译
			preparedStatement = connection.prepareStatement(sql);
			// 设置参数
			// 判断参数集合是否为空
			if (params != null && params.size() > 0) {
				for(int i = 0; i < params.size(); i++) {
					preparedStatement.setObject(i+1, params.get(i));
				}
			}
			// 执行查询，返回结果集
			resultSet = preparedStatement.executeQuery();
			
			
			// 得到结果集的元数据  得到查询到的字段名称和查询到的字段数量
			ResultSetMetaData metaData = resultSet.getMetaData();
			// 得到查询的字段的数量
			int fieldNum = metaData.getColumnCount();
			
			// 分析结果集
			while(resultSet.next()) {
				// 得到object对象
				Object object = cls.newInstance();
				
				for(int i = 0; i < fieldNum; i++) {
					// 得到数据库查询到的字段名
					String columName = metaData.getColumnLabel(i+1);
					// 字段名通过反射得到field对象
					Field field = cls.getDeclaredField(columName);
					// 设置set方法
					String setMethod = "set" + columName.substring(0, 1).toUpperCase() + columName.substring(1);
					// 通过反射得到Method
					Method method = cls.getDeclaredMethod(setMethod, field.getType());
					// 调用方法
					method.invoke(object, resultSet.getObject(columName));
				}
				
				// 将object对象加到集合中
				list.add(object);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			DBUtil.close(resultSet, preparedStatement, connection);
		}
		
		return list;
	}
	
	
	/**
	 * 查询指定java对象
	 * @param sql
	 * @param params
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Object queryRow(String sql, List<Object> params, Class cls) {
		List list = queryRows(sql, params, cls);
		Object object = null;
		if (list != null && list.size() > 0) {
			 object = list.get(0);
		}
		return object;
	}
	
}
