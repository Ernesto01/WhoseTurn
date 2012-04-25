package edu.unlv.cs.whoseturn.client;

import java.util.List;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TurnServiceAsync {
	/**
	 * Used to find a driver based off the supplied category
	 * 
	 * @param usernames Usernames of the users attending the turn. (excluding the user initiating the turn)
	 * @param category Category name to base the turn off of.
	 * @param callback The async callback.
	 * @return Returns data to be used with the badge service and client layer
	 * 			@0	Username of the chosen user
	 * 			@1	Keystring of the turn
	 * @throws IllegalArgumentException
	 */
	void findDriver(List<String> usernames, String categoryName, AsyncCallback<List<String>> callback) throws IllegalArgumentException;
}
