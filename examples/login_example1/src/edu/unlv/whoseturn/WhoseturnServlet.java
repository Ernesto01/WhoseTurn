package edu.unlv.whoseturn;

import java.io.IOException;
import javax.servlet.http.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class WhoseturnServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser(); // or req.getUserPrincipal()
        
        resp.setContentType("text/html");
        if(user != null) {
			
			resp.getWriter().println("<b>Hello </b>" + user.getNickname());
        }
        else {
        	resp.getWriter().println("<b>Hello anonymous</b>");
        }
	
	}
}
