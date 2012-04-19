/*
 * This class keeps a record of a user's history based on category
 * This class needs to be stored in the datastore for persistence
 */
package com.camilo.fedId;


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
	
	// Get all records
	public static Iterable<Entity> getAllRecords() {
		Iterable<Entity> entities = Util.listEntities("Record", null, null);
		return entities;
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
	// returns record Entity if successful, null otherwise
	public static Entity incrementTimesGone(String userName, String category) {
		Entity record = getSingleRecord(userName+category);
		if(record != null) {
			record = createOrUpdateRecord(userName, category, (java.lang.Long)record.getProperty("timesGone")+1,
					(java.lang.Long)record.getProperty("timeUsed"));
			return record;
		}
		return null;
	}
	
	// Increment times used
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
