/*
 * Copyright notice
 */
package io.github.jeesk.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * LoginServlet.java
 * @version
 * 2018年4月6日 上午12:07:40
 * @author jeesk
 * @since 1.0
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String username = req.getParameter("username");

		String password = req.getParameter("password");
		Map<String, String> map = new HashMap<>();
		if (username == null || !username.trim().equals("admin")) {

			PrintWriter writer = resp.getWriter();
			writer.write("username is not corrent!");
		} else if (password == null || !password.trim().equals("password")) {

			PrintWriter writer = resp.getWriter();
			writer.write("password is not corrent!");

		} else {

			map.put("username", username);
			map.put("password", password);
			req.setAttribute("map", map);
			req.getRequestDispatcher("/articleflat").forward(req, resp);
		}

	}
}
