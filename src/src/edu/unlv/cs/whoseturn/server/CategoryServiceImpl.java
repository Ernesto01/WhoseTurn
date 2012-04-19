package edu.unlv.cs.whoseturn.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.unlv.cs.whoseturn.client.CategoryService;
import edu.unlv.cs.whoseturn.domain.Category;
import edu.unlv.cs.whoseturn.domain.PMF;

public class CategoryServiceImpl extends RemoteServiceServlet implements CategoryService {

	PersistenceManager pm = PMF.get().getPersistenceManager();
	
	
	public void addCategory(String categoryName) {
		try {
		      pm.makePersistent(new Category(categoryName));
		} finally {
		      pm.close();
		}
		
	}

	
	@SuppressWarnings("unchecked")
	public void removeCategory(String categoryName) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query q = pm.newQuery(edu.unlv.cs.whoseturn.domain.Category.class, "name == n");
		    q.declareParameters("java.lang.String n");
			List<edu.unlv.cs.whoseturn.domain.Category> categories = (List<edu.unlv.cs.whoseturn.domain.Category>) q.execute(categoryName);
		    
		    for (edu.unlv.cs.whoseturn.domain.Category category : categories) {
		        if (categoryName.equals(category.getName())) {
		          pm.deletePersistent(category);
		        }
		    }
		} finally {
			pm.close();
		}
		
	}

	// Return all category names
	public List<String> getAllCategories() {
		List<edu.unlv.cs.whoseturn.domain.Category> categories;
		List<String> categoryNames = new ArrayList<String>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
	    
	    try {
	    	Query q = pm.newQuery(edu.unlv.cs.whoseturn.domain.Category.class);
	    	categories = (List<edu.unlv.cs.whoseturn.domain.Category>) q.execute();
	    	
	    } finally {
	        pm.close();
	    }

	    for(edu.unlv.cs.whoseturn.domain.Category category: categories) {
	    	categoryNames.add(category.getName());
	    }
	    return categoryNames;
	}
	
}
