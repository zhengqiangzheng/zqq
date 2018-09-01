package com.shsxt.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;


public class DBUtil {

	/**
	 * 得到数据库连接
	 * @return
	 */
	public static Connection getConnection() {
		
		Connection connection = null;
		
		/*try {
			// 加载驱动
			Class.forName("com.mysql.jdbc.Driver");
			// 得到数据库连接
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		try {
			// 加载配置文件，把配置文件的内容加载到输入流中   路径不需要加"/"，因为路径在classpath下
			InputStream inputStream = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");
			// 得到属性文件对象
			Properties properties = new Properties();
			// 将输入流加载到属性文件对象中 ，通过properties对象的load()方法
			properties.load(inputStream);
			
			// 通过properties的getProperty()方法得到属性文件中的value
			// 得到驱动名
			String jdbcName = properties.getProperty("jdbcName");
			String url = properties.getProperty("url");
			String dbName = properties.getProperty("dbName");
			String dbPwd = properties.getProperty("dbPwd");
			
			// 加载驱动
			Class.forName(jdbcName);
			// 得到数据库连接
			connection = DriverManager.getConnection(url, dbName, dbPwd);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return connection;
		
	}
	
	/**
	 * 关闭资源
	 * @param resultSet
	 * @param preparedStatement
	 * @param connection
	 */
	public static void close(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection) {
		try {
			// 判断对象不为空，并关闭对象
			if (resultSet != null) {
				resultSet.close();
			}
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public static void main(String[] args) {
		Connection connection = DBUtil.getConnection();
		System.out.println(connection);
	}

}
