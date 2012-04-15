/*
 * This class keeps a record of a user's history based on category
 * This class needs to be stored in the datastore for persistence
 */
package com.camilo.fedId;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;



public class Record {
	
	public static Entity createOrUpdateRecord(String userName, String category, java.lang.Long timesGone, java.lang.Long timesUsed) {
		String recordName = userName + category;
		Entity user = User.getUser(userName);
		Entity record = getSingleRecord(recordName);
		
		if(record == null) {
			record = new Entity("Record", user.getKey());
			record.setProperty("name", recordName);
			record.setProperty("user", userName);
			record.setProperty("timesGone", timesGone);
			record.setProperty("timesUsed", timesUsed);
			record.setProperty("participationDate", new Date());
		}
		else {
			record.setProperty("timesGone", timesGone);
			record.setProperty("timesUsed", timesUsed);
			record.setProperty("participationDate", new Date());
		}
		Util.persistEntity(record);
		return record;
	}
	
	// delete record corresponding to given record name
	public static void deleteRecord(String recordKey) {
		Entity entity = getSingleRecord(recordKey);
	
		if(entity != null) {
			Util.deleteEntity(entity.getKey());
		}
	}
	
	// Delete record corresponding to record key
	public static void deleteRecord(Key recordKey) {
		Util.deleteEntity(recordKey);
	}
	
	// get record by its record name
	public static Iterable<Entity> getRecord(String recordName) {
		Iterable<Entity> entities = Util.listEntities("Record", "name", recordName);
		return entities;
	}
	
	// Get a single record using the record name
	public static Entity getSingleRecord(String recordName) {
		Query query = new Query("Record");
		query.addFilter("name", FilterOperator.EQUAL, recordName);
		List<Entity> results = Util.getDatastoreServiceInstance().prepare(query).asList(FetchOptions.Builder.withDefaults());
		if(!results.isEmpty())
			return (Entity)results.remove(0);
		return null;
	}
	
	// Get all records for given user
	public static Iterable<Entity> getRecordsForUser(String userName) {
		Key ancestorKey = KeyFactory.createKey("User", userName);
		return Util.listChildren("Record", ancestorKey);
	}
	
	// Increment times gone
	// returns true if successful in incrementing, false otherwise
	public static boolean incrementTimesGone(String recordName) {
		Entity record = getSingleRecord(recordName);
		if(record != null) {
			record.setProperty("timesGone", (java.lang.Long)record.getProperty("timesGone") + 1);
			return true;
		}
		return false;
	}
	
	// Increment times used
	// returns true if successful in incrementing, false otherwise
	public static boolean incrementTimesUsed(String recordName) {
		Entity record = getSingleRecord(recordName);
		if(record != null) {
			record.setProperty("timesUsed", 3);
			
			return true;
		}
		return false;
	}
	
	public static Entity incrementTimesUsed(String userName, String category) {
		Entity record = getSingleRecord(userName + category);
		
		if(record != null) {
			record = createOrUpdateRecord(userName, category, (java.lang.Long)record.getProperty("timesGone"), 
					(java.lang.Long) record.getProperty("timesUsed") + 1);
			return record;
		}
		return null;
	}

}
