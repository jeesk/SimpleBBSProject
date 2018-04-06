package io.github.jeesk.myutil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import io.github.jeesk.ArticleDaoImpl.ReHandler;
import io.github.jeesk.domain.Article;

/**
 *  简单模拟dbutils的CRUD
 */
public interface JDBCTemplate {

	public static int update(String sql, Object... parms) {
		PreparedStatement ps = null;
		Connection conn = null;
		ResultSet rs = null;
		Statement state = null;

		try {
			conn = DBUtils.getConnection();
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < parms.length; i++) {
				ps.setObject(i + 1, parms[i]);
			}
			int effcctRow = ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			rs.next();
			int rootid = rs.getInt(1);
			String sql2 = " update article set rootid = " + rootid + " where id =" + rootid;
			state = DBUtils.createStmt(conn);
			state.executeUpdate(sql2);
			return effcctRow;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn, ps, rs);
			DBUtils.close(null, state, null);
		}
		return 0;
	}

	/*	public static <T> T query(String sql, ReHandler<T> rsh, Object... parms) {
			PreparedStatement ps = null;
			Connection conn = null;
			ResultSet rs = null;
			try {
				conn = MyDBUtils.INSTANCE.getConnection();
				ps = conn.prepareStatement(sql);
				for (int i = 0; i < parms.length; i++) {
					ps.setObject(i + 1, parms[i]);
				}
	
				rs = ps.executeQuery();
				return rsh.hanlder(rs);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				MyDBUtils.close(ps, conn, rs);
			}
			return null;
		}
	*/
	public static List<Article> query(List<Article> list, int id, int grade) {

		String sql = "SELECT * FROM article WHERE pid =" + id;
		Article article;
		Connection conn = DBUtils.getConnection();
		Statement createStmt = DBUtils.createStmt(conn);

		ResultSet rs;
		try {
			rs = createStmt.executeQuery(sql);
			while (rs.next()) {
				article = new Article();
				article.initArticleFromRs(rs);
				article.setGrade(grade);
				list.add(article);
				if (!article.isIsleaf()) {

					query(list, article.getId(), grade + 1);

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;

	}

	// ___________________________________________________

	public static List<Article> query(Object[] parms) {
		

		List<Article> list = new ArrayList<>();
		String sql = "select * from article where pid = 0 order by pdate DESC limit ?,?";
		Article article;
		ResultSet rs = null;
		Connection conn = DBUtils.getConnection();
		PreparedStatement ps = DBUtils.prepareStmt(conn, sql);
		try {
			for (int i = 0; i < parms.length; i++) {
				ps.setObject(i + 1, parms[i]);
			}

			rs = ps.executeQuery();
			while (rs.next()) {
				article = new Article();
				article.initArticleFromRs(rs);
				list.add(article);

			}
			
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn, ps, rs);
		}
		return list;

	}
	
	
public static List<Article> queryflat(Object[] parms) {
		

		List<Article> list = new ArrayList<>();
		String sql = "select * from article order by pdate DESC limit ?,?";
		Article article;
		ResultSet rs = null;
		Connection conn = DBUtils.getConnection();
		PreparedStatement ps = DBUtils.prepareStmt(conn, sql);
		try {
			for (int i = 0; i < parms.length; i++) {
				ps.setObject(i + 1, parms[i]);
			}

			rs = ps.executeQuery();
			while (rs.next()) {
				article = new Article();
				article.initArticleFromRs(rs);
				list.add(article);

			}
			
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn, ps, rs);
		}
		return list;

	}
	
	
	
	
	
	
	
	
	
	
	
	

	// ______________________________

	public static int count(String sql, List<?> list) {
		PreparedStatement ps = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < list.size(); i++) {
				ps.setObject(i + 1, list.get(i));
			}

			rs = ps.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn, ps, rs);
		}
		return 0;

	}

}
