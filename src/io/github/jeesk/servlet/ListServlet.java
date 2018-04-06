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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.github.jeesk.ArticleDao.ArticleDao;
import io.github.jeesk.ArticleDaoImpl.ArticleDaoImpl;
import io.github.jeesk.domain.Article;
import io.github.jeesk.myutil.DBUtils;
import io.github.jeesk.myutil.JDBCTemplate;

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
	private ArticleDaoImpl articleDaoImpl;

	@Override
	public void init() throws ServletException {
		articleDaoImpl = new ArticleDaoImpl();
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("utf8");
		String parms = req.getParameter("method");

		if ("details".equals(parms)) {
			details(req, resp);
		} else if ("reply".equals(parms)) {
			// 新帖子
			reply(req, resp);
		} else if ("addReply".equals(parms)) {

			try {
				addReply(req, resp);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if ("posts".equals(parms)) {
			// 增加新帖子
			posts(req, resp);
		} else {
			list(req, resp);
		}

	}

	/** 
	 * 增加新帖子
	 * @param req
	 * @param resp
	 */
	private void posts(HttpServletRequest req, HttpServletResponse resp) {
		String title = req.getParameter("title");
		String content = req.getParameter("content");

		Article article = new Article();
		article.setTitle(title);
		article.setCont(content);
		try {
			articleDaoImpl.save(article);
			resp.sendRedirect(req.getContextPath() + "/replyed.jsp");
		} catch (SQLException | IOException e1) {
			e1.printStackTrace();
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
				String sql1 = "update article set isleaf=1 where id = " + pid;
				PreparedStatement prepareStatement = conn.prepareStatement(sql1);
				prepareStatement.executeUpdate();
				conn.commit();
				conn.setAutoCommit(autoCommit);
				resp.sendRedirect(req.getContextPath() + "/replyed.jsp");
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

		String strCurrent = req.getParameter("current");

		int current;
		if (strCurrent != null && !"".equals(strCurrent.trim())) {
			current = Integer.parseInt(strCurrent);
		} else {
			current = 1;
		}
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("current", current);
		int pageSize = 4;
		int offSet = (current - 1) * pageSize;
		Object[] parms = { offSet, pageSize };
		// 查询信息
		List<Article> articles = JDBCTemplate.queryflat(parms);

		// 查询总量
		int count = 0;
		try {
			count = JDBCTemplate.count("select count(*) from article", new ArrayList<Object>());
			count = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
			map.put("count", count);
			req.setAttribute("maps", map);
			req.setAttribute("uri", req.getRequestURL());
			req.setAttribute("articles", articles);
			req.getRequestDispatcher("/article.jsp").forward(req, resp);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		} finally {

		}

	}

}
