/*
 * Copyright notice
 */
package io.github.jeesk.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DBUtils.java
 * @version
 * 2018年3月31日 下午12:42:47
 * @author jeesk
 * @since 1.0
 */
public enum DBUtils {

	INSTANCE;
	static {

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}

	}

	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbs", "root", "admin");
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return conn;
	}

	public static Statement createStmt(Connection conn) {

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return stmt;
	}
	public static PreparedStatement prepareStmt(Connection conn,String sql) {
		
		PreparedStatement prepareStatement=null;
		try {
			prepareStatement = conn.prepareStatement(sql);
			return prepareStatement;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return prepareStatement;
		
		
	}

	public static ResultSet executeQuery(String sql, Statement stmt) {

		ResultSet rs = null;
		try {
			rs =	stmt.executeQuery(sql);
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	

	
	public static void close(Connection conn,Statement stmt,ResultSet rs) {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}finally {
				if(stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}finally {
						
						if(rs!=null) {
							try {
								rs.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		
		
	}

}
