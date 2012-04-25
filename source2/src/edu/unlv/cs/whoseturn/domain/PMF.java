package edu.unlv.cs.whoseturn.domain;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

/**
 * The Persistence Manager Factory. Singleton pattern.
 */
public final class PMF {

    /**
     * The PMF.
     */
    private static final PersistenceManagerFactory pmfInstance = JDOHelper
            .getPersistenceManagerFactory("transactions-optional");

    /**
     * Default constructor, needed for singleton pattern.
     */
    private PMF() {
    }

    /**
     * Handle to the factory.
     * 
     * @return handle our the factory.
     */
    public static PersistenceManagerFactory get() {
        return pmfInstance;
    }
}
