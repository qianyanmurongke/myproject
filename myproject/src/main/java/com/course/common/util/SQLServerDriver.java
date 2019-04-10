package com.course.common.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLServerDriver {
	public static Connection  getConnection(){
		Connection conn = null;
		try {
			String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			String dbURL = "jdbc:sqlserver://192.168.1.147;DatabaseName=ZYTK35_new";
			String userName = "sa"; 
			String userPwd = "123456";
			Class.forName(driverName);
			conn = DriverManager.getConnection(dbURL, userName, userPwd);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return conn;
	}
}
