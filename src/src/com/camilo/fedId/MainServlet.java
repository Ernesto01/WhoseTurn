package com.camilo.fedId;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class MainServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		/*
		resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("Hello");
        */
        
		doPut(req, resp);
	}
		
	
	
	
	@Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
		resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        
        // Create user
        User.createUser("Master", "the master user", true);
        
        //Create record for Master user
        Entity record = Record.createOrUpdateRecord("Master", "drive", (long)2, (long)10);
        
        // Retrieve master user record - note the record name is passed in
        Entity record2 = Record.getSingleRecord("Master"+"drive");
        
        displayTimesUsed(out, record);
        displayTimesUsed(out, record2);
        
        incrementTimesUsed(record);
        
        //record.setProperty("timesUsed", 25);
        /*
        out.println("<p>");
        out.println(record2.getProperty("timesUsed"));
        out.println("</p>");
        */
        
        // Increase times used for the record belonging to user 'Master' in category 'drive'
        record2 = record; //Record.incrementTimesUsed("Master","drive"); 
        
        if(record2 != null) {
        	out.println("<p>");
        	out.println("Hello World --- ");
        	out.println(record2.getProperty("name"));
        	out.println(" --- ");
        	out.println(record2.getProperty("timesGone"));
        	out.println(" --- ");
            out.println(record2.getProperty("timesUsed"));
            out.println(" ------ ");
            out.println(record2.getProperty("participationDate"));
            out.println("</p>");
            out.println("<p>");
            out.println(ManageUsers.getTimesUsed("Master", "drive"));
            out.println("</p>");
            
        }
        else {
        	out.println("Error getting entity");
        }
        
	}
	
	void displayAllRecords(PrintWriter out) {
		Iterable<Entity> records = Record.getAllRecords();
		
		for(Entity record : records) {
			displayTimesUsed(out, record);
		}
	}
	
	void displayTimesUsed(PrintWriter out, Entity entity) {
		out.println("<p> ------");
        out.println(entity.getProperty("timesUsed"));
        out.println(" ------- </p>");
	}
	
	void displayTimesGone(PrintWriter out, Entity entity) {
		out.println("<p> ------");
        out.println(entity.getProperty("timesGone"));
        out.println(" ------ </p>");
	}
	
	void incrementTimesUsed(Entity record) {
		record.setProperty("timesUsed", (java.lang.Long)record.getProperty("timesUsed") + 1);
	}
}
