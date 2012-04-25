package edu.unlv.cs.whoseturn.shared;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.apache.commons.validator.routines.EmailValidator;

import edu.unlv.cs.whoseturn.domain.PMF;

/**
 * EntryVerifier validates that the data the user enters is valid.
 */
public class EntryVerifier 
{	
	private static String errorMessage; // The message to be displayed when an error occurs
	
	/**
	 * Verifies that the e-mail is possibly valid and doesn't already exist.
	 * @param email the email to validate
	 * @return true if valid, false if invalid
	 */
	@SuppressWarnings("unchecked")
	public static String isEmailValid(String email)
	{
		// The email can't be null
		if (email.isEmpty())
		{
			errorMessage = "E-mail cannot be empty.";
			return errorMessage;
		}
		
		// Apache Commons email validator
		// Gets it valid enough so only things of the form "bob@domain.com" get through
		EmailValidator validator = EmailValidator.getInstance();
		boolean isValid = validator.isValid(email);
		
		// For one reason or another (invalid characters, no domain, no "@", etc.), the email address is invalid
		if (!isValid)
		{
			errorMessage = "Invalid e-mail address";
			return errorMessage;
		}
		
		
		// The following checks for a duplicate email address in the database of current users
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(edu.unlv.cs.whoseturn.domain.User.class);

		List<edu.unlv.cs.whoseturn.domain.User> results;
	    
	    results = (List<edu.unlv.cs.whoseturn.domain.User>) query.execute();
	   	if (!results.isEmpty()) 
	   	{
	            for (edu.unlv.cs.whoseturn.domain.User e : results) 
	            {
	                if (email.equals(e.getEmail()))
	                {
	                	errorMessage = "Email already exists";
	                	return errorMessage;
	                }
	            }
	   	}
		
		// If we're here, the email is new and (hopefully) valid	
		return "Valid";
	}

	/**
	 * Verifies that the username is valid doesn't already exist.
	 * @param username the username to validate
	 * @return true if valid, false if invalid
	 */
	@SuppressWarnings("unchecked")
	public static String isUsernameValid(String username)
	{
		// The username can't be less than 3 characters
		if (username.length() < 3)
		{
			errorMessage = "Username must have at least 3 characters";
			return errorMessage;
		}
		
		// The username can't be longer than 30 characters
		if (username.length() > 30)
		{
			errorMessage = "Username must be under 30 characters";
			return errorMessage;
		}
		
		// The username can't contain special characters
		if (!username.matches("^[a-zA-Z0-9 _]+$"))
		{
			errorMessage = "Username can't contain special characters";
			return errorMessage;
		}

		// The following checks for a duplicate username in the database of current users
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(edu.unlv.cs.whoseturn.domain.User.class);

		List<edu.unlv.cs.whoseturn.domain.User> results;
			    
		results = (List<edu.unlv.cs.whoseturn.domain.User>) query.execute();
		if (!results.isEmpty()) 
		{
			for (edu.unlv.cs.whoseturn.domain.User e : results) 
			{
				if (username.equals(e.getUsername()))
				{
					errorMessage = "Username already exists";
					return errorMessage;
				}
			}
		}
      
		// If we're here, the username is new and within the specified bounds
		return "Valid";
	}
	
	/**
	 * Verifies that the category is valid and doesn't already exist.
	 * @param category The category to be checked.
	 * @return true is valid, false if invalid
	 */
	@SuppressWarnings("unchecked")
	public static String isCategoryValid(String category) {
		// The category can't be less than 3 characters
		if (category.length() < 3) {
			errorMessage = "Category must have at least 3 characters";
			return errorMessage;
		}
		
		// The category can't be longer than 40 characters
		if (category.length() > 40) {
			errorMessage = "Category must be under 40 characters";
			return errorMessage;
		}
		
		// The category can't contain special characters
		if (!category.matches("^[a-zA-Z0-9 _]+$")) {
			errorMessage = "A category can't contain special characters";
			return errorMessage;
		}
		
		// The following checks for a duplicate category in the database of current categories
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(edu.unlv.cs.whoseturn.domain.Category.class);

		List<edu.unlv.cs.whoseturn.domain.Category> results;
			    
		results = (List<edu.unlv.cs.whoseturn.domain.Category>) query.execute();
		
		if (!results.isEmpty()) 
		{
			for (edu.unlv.cs.whoseturn.domain.Category queriedCategory : results) 
			{
				if (category.equals(queriedCategory.getName()))
				{
					errorMessage = "Category already exists";
					return errorMessage;
				}
			}
		}
		
		// If we're here, the category is new and has proper character constraints
		return "Valid";
	}

	/**
	 * Verifies that the timeBoundary is within proper bounds
	 * @param timeBoundary The minimum amount of time between events
	 * @return true if valid, false if invalid
	 */
	public static String isTimeValid(Integer timeBoundary) {
		// The time boundary can't be empty
		if (timeBoundary == null) {
			errorMessage = "The time boundary can't be empty";
			return errorMessage;
		}
				
		// The time boundary can't be less than 1 hour
		if (timeBoundary.intValue() < 1) {
			errorMessage = "The time boundary must be at least 1 hour";
			return errorMessage;
		}
				
		// The time boundary can't be more than 24 hours
		if (timeBoundary.intValue() > 24) {
			errorMessage = "The time boundary can't be greater than 1 day (24 hours)";
			return errorMessage;
		}
		
		// If we're here, the timeBoundary is valid
		return "Valid";
	}
}
