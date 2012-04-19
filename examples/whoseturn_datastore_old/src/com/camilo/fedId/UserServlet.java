package com.camilo.fedId;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserServlet extends BaseServlet {
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
		throws ServletException, IOException {
		super.doGet(req, resp);
		
		String searchFor = req.getParameter("q");
		PrintWriter out = resp.getWriter();
	}

}
