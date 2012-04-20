package edu.unlv.cs.whoseturn.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface UsersServiceAsync {
	void usersServer(AsyncCallback<String> callback)
			throws IllegalArgumentException;
	
	void isLoggedIn(AsyncCallback<Boolean> callback)
			throws IllegalArgumentException;
	
	void getLoginURL(String providerName, String location, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	
	void getUsername(AsyncCallback<String> callback)
			throws IllegalArgumentException;
	
	void getLogoutURL(String location, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	
	void addTestUser(String username, String email, Boolean admin, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	
	void findUsers(AsyncCallback<List<String[]>> callback)
			throws IllegalArgumentException;
	
	void findUserByKey(String key, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	
	void getUsernames(AsyncCallback<List<String>> callback) 
			throws IllegalArgumentException;
}