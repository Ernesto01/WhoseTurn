package edu.unlv.cs.whoseturn.client.views;

/**
 * Status bar interface. Used to specify accessors for what can be displayed in
 * the status bar.
 */
public interface StatusBar extends View {
    /**
     * Status getter.
     * 
     * @return status as a string.
     */
    String getStatus();

    /**
     * Status setter.
     * 
     * @param status
     *            as a string.
     */
    void setStatus(String status);

}
