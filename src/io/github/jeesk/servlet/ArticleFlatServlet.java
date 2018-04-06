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

import io.github.jeesk.ArticleDaoImpl.ArticleDaoImpl;
import io.github.jeesk.domain.Article;
import io.github.jeesk.myutil.DBUtils;
import io.github.jeesk.myutil.JDBCTemplate;

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
	private ArticleDaoImpl articleDaoImpl;

	@Override
	public void init() throws ServletException {
		articleDaoImpl = new ArticleDaoImpl();
	}

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
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("current", current);
		int pageSize = 4;
		int offSet = (current - 1) * pageSize;
		Object[] parms = { offSet, pageSize };
		// 查询信息
		List<Article> articles = JDBCTemplate.query(parms);

		// 查询总量
		int count = 0;
		try {
			count = JDBCTemplate.count("select count(*) from article where pid = 0", new ArrayList<Object>());
			count = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
			map.put("count", count);
			req.setAttribute("maps", map);
			req.setAttribute("uri", req.getRequestURL());
			req.setAttribute("articles", articles);
			req.getRequestDispatcher("/articleflat.jsp").forward(req, resp);
		} finally {

		}
	}

}
