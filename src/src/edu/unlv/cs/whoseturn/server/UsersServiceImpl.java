package edu.unlv.cs.whoseturn.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;


import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.unlv.cs.whoseturn.client.UsersService;
import edu.unlv.cs.whoseturn.domain.Badge;
import edu.unlv.cs.whoseturn.domain.BadgeAwarded;
import edu.unlv.cs.whoseturn.domain.PMF;

@SuppressWarnings("serial")
public class UsersServiceImpl extends RemoteServiceServlet implements
		UsersService {

	private static final Map<String, String> openIdProviders;
    static {
        openIdProviders = new HashMap<String, String>();
        openIdProviders.put("google", "www.google.com/accounts/o8/id");
        openIdProviders.put("yahoo", "yahoo.com");
        openIdProviders.put("myspace", "myspace.com");
        openIdProviders.put("aol", "aol.com");
        openIdProviders.put("myopenid", "myopenid.com");
        openIdProviders.put("facebook", "facebook.com");
    }
    
	public String usersServer()
	{
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
        if (user != null) {
            return user.getNickname();
        } else {
            return userService.createLoginURL("");
        }
	}
	
	public Boolean isLoggedIn()
	{
		UserService userService = UserServiceFactory.getUserService();

		return userService.isUserLoggedIn();
	}
	
	public String getUsername()
	{
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        if(user != null)
        	return user.getEmail();
        
        return null;
	}
	
	public String getLoginURL(String providerName, String location)
	{
		UserService userService = UserServiceFactory.getUserService();
		String providerUrl = openIdProviders.get(providerName);
        String loginUrl = userService.createLoginURL(location, null, providerUrl, null);
        return loginUrl;
	}
	
	public String getLogoutURL(String location)
	{
		UserService userService = UserServiceFactory.getUserService();
		
		return userService.createLogoutURL(location);

	}
	
	public String addTestUser(String username, String email, Boolean admin)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();

        edu.unlv.cs.whoseturn.domain.User user = new edu.unlv.cs.whoseturn.domain.User();
        user.setAdmin(admin);
        user.setAvatarBlob(null);
        user.setDeleted(false);
        user.setEmail(email);
        user.setUsername(username);
        user.setBadges(new HashSet<String>());
        
        Badge badge = new Badge();
        badge.setBadgeCriteria("IDUNNO");
        badge.setBadgeName("TESTBADGE");
        badge.setDeleted(false);
        badge = pm.makePersistent(badge);
        
        BadgeAwarded badgeAwarded = new BadgeAwarded();
        badgeAwarded.setBadgeTypeKeyString(badge.getKeyString());
        badgeAwarded.setDeleted(false);
        badgeAwarded = pm.makePersistent(badgeAwarded);
        
        user.addBadge(badgeAwarded);
        
        try {
            pm.makePersistent(user);
        } finally {
            pm.close();
        }
        
        return user.getKeyString();
	}
	
	public List<String[]> findUsers()
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		javax.jdo.Query query = pm.newQuery(edu.unlv.cs.whoseturn.domain.User.class);

	    List<String[]> resultStringList = new ArrayList<String[]>();
	    List<edu.unlv.cs.whoseturn.domain.User> results;
	    
	    try {
	        results = (List<edu.unlv.cs.whoseturn.domain.User>) query.execute();
	        if (!results.isEmpty()) {
	            for (edu.unlv.cs.whoseturn.domain.User e : results) {
	                resultStringList.add(new String[] {e.getUsername(), e.getEmail(), e.getAdmin().toString()});
	            }
	        } else {
	            return null;
	        }
	    } finally {
	        query.closeAll();
	        pm.close();
	    }
		return resultStringList;
	}
	
	public String findUserByKey(String key)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Key k = KeyFactory.stringToKey(key);
		edu.unlv.cs.whoseturn.domain.User user = pm.getObjectById(edu.unlv.cs.whoseturn.domain.User.class, k);
		
		return "Username: "+user.getUsername()+"<br />Email: "+user.getEmail()+"<br />Admin: "+user.getAdmin().toString();
	}
	
	// get user(s) by username
	@SuppressWarnings("unchecked")
	public List<edu.unlv.cs.whoseturn.domain.User> getUserByUsername(String userName) {
		List<edu.unlv.cs.whoseturn.domain.User> users = new ArrayList<edu.unlv.cs.whoseturn.domain.User>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
		      Query q = pm.newQuery(edu.unlv.cs.whoseturn.domain.User.class, "username == u");
		      q.declareParameters("java.lang.String u");
		      users = (List<edu.unlv.cs.whoseturn.domain.User>) q.execute(userName);
		      
		} finally {
			pm.close();
		}
		return users;
	}
	
	// Remove user by username
	@SuppressWarnings("unchecked")
	public void removeUserByUsername(String username) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query q = pm.newQuery(edu.unlv.cs.whoseturn.domain.User.class, "username == u");
		    q.declareParameters("jvaa.lang.String u");
		    List<edu.unlv.cs.whoseturn.domain.User> users = (List<edu.unlv.cs.whoseturn.domain.User>) q.execute(username);
		    
		    for (edu.unlv.cs.whoseturn.domain.User user : users) {
		        if (username.equals(user.getUsername())) {
		          pm.deletePersistent(user);
		        }
		    }
		} finally {
			pm.close();
		}
	}
}
