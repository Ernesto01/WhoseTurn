package edu.unlv.cs.whoseturn.client.views;

import com.google.gwt.user.client.ui.Widget;

/**
 * Interface for view. Allows for view to returned as a panel widget.
 */
public interface View {
    /**
     * Returns a widget, which can replace a panel display.
     * 
     * @return A panel as a widget.
     */
    Widget asWidget();

}
