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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.github.jeesk.Article;
import io.github.jeesk.util.DBUtils;

/**
 * ListServlet.java
 * @version
 * 2018年3月31日 下午2:26:54
 * @author jeesk
 * @since 1.0
 */
@WebServlet("/list")
public class ListServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {

	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("utf8");
		String parms = req.getParameter("method");

		if ("details".equals(parms)) {
			details(req, resp);
		} else if ("reply".equals(parms)) {
			reply(req, resp);
		} else if ("addReply".equals(parms)) {
			try {
				addReply(req, resp);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else if("posts".equals(parms)) {
			posts(req,resp);
		} else {
			list(req, resp);
		}

	}

	/**
	 * @param req
	 * @param resp
	 */
	private void posts(HttpServletRequest req, HttpServletResponse resp) {
		String title  = req.getParameter("title");
		String content = req.getParameter("content");
		Connection conn = DBUtils.INSTANCE.getConnection();
		Connection conn1 = DBUtils.INSTANCE.getConnection();
		PreparedStatement ps=null;
		Statement state =null;
		ResultSet rs =null;
		
		String sql = "INSERT INTO article VALUE (null,?,?,?,?,now(),?)";
		try {
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			int rootid =-1;
			ps.setInt(1, 0);
			ps.setInt(2, rootid);
			ps.setString(3, title);
			ps.setString(4, content);
			ps.setInt(5, 0);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			rs.next();
			rootid = rs.getInt(1);
			String sql2 = " update article set rootid = " +rootid+ " where id =" + rootid;
			state = conn.createStatement();
			state.executeUpdate(sql2);

			resp.sendRedirect(req.getContextPath() +"/replyed.jsp");
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBUtils.INSTANCE.close(conn, ps, rs);
			DBUtils.INSTANCE.close(conn1, state, null);
			
		}
		
		
	}	

	/**
	 * @param req
	 * @param resp
	 * @throws SQLException 
	 */
	private void addReply(HttpServletRequest req, HttpServletResponse resp) throws SQLException {

		Integer pid = Integer.parseInt(req.getParameter("pid"));
		Integer rootid = Integer.parseInt(req.getParameter("rootid"));
		String title = req.getParameter("title");
		String content = req.getParameter("content");
		Connection conn = DBUtils.INSTANCE.getConnection();
		boolean autoCommit = conn.getAutoCommit();
		conn.setAutoCommit(false);
		String sql = " INSERT INTO article value(null,?,?,?,?,now(),?)";
		PreparedStatement ps = DBUtils.prepareStmt(conn, sql);
		try {

			try {
				ps.setInt(1, pid);
				ps.setInt(2, rootid);
				ps.setString(3, title);
				ps.setString(4, content);
				ps.setInt(5, 0);
				ps.executeUpdate();
				String sql1 = "update article set isleaf=1 where id = "+pid;
				PreparedStatement prepareStatement = conn.prepareStatement(sql1);
				prepareStatement.executeUpdate();
				conn.commit();
				conn.setAutoCommit(autoCommit);
				resp.sendRedirect(req.getContextPath() +"/replyed.jsp");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			DBUtils.INSTANCE.close(conn, ps, null);
		}

	}

	/**
	 * @param req
	 * @param resp
	 */
	private void reply(HttpServletRequest req, HttpServletResponse resp) {

		int pid = Integer.parseInt(req.getParameter("id"));
		int rootid = Integer.parseInt(req.getParameter("rootid"));
		Article article = new Article();
		article.setId(pid);
		article.setRootid(rootid);
		req.setAttribute("article", article);
		try {
			req.getRequestDispatcher("/reply.jsp").forward(req, resp);

		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param req
	 * @param resp
	 */
	private void details(HttpServletRequest req, HttpServletResponse resp) {
		int id = Integer.valueOf(req.getParameter("id"));
		Connection conn = DBUtils.INSTANCE.getConnection();
		String sql = "SELECT * FROM article WHERE id =" + id;
		Article article = null;
		ResultSet executeQuery = null;
		Statement createStmt = DBUtils.INSTANCE.createStmt(conn);
		try {
			executeQuery = createStmt.executeQuery(sql);
			while (executeQuery.next()) {
				article = new Article();
				article.initArticleFromRs(executeQuery);
			}
			if (article == null) {
				System.out.println("有错误");
				return;
			}
			req.setAttribute("article", article);
			
			req.getRequestDispatcher("/articledetails.jsp").forward(req, resp);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtils.INSTANCE.close(conn, createStmt, executeQuery);
		}

	}

	/**
	 * @param req
	 * @param resp
	 */
	private void list(HttpServletRequest req, HttpServletResponse resp) {

		int id = 0;
		int grade = 0;
		List<Article> articles = new ArrayList<>();
		Connection conn = DBUtils.INSTANCE.getConnection();

		try {
			tree(articles, conn, 0, 1);
			req.setAttribute("posts", articles);
			req.getRequestDispatcher("/article.jsp").forward(req, resp);
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param articles
	 * @param conn
	 * @param id
	 * @param grade
	 * @throws SQLException 
	 */
	private void tree(List<Article> articles, Connection conn, int id, int grade) throws SQLException {
		String sql = "SELECT * FROM article WHERE pid =" + id;
		Article article;
		Statement createStmt = DBUtils.INSTANCE.createStmt(conn);

		ResultSet rs = createStmt.executeQuery(sql);

		while (rs.next()) {
			article = new Article();
			article.initArticleFromRs(rs);
			article.setGrade(grade);
			articles.add(article);
			if (!article.isIsleaf()) {

				tree(articles, conn, article.getId(), grade + 1);

			}
		}

	}

}
