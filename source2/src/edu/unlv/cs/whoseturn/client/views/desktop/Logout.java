package edu.unlv.cs.whoseturn.client.views.desktop;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.unlv.cs.whoseturn.client.views.AbstractNavigationView;
import edu.unlv.cs.whoseturn.client.views.NavigationView;

/**
 * View that allows a user to logout.
 */
public class Logout extends AbstractNavigationView implements NavigationView {

    /**
     * @wbp.parser.entryPoint
     */
    @Override
    public final Widget bodyAsWidget() {
        final AbsolutePanel loginPanel = new AbsolutePanel();
        loginPanel.setSize("1000px", "500px");

        // TODO Allow users to logout, which should take them back to the login
        // screen.
        final Label lblNewLabel = new Label("Placeholder for logout.");
        loginPanel.add(lblNewLabel);

        return loginPanel;
    }
}
