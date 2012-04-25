package edu.unlv.cs.whoseturn.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


/**
 * Interface for the User Service.
 */
@RemoteServiceRelativePath("users")
public interface UsersService extends RemoteService {

    /**
     * Add a new user to the database.
     * 
     * @param username
     *            The user to add.
     * @param email
     *            The user's email address. If they are a guest, this is blank.
     * @param admin
     *            Boolean of if they are an admin or not. True for admin status.
     * @return Returns the user's key string (their id) or an error code.
     * @throws IllegalArgumentException
     */
    String addNewUser(String username, String email, Boolean admin)
            throws IllegalArgumentException;

    /**
     * Get a list of all guests.
     * 
     * @return A list of all the users as a string array.
     * @throws IllegalArgumentException
     */
    List<String> findNonDeletedUsers() throws IllegalArgumentException;
    
    /**
     * Get a list of all users, deleted or not.
     * 
     * @param callback The asynch callback.
     * @throws IllegalArgumentException
     */
    List<String> findAllUsers() throws IllegalArgumentException;

    /**
     * Get a list of all users.
     * 
     * @return A list of all the users as a string array.
     * @throws IllegalArgumentException
     */
    List<String[]> findUsers() throws IllegalArgumentException;

    /**
     * Get the login url.
     * 
     * @param providerName
     *            Name of provider, e.g. gmail.
     * @param location
     *            The location to use.
     * @return The login url as a string.
     * @throws IllegalArgumentException
     */
    String getLoginURL(String providerName, String location)
            throws IllegalArgumentException;

    /**
     * Get the logout url.
     * 
     * @param location
     *            The location to use.
     * @return The logout url as a string.
     * @throws IllegalArgumentException
     */
    String getLogoutURL(String location) throws IllegalArgumentException;

    /**
     * Get the users name.
     * 
     * @return User's name as a string.
     * @throws IllegalArgumentException
     */
    String getUsername() throws IllegalArgumentException;

    /**
     * Used to initialize the database with some information to be used.
     * 
     * @throws IllegalArgumentException
     */
    void initializeServer() throws IllegalArgumentException;

    /**
     * Checks to see if user is logged in.
     * 
     * @return boolean value, true if they are logged in.
     * @throws IllegalArgumentException
     */
    Boolean isLoggedIn() throws IllegalArgumentException;

    /**
     * Checks to see if the current users is an admin.
     *  
     * @return true if admin.
     */
    boolean isAdmin();

    /**
     * Gets the information associated with the user with the specified username.
     * 
     * @param usermame The username.
     * @return A list of zero or one that has a string[] with the information. {username, email, admin, deleted} 
     */
    List<String[]> getUserInfo(String usermame);

    /**
     * Update a user in the database.
     * 
     * @param previousUsername The previous username.
     * @param previousEmail The previous email.
     * @param userName The User's name to update.
     * @param email The user's email.
     * @param admin Admin state.
     * @param deleted Deleted state.
     * @return Result status. Success for successful.
     */
    String updateUser(String previousUsername, String previousEmail,  String userName, String email, Boolean admin,
            Boolean deleted);
}
