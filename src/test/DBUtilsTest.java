/*
 * Copyright notice
 */
package test;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.crypto.AEADBadTagException;

import org.junit.jupiter.api.Test;

import io.github.jeesk.Article;
import io.github.jeesk.util.DBUtils;

/**
 * DBUtilsTest.java
 * @version
 * 2018年4月1日 上午10:16:16
 * @author jeesk
 * @since 1.0
 */
class DBUtilsTest {

	/**
	 * Test method for {@link io.github.jeesk.util.DBUtils#getConnection()}.
	 * @throws SQLException 
	 */
	@Test
	void testGetConnection() throws SQLException {
			Connection conn  = DBUtils.getConnection();
				String sql = " INSERT INTO article (pid,rootid,title,cont,pdate,isleaf)value(?,?,?,?,now(),?)";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setInt(1,20);
				ps.setInt(2, 2);
				ps.setString(3, "213");
				ps.setString(4, "2313");
				ps.setInt(5, 0);
				ps.executeUpdate();
				System.out.println("ok");
	}

	/**
	 * Test method for {@link io.github.jeesk.util.DBUtils#createStmt(java.sql.Connection)}.
	 */
	@Test
	void testCreateStmt() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link io.github.jeesk.util.DBUtils#executeQuery(java.lang.String, java.sql.Statement)}.
	 */
	@Test
	void testExecuteQuery() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link io.github.jeesk.util.DBUtils#close(java.sql.Connection, java.sql.Statement, java.sql.ResultSet)}.
	 */
	@Test
	void testClose() {
		
	}

}
