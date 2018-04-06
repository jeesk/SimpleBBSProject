/*
 * Copyright notice
 */
package io.github.jeesk.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import lombok.Data;

/**
 * Article.java
 * @version
 * 2018年3月31日 下午2:18:33
 * @author jeesk
 * @since 1.0
 */
@Data
public class Article {
	private Integer id;
	private Integer pid;  // article prent id 
	private Integer rootid;
	private String title;
	private String cont;
	private Date pdate;
	private boolean isleaf;
	private int grade;
	public void initArticleFromRs(ResultSet rs) {
		
		try {
			setTitle(rs.getString("title"));
			setId(rs.getInt("id"));
			setPid(rs.getInt("pid"));
			setCont(rs.getString("cont"));
			setRootid(rs.getInt("rootid"));
			setIsleaf(rs.getInt("isleaf")==0 ? true :false);
			setGrade(0);
			setPdate(rs.getTimestamp("pdate"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		

		
	}

	public String getGradexx() {
		String str="";
		for(int i=0;i<getGrade();i++) {
			str+="----";
		}
		return str;
	}


	
}
