package edu.unlv.cs.whoseturn.client.views;

import com.google.gwt.user.client.ui.Widget;

/**
 * Interface for Navigation View. This view will show both the navigation bar at
 * the top, plus the body view.
 */
public interface NavigationView extends View {
    /**
     * Main portion of the view. This is what is unique about view.
     * 
     * @return
     */
    Widget bodyAsWidget();
}
