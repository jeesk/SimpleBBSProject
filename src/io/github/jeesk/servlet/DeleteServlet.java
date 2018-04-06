/*
 * Copyright notice
 */
package io.github.jeesk.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.github.jeesk.myutil.DBUtils;

/**
 * DeleteServlet.java
 * @version
 * 2018年4月5日 下午9:11:47
 * @author jeesk
 * @since 1.0
 */
@WebServlet("/delete")
public class DeleteServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String requestURL = req.getParameter("url");
		int id = Integer.parseInt(req.getParameter("id"));
		boolean isleaf = Boolean.parseBoolean(req.getParameter("isleaf"));
		int pid = Integer.parseInt(req.getParameter("pid"));
		PreparedStatement createStmt = null;
		ResultSet executeQuery = null;
		Connection conn = DBUtils.getConnection();
		try {
			boolean autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			delete(conn, id, isleaf);

			String sql3 = "select count(*) from article where pid = ?";
			
			createStmt = DBUtils.prepareStmt(conn, sql3);
			createStmt.setInt(1, pid);
			executeQuery = createStmt.executeQuery();
			executeQuery.next();
			int count = executeQuery.getInt(1);
			if (count <= 0) {

				DBUtils.prepareStmt(conn, "update article set isleaf = 0 where id=" + pid);

			}

			conn.commit();
			conn.setAutoCommit(autoCommit);
			resp.sendRedirect(requestURL);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn, createStmt, executeQuery);

		}

	}

	private void delete(Connection conn, int id, boolean isleaf) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps1 = null;
		if (!isleaf) {

			String sql = "select * from article where pid = ?";
			ps = DBUtils.prepareStmt(conn, sql);
			try {
				ps.setInt(1, id);
				rs = ps.executeQuery();
				while (rs.next()) {

					delete(conn, rs.getInt("id"), rs.getInt("isleaf") == 0);
					System.out.println("遍历子节点");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBUtils.close(null, ps, rs);
			}

		}

		try {
			String sql2 = "delete from article where id =?";
			ps1 = conn.prepareStatement(sql2);
			ps1.setInt(1, id);
			ps1.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(null, ps1, null);

		}

	}

}
