package edu.unlv.cs.whoseturn.mobile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import edu.unlv.cs.whoseturn.domain.Category;
import edu.unlv.cs.whoseturn.domain.PMF;
import edu.unlv.cs.whoseturn.domain.UserSelection;

@SuppressWarnings("serial")
public class MobileCategoryScreenController extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		RequestDispatcher view = request.getRequestDispatcher("categoryscreen.jspx");
		try {
			doStuff(request, response);
			view.forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void doStuff(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String dbg = "";
		
		PersistenceManager manager = PMF.get().getPersistenceManager();
		
		// Find which category, ensure validity
		String categoryKey = request.getParameter("keyString");
		
		if (categoryKey == null) {
			response.getOutputStream().print("Error: invalid category");
			return;
		}
		
		Object categoryKeyStringObject = manager.getObjectById(Category.class, categoryKey);
		if (!(categoryKeyStringObject instanceof Category)) {
			response.getOutputStream().print("Error: invalid category");
			return;
		}
		Category category = (Category)categoryKeyStringObject;
		
		request.setAttribute("currentCategory", category);
		
		// Model who is logged in
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
        request.setAttribute("currentUser", user);
        
        // Model who is currently selected
        String selectedKeys = request.getParameter("selectedPersons");
        if (selectedKeys == null) {
        	selectedKeys =  "";
        }
        
        // Split comma separated values
        String[] selectedKeyStrings = selectedKeys.split(",\\s*");
        
        // Find the currently selected users, add to model
        List<edu.unlv.cs.whoseturn.domain.User> selectedUsers = new LinkedList<edu.unlv.cs.whoseturn.domain.User>();
        for (String personKey : selectedKeyStrings) {
        	if ((personKey == null) || (personKey.equals(""))) {
        		continue;
        	}
        	Object personObject;
        	try {
            	personObject = manager.getObjectById(edu.unlv.cs.whoseturn.domain.User.class, personKey);
        	}
        	catch (JDOObjectNotFoundException e) {
        		// User manually mucked about with the URL, diregard
        		continue;
        	}
        	
        	if (!(personObject instanceof edu.unlv.cs.whoseturn.domain.User)) {
        		continue;
        	}
        	edu.unlv.cs.whoseturn.domain.User selectedUser = (edu.unlv.cs.whoseturn.domain.User)personObject;
        	selectedUsers.add(selectedUser);
        }
        
        request.setAttribute("selectedPersons", selectedUsers);
		
        // List users

		List<UserSelection> persons = new ArrayList<UserSelection>();
		List<edu.unlv.cs.whoseturn.domain.User> users = new ArrayList<edu.unlv.cs.whoseturn.domain.User>();
//		
//		Extent<Category> extent = manager.getExtent(Category.class, true);
//		for (Category category : extent) {
//			categories.add(category);
//		}
		
		javax.jdo.Query query = manager.newQuery(edu.unlv.cs.whoseturn.domain.User.class);
		List<Object> results;
		try {
			results = (List<Object>)query.execute();
			
			for (Object result : results) {
				if (result instanceof edu.unlv.cs.whoseturn.domain.User)
				{
					edu.unlv.cs.whoseturn.domain.User domainUser = (edu.unlv.cs.whoseturn.domain.User)result;
					users.add(domainUser);
					
					boolean selected = selectedUsers.contains(domainUser);
					UserSelection userSelection = new UserSelection(domainUser, selected);
					persons.add(userSelection);
				}
			}
		} finally {
			query.closeAll();
			manager.close();
		}
		
		request.setAttribute("persons", persons);
	}
}
