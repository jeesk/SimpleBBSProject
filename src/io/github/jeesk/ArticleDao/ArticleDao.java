package io.github.jeesk.ArticleDao;

import java.sql.SQLException;
import java.util.List;

import io.github.jeesk.domain.Article;
/**
 * 商品管理的DAO
 * 
 * @param <T>
 */
public interface ArticleDao<T> {
	public void save(Article product) throws SQLException;

	public void delete(Long id) throws SQLException;

	public void update(Article product) throws SQLException;

	public Article getOne(Long id) throws SQLException;

	public List<Article> getAll() throws SQLException;

	/**
	 * @param list
	 * @param id
	 * @param grade
	 * @return
	 * @throws SQLException
	 */
	
	
	//public PageList query(IQuery po) throws SQLException;
}
