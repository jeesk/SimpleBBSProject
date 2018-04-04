/*
 * Copyright notice
 */
package io.github.jeesk.servlet;

import java.io.IOException;
import java.sql.Connection;
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
import io.github.jeesk.util.DBUtils;

/**
 * ArticleFlatServlet.java
 * @version
 * 2018年4月4日 上午12:27:20
 * @author jeesk
 * @since 1.0
 */
@WebServlet("/articleflat")
public class ArticleFlatServlet extends HttpServlet{


	private static final long serialVersionUID = 1L;
	
	

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("utf8");
		List<Article> articles =  new ArrayList<Article>();
	    Connection conn=  DBUtils.INSTANCE.getConnection();
	    Statement stmt =  DBUtils.createStmt(conn);
	    Article article=null;
	    ResultSet rs= DBUtils.executeQuery("select * from article where pid = 0", stmt);
	    try {
	    	
			while(rs.next()){
				article	 =  new Article();
			        article.initArticleFromRs(rs);
			        articles.add(article);
			        System.out.println(article);
			}
			req.setAttribute("articles", articles);
			req.getRequestDispatcher("/articleflat.jsp").forward(req, resp);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
