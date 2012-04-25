package edu.unlv.cs.whoseturn.client;

import java.util.List;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Interface for the Category Service, async version.
 */
public interface CategoryServiceAsync {

    /**
     * Used to add a Category to the database. Async Version.
     * 
     * @param category
     *            Category name.
     * @param strategy
     *            Picking strategy to use for whose turn.
     * @param timeBoundary
     *            Integer time boundary for how often the turn can occur.
     * @param async the aysnc callback.
     */
    void addCategory(String category, String strategy, Integer timeBoundary,
            AsyncCallback<String> async);

    /**
     * Allows for a soft delete of a category. Async Version.
     * 
     * @param category
     *            The category name do do a soft delete on.
     * @param async The async callback.
     */
    void removeCategory(String category, AsyncCallback<String> async);

    /**
     * Return all category names. Async Version.
     * 
     * @param async The async callback.
     */
    void getAllCategories(AsyncCallback<List<String>> async);

    /**
     * A way to help populate the database with some useful info, for testing,
     * debugging, etc. Async Version.
     * 
     * @param async The async callback.
     */
    void loadInitialCategories(AsyncCallback<Void> async);

    /**
     * Returns a list of all strategies in the system. Useful for when adding a
     * new category. Async Version.
     * 
     * @param async The async callback.
     */
    void getAllStrategies(AsyncCallback<List<String>> async);
}
