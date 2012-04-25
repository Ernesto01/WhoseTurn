package edu.unlv.cs.whoseturn.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.unlv.cs.whoseturn.client.TurnService;
import edu.unlv.cs.whoseturn.domain.Category;
import edu.unlv.cs.whoseturn.domain.PMF;
import edu.unlv.cs.whoseturn.domain.Strategy;
import edu.unlv.cs.whoseturn.domain.Turn;
import edu.unlv.cs.whoseturn.domain.TurnItem;

/**
 * Category Service which allows the client to get information from the server
 * regarding categories.
 */
@SuppressWarnings("serial")
public class TurnServiceImpl extends RemoteServiceServlet implements
		TurnService {

	/**
	 * findDriver is called from another function located in a service call on
	 * the client layer.
	 * 
	 * @param usernames The list of usernames in the turn.
	 * @param category	The category name of the turn.
	 * @return a String which will represent the selected driver of Whose Turn
	 */
	public List<String> findDriver(List<String> usernames, String categoryName) {
		edu.unlv.cs.whoseturn.domain.User driver;

		/**
		 * Persistence manager
		 */
		PersistenceManager pm = PMF.get().getPersistenceManager();

		/**
		 * Add the logged in user's username to the list.
		 */
		usernames = addLoggedUser(usernames);
		
		/**
		 * Get the user objects based off the usernames provided.
		 */
		List<edu.unlv.cs.whoseturn.domain.User> userObjects = getUserObjects(usernames);
		
		/**
		 * Get the category based off the category name provided.
		 */
		Category category = getCategoryObject(categoryName);

		/**
		 * Get the strategy from the category.
		 */
		String strategyKeyString = category.getStrategyKeyString();			// The keystring of the strategy for the category
		Key strategyKey = KeyFactory.stringToKey(strategyKeyString);		// The key of the strategy
		Strategy strategy = (Strategy) pm.getObjectById(Strategy.class,		// The strategy object
				strategyKey);

		/**
		 * Close the persistence manager
		 */
		pm.close();

		/**
		 * Depending on the strategy from the category, execute the respective
		 * strategy.
		 * 
		 * Case 1: Least recently gone
		 * Case 2: Lowest ratio
		 * Case 3: Random user
		 */
		switch (strategy.getStrategyId()) {
		case 1:
			driver = leastRecentlyGone(userObjects, category);
			break;
		case 2:
			driver = lowestRatio(userObjects, category);
			break;
		case 3:
			driver = chooseRandomUser(userObjects);
			break;
		default:
			// TODO Error message if an invalid strategy ID was found
			driver = new edu.unlv.cs.whoseturn.domain.User();
			driver.setUsername("UnknownDriver");
		}

		/**
		 * Persistence manager
		 */
		pm = PMF.get().getPersistenceManager();
		
		/**
		 * Generate a turn for this request to be persisted.
		 */
		Turn turn = new Turn();									// The turn object for this turn
		TurnItem tempTurnItem;									// Temporary turnitem to be used for persistence for each user
		List<edu.unlv.cs.whoseturn.domain.User> userList = new ArrayList<edu.unlv.cs.whoseturn.domain.User>();	// The list of the user objects from the database for modification
		edu.unlv.cs.whoseturn.domain.User tempUser;				// Temporary user to calculate a turn item for
		String tempUserKeyString;								// Keystring for the user to be used for turnitem addition
		Key tempUserKey;										// Key of the user to be usd for turnitem addition
		turn.setCategoryKeyString(category.getKeyString());		// Sets the turn's category
		turn.setTurnDateTime(new Date());						// Sets the turn's datetime to now
		turn.setTurnItems(new HashSet<String>());				// Prepares the turn for a set of turn items
		
		/**
		 * The user chose no one for the turn and therefore the turn must be deleted.
		 */
		if (usernames.size() == 1) {
			turn.setDeleted(true);
		} else {
			turn.setDeleted(false);
		}
		
		/**
		 * Persist the turn.
		 */
		turn = pm.makePersistent(turn);
		String turnKeyString = turn.getKeyString();
		pm.close();
		
		
		pm = PMF.get().getPersistenceManager();
		turn = new Turn();
		turn = pm.getObjectById(Turn.class, KeyFactory.stringToKey(turnKeyString));

		/**
		 * Generate turn items for reach user and persist those for each user,
		 * adding it to their TurnItem lists.
		 */
		for (int i = 0; i < userObjects.size(); i++) {
			
			/**
			 * Generate a turnitem for the current user in the list.
			 */
			tempTurnItem = new TurnItem();											// Generate a new temporary turnitem for the current user in the list
			tempTurnItem.setCategoryKeyString(category.getKeyString());				// Set the category of the turnitem to the turn's category
			tempTurnItem.setDeleted(false);											// Set the deleted flag to false
			tempTurnItem.setOwnerKeyString(userObjects.get(i).getKeyString());		// Set the owner of the turnitem to the current user in the list
			
			/**
			 * If the chosen driver equals this current user in the list,
			 * set them as being selected.
			 */
			if (driver.getUsername().equals(userObjects.get(i).getUsername())) {
				tempTurnItem.setSelected(true);
			} else {
				tempTurnItem.setSelected(false);
			}
			
			tempTurnItem.setTurnKeyString(turn.getKeyString());		// Set the turnkeystring to the persisted turn's
			tempTurnItem.setVote(0);								// Default the vote flag to 0 (not verified)
			
			/**
			 * The user chose no one for the turn and therefore the turnitem must also be deleted.
			 */
			if (usernames.size() == 1) {
				tempTurnItem.setDeleted(true);
			} else {
				tempTurnItem.setDeleted(false);
			}
			
			tempTurnItem = pm.makePersistent(tempTurnItem);		// Persist the turnitem for the current user
			turn.addTurnItem(tempTurnItem);						// Add the turnitem to the turn
			
			/**
			 * Add the turnitem to the user's turnitem list.
			 * Users need be retrieved again to allow the persistance 
			 * manager to keep track of the modification to the new objects.
			 */
			tempUser = new edu.unlv.cs.whoseturn.domain.User();									// Creates a temporary user
			tempUserKeyString = userObjects.get(i).getKeyString();								// Gets the user's keystring out of the userObject list
			tempUserKey = KeyFactory.stringToKey(tempUserKeyString);							// Generate and store the key from the keystring
			tempUser = pm.getObjectById(edu.unlv.cs.whoseturn.domain.User.class, tempUserKey);	// Get the user from the database
			tempUser.addTurnItem(tempTurnItem);													// Add the generated turnitem to the user's list
			userList.add(tempUser);																// Store the user into a list for update
		}
		
		
		/**
		 * Close the persistance manager, persist changes, and return the found username and keystring
		 */
		
		pm.close();
		
		List<String> returnList = new ArrayList<String>();
		returnList.add(driver.getUsername());
		returnList.add(turn.getKeyString());
		return returnList;
	}

	/**
	 * Algorithm which chooses a user based explicitly on the amount of times
	 * they were selected/turnItems; the user with the least ratio will be
	 * chosen to handle all driving responsibilities
	 * 
	 * @param users
	 *            list of users, as well as a category are passed into the
	 *            lowestRatio method to handle and retrieve the amount of times
	 *            the user participated in such a category.
	 * @param category
	 *            will represent a drive, chips & salsa, or ice cream
	 * @return User, which then will be used to access the a string
	 *         representing the user name
	 */
	public edu.unlv.cs.whoseturn.domain.User lowestRatio(
			List<edu.unlv.cs.whoseturn.domain.User> users, Category category) {
		
		List<Double> ratioList = new ArrayList<Double>();	// Ratio list to store the ratio of the users in the turn
		List<String> turnItemsKeyStrings;					// Temporary storage of a user's turnitems keystrings
		List<TurnItem> turnItems;							// Temporary storage of a user's turnitem objects
		Double tempTurnCount;								// Temporary storage of a user's total times gone in the category
		Double tempSelectedCount;							// Temporary storage of a user's selected times in the category
		String turnItemKeyString;							// Temporary storage of a user's turnitem keystring
		
		/**
		 * Persistence manager
		 */
		PersistenceManager pm = PMF.get().getPersistenceManager();

		
		/**
		 * Generates the user's ratio in the specified category and stores it.
		 */
		for (int i = 0; i < users.size(); i++) {
			turnItems = new ArrayList<TurnItem>();			// Resets the temporary turnitems list
			tempTurnCount = 0.0;							// Resets the turn count
			tempSelectedCount = 0.0;						// Resets the selected count
			turnItemsKeyStrings = new ArrayList<String>(users.get(i).getTurnItems());	// Sets the keystrings to the list retrieved from the user's object

			/**
			 * Gets all the turnitem objects for the current user.
			 */
			for (int j = 0; j < turnItemsKeyStrings.size(); j++) {
				turnItemKeyString = turnItemsKeyStrings.get(j);				// Gets the keystring for the current turnitem to retrieve
				turnItems.add(pm.getObjectById(TurnItem.class, KeyFactory	// Adds the turnitem's object in the turnItems list
						.stringToKey(turnItemKeyString)));
			}

			/**
			 * Generates a ratio for the user based off how many times 
			 * they've gone compared to the number of times they've driven.
			 */
			for (int k = 0; k < turnItems.size(); k++) {
				
				/**
				 * Checks if the turnitem is in the current category.
				 * If so, add it to the ratio calculation.
				 */
				if (turnItems.get(k).getCategoryKeyString()
						.equals(category.getKeyString())) {
					tempTurnCount++;
					
					/**
					 * Check if the user drove for this turnitem.
					 * If so, increase the selected count.
					 */
					if (turnItems.get(k).getSelected()) {
						tempSelectedCount++;
					}
				}
			}
			
			/**
			 * Add the ratio to the ratio list.
			 */
			ratioList.add(tempSelectedCount / tempTurnCount);
		}

		/**
		 * Calculate who is to drive based off the lowest ratio.
		 */
		Integer index = 0;								// Index of the currently chosen user
		Double tempCurrentRatio = ratioList.get(0);		// Sets the first ratio to the first user in the list
		Double tempRatio;								// Storage of a user's ratio to compare to the currentratio
		Integer sameCounter = 0;						// Counter to check if all the users in the turn have the same ratio.

		/**
		 * Calculate what user in the turn has the lowest ratio.
		 */
		for (int i = 1; i < ratioList.size(); i++) {
			tempRatio = ratioList.get(i);				// Gets the ratio of the user from the ratioList

			/**
			 * Checks if the ratio of this user is lower than the current lowest user.
			 * If so, set the currentRatio to this user's ratio, and set the index to this user.
			 */
			if (tempRatio < tempCurrentRatio) {
				tempCurrentRatio = tempRatio;
				index = i;
			}
			
			/**
			 * Increases the counter if the current user has the same ratio as the first user
			 */
			if (tempRatio.equals(tempCurrentRatio)) {
				sameCounter++;
			}
		}

		/**
		 * If all the users have the same ratio, return a random user.
		 */
		if (sameCounter.equals(users.size())) {
			return chooseRandomUser(users);
		}
		
		/**
		 * Return the user based off the index found
		 */
		return users.get(index);
	}

	/**
	 * Algorithm which chooses a user based explicitly on turnDateTime from
	 * Turn.java, the user which has the oldest date will be selected to drive.
	 * Elementary comparisons between a default currentTurnDate and a tempTurnDate,
	 * will be used to determine which of the users has the earliest of Dates once
	 * the for loop terminates
	 * 
	 * @param users
	 *            list of users, as well as a category are passed into the
	 *            leastRecentlyGone method to handle and retrieve the amount of
	 *            times the user participated in such a category.
	 * @param category
	 *            will represent a drive, chips & salsa, or ice cream
	 * @return a User, which then will be used to access the a string
	 *         representing the user name
	 */
	public edu.unlv.cs.whoseturn.domain.User leastRecentlyGone(
			List<edu.unlv.cs.whoseturn.domain.User> users, Category category) {
		List<String> turnItemsKeyStrings;							// Temporary storage of a user's turnitems keystrings
		List<TurnItem> turnItems;									// Temporary storage of a user's turnitem objects
		List<Long> millisecondsList = new ArrayList<Long>();		// Storage of the milliseconds of the difference between now and the user's last turn
		
		/**
		 * Persistence manager
		 */
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Turn tempTurn;				// Temporary storage of the turn retrieved
		Date today = new Date();	// Stores today's date
		Long tempMilliSeconds;		// Temporary storage of difference between now and the user's last turn

		/**
		 * Generates the user's difference between now and the last turn
		 * and stores it into the millisecondsList
		 */
		for (int i = 0; i < users.size(); i++) {
			turnItemsKeyStrings = new ArrayList<String>(users.get(i).getTurnItems());	// Gets the user's list of turnitems keystrings
			turnItems = new ArrayList<TurnItem>();										// Startsa  new list for turnitems
			
			/**
			 * Gets the user's turnItems
			 */
			for (int j = 0; j < turnItemsKeyStrings.size(); j++) {
				turnItems.add(pm.getObjectById(TurnItem.class, KeyFactory
						.stringToKey(turnItemsKeyStrings.get(j))));
			}
			
			tempMilliSeconds = 9223372036854775807L;	// Stores the maxiumum long value as the default milliseconds of last turn
			
			/**
			 * Finds the last date that the user attended in this category
			 * and stores the milliseconds between today and then.
			 */
			for (int k = 0; k < turnItems.size(); k++) {
				
				/**
				 * Check if the turn is part of the specified category.
				 */
				if (turnItems.get(k).getCategoryKeyString().equals(category.getKeyString())) {
					tempTurn = pm.getObjectById(Turn.class, KeyFactory									// Stores the turn from this turnitem
							.stringToKey(turnItems.get(k).getTurnKeyString()));
					
					/**
					 * Check if this turn's date is closer than the current turn.
					 * If so, store this new milliseconds difference.
					 */
					if((today.getTime() - tempTurn.getTurnDateTime().getTime()) < tempMilliSeconds) {
						tempMilliSeconds = today.getTime() - tempTurn.getTurnDateTime().getTime();
					}
				}
			}
			
			/**
			 * Adds the calculated milliseconds to the list.
			 */
			millisecondsList.add(tempMilliSeconds);
		}

		/**
		 * Closes the persistence manager.
		 */
		pm.close();

		/**
		 * Finds the highest value in the milliseconds list.
		 */
		Integer index = 0;											// Set the index inlitally to the first user in the list
		Long tempCurrentMilliSeconds = millisecondsList.get(0);		// Set the currentMilliseconds to the first user in the list
		Integer sameCounter = 0;									// Set the samecounter to 0
		
		/**
		 * Loops through the milliseconds list to find the highest value.
		 */
		for (int i = 1; i < millisecondsList.size(); i++) {
			tempMilliSeconds = millisecondsList.get(i);			// Stores the user's milliseconds for comparison

			/**
			 * Check if this user's milliseconds is more than the current user's
			 * If so, store this as the new highest found.
			 */
			if (tempMilliSeconds > tempCurrentMilliSeconds) {
				tempCurrentMilliSeconds = tempMilliSeconds;
				index = i;
			}
			
			/**
			 * Checks to see if everyone has the same milliseconds.
			 * If so, increase the sameCounter.
			 */
			if (tempMilliSeconds.equals(tempCurrentMilliSeconds)) {
				sameCounter++;
			}
		}
		
		/**
		 * If all users have the same milliseconds, choose a random user instead.
		 */
		if (sameCounter.equals(millisecondsList.size())) {
			return chooseRandomUser(users);
		}

		/**
		 * Return the user who is chosen.
		 */
		return users.get(index);
	}

	/**
	 * Algorithm which chooses a user based explicitly on the predefined random
	 * generator The Random object will use the nextInt() method to generate an
	 * integer value, which given a parameter of the the number of users will
	 * choose a number in such 0 to n-1 range
	 * 
	 * @param users The list of usernames.
	 * @return a User at the arbitrarily generated index, which then will be
	 *         used to access a string representing the user name
	 */
	public edu.unlv.cs.whoseturn.domain.User chooseRandomUser(
			List<edu.unlv.cs.whoseturn.domain.User> users) {
		
		Random generator = new Random();					// Creates a new random generator
		int randomIndex = generator.nextInt(users.size());	// Gets a random index value with a max of the size of the users

		/**
		 * Return the random user.
		 */
		return users.get(randomIndex);
	}

	@SuppressWarnings("unchecked")
	public List<edu.unlv.cs.whoseturn.domain.User> getUserObjects(
			List<String> usernames) {
		
		/**
		 * Persistence manager
		 */
		PersistenceManager pm = PMF.get().getPersistenceManager();

		/**
		 * Get the user objects of the users in the username list
		 */
		Query userQuery = pm.newQuery(edu.unlv.cs.whoseturn.domain.User.class,
				"username == usernameParam");
		userQuery.declareParameters("String usernameParam");

		List<edu.unlv.cs.whoseturn.domain.User> userList = new ArrayList<edu.unlv.cs.whoseturn.domain.User>();
		List<edu.unlv.cs.whoseturn.domain.User> tempUserList = new ArrayList<edu.unlv.cs.whoseturn.domain.User>();

		for (int i = 0; i < usernames.size(); i++) {
			tempUserList = (List<edu.unlv.cs.whoseturn.domain.User>) userQuery
					.execute(usernames.get(i));
			userList.add(tempUserList.get(0));
		}
		userList.size();
		userQuery.closeAll();
		pm.close();
		return userList;
	}

	/**
	 * Gets the object of the category based off the categoryName.
	 * @param categoryName	Name of the category.
	 * @return	Returns the category object.
	 */
	@SuppressWarnings("unchecked")
	public Category getCategoryObject(String categoryName) {
		
		/**
		 * Persistence manager
		 */
		PersistenceManager pm = PMF.get().getPersistenceManager();

		/**
		 * Get the category object
		 */
		Query categoryQuery = pm.newQuery(Category.class,					// Generates a query for the category based off the categoryNameParam
				"name == categoryNameParam");
		categoryQuery.declareParameters("String categoryNameParam");		// Adds the parameter to the query

		List<Category> categoryList = (List<Category>) categoryQuery		// Gets the list of categories that match the query (Should only be one)
				.execute(categoryName);
		Category category = categoryList.get(0);							// Stores the category object

		/**
		 * Close the persistence manager.
		 */
		categoryQuery.closeAll();
		pm.close();
		
		/**
		 * Return the category object.
		 */
		return category;
	}

	/**
	 * Adds the currently logged in user to the list.
	 * 
	 * @param usernames Usernames of the users currently in the turn.
	 * @return	Returns the updated username list.
	 */
	@SuppressWarnings("unchecked")
	public List<String> addLoggedUser(List<String> usernames) {
		/**
		 * User auth service.
		 */
		UserService userService = UserServiceFactory.getUserService();
		
		/**
		 * Logged in user.
		 */
		User user = userService.getCurrentUser();

		/**
		 * Persistence manager
		 */
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		/**
		 * Finds the user's username who is logged in based off their OpenID email.
		 */
		Query loggedUserQuery = pm.newQuery(										// Generates a query for users whose email is emailParam
				edu.unlv.cs.whoseturn.domain.User.class, "email == emailParam");
		loggedUserQuery.declareParameters("String emailParam");						// Adds the paramter to the query
		
		// Get a list of users who meet the query. (Should only be one)
		List<edu.unlv.cs.whoseturn.domain.User> loggedUserList = (List<edu.unlv.cs.whoseturn.domain.User>) loggedUserQuery
				.execute(user.getEmail());
		usernames.add(loggedUserList.get(0).getUsername());		// Add the user to the list
		usernames.size();										// Object Manager bug fix
		
		/**
		 * Closes the persistence manager.
		 */
		loggedUserQuery.closeAll();
		pm.close();
		
		/**
		 * Returns their updated username list.
		 */
		return usernames;
	}
}
