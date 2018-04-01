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
public class ListServlet extends HttpServlet{
	
	
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		
	}
	

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
			list(req,resp);
		
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
			tree(articles,conn,0,1);
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
			
			while(rs.next()) {
				article = new Article();
				article.setTitle(rs.getString("title"));
				article.setId(rs.getInt("id"));
				article.setPid(rs.getInt("pid"));
				article.setCont(rs.getString("cont"));
				article.setRootid(rs.getInt("rootid"));
				article.setIsleaf(rs.getInt("isleaf")==0 ? true :false);
				article.setGrade(grade);
				article.setPdate(rs.getTimestamp("pdate"));
				articles.add(article);
				if( !article.isIsleaf() ) {

					tree(articles,conn,article.getId(),grade+1);
					
				}
			}
		
		
	}

}
