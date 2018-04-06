/*
 * Copyright notice
 */
package io.github.jeesk.myutil;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSourceFactory;

/**
 * DBUtils.java
 * @version
 * 2018年3月31日 下午12:42:47
 * @author jeesk
 * @since 1.0
 */
public enum DBUtils {

	INSTANCE;
	private static DataSource dataSource = null;
	static {

		Properties p = new Properties();
		try {
			// 这儿使用的是dbcp连接池
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("dbcp.properties");
			p.load(in);
			dataSource = BasicDataSourceFactory.createDataSource(p);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static Connection getConnection() {

		try {
			return dataSource.getConnection();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Statement createStmt(Connection conn) {

		try {
			return conn.createStatement();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return null;
	}

	public static PreparedStatement prepareStmt(Connection conn, String sql) {

		try {
			return conn.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static ResultSet executeQuery(String sql, Statement stmt) {

		try {

			return stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	

	public static void close(Connection conn, Statement stmt, ResultSet rs) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {

				e.printStackTrace();
			} finally {
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {

						if (rs != null) {
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
