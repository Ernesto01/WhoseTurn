package com.camilo.fedId;

import com.google.appengine.api.datastore.Entity;

public class ManageUsers {
	
	public static void addUser(String name, String description, boolean isAdmin) {
		User.createUser(name, description, isAdmin);
	}
	
	public static long getTimesGone(String userName, String category) {
		Entity record = Record.getSingleRecord(userName + category);
		if(record == null) {
			return -1;
		}
		return (java.lang.Long)record.getProperty("timesGone");
	}
	
	public static long getTimesUsed(String userName, String category) {
		Entity record = Record.getSingleRecord(userName + category);
		if(record == null) {
			return -1;
		}
		return (java.lang.Long)record.getProperty("timesUsed");
	}

}
