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
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.github.jeesk.Article;
import io.github.jeesk.myutil.DBUtils;

/**
 * ArticleFlatDetailsServlet.java
 * @version
 * 2018年4月5日 上午12:31:18
 * @author jeesk
 * @since 1.0
 */
@WebServlet("/articleflatdetails")
public class ArticleFlatDetailsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("utf8");
		List<Article> arrayList = new ArrayList<Article>();
		Article article = null;
		Connection conn = DBUtils.INSTANCE.getConnection();
		int id = Integer.valueOf(req.getParameter("id"));
		String sql = "select * from article where rootid = ? order by pdate asc";
		ResultSet rs = null;
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				article = new Article();
				article.initArticleFromRs(rs);
				arrayList.add(article);
			}
			req.setAttribute("articles", arrayList);
			req.getRequestDispatcher("/articledetailsflat.jsp").forward(req, resp);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {

			DBUtils.close(conn, ps, rs);
		}
	}
}
