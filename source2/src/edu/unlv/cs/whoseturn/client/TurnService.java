package edu.unlv.cs.whoseturn.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("turn")
public interface TurnService extends RemoteService {
	/**
	 * Used to find a driver based off the supplied category
	 * 
	 * @param usernames Usernames of the users attending the turn. (excluding the user initiating the turn)
	 * @param category Category name to base the turn off of
	 * @return Returns data to be used with the badge service and client layer
	 * 			@0	Username of the chosen user
	 * 			@1	Keystring of the turn
	 * @throws IllegalArgumentException
	 */
	List<String> findDriver(List<String> usernames, String category) throws IllegalArgumentException;
}
