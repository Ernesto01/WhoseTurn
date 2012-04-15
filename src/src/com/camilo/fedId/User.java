package com.camilo.fedId;

import java.util.List;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

public class User {
	// Create user entity and add it to the datastore for persistence
	public static void createUser(String name) {
		Entity user = getUser(name);
		if(user == null) {
			user = new Entity("User", name);
			
		}
		Util.persistEntity(user);
	}
	
	public static Entity getUser(String name) {
		Key key = KeyFactory.createKey("User", name);
		return Util.findEntity(key);
	}
	
	void changePassword() {
		
	}
	
	public static List<Entity> getRecords(String name) {
		Query query = new Query();
		Key parentKey = KeyFactory.createKey("User", name);
		query.setAncestor(parentKey);
		query.addFilter(Entity.KEY_RESERVED_PROPERTY, Query.FilterOperator.GREATER_THAN, parentKey);
		List<Entity> results = Util.getDatastoreServiceInstance().prepare(query).asList(FetchOptions.Builder.withDefaults());
		return results;
	}
	
	// Delete user and all records associated with the user
	public static void deleteUser(String userKey) {
		Key key = KeyFactory.createKey("User", userKey);
		
		List<Entity> records = getRecords(userKey);
		for(Entity e: records) {
			Record.deleteRecord(e.getKey());
		}
		
		Util.deleteEntity(key);
	}
	
	/*
	public int getTimesGone(String userName, String category) {
		Record record = getRecord(userName);
		return record.getTimesGone(category);
	}
	
	public int getTimesUsed(String userName, String category) {
		Record record = getRecord(userName);
		return record.getTimesUsed(category);
	}
	
	public void increaseTimesGone(String userName, String category) {
		Record record = getRecord(userName);
		record.increaseTimesGone(category);
	}
	
	public void increaseTimesUsed(String userName, String category) {
		Record record = getRecord(userName);
		record.increaseTimesUsed(category);
	}
	
	// Helper Functions
	Record getRecord(String userName) {
		Entity entity = getUser(userName);
		return (Record) entity.getProperty("record");
	}
	*/
}
