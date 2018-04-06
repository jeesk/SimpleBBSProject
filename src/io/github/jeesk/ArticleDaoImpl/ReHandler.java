package io.github.jeesk.ArticleDaoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *  处理器接口类
 * 
 *
 * @param <T>
 */
public interface ReHandler<T> {
	
	T hanlder(ResultSet rs) throws SQLException;
}
