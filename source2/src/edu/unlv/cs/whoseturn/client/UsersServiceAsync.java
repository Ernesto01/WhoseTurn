package edu.unlv.cs.whoseturn.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Interface for the User Service, async version.
 */
public interface UsersServiceAsync {

    /**
     * Add a new user to the database. Async version.
     * 
     * @param username
     *            The user to add.
     * @param email
     *            The user's email address. If they are a guest, this is blank.
     * @param admin
     *            Boolean of if they are an admin or not. True for admin status.
     * @param callback The async callback.
     * @throws IllegalArgumentException
     */
    void addNewUser(String username, String email, Boolean admin,
            AsyncCallback<String> callback) throws IllegalArgumentException;

    /**
     * Get a list of all non deleted users.
     * 
     * @param callback The asynch callback.
     * @throws IllegalArgumentException
     */
    void findNonDeletedUsers(AsyncCallback<List<String>> callback)
            throws IllegalArgumentException;
    
    /**
     * Get a list of all users, deleted or not.
     * 
     * @param callback The asynch callback.
     * @throws IllegalArgumentException
     */
    void findAllUsers(AsyncCallback<List<String>> callback)
            throws IllegalArgumentException;

    /**
     * Get a list of all users. Async version.
     * 
     * @param callback The async callback.
     * @throws IllegalArgumentException
     */
    void findUsers(AsyncCallback<List<String[]>> callback)
            throws IllegalArgumentException;

    /**
     * Get the login url. Async version.
     * 
     * @param providerName
     *            Name of provider, e.g. gmail.
     * @param location
     *            The location to use.
     * @param callback The async callback.
     * @throws IllegalArgumentException
     */
    void getLoginURL(String providerName, String location,
            AsyncCallback<String> callback) throws IllegalArgumentException;

    /**
     * Get the logout url. Async version.
     * 
     * @param location
     *            The location to use.
     * @param callback The async callback.
     * @throws IllegalArgumentException
     */
    void getLogoutURL(String location, AsyncCallback<String> callback)
            throws IllegalArgumentException;

    /**
     * Get the users name. Async version.
     * 
     * @param callback The async callback.
     * @throws IllegalArgumentException
     */
    void getUsername(AsyncCallback<String> callback)
            throws IllegalArgumentException;

    /**
     * Used to initialize the database with some information to be used. Async
     * version.
     * 
     * @param callback The async callback.
     * @throws IllegalArgumentException
     */
    void initializeServer(AsyncCallback<Void> callback)
            throws IllegalArgumentException;

    /**
     * Checks to see if user is logged in. Async version.
     * 
     * @param callback The async callback.
     * @throws IllegalArgumentException
     */
    void isLoggedIn(AsyncCallback<Boolean> callback)
            throws IllegalArgumentException;

    /**
     * Checks if the current users is an admin.
     * 
     * @param callback The callback.
     */
    void isAdmin(AsyncCallback<Boolean> callback);

    /**
     * Gets the user's information. A list of zero or one that has a string[] with the information. {username, email, admin, deleted}
     * 
     * @param usermame The user name.
     * @param callback Async callback.
     */
    void getUserInfo(String usermame, AsyncCallback<List<String[]>> callback);

    /**
     * Update a user in the database.
     * 
     * @param previousUsername Previous username.
     * @param previousEmail Previous email.
     * @param userName The User's name to update.
     * @param email The user's email.
     * @param admin Admin state.
     * @param deleted Deleted state.
     */
    void updateUser(String previousUsername, String previousEmail, String userName, String email, Boolean admin, Boolean deleted, AsyncCallback<String> callback);

}
