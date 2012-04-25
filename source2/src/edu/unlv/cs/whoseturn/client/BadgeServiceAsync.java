package edu.unlv.cs.whoseturn.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BadgeServiceAsync {

	/**
	 * Used to calculate badges. This method initiates the calculation of all badges.
	 * 
	 * @param turnKeyString The turn's keystring
	 * @param callback The async callback.
	 */
	void calculateBadges(String turnKeyString, AsyncCallback<Void> callback) throws IllegalArgumentException;

}