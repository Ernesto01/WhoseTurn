package edu.unlv.cs.whoseturn.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Interface for the Category Service.
 */
@RemoteServiceRelativePath("categories")
public interface CategoryService extends RemoteService {
    /**
     * Used to add a Category to the database.
     * 
     * @param category
     *            Category name.
     * @param strategy
     *            Picking strategy to use for whose turn.
     * @param timeBoundary
     *            Integer time boundary for how often the turn can occur.
     * @return The key for the category.
     */
    String addCategory(String category, String strategy, Integer timeBoundary);

    /**
     * Allows for a soft delete of a category.
     * 
     * @param category
     *            The category name do do a soft delete on.
     * @return "Success" if successful.
     */
    String removeCategory(String category);

    /**
     * Return all category names.
     * 
     * @return List of all category names.
     */
    List<String> getAllCategories();

    /**
     * A way to help populate the database with some useful info, for testing,
     * debugging, etc.
     */
    void loadInitialCategories();

    /**
     * Returns a list of all strategies in the system. Useful for when adding a
     * new category.
     * 
     * @return List of all strategies to select from.
     */
    List<String> getAllStrategies();
}
