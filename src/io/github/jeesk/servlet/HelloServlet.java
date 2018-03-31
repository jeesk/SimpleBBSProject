/*
 * Copyright notice
 */
package io.github.jeesk.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HelloServlet.java
 * @version
 * 2018年3月30日 下午6:23:31
 * @author jeesk
 * @since 1.0
 */
public class HelloServlet extends HttpServlet {
	private String token;

	private static final long serialVersionUID = 1L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		token = config.getInitParameter("token");
		System.out.println("世界你好!!");
		
		System.out.println("anlkw");
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			
	}

}
