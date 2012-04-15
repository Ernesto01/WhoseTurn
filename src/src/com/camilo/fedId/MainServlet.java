package com.camilo.fedId;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class MainServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("Hello");
        
		doPut(req, resp);
	}
		
	
	
	
	@Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
		resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        
        User.createUser("Master");
        Entity user = User.getUser("Master");
        
        Entity record = Record.createOrUpdateRecord("Master", "drive", (long)2, (long)10);
        
        record = Record.incrementTimesUsed("Master","drive"); // Change entity
        if(record != null) {
        	out.println("Hello World --- ");
        	out.println(record.getProperty("name"));
        	out.println(" --- ");
        	out.println(record.getProperty("timesGone"));
        	out.println(" --- ");
            out.println(record.getProperty("timesUsed"));
            out.println(" ------ ");
  
            out.println(record.getProperty("participationDate"));
        }
        else {
        	out.println("Error getting entity");
        }
        
	}
}
