package edu.unlv.cs.whoseturn.server;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.unlv.cs.whoseturn.client.UsersService;
import edu.unlv.cs.whoseturn.domain.Badge;
import edu.unlv.cs.whoseturn.domain.BadgeAwarded;
import edu.unlv.cs.whoseturn.domain.Category;
import edu.unlv.cs.whoseturn.domain.PMF;
import edu.unlv.cs.whoseturn.domain.Strategy;
import edu.unlv.cs.whoseturn.domain.Turn;
import edu.unlv.cs.whoseturn.domain.TurnItem;
import edu.unlv.cs.whoseturn.shared.EntryVerifier;

/**
 * User service that allows the client to CRUD information about users.
 */
@SuppressWarnings("serial")
public class UsersServiceImpl extends RemoteServiceServlet implements
        UsersService  {

    /**
     * Used to keep track of open id providers.
     */
    private static final Map<String, String> openIdProviders;

    /**
     * Static block, used to populate our open id provider list.
     */
    static {
        openIdProviders = new HashMap<String, String>();
        openIdProviders.put("google", "www.google.com/accounts/o8/id");
        openIdProviders.put("yahoo", "yahoo.com");
        openIdProviders.put("myspace", "myspace.com");
        openIdProviders.put("aol", "aol.com");
        openIdProviders.put("myopenid", "myopenid.com");
    }

    @Override
    public final Boolean isLoggedIn() {
        UserService userService = UserServiceFactory.getUserService();

        return userService.isUserLoggedIn();
    }

    @SuppressWarnings("unchecked")
    @Override
    public final String getUsername() {
        /**
         * User auth service.
         */
        UserService userService = UserServiceFactory.getUserService();
        
        /**
         * Logged in user.
         */
        User user = userService.getCurrentUser();

        // Ensure the user is logged in
        if (user == null) {
            return "UserNotLoggedIn";
        }

        /**
         * Persistence manager, used for CRUD.
         */
        PersistenceManager pm = PMF.get().getPersistenceManager();

        /**
         * Query to find the user based off the email
         */
        Query query = pm.newQuery(edu.unlv.cs.whoseturn.domain.User.class,
                "email == emailParam");

        /**
         * Parameter for search.
         */
        query.declareParameters("String emailParam");

        /**
         * Execute the query with the email parameter
         */
        List<edu.unlv.cs.whoseturn.domain.User> results = (List<edu.unlv.cs.whoseturn.domain.User>) query
                .execute(user.getEmail());

        // Check to make sure only one user was found and return the username
        if (results.size() == 1) {
            return results.get(0).getUsername();
        }
        if (results.size() == 0) {
            return "UserNotFound";
        }

        // TODO, should we change this to throw illegal states instead?
        // Something went wrong if we're down here
        return "ErrorFindingUser";
    }

    @SuppressWarnings("unchecked")
    @Override
    public final boolean isAdmin() {
        /**
         * User auth service.
         */
        UserService userService = UserServiceFactory.getUserService();

        /**
         * Logged in user.
         */
        User user = userService.getCurrentUser();

        // Ensure the user is logged in
        if (user == null) {
            return false;
        }

        /**
         * Persistence manager, used for CRUD.
         */
        PersistenceManager pm = PMF.get().getPersistenceManager();

        /**
         * Query to find the user based off the email
         */
        Query query = pm.newQuery(edu.unlv.cs.whoseturn.domain.User.class,
                "email == emailParam");

        /**
         * Parameter for search.
         */
        query.declareParameters("String emailParam");

        /**
         * Execute the query with the email parameter
         */
        List<edu.unlv.cs.whoseturn.domain.User> results = (List<edu.unlv.cs.whoseturn.domain.User>) query
                .execute(user.getEmail());

        // Check to make sure only one user was found and return the username
        if (results.size() == 1) {
            return results.get(0).getAdmin();
        }

        return false;
    }

    @Override
    public final String getLoginURL(final String providerName,
            final String location) {
        UserService userService = UserServiceFactory.getUserService();
        String providerUrl = openIdProviders.get(providerName);
        return userService.createLoginURL(location, null, providerUrl, null);
    }

    @Override
    public final String getLogoutURL(final String location) {
        UserService userService = UserServiceFactory.getUserService();

        return userService.createLogoutURL(location, userService
                .getCurrentUser().getAuthDomain());
    }

    @SuppressWarnings("unchecked")
    @Override
    public final String addNewUser(final String username, final String email,
            final Boolean admin) {
        // Get rid of any leading and trailing whitespace in the username and
        // email address
        username.trim();
        email.trim();

        /**
         * The error message to be displayed when the username or email is
         * invalid.
         */
        String errorMessage;

        // A Valid username will return "Valid"
        // An invalid username will return "Invalid username"
        // A duplicate username will return "Username already exists"
        errorMessage = EntryVerifier.isUsernameValid(username);

        // If the username isn't "Valid", there was an error so return
        if (errorMessage != "Valid") {
            return errorMessage;
        }

        // A valid email will return "Valid"
        // An invalid email will return "Invalid e-mail address"
        // A duplicate email will return "E-mail address already exists."
        errorMessage = EntryVerifier.isEmailValid(email);

        // If the email address isn't "Valid", there was an error so return
        if (errorMessage != "Valid") {
            return errorMessage;
        }

        /**
         * The persistence manager.
         */
        PersistenceManager pm = PMF.get().getPersistenceManager();

        /**
         * New user object to add.
         */
        edu.unlv.cs.whoseturn.domain.User user = new edu.unlv.cs.whoseturn.domain.User();

        // Set properties of the user
        user.setAdmin(admin);
        user.setAvatarBlob(null);
        user.setDeleted(false);
        user.setEmail(email);
        user.setUsername(username);
        user.setPenaltyCount(0);
        user.setBadges(new HashSet<String>());

        // Creation of the user's default badges
        /**
         * Query the database for all badge types
         */
        Query query = pm.newQuery(Badge.class);

        /**
         * A results list.
         */
        List<Badge> results;

        /**
         * A temporary badgeAwarded to be used for the user.
         */
        BadgeAwarded tempBadgeAwarded;

        // Execute the query and set the results
        results = (List<Badge>) query.execute();

        // Make sure badges were found
        if (!results.isEmpty()) {
            // Loop through all the badge types and create a BadgeAwarded for
            // this user with count set to 0
            for (Badge e : results) {
                tempBadgeAwarded = new BadgeAwarded();
                tempBadgeAwarded.setBadgeId(e.getBadgeId());
                tempBadgeAwarded.setCount(0);
                tempBadgeAwarded.setDeleted(false);
                pm.makePersistent(tempBadgeAwarded);
                user.addBadge(tempBadgeAwarded);
            }
        }

        // Persist the new user
        try {
            pm.makePersistent(user);
        } finally {
            query.closeAll();
            pm.close();
        }

        return "Success";
    }

    @SuppressWarnings("unchecked")
    @Override
    public final List<String[]> getUserInfo(final String usermame) {
        /**
         * Persistence manager for CRUD.
         */
        PersistenceManager pm = PMF.get().getPersistenceManager();

        /**
         * Query for the users.
         */
        Query query = pm.newQuery(edu.unlv.cs.whoseturn.domain.User.class);

        /**
         * Result string list.
         */
        List<String[]> resultStringList = new ArrayList<String[]>();

        /**
         * Result List.
         */
        List<edu.unlv.cs.whoseturn.domain.User> results;

        try {
            results = (List<edu.unlv.cs.whoseturn.domain.User>) query.execute();
            if (!results.isEmpty()) {
                for (edu.unlv.cs.whoseturn.domain.User e : results) {
                    if (e.getUsername().equals(usermame)) {
                        resultStringList.add(new String[] { e.getUsername(),
                                e.getEmail(), e.getAdmin().toString(),
                                e.getDeleted().toString() });
                    }
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

    @SuppressWarnings("unchecked")
    @Override
    public final List<String[]> findUsers() {
        /**
         * Persistence manager for CRUD.
         */
        PersistenceManager pm = PMF.get().getPersistenceManager();

        /**
         * Query for the users.
         */
        Query query = pm.newQuery(edu.unlv.cs.whoseturn.domain.User.class);

        /**
         * Result string list.
         */
        List<String[]> resultStringList = new ArrayList<String[]>();

        /**
         * Result List.
         */
        List<edu.unlv.cs.whoseturn.domain.User> results;

        try {
            results = (List<edu.unlv.cs.whoseturn.domain.User>) query.execute();
            if (!results.isEmpty()) {
                for (edu.unlv.cs.whoseturn.domain.User e : results) {
                    resultStringList.add(new String[] { e.getUsername(),
                            e.getEmail(), e.getAdmin().toString() });
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

    @SuppressWarnings("unchecked")
    @Override
    public final List<String> findNonDeletedUsers() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(edu.unlv.cs.whoseturn.domain.User.class,
                "deleted != true");

        List<String> resultStringList = new ArrayList<String>();
        List<edu.unlv.cs.whoseturn.domain.User> results;

        try {
            results = (List<edu.unlv.cs.whoseturn.domain.User>) query.execute();
            if (!results.isEmpty()) {
                for (edu.unlv.cs.whoseturn.domain.User e : results) {
                    resultStringList.add(e.getUsername());
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

    @SuppressWarnings("unchecked")
	@Override
    public final List<String> findAllUsers() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(edu.unlv.cs.whoseturn.domain.User.class);

        List<String> resultStringList = new ArrayList<String>();
        List<edu.unlv.cs.whoseturn.domain.User> results;

        try {
            results = (List<edu.unlv.cs.whoseturn.domain.User>) query.execute();
            if (!results.isEmpty()) {
                for (edu.unlv.cs.whoseturn.domain.User e : results) {
                    resultStringList.add(e.getUsername());
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

    @SuppressWarnings("unchecked")
    @Override
    public final void initializeServer() {
        /**
         * The persistence manager.
         */
        PersistenceManager pm = PMF.get().getPersistenceManager();
        
        // Wipe the database
        Query BadgeWipeQuery = pm.newQuery(Badge.class);
        List <Badge> BadgeWipeList = (List<Badge>)BadgeWipeQuery.execute();
        pm.deletePersistentAll(BadgeWipeList);
        
        Query BadgeAwardedWipeQuery = pm.newQuery(BadgeAwarded.class);
        List <BadgeAwarded> BadgeAwardedWipeList = (List<BadgeAwarded>)BadgeAwardedWipeQuery.execute();
        pm.deletePersistentAll(BadgeAwardedWipeList);
        
        Query CategoryWipeQuery = pm.newQuery(Category.class);
        List <Category> CategoryWipeList = (List<Category>)CategoryWipeQuery.execute();
        pm.deletePersistentAll(CategoryWipeList);
        
        Query StrategyWipeQuery = pm.newQuery(Strategy.class);
        List <Strategy> StrategyWipeList = (List<Strategy>)StrategyWipeQuery.execute();
        pm.deletePersistentAll(StrategyWipeList);
        
        Query TurnWipeQuery = pm.newQuery(Turn.class);
        List <Turn> TurnWipeList = (List<Turn>)TurnWipeQuery.execute();
        pm.deletePersistentAll(TurnWipeList);
        
        Query TurnItemWipeQuery = pm.newQuery(TurnItem.class);
        List <TurnItem> TurnItemWipeList = (List<TurnItem>)TurnItemWipeQuery.execute();
        pm.deletePersistentAll(TurnItemWipeList);
        
        Query UserWipeQuery = pm.newQuery(edu.unlv.cs.whoseturn.domain.User.class);
        List <edu.unlv.cs.whoseturn.domain.User> UserWipeList = (List<edu.unlv.cs.whoseturn.domain.User>)UserWipeQuery.execute();
        pm.deletePersistentAll(UserWipeList);
        
        // Create badges
        
        List<Badge> badgeList = new ArrayList<Badge>();
        
        Badge badge = new Badge();
        badge.setBadgeCriteria("User submitted a turn with only himself.");
        badge.setBadgeId(1000);
        badge.setBadgeName("Jackass");
        badge.setDeleted(false);
        badge = pm.makePersistent(badge);
        badgeList.add(badge);

        badge = new Badge();
        badge.setBadgeCriteria("Selected out of group of 4.");
        badge.setBadgeId(1001);
        badge.setBadgeName("Corner Stone");
        badge.setDeleted(false);
        badge = pm.makePersistent(badge);
        badgeList.add(badge);

        badge = new Badge();
        badge.setBadgeCriteria("Not selected out of a group of 4.");
        badge.setBadgeId(1002);
        badge.setBadgeName("Don't Cross The Streams.");
        badge.setDeleted(false);
        badge = pm.makePersistent(badge);
        badgeList.add(badge);
        
        badge = new Badge();
        badge.setBadgeCriteria("Selected out of a group of 5.");
        badge.setBadgeId(1003);
        badge.setBadgeName("Human Sacrifice");
        badge.setDeleted(false);
        badge = pm.makePersistent(badge);
        badgeList.add(badge);

        badge = new Badge();
        badge.setBadgeCriteria("Not selected out of a group of 5.");
        badge.setBadgeId(1004);
        badge.setBadgeName("Not The Thumb!");
        badge.setDeleted(false);
        badge = pm.makePersistent(badge);
        badgeList.add(badge);
        
        badge = new Badge();
        badge.setBadgeCriteria("Selected out of a group of 6.");
        badge.setBadgeId(1005);
        badge.setBadgeName("Six Minute Abs");
        badge.setDeleted(false);
        badge = pm.makePersistent(badge);
        badgeList.add(badge);
        
        badge = new Badge();
        badge.setBadgeCriteria("Not selected out of a group of 6.");
        badge.setBadgeId(1006);
        badge.setBadgeName("Pick Up Sticks");
        badge.setDeleted(false);
        badge = pm.makePersistent(badge);
        badgeList.add(badge);
        
        badge = new Badge();
        badge.setBadgeCriteria("Selected out of a group of 7.");
        badge.setBadgeId(1007);
        badge.setBadgeName("Crapped Out");
        badge.setDeleted(false);
        badge = pm.makePersistent(badge);
        badgeList.add(badge);
        
        badge = new Badge();
        badge.setBadgeCriteria("Not selected out of a group of 7.");
        badge.setBadgeId(1008);
        badge.setBadgeName("Lucky No. 7");
        badge.setDeleted(false);
        badge = pm.makePersistent(badge);
        badgeList.add(badge);
        
        badge = new Badge();
        badge.setBadgeCriteria("Selected out of a group of 8.");
        badge.setBadgeId(1009);
        badge.setBadgeName("Snow White");
        badge.setDeleted(false);
        badge = pm.makePersistent(badge);
        badgeList.add(badge);
        
        badge = new Badge();
        badge.setBadgeCriteria("Not selected out of a group of 8.");
        badge.setBadgeId(1010);
        badge.setBadgeName("Dwarf");
        badge.setDeleted(false);
        badge = pm.makePersistent(badge);
        badgeList.add(badge);
        
        badge = new Badge();
        badge.setBadgeCriteria("Selected out of a group of more than 8.");
        badge.setBadgeId(1011);
        badge.setBadgeName("FML");
        badge.setDeleted(false);
        badge = pm.makePersistent(badge);
        badgeList.add(badge);
        
        badge = new Badge();
        badge.setBadgeCriteria("Not selected out of a group of more than 8.");
        badge.setBadgeId(1012);
        badge.setBadgeName("Statisitcally Speaking");
        badge.setDeleted(false);
        badge = pm.makePersistent(badge);
        badgeList.add(badge);
        
        badge = new Badge();
        badge.setBadgeCriteria("User has no lies for 50 turns.");
        badge.setBadgeId(1013);
        badge.setBadgeName("Saint");
        badge.setDeleted(false);
        badge = pm.makePersistent(badge);
        badgeList.add(badge);
        
        badge = new Badge();
        badge.setBadgeCriteria("User is part of a turn with more than 10 people.");
        badge.setBadgeId(1014);
        badge.setBadgeName("Socialite");
        badge.setDeleted(false);
        badge = pm.makePersistent(badge);
        badgeList.add(badge);
        
        badge = new Badge();
        badge.setBadgeCriteria("User has participated in 10 turns.");
        badge.setBadgeId(1015);
        badge.setBadgeName("Rookie");
        badge.setDeleted(false);
        badge = pm.makePersistent(badge);
        badgeList.add(badge);
        
        badge = new Badge();
        badge.setBadgeCriteria("User has participated in 100 turns.");
        badge.setBadgeId(1016);
        badge.setBadgeName("Veteran");
        badge.setDeleted(false);
        badge = pm.makePersistent(badge);
        badgeList.add(badge);
        
        badge = new Badge();
        badge.setBadgeCriteria("User has participated in 250 turns.");
        badge.setBadgeId(1017);
        badge.setBadgeName("Elite");
        badge.setDeleted(false);
        badge = pm.makePersistent(badge);
        badgeList.add(badge);
        
        badge = new Badge();
        badge.setBadgeCriteria("User has every badge.");
        badge.setBadgeId(1018);
        badge.setBadgeName("Whose Turn Master");
        badge.setDeleted(false);
        badge = pm.makePersistent(badge);
        badgeList.add(badge);
        
        badge = new Badge();
        badge.setBadgeCriteria("Everyone in a turn was selected.");
        badge.setBadgeId(1019);
        badge.setBadgeName("Team Cheater");
        badge.setDeleted(false);
        badge = pm.makePersistent(badge);
        badgeList.add(badge);
        
        badge = new Badge();
        badge.setBadgeCriteria("User is Chris Jones.");
        badge.setBadgeId(1020);
        badge.setBadgeName("StormShadow");
        badge.setDeleted(false);
        badge = pm.makePersistent(badge);
        badgeList.add(badge);
        
        badge = new Badge();
        badge.setBadgeCriteria("User is Matthew Sowders");
        badge.setBadgeId(1021);
        badge.setBadgeName("MythBusters");
        badge.setDeleted(false);
        badge = pm.makePersistent(badge);
        badgeList.add(badge);

        // Create strategies & categories
        Strategy strategy = new Strategy();
        strategy.setDeleted(false);
        strategy.setStrategyName("Least Recently Gone");
        strategy.setStrategyId(1);
        strategy = pm.makePersistent(strategy);
        
        Category category = new Category();
        category.setDeleted(false);
        category.setName("BeerLRG");
        category.setStrategyKeyString(strategy.getKeyString());
        category.setTimeBoundaryInHours(12);
        category = pm.makePersistent(category);

        strategy = new Strategy();
        strategy.setDeleted(false);
        strategy.setStrategyName("Lowest Ratio");
        strategy.setStrategyId(2);
        strategy = pm.makePersistent(strategy);

        category = new Category();
        category.setDeleted(false);
        category.setName("ChipsLR");
        category.setStrategyKeyString(strategy.getKeyString());
        category.setTimeBoundaryInHours(12);
        category = pm.makePersistent(category);

        strategy = new Strategy();
        strategy.setDeleted(false);
        strategy.setStrategyName("Completely Random");
        strategy.setStrategyId(3);
        strategy = pm.makePersistent(strategy);
        
        category = new Category();
        category.setDeleted(false);
        category.setName("DriveCR");
        category.setStrategyKeyString(strategy.getKeyString());
        category.setTimeBoundaryInHours(12);
        category = pm.makePersistent(category);

        // Creates a new user object to add
        edu.unlv.cs.whoseturn.domain.User user = new edu.unlv.cs.whoseturn.domain.User();

        // Set properties of the user
        user.setAdmin(true);
        user.setAvatarBlob(null);
        user.setDeleted(false);
        user.setEmail("lombar40@unlv.nevada.edu");
        user.setUsername("Ryan Lombardo");
        user.setPenaltyCount(0);
        user.setBadges(new HashSet<String>());

        BadgeAwarded tempBadgeAwarded;

        // Make sure badges were found
        if (!badgeList.isEmpty()) {
            // Loop through all the badge types and create a BadgeAwarded for
            // this user with count set to 0
            for (Badge e : badgeList) {
                tempBadgeAwarded = new BadgeAwarded();
                tempBadgeAwarded.setBadgeId(e.getBadgeId());
                tempBadgeAwarded.setCount(0);
                tempBadgeAwarded.setDeleted(false);
                pm.makePersistent(tempBadgeAwarded);
                user.addBadge(tempBadgeAwarded);
            }
        }

        pm.makePersistent(user);

        user = new edu.unlv.cs.whoseturn.domain.User();

        // Set properties of the user
        user.setAdmin(true);
        user.setAvatarBlob(null);
        user.setDeleted(false);
        user.setEmail("test@example.com");
        user.setUsername("Test User");
        user.setPenaltyCount(0);
        user.setBadges(new HashSet<String>());

        // Make sure badges were found
        if (!badgeList.isEmpty()) {
            // Loop through all the badge types and create a BadgeAwarded for
            // this user with count set to 0
            for (Badge e : badgeList) {
                tempBadgeAwarded = new BadgeAwarded();
                tempBadgeAwarded.setBadgeId(e.getBadgeId());
                tempBadgeAwarded.setCount(0);
                tempBadgeAwarded.setDeleted(false);
                pm.makePersistent(tempBadgeAwarded);
                user.addBadge(tempBadgeAwarded);
            }
        }

        pm.makePersistent(user);

        pm.close();
    }

    @SuppressWarnings("unchecked")
	@Override
    public String updateUser(String previousUsername, String previousEmail, String username, String email, Boolean admin,
            Boolean deleted) {

        // Get rid of any leading and trailing whitespace in the username and
        // email address
        username.trim();
        email.trim();

        /**
         * The error message to be displayed when the username or email is
         * invalid.
         */
        String errorMessage;

        // A Valid username will return "Valid"
        // An invalid username will return "Invalid username"
        // A duplicate username will return "Username already exists"
        
        if(previousUsername.equals(username)){
        	errorMessage = "Valid";
        }else{
        	errorMessage = EntryVerifier.isUsernameValid(username);
        }
        	

        // If the username isn't "Valid", there was an error so return
        if (errorMessage != "Valid") {
            return errorMessage;
        }

        // A valid email will return "Valid"
        // An invalid email will return "Invalid e-mail address"
        // A duplicate email will return "E-mail address already exists."
        if(previousEmail.equals(email)){
        	errorMessage = "Valid";
        }else{
        	errorMessage = EntryVerifier.isEmailValid(email);
        }

        // If the email address isn't "Valid", there was an error so return
        if (errorMessage != "Valid") {
            return errorMessage;
        }

        /**
         * The persistence manager.
         */
        PersistenceManager pm = PMF.get().getPersistenceManager();
        
        /**
         * Query to find the user based off the email
         */
        Query query = pm.newQuery(edu.unlv.cs.whoseturn.domain.User.class,
                "username == usernameParam");

        /**
         * Parameter for search.
         */
        query.declareParameters("String usernameParam");

        /**
         * Execute the query with the email parameter
         */
        List<edu.unlv.cs.whoseturn.domain.User> resultList = (List<edu.unlv.cs.whoseturn.domain.User>) query
                .execute(previousUsername);

        /**
         * User object to update.
         */
        edu.unlv.cs.whoseturn.domain.User user = resultList.get(0);

        // Set properties of the user
        user.setAdmin(admin);
        user.setDeleted(deleted);
        user.setEmail(email);
        user.setUsername(username);

        // Persist the new user
        pm.close();

        return "Success";
    }
}
