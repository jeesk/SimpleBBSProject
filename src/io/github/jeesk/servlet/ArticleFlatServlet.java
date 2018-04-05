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

import io.github.jeesk.Article;
import io.github.jeesk.util.DBUtils;

/**
 * ArticleFlatServlet.java
 * @version
 * 2018年4月4日 上午12:27:20
 * @author jeesk
 * @since 1.0
 */
@WebServlet("/articleflat")
public class ArticleFlatServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		
		req.setCharacterEncoding("utf8");
		String strCurrent = req.getParameter("current");

		int current;
		if (strCurrent != null && !"".equals(strCurrent.trim())) {
			current = Integer.parseInt(strCurrent);
		} else {
			current = 1;
		}
		List<Article> articles = new ArrayList<Article>();
		Connection conn1 = DBUtils.getConnection();
		Map<String, Integer> map = new HashMap();
		Article article = null;
		ResultSet rs = null;

		Statement stmt1 = DBUtils.createStmt(conn1);

		map.put("current", current);
		int pageSize = 4;
		int offSet = (current - 1) * pageSize;
		System.out.println("offset=" + offSet);
		// 查询信息

		Connection conn = DBUtils.INSTANCE.getConnection();
		String sql = "select * from article where pid = 0 limit ?,?";

		PreparedStatement ps = DBUtils.prepareStmt(conn, sql);
		try {
			ps.setInt(1, offSet);
			ps.setInt(2, pageSize);
			rs = ps.executeQuery();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		// 查询总量
		ResultSet rs1 = DBUtils.executeQuery("select count(*) from article where pid = 0", stmt1);
		int count = 0;

		try {

			rs1.next();
			count = rs1.getInt(1);
			count = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
			map.put("count", count);
			req.setAttribute("maps", map);

			while (rs.next()) {
				article = new Article();
				article.initArticleFromRs(rs);
				articles.add(article);
			}
			
			req.setAttribute("uri", req.getRequestURL());
			req.setAttribute("articles", articles);
			req.getRequestDispatcher("/articleflat.jsp").forward(req, resp);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn, ps, rs);
			DBUtils.close(conn1, stmt1, rs1);

		}
	}

}
